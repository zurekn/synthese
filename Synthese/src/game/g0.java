package game;

import data.Data;

public class g0 {
	WindowGame windowgame = WindowGame.getInstance();
	String actionString = "";
	String deplacementString = "p";
	int up=Data.NORTH;
	int down=Data.SOUTH;
	int left=Data.WEST;
	int right=Data.EAST;
	
	public g0() {
	}
	
	public String run(Character ch)
	{
		    actionString = ch.getMaxHealingSpellId()+":"+ Data.SOUTH;
		if(ch.getStats().getMaxLife()<= ch.getStats().getMaxLife() )
		{
		    deplacementString = ch.getDeplacement(0,-1);
		}
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
