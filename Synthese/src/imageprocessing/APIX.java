package imageprocessing;

import game.GameHandler;
import game.WindowGame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.DebugGraphics;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamCompositeDriver;
import com.github.sarxos.webcam.WebcamResolution;
import com.github.sarxos.webcam.ds.buildin.WebcamDefaultDriver;

import data.Data;
import data.Handler;

import javax.swing.event.EventListenerList;

import org.omg.CORBA.OMGVMCID;

import sun.java2d.opengl.WGLGraphicsConfig;

public class APIX extends Handler {

	private String QRDatas;
	private QRCam qrcam;
	private ImageProcessingHandler imageHandler;
	private Webcam webcam;
	private final EventListenerList listeners = new EventListenerList();
	public static boolean isInit = false;
	private int relativeX = -1;
	private int relativeY = -1;
	private int blockSizeX = -1;
	private int blockSizeY = -1;
	private int seuil = 100;
	private boolean firstTry = true;
	private boolean isRunning = false;
	
	private static APIX apix ;
	
	private APIX() {
		super();
		if (!Data.RUN_APIX){
			isInit = true;
			return;	
		}
		Dimension size = WebcamResolution.VGA.getSize();

		webcam = Webcam.getWebcams().get(0);
		// webcam.setViewSize(size);
		webcam.setCustomViewSizes(new Dimension[] { new Dimension(1920, 1080) });
		webcam.setViewSize(new Dimension(1920, 1080));
		webcam.open();
		System.out.println("Launch on the [" + webcam.getName() + "]");

	}
	
	public int getBlockSizeX() {
		return blockSizeX;
	}

	public void setBlockSizeX(int blockSizeX) {
		this.blockSizeX = blockSizeX;
	}

	public int getBlockSizeY() {
		return blockSizeY;
	}

	public void setBlockSizeY(int blockSizeY) {
		this.blockSizeY = blockSizeY;
	}

	public int getRelativeX() {
		return relativeX;
	}

	public int getRelativeY() {
		return relativeY;
	}

