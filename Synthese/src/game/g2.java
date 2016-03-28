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
	
	public g2() {
	}
	
	public String run(Character ch)
	{
		    deplacementString = ch.getDeplacement(2,2);
		    deplacementString = ch.getDeplacement(0,2);
		if(	0.3>= 	ch.getStats().getLifePercentage() )
		{
		    actionString = ch.getMaxDamagingSpellId()+":"+ Data.EAST;
		if(ch.getStats().getLife()== ((ch.researchCharacter(left)==null)?0:ch.researchCharacter(left).getStats().getMaxLife()/3) )
		{
		    deplacementString = ch.getDeplacement(0,-2);
		}
		else
		{
		    deplacementString = ch.getDeplacement(-2,2);
		}
		}
		else
		{
		    actionString = ch.getMaxDamagingSpellId()+":"+ Data.SOUTH;
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
