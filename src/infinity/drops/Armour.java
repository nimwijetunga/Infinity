/**
 * To give Armour stats to player
 * Armour.java
 * @author Nim Wijetunga
 */
package infinity.drops;

public class Armour {
	private int healthIncr, damIncr, regenFactor;
	private boolean equipped, purchased, incrStat;
	
	
	/**
	 * @param healthIncr is the amount of health the Armour gives
	 * @param damIncr is how much damage is added to the player's stats
	 * @param regenFactor is how much the players health regens by
	 * @param equipped to see if the Armour is on the player or not
	 * @param purchased to see if the Armour has been purchased
	 * @param incrStat to see if the Armour stats has already been given to the player
	 */
	public Armour(int healthIncr, int damIncr, int regenFactor, boolean equipped, boolean purchased
			,boolean incrStat){
		this.healthIncr = healthIncr;
		this.damIncr = damIncr;
		this.regenFactor = regenFactor;
		this.equipped = equipped;
		this.purchased = purchased;
		this.setIncrStat(incrStat);
	}

	//Getters and setters for the Armour Class
	
	public int getHealthIncr() {
		return healthIncr;
	}

	public int getDamIncr() {
		return damIncr;
	}

	public int getRegenFactor() {
		return regenFactor;
	}
	
	public boolean isEquipped() {
		return equipped;
	}

	public boolean isPurchased() {
		return purchased;
	}
	
	public void setEquipped(boolean equipped) {
		this.equipped = equipped;
	}

	public void setPurchased(boolean purchased) {
		this.purchased = purchased;
	}

	public boolean isIncrStat() {
		return incrStat;
	}

	public void setIncrStat(boolean incrStat) {
		this.incrStat = incrStat;
	}
	
}
