package imageprocessing;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;
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
	
	public void initTI()
	{
		System.out.println("Début de la phase d'initialisation");
		
		SwingUtilities.invokeLater(new WebCamCapture()); // A modifier pour ne pas avoir de JFrame
		long time = System.currentTimeMillis();
		int seuil = 200;
		TraitementImage ti = new TraitementImage();
		List<FormObject> lf = ti.etiquetageIntuitifImageGiveList("toto.jpg", "toto.jpg",seuil);
		setRelativeValues(lf, ti.getImgHeight(), ti.getImgWidth());
		
		System.out.println("Fin de la phase d'initialisation après " + (System.currentTimeMillis()-time) + " millisecondes");
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
	
	public void setRelativeValues(List<FormObject> listInit, int imgHeight, int imgWidth) 
	{
		int separationX = imgHeight/2;
		int separationY = imgWidth/2;
		int tempX = 0, tempY = 0;
		
		List<FormObject> HautGauche = new ArrayList<FormObject>();
		List<FormObject> HautDroite = new ArrayList<FormObject>();
		List<FormObject> BasGauche = new ArrayList<FormObject>();
		List<FormObject> BasDroite = new ArrayList<FormObject>();

		Pixel pix = null;
		
		for (FormObject formList : listInit) 
		{
			tempX = formList.getBaryCenter().getX();
			tempY = formList.getBaryCenter().getY();
			
			if(tempX <= separationX)
			{
				if(tempY <= separationY)
					BasGauche.add(formList);
				else
					BasDroite.add(formList);
			}
			else
			{
				if(tempY <= separationY)
					HautGauche.add(formList);
				else
					HautDroite.add(formList);
			}
		}
		
		if(HautGauche.size() == 0 && BasDroite.size() == 1 && (BasGauche.size() != 0 || HautDroite.size() != 0))
			pix = BasDroite.get(0).getBaryCenter();
		
		if(HautDroite.size() == 0 && BasGauche.size() == 1 && (BasDroite.size() != 0 || HautGauche.size() != 0))
			pix = BasGauche.get(0).getBaryCenter();
		
		if(BasDroite.size() == 0 && HautGauche.size() == 1 && (BasGauche.size() != 0 || HautDroite.size() != 0))
			pix = HautGauche.get(0).getBaryCenter();
		
		if(BasGauche.size() == 0 && HautDroite.size() == 1 && (BasDroite.size() != 0 || HautGauche.size() != 0))
			pix = HautDroite.get(0).getBaryCenter();

		setRelativeX(pix.getX());
		setRelativeY(pix.getY());
	}
	
	public void setRelativeX(int valueX)
	{
		relativeX = valueX;
	}
	
	public void setRelativeY(int valueY)
	{
		relativeY = valueY;
	}
	
}
