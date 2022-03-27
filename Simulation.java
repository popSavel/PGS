import java.util.ArrayList;

public class Simulation {
	
	Foreman foreman;
	Ferry ferry;
	Worker [] workers;
	Thread [] threadsWorker;
	Thread [] threadsLorry;
	ArrayList<Lorry> lorryArray = new ArrayList<Lorry>();
	Lorry currLorry;
	int capLorry;
	int tLorry;
	int capFerry;
	int blockNum;
	int lorryIndex;
	int currLorryIndex;
	
	public Simulation (Foreman foreman, Ferry ferry, Worker[] workers, Thread[] threadsWorker, int capLorry, int tLorry, int capFerry) {
		this.foreman = foreman;
		this.workers = workers;
		this.threadsWorker = threadsWorker;
		for(int i = 0; i < workers.length; i++) {
			workers[i].simulation = this;
		}
		this.capLorry = capLorry;
		this.tLorry = tLorry;
		this.capFerry = capFerry;
		this.ferry = ferry;
		lorryIndex = 1;
		threadsLorry = new Thread[capFerry];
		for(int i = 0; i < capFerry; i++) {
			lorryArray.add(new Lorry(this.capLorry, this.tLorry, this, lorryIndex));
			lorryIndex++;
			threadsLorry[i] = new Thread(lorryArray.get(i));
		}
		for(int i = 0; i < threadsLorry.length; i++) {
			threadsLorry[i] = new Thread(lorryArray.get(i));
		}
		currLorryIndex = 0;
		currLorry = lorryArray.get(currLorryIndex);
		blockNum = 0;
	}

	public void start() {
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("[" + Long.toString(System.currentTimeMillis()) + "] Worker " + worker.workerNum +" loaded one, lorry " + currLorry.lorryIndex+ " resources: " + currLorry.resCount);
		currLorry.load();
		if(currLorry.isFull) {
			threadsLorry[currLorryIndex].start();
			currLorry = lorryArray.get(currLorryIndex + 1);
			currLorryIndex++;
			ferry.addLorry();
		}	
	}
	
	public void loadResources(Worker worker, int resourcesSize) {
		for(int i = 0; i < resourcesSize; i++) {
			loadResource(worker);
		}
	}

	public boolean isOver() {
		if(foreman.hasNext() && blockNum < 1500) {
			return false;
		}else {
			return true;
		}
	}

}
