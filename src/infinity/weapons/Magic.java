/**
 * Class for the magic weapon 

 * Magic.java
 * @author Nim Wijetunga
 */

package infinity.weapons;

import java.awt.Color;
import java.awt.Graphics;

import infinity.entites.Entity;
import infinity.handlers.SoundFX;
import infinity.level.Tile;
import infinity.main.Main;

public class Magic extends Weapon{

	//How fast the magic travels
	private int speed;

	protected Main main;

	//For Animation
	private int aniFrame = 0;
	private int aniTime =  35;
	private int aniDelta = 0;

	private long delayTimer;
	private boolean doneDelay = true;

	/**
	 * @param type of magic being used
	 * @param x cord of the entity
	 * @param y cord of the entity
	 * @param damage the amount of damage done by the weapon 
	 * @param range how far the magic travels 
	 * @param width of the magic collision 
	 * @param height of the magic collision
	 * @param main
	 * @param up which direction the character is facing 
	 * @param down which direction the character is facing 
	 * @param left which direction the character is facing 
	 * @param right which direction the character is facing 
	 * @param e is the entity using the weapon
	 */
	public Magic(WeaponEnum type, double x, double y, double damage, double range,double width, double height, Main main, boolean up, boolean down,boolean left, boolean right, Entity e) {
		super(type, x, y, damage + 30, range, width, height, main, up, down, left, right, e);
		this.speed = 3;
		this.main = main;
	}

	/**
	 * Plays a different sound effect based on which magic is being used
	 * @param magic which magic is used
	 */
	public void soundEffect(SoundFX magic){
		if(aniFrame < 11){
			magic.start();
		}else{
			magic.stopClip();
		}
	}

