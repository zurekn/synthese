package imageprocessing;

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
import javax.swing.SwingUtilities;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamCompositeDriver;
import com.github.sarxos.webcam.WebcamResolution;
import com.github.sarxos.webcam.ds.buildin.WebcamDefaultDriver;

import data.Data;

import javax.swing.event.EventListenerList;

import org.omg.CORBA.OMGVMCID;

public class APIX implements Runnable {

	private String QRDatas;
	private QRCam qrcam;
	private ImageProcessing ip;
	private Webcam webcam;
	private final EventListenerList listeners = new EventListenerList();
	public static boolean isInit = false;
	private int relativeX = -1;
	private int relativeY = -1;
	private int seuil = 100;

	public APIX() {
		if (!Data.RUN_APIX)
			return;
		Dimension size = WebcamResolution.VGA.getSize();

		webcam = Webcam.getWebcams().get(0);
		// webcam.setViewSize(size);
		webcam.setCustomViewSizes(new Dimension[] { new Dimension(1920, 1080) });
		webcam.setViewSize(new Dimension(1920, 1080));
		webcam.open();
		System.out.println("Launch on the [" + webcam.getName() + "]");
		// Init the QRCode part
		qrcam = new QRCam(webcam);
		qrcam.addQRCodeListener(new QRCodeAdapter() {
			@Override
			public void newQRCode(QRCodeEvent e) {
				addQREvent(e);
			}
		});

		// init the ImageProcessing part

		ip = new ImageProcessing(webcam);
		ip.addMovementListener(new MovementAdapter() {

			public void newMovement(MovementEvent e) {

				addMovementEvent(new MovementEvent(e.getX() - relativeX, e
						.getY() - relativeY));
			}
		});

	}

	public void initTI() {
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
                elementsRes[x][y] =  elementsImg[x][y] < seuil ? 255 : 0;
               /*TODO
                * Ajouter une popup pour demander le seuil en cas d'échec de la phase d'initialisation
                */
                
            }
           
        }
//        // 4 boucles
        for (int x = 0; x < image.getWidth(); ++x)
        {
        	for (int y = 0; y < image.getHeight(); ++y)
            {
        		if(elementsRes[x][y]==255)
        			elementsRes[x][y] = 0;
        		else
        			break;
            }
        }
        for (int x = image.getWidth()-1; x > 0 ; --x)
        {
        	for (int y = 0; y < image.getHeight(); ++y)
            {
        		if(elementsRes[x][y]==255)
        			elementsRes[x][y] = 0;
        		else
        			break;
            }
        }
        
        for (int x = 0; x < image.getHeight(); ++x)
        {
        	for (int y = 0; y < image.getWidth(); ++y)
            {
        		if(elementsRes[y][x]==255)
        			elementsRes[y][x] = 0;
        		else
        			break;
            }
        }
        for (int x = image.getHeight()-1; x > 0 ; --x)
        {
        	for (int y = 0; y < image.getWidth(); ++y)
            {
        		if(elementsRes[y][x]==255)
        			elementsRes[y][x] = 0;
        		else
        			break;
            }
        }

		TraitementImage ti = new TraitementImage();
		//elementsRes = ti.Ouverture(elementsRes, 20);
        
        
         
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
								Data.IMAGE_DIR + "initialisationITout_bin"+Data.getDate()+".jpg"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		setRelativeValues(
				ip.ti.etiquetageIntuitifImageGiveList2(image1, image1, 200),
				image1.getHeight(), image1.getWidth());
		if(Data.debug)
			System.out.println("Relative position found at : ["+relativeX+":"+relativeY+"]");
		if (relativeX != -1){
			isInit = true;
			ip.begin();
		}
			
		System.out.println("Fin de la phase d'initialisation après "
				+ (System.currentTimeMillis() - time) + " millisecondes");
	}

	protected void addMovementEvent(MovementEvent e) {
		for (APIXListener listener : getAPIXListener())
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
	}

	public void setRelativeX(int valueX) {
		this.relativeX = valueX;
	}

	public void setRelativeY(int valueY) {
		this.relativeY = valueY;
	}

	public ImageProcessing getImageProcessing() {
		return ip;
		
	}

}
