package game;

import java.io.IOException;
import java.util.Scanner;

import data.Data;

public class MovementHandler implements Runnable{

	//For debug
	private Scanner sc;
	private String str;
	
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
	
	private String randomMove(){
		String s = "";
		
		return s;
	}
	
	public void run() {
		if(Data.debug){
			str = sc.nextLine();
			windowGame.move(randomMove());
		}
		
	}

}