	public void run() {
		GameHandler game = WindowGame.getInstance().getHandler();
		lock(2);
		
		// Init the QRCode part
		qrcam = new QRCam(webcam);
		qrcam.addQRCodeListener(new QRCodeAdapter() {
			@Override
			public void newQRCode(QRCodeEvent e) {
				addQREvent(e);
			}
		});

		// init the ImageProcessing part

		imageHandler = ImageProcessingHandler.getInstance(webcam);
		imageHandler.addMovementListener(new MovementAdapter() {

			public void newMovement(MovementEvent e) {

				addMovementEvent(new MovementEvent(e.getX() - relativeX, e
						.getY() - relativeY));
			}
		});
		//imageHandler.begin();

		
		do{
			try {
				Thread.sleep(6000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//System.out.println("6 sec passé unlock !!");
			unlockTemporay(2);
		}while(true);
	}

	public void initTI() {
		if(isRunning)
			return;
		isRunning = true;
		System.out.println("Begin of the imageProcessing initialization");

		long time = System.currentTimeMillis();
		BufferedImage image = webcam.getImage();
		
		
//////////////////////////////////////////////////////////////////////////////////////////////////////	
		

        int[][] elementsImg = null;
        int[][] elementsRes = null;

        elementsImg = new int[image.getWidth()][image.getHeight()];
        elementsRes = new int[image.getWidth()][image.getHeight()];
        
        for (int x = 0; x < image.getWidth(); ++x){
            for (int y = 0; y < image.getHeight(); ++y)
            {
                /*    Get gray color from RGB origin pixel image 1    */
                Color pixelcolor1= new Color(image.getRGB(x, y));
                int r1=pixelcolor1.getRed();
                int g1=pixelcolor1.getGreen();
                int b1=pixelcolor1.getBlue();
                
                int grayLevel1 = (r1 + g1 + b1) / 3;
                elementsImg[x][y] = grayLevel1;

                /*        Binary pixel [x][y]        */
                //System.out.println(elementsImg[x][y]+" ");
                // 0 = white and 255 = black
                
                if(!firstTry)
                	seuil =Integer.parseInt( JOptionPane.showInputDialog("Nouveau seuil d'init [ancien : "+seuil+"]"));
                elementsRes[x][y] =  elementsImg[x][y] < seuil ? 255 : 0;
               firstTry = true;
                
            }
           
        }
//        // 4 boucles
        elementsRes = imageHandler.cutBlackBorder(elementsRes, image.getWidth(), image.getHeight());
        

		ImageProcessing ip = new ImageProcessing();
		elementsRes = ip.Ouverture(elementsRes, 20);
        
        
         
    		int imgWidth = elementsRes.length;
    		int imgHeight = elementsRes[0].length;
    		BufferedImage image1 = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
    		for(int i = 1; i < elementsRes.length; i++){
    			for(int j = 1; j < elementsRes[i].length; j++){
    				Color pixelColor= new Color(0);
    				if (elementsRes[i][j] == 0)
    					pixelColor = Color.WHITE;
    				else
    					pixelColor = Color.BLACK;
    				image1.setRGB(i, j, pixelColor.getRGB());
    			}
    		}	
// 
		
		
		
		
//////////////////////////////////////////////////////////////////////////////////////////////////////	
		
		
		if (Data.debug) {
			try {
				System.out.println("image high : " + image1.getHeight()+ " , image width : "+image1.getWidth() );
				ImageIO.write(
						image,
						"jpg",
						new File(
								Data.IMAGE_DIR + "initialisationIT"+Data.getDate()+".jpg"));
				ImageIO.write(
						image1,
						"jpg",
						new File(
								Data.IMAGE_DIR + "initialisationITout_bin_"+seuil+"_"+Data.getDate()+".jpg"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		setRelativeValues(
				imageHandler.ip.etiquetageIntuitifImageGiveList2(image1, image1, 200),
				image1.getHeight(), image1.getWidth());
		if(Data.debug)
			System.out.println("Relative position found at : ["+relativeX+":"+relativeY+"]");
		if (relativeX != -1){
			isInit = true;
			imageHandler.begin();
		}
			
		System.out.println("Fin de la phase d'initialisation après "
				+ (System.currentTimeMillis() - time) + " millisecondes");
		isRunning = false;
	}

	protected void addMovementEvent(MovementEvent e) {
		MovementEvent event = new MovementEvent(e.getX() - relativeX, e.getY() - relativeY);
		for (APIXListener listener : getAPIXListener())
			listener.newMouvement(event);
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
					HautGauche.add(formList);
				else
					BasGauche.add(formList);
			} else {
				if (tempY <= separationY)
					HautDroite.add(formList);
				else
					BasDroite.add(formList);
			}
		}
		System.out.println("BG = "+BasGauche.size()+", BD = "+BasDroite.size()+", HG = "+HautGauche.size()+", HD = "+HautDroite.size());

		if (HautGauche.size() == 0 && BasDroite.size() == 1
				&& (BasGauche.size() != 0 || HautDroite.size() != 0))
		{	pix = BasDroite.get(0).getBaryCenter();
		System.out.println("trouvé en haut gauche : " + pix.getX()+ "  "+pix.getY());
		}

		if (HautDroite.size() == 0 && BasGauche.size() == 1
				&& (BasDroite.size() != 0 || HautGauche.size() != 0))
		{	pix = BasGauche.get(0).getBaryCenter();
		System.out.println("trouvé en haut droite : " + pix.getX()+ "  "+pix.getY());
		}

		if (BasDroite.size() == 0 && HautGauche.size() == 1
				&& (BasGauche.size() != 0 || HautDroite.size() != 0))
		{	pix = HautGauche.get(0).getBaryCenter();
		System.out.println("trouvé en bas droite : " + pix.getX()+ "  "+pix.getY());
		}

		if (BasGauche.size() == 0 && HautDroite.size() == 1
				&& (BasDroite.size() != 0 || HautGauche.size() != 0))
		{pix = HautDroite.get(0).getBaryCenter();
		System.out.println("trouvé en bas gauche : " + pix.getX()+ "  "+pix.getY());
		}

		if (pix == null)
			return;

		setRelativeX(pix.getX());
		setRelativeY(pix.getY());
		blockSizeX =HautDroite.get(0).getBaryCenter().getX() - HautGauche.get(0).getBaryCenter().getX();
		blockSizeX = blockSizeX / Data.BLOCK_NUMBER_X;
		
		blockSizeY = BasGauche.get(0).getBaryCenter().getY() - HautGauche.get(0).getBaryCenter().getY();
		blockSizeY = blockSizeY / Data.BLOCK_NUMBER_Y;
		
	}

	public void setRelativeX(int valueX) {
		this.relativeX = valueX;
	}

	public void setRelativeY(int valueY) {
		this.relativeY = valueY;
	}

	public static APIX getInstance() {
		if(apix ==null)
			apix = new APIX();
		return apix;
		
	}

	@Override
	public void begin() {
		getThread().start();
		
	}

}
