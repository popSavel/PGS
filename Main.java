import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		int cWorker = Integer.parseInt(args[0]);
		int tWorker= Integer.parseInt(args[1]);
		int tLorry = Integer.parseInt(args[2]);
		int capLorry = Integer.parseInt(args[3]);
		int capFerry = Integer.parseInt(args[4]);
		
		String file = "ref_input.txt";
		int [] blocks = loadData(file);
		Foreman foreman = new Foreman(blocks);
		Worker [] workers = new Worker[cWorker];
		for(int i = 0; i < workers.length; i++) {
			workers[i] = new Worker(i + 1, tWorker);
		}
		Thread [] threads = new Thread[workers.length];
		Ferry ferry = new Ferry(capFerry);
		
		for(int i = 0; i < workers.length; i++) {
			threads[i] = new Thread(workers[i]);
		}
		Simulation simulation = new Simulation(foreman, ferry, workers, threads, capLorry, tLorry, capFerry);
		simulation.start();
	}

	private static int [] loadData(String file) {
		int [] blocks = null;
		Path path = Path.of(file);
		try {
			Scanner sc = new Scanner(path);
			ArrayList<Integer>result = new ArrayList<Integer>();
			while(sc.hasNext()) {
				String block = sc.next();
				int blockLength = block.length();
				result.add(blockLength);
			}
			blocks = new int[result.size()];
			for(int i = 0; i < blocks.length; i++) {
				blocks[i] = result.get(i);
			}
			sc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return blocks;
	}

}
