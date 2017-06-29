/**
 * Boss super-class for all boss entities
 * Boss.java
 * @author Nim Wijetunga
 */

package infinity.entites;

import infinity.main.Main;

public abstract class Boss extends Enemy{

	/**
	 * @param type of entity
	 * @param main
	 * @param x position of boss
	 * @param y position of boss
	 * @param width of boss
	 * @param height of boss
	 * @param health of boss
	 * @param damage of boss
	 */
	public Boss(EntityEnum type, Main main, double x, double y, int width, int height,
			double health, double damage) {
		//Pass values to the Enemy super class constructor
		super(type, main, x, y, width, height, health, damage);
		this.setMain(main);
	}
	
	/**bossKilled checks to see if the current boss has a health of 0
	 * 
	 */
	public void bossKilled(){
		if(health <= 0)
			getMain().getPlayer().setKilledBoss(true);
	}
	
	//abstract method so each boss can have a "special ability"
	public abstract void specialAttack();
}
