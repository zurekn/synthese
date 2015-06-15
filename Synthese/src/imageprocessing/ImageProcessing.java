package imageprocessing;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import data.Data;


public class ImageProcessing {
	String urlImage = Data.getImageDir();//"Synthese"+File.separator+"res"+File.separator+"testRes"+File.separator;
	//String urlImage = "C:/Users/frédéric/Desktop/eclipse/workspace/TraitementImages/res/init/debug/";
	int imgHeight;
	int imgWidth;
	int minX; 
	int maxX; 
	int minY; 
	int maxY;
	public static final int MIN_SEUIL_FORM = 50;
	public static final int MAX_SEUIL_FORM = 5000;
	public static final int NIV_OUVERTURE = 5;

	public ImageProcessing() 
	{
		super();
		minX = 0; 
		maxX = 0; 
		minY = 0; 
		maxY = 0;
	}
	
	/*
	 * Génère et sauvegarde une image binaire à partire d'une image normale
	 */
	@Deprecated
	public void makeBinaryImage(BufferedImage img, String resultImgName, String formatSortie, int seuil)
	{
		try {
			if(Data.tiDebug)
				System.out.println("width " + img.getWidth() + " ::: height "+ img.getHeight());
//		    imgHeight = img.getHeight();
//		    imgWidth = img.getWidth();
		    
		    for (int i = 0; i < img.getWidth(); i++) {
		        for (int j = 0; j < img.getHeight(); j++) {
		        // recuperer couleur de chaque pixel
		        Color pixelcolor= new Color(img.getRGB(i, j));
		         
		        // recuperer les valeur rgb (rouge ,vert ,bleu) de cette couleur
		        int r=pixelcolor.getRed();
		        int g=pixelcolor.getGreen();
		        int b=pixelcolor.getBlue();
		        // faire le changement de couleur
		        if ( ((r+g+b)/3) > seuil)
		        {
			        pixelcolor = Color.WHITE;
		        }
		        else
		        {
			        pixelcolor = Color.BLACK;
		        }
		        
		        // changer la couleur de pixel avec la nouvelle couleur inversée
		        img.setRGB(i, j, pixelcolor.getRGB());		 
		        }
		    }
		    ImageIO.write(img, formatSortie, new File(urlImage + resultImgName));   
		} 
		catch (IOException e) {
			System.out.println("image not found");
		}
		if(Data.tiDebug)
			System.out.println("Done");
	}
	
	/*
	 * Test si les images sont égales.
	 * Retourne le nombre de pixels égaux
	 */
	@Deprecated
	public int pixelsBufferedImagesEqual(BufferedImage img1, BufferedImage img2)
	{
		int percentComparision = 0;
		int percentNotSame = 0;
		try 
		{
			BufferedImage imgRes = new BufferedImage(img1.getWidth(), img1.getHeight(), img1.getType()); 
		    if (img1.getWidth() == img2.getWidth() && img1.getHeight() == img2.getHeight()) {
		        for (int x = 0; x < img1.getWidth(); x++) {
		            for (int y = 0; y < img1.getHeight(); y++) {		            	
		            	//traitement de comparaison
		            	if (img1.getRGB(x, y) == img2.getRGB(x, y))
		            	{
		                	percentComparision++;
		                	//on met en blanc les pixels qui sont identiques
		                	int rgb=new Color(Math.abs(255),Math.abs(255),Math.abs(255)).getRGB();
		            		imgRes.setRGB(x, y, rgb);	                	
		                	
		                	/*
		                	 * créer une liste d'objet pixels avec :
		                	 * x
		                	 * y
		                	 * pour pouvoir recréer image si nécessaire
		                	 */
		            	}
		            	else
		            	{
		            		//int rgb=new Color(Math.abs(0),Math.abs(0),Math.abs(0)).getRGB();
		            		imgRes.setRGB(x, y, img1.getRGB(x, y));
		            		percentNotSame++;
		            	}
		            }
		        }
		        ImageIO.write(imgRes, "jpg", new File(urlImage + "lineTest.jpg"));
		        if(Data.tiDebug)
		        	System.out.println("there are "+percentNotSame+"px which are not same.");
		    } 
		} 
		catch (Exception e) 
		{
			if(Data.tiDebug)
				System.out.println("not enter in try of pixelsBufferedImagesEqual()");
			return 0;
		}
			return percentComparision;
	}
	
