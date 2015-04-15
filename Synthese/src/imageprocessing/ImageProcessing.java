package imageprocessing;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javax.swing.event.EventListenerList;

import com.github.sarxos.webcam.Webcam;

import data.Data;

public class ImageProcessing implements Runnable, ThreadFactory {

	private Webcam webcam;
	private BufferedImage imageRef;
	private int seuil;
	protected TraitementImage ti = new TraitementImage();
	private Executor executor = Executors.newSingleThreadExecutor(this);
	private final EventListenerList listeners = new EventListenerList();

	public ImageProcessing(Webcam webcam) {
		this.webcam = webcam;
		seuil = 200;
	}

	public void initImageRef() {
		imageRef = webcam.getImage();
	}

	public Thread newThread(Runnable arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public void addMovementListener(MovementListener listener){
		listeners.add(MovementListener.class, listener);
	}
	
	public MovementListener[] getMouvementListener(){
		return listeners.getListeners(MovementListener.class);
	}
	
	public void addMovement(List<FormObject> lf){

		for(FormObject o : lf){
			MovementEvent e = new MovementEvent(o.getBaryCenter().getX(), o.getBaryCenter().getY());
			for(MovementListener listener : getMouvementListener())
				listener.newMovement(e);
		}
		
	}
	
	public void run() {

		BufferedImage image;
		initImageRef();

		while (true) {
			try {
				executor.wait(Data.WAINTING_TIME);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			image = webcam.getImage();
			List<FormObject> lf = ti.etiquetageIntuitifImageGiveList2(image,
					imageRef, seuil);
			addMovement(lf);
		}
	}

}
