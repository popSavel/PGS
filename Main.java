import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Main {

	public static void main(String[] args) {
		
		PrintWriter output = null;
		
		/**
		 * saving input parameters
		 */
		int cWorker = Integer.parseInt(args[0]);
		int tWorker= Integer.parseInt(args[1]);
		int tLorry = Integer.parseInt(args[2]);
		int capLorry = Integer.parseInt(args[3]);
		int capFerry = Integer.parseInt(args[4]);
		
		/**
		 * printwriter initialization
		 */
		try {
			output = new PrintWriter(new FileWriter("output.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/**
		 * initialization of Workers, Lorries, Ferry and their Threads
		 */
		String file = "ref_input.txt";
		//String file = "test.txt";
		Foreman foreman = new Foreman(file, output);
		
		Worker [] workers = new Worker[cWorker];
		for(int i = 0; i < workers.length; i++) {
			workers[i] = new Worker(i + 1, tWorker, output);
		}
		
		Thread [] threads = new Thread[workers.length];
		Ferry ferry = new Ferry(capFerry, output);
		for(int i = 0; i < workers.length; i++) {
			threads[i] = new Thread(workers[i]);
		}
		
		/**
		 * preparing and starting simulation
		 */
		Simulation simulation = new Simulation(foreman, ferry, workers, threads, capLorry, tLorry, capFerry, output);
		foreman.simulation = simulation;
		ferry.simulation = simulation;
		simulation.start();
		
		/**
		 * printing input paramaters and data loading results
		 */
		System.out.println("Specified parameters:");
		System.out.println("cWorker: " + cWorker);
		System.out.println("tWorker: " + tWorker);
		System.out.println("tLorry: " + tLorry);
		System.out.println("capLorry: " + capLorry);
		System.out.println("capFerry: " + capFerry);
		System.out.println("blocks found: " + foreman.blocks.length);
		System.out.println("sources found: " + foreman.getSourceCount());
		System.out.println("STARTING SIMULATION");
		
		/**
		 * waiting for the end of simulation, so final statistics are relevant
		 */
		while(!simulation.isOver()) {
			
		}
		
		/**
		 * printing statistics after simulation
		 */
		output.close();
		System.out.println("SIMULATION OVER");
		for(int i = 0; i < workers.length; i++) {
			System.out.println("Worker " + (i+1) + " extracted " + workers[i].extractedTotal+" sources");
		}
		System.out.println("Sources extracted: " + simulation.extractedTotal);
	}

}
