package main;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

import game.WindowGame;

public class Main {
	
	public static void main(String[] args) throws SlickException {
        
        AppGameContainer gameContaineur =  new AppGameContainer(new WindowGame(), 1600, 1200, false);
    	gameContaineur.setTargetFrameRate(60);
    	gameContaineur.start();
	}

}
