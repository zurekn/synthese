package javacompiler;

import game.Player;
import game.WindowGame;

import java.io.Serializable;

import data.Data;
import exception.IllegalActionException;
import exception.IllegalCaracterClassException;

@SuppressWarnings("serial")
public class maClasseTest extends Player implements Serializable {
	public int life = -10;
	public boolean ennemyInSigth = true;
	WindowGame windogame = WindowGame.getInstance();
	
	public maClasseTest(int x, int y, String id, String caracterClass) throws IllegalCaracterClassException {
		super(x, y, id, caracterClass);
	}
	
	public void run() throws IllegalActionException
	{
		
	}
	
	public int getLife() {
		return life;
	}
	public void setLife(int life) {
		this.life = life;
	}
	public boolean isEnnemyInSigth() {
		return ennemyInSigth;
	}
	public void setEnnemyInSigth(boolean ennemyInSigth) {
		this.ennemyInSigth = ennemyInSigth;
	}
	
	public void display(){
		System.out.println(this.getLife());
	}
}
