import java.io.PrintWriter;
import java.util.Random;

public class Lorry implements Runnable{
	
	int capLorry;
	int resCount;
	int tLorry;
	int lorryIndex;
	boolean isFull;
	Simulation simulation;
	PrintWriter output;
	Barrier barrier;
	
	/**
	 * time, this started waiting to be filled
	 */
	long waitTime;

	public Lorry(Barrier barrier, int capLorry, int tLorry, Simulation simulation, int index, PrintWriter output) {
		this.capLorry = capLorry;
		this.tLorry = tLorry;
		this.simulation = simulation;
		resCount = 0;
		isFull = false;
		lorryIndex = index;
		this.output = output;
		this.barrier = barrier;
	}

	/**
	 * it wait random time from 0 to tLorry as it is going to ferry, then add this to Ferry, than call synchronize from Barrier, so it wait for Ferry to get filled with lorries
	 * after ferry is full, lorry waits again random time from 0 to tLorry as it is going from ferry to finish and then add transported resources as resCount to extractedTotal in Simulation 
	 */
	@Override
	public void run() {
		Random r = new Random();
		int time = r.nextInt(tLorry + 1);
			try {
				output.println("<"+(System.currentTimeMillis() - simulation.startTime)+"> <lorry "+this.lorryIndex+"> <"+Thread.currentThread().getName()+"> <lorry full, it waited "+ (System.currentTimeMillis() - this.waitTime) +" ms>");
				long tookOff = System.currentTimeMillis();
				Thread.sleep(time);
				output.println("<"+(System.currentTimeMillis() - simulation.startTime)+"> <lorry "+this.lorryIndex+"> <"+Thread.currentThread().getName()+"> <lorry arrived to ferry, it took "+ (System.currentTimeMillis() - tookOff)+" ms>");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			simulation.ferry.addLorry();
			barrier.synchronize(this);
			
			try {
				long tookOff = System.currentTimeMillis();
				Thread.sleep(time);
				output.println("<"+(System.currentTimeMillis() - simulation.startTime)+"> <lorry "+this.lorryIndex+"> <"+Thread.currentThread().getName()+"> <lorry arrived to finish, it took "+ (System.currentTimeMillis() - tookOff)+" ms, from ferry arrival>");
				//simulation.extractedTotal += resCount;
				simulation.addExtracted(resCount);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}

	/**
	 * increase total of resources loaded, and if it is equal to capacity, change boolean isFull to true
	 */
	public void load() {
		resCount++;
		if(resCount == capLorry) {
			isFull = true;
		}	
	}

}
