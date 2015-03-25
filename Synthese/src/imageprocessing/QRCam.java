package imageprocessing;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javax.swing.JFrame;
import javax.swing.JTextArea;

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

import imageprocessing. QRCodeProcessing;

public class QRCam extends JFrame implements Runnable, ThreadFactory {
	
	private static final long serialVersionUID = 6441489157408381878L;
	private Executor executor = Executors.newSingleThreadExecutor(this);
	private Webcam webcam = null;
	private WebcamPanel panel = null;
	private JTextArea textarea = null;
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
	
	public void run() {
		do {
			try {
				Thread.sleep(900);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// Those lines are for the multi QR code reader
			String testMulti = null;
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
					tqr.findAllQR("",20, image);
					testMulti = tqr.getQRDatas();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NotFoundException e) {
					// fall thru, it means there is no QR code in image
					System.out.println("no QRCode in image");
				}
			}

			if(testMulti != null){
				textarea.setText(testMulti);
				setQRDatas(testMulti);
			}else{
				textarea.setText("");
			}
		} while (true);
	}
	
	public Thread newThread(Runnable r) {
		Thread t = new Thread(r, "QR Decoder");
		t.setDaemon(true);
		return t;
	}
	
	
	public static void main(String[] args) {
		new QRCam();
	}
	
	public String getQRDatas(){
		return QRDatas;
	}
	
	public void setQRDatas(String QRDatas){
		this.QRDatas = QRDatas;
	}
}