	/*
	 * Mise en place de l'algorithme d'étiquetage intuitif
	 */
	@Deprecated
	public List<FormObject> etiquetageIntuitifImage2(BufferedImage imgCompare, BufferedImage imgSrcRef, int seuil)
	{	
		int[][] subImgElements  = getGraySubstractAndBinaryImage(imgCompare, imgSrcRef, seuil);//getSubstractImg(imgCompare, imgSrcRef, seuil);
		int [][] etiquettes = new int[imgWidth][imgHeight];	
		
		//#debug
		if(countPixelsNotNull(subImgElements) == 0)
			subImgElements = getOneGrayAndBinaryImage(imgCompare, seuil);
		//#debug
		
		int attA, attB,attC, temp = 1, numEt = 1;
		List<Integer> T = new ArrayList<Integer>();
		List<ArrayList<Pixel>> Num = new ArrayList<ArrayList<Pixel>>();
		Num.add(new ArrayList<Pixel>());//pour etiquette 0
		if(subImgElements!=null)
		{
			for(int i = 1; i < imgWidth; i++)
			{
				for(int j = 1; j < imgHeight; j++)
				{
					if(subImgElements[i][j]== 255)
					{
						attA = subImgElements[i-1][j];
						attB = subImgElements[i][j-1];
						attC = subImgElements[i][j];					
	
						if((attC!=attA) && (attC!=attB))//si att(c) != att(a) et att(c) != att(b) => E(c) = nouvelle étiquette
						{
							etiquettes[i][j] = numEt;
							Num.add(new ArrayList<Pixel>());
							Num.get(numEt).add(new Pixel(j, i));
							T.add(temp);
							numEt++;
						}
			 			else if(attC == attA && attC != attB)//si att(c) = att(a) et att(c) != att(b) => E(c) = E(a)
						{	
			 				etiquettes[i][j] = etiquettes[i-1][j]; 
			 				Num.get(etiquettes[i][j]).add(new Pixel(j, i));
			 				temp++;
						}
						else if(attC != attA && attC == attB)//si att(c) != att(a) et att(c) = att(b) => E(c) = E(b)
						{
							etiquettes[i][j] = etiquettes[i][j-1];
							Num.get(etiquettes[i][j]).add(new Pixel(j, i));
							temp++;
						}
						else if(attC == attA && attC == attB && etiquettes[i-1][j]==etiquettes[i][j-1])//si att(c) = att(a) et att(c) != att(b)  et E(a) = E(b) => E(c) = E(a)
						{
							etiquettes[i][j] = etiquettes[i][j-1];
							Num.get(etiquettes[i][j]).add(new Pixel(j, i));
							temp++;
						}
						else if(attC == attA && attC == attB && etiquettes[i-1][j]!=etiquettes[i][j-1])	//si att(c) = att(a) et att(c) != att(b)  et E(a) = E(b) => E(c) = E(b) et on change toutes E(a) en E(b)
						{
							Num.get(etiquettes[i][j-1]).addAll(Num.get(etiquettes[i-1][j]));
							Num.get(etiquettes[i-1][j]).clear();
							
							etiquettes[i][j] = etiquettes[i][j-1];
							Num.get(etiquettes[i][j]).add(new Pixel(j, i));
							//System.out.println("position : [" +i+","+j +"] clear de l'etiquette courante c : " + etiquettes[i][j] + " , b : "+etiquettes[i][j-1] +" , a : "+etiquettes[i-1][j] );
							temp++;
				
							for(int x=0;x<=i;x++)
							{
								for(int y=0;y<=j;y++)
								{
									if(etiquettes[x][y]==etiquettes[i-1][j])
									{	
										etiquettes[x][y]=etiquettes[i][j-1];
										temp++;
									}
								}					
							}
						}
						else {
							if(Data.tiDebug)
								System.out.println("pas passé");
						}
					}
				}
			}
			List<FormObject> formList = new ArrayList<FormObject>();
			if(Data.tiDebug)
				System.out.println("num size = " +Num.size());
			for (ArrayList<Pixel> OneArray : Num) {
//				System.out.println("OneArray size = "+OneArray.size());
				if(OneArray.size()>50)
				{
//					System.out.println("gagné !!");

					FormObject myForm = new FormObject(OneArray, this.imgHeight, this.imgWidth);
//					display(myForm.getMatrix());
					formList.add(myForm);
					filtreSobel(myForm);
					myForm.findObjectType();
				}
			}
			if(Data.tiDebug)
				displayListForm(formList);
			return formList;
		}
		else
		{
			if(Data.tiDebug)
				System.out.println("fail");
			return null;
		}
	}
	
	/*
	 * Mise en place de l'algorithme d'étiquetage perso
	 */
	public List<FormObject> etiquetageIntuitifImageGiveList2(BufferedImage imgCompare, BufferedImage imgSrcRef, int seuil)
	{	
		// Ajout d'une vérification : si les images ne sont pas de la même taille, il ne peut y avoir de traitement !!!!!
		if (imgCompare.getWidth() != imgSrcRef.getWidth() || imgCompare.getHeight() != imgSrcRef.getHeight()) 
			return null;
		int[][] subImgElements  = getGraySubstractAndBinaryImage(imgCompare, imgSrcRef, seuil);//getSubstractImg(imgCompare, imgSrcRef, seuil);
		int [][] etiquettes = new int[imgWidth][imgHeight];
		
		//#debug
		if(countPixelsNotNull(subImgElements) == 0)
		{	
			if(APIX.isInit)
				return null;
			subImgElements = getOneGrayAndBinaryImage(imgCompare, seuil);
			
		}
		
		if(Data.debug)
		{
		    try
		    {	ImageIO.write(intTableToBinaryBufferedImage(subImgElements), "jpg", new File(urlImage + "imageSoustraction"+Data.getDate()
		    		+".jpg"));}
		    catch (IOException e) 
			{	e.printStackTrace();}   
		}
		//#debug
		if(Data.tiDebug)
			System.out.println("Ouverture sur image : subImgElements");
		
		if(APIX.isInit)
		{	
			subImgElements = Fermeture(subImgElements, NIV_OUVERTURE);
			subImgElements = Ouverture(subImgElements, NIV_OUVERTURE+5);
		}
		
		if(Data.tiDebug)
		{
			try
		    {	ImageIO.write(intTableToBinaryBufferedImage(subImgElements), "jpg", new File(urlImage + "imageApresFermeture&Ouverture"+Data.getDate()
		    		+".jpg"));}
		    catch (IOException e) 
			{	e.printStackTrace();} 
		}
		if(Data.tiDebug)
			System.out.println("début étiquettage sur image : subImgElements");
		
		int attA, attB,attC, temp = 1, numEt = 1;
		List<Integer> T = new ArrayList<Integer>();
		List<ArrayList<Pixel>> Num = new ArrayList<ArrayList<Pixel>>();
		Num.add(new ArrayList<Pixel>());//pour etiquette 0
		if(subImgElements!=null)
		{
			for(int i = 1; i < imgWidth; i++)
			{
				for(int j = 1; j < imgHeight; j++)
				{
					if(subImgElements[i][j]== 255)
					{
						attA = subImgElements[i-1][j];
						attB = subImgElements[i][j-1];
						attC = subImgElements[i][j];
	
						if((attC!=attA) && (attC!=attB))//si att(c) != att(a) et att(c) != att(b) => E(c) = nouvelle étiquette
						{
							etiquettes[i][j] = numEt;
							Num.add(new ArrayList<Pixel>());
							Num.get(numEt).add(new Pixel(i, j));
							T.add(temp);
							numEt++;
						}
			 			else if(attC == attA && attC != attB)//si att(c) = att(a) et att(c) != att(b) => E(c) = E(a)
						{	
			 				etiquettes[i][j] = etiquettes[i-1][j]; 
			 				Num.get(etiquettes[i][j]).add(new Pixel(i, j));
			 				// Si on dépasse la taille maximale d'une forme, on arrête le traitement
			 				// Cela signifie que on a détecté une main ou tout autre objet trop gros
			 				//if(Num.get(etiquettes[i][j]).size()>MAX_SEUIL_FORM)
			 				//	return null;
			 				temp++;
						}
						else if(attC != attA && attC == attB)//si att(c) != att(a) et att(c) = att(b) => E(c) = E(b)
						{
							etiquettes[i][j] = etiquettes[i][j-1];
							Num.get(etiquettes[i][j]).add(new Pixel(i, j));
							// Si on dépasse la taille maximale d'une forme, on arrête le traitement
			 				// Cela signifie que on a détecté une main ou tout autre objet trop gros
							//if(Num.get(etiquettes[i][j]).size()>MAX_SEUIL_FORM)
			 				//	return null;
							temp++;
						}
						else if(attC == attA && attC == attB && etiquettes[i-1][j]==etiquettes[i][j-1])//si att(c) = att(a) et att(c) != att(b)  et E(a) = E(b) => E(c) = E(a)
						{
							etiquettes[i][j] = etiquettes[i][j-1];
							Num.get(etiquettes[i][j]).add(new Pixel(i, j));
							// Si on dépasse la taille maximale d'une forme, on arrête le traitement
			 				// Cela signifie que on a détecté une main ou tout autre objet trop gros
							//if(Num.get(etiquettes[i][j]).size()>MAX_SEUIL_FORM)
	 						//	return null;
							temp++;
						}
						else if(attC == attA && attC == attB && etiquettes[i-1][j]!=etiquettes[i][j-1])	//si att(c) = att(a) et att(c) != att(b)  et E(a) = E(b) => E(c) = E(b) et on change toutes E(a) en E(b)
						{
							Num.get(etiquettes[i][j-1]).addAll(Num.get(etiquettes[i-1][j]));
							// Si on dépasse la taille maximale d'une forme, on arrête le traitement
			 				// Cela signifie que on a détecté une main ou tout autre objet trop gros
							//if(Num.get(etiquettes[i][j]).size()>MAX_SEUIL_FORM)
			 				//	return null;
							Num.get(etiquettes[i-1][j]).clear();
							
							etiquettes[i][j] = etiquettes[i][j-1];
							Num.get(etiquettes[i][j]).add(new Pixel(i, j));
							//System.out.println("position : [" +i+","+j +"] clear de l'etiquette courante c : " + etiquettes[i][j] + " , b : "+etiquettes[i][j-1] +" , a : "+etiquettes[i-1][j] );
							temp++;
				
							for(int x=0;x<=i;x++)
							{
								for(int y=0;y<=j;y++)
								{
									if(etiquettes[x][y]==etiquettes[i-1][j])
									{	
										etiquettes[x][y]=etiquettes[i][j-1];
										temp++;
									}
								}					
							}
						}
						else {
							if(Data.tiDebug)
								System.out.println("pas passé");
						}
					}
				}
			}
			List<FormObject> formList = new ArrayList<FormObject>();
			if(Data.tiDebug)
				System.out.println("Nombre Etiquettes = " +Num.size());
			for (ArrayList<Pixel> OneArray : Num) {
//				System.out.println("OneArray size = "+OneArray.size());
				if(OneArray.size() < MAX_SEUIL_FORM && OneArray.size() > MIN_SEUIL_FORM )
				{
//					System.out.println("gagné !!");

					FormObject myForm = new FormObject(OneArray, this.imgHeight, this.imgWidth);
//					display(myForm.getMatrix());
					formList.add(myForm);
					filtreSobel(myForm); //permet d'avoir le périmètre de l'objet
					myForm.findObjectType();
				}
			}
			if(Data.tiDebug)
				displayListForm(formList);
			return formList;
		}
		else
		{
			if(Data.tiDebug)
				System.out.println("fail");
			return null;
		}
	}
	
