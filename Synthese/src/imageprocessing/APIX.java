package imageprocessing;

public class APIX {

	private String QRDatas;
	private QRCam qrcam;
	public APIX(){
		// qrcam.run();
	}
	
	public void updateQR(){
		QRDatas = qrcam.getQRDatas();
	}
	
}
