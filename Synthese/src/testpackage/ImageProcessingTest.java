package testpackage;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import imageprocessing.Pixel;
import imageprocessing.TraitementImage;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;


public class ImageProcessingTest{
	
public static long time;
public static String fileName = "IN.jpg";
public static String outFileName = "OUT.jpg";
public static String pathToDir = "Synthese"+File.separator+"res"+File.separator+"testRes"+File.separator;

	public static void main(String[] args) {
		
//		SwingUtilities.invokeLater(new WebCamCapture());
		
//		long time = System.currentTimeMillis();
		TraitementImage ti = new TraitementImage();
		
		//////////////////////////////////test//////////////////////////////////
		String urlImage = "res"+File.separator;
		BufferedImage img, imgVierge;
		int seuil = 20;
		try {
			img = ImageIO.read(new File(urlImage + "premiere_im_refont.png"));
			imgVierge = ImageIO.read(new File(urlImage + "premiere_im_refont_vierge.png"));
			
			int[][] elementsSubImg = new int[img.getWidth()][img.getHeight()];
			ti.setImgHeight(img.getHeight());
		    ti.setImgWidth(img.getWidth());
		    
		    elementsSubImg = ti.getSubstractImg(img, imgVierge, seuil);
		    
		    ImageIO.write(ti.tableToBufferedImage(elementsSubImg), "png", new File(urlImage + "toto.png"));
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	    ////////////////////////////////////////////////////////////////////////
		
//		int seuil = 110;
//		Pixel p = ti.EtiquetageIntuitifImage("Manathan.jpg", "Manathan_vide.jpg");
//		System.out.println("pixel centre gravité : "+p.getX()+" "+p.getY());
		
//		List<FormObject> lf = ti.etiquetageIntuitifImage("testBlocPS.jpg", "testBlocBlanc.jpg");
//		System.out.println(System.currentTimeMillis()-time + " end time");
	}

}
