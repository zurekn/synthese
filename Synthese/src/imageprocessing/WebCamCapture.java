package imageprocessing;

/**
 * @site http://webcam-capture.sarxos.pl/
 */
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.Thread.UncaughtExceptionHandler;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamDiscoveryEvent;
import com.github.sarxos.webcam.WebcamDiscoveryListener;
import com.github.sarxos.webcam.WebcamEvent;
import com.github.sarxos.webcam.WebcamListener;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamPicker;
import com.github.sarxos.webcam.WebcamResolution;

public class WebCamCapture extends JFrame implements Runnable, WebcamListener,
		WindowListener, UncaughtExceptionHandler, ActionListener, ItemListener,
		WebcamDiscoveryListener {
	private static final long serialVersionUID = 1L;
	private Webcam webcam = null;
	private WebcamPanel panel = null;
	private WebcamPicker picker = null;

	public void run() {

		Webcam.addDiscoveryListener(this);

		setTitle("Java Webcam Capture POC");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		addWindowListener(this);

		picker = new WebcamPicker();
		picker.addItemListener(this);

		JButton buttonCapture = new JButton();
		buttonCapture.addActionListener(this);

		webcam = picker.getSelectedWebcam();

		if (webcam == null) {
			System.out.println("No webcams found...");
			System.exit(1);
		}

		webcam.setViewSize(WebcamResolution.VGA.getSize());
		webcam.addWebcamListener(WebCamCapture.this);

		panel = new WebcamPanel(webcam, false);
		panel.setFPSDisplayed(true);

		add(picker, BorderLayout.NORTH);
		add(buttonCapture, BorderLayout.EAST);
		add(panel, BorderLayout.CENTER);

		pack();
		setVisible(true);

		Thread t = new Thread() {

			public void run() {
				panel.start();
			}
		};
		t.setName("example-starter");
		t.setDaemon(true);
		t.setUncaughtExceptionHandler(this);
		t.start();
	}

	public void webcamOpen(WebcamEvent we) {
		System.out.println("webcam open");
	}

	public void webcamClosed(WebcamEvent we) {
		System.out.println("webcam closed");
	}

	public void webcamDisposed(WebcamEvent we) {
		System.out.println("webcam disposed");
	}

	public void webcamImageObtained(WebcamEvent we) {
		// do nothing
	}

	public void windowActivated(WindowEvent e) {
	}

	public void windowClosed(WindowEvent e) {
		webcam.close();
	}

	public void windowClosing(WindowEvent e) {
	}

	public void windowOpened(WindowEvent e) {
	}

	public void windowDeactivated(WindowEvent e) {
	}

	public void windowDeiconified(WindowEvent e) {
		System.out.println("webcam viewer resumed");
		panel.resume();
	}

	public void windowIconified(WindowEvent e) {
		System.out.println("webcam viewer paused");
		panel.pause();
	}

	public void uncaughtException(Thread t, Throwable e) {
		System.err
				.println(String.format("Exception in thread %s", t.getName()));
		e.printStackTrace();
	}

	public void itemStateChanged(ItemEvent e) {
		if (e.getItem() != webcam) {
			if (webcam != null) {

				panel.stop();

				remove(panel);

				webcam.removeWebcamListener(this);
				webcam.close();

				webcam = (Webcam) e.getItem();
				// set the camera resolution
				// webcam.setViewSize(WebcamResolution.VGA.getSize());
				webcam.setViewSize(new Dimension(640, 480));

				webcam.addWebcamListener(this);

				System.out.println("selected " + webcam.getName());

				panel = new WebcamPanel(webcam, false);
				panel.setFPSDisplayed(true);

				add(panel, BorderLayout.CENTER);
				pack();

				Thread t = new Thread() {

					public void run() {
						panel.start();
					}
				};
				t.setName("example-stoper");
				t.setDaemon(true);
				t.setUncaughtExceptionHandler(this);
				t.start();
			}
		}
	}

	public void webcamFound(WebcamDiscoveryEvent event) {
		if (picker != null) {
			picker.addItem(event.getWebcam());
		}
	}

	public void webcamGone(WebcamDiscoveryEvent event) {
		if (picker != null) {
			picker.removeItem(event.getWebcam());
		}
	}

	public void actionPerformed(ActionEvent e) {
		// get image
		BufferedImage image = webcam.getImage();
		String fileName = "webcamCapture.jpg";
		// save image to PNG file
		try {
			ImageIO.write(image, "jpg", new File(fileName));
			TraitementImage ti = new TraitementImage();
			// ti.makeBinaryImage("webcamCapture.jpg",
			// "vintageWebcamCapture.jpg", "jpg", 80);
			// int seuil = 110;
			ti.makeBinaryImage(fileName, "manathan_bin.jpg", "jpg", 110);
			// Pixel p =
			// ti.EtiquetageIntuitifImage("Manathan.jpg","Manathan_vide.jpg");
			// System.out.println("pixel centre gravit� : "+p.getX()+" "+p.getY());
		} catch (Exception e2) {
			System.out.println(e2);
		}
		// TAKE A PICTURE

	}
}