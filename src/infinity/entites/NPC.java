/**
 * NPC class to control all NPC (Non Playable Character) interactions
 * NPC.java
 * @author Umar Syed
 */

package infinity.entites;

import java.awt.Graphics;

import infinity.level.Tile;
import infinity.main.Main;

public class NPC extends Entity{
	
	private int[][] pImgDown = {{0,2}, {1,2}, {2,2}, {3,2}, {4,2}, {5,2}, {6,2}, {7,2}, {8,2}};
	/**
	 * @param main
	 * @param x position of entity
	 * @param y position of entity
	 * @param width of entity 
	 * @param height of entity
	 * @param health of entity
	 */
	public NPC(Main main, double x, double y, int width, int height, double health) {
		//Pass info to the super class
		super(EntityEnum.NPC, main, x, y, width, height, health);
	}
	
	//Does nothing
	public void move(double delta){}
	
	/**
	 * Opens the NPC shop
	 * changes to open shop to true and pauses the game
	 */
	public void openShop(){
		main.setOpenShop(true);
		main.setPaused(true);	
	}
	
	/**render the NPC
	 * g is the graphics component
	 */
	public void render(Graphics g){
		g.drawImage(Tile.npc, (int) (x - main.getCamOfSetX()), (int) (y - main.getCamOfSetY()), (int) (x + getWidth() - main.getCamOfSetX()), (int) (y + getHeight() - main.getCamOfSetY()), pImgDown[0][0]*Tile.size, pImgDown[0][1]*Tile.size, pImgDown[0][0]*Tile.size + Tile.size,pImgDown[0][1]*Tile.size + Tile.size, null);
	}

}
