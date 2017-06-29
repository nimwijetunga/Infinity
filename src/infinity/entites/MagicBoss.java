/**
 * SkeletonBoss sub-class (only boss in the game currently)
 * SkeletonBoss.java
 * @author Nim Wijetunga
 */

package infinity.entites;

import infinity.main.Main;
import infinity.weapons.Arrow;
import infinity.weapons.Magic;
import infinity.weapons.WeaponEnum;

public class MagicBoss extends Boss{
	
	private int barrageCount = 1;
	private boolean takeBreak, fired;
	protected long barrageDelay, breakDelay;
	private int arrowDamage = 5;
	private WeaponEnum wType;
	
	/**
	 * @param main 
	 * @param x position of boss
	 * @param y position of boss
	 * @param width of boss
	 * @param height of boss
	 * @param health of boss
	 * @param damage of boss
	 */
	public MagicBoss(Main main, double x, double y, int width, int height, double health, double damage,WeaponEnum wType) {
		//pass values to the super class Boss Constructor
		super(EntityEnum.BOSS, main, x, y, width, height, health, damage);
		this.wType = wType;
	}

	@Override
	/**specialAttack to fire arrows in all directions
	 * 
	 */
	public void specialAttack(){
		int arrowWidth = 90, arrowHeight = 90;
		bossKilled();
		stopFire();
		//Create 4 arrows in all 4 directions and add them to the weapon arraylist
		if(!fired ){
			barrageCount++;
			getMain().getWeapons().add(new Magic(wType,x, 
					y - getHeight(), arrowDamage, 200, arrowWidth, arrowHeight, getMain(), true,false,false,false,this));
			getMain().getWeapons().add(new Magic(wType,x, 
					y + getHeight(), arrowDamage, 200, arrowWidth, arrowHeight, getMain(), false,true,false,false,this));
			getMain().getWeapons().add(new Magic(wType, x - getWidth(), 
					y, arrowDamage, 200, arrowWidth, arrowHeight, getMain(), false,false,true,false,this));
			getMain().getWeapons().add(new Magic(wType,x + getWidth(), 
					y, arrowDamage, 200, arrowWidth, arrowHeight, getMain(), false,false,false,true,this));
			fired = true;
			barrageDelay = System.nanoTime();
		}
	}
	
	/**update delay between arrow barrages
	 * 
	 */
	public void update(){
		long elapsed = (System.nanoTime() - barrageDelay) / 1000000;
		if(elapsed > 500 && !takeBreak){
			fired = false;
		}	
	}
	
	/**stopFire to momentarily stop firing arrows to give player a chance to attack
	 * 
	 */
	public void stopFire(){
		if(barrageCount % 4 == 0 && !takeBreak){                                                                                                          
			takeBreak = true;
			fired = true;
			breakDelay = System.nanoTime();
		}
		long elapsed = (System.nanoTime() - breakDelay) / 1000000;
		//If the delay is over (3 sec) then start firing again
		if(elapsed > 3000){
			takeBreak = false;
			update();
		}
	}

}
