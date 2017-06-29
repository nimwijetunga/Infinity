/**
 * To spawn item drops if at the location where an enemy as killed
 * ItemDrop.java
 * @author Nim Wijetunga
 */

package infinity.drops;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

import infinity.level.Tile;
import infinity.main.Main;

public class ItemDrop extends Rectangle{
	protected Main main;
	protected boolean hasDropped;
	private DropEnum type;

	/**
	 * @param type of item
	 * @param x position of item
	 * @param y position of item
	 * @param width of item
	 * @param height of item
	 * @param main
	 * @param hasDropped if the item has been dropped or not
	 */
	public ItemDrop(DropEnum type, int x, int y, int width, int height, Main main, boolean hasDropped){
		this.type = type;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.main = main;
		this.hasDropped = hasDropped;
	}
	
	/**generate a random number for the amount of "items" the player will receive
	 * @return returns the number of items player gets
	 */
	public int value(){
		Random random = new Random();
		return random.nextInt(9) + 1;
	}
	
	/**draw the items
	 * @param g is the graphics component
	 */
	public void render(Graphics g){
		//Draw based on the type of item
		if(type == DropEnum.COIN)
			g.drawImage(Tile.coin, x - (int) main.getCamOfSetX(), y - (int)main.getCamOfSetY(), width, height, null);
		else if(type == DropEnum.AMMO)
			g.drawImage(Tile.arrowP, x - (int) main.getCamOfSetX(), y - (int)main.getCamOfSetY(), width, height, null);
		else if(type == DropEnum.HEALTH)
			g.drawImage(Tile.health, x - (int) main.getCamOfSetX(), y - (int)main.getCamOfSetY(), width, height, null);
		else if(type == DropEnum.MANA)
			g.drawImage(Tile.mana, x - (int) main.getCamOfSetX(), y - (int)main.getCamOfSetY(), width, height, null);
		
	}
	
	/**checks to see if the item collides with the player
	 * @return true if the player collides with item false otherwise
	 */
	public boolean colidesWith(){
		//Set bounds of player and item
		Rectangle player = new Rectangle();
		player.setBounds((int) (main.getCamOfSetX() + main.getPlayer().getX()), (int) (main.getCamOfSetY() + main.getPlayer().getY()), (int)main.getPlayer().getWidth(), (int)main.getPlayer().getHeight());
		this.setBounds(x,y,width,height);
		return player.intersects(this);
	}
	
	public DropEnum getType() {
		return type;
	}
}