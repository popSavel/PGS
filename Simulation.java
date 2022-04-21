import java.io.PrintWriter;
import java.util.ArrayList;

public class Simulation {
	
	Foreman foreman;
	Ferry ferry;
	Worker [] workers;
	Thread [] threadsWorker;
	Thread [] lorryThread;
	Thread currLorryThread;
	ArrayList<Lorry> lorryArray = new ArrayList<Lorry>();
	Lorry currLorry;
	int capLorry;
	int tLorry;
	int capFerry;
	int extractedTotal;
	PrintWriter output;
	long startTime;
	long lorryWaitTime;
	Barrier barrier;
	Object changedExtracted = new Object();
	
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
		lorryThread = new Thread[lorryArray.size()];
		for(int i = 0; i < lorryArray.size(); i++) {
			lorryThread[i] = new Thread(lorryArray.get(i));
		}
		
		currLorryIndex = 0;
		currLorry = lorryArray.get(currLorryIndex);
		blockNum = 0;
		currLorryThread = lorryThread[0];
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
	 * synchronized, so workers dont get the same block
	 */
	synchronized public int getBLock() {
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
			currLorryThread.start();
			currLorry = lorryArray.get(currLorryIndex + 1);
			currLorry.waitTime = System.currentTimeMillis();
			currLorryIndex++;
			currLorryThread = lorryThread[currLorryIndex];
			}	
	}
	
	/**
	 * create capFerry lorries and add then on first capFerry indexes in lorryArray
	 */
	private void prepareNewLorries() {
		currLorryIndex = - 1;
		for(int i = 0; i < capFerry; i++) {
			lorryArray.add(i, new Lorry(this.capLorry, this.tLorry, this, lorryIndex, this.output));
			lorryThread[i] = new Thread(lorryArray.get(i));
			lorryIndex++;
		}
	}

	/**
	 * for all worker and lorry threads call join, so simulation waits for those threads to die
	 * this method also calls wait over Object changedExtracted, so it could not end before extracted resources count is equal to count of resources foreman found 
	 */
	public void waitUntilOver() {
		for(int i = 0; i < threadsWorker.length; i++) {
			try {
				threadsWorker[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		for(int i = 0; i < lorryThread.length; i++) {
			try {
				lorryThread[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	
		}
		synchronized(changedExtracted){
			while(this.extractedTotal != foreman.getSourceCount()) {
				try {
					changedExtracted.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * increase extractedTotal by resCount and notify Object changedExtracted
	 * is synchronized if two lorries called this at the same time, they could read the same number, but it is unlikely to happen
	 * @param resCount count of resources lorry, which called this method, carried
	 */
	synchronized public void addExtracted(int resCount) {
		this.extractedTotal += resCount;
		synchronized (changedExtracted){
			changedExtracted.notify();
		}
	}
	
	/**
	 * add next lorry to the ferry
	 */
	public void addLorry() {
		this.ferry.addLorry();
	}

	/**
	 * @return if foreman has next resource to be extracted
	 */
	public boolean hasNext() {
		return this.foreman.hasNext();
	}

}
