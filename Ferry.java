import java.io.PrintWriter;

public class Ferry {
	
	int capFerry;
	int lorryCount;
	PrintWriter output;
	long waitTime;
	Simulation simulation;

	public Ferry(int capFerry, PrintWriter output) {
		lorryCount = 0;
		this.capFerry = capFerry;
		this.output = output;
		
	}

	/**
	 * if number of lorries in ferry is equal to ferry capacity it will set lorryCount to 0 
	 * @param currLorry last loaded Lorry
	 */
	public void addLorry() {
		lorryCount++;
		if(lorryCount == capFerry) {
			System.out.println("<" +(System.currentTimeMillis() - simulation.startTime)+ "> <Ferry> <"+ Thread.currentThread().getName()+"> <drove out>");
			output.println("<"+(System.currentTimeMillis() - simulation.startTime)+"> <ferry> <"+Thread.currentThread().getName()+"> <took off, it waited "+ (System.currentTimeMillis() - this.waitTime)+" ms>");
			lorryCount = 0;
			waitTime = System.currentTimeMillis();
		}
		
	}

}
