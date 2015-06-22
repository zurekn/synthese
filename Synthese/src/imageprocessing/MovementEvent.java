package imageprocessing;

public class MovementEvent {

	private int x;
	private int y;
	private int size;

	public MovementEvent(int x, int y, int size) {
		super();
		this.x = x;
		this.y = y;
		this.size = size;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public String toString() {
		return "MovementEvent [x=" + x + ", y=" + y + "]";
	}

	public int getSize() {
		return size;
	}
	
	
}
