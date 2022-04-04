import java.io.PrintWriter;
import java.util.ArrayList;

public class Simulation {
	
	Foreman foreman;
	Ferry ferry;
	Worker [] workers;
	Thread [] threadsWorker;
	Thread lorryThread;
	ArrayList<Lorry> lorryArray = new ArrayList<Lorry>();
	Lorry currLorry;
	int capLorry;
	int tLorry;
	int capFerry;
	int extractedTotal;
	PrintWriter output;
	long startTime;
	long lorryWaitTime;
	
	/**
	 *  number of blocks assigned
	 */
	int blockNum;
	
	/**
	 * number of sources, last lorry will carry
	 */
	int lastBatch;
	
	/**
	 * index to identify Lorries
	 */
	int lorryIndex;
	
	/**
	 * index of currently filling lorry in lorryArray
	 */
	int currLorryIndex;
	
	public Simulation (Foreman foreman, Ferry ferry, Worker[] workers, Thread[] threadsWorker, int capLorry, int tLorry, int capFerry, PrintWriter output) {
		this.foreman = foreman;
		this.workers = workers;
		this.threadsWorker = threadsWorker;
		this.output = output;
		for(int i = 0; i < workers.length; i++) {
			workers[i].simulation = this;
		}
		this.capLorry = capLorry;
		this.tLorry = tLorry;
		this.capFerry = capFerry;
		this.ferry = ferry;
		lorryIndex = 1;
		for(int i = 0; i < capFerry; i++) {
			lorryArray.add(new Lorry(this.capLorry, this.tLorry, this, lorryIndex, this.output));
			lorryIndex++;
		}
		
		currLorryIndex = 0;
		currLorry = lorryArray.get(currLorryIndex);
		blockNum = 0;
		lorryThread = new Thread(lorryArray.get(0));
		extractedTotal = 0;
	}

	/**
	 * start the simulation
	 * set start time for workers, ferry and first lorry to current system time, calls loadData() from Foreman and start worker threads
	 */
	public void start() {
		long time = System.currentTimeMillis();
		this.ferry.waitTime = time;
		this.currLorry.waitTime = time;
		this.startTime = time;
		
		for(int i = 0; i < lorryArray.size(); i++) {
			lorryArray.get(i).waitTime = time;
		}
		
		foreman.loadData();
		lastBatch = foreman.getSourceCount() % capLorry;
		for(int i = 0; i < workers.length; i++) {
			threadsWorker[i].start();
		}
	}

	/**
	 * get block from foreman
	 */
	public int getBLock() {
		blockNum++;
		return foreman.getBLock();
	}
	
	/**
	 * loading one resource, waits for 10 ms, as the resource is getting loaded
	 * in case lorry is full, or it is last resource of last batch, send lorry to ferry and set currLorry to next lorry in lorryArray
	 * in case the lorry was last in lorryArray, create new lorries
	 * it is synchronized, so two workers can not load resource at one time
	 * @param worker who is loading the resource
	 */
	synchronized public void loadResource(Worker worker) {
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		currLorry.load();
		if(currLorry.isFull || (!foreman.hasNext() && currLorry.resCount == this.lastBatch)) {
			if(currLorryIndex == ferry.capFerry - 1) {
				prepareNewLorries();
			}
			lorryThread.start();
			currLorry = lorryArray.get(currLorryIndex + 1);
			currLorry.waitTime = System.currentTimeMillis();
			currLorryIndex++;
			lorryThread = new Thread(lorryArray.get(currLorryIndex));
			}	
	}
	
	/**
	 * create capFerry lorries and add then on first capFerry indexes in lorryArray
	 */
	private void prepareNewLorries() {
		currLorryIndex = - 1;
		for(int i = 0; i < capFerry; i++) {
			lorryArray.add(i, new Lorry(this.capLorry, this.tLorry, this, lorryIndex, this.output));
			lorryIndex++;
		}
	}

	/**
	 * check if any lorry or worker thread is alive and if total of extracted sources is equal to source count from foreman
	 * lorriesGoing boolean from ferry is there to secure the moment, when last ferry unload last lorries, their lorry threads exist, but have not been started yet
	 * @return true if simulation is completely over and statistics can be printed
	 */
	public boolean isOver() {
		for(int i = 0; i < threadsWorker.length; i++) {
			if(threadsWorker[i].isAlive()) {
				return false;
			}
		}
		if(lorryThread.isAlive()) {
			return false;
		}
		for(int i = 0; i < ferry.lorryThread.length; i++) {
			
			if(ferry.lorryThread[i] == null) {
				if(this.extractedTotal == foreman.getSourceCount()) {
					return true;
				}else {
					return false;
				}
			}else {
				if(ferry.lorriesGoing) {
					return false;
				}else {
					if(ferry.lorryThread[i].isAlive()) {
						return false;
					}	
				}
			}
		}
		return true;
	}

}
