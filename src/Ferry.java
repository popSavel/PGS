import java.io.PrintWriter;

public class Ferry {
	
	int capFerry;
	int lorryCount;
	PrintWriter output;
	long waitTime;
	Simulation simulation;
	Barrier barrier;

	public Ferry(int capFerry, PrintWriter output) {
		lorryCount = 0;
		this.capFerry = capFerry;
		this.output = output;
		this.barrier = new Barrier(this.capFerry);
		
	}

	/**
	 * increment counter and send lorry to barrier
	 */
	public void addLorry() {
		increment();
		barrier.synchronize();
	}
	
	/**
	 * method for incrementing lorry counter and printing info about ferry drove out
	 * if number of lorries in ferry is equal to ferry capacity it will set lorryCount to 0
	 * synchronized - if other lorry increment before previous one printed
	 */
	synchronized private void increment() {
		lorryCount++;
		if(lorryCount == capFerry) {
			System.out.println("<" +(System.currentTimeMillis() - simulation.startTime)+ "> <Ferry> <"+ Thread.currentThread().getName()+"> <drove out>");
			output.println("<"+(System.currentTimeMillis() - simulation.startTime)+"> <ferry> <"+Thread.currentThread().getName()+"> <took off, it waited "+ (System.currentTimeMillis() - this.waitTime)+" ms>");
			lorryCount = 0;
			waitTime = System.currentTimeMillis();
		}
		
	}
	
}
