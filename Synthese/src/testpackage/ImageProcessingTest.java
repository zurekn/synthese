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
		System.out.println(Data.QRCamSeuil);
		System.out.println(Data.INIT_MAX_TIME);
		System.out.println(Data.MUSIC_VOLUM);
		System.out.println(Data.WAIT_TI);
	}
	
	
	
	
	
	public static void checkValuesIni(String filePath)
	{
		Scanner scanner;
		try {
			File file = new File(filePath);
			if(file.exists() && file.length() > 0)
			{
				System.out.println("length = "+file.length());
				scanner = new Scanner(file);	
				while (scanner.hasNextLine()) 
				{
				    String line = scanner.nextLine();
				    if(line.contains("=") && !line.contains("#"))
				    {
				    	try {
					    	line = line.replaceAll(" ", "");
					    	line = line.replaceAll(";", "");
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
					    	if(line.contains("qrcamseuil"))
					    		if(!line.substring(line.lastIndexOf("=")+1).equals(""))
					    			Data.QRCamSeuil = Integer.parseInt(line.substring(line.lastIndexOf("=")+1));
					    	if(line.contains("initmaxtime"))
					    		if(!line.substring(line.lastIndexOf("=")+1).equals(""))
					    			Data.INIT_MAX_TIME = Integer.parseInt(line.substring(line.lastIndexOf("=")+1));
					    	if(line.contains("musicvolum"))
					    		if(!line.substring(line.lastIndexOf("=")+1).equals(""))
					    			Data.MUSIC_VOLUM = Float.parseFloat(line.substring(line.lastIndexOf("=")+1));
					    	if(line.contains("gamewait"))
					    		if(!line.substring(line.lastIndexOf("=")+1).equals(""))
					    		{	
					    			int tempWait = Integer.parseInt(line.substring(line.lastIndexOf("=")+1));
					    			if(tempWait > 2000 && tempWait < 10000)
					    				Data.WAIT_TI  = tempWait;
					    		}
				    	} 
				    	catch (Exception e) {}
				    }
				}
				scanner.close();
			}
			else // file does not exist
			{
				System.out.println("fail------------------------------");
				try {
					file.createNewFile();
					FileWriter writer = new FileWriter(file, true);

					String texte = "seuilinit="+Data.SEUILINITTI+";";
					writer.write(texte,0,texte.length());
					writer.write("\r\n");
					
					texte = "seuiletiquetage="+Data.SEUILETI+";";
					writer.write(texte,0,texte.length());
					writer.write("\r\n");
					
					texte = "seuilmin="+Data.MIN_SEUIL_FORM+";";
					writer.write(texte,0,texte.length());
					writer.write("\r\n");
					
					texte = "seuilmax="+Data.MAX_SEUIL_FORM+";";
					writer.write(texte,0,texte.length());
					writer.write("\r\n");
					
					texte = "";
					writer.write(texte,0,texte.length());
					writer.write("\r\n");
					
					texte = "qrcamseuil="+Data.QRCamSeuil+";";
					writer.write(texte,0,texte.length());
					writer.write("\r\n");
					
					texte = "initmaxtime="+Data.INIT_MAX_TIME+";";
					writer.write(texte,0,texte.length());
					writer.write("\r\n");
					
					texte = "musicvolum="+Data.MUSIC_VOLUM+";";
					writer.write(texte,0,texte.length());
					writer.write("\r\n");
					
					texte = "#Attention, ce paramètre a un fort impact sur le jeu";
					writer.write(texte,0,texte.length());
					writer.write("\r\n");
					
					texte = "gamewait="+Data.WAIT_TI+";";
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
