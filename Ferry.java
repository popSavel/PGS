
public class Ferry {
	
	int capFerry;
	int lorryCount;
	Lorry [] lorry;
	Thread [] lorryThread;

	public Ferry(int capFerry) {
		lorryCount = 0;
		this.capFerry = capFerry;
		this.lorry = new Lorry[capFerry];
		lorryThread = new Thread[capFerry];
	}

	public void addLorry(Lorry currLorry) {
		lorry[lorryCount] = currLorry;
		lorryCount++;
		if(lorryCount == capFerry) {
			System.out.println("<" + System.currentTimeMillis() + "> <Ferry> <drove out>");
			for(int i = 0; i < lorryCount; i++) {
				lorryThread[i] = new Thread(lorry[i]);
			}
			for(int i = 0; i < lorryCount; i++) {
				lorryThread[i].start();
			}
			lorryCount = 0;
		}
		
	}

}
