package imageprocessing;

import java.util.EventListener;

public interface QRCodeListener extends EventListener{

	void newQRCode(QRCodeEvent e);
}
