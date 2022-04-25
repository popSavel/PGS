
public class Barrier {
	
	private int count;
	private int capFerry;
	private boolean sleep;
	
	public Barrier(int capFerry) {
		this.capFerry = capFerry;
		count = 0;
		this.sleep = true;
	}
	
	/**
	 * implemented barrier, that make lorries wait, until ferry is filled 
	 * if sleep boolean is set to false, it means that lorries from previous ferry are still in the barrier and current lorry thread needs to wait
	 * if count of lorries waiting in barrier is equal to ferry capacity, sleep will set to false, so next lorries will wait till current lorries leave
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
