
public class Foreman {
	
	int [] blocks;
	int index;
	
	public Foreman(int[] blocks) {
		this.blocks = blocks;
		index = 0;
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


}