	public List<FormObject> etiquetageIntuitifImageGiveListOpti(BufferedImage imgCompare, BufferedImage imgSrcRef, int seuil, int min_X, int max_X, int min_Y, int max_Y)	
	{
		minX = min_X;
		maxX = max_X;
		minY = min_Y;
		maxY = max_Y;
		imgHeight = imgCompare.getHeight();
		imgWidth = imgCompare.getWidth();
		if(maxX <= 0 || maxX > imgWidth)
			maxX = imgWidth;
		if (maxY <= 0 || maxY > imgHeight)
			maxY = imgHeight;
		if(minX < 0)
			minX = 0;
		if(minY < 0)
			minY = 0;
		if (imgCompare.getWidth() != imgSrcRef.getWidth() || imgCompare.getHeight() != imgSrcRef.getHeight()) 
			return null;
		
		int[][] subImgElements  = getGraySubstractAndBinaryImageOpti(imgCompare, imgSrcRef, seuil);//getSubstractImg(imgCompare, imgSrcRef, seuil);
		int [][] etiquettes = new int[maxX][maxY];
		
		//#debug
		if(countPixelsNotNull(subImgElements) == 0)
		{	
			System.out.println("pixels number : " + countPixelsNotNull(subImgElements));
			if(!APIX.isInit)
				return null;
			subImgElements = getOneGrayAndBinaryImageOpti(imgCompare, seuil);
			
		}
		
		if(Data.tiDebug)
		{
		    try
		    {	ImageIO.write(intTableToBinaryBufferedImage(subImgElements), "jpg", new File(urlImage + "imageSoustraction"
		    		+".jpg"));}
		    catch (IOException e) 
			{	e.printStackTrace();}   
		}
		//#debug
		if(Data.tiDebug)
			System.out.println("Ouverture sur image : subImgElements");
		
		if(APIX.isInit)
		{	
			subImgElements = Fermeture(subImgElements, NIV_OUVERTURE);
			subImgElements = Ouverture(subImgElements, NIV_OUVERTURE+1);
		}
		
		if(Data.tiDebug)
		{
			try
		    {	ImageIO.write(intTableToBinaryBufferedImage(subImgElements), "jpg", new File(urlImage + "imageApresFermeture&Ouverture"
		    		+".jpg"));}
		    catch (IOException e) 
			{	e.printStackTrace();} 
		}
		if(Data.tiDebug)
			System.out.println("début étiquettage sur image : subImgElements");
		
		int attA, attB,attC, temp = 1, numEt = 1;
		List<Integer> T = new ArrayList<Integer>();
		List<ArrayList<Pixel>> Num = new ArrayList<ArrayList<Pixel>>();
		Num.add(new ArrayList<Pixel>());//pour etiquette 0
		if(subImgElements!=null)
		{
			for(int i = minX+1; i < maxX; i++)
			{
				for(int j = minY+1; j < maxY; j++)
				{
					if(subImgElements[i][j]== 255)
					{
						attA = subImgElements[i-1][j];
						attB = subImgElements[i][j-1];
						attC = subImgElements[i][j];
	
						if((attC!=attA) && (attC!=attB))//si att(c) != att(a) et att(c) != att(b) => E(c) = nouvelle étiquette
						{
							etiquettes[i][j] = numEt;
							Num.add(new ArrayList<Pixel>());
							Num.get(numEt).add(new Pixel(i, j));
							T.add(temp);
							numEt++;
						}
			 			else if(attC == attA && attC != attB)//si att(c) = att(a) et att(c) != att(b) => E(c) = E(a)
						{	
			 				etiquettes[i][j] = etiquettes[i-1][j]; 
			 				Num.get(etiquettes[i][j]).add(new Pixel(i, j));
			 				// Si on dépasse la taille maximale d'une forme, on arrête le traitement
			 				// Cela signifie que on a détecté une main ou tout autre objet trop gros
			 				//if(Num.get(etiquettes[i][j]).size()>MAX_SEUIL_FORM)
			 				//	return null;
			 				temp++;
						}
						else if(attC != attA && attC == attB)//si att(c) != att(a) et att(c) = att(b) => E(c) = E(b)
						{
							etiquettes[i][j] = etiquettes[i][j-1];
							Num.get(etiquettes[i][j]).add(new Pixel(i, j));
							// Si on dépasse la taille maximale d'une forme, on arrête le traitement
			 				// Cela signifie que on a détecté une main ou tout autre objet trop gros
							//if(Num.get(etiquettes[i][j]).size()>MAX_SEUIL_FORM)
			 				//	return null;
							temp++;
						}
						else if(attC == attA && attC == attB && etiquettes[i-1][j]==etiquettes[i][j-1])//si att(c) = att(a) et att(c) != att(b)  et E(a) = E(b) => E(c) = E(a)
						{
							etiquettes[i][j] = etiquettes[i][j-1];
							Num.get(etiquettes[i][j]).add(new Pixel(i, j));
							// Si on dépasse la taille maximale d'une forme, on arrête le traitement
			 				// Cela signifie que on a détecté une main ou tout autre objet trop gros
							//if(Num.get(etiquettes[i][j]).size()>MAX_SEUIL_FORM)
	 						//	return null;
							temp++;
						}
						else if(attC == attA && attC == attB && etiquettes[i-1][j]!=etiquettes[i][j-1])	//si att(c) = att(a) et att(c) != att(b)  et E(a) = E(b) => E(c) = E(b) et on change toutes E(a) en E(b)
						{
							Num.get(etiquettes[i][j-1]).addAll(Num.get(etiquettes[i-1][j]));
							// Si on dépasse la taille maximale d'une forme, on arrête le traitement
			 				// Cela signifie que on a détecté une main ou tout autre objet trop gros
							//if(Num.get(etiquettes[i][j]).size()>MAX_SEUIL_FORM)
			 				//	return null;
							Num.get(etiquettes[i-1][j]).clear();
							
							etiquettes[i][j] = etiquettes[i][j-1];
							Num.get(etiquettes[i][j]).add(new Pixel(i, j));
							//System.out.println("position : [" +i+","+j +"] clear de l'etiquette courante c : " + etiquettes[i][j] + " , b : "+etiquettes[i][j-1] +" , a : "+etiquettes[i-1][j] );
							temp++;
				
							for(int x=0;x<=i;x++)
							{
								for(int y=0;y<=j;y++)
								{
									if(etiquettes[x][y]==etiquettes[i-1][j])
									{	
										etiquettes[x][y]=etiquettes[i][j-1];
										temp++;
									}
								}					
							}
						}
						else {
							if(Data.tiDebug)
								System.out.println("pas passé");
						}
					}
				}
			}
			List<FormObject> formList = new ArrayList<FormObject>();
			if(Data.tiDebug)
				System.out.println("Nombre Etiquettes = " +Num.size());
			for (ArrayList<Pixel> OneArray : Num) {
				System.out.println("OneArray size = "+OneArray.size());
				if(OneArray.size() > MIN_SEUIL_FORM && OneArray.size() < MAX_SEUIL_FORM)
				{
//					System.out.println("gagné !!");

					FormObject myForm = new FormObject(OneArray, this.imgHeight, this.imgWidth);
//					display(myForm.getMatrix());
					formList.add(myForm);
					filtreSobel(myForm); //permet d'avoir le périmètre de l'objet
					myForm.findObjectType();
				}
			}
			if(Data.tiDebug)
				displayListForm(formList);
			return formList;
		}
		else
		{
			if(Data.tiDebug)
				System.out.println("fail");
			return null;
		}
	}
	
