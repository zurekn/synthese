package main;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

import data.Data;
import game.WindowGame;

public class Main {
	
	public static void main(String[] args) throws SlickException {
        
		//Load the screen size
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		Data.SCREEN_WIDTH = gd.getDisplayMode().getWidth();
		Data.SCREEN_HEIGHT = gd.getDisplayMode().getHeight();
	
//		Data.SCREEN_WIDTH = 1920;
//		Data.SCREEN_HEIGHT = 1080;
	
		
        AppGameContainer gameContaineur =  new AppGameContainer(new WindowGame(), Data.SCREEN_WIDTH, Data.SCREEN_HEIGHT, false);
    	gameContaineur.setTargetFrameRate(30);
    	gameContaineur.start();
	}

}
