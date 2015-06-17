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
		checkValuesIni("paramTI.ini");
		
		System.out.println(Data.SEUILINITTI);
		System.out.println(Data.SEUILETI);
		System.out.println(Data.MIN_SEUIL_FORM);
		System.out.println(Data.MAX_SEUIL_FORM);

	}
	
	
	
	
	
	public static void checkValuesIni(String filePath)
	{
		Scanner scanner;
		try {
			File file = new File(filePath);
			if(file.exists())
			{
				scanner = new Scanner(file);	
				while (scanner.hasNextLine()) 
				{
				    String line = scanner.nextLine();
				    if(line.contains("="))
				    {
				    	line = line.replaceAll(" ", "");
				    	if(line.contains("seuilinit"))
				    		if(!line.substring(line.lastIndexOf("=")+1).equals(""))
				    			Data.SEUILINITTI = Integer.parseInt(line.substring(line.lastIndexOf("=")+1));
				    	if(line.contains("seuiletiquetage"))
				    		if(!line.substring(line.lastIndexOf("=")+1).equals(""))
				    			Data.SEUILETI = Integer.parseInt(line.substring(line.lastIndexOf("=")+1));
				    	if(line.contains("seuilmin"))
				    		if(!line.substring(line.lastIndexOf("=")+1).equals(""))
				    			Data.MIN_SEUIL_FORM = Integer.parseInt(line.substring(line.lastIndexOf("=")+1));
				    	if(line.contains("seuilmax"))
				    		if(!line.substring(line.lastIndexOf("=")+1).equals(""))
				    			Data.MAX_SEUIL_FORM = Integer.parseInt(line.substring(line.lastIndexOf("=")+1));
				    }
				}
				scanner.close();
			}
			else // file does not exist
			{
				try {
					file.createNewFile();
					FileWriter writer = new FileWriter(file, true);

					String texte = "seuilinit="+Data.SEUILINITTI+"\n";
					writer.write(texte,0,texte.length());
					writer.write("\r\n");
					
					texte = "seuiletiquetage="+Data.SEUILETI;
					writer.write(texte,0,texte.length());
					writer.write("\r\n");
					
					texte = "seuilmin="+Data.MIN_SEUIL_FORM;
					writer.write(texte,0,texte.length());
					writer.write("\r\n");
					
					texte = "seuilmax="+Data.MAX_SEUIL_FORM;
					writer.write(texte,0,texte.length());
					writer.write("\r\n");
					
					writer.close(); // fermer le fichier à la fin des traitements					
				} 
				catch (IOException e) 
				{e.printStackTrace();} 
			}
		} 
		catch (FileNotFoundException e) 
		{e.printStackTrace();}
	}

}
