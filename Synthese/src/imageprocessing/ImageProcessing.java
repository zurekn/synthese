package imageprocessing;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javax.imageio.ImageIO;
import javax.swing.DebugGraphics;
import javax.swing.event.EventListenerList;

import com.github.sarxos.webcam.Webcam;

import data.Data;

public class ImageProcessing implements Runnable, ThreadFactory {

	Thread thread;
	private Webcam webcam;
	private BufferedImage imageRef;
	private int seuil;
	protected TraitementImage ti = new TraitementImage();
	private Executor executor = Executors.newSingleThreadExecutor(this);
	private final EventListenerList listeners = new EventListenerList();

	public ImageProcessing(Webcam webcam) {
		this.webcam = webcam;
		seuil = 200;
		thread = new Thread(this);
	}

	public void initImageRef() {
		imageRef = webcam.getImage();
		if(Data.debugPicture){
			try {
				ImageIO.write(imageRef, "jpg", new File(
									Data.getImageDir()+"imageRef"+Data.getDate()+".jpg"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public Thread getThread() {
		return thread;
	}

	public Thread newThread(Runnable arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public void addMovementListener(MovementListener listener) {
		listeners.add(MovementListener.class, listener);
	}

	public MovementListener[] getMouvementListener() {
		return listeners.getListeners(MovementListener.class);
	}

	public void addMovement(List<FormObject> lf) {
		if(lf == null)
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

		while (true) {
			try {
				if (Data.tiDebug)
					System.out.println("lancement thread -----------------------------------------------------------------------");
				thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("commencement traitement image");
			image = webcam.getImage();
			List<FormObject> lf = ti.etiquetageIntuitifImageGiveList2(image,
					imageRef, seuil);
			addMovement(lf);
			try {
				//thread.wait(0);
				thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				ImageIO.write(image, "jpg", new File(
						Data.getImageDir()+"traitementImage"+Data.getDate()+".jpg"));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	public void begin() {
		System.out.println("Launch the Image Processing Thread");
		thread.start();

	}

}