	/*
	 * fonction permettant de faire une érosion sur une forme (forme noir sur fond blanc)
	 */
	public int[][] erosion(int[][] erosionElements) 
	{

		int height = erosionElements[0].length;
		int width = erosionElements.length;
		int [][] resultErrosion = new int[width][height];
		for(int i = 0; i < width; i++)
			resultErrosion[i][0] = 0;
		for(int j = 0; j < height; j++)
			resultErrosion[0][j] = 0;
		
		int up, down, right, left, upRight, upLeft, downRight, downLeft;
		if(erosionElements!=null)
		{
			for(int i = 1; i < width-1; i++)
			{
				for(int j = 1; j < height-1; j++)
				{
					left = erosionElements[i-1][j];
					up = erosionElements[i][j-1];
					right = erosionElements[i+1][j];
					down = erosionElements[i][j+1];
					upRight = erosionElements[i+1][j-1];
				    upLeft = erosionElements[i-1][j-1];
				    downRight = erosionElements[i+1][j+1];
				    downLeft = erosionElements[i-1][j+1];
				    
				    if( up != 0 && down != 0 && left != 0 && right != 0 && upRight != 0 && upLeft != 0 && downRight != 0 && downLeft != 0 )
						resultErrosion[i][j] = 255;
					else
						resultErrosion[i][j] = 0;
				}
			}
		} 
		return resultErrosion;
	}
	
	/*
	 * fonction permettant de faire une dilatation sur une forme (forme noir sur fond blanc)
	 */
	public int[][] dilatation(int[][] dilatationElements) 
	{

		int height = dilatationElements[0].length;
		int width = dilatationElements.length;
		int [][] resultDilatation = new int[width][height];
		for(int i = 0; i < width; i++)
			resultDilatation[i][0] = 0;
		for(int j = 0; j < height; j++)
			resultDilatation[0][j] = 0;
		
		int up, down, right, left, upRight, upLeft, downRight, downLeft;
		if(dilatationElements!=null)
		{
			for(int i = 1; i < width-1; i++)
			{
				for(int j = 1; j < height-1; j++)
				{
					up = dilatationElements[i][j-1];
				    down = dilatationElements[i][j+1];
				    right = dilatationElements[i+1][j];
				    left = dilatationElements[i-1][j];
				    upRight = dilatationElements[i+1][j-1];
				    upLeft = dilatationElements[i-1][j-1];
				    downRight = dilatationElements[i+1][j+1];
				    downLeft = dilatationElements[i-1][j+1];

					if( up != 0 || down != 0 || left != 0 || right != 0 || upRight != 0 || upLeft != 0 || downRight != 0 || downLeft != 0 )
						resultDilatation[i][j] = 255;
					else
						resultDilatation[i][j] = 0;
				}
			}
		} 
		return resultDilatation;
	}

