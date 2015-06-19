package imageprocessing;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.event.EventListenerList;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import data.Data;
import imageprocessing. QRCodeProcessing;

public class QRCam extends JFrame implements Runnable, ThreadFactory {
	
	private static final long serialVersionUID = 6441489157408381878L;
	private Executor executor = Executors.newSingleThreadExecutor(this);
	private Webcam webcam = null;
	private WebcamPanel panel = null;
	private JTextArea textarea = null;
	private final EventListenerList listeners = new EventListenerList();
	// QR datas
	private String QRDatas;
	
	public QRCam() {
		super();
		setLayout(new FlowLayout());
		setTitle("Read QR / Bar Code With Webcam");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Dimension size = WebcamResolution.QVGA.getSize();
		webcam = Webcam.getWebcams().get(0);
		webcam.setViewSize(size);
		
		//JFRAME
		panel = new WebcamPanel(webcam);
		panel.setPreferredSize(size);
		textarea = new JTextArea();
		textarea.setEditable(false);
		textarea.setPreferredSize(size);
		add(panel);
		add(textarea);
		pack();
		setVisible(true);
		
		executor.execute(this);
	}
	
	public QRCam(Webcam webcam){
		this.webcam = webcam;
		executor.execute(this);
		System.out.println("QRcode initialized");
	}
	
	public void run() {
		APIX apix = APIX.getInstance();
		if(!Data.runQRCam)
			return;
		do {
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//Wait APIX thread
			//apix.waitLock();
			//System.out.println("QRCode after waitlock()");
			
			// Those lines are for the multi QR code reader
			QRCodeEvent testMulti = null;
			QRCodeProcessing tqr = new QRCodeProcessing();

			Result result = null;
			// image will contain the webcam captured pictures
			BufferedImage image = null;

			if (webcam.isOpen()) {
				if ((image = webcam.getImage()) == null) {
					continue;
				}
				try {
						// set seuil to 20 to ensure better results
					
					tqr.findAllQR("",Data.QRCamSeuil, image);
					if(Data.debugQR){
						System.out.println("Image taken for QRcode");
						ImageIO.write(image, "jpg", new File(Data.getImageDir()+"QRCode.jpg"));
					}
					testMulti = tqr.getQRCodeEvent();
					System.out.println("After getQRCodeEvent");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NotFoundException e) {
					// fall thru, it means there is no QR code in image
					if(Data.debugQR)
						System.out.println("no QRCode in image at "+(System.currentTimeMillis() - Data.beginTime));
				}
			}else{
				System.err.println("Webcam ["+webcam.getName()+"] is not open");
			}

			if(testMulti != null){
//				textarea.setText(testMulti);
				if(Data.debugQR)
					System.out.println("New QRCode find : "+testMulti);
				if(Data.debug && Data.inTest && Data.debugPicture){
					try {
						System.out.println("=======================DEBUG============================");
						ImageIO.write(
								image,
								"jpg",
								new File(
										"C:/Users/boby/Google Drive/Master1/Synthèse/Rapport/newQRCode.jpg"));
						Data.debugPicture = false;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				newQRDatas(testMulti);
			}else{
				//textarea.setText("");
			}
		} while (true);
	}
	
	public Thread newThread(Runnable r) {
		Thread t = new Thread(r, "QR Decoder");
		t.setDaemon(true);
		return t;
	}
	

	public String getQRDatas(){
		return QRDatas;
	}
	
	
	public void newQRDatas(QRCodeEvent e){
		for(QRCodeListener listerner : getQRCodeListener()){
			listerner.newQRCode(e);
		}
	}
	
	public void setQRDatas(String QRDatas){
		this.QRDatas = QRDatas;
		QRCodeEvent event = null;
		for(QRCodeListener listerner : getQRCodeListener()){
			if(event == null)
				event = new QRCodeEvent(QRDatas);
			listerner.newQRCode(event);
		}
	}
	
	public void addQRCodeListener(QRCodeListener listener){
		listeners.add(QRCodeListener.class, listener);
	}
	
	public QRCodeListener[] getQRCodeListener(){
		return listeners.getListeners(QRCodeListener.class);
	}
}