
public class Lorry implements Runnable{
	
	int capLorry;
	int tLorry;

	public Lorry(int capLorry, int tLorry) {
		this.capLorry = capLorry;
		this.tLorry = tLorry;
	}

	@Override
	synchronized public void run() {
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void loadResources(int resources, Worker worker) {
		for(int i = 0; i < resources; i++) {
			System.out.println("[" + Long.toString(System.currentTimeMillis()) + "] Lorry loading by Worker " + worker.workerNum);
			run();
			System.out.println("[" + Long.toString(System.currentTimeMillis()) + "] Lorry loaded by Worker " + worker.workerNum);
		}
	}

}
