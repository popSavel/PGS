
public class Barrier {
	
	private int count;
	private int capFerry;
	
	public Barrier(int capFerry) {
		this.capFerry = capFerry;
		count = 0;
	}
	
	public synchronized void synchronize(Lorry l) {
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
