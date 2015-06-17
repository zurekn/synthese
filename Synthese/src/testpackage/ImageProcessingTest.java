package testpackage;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

import imageprocessing.FormObject;
import imageprocessing.ImageProcessing;
import imageprocessing.Pixel;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

import data.Data;


public class ImageProcessingTest{
	
public static long time;
public static String fileName = "IN.jpg";
public static String outFileName = "OUT.jpg";
public static String pathToDir = "Synthese"+File.separator+"res"+File.separator+"testRes"+File.separator;
public static String urlImage = "C:/Users/frédéric/Desktop/eclipse/workspace/TraitementImages/res/init/debug/";

	public static void main(String[] args) {
		
//		SwingUtilities.invokeLater(new WebCamCapture());
		
		long time = System.currentTimeMillis();
		int seuil = 100;
//		ImageProcessing ti = new ImageProcessing();
//		List<FormObject> lf = ti.etiquetageIntuitifImageGiveList("toto.jpg", "toto.jpg",seuil);
		
		//////////////////////////////////test//////////////////////////////////

		
//		BufferedImage imgCompare = null;
//		BufferedImage imgSrcRef = null;
//		try {
//			imgCompare = ImageIO.read(new File(urlImage + "testInit.jpg"));
//			//imgSrcRef = ImageIO.read(new File(pathToDir + "testBloc.jpg"));
//			//ti.getOneGrayAndBinaryImage(imgCompare, seuil);
//			
//			List<FormObject> lf = ti.etiquetageIntuitifImageGiveList2(imgCompare, imgCompare ,seuil);
//			
//			System.out.println(System.currentTimeMillis()-time + " end time");
//		} catch (IOException e) {}

		
		
	    ////////////////////////////////////////////////////////////////////////
		
		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString();
		System.out.println("Current relative path is: " + s);


	}
	
	
	
	
	
	

}
