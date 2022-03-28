import java.util.Random;

public class Lorry implements Runnable{
	
	int capLorry;
	int resCount;
	int tLorry;
	int lorryIndex;
	boolean isFull;
	boolean atFerry;
	Simulation simulation;
	boolean isLast;

	public Lorry(int capLorry, int tLorry, Simulation simulation, int index) {
		this.capLorry = capLorry;
		this.tLorry = tLorry;
		this.simulation = simulation;
		resCount = 0;
		isFull = false;
		lorryIndex = index;
		atFerry = false;
		isLast = false;
	}

	@Override
	public void run() {
		Random r = new Random();
		int time = r.nextInt(tLorry + 1);
		if(!atFerry) {
			try {
				System.out.println("[" + Long.toString(System.currentTimeMillis()) + "] Lorry " +lorryIndex+" set off TO ferry, it should take " + time);
				Thread.sleep(time);
				System.out.println("[" + Long.toString(System.currentTimeMillis()) + "] Lorry " +lorryIndex+" arrived to ferry ");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			atFerry = true;
			
			simulation.ferry.addLorry(this);
		
		}else {
			try {
				System.out.println("[" + Long.toString(System.currentTimeMillis()) + "] Lorry " +lorryIndex+" set off FROM ferry, it should take " + time);
				Thread.sleep(time);
				System.out.println("[" + Long.toString(System.currentTimeMillis()) + "] Lorry " +lorryIndex+" arrived to finish ");
				simulation.extractedTotal += resCount;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void load() {
		resCount++;
		if(resCount == capLorry) {
			isFull = true;
		}	
	}

}