	/*
	 * fonction permettant de faire une érosion puis une dilatation sur une forme
	 */
	public int[][] Ouverture(int[][] elemOuverture, int seuil) 
	{
		//int [][] elemOuverture = getBinaryImage(elemOuverture, seuil);		
		int i;
		int [][] resOuverture = elemOuverture;
		for (i=0 ; i<seuil ; i++)
			resOuverture = erosion(resOuverture);
		for (i=0 ; i<seuil ; i++)
			resOuverture = dilatation(resOuverture);
		
		return resOuverture;
	}
	
	/*
	 * fonction permettant de faire une dilatation puis une érosion sur une forme
	 */
	public int[][] Fermeture(int [][] elemFermeture, int seuil) 
	{
		//int [][] elemFermeture = getBinaryImage(img, seuil);		
		int i;
		int [][] resFermeture = elemFermeture;
		for (i=0 ; i<seuil ; i++)
			resFermeture = dilatation(resFermeture);
		for (i=0 ; i<seuil ; i++)
			resFermeture = erosion(resFermeture);
		
		return resFermeture;
	}
	
	/*
	 * Filtre de Sobel
	 */
	public void filtreSobel(FormObject myForm)
    {
        int [][] pixel = new int[imgWidth][imgHeight];
        int [][] decalagePixel = new int[imgWidth][imgHeight];
        int [][] outPixel = new int[imgWidth][imgHeight];
        pixel = myForm.matrix;
        int x,y,g;

		//*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
		// Filtrage de Sobel
		for (int i = 1; i < imgWidth-2; i++){ // mettre -1 à -2 si utilisation ancienne version
			for (int j = 1; j < imgHeight-2; j++){
				// ancienne version
				x = (pixel[i][j+2]+2*pixel[i+1][j+2]+pixel[i+2][j+2])-(pixel[i][j]+2*pixel[i+1][j]+pixel[i+2][j]);
				y = (pixel[i+2][j]+2*pixel[i+2][j+1]+pixel[i+2][j+2])-(pixel[i][j]+2*pixel[i][j+1]+pixel[i][j+2]);
//				x = (pixel[i][j+1]+2*pixel[i+1][j+1]+pixel[i+1][j+1])-(pixel[i][j]+2*pixel[i+1][j]+pixel[i+1][j]);
//				y = (pixel[i+1][j]+2*pixel[i+1][j+1]+pixel[i+1][j+1])-(pixel[i][j]+2*pixel[i][j+1]+pixel[i][j+1]);
				g=Math.abs(x)+Math.abs(y);    
				//System.out.println(g);
				pixel[i][j]=g;
			}
		}
		
		// mes tests
		int perimetre = 0;
		for (int i = 2; i < imgWidth; i++){
			for (int j = 2; j < imgHeight; j++){
				decalagePixel[i][j] = pixel[i-1][j-1];
				if(myForm.matrix[i][j]>0 && decalagePixel[i][j]>0){
					perimetre ++;
					outPixel[i][j] = 255;
				}
				else{
					outPixel[i][j] = 0;
				}
			}
		}
		if(Data.tiDebug)
			System.out.println("le périmètre est de : " + perimetre);
		// fin de mes tests
		if(Data.tiDebug)
			System.out.println("fin sobel");
		myForm.setPerimeter(perimetre);
    }

	/*
	 * compte le nombre de pixels non nulls
	 */
	private int countPixelsNotNull(int [][]myMatrix)
	{
		int nbNotNull = 0;
		for(int i = 0; i < myMatrix.length; i++)
		{
			for(int j = 0; j < myMatrix[i].length; j++)
			{
				if(myMatrix[i][j]!=0)
					nbNotNull++;
			}
		}
		return nbNotNull;
	}

	/*
	 * Affichage d'une matrice
	 */
	private void display(int[][] myMatrix) 
	{
		System.out.println("matrix width = "+ myMatrix.length + "matrix height = "+myMatrix[0].length);
		for(int j = 0; j < myMatrix[0].length; j++)
		{
			for(int i = 0; i < myMatrix.length; i++)
			{
				System.out.print(myMatrix[i][j]+" ");
			}
			System.out.println();
		}
	}
		
	/*
	 * Affiche tous les éléments d'une liste de FormObject
	 */
	private void displayListForm(List<FormObject> myList) 
	{
		System.out.println("test");
		System.out.println(" Nous avons trouvé : " + myList.size() +" objets");
		if (myList.size() > 0)
			System.out.println("Dont les caractéristiques sont :");
		for (FormObject pixel : myList) {
			//System.out.println("form gravity center : " + pixel.getGravityCenter().getX() + " : "+pixel.getGravityCenter().getY());
			System.out.println("bary center :  " + pixel.getBaryCenter().getX() + " : " + pixel.getBaryCenter().getY());
			System.out.println("ecart type : " + pixel.sigmaX + " : " + pixel.sigmaY);
		}
	}
	
	//********************************** Getters & Setters ************************************//
	/*
	 * Retourne une matrice représentant l'image binarisée mise en paramètre
	 */
	public int[][] getBinaryImage(BufferedImage img, int seuil)
	{
		int[][] elements = null;
		if(Data.tiDebug)
			System.out.println("width " + img.getWidth() + " ::: height "+ img.getHeight());
		elements = new int[img.getWidth()][img.getHeight()];
		
		for (int i=0;i<img.getWidth();i++)
			elements[i][0]=0;

		for (int i=0;i<img.getHeight();i++)
			elements[0][i]=0;
		
		for (int i = 1; i < img.getWidth(); i++) {
		    for (int j = 1; j < img.getHeight(); j++) {
		    // recuperer couleur de chaque pixel
		    Color pixelcolor= new Color(img.getRGB(i, j));
		    // recuperer les valeur rgb (rouge ,vert ,bleu) de cette couleur
		    int r=pixelcolor.getRed();
		    int g=pixelcolor.getGreen();
		    int b=pixelcolor.getBlue();
		    
		    
		    if ( ((r+g+b)/3) > seuil)//blanc
		    	elements[i][j]=0;
		    else //noir
		    	elements[i][j]=255;		 
		    }
		}
		return elements;
	}
	
