import java.io.PrintWriter;

public class Ferry {
	
	int capFerry;
	int lorryCount;
	Lorry [] lorry;
	Thread [] lorryThread;
	PrintWriter output;
	long waitTime;
	boolean lorriesGoing;
	Simulation simulation;

	public Ferry(int capFerry, PrintWriter output) {
		lorryCount = 0;
		this.capFerry = capFerry;
		this.lorry = new Lorry[capFerry];
		lorryThread = new Thread[capFerry];
		this.output = output;
		
	}

	/**
	 * add lorry into lorry[] field
	 * if number of lorries in ferry is equal to ferry capacity, it will create new threads for each lorry and then start them
	 * then it will set lorryCount to 0 and nulls into lorry and lorryThread fields
	 * @param currLorry last loaded Lorry
	 */
	public void addLorry(Lorry currLorry) {
		lorry[lorryCount] = currLorry;
		lorryCount++;
		if(lorryCount == capFerry) {
			System.out.println("<" +(System.currentTimeMillis() - simulation.startTime)+ "> <Ferry> <"+ Thread.currentThread().getName()+"> <drove out>");
			output.println("<"+(System.currentTimeMillis() - simulation.startTime)+"> <ferry> <"+Thread.currentThread().getName()+"> <took off, it waited "+ (System.currentTimeMillis() - this.waitTime)+" ms>");
			lorriesGoing = true;
			for(int i = 0; i < lorryCount; i++) {
				lorryThread[i] = new Thread(lorry[i]);
			}
			for(int i = 0; i < lorryCount; i++) {
				lorryThread[i].start();
			}
			lorriesGoing = false;
			lorryCount = 0;
			for(int i = 0; i < lorry.length; i++) {
				lorry[i] = null;
				lorryThread[i] = null;
			}
			waitTime = System.currentTimeMillis();
		}
		
	}

}
