/**
 * Has all functions for weapons usage 
 * Weapon.java
 * @author Nim Wijetunga
 */
package infinity.weapons;

import java.awt.Graphics;
import java.awt.Rectangle;

import infinity.entites.Enemy;
import infinity.entites.Entity;
import infinity.entites.EntityEnum;
import infinity.handlers.SoundFX;
import infinity.level.Tile;
import infinity.main.Main;

public abstract class Weapon extends Rectangle{

	//How much damage the weapon does and its range 
	protected double damage, range;
	//Enemy x and y positions 
	protected double eX, eY;
	//Which direction the attack should go
	protected boolean up,down,left,right;
	//Original x and y positions 
	private double origX, origY;
	//Which type of weapon is being used 
	protected WeaponEnum type;
	//Character 
	private Entity ch;
	//For collisions 
	protected double x,y,width,height;
	//See if a weapon should be removed and if an enemy has been hit 
	protected boolean shouldRemove = false, hitEnemy = false;

	protected Main main;

	/**
	 * @param type Which weapon is being used
	 * @param x cord of the entity
	 * @param y cord of the entity
	 * @param damage the weapon does
	 * @param range how far the weapon travels 
	 * @param width of the weapons collision
	 * @param height of the weapons collision
	 * @param main
	 * @param up which direction the character is facing 
	 * @param down which direction the character is facing 
	 * @param left which direction the character is facing 
	 * @param right which direction the character is facing 
	 * @param e is the entity using the weapon 
	 */
	public Weapon(WeaponEnum type, double x, double y, double baseDamage, double range, double width, double height, Main main, boolean up, boolean down, boolean left, boolean right, Entity e){
		this.x = x;
		this.y = y;
		this.origX = x;
		this.origY = y;
		this.damage = baseDamage;
		this.range = range;
		this.width = width;
		this.height = height;
		this.main = main;
		this.up = up;
		this.down = down;
		this.left = left;
		this.right = right;
		this.ch = e;
		this.type = type;
	}

	/**
	 * Checks to see if player collides with an entity 
	 * @param entity is an entity 
	 * @param player is the entity we are checking is the player
	 * @return true if they collide, false if they dont 
	 */
	public boolean hasColided(Entity entity, boolean player){
		setBounds((int)x,(int)y,(int)width,(int)height);
		if(player){
			entity.setBounds((int) (main.getCamOfSetX() + entity.getPx()), (int) (main.getCamOfSetY() + entity.getPy()), (int)entity.getPWidth(), (int)entity.getPHeight());
		}else
			entity.setBounds((int)entity.getX(), (int)entity.getY(), (int)entity.getWidth(), (int)entity.getHeight());
		return this.intersects(entity);
	}

	/**
	 * To make sure the ranged weapon has not fired passed it sets range
	 * @return true if the range weapon has traveled it's maximum range, false if it has not  
	 */
	public boolean exceedRange(){
		if(Math.abs(Math.abs(x) - Math.abs(origX)) == range)
			return true;
		if(Math.abs(Math.abs(y) - Math.abs(origY)) == range)
			return true;
		return false;
	}

	/**
	 * Checks to see if weapons collided with a solid in order to stop it 
	 * @return true if it collided with a solid, false if it did not
	 */
	public boolean colidesWithSolid(){
		if(main.getPlayer().isFacingRight() || main.getPlayer().isFacingDown())
			return main.getLevel().tileIsSolid((int) x /Tile.size + 1, (int) y / Tile.size + 1);
		return main.getLevel().tileIsSolid((int) x /Tile.size, (int) y /Tile.size);
	}

	/**
	 * Checks to see if the player or if the entity got hit
	 */
	public void hitEnemy(){
		if(colidesWithSolid()|| exceedRange()){
			shouldRemove = true;
			return;
		}
		//If any of the enemies or boss collide with the player, the player takes damage  
		for(Entity i : main.getEntities()){
			if(ch.getType() == EntityEnum.ENEMY || ch.getType() == EntityEnum.BOSS){
				if(this.hasColided(main.getPlayer(), true)){
					enemyEffect(main.getPlayer());
					main.getPlayer().takeDamage(damage);
					hitEnemy = true;
					shouldRemove = true;
				}
			}
			//If one of the players weapons collides with the enemies the enemy takes damage
			else if(ch.getType() == EntityEnum.PLAYER){
				if(this.hasColided(i, false) && i.getType() != EntityEnum.NPC && i.getType() != EntityEnum.PET){
					damageEnemy((Enemy)i);
					enemyEffect((Enemy)i);
				}
			}
			else if(ch.getType() == EntityEnum.PET){
				if(this.hasColided(i, false) && i.getType() != EntityEnum.NPC && i.getType() != EntityEnum.PLAYER
						&& i.getType() != EntityEnum.PET){
					damageEnemy((Enemy)i);
					enemyEffect((Enemy)i);
				}
			}
		}
	}
	
	public void damageEnemy(Enemy i){
		Enemy e = (Enemy) i;
		eX = e.getX();
		eY = e.getY();
		e.getDamaged(damage);
		hitEnemy = true;
		//Magic can go through enemies other weapons cannot 
		if(this.type != WeaponEnum.LIGHTNING && this.type != WeaponEnum.EARTH && this.type != WeaponEnum.FIRE 
				&& this.type != WeaponEnum.WATER && this.type != WeaponEnum.WIND){
			shouldRemove = true;
		}else
			shouldRemove = false;
		enemyEffect(e);
	}
	/**special damage to enemy based on magic properties
	 * @param e is the enemy affected by magic
	 */
	public void enemyEffect(Entity e){
		if(type == WeaponEnum.LIGHTNING || type == WeaponEnum.WATER)
			e.stun();
		else if(type == WeaponEnum.WIND || type == WeaponEnum.EARTH)
			e.dizzy();
		else if (type == WeaponEnum.FIRE)
			e.burn();
	}

	/**
	 * Checks to see if a weapon should be removed 
	 * @return true if weapon should be removed, false if it should  not
	 */
	public boolean removeWeapon(){
		if(shouldRemove)
			return true;
		return false;
	}

	/**
	 * Attacks enemies 
	 */
	public void basicAttack(){
		if(!shouldRemove){
			if(up){
				setY(getY() - 1);
				hitEnemy();
			}else if(down){
				setY(getY() + 1);
				hitEnemy();
			}else if(left){
				setX(getX() - 1);
				hitEnemy();
			}else if(right){
				setX(getX() + 1);
				hitEnemy();
			}
		}
	}
	public abstract void attack(Graphics g);

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public boolean isHitEnemy() {
		return hitEnemy;
	}

	public void setHitEnemy(boolean hitEnemy) {
		this.hitEnemy = hitEnemy;
	}

	public boolean shouldRemove() {
		return shouldRemove;
	}

	public void setShouldRemove(boolean shouldRemove) {
		this.shouldRemove = shouldRemove;
	}

	public double getRange() {
		return range;
	}

}
