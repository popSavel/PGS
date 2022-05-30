import java.io.PrintWriter;

public class Barrier {
	
	private int count;
	private int capFerry;
	private boolean sleep;
	long startTime;
	PrintWriter output;
	long waitTime;
	
	public Barrier(int capFerry, PrintWriter output) {
		this.capFerry = capFerry;
		count = 0;
		this.sleep = true;
		this.output = output;
	}
	
	/**
	 * implemented barrier, that make lorries wait, until ferry is filled 
	 * if sleep boolean is set to false, it means that lorries from previous ferry are still in the barrier and current lorry thread needs to wait
	 * if count of lorries waiting in barrier is equal to ferry capacity, sleep will set to false, so next lorries will wait till current lorries leave
	 * and print info about ferry leaving
	 * after all lorries leave sleep will set to true again
	 */
	public synchronized void synchronize() {	
		
		
		while(sleep == false) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.out.println("Error thread waiting, simulation manually stopped before finish!!");
				System.exit(0);
			}
		}
		
		this.count++;
		
		if(this.count == capFerry) {
			sleep = false;
			notifyAll();
			
			System.out.println("<" +(System.currentTimeMillis() - startTime)+ "> <Ferry> <"+ Thread.currentThread().getName()+"> <drove out>");
			output.println("<"+(System.currentTimeMillis() - startTime)+"> <ferry> <"+Thread.currentThread().getName()+"> <took off, it waited "+ (System.currentTimeMillis() - this.waitTime)+" ms>");
			this.waitTime = System.currentTimeMillis();
		}
		
		while(sleep == true) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.out.println("Error thread waiting, simulation manually stopped before finish!!");
				System.exit(0);
			}
		}
		
		count --;
		
		if(count == 0) {
			sleep = true;
			notifyAll();
		}
	}
}