	/*
	 * Retourne une matrice des éléments résultants de la soustraction de deux images
	 */
	public int[][] getSubstractImg(BufferedImage imgToCompare, BufferedImage imgRef, int seuil)
	{
		int percentComparision = 0;
		int percentNotSame = 0;
    	int[][] elementsFirstImg = null;
		int[][] elementsSecondImg = null;
		int[][] elementsSubImg = null;
					
	    if (imgToCompare.getWidth() == imgRef.getWidth() && imgToCompare.getHeight() == imgRef.getHeight()) {

			elementsFirstImg = new int[imgToCompare.getWidth()][imgToCompare.getHeight()];
			elementsFirstImg = getBinaryImage(imgToCompare, seuil);
			elementsSecondImg = new int[imgToCompare.getWidth()][imgToCompare.getHeight()];;
			elementsSecondImg = getBinaryImage(imgRef, seuil);
			elementsSubImg = new int[imgToCompare.getWidth()][imgToCompare.getHeight()];
			
			for (int x = 0; x < imgToCompare.getWidth(); x++) {
	            for (int y = 0; y < imgToCompare.getHeight(); y++) {		            	
	            	//traitement de comparaison
	            	if (elementsFirstImg[x][y] == elementsSecondImg[x][y])
	            	{
	                	percentComparision++;
	            	}
	            	else
	            	{	
	            		elementsSubImg [x][y] = elementsSecondImg[x][y];
	            		percentNotSame++;
	            	}
	            }
	        }
			//Display(elementsSubImg);
			if(Data.tiDebug)
				System.out.println("there are "+percentNotSame+"px which are not same and "+percentComparision+" which are same.");
	    } 
		return elementsSubImg;
	}
	
	/*
     * Transform one RGB image in Gray image,
     * Binary each pixels from image.
     */
	public int[][] getOneGrayAndBinaryImage(BufferedImage image, int seuil) 
    {
    	if(Data.tiDebug)
    		System.out.println("les deux images sont identiques");
        //int[][] elementsImg = null;
        int[][] elementsRes = null;
        BufferedImage imgRes = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
        BufferedImage imgRes_Bin = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
        imgHeight = image.getHeight();
        imgWidth = image.getWidth();
       // elementsImg = new int[image.getWidth()][image.getHeight()];
        elementsRes = new int[image.getWidth()][image.getHeight()];
        
        for (int x = 0; x < image.getWidth(); ++x)
            for (int y = 0; y < image.getHeight(); ++y)
            {                
                int rgb = image.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = (rgb & 0xFF);

                int grayLevel = (r + g + b) / 3;
                elementsRes[x][y] = grayLevel ;
                imgRes.setRGB(x, y, (grayLevel << 16) + (grayLevel << 8) + grayLevel);

                /*       Affect Binary value to a pixel [x][y]        */
                elementsRes[x][y] = elementsRes[x][y] < (seuil) ? 255 : 0;
                
                //imgRes_Bin.setRGB(x, y, elementsRes[x][y]);
            }
		for(int i = 1; i < elementsRes.length; i++)
		{
			for(int j = 1; j < elementsRes[i].length; j++)
			{
				//Color pixelColor= new Color(0);
				if (elementsRes[i][j] == 0)
					imgRes_Bin.setRGB(i, j, ((255 << 16) + (255 << 8) + 255));//pixelColor = Color.WHITE;
				else
					imgRes_Bin.setRGB(i, j, ((0 << 16) + (0 << 8) + 0));//pixelColor = Color.BLACK;
				//imgRes_Bin.setRGB(i, j, pixelColor.getRGB());
			}
		}
		
        /*	fin ajout */
        try 
        {
			ImageIO.write(imgRes, "jpg", new File(urlImage + "test_getOneGrayImage_NG"+Data.getDate()+".jpg"));
			ImageIO.write(imgRes_Bin, "jpg", new File(urlImage + "test_getOneGrayImage_Bin"+Data.getDate()+".jpg"));
			//ImageIO.write(imgRes_Bin_ouverture, "jpg", new File(urlImage + "test_getOneGrayImage_Bin_ouverture"+Data.getDate()+".jpg"));
		} 
        catch (IOException e) 
        {
			e.printStackTrace();
		}
        return elementsRes;
    }
    
	public int[][] getOneGrayAndBinaryImageOpti(BufferedImage image, int seuil) 
    {
        //int[][] elementsImg = null;
        int[][] elementsRes = null;
        BufferedImage imgRes = new BufferedImage(maxX, maxY, BufferedImage.TYPE_INT_RGB);
        BufferedImage imgRes_Bin = new BufferedImage(maxX, maxY, BufferedImage.TYPE_INT_RGB);
        //imgHeight = image.getHeight();
        //imgWidth = image.getWidth();
       // elementsImg = new int[image.getWidth()][image.getHeight()];
        elementsRes = new int[maxX][maxY];
        
        for (int x = minX; x < maxX; ++x)
		    for (int y = minY; y < maxY; ++y)
		    {                
                int rgb = image.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = (rgb & 0xFF);

                int grayLevel = (r + g + b) / 3;
                elementsRes[x][y] = grayLevel ;
                imgRes.setRGB(x, y, (grayLevel << 16) + (grayLevel << 8) + grayLevel);

                /*       Affect Binary value to a pixel [x][y]        */
                elementsRes[x][y] = elementsRes[x][y] < (seuil) ? 255 : 0;
                
                //imgRes_Bin.setRGB(x, y, elementsRes[x][y]);
            }
        System.out.println("Les images sont identiques : début binarisation");
		for(int i = 1; i < elementsRes.length; i++)
		{
			for(int j = 1; j < elementsRes[i].length; j++)
			{
				//Color pixelColor= new Color(0);
				if (elementsRes[i][j] == 0)
					imgRes_Bin.setRGB(i, j, ((255 << 16) + (255 << 8) + 255));//pixelColor = Color.WHITE;
				else
					imgRes_Bin.setRGB(i, j, ((0 << 16) + (0 << 8) + 0));//pixelColor = Color.BLACK;
				//imgRes_Bin.setRGB(i, j, pixelColor.getRGB());
			}
		}
		
        /*	fin ajout */
        try 
        {
			ImageIO.write(imgRes, "jpg", new File(urlImage + "test_getOneGrayImage_NG.jpg"));
			ImageIO.write(imgRes_Bin, "jpg", new File(urlImage + "test_getOneGrayImage_Bin.jpg"));
			//ImageIO.write(imgRes_Bin_ouverture, "jpg", new File(urlImage + "test_getOneGrayImage_Bin_ouverture"+Data.getDate()+".jpg"));
		} 
        catch (IOException e) 
        {
			e.printStackTrace();
		}
        return elementsRes;
    }
	
