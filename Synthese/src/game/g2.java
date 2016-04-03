package game;

import data.Data;

public class g2 {
	WindowGame windowgame = WindowGame.getInstance();
	String actionString = "";
	String deplacementString = "p";
	int up=Data.NORTH;
	int down=Data.SOUTH;
	int left=Data.WEST;
	int right=Data.EAST;
	Character defaultC = null;
	String defaultString = "";
	int defaultInt = 0;
	float defaultFloat = 0f;
	boolean defaultBoolean = false;
	
	
	public g2() {
	}
	
	public String run(Character ch)
	{
		    deplacementString = ch.getDeplacement(1,1);
		    actionString = ch.passerTour();
		if(0.2== ((ch.researchCharacter(left)==null)?defaultFloat:ch.researchCharacter(left).getStats().getManaPercentage()) )
		{
		    deplacementString = ch.getDeplacement(-2,0);
		}
		else
		{
		    deplacementString = ch.getDeplacement(-1,2);
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
