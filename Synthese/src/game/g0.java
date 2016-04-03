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
	Character defaultC = null;
	String defaultString = "";
	int defaultInt = 0;
	float defaultFloat = 0f;
	boolean defaultBoolean = false;
	
	
	public g0() {
	}
	
	public String run(Character ch)
	{
		    deplacementString = ch.getDeplacement(3,-1);
		    deplacementString = ch.getDeplacement(2,2);
		if(true== (ch.isCharacterInLine(left)) )
		{
		    deplacementString = ch.getDeplacement(1,-1);
		    deplacementString = ch.getDeplacement(1,0);
		if(((ch.researchCharacter(down)==null)?defaultInt:ch.researchCharacter(down).getStats().getMaxMana()/3)< ((ch.researchCharacter(left)==null)?defaultInt:ch.researchCharacter(left).getStats().getStrength()) )
		{
		    deplacementString = ch.getDeplacement(1,1);
		}
		else
		{
		    actionString = ch.getMaxDamagingSpellId()+":"+ Data.EAST;
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
