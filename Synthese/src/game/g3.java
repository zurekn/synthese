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
	
	public g3() {
	}
	
	public String run(Character ch)
	{
		    actionString = ch.getMaxHealingSpellId()+":"+ Data.EAST;
		if((ch.getStats().getArmor())<= ((ch.researchCharacter(right)==null)?0:ch.researchCharacter(right).getStats().getMaxLife()) )
		{
		    deplacementString = ch.getDeplacement(1,1);
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