    /*
     * Transform one RGB image in Gray image,
     * Binary each pixels from image.
     */
    public void getOneGrayImage(BufferedImage img) 
    {
    	for (int x = 0; x < img.getWidth(); ++x)
    	{
    	    for (int y = 0; y < img.getHeight(); ++y)
    	    {
    	        int rgb = img.getRGB(x, y);
    	        int r = (rgb >> 16) & 0xFF;
    	        int g = (rgb >> 8) & 0xFF;
    	        int b = (rgb & 0xFF);

    	        int grayLevel = (r + g + b) / 3;
    	        int gray = (grayLevel << 16) + (grayLevel << 8) + grayLevel; 
    	        img.setRGB(x, y, grayLevel);
    	        
    	        /*
    	         * Autre version plus lente mais utilisant des objets
    	        Color pixelcolor= new Color(img.getRGB(x, y));
    	        //int rgb = img.getRGB(x, y);
    	        int r = pixelcolor.getRed();
    	        int g = pixelcolor.getGreen();
    	        int b = pixelcolor.getBlue();

    	        int grayLevel = (r + g + b) / 3;
    	        Color pixel = new Color(grayLevel, grayLevel, grayLevel);
    	        img.setRGB(x, y, pixel.getRGB());
    	         */
    	    }
    	}
    	try {
			ImageIO.write(img, "jpg", new File(urlImage + "gray50"+Data.getDate()+".jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
	/*
	 * Transform two images RGB in Gray images,
	 * Substract them,
	 * Binary each pixels from substract image.
	 */
    public int[][] getGraySubstractAndBinaryImage(BufferedImage img1, BufferedImage img2, int seuil) 
	{
    	if(Data.tiDebug)
    		System.out.println("mise en NG, soustraction et binarisation de la soustraction, de deux images");
		//int[][] elements1 = null;
		//int[][] elements2 = null;
		int[][] elementsRes = null;

		imgHeight = img1.getHeight();
		imgWidth = img1.getWidth();
		//BufferedImage imgRes_1 = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
		//BufferedImage imgRes_2 = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
		BufferedImage imgRes_Sub= new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
		BufferedImage imgRes_Bin= new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
		//elements1 = new int[img1.getWidth()][img1.getHeight()];
		//elements2 = new int[img1.getWidth()][img1.getHeight()];
		elementsRes = new int[img1.getWidth()][img1.getHeight()];
		
		if (img1.getWidth() == img2.getWidth() && img1.getHeight() == img2.getHeight()) 
		{
		    for (int x = 0; x < img1.getWidth(); ++x)
			    for (int y = 0; y < img1.getHeight(); ++y)
			    {
			    	int rgb = img1.getRGB(x, y);
	                int r = (rgb >> 16) & 0xFF;
	                int g = (rgb >> 8) & 0xFF;
	                int b = (rgb & 0xFF);

	                /* Mise en nuance de gris des images
	                int grayLevel = (r + g + b) / 3;
	                elements1[x][y] = (grayLevel << 16) + (grayLevel << 8) + grayLevel;
	                imgRes_1.setRGB(x, y, elements1[x][y]);
	               */
	                
	                int rgb2 = img2.getRGB(x, y);
	                int r2 = (rgb2 >> 16) & 0xFF;
	                int g2 = (rgb2 >> 8) & 0xFF;
	                int b2 = (rgb2 & 0xFF);

	                /* Mise en nuance de gris des images
	                int grayLevel2 = (r2 + g2 + b2) / 3;
	                elements2[x][y] = (grayLevel2 << 16) + (grayLevel2 << 8) + grayLevel2;
	                imgRes_2.setRGB(x, y, elements2[x][y]);
	                */
			        
	                /*		Substract images		*/               
	                elementsRes[x][y] = ((r-r2 < 0 ? r2-r : r-r2)  +
	                					(g-g2<0?g2-g:g-g2) + 
	                					(b-b2<0?b2-b:b-b2))/3;
	                /*					
	                int graylevel1 = (r + g + b) / 3;
	                int graylevel2 = (r2 + g2 + b2) / 3;
	                elementsRes[x][y] = (graylevel1 - graylevel2) < 0 ? (graylevel2 - graylevel1) : (graylevel1 - graylevel2);
	                */
	                imgRes_Sub.setRGB(x, y, (elementsRes[x][y]));//<<16+elementsRes[x][y]<<8+elementsRes[x][y]));
	        
			        /*		Binary pixel [x][y]		*/
			        elementsRes[x][y] = elementsRes[x][y] > (seuil) ? 255 : 0;
			        
   
			    }
		      System.out.println("Les images ne sont pas identiques : début binarisation");
		    for(int i = 1; i < elementsRes.length; i++)
			{
				for(int j = 1; j < elementsRes[i].length; j++)
				{
					
					//Color pixelColor= new Color(0);
					if (elementsRes[i][j] == 0)
						imgRes_Bin.setRGB(i, j, ((255 << 16) + (255 << 8) + 255));//pixelColor = Color.WHITE;
					else
						imgRes_Bin.setRGB(i, j, ((0 << 16) + (0 << 8) + 0));//pixelColor = Color.BLACK;
					//imgRes_Bin.setRGB(i, j, pixelColor.getRGB());
				}
			}
			}
			else
				System.out.println("images non équivalentes en taille. Dommage!");
		 try {
				//ImageIO.write(imgRes_1, "jpg", new File(urlImage + "test_getGrayImage_Res_1.jpg"+Data.getDate()+""));
				//ImageIO.write(imgRes_2, "jpg", new File(urlImage + "test_getGrayImage_Res_2.jpg"+Data.getDate()+""));
				ImageIO.write(imgRes_Sub, "jpg", new File(urlImage + "test_getGrayImage_Res_Sub"+Data.getDate()+".jpg"));
				ImageIO.write(imgRes_Bin, "jpg", new File(urlImage + "test_getGrayImage_Res_Bin"+Data.getDate()+".jpg"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		return elementsRes;
	}
		
    public int[][] getGraySubstractAndBinaryImageOpti(BufferedImage img1, BufferedImage img2, int seuil) 
	{
    	if(Data.tiDebug)
    		System.out.println("mise en NG, soustraction et binarisation de la soustraction, de deux images");
		//int[][] elements1 = null;
		//int[][] elements2 = null;
		int[][] elementsRes = null;

		//BufferedImage imgRes_1 = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
		//BufferedImage imgRes_2 = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
		BufferedImage imgRes_Sub= new BufferedImage(maxX, maxY, BufferedImage.TYPE_INT_RGB);
		BufferedImage imgRes_Bin= new BufferedImage(maxX, maxY, BufferedImage.TYPE_INT_RGB);
		//elements1 = new int[img1.getWidth()][img1.getHeight()];
		//elements2 = new int[img1.getWidth()][img1.getHeight()];
		elementsRes = new int[maxX][maxY];
		
		if (img1.getWidth() == img2.getWidth() && img1.getHeight() == img2.getHeight()) 
		{
		    for (int x = minX; x < maxX; ++x)
			    for (int y = minY; y < maxY; ++y)
			    {
			    	int rgb = img1.getRGB(x, y);
	                int r = (rgb >> 16) & 0xFF;
	                int g = (rgb >> 8) & 0xFF;
	                int b = (rgb & 0xFF);

	                /* Mise en nuance de gris des images
	                int grayLevel = (r + g + b) / 3;
	                elements1[x][y] = (grayLevel << 16) + (grayLevel << 8) + grayLevel;
	                imgRes_1.setRGB(x, y, elements1[x][y]);
	               */
	                
	                int rgb2 = img2.getRGB(x, y);
	                int r2 = (rgb2 >> 16) & 0xFF;
	                int g2 = (rgb2 >> 8) & 0xFF;
	                int b2 = (rgb2 & 0xFF);

	                /* Mise en nuance de gris des images
	                int grayLevel2 = (r2 + g2 + b2) / 3;
	                elements2[x][y] = (grayLevel2 << 16) + (grayLevel2 << 8) + grayLevel2;
	                imgRes_2.setRGB(x, y, elements2[x][y]);
	                */
			        
	                /*		Substract images		*/               
	                elementsRes[x][y] = ((r-r2 < 0 ? r2-r : r-r2)  +
	                					(g-g2<0?g2-g:g-g2) + 
	                					(b-b2<0?b2-b:b-b2))/3;
	                /*					
	                int graylevel1 = (r + g + b) / 3;
	                int graylevel2 = (r2 + g2 + b2) / 3;
	                elementsRes[x][y] = (graylevel1 - graylevel2) < 0 ? (graylevel2 - graylevel1) : (graylevel1 - graylevel2);
	                */
	                imgRes_Sub.setRGB(x, y, (elementsRes[x][y]));//<<16+elementsRes[x][y]<<8+elementsRes[x][y]));
	        
			        /*		Binary pixel [x][y]		*/
			        elementsRes[x][y] = elementsRes[x][y] > (seuil) ? 255 : 0;
			        
   
			    }
		      System.out.println("Les images ne sont pas identiques : début binarisation");
		    for(int i = 1; i < elementsRes.length; i++)
			{
				for(int j = 1; j < elementsRes[i].length; j++)
				{
					
					//Color pixelColor= new Color(0);
					if (elementsRes[i][j] == 0)
						imgRes_Bin.setRGB(i, j, ((255 << 16) + (255 << 8) + 255));//pixelColor = Color.WHITE;
					else
						imgRes_Bin.setRGB(i, j, ((0 << 16) + (0 << 8) + 0));//pixelColor = Color.BLACK;
					//imgRes_Bin.setRGB(i, j, pixelColor.getRGB());
				}
			}
			}
			else
				System.out.println("images non équivalentes en taille. Dommage!");
		 try {
				//ImageIO.write(imgRes_1, "jpg", new File(urlImage + "test_getGrayImage_Res_1.jpg"+Data.getDate()+""));
				//ImageIO.write(imgRes_2, "jpg", new File(urlImage + "test_getGrayImage_Res_2.jpg"+Data.getDate()+""));
				ImageIO.write(imgRes_Sub, "jpg", new File(urlImage + "test_getGrayImage_Res_Sub.jpg"));
				ImageIO.write(imgRes_Bin, "jpg", new File(urlImage + "test_getGrayImage_Res_Bin.jpg"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		return elementsRes;
	}
    
	/*
	 * Crée un objet de type FormObject en fonction d'une liste de pixels pour l'étiquette sélectionnée
	 */
    @Deprecated
	private FormObject etiquetteToForm(int[][] myEtiquetteImg, int etiquetteMax) 
	{
		List<Pixel> pixelsByEtiquette = new ArrayList<Pixel>();
		for(int i = 1; i < myEtiquetteImg.length; i++){
			for(int j = 1; j < myEtiquetteImg[i].length; j++){
				if (myEtiquetteImg[i][j] == etiquetteMax){	
					pixelsByEtiquette.add(new Pixel(i, j));
				}
			}
		}
		//FormObject myForm = new FormObject(pixelsByEtiquette, this.imgHeight, this.imgWidth);
		//return myForm;

		FormObject myForm;
		if (pixelsByEtiquette.size()>50)
		{
			if(Data.tiDebug)
				System.out.println("imgWidth " + imgWidth +" "+ "imgHeight "+imgHeight);
			myForm = new FormObject(pixelsByEtiquette, this.imgHeight, this.imgWidth);
		}
		else
			myForm = null;
		return myForm;
	}
	
	/*
	 * Créer un BufferedImage à partir d'une matrice
	 */
	public BufferedImage intTableToBinaryBufferedImage(int[][] myEtiquetteImg) 
	{
		imgWidth = myEtiquetteImg.length;
		imgHeight = myEtiquetteImg[0].length;
		BufferedImage image = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
		for(int i = 1; i < myEtiquetteImg.length; i++){
			for(int j = 1; j < myEtiquetteImg[i].length; j++){
				Color pixelColor= new Color(0);
				if (myEtiquetteImg[i][j] == 0)
					pixelColor = Color.WHITE;
				else
					pixelColor = Color.BLACK;
				image.setRGB(i, j, pixelColor.getRGB());
			}
		}	
		return image;  
	}
	
	public BufferedImage intTableToBufferedImage(int[][] myEtiquetteImg) 
	{
		imgWidth = myEtiquetteImg.length;
		imgHeight = myEtiquetteImg[0].length;
		BufferedImage image = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
		for(int i = 1; i < myEtiquetteImg.length; i++){
			for(int j = 1; j < myEtiquetteImg[i].length; j++){
				image.setRGB(i, j, myEtiquetteImg[i][j]);
			}
		}	
		return image;  
	}
	
	public int getImgHeight() 
	{
		return imgHeight;
	}

	public void setImgHeight(int imgHeight) 
	{
		this.imgHeight = imgHeight;
	}

	public int getImgWidth() 
	{
		return imgWidth;
	}

	public void setImgWidth(int imgWidth) 
	{
		this.imgWidth = imgWidth;
	}

}
