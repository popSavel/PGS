import java.io.PrintWriter;
import java.util.Random;

public class Lorry implements Runnable{
	
	int capLorry;
	int resCount;
	int tLorry;
	int lorryIndex;
	boolean isFull;
	boolean atFerry;
	Simulation simulation;
	PrintWriter output;
	
	/**
	 * time, this started waiting to be filled
	 */
	long waitTime;

	public Lorry(int capLorry, int tLorry, Simulation simulation, int index, PrintWriter output) {
		this.capLorry = capLorry;
		this.tLorry = tLorry;
		this.simulation = simulation;
		resCount = 0;
		isFull = false;
		lorryIndex = index;
		atFerry = false;
		this.output = output;
	}

	/**
	 * if atFerry is false - it wait random time from 0 to tLorry as it is going to ferry, then add this to Ferry and change atFerry to true
	 * if atFerry is true - it wait random time from 0 to tLorry as it is going from ferry to finish and then add transported resources as resCount to extractedTotal in Simulation 
	 */
	@Override
	public void run() {
		Random r = new Random();
		int time = r.nextInt(tLorry + 1);
		if(!atFerry) {
			try {
				output.println("<"+(System.currentTimeMillis() - simulation.startTime)+"> <lorry "+this.lorryIndex+"> <"+Thread.currentThread().getName()+"> <lorry full, it waited "+ (System.currentTimeMillis() - this.waitTime) +" ms>");
				long tookOff = System.currentTimeMillis();
				Thread.sleep(time);
				output.println("<"+(System.currentTimeMillis() - simulation.startTime)+"> <lorry "+this.lorryIndex+"> <"+Thread.currentThread().getName()+"> <lorry arrived to ferry, it took "+ (System.currentTimeMillis() - tookOff)+" ms>");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			atFerry = true;
			
			simulation.ferry.addLorry(this);
		
		}else {
			try {
				long tookOff = System.currentTimeMillis();
				Thread.sleep(time);
				output.println("<"+(System.currentTimeMillis() - simulation.startTime)+"> <lorry "+this.lorryIndex+"> <"+Thread.currentThread().getName()+"> <lorry arrived to finish, it took "+ (System.currentTimeMillis() - tookOff)+" ms, from ferry arrival>");
				simulation.extractedTotal += resCount;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
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
