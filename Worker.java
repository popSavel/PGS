import java.util.Random;

public class Worker implements Runnable{
	
	Foreman foreman;
	Lorry lorry;
	Random r;
	int tWorker;
	int workerNum;

	public Worker(int workerNum, Foreman foreman, int tWorker, Lorry lorry) {
		this.foreman = foreman;
		r = new Random();
		this.tWorker = tWorker;
		this.workerNum = workerNum;
		this.lorry = lorry;
	}

	@Override
	public void run() {
		int resourceSize = foreman.getBLock();
		System.out.println("[" + Long.toString(System.currentTimeMillis()) + "] Worker " + workerNum + " got " + resourceSize + "resources");
		for(int i = 0; i < resourceSize; i++) {
			int time = r.nextInt(tWorker);
			try {
				System.out.println("[" + Long.toString(System.currentTimeMillis()) + "] Worker " + workerNum + " loading " + time + "millis");
				Thread.sleep(time);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//System.out.println("[" + Long.toString(System.currentTimeMillis()) + "] Worker " + workerNum + " loaded");	
		}
		lorry.loadResources(resourceSize, this);
	}

}
