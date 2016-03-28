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
	
	public g1() {
	}
	
	public String run(Character ch)
	{
		    actionString = ch.getSpells().get(0).getId()+":"+ Data.NORTH;
		    actionString = ch.getMaxDamagingSpellId()+":"+ Data.SOUTH;
		if(((ch.researchCharacter(down)==null)?0:ch.researchCharacter(down).getStats().getMaxLife()/3)> (ch.getStats().getMaxLife()/3) )
		{
		    deplacementString = ch.getDeplacement(-2,-1);
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
