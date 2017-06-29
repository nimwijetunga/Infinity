/**
 * Used for collisions and rendering images
 * Tile.java
 * @author Umar Syed
 */
package infinity.level;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;


import javax.imageio.ImageIO;

import infinity.entites.Entity;
import infinity.entites.Player;
import infinity.main.Main;
import infinity.weapons.Weapon;

public class Tile extends Rectangle{

	protected double x,y;
	protected int [] id;
	public double getX() {
		return x;
	}


	public double getY() {
		return y;
	}
	private static String [] path = new String [10];

	public final static int[] blank = {-1, -1}; 

	public final static int[] grass = {0,0};

	public final static int[] pathV = {1,0};
	public final static int[] pathH = {6,0};
	public final static int[] pathBL = {2,0};
	public final static int[] pathBR = {3,0};
	public final static int[] pathTL = {5,0};
	public final static int[] pathTR = {4,0};

	public final static int [] stairs1 = {0,3};
	public final static int [] stairs2 = {1,3};

	public final static int[] waterHT = {2,1};
	public final static int[] waterHB = {4,2};
	public final static int[] waterVL = {6,1};
	public final static int[] waterVR = {0,2};
	public final static int[] waterBL = {2,2};
	public final static int[] waterBR = {6,2};
	public final static int[] waterTL = {0,1};
	public final static int[] waterTR = {4,1};

	//Collisions
	public final static int[] wall = {0,0};
	public final static int [] water = {4,0};
	public final static int [] wallGrass = {1,0};
	public final static int [] wall2 = {3,0};
	public final static int [] wallGrass2 = {2,0};

	public final static int [] houseTL = {2,1};
	public final static int [] houseTR = {3,1};
	public final static int [] houseBL = {2,2};
	public final static int [] houseBR = {3,2};

	public final static int [] house2TL = {4,1};
	public final static int [] house2TR = {5,1};
	public final static int [] house2BL = {4,2};
	public final static int [] house2BR = {5,2};

	public final static int [] house3TL = {6,1};
	public final static int [] house3TR = {7,1};
	public final static int [] house3BL = {6,2};
	public final static int [] house3BR = {7,2};

	public final static int [] fountainTL = {0,3};
	public final static int [] fountainTR = {1,3};
	public final static int [] fountainBL = {0,4};
	public final static int [] fountainBR = {1,4};

	public final static int [] fenceTL = {2,3};
	public final static int [] fenceTR = {3,3};
	public final static int [] fenceBL = {2,4};
	public final static int [] fenceBR = {3,4};

	public final static int [] fenceT = {4,3};
	public final static int [] fenceB = {4,4};

	public final static int [] fenceL = {2,5};
	public final static int [] fenceR = {3,5};

	public final static int [] bushH1 = {5,3};
	public final static int [] bushH2 = {6,3};
	public final static int [] bushH3 = {7,3};

	public final static int [] bushV1 = {7,4};
	public final static int [] bushV2 = {7,5};
	public final static int [] bushV3 = {7,6};

	public final static int size = 64; 

	public static BufferedImage terrain, background, items, characters, charactersTop, charactersBottom, npc, enemies, health,sword,blood, boss1, dizzy, pet; 
	public static BufferedImage weaponBow, weaponArrow, weaponSword, coin, arrowP, button, arrowDisp, mana,magic, mainMenu,instructions,respawn;
	public static BufferedImage inventory, shop,charactersHead,charactersHand,charactersShoulder,charactersShoes,charactersBelt;

	protected TileEnum type;

	static Main main;

	Tile(Main main, int x, int y){
		for (int i = 0; i < path.length; i++)
			path[i]= "-1";
		this.type = TileEnum.BLANK;
		this.x = x;
		this.y = y;
		this.main = main;
	}

