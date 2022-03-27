import java.util.Random;

public class Lorry implements Runnable{
	
	int capLorry;
	int resCount;
	int tLorry;
	int lorryIndex;
	boolean isFull;
	Simulation simulation;

	public Lorry(int capLorry, int tLorry, Simulation simulation, int index) {
		this.capLorry = capLorry;
		this.tLorry = tLorry;
		this.simulation = simulation;
		resCount = 0;
		isFull = false;
		lorryIndex = index;
	}

	@Override
	public void run() {
		
			Random r = new Random();
			int time = r.nextInt(tLorry + 1);
			try {
				System.out.println("[" + Long.toString(System.currentTimeMillis()) + "] Lorry " +lorryIndex+" set off to ferry, it should take " + time);
				Thread.sleep(time);
				System.out.println("[" + Long.toString(System.currentTimeMillis()) + "] Lorry " +lorryIndex+" arrived to ferry ");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	public void load() {
		resCount++;
		if(resCount == capLorry) {
			isFull = true;
		}	
	}

}
