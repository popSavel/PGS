
public class Lorry implements Runnable{
	
	int capLorry;
	int resCount;
	int tLorry;
	Simulation simulation;

	public Lorry(int capLorry, int tLorry, Simulation simulation) {
		this.capLorry = capLorry;
		this.tLorry = tLorry;
		this.simulation = simulation;
		resCount = 0;
	}

	@Override
	synchronized public void run() {
		try {
			Thread.sleep(10);
			System.out.println("[" + Long.toString(System.currentTimeMillis()) + "] LOADIN");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void loadResources(int resources, Worker worker) {
		for(int i = 0; i < resources; i++) {
			//System.out.println("[" + Long.toString(System.currentTimeMillis()) + "] Lorry loading by Worker " + worker.workerNum);
			run();
			System.out.println("[" + Long.toString(System.currentTimeMillis()) + "] Lorry loaded by Worker " + worker.workerNum);
			resCount++;
			if(resCount == capLorry) {
				simulation.prepareNew();
			}
		}
	}

}
