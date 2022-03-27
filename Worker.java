import java.util.Random;

public class Worker implements Runnable{
	
	Random r;
	int tWorker;
	int workerNum;
	Simulation simulation;

	public Worker(int workerNum, int tWorker) {
		r = new Random();
		this.tWorker = tWorker;
		this.workerNum = workerNum;
	}

	@Override
	public void run() {
		while(!simulation.isOver()) {
			int resourceSize = simulation.getBLock();
			System.out.println("--------------[" + Long.toString(System.currentTimeMillis()) + "] Worker " + workerNum + " got " + resourceSize + " resources");
			for(int i = 0; i < resourceSize; i++) {
				int time = r.nextInt(tWorker + 1);
				try {
					//System.out.println("[" + Long.toString(System.currentTimeMillis()) + "] Worker " + workerNum + " loading " + time + "millis");
					Thread.sleep(time);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//System.out.println("[" + Long.toString(System.currentTimeMillis()) + "] Worker " + workerNum + " loaded");	
			}
			System.out.println("--------------[" + Long.toString(System.currentTimeMillis()) + "] Worker " + workerNum + " finished ");
			//simulation.loadResources(resourceSize, this);	
			
			for(int i = 0; i < resourceSize; i++) {
				
					//System.out.println("[" + Long.toString(System.currentTimeMillis()) + "] Worker " + workerNum +" loaded one");
					simulation.loadResource(this);
				
			}
			//simulation.loadResources(this, resourceSize);
		}
	}

}
