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
		this.barrier = new Barrier(this.capFerry, output);
		
	}

	/**
	 * increment counter and send lorry to barrier
	 */
	public void addLorry() {
		barrier.synchronize();
	}
	

	public void setTime(long time) {
		this.barrier.startTime = time;
		this.barrier.waitTime = time;
	}
	
}
