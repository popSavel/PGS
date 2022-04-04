
public class Barrier {
	
	private int count;
	private int capFerry;
	
	public Barrier(int capFerry) {
		this.capFerry = capFerry;
		count = 0;
	}
	
	/**
	 * implemented barrier, taht make lorries wait, until ferry is filled 
	 * then it will notifyAll those lorries and empty barrier 
	 * @param lorry
	 */
	public synchronized void synchronize(Lorry lorry) {
		this.count++;
		if(this.count == capFerry) {
			this.count = 0;
			notifyAll();
		}else {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
