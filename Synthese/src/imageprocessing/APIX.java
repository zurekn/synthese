package imageprocessing;

import javax.swing.event.EventListenerList;

public class APIX {

	private String QRDatas;
	private QRCam qrcam;
	private final EventListenerList listeners = new EventListenerList();
	
	public APIX(){
		// qrcam.run();
	}
	
	public String getQRDatas() {
		return QRDatas;
	}

	public void setQRDatas(String qRDatas) {
		QRDatas = qRDatas;
	}

	public void updateQR(){
		String s = qrcam.getQRDatas();
		if (!s.equals(QRDatas)){
			QRDatas = s;
			dataChanged(QRDatas);	
		}
	}
	
	public void addAPIXListener(APIXListener listener){
		listeners.add(APIXListener.class, listener);
	}
	
	public void removeAPIXListener(APIXListener listener){
		listeners.remove(APIXListener.class, listener);
	}
	
	public APIXListener[] getAPIXListener(){
		return listeners.getListeners(APIXListener.class);
	}
	
	protected void dataChanged(String data){
		QRCodeEvent event = null;
		for(APIXListener listener : getAPIXListener()){
			if(event == null)
				event = new QRCodeEvent(data);
			listener.newQRCode(event);
		}
	}
	
}
