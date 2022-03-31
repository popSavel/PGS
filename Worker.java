import java.io.PrintWriter;
import java.util.Random;

public class Worker implements Runnable{
	
	Random r;
	int tWorker;
	int workerNum;
	int extractedTotal;
	Simulation simulation;
	PrintWriter output;

	public Worker(int workerNum, int tWorker, PrintWriter output) {
		r = new Random();
		this.tWorker = tWorker;
		this.workerNum = workerNum;
		extractedTotal = 0;
		this.output = output;
	}

	@Override
	public void run() {
		while(!simulation.isOver()) {
			int resourceSize = simulation.getBLock();
			int blockTime = 0;
			System.out.println("--------------[" + Long.toString(System.currentTimeMillis()) + "] Worker " + workerNum + " got " + resourceSize + " resources");
			for(int i = 0; i < resourceSize; i++) {
				int time = r.nextInt(tWorker + 1);
				blockTime += time;
				try {
					//System.out.println("[" + Long.toString(System.currentTimeMillis()) + "] Worker " + workerNum + " loading " + time + "millis");
					Thread.sleep(time);
					output.println("<"+System.currentTimeMillis()+"> <worker "+this.workerNum+"> <"+Thread.currentThread()+"> <resource extraction finished, it took "+ time+" ms>");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//System.out.println("[" + Long.toString(System.currentTimeMillis()) + "] Worker " + workerNum + " loaded");	
			}
			output.println("<"+System.currentTimeMillis()+"> <worker "+this.workerNum+"> <"+Thread.currentThread()+"> <block extraction finished, it took "+ blockTime+" ms>");
			System.out.println("--------------[" + Long.toString(System.currentTimeMillis()) + "] Worker " + workerNum + " finished ");
			//simulation.loadResources(resourceSize, this);	
			
			for(int i = 0; i < resourceSize; i++) {
				
					//System.out.println("[" + Long.toString(System.currentTimeMillis()) + "] Worker " + workerNum +" loaded one");
					simulation.loadResource(this);
				
			}
			extractedTotal+=resourceSize;
			//simulation.loadResources(this, resourceSize);
		}
	}

}
