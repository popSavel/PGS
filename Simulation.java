import java.util.ArrayList;

public class Simulation {
	
	Foreman foreman;
	Worker [] workers;
	Thread [] threads;
	ArrayList<Lorry> lorryArray = new ArrayList<Lorry>();
	int capLorry;
	int tLorry;
	int capFerry;
	int blockNum; 
	
	public Simulation (Foreman foreman, Worker[] workers, Thread[] threads, int capLorry, int tLorry, int capFerry) {
		this.foreman = foreman;
		this.workers = workers;
		this.threads = threads;
		for(int i = 0; i < workers.length; i++) {
			workers[i].simulation = this;
		}
		this.capLorry = capLorry;
		this.tLorry = tLorry;
		this.capFerry = capFerry;
		for(int i = 0; i < capFerry; i++) {
			lorryArray.add(new Lorry(this.capLorry, this.tLorry, this));
		}
		blockNum = 0;
	}

	public void start() {
		for(int i = 0; i < workers.length; i++) {
			//threads[i] = new Thread(workers[i]);
			threads[i].start();
		}
	}

	public int getBLock() {
		blockNum++;
		return foreman.getBLock();
	}

	public void loadResources(int resourceSize, Worker worker) {
		lorryArray.get(0).loadResources(resourceSize, worker);
	}

	public void prepareNew() {
		
	}

	public boolean isOver() {
		if(foreman.hasNext() && blockNum < 100) {
			return false;
		}else {
			return true;
		}
	}

}
