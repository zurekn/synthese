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
		
		long time = System.currentTimeMillis();
		int seuil = 200;
		TraitementImage ti = new TraitementImage();
//		List<FormObject> lf = ti.etiquetageIntuitifImage2("toto.jpg", "toto.jpg",seuil);
		
		//////////////////////////////////test//////////////////////////////////

		try {
			BufferedImage img = ImageIO.read(new File(pathToDir + "toto.jpg"));
			
			
			int [][] resErosion = ti.Ouverture(img, seuil);
			
			
			
			BufferedImage imgRes = ti.intTableToBufferedImage(resErosion);
			ImageIO.write(imgRes, "jpg", new File(pathToDir + "ouverture.jpg"));
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
		
	    ////////////////////////////////////////////////////////////////////////
		
		System.out.println(System.currentTimeMillis()-time + " end time");
	}

}