	/**
	 * Draws the type of magic being using and in a certain direction 
	 * @param g is the graphics component 
	 */
	public void render(Graphics g){
		animate();
		SoundFX magic = null;
		if(type == WeaponEnum.FIRE){
			magic = main.getLevel().getClip("fire");
			soundEffect(magic);
			if(right)
				g.drawImage(Tile.magic, (int) (getX() - main.getCamOfSetX()),  (int) (getY() - main.getCamOfSetY()), (int) ((getX() + (width ) - main.getCamOfSetX())+Tile.size), (int) ((getY() + (height )  - main.getCamOfSetY())+Tile.size),aniFrame*Tile.size, 0,  aniFrame*Tile.size+Tile.size, Tile.size, null);
			else if (left)
				g.drawImage(Tile.magic, (int) (getX() - main.getCamOfSetX()),  (int) (getY() - main.getCamOfSetY()), (int) ((getX() + (width ) - main.getCamOfSetX())+Tile.size), (int) ((getY() + (height )  - main.getCamOfSetY())+Tile.size),aniFrame*Tile.size, 64,  aniFrame*Tile.size+Tile.size, 2*Tile.size, null);
			else if (up)
				g.drawImage(Tile.magic, (int) (getX() - main.getCamOfSetX()),  (int) (getY() - main.getCamOfSetY()), (int) ((getX() + (width ) - main.getCamOfSetX())+Tile.size), (int) ((getY() + (height )  - main.getCamOfSetY())+Tile.size),aniFrame*Tile.size, 128,  aniFrame*Tile.size+Tile.size, 3*Tile.size, null);
			else if (down)
				g.drawImage(Tile.magic, (int) (getX() - main.getCamOfSetX()),  (int) (getY() - main.getCamOfSetY()), (int) ((getX() + (width ) - main.getCamOfSetX())+Tile.size), (int) ((getY() + (height )  - main.getCamOfSetY())+Tile.size),aniFrame*Tile.size, 192,  aniFrame*Tile.size+Tile.size, 4*Tile.size, null);
		}if(type == WeaponEnum.EARTH){
			magic = main.getLevel().getClip("earth");
			soundEffect(magic);
			if(right){
				g.drawImage(Tile.magic, (int) (getX() - main.getCamOfSetX()),  (int) (getY() - main.getCamOfSetY()), (int) ((getX() + (width ) - main.getCamOfSetX())+Tile.size), (int) ((getY() + (height )  - main.getCamOfSetY())+Tile.size),aniFrame*Tile.size, 256,  aniFrame*Tile.size+Tile.size, 5*Tile.size, null);
			}else if (left){
				g.drawImage(Tile.magic, (int) (getX() - main.getCamOfSetX()),  (int) (getY() - main.getCamOfSetY()), (int) ((getX() + (width ) - main.getCamOfSetX())+Tile.size), (int) ((getY() + (height )  - main.getCamOfSetY())+Tile.size),aniFrame*Tile.size, 320,  aniFrame*Tile.size+Tile.size, 6*Tile.size, null);
			}else if (up){
				g.drawImage(Tile.magic, (int) (getX() - main.getCamOfSetX()),  (int) (getY() - main.getCamOfSetY()), (int) ((getX() + (width ) - main.getCamOfSetX())+Tile.size), (int) ((getY() + (height )  - main.getCamOfSetY())+Tile.size),aniFrame*Tile.size, 384,  aniFrame*Tile.size+Tile.size, 7*Tile.size, null);
			}else if (down){
				g.drawImage(Tile.magic, (int) (getX() - main.getCamOfSetX()),  (int) (getY() - main.getCamOfSetY()), (int) ((getX() + (width ) - main.getCamOfSetX())+Tile.size), (int) ((getY() + (height )  - main.getCamOfSetY())+Tile.size),aniFrame*Tile.size, 448,  aniFrame*Tile.size+Tile.size, 8*Tile.size, null);
			}
		}if (type == WeaponEnum.WATER){
			magic = main.getLevel().getClip("water");
			soundEffect(magic);
			g.drawImage(Tile.magic, (int) (getX() - main.getCamOfSetX()),  (int) (getY() - main.getCamOfSetY()), (int) ((getX() + (width ) - main.getCamOfSetX())+Tile.size), (int) ((getY() + (height )  - main.getCamOfSetY())+Tile.size),aniFrame*Tile.size, 640,  aniFrame*Tile.size+Tile.size, 11*Tile.size, null);
		}if(type == WeaponEnum.LIGHTNING){
			magic = main.getLevel().getClip("lightning");
			soundEffect(magic);
			g.drawImage(Tile.magic, (int) (getX() - main.getCamOfSetX()),  (int) (getY() - main.getCamOfSetY()), (int) ((getX() + (width ) - main.getCamOfSetX())+Tile.size), (int) ((getY() + (height )  - main.getCamOfSetY())+Tile.size),aniFrame*Tile.size, 512,  aniFrame*Tile.size+Tile.size, 9*Tile.size, null);
		}
		if(type == WeaponEnum.WIND){
			magic = main.getLevel().getClip("wind");
			soundEffect(magic);
			g.drawImage(Tile.magic, (int) (getX() - main.getCamOfSetX()),  (int) (getY() - main.getCamOfSetY()), (int) ((getX() + (width ) - main.getCamOfSetX())+Tile.size), (int) ((getY() + (height )  - main.getCamOfSetY())+Tile.size),aniFrame*Tile.size, 576,  aniFrame*Tile.size+Tile.size, 10*Tile.size, null);
		}
	}

	/**
	 * Animates the magic 
	 */
	public void animate (){
		aniDelta++;
		if(aniDelta >= aniTime){
			aniFrame++;
			aniDelta = 0;
			if(aniFrame > 15)
				aniFrame = 0;
		}
	}

	/**
	 * Starts to render the magic and calls basic attack from the weapons class to attack 
	 * @param g is the graphics component 
	 */
	public void attack(Graphics g){
		for(int i = 0; i < speed; i++){
			g.setColor(Color.BLACK);
			render(g);
			checkDelay();
			if(doneDelay){
				basicAttack();
				delayTimer = System.nanoTime();
				doneDelay = false;
			}
		}
	}

	public void checkDelay(){
		long elapsed = (System.nanoTime() - delayTimer);
		if(elapsed > 1){
			doneDelay = true;
		}
	}
}



