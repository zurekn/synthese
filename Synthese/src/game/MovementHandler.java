package game;

import java.io.IOException;
import java.util.Scanner;

import data.Data;

public class MovementHandler implements Runnable{

	//For debug
	Scanner sc;
	String str;
	
	private WindowGame windowGame;
	
	public MovementHandler(WindowGame windowGame){
		this.windowGame = windowGame;
		init();
	}
	
	public void init(){
		if(Data.debug){
			sc = new Scanner(System.in);
		}
	}
	public void run() {
		if(Data.debug){
			str = sc.nextLine();
			windowGame.move(str);
			
		}
		
	}

}
