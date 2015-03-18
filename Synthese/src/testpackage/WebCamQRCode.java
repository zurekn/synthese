package testpackage;

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

import imageprocessing.TraitementQRjavapapers;

public class WebCamQRCode extends JFrame implements Runnable, ThreadFactory {
	
	private static final long serialVersionUID = 6441489157408381878L;
	private Executor executor = Executors.newSingleThreadExecutor(this);
	private Webcam webcam = null;
	private WebcamPanel panel = null;
	private JTextArea textarea = null;
	
	public WebCamQRCode() {
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
			Thread.sleep(100);
			} catch (InterruptedException e) {
			e.printStackTrace();
			}
			// Those lines are for the multi QR code reader
			String testMulti = null;
			TraitementQRjavapapers tqr = new TraitementQRjavapapers();
			
			Result result = null;
			// image will contain the webcam captured pictures
			BufferedImage image = null;
			if (webcam.isOpen()) {
				if ((image = webcam.getImage()) == null) {
					continue;
				}
				LuminanceSource source = new BufferedImageLuminanceSource(image);
				BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
				try {
					result = new MultiFormatReader().decode(bitmap);
					try {
						testMulti=tqr.findAllQR("", 2, image);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (NotFoundException e) {
				// fall thru, it means there is no QR code in image
					System.out.println("no QRCode in image");
				}
			}
			if(testMulti != null){
				textarea.setText(testMulti);
			}else{
				textarea.setText("");
			}
			/*
			if (result != null) {
				
				textarea.setText(result.getText());
				
			}
			*/
		} while (true);
	}
	
	public Thread newThread(Runnable r) {
		Thread t = new Thread(r, "example-runner");
		t.setDaemon(true);
		return t;
	}
	
	
	public static void main(String[] args) {
		new WebCamQRCode();
	}
}