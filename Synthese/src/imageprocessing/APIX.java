package imageprocessing;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;

import javax.swing.event.EventListenerList;

public class APIX implements Runnable {

	private String QRDatas;
	private QRCam qrcam;
	private ImageProcessing ip;
	private Webcam webcam;
	private final EventListenerList listeners = new EventListenerList();
	private boolean isInit = true;
	private int relativeX = -1;
	private int relativeY = -1;

	public APIX() {

		Dimension size = WebcamResolution.QVGA.getSize();

		webcam = Webcam.getWebcams().get(0);
		webcam.setViewSize(size);
		webcam.open();

		// Init the QRCode part
		qrcam = new QRCam(webcam);
		qrcam.addQRCodeListener(new QRCodeAdapter() {
			@Override
			public void newQRCode(QRCodeEvent e) {
				addQREvent(e);
			}
		});

		// init the ImageProcessing part
		initTI();

	}

	public void initTI() {
		System.out.println("Begin of the imageProcessing initialization");

		ip = new ImageProcessing(webcam);
		ip.addMovementListener(new MovementAdapter(){
			
			public void newMovement(MovementEvent e){
				
				addMovementEvent(new MovementEvent(e.getX() - relativeX, e.getY() - relativeY));
			}
		});
		
		long time = System.currentTimeMillis();
		BufferedImage image = webcam.getImage();
		setRelativeValues(ip.ti.etiquetageIntuitifImageGiveList2(image, image, 200), image.getHeight(), image.getWidth());
		isInit = true;
		
		System.out.println("Fin de la phase d'initialisation après "
				+ (System.currentTimeMillis() - time) + " millisecondes");
	}

	protected void addMovementEvent(MovementEvent e){
		for(APIXListener listener : getAPIXListener())
			listener.newMouvement(e);
	}
	
	public String getQRDatas() {
		return QRDatas;
	}

	protected void addQREvent(QRCodeEvent e) {
		for (APIXListener listener : getAPIXListener()) {
			listener.newQRCode(e);
		}
	}

	public void setQRDatas(String qRDatas) {
		if (!qRDatas.equals(QRDatas)) {
			System.out.println("New set dans setQRDatas");
			QRDatas = qRDatas;
			dataChanged(QRDatas);
		}
		this.QRDatas = qRDatas;

	}

	public boolean isInit() {
		return isInit;
	}

	public void setInit(boolean b) {
		isInit = b;
	}

	public void updateQR() {
		String s = qrcam.getQRDatas();
		if (!s.equals(QRDatas)) {
			QRDatas = s;
			dataChanged(QRDatas);
		}
	}

	public void addAPIXListener(APIXListener listener) {
		listeners.add(APIXListener.class, listener);
	}

	public void removeAPIXListener(APIXListener listener) {
		listeners.remove(APIXListener.class, listener);
	}

	public APIXListener[] getAPIXListener() {
		return listeners.getListeners(APIXListener.class);
	}

	/**
	 * Add a event to the listener when a the data is set
	 * 
	 * @param data
	 *            , String
	 */
	protected void dataChanged(String data) {
		QRCodeEvent event = null;
		for (APIXListener listener : getAPIXListener()) {
			if (event == null)
				event = new QRCodeEvent(data);
			listener.newQRCode(event);
		}
	}

	public void run() {
		// TODO

	}

	public void setRelativeValues(List<FormObject> listInit, int imgHeight,
			int imgWidth) {
		int separationX = imgHeight / 2;
		int separationY = imgWidth / 2;
		int tempX = 0, tempY = 0;

		List<FormObject> HautGauche = new ArrayList<FormObject>();
		List<FormObject> HautDroite = new ArrayList<FormObject>();
		List<FormObject> BasGauche = new ArrayList<FormObject>();
		List<FormObject> BasDroite = new ArrayList<FormObject>();

		Pixel pix = null;

		for (FormObject formList : listInit) {
			tempX = formList.getBaryCenter().getX();
			tempY = formList.getBaryCenter().getY();

			if (tempX <= separationX) {
				if (tempY <= separationY)
					BasGauche.add(formList);
				else
					BasDroite.add(formList);
			} else {
				if (tempY <= separationY)
					HautGauche.add(formList);
				else
					HautDroite.add(formList);
			}
		}

		if (HautGauche.size() == 0 && BasDroite.size() == 1
				&& (BasGauche.size() != 0 || HautDroite.size() != 0))
			pix = BasDroite.get(0).getBaryCenter();

		if (HautDroite.size() == 0 && BasGauche.size() == 1
				&& (BasDroite.size() != 0 || HautGauche.size() != 0))
			pix = BasGauche.get(0).getBaryCenter();

		if (BasDroite.size() == 0 && HautGauche.size() == 1
				&& (BasGauche.size() != 0 || HautDroite.size() != 0))
			pix = HautGauche.get(0).getBaryCenter();

		if (BasGauche.size() == 0 && HautDroite.size() == 1
				&& (BasDroite.size() != 0 || HautGauche.size() != 0))
			pix = HautDroite.get(0).getBaryCenter();

		setRelativeX(pix.getX());
		setRelativeY(pix.getY());
	}

	public void setRelativeX(int valueX) {
		this.relativeX = valueX;
	}

	public void setRelativeY(int valueY) {
		this.relativeY = valueY;
	}

}
