/**
 * Class for the arrow weapon
 
 * Arrow.java
 * @author Nim Wijetunga
 */
package infinity.weapons;

import java.awt.Color;
import java.awt.Graphics;

import infinity.entites.Entity;
import infinity.level.Tile;
import infinity.main.Main;

public class Arrow extends Weapon{
	
	//How fast the arrow moves 
	private int speed;
	
	/**
	 * @param x cord of the entity 
	 * @param y cord of the entity 
	 * @param damage the arrow does
	 * @param range the arrow travels  
	 * @param width of the arrow's collision 
	 * @param height of the arrow's collision 
	 * @param main
	 * @param up which direction the character is facing 
	 * @param down which direction the character is facing 
	 * @param left which direction the character is facing 
	 * @param right which direction the character is facing 
	 * @param e is the entity using the weapon 
	 */
	public Arrow(WeaponEnum type, double x, double y, double damage, double range, double width, double height, Main main, boolean up,boolean down,boolean left, boolean right, Entity e) {
		super(WeaponEnum.ARROW, x, y, damage + 15, range, width, height,main,up,down,left,right,e);
		this.main = main;
		this.speed = 3;
	}
	
	/**
	 * Draw the arrow being fired in a certain direction
	 * @param g is the graphics component
	 */
	public void drawArrow(Graphics g){
		//Put Image Code Here
		int ofset = 20;
		if (up) {
			g.drawImage(Tile.weaponArrow, (int) (getX() - main.getCamOfSetX()),  (int) (getY() - main.getCamOfSetY())-12, (int) ((getX() + (width - ofset) - main.getCamOfSetX())+Tile.size), (int) ((getY() + (height - ofset)  - main.getCamOfSetY())+Tile.size)-12, 24*Tile.size, 0, 24*Tile.size + Tile.size, Tile.size, null);
		}
		if (down) {
			g.drawImage(Tile.weaponArrow, (int) (getX() - main.getCamOfSetX()),  (int) (getY() - main.getCamOfSetY())-12, (int) ((getX() + (width - ofset) - main.getCamOfSetX())+Tile.size), (int) ((getY() + (height - ofset) - main.getCamOfSetY())+Tile.size)-12, 24*Tile.size, 2*Tile.size, 24*Tile.size + Tile.size,2*Tile.size + Tile.size, null);
		}
		if (left) {
			g.drawImage(Tile.weaponArrow, (int) (getX() - main.getCamOfSetX()),  (int) (getY() - main.getCamOfSetY())-12, (int) ((getX() + (width - ofset)  - main.getCamOfSetX())+Tile.size), (int) ((getY() + (height - ofset) - main.getCamOfSetY())+Tile.size)-12, 24*Tile.size, Tile.size, 24*Tile.size + Tile.size,Tile.size + Tile.size, null);
		}
		if (right) {
			g.drawImage(Tile.weaponArrow, (int) (getX() - main.getCamOfSetX()),  (int) (getY() - main.getCamOfSetY())-12, (int) ((getX() + (width - ofset) - main.getCamOfSetX())+Tile.size), (int) ((getY() + (height - ofset) - main.getCamOfSetY())+Tile.size)-12, 24*Tile.size, 3*Tile.size, 24*Tile.size + Tile.size,3*Tile.size + Tile.size, null);
		}
	}
	/**
	 * Calls the method to draw the arrow and calls the basicAttack method from weapon to hit enemies
	 * @param g is the graphics component
	 */
	public void attack(Graphics g){
		for(int i = 0; i < speed; i++){
			g.setColor(Color.BLACK);
			drawArrow(g);
			basicAttack();
			}
		}
	}
	
	

