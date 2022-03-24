
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


}
