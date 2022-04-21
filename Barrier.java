
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
	 * then it will notifyAll those lorries and empty barrier 
	 * @param lorry
	 */
	public synchronized void synchronize() {	
		
		
		while(sleep == false) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		this.count++;
		
		if(this.count == capFerry) {
			//this.count = 0;
			sleep = false;
			notifyAll();
		}
		
		while(sleep == true) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		count --;
		
		if(count == 0) {
			sleep = true;
			notifyAll();
		}
		
		/*
		this.count++;
		if(this.count == capFerry) {
			this.count = 0;
			notifyAll();
		}
		else {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}*/
	}
}
