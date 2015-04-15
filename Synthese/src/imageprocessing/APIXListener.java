package imageprocessing;

import java.util.EventListener;

public interface APIXListener extends EventListener{

	void newQRCode(QRCodeEvent e);
	
	void newMouvement(MovementEvent e);
}
