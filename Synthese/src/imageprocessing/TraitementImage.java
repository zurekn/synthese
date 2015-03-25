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
	public void makeBinaryImage(String srcImgName, String resultImgName, String formatSortie, int seuil)
	{
		try {
			BufferedImage img = ImageIO.read(new File(urlImage + srcImgName));
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
			System.out.println("image " + srcImgName + " not found");
		}
		System.out.println("Done");
	}
	
	/*
	 * Test si les images sont égales.
	 * Retourne le nombre de pixels égaux
	 */
	public int pixelsBufferedImagesEqual(String srcImgName, String resultImgName)
	{
		int percentComparision = 0;
		int percentNotSame = 0;

		try 
		{
			BufferedImage img1 = ImageIO.read(new File(urlImage + srcImgName));
			BufferedImage img2 = ImageIO.read(new File(urlImage + resultImgName));
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
	public List<FormObject> etiquetageIntuitifImage2(String webCamCaptureImg, String srcImg, int seuil)
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
							System.out.println("position : [" +i+","+j +"] clear de l'etiquette courante c : " + etiquettes[i][j] + " , b : "+etiquettes[i][j-1] +" , a : "+etiquettes[i-1][j] );
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
//			int max=0;
//			for (Integer integer : T) {
//				if (integer>max)
//					max=integer;
//			}
//			System.out.println("numEt " + numEt + " max "+max);
			List<FormObject> formList = new ArrayList<FormObject>();
			System.out.println("num size = " +Num.size());
			for (ArrayList<Pixel> OneArray : Num) {
				System.out.println("OneArray size = "+OneArray.size());
				if(OneArray.size()>50)
				{
					System.out.println("gagné !!");

					FormObject myForm = new FormObject(OneArray, this.imgHeight, this.imgWidth);
					display(myForm.getMatrix());
					formList.add(myForm);
					filtreSobel(myForm);
					myForm.findObjectType();
				}
			}		
//			List<FormObject> formList = new ArrayList<FormObject>();
//			System.out.println("t size" + T.size());			
//			
//			if (T.size() == 1)
//			{
//				//FormObject myForm = etiquetteToForm(etiquettes, T.indexOf(max)+1);
//				FormObject myForm = etiquetteToForm(etiquettes, numEt);
//				display(myForm.getMatrix());
//				formList.add(myForm);
//				filtreSobel(myForm);
//			}
//			else
//			{
//				for (Integer it : T) 
//				{
//					//FormObject myForm = etiquetteToForm(etiquettes, T.indexOf(it));
//					FormObject myForm = etiquetteToForm(etiquettes, numEt);
//					if (myForm != null)
//					{
//						display(myForm.getMatrix());
//						formList.add(myForm);
//						filtreSobel(myForm);
//						myForm.findObjectType();
//					}
//				}
//			}
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

		int attA, attB,attC, numEt = 1;
		
		/* initialisation de T[i] = i */
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
							T.add(numEt);
							numEt++;
						}
			 			else if(attC == attA && attC != attB)//si att(c) = att(a) et att(c) != att(b) => E(c) = E(a)
						{	
			 				etiquettes[i][j] = etiquettes[i-1][j]; 
						}
						else if(attC != attA && attC == attB)//si att(c) != att(a) et att(c) = att(b) => E(c) = E(b)
						{
//							etiquettes[i][j] = T[etiquettes[i][j-1]];
						}
						else if(attC == attA && attC == attB && etiquettes[i-1][j]==etiquettes[i][j-1])//si att(c) = att(a) et att(c) != att(b)  et E(a) = E(b) => E(c) = E(a)
						{
							etiquettes[i][j] = etiquettes[i][j-1];
						}
						else if(attC == attA && attC == attB && etiquettes[i-1][j]!=etiquettes[i][j-1])	//si att(c) = att(a) et att(c) != att(b)  et E(a) = E(b) => E(c) = E(b) et on change toutes E(a) en E(b)
						{
							etiquettes[i][j] = etiquettes[i][j-1];
							
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
			
			
			List<FormObject> formList = new ArrayList<FormObject>();
			FormObject myForm = etiquetteToForm(etiquettes, T.indexOf(max)+1);
			formList.add(myForm);
			filtreSobel(myForm);
			
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
			System.out.println(" form gravity center : " + pixel.getGravityCenter().getX() + " : "+pixel.getGravityCenter().getY());
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
	            		elementsSubImg [x][y] = elementsFirstImg[x][y];
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
	public BufferedImage tableToBufferedImage(int[][] myEtiquetteImg) 
	{
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
