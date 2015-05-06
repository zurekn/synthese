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
//		List<FormObject> lf = ti.etiquetageIntuitifImageGiveList("toto.jpg", "toto.jpg",seuil);
		
		//////////////////////////////////test//////////////////////////////////

		
		BufferedImage imgCompare = null;
		BufferedImage imgSrcRef = null;
		try {
			imgCompare = ImageIO.read(new File(pathToDir + "dilatation.jpg"));
			imgSrcRef = ImageIO.read(new File(pathToDir + "testBloc.jpg"));
			
			List<FormObject> lf = ti.etiquetageIntuitifImage2(imgCompare, imgSrcRef ,100);
		} catch (IOException e) {}

		
		
	    ////////////////////////////////////////////////////////////////////////
		
			System.out.println(System.currentTimeMillis()-time + " end time");


	}

}
