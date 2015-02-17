package main;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

import game.WindowGame;

public class Main {
	
	public static void main(String[] args) throws SlickException {
        
		//Load the screen size
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int width = gd.getDisplayMode().getWidth();
		int height = gd.getDisplayMode().getHeight();
		
        AppGameContainer gameContaineur =  new AppGameContainer(new WindowGame(), width, height, true);
    	gameContaineur.setTargetFrameRate(60);
    	gameContaineur.start();
	}

}