	/**
	 * Checks to see if a player or if an enemy is colliding with a solid tile
	 * @param e is the enemy
	 * @param player is the player
	 * @return true if colliding, false if not colliding 
	 */
	public boolean colidesWith(Entity e, boolean player){
		this.setBounds((int) x, (int)y, Tile.size, Tile.size);
		if(player)
			e.setBounds((int) (main.getCamOfSetX() + e.getPx()), (int) (main.getCamOfSetY() + e.getPy()), (int)e.getWidth(), (int)e.getHeight());
		else
			e.setBounds((int)e.getX(), (int)e.getY(), (int)e.getWidth(), (int)e.getHeight());
		return this.intersects(e);
	}

	/**
	 * Checks to see if a weapon is colliding with a solid tile 
	 * @param w is the weapon being used such as magic, arrow or sword
	 * @return true if colliding, false if not colliding
	 */
	public boolean colidesWith(Weapon w){
		this.setBounds((int) x, (int)y, Tile.size, Tile.size);
		Rectangle weapon = new Rectangle();
		weapon.setBounds((int)w.getX(), (int)w.getX(), (int)w.getWidth(), (int)w.getHeight());
		return this.intersects(weapon);
	}
/**
 * add all the equipID to the path array for use of changing the image path
 * @param equipID is the array holding the id of all the items equipped
 */
	public static void setPath (int equipID[]){
		for (int i  = 0; i < equipID.length; i++){
			path[i] = Integer.toString(equipID[i]);
		}
	}

	/**
	 * Getting all image paths 
	 */
	public static void loadImages(){
		setPath(main.getShop().inventory.getEquipID());
		try {
			background = ImageIO.read(new File("res/world/bg.png"));
			terrain = ImageIO.read(new File("res/world/solids.png"));
			// Character and Armour
			characters = ImageIO.read(new File("res/world/char3.png"));
			charactersHead =ImageIO.read(new File("res/world/charhead"+path[0]+".png"));
			charactersTop = ImageIO.read(new File("res/world/chartop"+path[2]+".png"));
			charactersShoulder = ImageIO.read(new File("res/world/charshoulders"+path[3]+".png"));
			charactersHand = ImageIO.read(new File("res/world/charhands"+path[5]+".png"));
			charactersBottom = ImageIO.read(new File("res/world/charbottom"+path[8]+".png"));
			charactersShoes = ImageIO.read(new File("res/world/charshoes"+path[9]+".png"));
			charactersBelt = ImageIO.read(new File("res/world/charBelt"+path[4]+".png"));

			weaponBow = ImageIO.read(new File("res/world/bow.png"));
			weaponArrow = ImageIO.read(new File("res/world/arrow.png"));
			weaponSword = ImageIO.read(new File("res/world/sword.png"));
			items = ImageIO.read(new File("res/world/items.png"));
			npc = ImageIO.read(new File("res/world/npc.png"));
			health = ImageIO.read(new File("res/world/health.png"));
			enemies = ImageIO.read(new File("res/world/enemies.png"));
			coin = ImageIO.read(new File("res/world/coin.png"));
			mana = ImageIO.read(new File("res/world/mana.gif"));
			arrowP = ImageIO.read(new File("res/world/arrowP.png"));
			button = ImageIO.read(new File("res/world/button.png"));
			blood = ImageIO.read(new File("res/world/blood.png"));
			arrowDisp = ImageIO.read(new File("res/world/arrowDisp.png"));
			boss1 = ImageIO.read(new File("res/world/boss1.png"));
			magic = ImageIO.read(new File("res/world/magic.png"));
			mainMenu = ImageIO.read(new File("res/world/MainMenu.png"));
			instructions = ImageIO.read(new File("res/world/instructions.png"));
			respawn = ImageIO.read(new File("res/world/respawn.png"));
			inventory = ImageIO.read(new File("res/world/inventory.png"));
            shop = ImageIO.read(new File("res/world/shop.png"));
            dizzy =  ImageIO.read(new File("res/world/dizzy.png"));
            pet =  ImageIO.read(new File("res/world/pet.png"));
		} catch (Exception e) {
			System.out.println("Error Loading Images");
		}
	}
}
