package game;


public class IAFitness {
	// used for local fitness
	private int totalScore;// total local score
	private int pHeal;
	private int pAction;
	private int pActionmissed;
	private int pMove;
	private int pPass;
	
	private int killEnemy = 30;
	private int killAlly = -30;
	private int attackAlly = -20;
	private int attackEnemy = 20;
	private int healAllyMaxLife = 1;
	private int healAlly = 10;
	private int healEnemyMaxLife = -10;
	private int healEnemy = -20;
	private int unlessSpell = 1;
	private int move = 1;
	private int pass = 1;
	
	//used for overall fitness
	private int nbTurn = 0; // number of turn AI stayed alive
	
	public IAFitness(boolean isNPC)
	{
		this.totalScore = 0;
		this.pHeal = 0;
		this.pAction = 0;
		this.pActionmissed = 0;
		this.pMove = 0;
		this.pPass = 0;
	}
	/**
	 *  Add a turn in number of turn passed alive
	 *  Should be used once per turn if character is alive.
	 */
	public void addTurn()
	{
		nbTurn++;
	}
	
	/*
	 * score un heal
	 */
	public void scoreHeal(Character focusCharacter, Character currentCharacter)
	{	// score : soigne quelqu'un dont la vie est inférieure à maxlife
		if(focusCharacter.getStats().getLife()<focusCharacter.getStats().getMaxLife()) 
		{	
			// score : soigne ennemi
			if(focusCharacter.isMonster() != currentCharacter.isMonster()) 
				currentCharacter.getFitness().setpHeal(currentCharacter.getFitness().getpHeal()+currentCharacter.getFitness().getHealEnemy());
			else // score : soigne allié
				currentCharacter.getFitness().setpHeal(currentCharacter.getFitness().getpHeal()+currentCharacter.getFitness().getHealAlly());
		}
		else // score : soigne quelqu'un dont la vie est supérieur ou égale à maxlife
		{	
			// score : soigne ennemi
			if(focusCharacter.isMonster() != currentCharacter.isMonster()) 
				currentCharacter.getFitness().setpHeal(currentCharacter.getFitness().getpHeal()+currentCharacter.getFitness().getHealEnemyMaxLife());
			else// score : soigne allié
				currentCharacter.getFitness().setpHeal(currentCharacter.getFitness().getpHeal()+currentCharacter.getFitness().getHealAllyMaxLife());
		}
	}
	
	/*
	 * score une attaque
	 */
	public void scoreSpell(Character focusCharacter, Character currentCharacter)
	{	// score : tue quelqu'un
		if(focusCharacter.checkDeath()) 
		{	
			// score : tue ennemi
			if(focusCharacter.isMonster() != currentCharacter.isMonster()) 
				currentCharacter.getFitness().setpAction(currentCharacter.getFitness().getpAction()+currentCharacter.getFitness().getKillEnemy());
			else // score : tue allié
				currentCharacter.getFitness().setpAction(currentCharacter.getFitness().getpAction()+currentCharacter.getFitness().getKillAlly());
		}
		else // score : attaque quelqu'un dont la vie est supérieur ou égale à maxlife
		{	// score : attaque ennemi
			if(focusCharacter.isMonster() != currentCharacter.isMonster()) 
				currentCharacter.getFitness().setpAction(currentCharacter.getFitness().getpAction()+currentCharacter.getFitness().getAttackEnemy());
			else // score : attaque allié
				currentCharacter.getFitness().setpAction(currentCharacter.getFitness().getpAction()+currentCharacter.getFitness().getAttackAlly());
		}
	}
	
	/*
	 * Score un lancement de spell inutile (si le spell n'atteint personne)
	 */
	public void scoreUnlessSpell()
	{
		this.setUnlessSpell(this.getpActionmissed()+this.getUnlessSpell());
	}
	
	/*
	 * score un passage de tour
	 */
	public void scorePassTurn()
	{
		this.setPass(this.getpPass()+this.getPass());
	}
	
	/*
	 * score deplacement
	 */
	public void scoreMove()
	{
		this.setMove(this.getpMove()+this.getMove());
	}
	
	//	Getters and Setters
	public int getpHeal() {
		return pHeal;
	}
	public void setpHeal(int pHeal) {
		this.pHeal = pHeal;
	}
	public int getpAction() {
		return pAction;
	}
	public void setpAction(int pAction) {
		this.pAction = pAction;
	}
	public int getpActionmissed() {
		return pActionmissed;
	}
	public void setpActionmissed(int pActionmissed) {
		this.pActionmissed = pActionmissed;
	}
	public int getpMove() {
		return pMove;
	}
	public void setpMove(int pMove) {
		this.pMove = pMove;
	}
	public int getpPass() {
		return pPass;
	}
	public void setpPass(int pPass) {
		this.pPass = pPass;
	}
	public int getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}
	public int getKillEnemy() {
		return killEnemy;
	}
	public void setKillEnemy(int killEnemy) {
		this.killEnemy = killEnemy;
	}
	public int getKillAlly() {
		return killAlly;
	}
	public void setKillAlly(int killAlly) {
		this.killAlly = killAlly;
	}
	public int getAttackAlly() {
		return attackAlly;
	}
	public void setAttackAlly(int attackAlly) {
		this.attackAlly = attackAlly;
	}
	public int getAttackEnemy() {
		return attackEnemy;
	}
	public void setAttackEnemy(int attackEnemy) {
		this.attackEnemy = attackEnemy;
	}
	public int getHealAllyMaxLife() {
		return healAllyMaxLife;
	}
	public void setHealAllyMaxLife(int healAllyMaxLife) {
		this.healAllyMaxLife = healAllyMaxLife;
	}
	public int getHealAlly() {
		return healAlly;
	}
	public void setHealAlly(int healAlly) {
		this.healAlly = healAlly;
	}
	public int getHealEnemyMaxLife() {
		return healEnemyMaxLife;
	}
	public void setHealEnemyMaxLife(int healEnemyMaxLife) {
		this.healEnemyMaxLife = healEnemyMaxLife;
	}
	public int getHealEnemy() {
		return healEnemy;
	}
	public void setHealEnemy(int healEnemy) {
		this.healEnemy = healEnemy;
	}
	public int getUnlessSpell() {
		return unlessSpell;
	}
	public void setUnlessSpell(int unlessSpell) {
		this.unlessSpell = unlessSpell;
	}
	public int getMove() {
		return move;
	}
	public void setMove(int move) {
		this.move = move;
	}
	public int getPass() {
		return pass;
	}
	public void setPass(int pass) {
		this.pass = pass;
	}
	public int getNbTurn() {
		return nbTurn;
	}
	public void setNbTurn(int nbTurn) {
		this.nbTurn = nbTurn;
	}
	
	
}
