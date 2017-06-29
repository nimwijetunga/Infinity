package infinity.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import infinity.level.Tile;

public class Menu {
	
	private Main main;
	
	public static final Rectangle respawn = new Rectangle();
	public static final Rectangle start = new Rectangle();
	public static final Rectangle instructions = new Rectangle();
	public static final Rectangle instructionsBack = new Rectangle();
	public static final Rectangle quit = new Rectangle();

	
	public Menu(Main main){
		this.main = main;
		respawn.setBounds(425, 105, 350, 90);
		start.setBounds(425,205, 350, 90);
		instructions.setBounds(425, 320, 350, 90);
		quit.setBounds(425,430, 350, 90);
		instructionsBack.setBounds(850,0, 350, 90);
	}
	
	public void respawn(Graphics g){
		if(main.isPlayerDead()){
			g.drawImage(Tile.respawn, 0, 0, null);
		}
		
	}
	
	public void mainMenu(Graphics g){
		if(main.isMainMenu())
			g.drawImage(Tile.mainMenu, 0, 0, null);
		instructions(g);
		quit();
		g.dispose();
	}
	
	public void instructions(Graphics g){
		if(main.isInstructions()){
			g.drawImage(Tile.instructions, 0, 0, null);
		}
	}
	
	public void quit(){
		if(main.quit && main.isMainMenu())
			System.exit(0);
	}

}
