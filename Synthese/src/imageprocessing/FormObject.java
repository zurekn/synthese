package imageprocessing;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;


public class FormObject {
	int imgHeight;
	int imgWidth;
	List<Pixel> pixelList;
	int[][] matrix;
	int perimeter;
	int surface;
	Pixel gravityCenter;
	typeForm myTypeForm;
	
	public enum typeForm{
		carre,
		rectangle,
		triangle,
		cercle;
	}

	public FormObject(List<Pixel> pixelList, int imgHeight, int imgWidth) {
		super();
		this.imgHeight = imgHeight;
		this.imgWidth = imgWidth;
		this.pixelList = pixelList;
		this.surface = getSurface();
		this.gravityCenter = getCenterForm(pixelList);
		recreateMatrix();
//		findObjectType();
	}

	public void recreateMatrix(){
		matrix = new int[imgWidth][imgHeight];
		for(int i = 0; i < matrix.length; i++){
			for(int j = 1; j < matrix[i].length; j++){
				matrix[i][j] = 0;
			}
		}
		System.out.println("pixel list.size " + pixelList.size());
		for (Pixel pixel : pixelList) {
			matrix[pixel.getX()][pixel.getY()] = 255;	
		}	
		//display(matrix);
	}
	
	/*
	 * Calcul de la compacité
	 */
	public double calculCompacite(int perimeter, int surface) {
		double compacite;
		compacite = 4*Math.PI*(surface/(perimeter*perimeter));
		System.out.println("compacité de : "+compacite);
		return compacite;
	}
	
	/*
	 * Trouver type d'objet en fonction de la compacité
	 */
	public void findObjectType() {
		double compacite = calculCompacite(perimeter, surface);
		if(compacite > 0.85)
			myTypeForm = typeForm.cercle;
		else if (compacite < 0.65)
			myTypeForm = typeForm.carre;
		else if (compacite < 0.45)
			myTypeForm = typeForm.rectangle;
		else
			myTypeForm = typeForm.triangle;
	}
	
	/*
	 * Récupère le centre de gravité d'un ensemble de points désignés par une étiquette
	 */
	public Pixel getCenterForm(List<Pixel>PointsInterests){
		int up, down, left, right;
		up = PointsInterests.get(0).getX();
		down = PointsInterests.get(0).getX();
		right= PointsInterests.get(0).getX();
		left = PointsInterests.get(0).getX();
		
		for (Pixel pixel : PointsInterests) {
			up = (up<pixel.getY())? up:pixel.getY();
			down = (down>pixel.getY())? down:pixel.getY();
			left = (left<pixel.getX())? left:pixel.getX();
			right = (right>pixel.getX())? right:pixel.getX();
			//System.out.println(pixel.getX() + " " + pixel.getY());
		}		
		
		Pixel gravityGenter = new Pixel(((right+left)/2), ((down+up)/2));
		return gravityGenter;
	}
	
	/*
	 * Affichage d'une matrice
	 */
	private void display(int[][] myMatrix) {
		for(int i = 0; i < myMatrix.length; i++)
		{
			for(int j = 0; j < myMatrix[i].length; j++)
			{
				System.out.print(myMatrix[i][j]+" ");
			}
			System.out.println();
		}
	}
		
	/*
	 * Affiche tous les éléments d'une liste
	 */
	private void displayList(List<Pixel> myList) {
		System.out.println("test");
		for (Pixel pixel : myList) {
			System.out.print(pixel.getX() +" "+ pixel.getY());
			System.out.println();
		}
	}
	
	//********************************** Getters & Setters ************************************//
	/*
	 * Récupération de la surface de la forme
	 */
	public int getSurface() {
		return pixelList.size();
	}
	public int getImgHeight() {
		return imgHeight;
	}
	public void setImgHeight(int imgHeight) {
		this.imgHeight = imgHeight;
	}
	public int getImgWidth() {
		return imgWidth;
	}
	public void setImgWidth(int imgWidth) {
		this.imgWidth = imgWidth;
	}
	public int[][] getMatrix() {
		return matrix;
	}
	public void setMatrix(int[][] matrix) {
		this.matrix = matrix;
	}
	public List<Pixel> getPixelList() {
		return pixelList;
	}
	public void setPixelList(List<Pixel> pixelList) {
		this.pixelList = pixelList;
	}
	public int getPerimeter() {
		return perimeter;
	}
	public void setPerimeter(int perimeter) {
		this.perimeter = perimeter;
	}
	public void setSurface(int surface) {
		this.surface = surface;
	}

	public Pixel getGravityCenter() {
		return gravityCenter;
	}

	public void setGravityCenter(Pixel gravityCenter) {
		this.gravityCenter = gravityCenter;
	}

	public typeForm getMyTypeForm() {
		return myTypeForm;
	}

	public void setMyTypeForm(typeForm myTypeForm) {
		this.myTypeForm = myTypeForm;
	}
	
}
