/**
 * Used to draw all solid tiles
 * Solid.java
 * @author Umar Syed
 */
package infinity.level;

import java.awt.Graphics;

import java.awt.Rectangle;

import infinity.main.Main;

public class Solid extends Rectangle{

	private static final long serialVersionUID = 1L;
	private int aniTime =0;
	private int aniSpeed = 35;
	private int aniFrame = 0;

	public int[] id = { -1, -1 };
	Main main;
	
	Solid(Main main, Rectangle rect, int id[]) {
		setBounds(rect);
		this.main = main;
		this.id = id;
	}
	/**
	 * Changes the animation frame
	 */
	public void aniUpdate (){
		aniTime++; 
		if(aniTime >= aniSpeed){
			aniFrame++;
			aniTime = 0;
			if(aniFrame > 2){
				aniFrame = 0;
			}
		}
	}
	/**
	 * draws all solid tiles
	 * @param g
	 */
	public void render(Graphics g){
		aniUpdate();
		// if it is a water tile draw it depending on what animation frame its on
		if (id[0] == 4 && id[1] == 0){
			g.drawImage(Tile.terrain, (int) (x - main.getCamOfSetX()), (int) (y - main.getCamOfSetY()), (int) (x + width - main.getCamOfSetX()), (int) (y + height - main.getCamOfSetY()), (id[0]+aniFrame)*Tile.size, id[1]*Tile.size, (id[0]+aniFrame)*Tile.size + Tile.size,id[1]*Tile.size + Tile.size, null);
		}
		else
			g.drawImage(Tile.terrain, (int) (x - main.getCamOfSetX()), (int) (y - main.getCamOfSetY()), (int) (x + width - main.getCamOfSetX()), (int) (y + height - main.getCamOfSetY()), id[0]*Tile.size, id[1]*Tile.size, id[0]*Tile.size + Tile.size,id[1]*Tile.size + Tile.size, null);
		//g.drawRect((int) (x - main.getCamOfSetX()), (int) (y - (int)main.getCamOfSetY()), Tile.size, Tile.size);

	}

}
