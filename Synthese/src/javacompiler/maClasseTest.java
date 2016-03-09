package javacompiler;

import java.io.Serializable;

public class maClasseTest implements Serializable {
	public int life = -10;
	public boolean ennemyInSigth = true;
	public void run()
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
}
