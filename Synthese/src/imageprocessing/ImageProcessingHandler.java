package imageprocessing;

import java.awt.im.InputMethodHighlight;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.event.EventListenerList;

import com.github.sarxos.webcam.Webcam;

import data.Data;
import data.Handler;

public class ImageProcessingHandler extends Handler {

	private Webcam webcam;
	private BufferedImage imageRef;
	private int seuil;
	private static ImageProcessingHandler handler;
	protected ImageProcessing ip = new ImageProcessing();
	private final EventListenerList listeners = new EventListenerList();

	private ImageProcessingHandler(Webcam webcam) {
		super();
		this.webcam = webcam;
		seuil = 200;
	}

	/**
	 * Is intended to be called just once
	 * 
	 * @param webcam
	 * @return
	 */
	public static ImageProcessingHandler getInstance(Webcam webcam) {
		if (handler == null)
			handler = new ImageProcessingHandler(webcam);
		return handler;
	}

	public static ImageProcessingHandler getInstance() {
		if (handler == null)
			System.err
					.println("You need to call getInstance with Webcam parameter once before using it.");
		return handler;
	}

	public void initImageRef() {
		imageRef = webcam.getImage();
		if (Data.debugPicture) {
			try {
				ImageIO.write(imageRef, "jpg", new File(Data.getImageDir()
						+ "imageRef" + Data.getDate() + ".jpg"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void addMovementListener(MovementListener listener) {
		listeners.add(MovementListener.class, listener);
	}

	public MovementListener[] getMouvementListener() {
		return listeners.getListeners(MovementListener.class);
	}

	public void addMovement(List<FormObject> lf) {
		if (lf == null)
			return;
		for (FormObject o : lf) {
			MovementEvent e = new MovementEvent(o.getBaryCenter().getX(), o
					.getBaryCenter().getY());
			for (MovementListener listener : getMouvementListener())
				listener.newMovement(e);
		}

	}

	public void run() {

		BufferedImage image;
		System.out.println("Image Processing running");
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		initImageRef();

		APIX apix = APIX.getInstance();

		while (true) {
			// Wait APIX thread
			System.out.println("Lock ...");
			
			//apix.waitLock();


			if (Data.tiDebug)
				System.out
						.println("lancement thread -----------------------------------------------------------------------");

			System.out.println("commencement traitement image");
			image = webcam.getImage();
			List<FormObject> lf = ip.etiquetageIntuitifImageGiveList2(image,
					imageRef, seuil);
			addMovement(lf);
			try {
				// thread.wait(0);
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				ImageIO.write(image, "jpg", new File(Data.getImageDir()
						+ "traitementImage" + Data.getDate() + ".jpg"));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	public void begin() {
		System.out.println("Launch the Image Processing Thread");
		getThread().start();

	}

	public int[][] cutBlackBorder(int[][] elementsRes, int width, int height) {
		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				if (elementsRes[x][y] == 255)
					elementsRes[x][y] = 0;
				else
					break;
			}
		}
		for (int x = width - 1; x > 0; --x) {
			for (int y = 0; y < height; ++y) {
				if (elementsRes[x][y] == 255)
					elementsRes[x][y] = 0;
				else
					break;
			}
		}

		for (int x = 0; x < height; ++x) {
			for (int y = 0; y < width; ++y) {
				if (elementsRes[y][x] == 255)
					elementsRes[y][x] = 0;
				else
					break;
			}
		}
		for (int x = height - 1; x > 0; --x) {
			for (int y = 0; y < width; ++y) {
				if (elementsRes[y][x] == 255)
					elementsRes[y][x] = 0;
				else
					break;
			}
		}

		return elementsRes;
	}

}
