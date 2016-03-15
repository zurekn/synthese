package game;

import data.Data;

public class IAScript {
	WindowGame windowgame = WindowGame.getInstance();
	String actionString = "";
	String deplacementString = "";
	
	public IAScript() {
	}
	
	public String run(Character ch)
	{
		
		return actionString+"!!"+deplacementString;
	}

	public String getActionString() {
		return actionString;
	}

	public void setActionString(String actionString) {
		this.actionString = actionString;
	}

	public String getDeplacementString() {
		return deplacementString;
	}

	public void setDeplacementString(String deplacementString) {
		this.deplacementString = deplacementString;
	}
}
