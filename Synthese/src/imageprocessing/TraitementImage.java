package imageprocessing;
/*
 * http://operationpixel.free.fr/traitementniveaudegris_detection_contour.php
 */
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import data.Data;


public class TraitementImage {
	String urlImage = "Synthese"+File.separator+"res"+File.separator+"testRes"+File.separator;
	int imgHeight;
	int imgWidth;

	public TraitementImage() 
	{
		super();
	}
	
	/*
	 * Génère et sauvegarde une image binaire à partire d'une image normale
	 */
	public void makeBinaryImage(BufferedImage img, String resultImgName, String formatSortie, int seuil)
	{
		try {
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
		System.out.println("Done");
	}
	
	/*
	 * Test si les images sont égales.
	 * Retourne le nombre de pixels égaux
	 */
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
		        System.out.println("there are "+percentNotSame+"px which are not same.");
		    } 
		} 
		catch (Exception e) 
		{
			System.out.println("not enter");
			return 0;
		}
			return percentComparision;
	}

/////////////////////////////////////	Fonctions non utilisées	//////////////////////////////////////////////////////
	/*
	 * Mise en place de l'algorithme d'étiquetage intuitif
	 */
	public List<FormObject> etiquetageIntuitifImage(String webCamCaptureImg, String srcImg, int seuil)
	{			
		BufferedImage imgCompare = null;
		BufferedImage imgSrcRef = null;
		try {
			imgCompare = ImageIO.read(new File(urlImage + webCamCaptureImg));
			imgSrcRef = ImageIO.read(new File(urlImage + srcImg));
		} 
		catch (Exception e) 
		{	System.out.println("problème chargement des images");}
		
		imgHeight = imgCompare.getHeight();
		imgWidth = imgCompare.getWidth();
		int[][] subImgElements = new int[imgWidth][imgHeight];
		int [][] etiquettes = new int[imgWidth][imgHeight];
		
		subImgElements = getSubstractImg(imgCompare, imgSrcRef, seuil);

		//#debug
		if(countPixelsNotNull(subImgElements) == 0)
			subImgElements = getBinaryImage(imgCompare, seuil);
		//#debug
		
		int attA, attB,attC, temp = 1, numEt = 1;
		List<Integer> T = new ArrayList<Integer>();
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
							T.add(temp);
							numEt++;
						}
			 			else if(attC == attA && attC != attB)//si att(c) = att(a) et att(c) != att(b) => E(c) = E(a)
						{	
			 				etiquettes[i][j] = etiquettes[i-1][j]; 
			 				temp++;
						}
						else if(attC != attA && attC == attB)//si att(c) != att(a) et att(c) = att(b) => E(c) = E(b)
						{
							etiquettes[i][j] = etiquettes[i][j-1];
							temp++;
						}
						else if(attC == attA && attC == attB && etiquettes[i-1][j]==etiquettes[i][j-1])//si att(c) = att(a) et att(c) != att(b)  et E(a) = E(b) => E(c) = E(a)
						{
							etiquettes[i][j] = etiquettes[i][j-1];
							temp++;
						}
						else if(attC == attA && attC == attB && etiquettes[i-1][j]!=etiquettes[i][j-1])	//si att(c) = att(a) et att(c) != att(b)  et E(a) = E(b) => E(c) = E(b) et on change toutes E(a) en E(b)
						{
							etiquettes[i][j] = etiquettes[i][j-1];
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
							System.out.println("pas passé");
						}
					}
				}
			}
			int max=0;
			for (Integer integer : T) {
				if (integer>max)
					max=integer;
			}
			System.out.println("numEt " + numEt + " max "+max);
			
			for (Integer integer : T) {
				System.out.print(integer);
			}
			
			System.out.println();
			System.out.println();
			display(etiquettes);
			System.out.println();
			System.out.println();


			
			List<FormObject> formList = new ArrayList<FormObject>();
			System.out.println("t size" + T.size());			
			
			if (T.size() == 1)
			{
				//FormObject myForm = etiquetteToForm(etiquettes, T.indexOf(max)+1);
				FormObject myForm = etiquetteToForm(etiquettes, numEt);
				display(myForm.getMatrix());
				formList.add(myForm);
				filtreSobel(myForm);
			}
			else
			{
				for (Integer it : T) 
				{
					//FormObject myForm = etiquetteToForm(etiquettes, T.indexOf(it));
					FormObject myForm = etiquetteToForm(etiquettes, numEt);
					if (myForm != null)
					{
						display(myForm.getMatrix());
						formList.add(myForm);
						filtreSobel(myForm);
						myForm.findObjectType();
					}
				}
			}
			displayListForm(formList);
			return formList;
		}
		else
		{
			System.out.println("fail");
			return null;
		}
	}
	
	/*
	 * Mise en place de l'algorithme d'étiquetage perso
	 */
	public List<FormObject> etiquetageIntuitifImageGiveList(String webCamCaptureImg, String srcImg, int seuil)//Version non utilisée
	{	
		BufferedImage imgCompare = null;
		BufferedImage imgSrcRef = null;
		try {
			imgCompare = ImageIO.read(new File(urlImage + webCamCaptureImg));
			imgSrcRef = ImageIO.read(new File(urlImage + srcImg));
		} 
		catch (Exception e) 
		{	System.out.println("problème chargement des images");}
		
		int[][] subImgElements  = getGraySubstractAndBinaryImage(imgSrcRef, imgCompare, seuil);//getSubstractImg(imgCompare, imgSrcRef, seuil);
		int [][] etiquettes = new int[imgWidth][imgHeight];
		
		if (Data.debug)
		{
			BufferedImage imgRes = intTableToBufferedImage(subImgElements);
			try {
				ImageIO.write(imgRes, "bmp", new File(urlImage + "resultTest.bmp"));
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//#debug
		if(countPixelsNotNull(subImgElements) == 0)
			subImgElements = getOneGrayAndBinaryImage(imgCompare, seuil);//getBinaryImage(imgCompare, seuil);
		//#debug
		
		int attA, attB,attC, temp = 1, numEt = 1;
		List<Integer> T = new ArrayList<Integer>();
		List<ArrayList<Pixel>> Num = new ArrayList<ArrayList<Pixel>>();
		Num.add(new ArrayList<Pixel>());//pour etiquette 0
		if(subImgElements!=null)
		{
			for(int j = 1; j < imgHeight; j++)
			{
				for(int i = 1; i < imgWidth; i++)
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
			 				temp++;
						}
						else if(attC != attA && attC == attB)//si att(c) != att(a) et att(c) = att(b) => E(c) = E(b)
						{
							etiquettes[i][j] = etiquettes[i][j-1];
							Num.get(etiquettes[i][j]).add(new Pixel(i, j));
							temp++;
						}
						else if(attC == attA && attC == attB && etiquettes[i-1][j]==etiquettes[i][j-1])//si att(c) = att(a) et att(c) != att(b)  et E(a) = E(b) => E(c) = E(a)
						{
							etiquettes[i][j] = etiquettes[i][j-1];
							Num.get(etiquettes[i][j]).add(new Pixel(i, j));
							temp++;
						}
						else if(attC == attA && attC == attB && etiquettes[i-1][j]!=etiquettes[i][j-1])	//si att(c) = att(a) et att(c) != att(b)  et E(a) = E(b) => E(c) = E(b) et on change toutes E(a) en E(b)
						{
							Num.get(etiquettes[i][j-1]).addAll(Num.get(etiquettes[i-1][j]));
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
							System.out.println("pas passé");
						}
					}
				}
			}
			List<FormObject> formList = new ArrayList<FormObject>();
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
			displayListForm(formList);
			return formList;
		}
		else
		{
			System.out.println("fail");
			return null;
		}
	}
	
	/*
	 * Mise en place de l'algorithme d'étiquetage avec tables de substitution
	 */
	public List<FormObject> etiquetageSubstitutionTableImage(String webCamCaptureImg, String srcImg, int seuil)
	{			
			return null;
	}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/*
	 * Mise en place de l'algorithme d'étiquetage perso
	 */
	public List<FormObject> etiquetageIntuitifImageGiveList2(BufferedImage imgCompare, BufferedImage imgSrcRef, int seuil)
	{	
		int[][] subImgElements  = getGraySubstractAndBinaryImage(imgSrcRef, imgCompare, seuil);//getSubstractImg(imgCompare, imgSrcRef, seuil);
		int [][] etiquettes = new int[imgWidth][imgHeight];
		
		if (Data.debug)
		{
			BufferedImage imgRes = intTableToBufferedImage(subImgElements);
			try {
				ImageIO.write(imgRes, "bmp", new File(urlImage + "resultTest.bmp"));
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		
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
			for(int j = 1; j < imgHeight; j++)
			{
				for(int i = 1; i < imgWidth; i++)
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
			 				temp++;
						}
						else if(attC != attA && attC == attB)//si att(c) != att(a) et att(c) = att(b) => E(c) = E(b)
						{
							etiquettes[i][j] = etiquettes[i][j-1];
							Num.get(etiquettes[i][j]).add(new Pixel(i, j));
							temp++;
						}
						else if(attC == attA && attC == attB && etiquettes[i-1][j]==etiquettes[i][j-1])//si att(c) = att(a) et att(c) != att(b)  et E(a) = E(b) => E(c) = E(a)
						{
							etiquettes[i][j] = etiquettes[i][j-1];
							Num.get(etiquettes[i][j]).add(new Pixel(i, j));
							temp++;
						}
						else if(attC == attA && attC == attB && etiquettes[i-1][j]!=etiquettes[i][j-1])	//si att(c) = att(a) et att(c) != att(b)  et E(a) = E(b) => E(c) = E(b) et on change toutes E(a) en E(b)
						{
							Num.get(etiquettes[i][j-1]).addAll(Num.get(etiquettes[i-1][j]));
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
							System.out.println("pas passé");
						}
					}
				}
			}
			List<FormObject> formList = new ArrayList<FormObject>();
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
			displayListForm(formList);
			return formList;
		}
		else
		{
			System.out.println("fail");
			return null;
		}
	}
	

	
	public int[][] erosion(int[][] erosionElements) 
	{

		int height = erosionElements[0].length;
		int width = erosionElements.length;
		int [][] resultErrosion = new int[width][height];
		for(int i = 1; i < width-1; i++)
			resultErrosion[i][0] = 0;
		for(int j = 1; j < height-1; j++)
			resultErrosion[0][j] = 0;
		
		int up, down, right, left;
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
					if( up != 0 && down != 0 && left != 0 && right != 0 && erosionElements[i][j] != 0)
						resultErrosion[i][j] = 255;
					else
						resultErrosion[i][j] = 0;
				}
			}
		} 
		return resultErrosion;
	}
	
	public int[][] dilatation(int[][] dilatationElements) 
	{

		int height = dilatationElements[0].length;
		int width = dilatationElements.length;
		int [][] resultDilatation = new int[width][height];
		for(int i = 1; i < width-1; i++)
			resultDilatation[i][0] = 0;
		for(int j = 1; j < height-1; j++)
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
	
	public int[][] Ouverture(int[][] elemOuverture, int seuil) 
	{
		//int [][] elemOuverture = getBinaryImage(elemOuverture, seuil);		
		int i;
		int [][] resOuverture = elemOuverture;
		for (i=0 ; i<10 ; i++)
			resOuverture = erosion(resOuverture);
		for (i=0 ; i<10 ; i++)
			resOuverture = dilatation(resOuverture);
		
		return resOuverture;
	}
	
	public int[][] fermeture(BufferedImage img, int seuil) 
	{
		int [][] elemFermeture = getBinaryImage(img, seuil);		
		int i;
		int [][] resFermeture = elemFermeture;
		for (i=0 ; i<10 ; i++)
			resFermeture = dilatation(resFermeture);
		for (i=0 ; i<10 ; i++)
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
		System.out.println("le périmètre est de : " + perimetre);
		// fin de mes tests
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
		System.out.println(" form list size " + myList.size());
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
	        System.out.println("there are "+percentNotSame+"px which are not same and "+percentComparision+" which are same.");
	    } 
		return elementsSubImg;
	}
	
	/*
     * Transform one image RGB in Gray images,
     * Binary each pixels from image.
     */
    public int[][] getOneGrayAndBinaryImage(BufferedImage image, int seuil) 
    {
        //BufferedImage img1 = null;
        //BufferedImage img2 = null;
        int[][] elementsImg = null;
        int[][] elementsRes = null;

            
        imgHeight = image.getHeight();
        imgWidth = image.getWidth();
        elementsImg = new int[image.getWidth()][image.getHeight()];
        elementsRes = new int[image.getWidth()][image.getHeight()];
        
        for (int x = 0; x < image.getWidth(); ++x)
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
                elementsRes[x][y] = binaryPixel(elementsImg[x][y], seuil);
                
            }

        return elementsRes;
    }
    
	/*
	 * Transform two images RGB in Gray images,
	 * Substract them,
	 * Binary each pixels from substract image.
	 */
	public int[][] getGraySubstractAndBinaryImage(BufferedImage img1, BufferedImage img2, int seuil) 
	{

		int[][] elements1 = null;
		int[][] elements2 = null;
		int[][] elementsRes = null;

			
			imgHeight = img1.getHeight();
			imgWidth = img1.getWidth();
			elements1 = new int[img1.getWidth()][img1.getHeight()];
			elements2 = new int[img1.getWidth()][img1.getHeight()];
			elementsRes = new int[img1.getWidth()][img1.getHeight()];
			
			if (img1.getWidth() == img2.getWidth() && img1.getHeight() == img2.getHeight()) 
			{
			    for (int x = 0; x < img1.getWidth(); ++x)
				    for (int y = 0; y < img1.getHeight(); ++y)
				    {
			    		/*	Get gray color from RGB origin pixel image 1	*/
				        Color pixelcolor1= new Color(img1.getRGB(x, y));
				        int r1=pixelcolor1.getRed();
				        int g1=pixelcolor1.getGreen();
				        int b1=pixelcolor1.getBlue();
				        
				        int grayLevel1 = (r1 + g1 + b1) / 3;
				        elements1[x][y] = grayLevel1;

				        /*	Get gray color from RGB origin pixel image 2	*/
				        Color pixelcolor= new Color(img2.getRGB(x, y));
				        int r2=pixelcolor.getRed();
				        int g2=pixelcolor.getGreen();
				        int b2=pixelcolor.getBlue();
			
				        int grayLevel2 = (r2 + g2 + b2) / 3;
				        elements2[x][y] = grayLevel2;
				        
				        /*		Substract images		*/
				        elementsRes[x][y] = elements1[x][y]-elements2[x][y]<0 ? elements2[x][y]-elements1[x][y] : elements1[x][y]-elements2[x][y];
				        
				        /*		Binary pixel [x][y]		*/
				        elementsRes[x][y] = binaryPixel(elementsRes[x][y], seuil);
				        
				    }
			}
			else
				System.out.println("images non équivalentes en taille. Dommage!");

		return elementsRes;
	}
	
	/*
	 * Binary a pixel
	 */
	public int binaryPixel(int pix, int seuil)
	{
		return pix < seuil ? 0 : 255;
	}
	
	/*
	 * Crée un objet de type FormObject en fonction d'une liste de pixels pour l'étiquette sélectionnée
	 */
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
	public BufferedImage intTableToBufferedImage(int[][] myEtiquetteImg) 
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
	
	/*
	 * Récupérer les points d'intérêts
	 */
	private Pixel getInterestPoints(int[][] myEtiquetteImg, int etiquetteMax) 
	{
//		List<Pixel> InterestPoints = new ArrayList<Pixel>();
//		//Display(myEtiquetteImg);
//		
//		for(int i = 1; i < myEtiquetteImg.length; i++)
//		{
//			for(int j = 1; j < myEtiquetteImg[i].length; j++)
//			{
//				if (myEtiquetteImg[i][j] == etiquetteMax && (i == myEtiquetteImg.length-1 || j == myEtiquetteImg[i].length-1))
//				{	
//					if (i == myEtiquetteImg.length-1 && j == myEtiquetteImg[i].length-1)
//						InterestPoints.add(new Pixel(i, j));
//					else
//					{
//						if (i == myEtiquetteImg.length)
//						{		
//							if (myEtiquetteImg[i][j] == etiquetteMax && myEtiquetteImg[i][j-1] != etiquetteMax)
//								InterestPoints.add(new Pixel(i, j));
//							if (myEtiquetteImg[i][j] == etiquetteMax && myEtiquetteImg[i][j+1] != etiquetteMax)
//								InterestPoints.add(new Pixel(i, j));
//						}
//						if (j == myEtiquetteImg[i].length)
//						{		
//							if (myEtiquetteImg[i][j] == etiquetteMax && myEtiquetteImg[i-1][j] != etiquetteMax)
//								InterestPoints.add(new Pixel(i, j));
//							if (myEtiquetteImg[i][j] == etiquetteMax && myEtiquetteImg[i+1][j] != etiquetteMax)
//								InterestPoints.add(new Pixel(i, j));
//						}
//					}
//				}
//				else 
//				{
//					//get left up pixel
//					if (myEtiquetteImg[i][j] == etiquetteMax && myEtiquetteImg[i-1][j] != etiquetteMax && myEtiquetteImg[i][j-1] != etiquetteMax)
//						InterestPoints.add(new Pixel(i, j));
//					//get right up pixel
//					if (myEtiquetteImg[i][j] == etiquetteMax && myEtiquetteImg[i+1][j] != etiquetteMax && myEtiquetteImg[i][j+1] != etiquetteMax)
//						InterestPoints.add(new Pixel(i, j));
//					//get left down pixel
//					if (myEtiquetteImg[i][j] == etiquetteMax && myEtiquetteImg[i-1][j] != etiquetteMax && (myEtiquetteImg[i][j+1] != etiquetteMax))
//						InterestPoints.add(new Pixel(i, j));
//					//get right down pixel
//					if (myEtiquetteImg[i][j] == etiquetteMax && myEtiquetteImg[i+1][j] != etiquetteMax && myEtiquetteImg[i][j+1] != etiquetteMax)
//						InterestPoints.add(new Pixel(i, j));
//				}
//			}
//		}
//		return getCenterForm(InterestPoints);
		return null;
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
