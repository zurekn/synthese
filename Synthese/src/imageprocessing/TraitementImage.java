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
import testpackage.ImageProcessingTest;


public class TraitementImage {
	String urlImage = "";
	
	public TraitementImage() 
	{
		super();
	}
	
	public void makeBinaryImage(String srcImgName, String resultImgName, String formatSortie, int seuil)
	{
		try {
			BufferedImage img = ImageIO.read(new File(urlImage + srcImgName));
		    System.out.println("width " + img.getWidth() + " ::: height "+ img.getHeight());
		   
		    
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

	public int bufferedImagesEqual(String srcImgName, String resultImgName)
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
			// TODO: handle exception
			System.out.println("not enter");
			return 0;
		}
			return percentComparision;
	}
		
	public int[][] getSubstractImg(BufferedImage imgToCompare, BufferedImage imgRef)
	{
		int percentComparision = 0;
		int percentNotSame = 0;
    	int[][] elementsFirstImg = null;
		int[][] elementsSecondImg = null;
		int[][] elementsSubImg = null;
		int seuil = 110;
					
	    if (imgToCompare.getWidth() == imgRef.getWidth() && imgToCompare.getHeight() == imgRef.getHeight()) {

			elementsFirstImg = new int[imgToCompare.getWidth()][imgToCompare.getHeight()];
			elementsSecondImg = new int[imgToCompare.getWidth()][imgToCompare.getHeight()];
			elementsSubImg = new int[imgToCompare.getWidth()][imgToCompare.getHeight()];
			
			elementsFirstImg = getBinaryImage(imgToCompare, seuil);
			elementsSecondImg = getBinaryImage(imgRef, seuil);
			
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

	
	public Pixel EtiquetageIntuitifImage(String webCamCaptureImg, String srcImg)
	{			
		BufferedImage imgCompare = null;
		BufferedImage imgSrcRef = null;
		try {
			imgCompare = ImageIO.read(new File(urlImage + webCamCaptureImg));
			imgSrcRef = ImageIO.read(new File(urlImage + srcImg));
		} 
		catch (Exception e) 
		{	System.out.println("problème chargement des images");
			return null;
		}
		
		int[][] subImgElements = new int[imgCompare.getWidth()][imgCompare.getHeight()];
		int [][] etiquettes = new int[imgCompare.getWidth()][imgCompare.getHeight()];
		subImgElements = getSubstractImg(imgCompare, imgSrcRef);
		
		int attA, attB,attC, numEt = 1, n=0;
		List<Integer> T = new ArrayList<Integer>();
		if(subImgElements!=null)
		{
			for(int i = 1; i < imgCompare.getWidth(); i++)
			{
				for(int j = 1; j < imgCompare.getHeight(); j++)
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
							etiquettes[i][j] = etiquettes[i][j-1];
						}
						else if(attC == attA && attC == attB && etiquettes[i-1][j]==etiquettes[i][j-1])//si att(c) = att(a) et att(c) != att(b)  et E(a) = E(b) => E(c) = E(a)
						{
							etiquettes[i][j] = etiquettes[i][j-1];
						}
						else if(attC == attA && attC == attB && etiquettes[i-1][j]!=etiquettes[i][j-1])	//si att(c) = att(a) et att(c) != att(b)  et E(a) = E(b) => E(c) = E(b) et on change toutes E(a) en E(b)
						{
							etiquettes[i][j] = etiquettes[i][j-1];
							T.remove((Integer) etiquettes[i-1][j]);
							for(int x=0;x<=i;x++)
							{
								for(int y=0;y<=j;y++)
								{
									if(etiquettes[x][y]==etiquettes[i-1][j])
									{	
										etiquettes[x][y]=etiquettes[i][j-1];
									}
								}					
							}
							n++;
						}
						else {
							System.out.println("pas passé");
						}
					}
				}
			}
			System.out.println("Nombre de reparcour de l'image: "+n);
			System.out.println("fin etiquetage en : " + (System.currentTimeMillis()-ImageProcessingTest.time)+"ms");
			int max=0;
			for (Integer integer : T) {
				if (integer>max)
					max=integer;
			}
//			System.out.println("max "+max);
			//Display(etiquettes);
	//		List<Pixel> pixelsInterest = new ArrayList<Pixel>();
	//		pixelsInterest = getInterestPoints(etiquettes, T.indexOf(max)+1);
			return getInterestPoints(etiquettes, max);
	//		System.out.println("Il y a : "+ (numEt-1) +" etiquettes.");
	//		DisplayList(pixelsInterest);
		}
		else
		{
			System.out.println("fail");
			return null;
		}
	}
	
	/*
	 * Récupérer les points d'intérêts
	 */
	private Pixel getInterestPoints(int[][] myEtiquetteImg, int etiquetteMax) {
		List<Pixel> InterestPoints = new ArrayList<Pixel>();
//		Display(myEtiquetteImg);
		
		for(int i = 1; i < myEtiquetteImg.length; i++)
		{
			for(int j = 1; j < myEtiquetteImg[i].length; j++)
			{
				if (myEtiquetteImg[i][j] == etiquetteMax && (i == myEtiquetteImg.length-1 || j == myEtiquetteImg[i].length-1))
				{	
					if (i == myEtiquetteImg.length-1 && j == myEtiquetteImg[i].length-1)
						InterestPoints.add(new Pixel(i, j));
					else
					{
						if (i == myEtiquetteImg.length-1)
						{		
							if (myEtiquetteImg[i][j-1] != etiquetteMax)
								InterestPoints.add(new Pixel(i, j));
							if (myEtiquetteImg[i][j+1] != etiquetteMax)
								InterestPoints.add(new Pixel(i, j));
						}
						if (j == myEtiquetteImg[i].length-1)
						{		
							if (myEtiquetteImg[i-1][j] != etiquetteMax)
								InterestPoints.add(new Pixel(i, j));
							if (myEtiquetteImg[i+1][j] != etiquetteMax)
								InterestPoints.add(new Pixel(i, j));
						}
					}
					
				}
				else 
				{
					
					//get left up pixel
					if (myEtiquetteImg[i][j] == etiquetteMax && myEtiquetteImg[i-1][j] != etiquetteMax && myEtiquetteImg[i][j-1] != etiquetteMax)
						InterestPoints.add(new Pixel(i, j));
					//get right up pixel
					if (myEtiquetteImg[i][j] == etiquetteMax && myEtiquetteImg[i+1][j] != etiquetteMax && myEtiquetteImg[i][j+1] != etiquetteMax)
						InterestPoints.add(new Pixel(i, j));
					//get left down pixel
					if (myEtiquetteImg[i][j] == etiquetteMax && myEtiquetteImg[i-1][j] != etiquetteMax && (myEtiquetteImg[i][j+1] != etiquetteMax))
						InterestPoints.add(new Pixel(i, j));
					//get right down pixel
					if (myEtiquetteImg[i][j] == etiquetteMax && myEtiquetteImg[i+1][j] != etiquetteMax && myEtiquetteImg[i][j+1] != etiquetteMax)
						InterestPoints.add(new Pixel(i, j));
				}
			}
		}
//		System.out.println("test display InterestPoints");
//		DisplayList(InterestPoints);
		return getCenterForm(InterestPoints);
	}
	
	public Pixel getCenterForm(List<Pixel>PointsInterests)
	{
		int up, down, left, right;
		System.out.println("taille PointsInterests : "+ PointsInterests.size());
		up = PointsInterests.get(0).getY();
		down = PointsInterests.get(0).getY();
		right= PointsInterests.get(0).getX();
		left = PointsInterests.get(0).getX();
		
		for (Pixel pixel : PointsInterests) {
			up = (up<pixel.getY())? up : pixel.getY();
			down = (down>pixel.getY())? down : pixel.getY();
			left = (left<pixel.getX())? left : pixel.getX();
			right = (right>pixel.getX())? right : pixel.getX();
//			System.out.println("getX getY "+pixel.getX() + " -- " + pixel.getY());
//			System.out.println("right left up down "+right+" "+left+" "+up+" "+down);
		}		
//		System.out.println("right left up down "+right+" "+left+" "+up+" "+down);
		Pixel gravityGenter = new Pixel(((right+left)/2), ((down+up)/2));
		return gravityGenter;
	}
	
	
	/*
	 * Affichage d'une matrice
	 */
	private void Display(int[][] myMatrix) 
	{
		
		for(int i = 0; i < myMatrix.length; i++)
		{
			for(int j = 0; j < myMatrix[i].length; j++)
			{
				System.out.print(myMatrix[i][j]+" ");
			}
			System.out.println();
		}
	}
	
	private void DisplayList(List<Pixel> myList) 
	{
		System.out.println("test");
		for (Pixel pixel : myList) {
			System.out.print(pixel.getX() +" "+ pixel.getY());
			System.out.println();
		}
	}
	

}
