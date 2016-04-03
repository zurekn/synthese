package game;

import data.Data;

public class g3 {
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
	
	
	public g3() {
	}
	
	public String run(Character ch)
	{
		    deplacementString = ch.getDeplacement(-1,2);
		if((ch.getStats().getEyeSight())== ((ch.researchCharacter(right)==null)?defaultInt:ch.researchCharacter(right).Portee(((ch.researchCharacter(right)==null)?defaultString:ch.researchCharacter(right).getMaxHealingSpellId()))) )
		{
		    actionString = ch.getMaxHealingSpellId()+":"+ Data.SELF;
		    deplacementString = ch.getDeplacement(1,2);
		}
		else
		{
		    deplacementString = ch.getDeplacement(2,2);
		if(0.3< ((ch.researchCharacter(right)==null)?defaultFloat:ch.researchCharacter(right).getStats().getManaPercentage()) )
		{
		    deplacementString = ch.getDeplacement(-2,-1);
		    deplacementString = ch.getDeplacement(1,-1);
		}
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
