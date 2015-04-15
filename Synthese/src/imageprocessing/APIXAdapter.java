package imageprocessing;

public abstract class APIXAdapter implements APIXListener{

	public void newQRCode(QRCodeEvent e){}
	
	public void newMovement(MovementEvent e){}
}
