package imageprocessing;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.event.EventListenerList;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;

public class APIX implements Runnable{

	private String QRDatas;
	private QRCam qrcam;
	private Webcam webcam;
	private final EventListenerList listeners = new EventListenerList();
	private boolean isInit = true;
	private int relativeX;
	private int relativeY;
	
	
	public APIX(){
		
		Dimension size = WebcamResolution.QVGA.getSize();
		webcam = Webcam.getWebcams().get(0);
		webcam.setViewSize(size);
		webcam.open();
		qrcam = new QRCam(webcam);
		qrcam.addQRCodeListener(new QRCodeAdapter(){
			@Override
			public void newQRCode(QRCodeEvent e) {addQREvent(e);}
		});

		//TODO need to create the TraitementImage part
		
//		qrcam.run();
	}
	
	public String getQRDatas() {
		return QRDatas;
	}

	protected void addQREvent(QRCodeEvent e){
		for(APIXListener listener : getAPIXListener()){
			listener.newQRCode(e);
		}
	}
	
	public void setQRDatas(String qRDatas) {
		if (!qRDatas.equals(QRDatas)){
			System.out.println("New set dans setQRDatas");
			QRDatas = qRDatas;
			dataChanged(QRDatas);	
		}
		this.QRDatas = qRDatas;

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
