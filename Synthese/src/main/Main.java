package main;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javacompiler.CompileString;
import javacompiler.IAGenetic;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

import ai.AStar;
import data.Data;
import game.Mob;
import game.WindowGame;

public class Main {
	public static AppGameContainer gameContaineur;
	public static int indexC;
	public static void main(String[] args) throws SlickException {
        indexC = 0;
        int maxGame = 3;
		//Load the screen size
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		
		if(!Data.SMALLSCREEN)
		{
			Data.SCREEN_WIDTH = gd.getDisplayMode().getWidth();
			Data.SCREEN_HEIGHT = gd.getDisplayMode().getHeight();
		}
		else
		{
			Data.SCREEN_WIDTH = 880;
			Data.SCREEN_HEIGHT = 660;
		}
		Data.checkValuesIni("paramTI.ini"); // Vérification des variables dans le fichier .ini
		System.out.println("launch the game");
		//launchGame(gameContaineurList.get(indexC));
		gameContaineur = new AppGameContainer(new WindowGame(), Data.SCREEN_WIDTH, Data.SCREEN_HEIGHT, Data.FULLSCREEN);
		launchGame();
	}
	public static void launchGame() throws SlickException
	{
		gameContaineur = new AppGameContainer(new WindowGame(), Data.SCREEN_WIDTH, Data.SCREEN_HEIGHT, Data.FULLSCREEN);
		indexC++;
		gameContaineur.setTargetFrameRate(30);
    	System.out.println("======Hello guys !!!!!====");
    	gameContaineur.start();
    	System.out.println("======Goodbye guys !!!!!====");
	}
	
	public static void reloadGame() throws SlickException
	{
		System.out.println("====reloading game !");
		//gameContaineurList.get(indexC-1).destroy();
		//launchGame(gameContaineurList.get(indexC));
		//gameContaineur.destroy();
		launchGame();
	}
}
