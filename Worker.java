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

	/**
	 * worker asks foreman for blocks, until he has some
	 * after he get block, he wait random time from 0 to toWorker for every resource in block, as he is extracting it
	 * after he extract whole block he calls loadResource() from Simulation for every resource he extracted
	 */
	@Override
	public void run() {
		while(simulation.foreman.hasNext()) {
			int resourceSize = simulation.getBLock();
			int blockTime = 0;
			for(int i = 0; i < resourceSize; i++) {
				int time = r.nextInt(tWorker + 1);
				blockTime += time;
				try {
					Thread.sleep(time);
					output.println("<"+(System.currentTimeMillis() - simulation.startTime)+"> <worker "+this.workerNum+"> <"+Thread.currentThread().getName()+"> <resource extraction finished, it took "+ time+" ms>");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			output.println("<"+(System.currentTimeMillis() - simulation.startTime)+"> <worker "+this.workerNum+"> <"+Thread.currentThread().getName()+"> <block extraction finished, it took "+ blockTime+" ms>");
			for(int i = 0; i < resourceSize; i++) {
				simulation.loadResource(this);
			}
			extractedTotal+=resourceSize;
		}
	}

}
