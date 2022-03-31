import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

public class Foreman {
	
	int [] blocks;
	int index;
	PrintWriter output;

	public Foreman(String file, PrintWriter output) {
		this.output = output;
		this.blocks = loadData(file);
		output.println("<"+System.currentTimeMillis()+"> <foreman> <"+Thread.currentThread()+"> <input file loaded, blocks count: "+blocks.length+", resources count: "+getSourceCount()+">");
	}

	public int getBLock() {
		int result = blocks[index];
		index++;
		return result;
	}
	
	public boolean hasNext() {
		if(index <= blocks.length - 1) {
			return true;
		}else {
			return false;
		}
	}

	public int getSourceCount() {
		int result = 0;
		for(int i = 0; i < blocks.length; i++) {
			result += blocks[i];
		}
		return result;
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
