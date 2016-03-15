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
	Player p = null;
	
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
	
	public Player getP() {
		return p;
	}

	public void setP(Player p) {
		this.p = p;
	}
	
	public void display(){
		System.out.println(this.getLife());
	}
}
