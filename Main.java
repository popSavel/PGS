import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Main {

	public static void main(String[] args) {
		
		PrintWriter output = null;
		
		/**
		 * saving input parameters
		 */
		String file = "";
		String outputName = "";
		int cWorker = 0;
		int tWorker= 0;
		int tLorry = 0;
		int capLorry = 0;
		int capFerry = 0;
		for(int i = 0; i < args.length; i+=2) {
			String param = args[i];
			switch(param) {
			case "-i":
				file = args[i+1];
			break;
			case "-o":
				outputName = args[i+1];
			break;
			case "-cWorker":
				cWorker = Integer.parseInt(args[i+1]);
			break;
			case "-tWorker":
				tWorker = Integer.parseInt(args[i+1]);
			break;
			case "-tLorry":
				tLorry = Integer.parseInt(args[i+1]);
			break;
			case "-capLorry":
				capLorry = Integer.parseInt(args[i+1]);
			break;
			case "-capFerry":
				capFerry = Integer.parseInt(args[i+1]);
			break;
			}
		}
		
		/**
		 * printWriter initialization
		 */
		try {
			output = new PrintWriter(new FileWriter(outputName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/**
		 * initialization of Workers, Lorries, Ferry and their Threads
		 */
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
		 * printing input parameters and data loading results
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
		simulation.waitUntilOver();
		
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
