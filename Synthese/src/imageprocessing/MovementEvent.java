package imageprocessing;

public class MovementEvent {

	private int x;
	private int y;

	public MovementEvent(int x, int y) {
		super();
		this.x = x;
		this.y = y;
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
	
	
}
