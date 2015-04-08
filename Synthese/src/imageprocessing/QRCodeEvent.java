package imageprocessing;

public class QRCodeEvent {

	private String data;
	private String id;
	private int direction;
	private int x;
	private int y;

	QRCodeEvent(String data) {
		this.data = data;
	}

	QRCodeEvent(String id, int direction, int x, int y) {
		this.id = id;
		this.direction = direction;
		this.x = x;
		this.y = y;
	}

	public String getId() {
		return id;
	}

	public int getDirection() {
		return direction;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public String getData() {
		return data;
	}

	@Override
	public String toString() {
		return "QRCodeEvent [id=" + id + ", direction=" + direction + ", x="
				+ x + ", y=" + y + "]";
	}

}
