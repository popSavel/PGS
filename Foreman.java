import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

public class Foreman {
	
	int [] blocks;
	
	/**
	 * index of last block given
	 */
	int index;
	
	String file;
	PrintWriter output;
	Simulation simulation;

	public Foreman(String file, PrintWriter output) {
		this.output = output;
		this.file = file;
	}

	/**
	 * @return first unextracted block
	 */
	public int getBLock() {
		int result = blocks[index];
		index++;
		return result;
	}
	
	/**
	 * @return true, if there are block left to extract
	 */
	public boolean hasNext() {
		if(index <= blocks.length - 1) {
			return true;
		}else {
			return false;
		}
	}

	/**
	 * @return total of sources in all blocks
	 */
	public int getSourceCount() {
		int result = 0;
		for(int i = 0; i < blocks.length; i++) {
			result += blocks[i];
		}
		return result;
	}
	
	/**
	 * Load all the locks from input file into field, as Integer equals to source count in the block
	 */
	public void loadData() {
		int [] blocks = null;
		Path path = Path.of(this.file);
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
		this.blocks = blocks;
		output.println("<"+(System.currentTimeMillis() - simulation.startTime)+"> <foreman> <"+Thread.currentThread().getName()+"> <input file loaded, blocks count: "+blocks.length+", resources count: "+getSourceCount()+">");
	}


}
