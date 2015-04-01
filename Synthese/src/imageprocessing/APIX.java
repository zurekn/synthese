package imageprocessing;

import javax.swing.event.EventListenerList;

public class APIX implements Runnable{

	private String QRDatas;
	private QRCam qrcam;
	private final EventListenerList listeners = new EventListenerList();
	private boolean isInit = true;
	private int relativeX;
	private int relativeY;
	
	
	public APIX(){
		// qrcam.run();
	}
	
	public String getQRDatas() {
		return QRDatas;
	}

	public void setQRDatas(String qRDatas) {
		this.QRDatas = qRDatas;
		if (!qRDatas.equals(QRDatas)){
			QRDatas = qRDatas;
			dataChanged(QRDatas);	
		}
	}
	
	public boolean isInit(){
		return isInit;
	}
	
	public void setInit(boolean b){
		isInit = b;
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
	
	/**
	 * Add a event to the listener when a the data is set
	 * @param data, String
	 */
	protected void dataChanged(String data){
		QRCodeEvent event = null;
		for(APIXListener listener : getAPIXListener()){
			if(event == null)
				event = new QRCodeEvent(data);
			listener.newQRCode(event);
		}
	}

	public void run() {
		//TODO
		
	}
	
}
