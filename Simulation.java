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
	int blockNum;
	int lorryIndex;
	int currLorryIndex;
	int extractedTotal;
	PrintWriter output;
	int lastBatch;
	long startTime;
	
	long lorryWaitTime;
	
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
		System.out.println("lastBatch: " + lastBatch);
		for(int i = 0; i < workers.length; i++) {
			threadsWorker[i].start();
		}
	}

	public int getBLock() {
		blockNum++;
		return foreman.getBLock();
	}
	
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
	
	private void prepareNewLorries() {
		currLorryIndex = - 1;
		for(int i = 0; i < capFerry; i++) {
			lorryArray.add(i, new Lorry(this.capLorry, this.tLorry, this, lorryIndex, this.output));
			lorryIndex++;
		}
	}

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
