package testpackage;
import java.io.File;

import imageprocessing.Pixel;
import imageprocessing.TraitementImage;

import javax.swing.SwingUtilities;


public class ImageProcessingTest{
	
public static long time;
public static String fileName = "IN.jpg";
public static String outFileName = "OUT.jpg";
public static String pathToDir = "Synthese"+File.separator+"res"+File.separator+"testRes"+File.separator;

	public static void main(String[] args) {
		
//		SwingUtilities.invokeLater(new WebCamCapture());
		
		time = System.currentTimeMillis();
		
		TraitementImage ti = new TraitementImage();
//		int seuil = 110;
		Pixel p = ti.EtiquetageIntuitifImage(pathToDir+fileName, pathToDir+outFileName);
		System.out.println("pixel centre gravité : "+p.getX()+" "+p.getY());
		System.out.println(System.currentTimeMillis()-time);
	}

}
