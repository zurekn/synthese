package game;

import data.Data;

public class g1 {
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
	
	
	public g1() {
	}
	
	public String run(Character ch)
	{
		    actionString = ch.getSpells().get(0).getId()+":"+ Data.WEST;
		    deplacementString = ch.getDeplacement(2,-2);
		if(((ch.researchCharacter(right)==null)?defaultInt:ch.researchCharacter(right).getStats().getMaxLife()/3)> ((ch.researchCharacter(left)==null)?defaultInt:ch.researchCharacter(left).getStats().getMagicPower()) )
		{
		    deplacementString = ch.getDeplacement(-1,-2);
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
