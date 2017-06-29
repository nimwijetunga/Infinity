/**
 * Used to render and animate all background tiles
 * Background.java
 * @author Umar Syed
 */
package infinity.level;
import java.awt.Graphics;
import java.awt.Rectangle;

import infinity.main.Main;

public class Background extends Rectangle{

	private static final long serialVersionUID = 1L;
	
	public int[] id = { -1, -1 };
	private int aniTime =0;
	private int aniSpeed = 60;
	private int aniFrame = 0;
	Main main;

	public Background(Main main, Rectangle rect, int id[]) {
		setBounds(rect);
		this.id = id;
		this.main = main;
	}
	/**
	 * changes the animation frame of the background animation
	 */
	public void aniUpdate (){
		aniTime++; 
		if(aniTime >= aniSpeed){
			aniFrame++;
			aniTime = 0;
			if(aniFrame > 1){
				aniFrame = 0;
			}
		}
	}
	/**
	 * Renders the background tiles
	 * @param g
	 */
	public void render(Graphics g){
		aniUpdate();
		//if the background tile is a tile on the edge, draw it depending of what animation frame it is on
		if (id[0] == Tile.waterHT[0] && id[1] == Tile.waterHT[1]){
			g.drawImage(Tile.background, x - (int) main.getCamOfSetX(), y - (int)main.getCamOfSetY(), x + width - (int)main.getCamOfSetX(), y + height - (int)main.getCamOfSetY(), (id[0]+ aniFrame)*Tile.size, id[1]*Tile.size, (id[0]+aniFrame)*Tile.size + Tile.size,id[1]*Tile.size + Tile.size, null);
		}else if (id[0] == Tile.waterHB[0] && id[1] == Tile.waterHB[1]){
			g.drawImage(Tile.background, x - (int) main.getCamOfSetX(), y - (int)main.getCamOfSetY(), x + width - (int)main.getCamOfSetX(), y + height - (int)main.getCamOfSetY(), (id[0]+ aniFrame)*Tile.size, id[1]*Tile.size, (id[0]+aniFrame)*Tile.size + Tile.size,id[1]*Tile.size + Tile.size, null);
		}else if (id[0] == Tile.waterVL[0] && id[1] == Tile.waterVL[1]){
			g.drawImage(Tile.background, x - (int) main.getCamOfSetX(), y - (int)main.getCamOfSetY(), x + width - (int)main.getCamOfSetX(), y + height - (int)main.getCamOfSetY(), (id[0]+ aniFrame)*Tile.size, id[1]*Tile.size, (id[0]+aniFrame)*Tile.size + Tile.size,id[1]*Tile.size + Tile.size, null);
		}else if (id[0] == Tile.waterVR[0] && id[1] == Tile.waterVR[1]){
			g.drawImage(Tile.background, x - (int) main.getCamOfSetX(), y - (int)main.getCamOfSetY(), x + width - (int)main.getCamOfSetX(), y + height - (int)main.getCamOfSetY(), (id[0]+ aniFrame)*Tile.size, id[1]*Tile.size, (id[0]+aniFrame)*Tile.size + Tile.size,id[1]*Tile.size + Tile.size, null);
		}else if (id[0] == Tile.waterBL[0] && id[1] == Tile.waterBL[1]){
			g.drawImage(Tile.background, x - (int) main.getCamOfSetX(), y - (int)main.getCamOfSetY(), x + width - (int)main.getCamOfSetX(), y + height - (int)main.getCamOfSetY(), (id[0]+ aniFrame)*Tile.size, id[1]*Tile.size, (id[0]+aniFrame)*Tile.size + Tile.size,id[1]*Tile.size + Tile.size, null);
		}else if (id[0] == Tile.waterBR[0] && id[1] == Tile.waterBR[1]){
			g.drawImage(Tile.background, x - (int) main.getCamOfSetX(), y - (int)main.getCamOfSetY(), x + width - (int)main.getCamOfSetX(), y + height - (int)main.getCamOfSetY(), (id[0]+ aniFrame)*Tile.size, id[1]*Tile.size, (id[0]+aniFrame)*Tile.size + Tile.size,id[1]*Tile.size + Tile.size, null);
		}else if (id[0] == Tile.waterTL[0] && id[1] == Tile.waterTL[1]){
			g.drawImage(Tile.background, x - (int) main.getCamOfSetX(), y - (int)main.getCamOfSetY(), x + width - (int)main.getCamOfSetX(), y + height - (int)main.getCamOfSetY(), (id[0]+ aniFrame)*Tile.size, id[1]*Tile.size, (id[0]+aniFrame)*Tile.size + Tile.size,id[1]*Tile.size + Tile.size, null);
		}else if (id[0] == Tile.waterTR[0] && id[1] == Tile.waterTR[1]){
			g.drawImage(Tile.background, x - (int) main.getCamOfSetX(), y - (int)main.getCamOfSetY(), x + width - (int)main.getCamOfSetX(), y + height - (int)main.getCamOfSetY(), (id[0]+ aniFrame)*Tile.size, id[1]*Tile.size, (id[0]+aniFrame)*Tile.size + Tile.size,id[1]*Tile.size + Tile.size, null);
		}
		else
		g.drawImage(Tile.background, (int) (x - main.getCamOfSetX()), (int) (y - main.getCamOfSetY()), (int) (x + width - main.getCamOfSetX()), (int) (y + height - main.getCamOfSetY()), id[0]*Tile.size, id[1]*Tile.size, id[0]*Tile.size + Tile.size,id[1]*Tile.size + Tile.size, null);
	}
}
