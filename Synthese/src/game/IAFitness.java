package game;

/** This class is designed to stock all genetic AI datas used to evaluate their fitness.
 * 	This class will be implemented in each IA character and will be supplied at each action.
 * 	
 * @author Sylphe
 *
 */
public class IAFitness {
	// used for local fitness
	/** is a mob or character ? */
	private boolean isNPC; 
	/** total local score */
	private int totalScore; 
	
	//used for overall fitness
	/** number of turn AI stayed alive */
	private int nbTurn = 0; 
	
	public IAFitness(boolean isNPC)
	{
		this.isNPC = isNPC;
		this.totalScore = 0;
	}
	
	public void evaluateAction(Character target, int damages, int heal)
	{
		
	}
	/**
	 *  Add a turn in number of turn passed alive
	 *  Should be used once per turn if character is alive.
	 */
	public void addTurn()
	{
		nbTurn++;
	}
}
