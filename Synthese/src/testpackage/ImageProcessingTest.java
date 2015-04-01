package testpackage;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import imageprocessing.FormObject;
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
		int seuil = 200;
		
//		try {
//			img = ImageIO.read(new File(pathToDir + "testmouvement.jpg"));
//			imgVierge = ImageIO.read(new File(pathToDir + "testmouvement_vierge.jpg"));
//			
//			int[][] elementsSubImg = new int[img.getWidth()][img.getHeight()];
//			ti.setImgHeight(img.getHeight());
//		    ti.setImgWidth(img.getWidth());
//		    
//		    elementsSubImg = ti.getSubstractImg(img, imgVierge, seuil);
//		    
//		    ImageIO.write(ti.tableToBufferedImage(elementsSubImg), "jpg", new File(pathToDir + "toto.jpg"));
//		} 
//		catch (IOException e) {
//			e.printStackTrace();
//		}
//		ti.errosion("mini_carre.jpg", seuil, 1);
//		ti.dilatation("toto.jpg", seuil, 2);
//		ti.dilatation("toto2_errosion.jpg", seuil, 1);
	    ////////////////////////////////////////////////////////////////////////
		
//		int seuil = 110;
//		Pixel p = ti.EtiquetageIntuitifImage("Manathan.jpg", "Manathan_vide.jpg");
//		System.out.println("pixel centre gravité : "+p.getX()+" "+p.getY());
		
		List<FormObject> lf = ti.etiquetageIntuitifImage2("toto.jpg", "toto.jpg",seuil);
		
//		System.out.println(System.currentTimeMillis()-time + " end time");
	}

}
