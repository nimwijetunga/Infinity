/**
 * Class for the sword weapon 

 * Sword.java
 * @author Nim Wijetunga
 */
package infinity.weapons;

import java.awt.Graphics;

import infinity.entites.Entity;
import infinity.main.Main;

public class Sword extends Weapon{

	/**
	 * @param x cord of the entity 
	 * @param y cord of the entity 
	 * @param damage the weapon does
	 * @param range of the weapon
	 * @param width of collision
	 * @param height of collision
	 * @param main
	 * @param up directions the entity is facing 
	 * @param down directions the entity is facing 
	 * @param left directions the entity is facing 
	 * @param right directions the entity is facing 
	 * @param e is the entity using the weapon 
	 */
	public Sword(double x, double y, double damage, double range, double width, double height, Main main, boolean up, boolean down, boolean left, boolean right, Entity e) {
		super(WeaponEnum.SWORD, x, y, damage + 10, range, width, height, main, up, down, left, right,e);
	}
	
	/**
	 * Allows the sword to attack enemies 
	 * @param g is the graphics component 
	 */
	public void attack(Graphics g){
		basicAttack();
	}
	
}
