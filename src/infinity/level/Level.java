/**
 * Used in rendering all tiles, enemies
 * getting correct sound effects and collisions
 * Level.java
 * @author Umar Syed
 */
package infinity.level;

import infinity.entites.*;
import infinity.handlers.SoundFX;
import infinity.main.Main;

import java.awt.Graphics;
import java.awt.Rectangle;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

public class Level {

	private int width = 100;

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	private int height = 100; 

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	private Background[][] bg = new Background[width][height];
	private Solid[][] solid = new Solid[width][height];
	public Tile[][] tile = new Tile[width][height];
	private TiledMap map = null; 
	
	//For switching map areas 
	private final String dPath = "res/World/Level_"; 
	private String path = dPath; 
	private Main main;


/**
 * Set all tiles to blank
 * @param main 
 * @param id is what level we are on
 */
	public Level (Main main, int id){
		this.main = main;
		path = dPath + Integer.toString(id) + ".tmx";

		try{
			map = new TiledMap(path, false);
		}catch (SlickException e){
			System.out.println("Error Loading Map");
		}

		// For adding multiple background layers 
		for (int x = 0; x < bg.length; x++){
			for (int y = 0; y < bg[0].length; y++ ){
				bg[x][y] = new Background (main, new Rectangle(x * Tile.size, y * Tile.size, Tile.size, Tile.size), Tile.blank);
				solid[x][y] = new Solid (main,new Rectangle(x * Tile.size, y * Tile.size, Tile.size, Tile.size), Tile.blank); 
				tile[x][y] = new Tile(main, x * Tile.size, y * Tile.size);
			}
		}

		loadWorld(); 
	}

	/**
	 * To render all tiles based on their id (Id can be found in txt files) 
	 */
	public void loadWorld (){

		int background = map.getLayerIndex("background");
		int solids = map.getLayerIndex("collision");

		for (int x = 0; x < bg.length; x++){
			for (int y = 0; y < bg[0].length; y++ ){
				//Grass
				if (map.getTileId(x, y, background) == 1){
					bg[x][y].id = Tile.grass;
					tile[x][y].type = TileEnum.GRASS;
				}
				//Beach
				if(map.getTileId(x, y, background) == 11){
					bg[x][y].id = Tile.waterHT;
					tile[x][y].type = TileEnum.SAND;
				}if(map.getTileId(x, y, background) == 21){
					bg[x][y].id = Tile.waterHB;
					tile[x][y].type = TileEnum.SAND;
				}if(map.getTileId(x, y, background) == 15){
					bg[x][y].id = Tile.waterVL;
					tile[x][y].type = TileEnum.SAND;
				}if(map.getTileId(x, y, background) == 17){
					bg[x][y].id = Tile.waterVR;
					tile[x][y].type = TileEnum.SAND;
				}if(map.getTileId(x, y, background) == 19){
					bg[x][y].id = Tile.waterBL;
					tile[x][y].id = Tile.pathV;
					tile[x][y].type = TileEnum.SAND;
				}if(map.getTileId(x, y, background) == 23){
					bg[x][y].id = Tile.waterBR;
					tile[x][y].type = TileEnum.SAND;
				}if(map.getTileId(x, y, background) == 9){
					bg[x][y].id = Tile.waterTL;
					tile[x][y].type = TileEnum.SAND;
				}if(map.getTileId(x, y, background) == 13){
					bg[x][y].id = Tile.waterTR;
					tile[x][y].type = TileEnum.SAND;
				}
				
				//Path
				if(map.getTileId(x, y, background) == 2){
					bg[x][y].id = Tile.pathV;
					tile[x][y].type = TileEnum.SAND;
				}if(map.getTileId(x, y, background) == 7){
					bg[x][y].id = Tile.pathH;
					tile[x][y].type = TileEnum.SAND;
				}if(map.getTileId(x, y, background) == 3){
					bg[x][y].id = Tile.pathBL;
					tile[x][y].type = TileEnum.SAND;
				}if(map.getTileId(x, y, background) == 4){
					bg[x][y].id = Tile.pathBR;
					tile[x][y].type = TileEnum.SAND;
				}if(map.getTileId(x, y, background) == 6){
					bg[x][y].id = Tile.pathTL;
					tile[x][y].type = TileEnum.SAND;
				}if(map.getTileId(x, y, background) == 5){
					bg[x][y].id = Tile.pathTR;
					tile[x][y].type = TileEnum.SAND;
				}if (map.getTileId(x, y, background) == 25){
					bg[x][y].id = Tile.stairs1;
					tile[x][y].type = TileEnum.SAND;
				}if (map.getTileId(x, y, background) == 26){
					bg[x][y].id = Tile.stairs2;
					tile[x][y].type = TileEnum.SAND;
				}
				
				//solids
				if (map.getTileId(x, y, solids) == 65){
					solid[x][y].id = Tile.wall;
					tile[x][y].type = TileEnum.SOLID;
				}if (map.getTileId(x, y, solids) == 69){
					solid[x][y].id = Tile.water;
					tile[x][y].type = TileEnum.SOLID;
				}if (map.getTileId(x, y, solids) == 66){
					solid[x][y].id = Tile.wallGrass;
					tile[x][y].type = TileEnum.SOLID;
				}if (map.getTileId(x, y, solids) == 68){
					solid[x][y].id = Tile.wall2;
					tile[x][y].type = TileEnum.SOLID;
				}if (map.getTileId(x, y, solids) == 67){
					solid[x][y].id = Tile.wallGrass2;
					tile[x][y].type = TileEnum.SOLID;
				}
				
				if (map.getTileId(x, y, solids) == 75){
					solid[x][y].id = Tile.houseTL;
					tile[x][y].type = TileEnum.SOLID;
				}if (map.getTileId(x, y, solids) == 76){
					solid[x][y].id = Tile.houseTR;
					tile[x][y].type = TileEnum.SOLID;
				}if (map.getTileId(x, y, solids) == 83){
					solid[x][y].id = Tile.houseBL;
					tile[x][y].type = TileEnum.SOLID;
				}if (map.getTileId(x, y, solids) == 84){
					solid[x][y].id = Tile.houseBR;
					tile[x][y].type = TileEnum.SOLID;
				}if (map.getTileId(x, y, solids) == 77){
					solid[x][y].id = Tile.house2TL;
					tile[x][y].type = TileEnum.SOLID;
				}if (map.getTileId(x, y, solids) == 78){
					solid[x][y].id = Tile.house2TR;
					tile[x][y].type = TileEnum.SOLID;
				}if (map.getTileId(x, y, solids) == 85){
					solid[x][y].id = Tile.house2BL;
					tile[x][y].type = TileEnum.SOLID;
				}if (map.getTileId(x, y, solids) == 86){
					solid[x][y].id = Tile.house2BR;
					tile[x][y].type = TileEnum.SOLID;
				}if (map.getTileId(x, y, solids) == 79){
					solid[x][y].id = Tile.house3TL;
					tile[x][y].type = TileEnum.SOLID;
				}if (map.getTileId(x, y, solids) == 80){
					solid[x][y].id = Tile.house3TR;
					tile[x][y].type = TileEnum.SOLID;
				}if (map.getTileId(x, y, solids) == 87){
					solid[x][y].id = Tile.house3BL;
					tile[x][y].type = TileEnum.SOLID;
				}if (map.getTileId(x, y, solids) == 88){
					solid[x][y].id = Tile.house3BR;
					tile[x][y].type = TileEnum.SOLID;
				}
				
				if (map.getTileId(x, y, solids) == 89){
					solid[x][y].id = Tile.fountainTL;
					tile[x][y].type = TileEnum.SOLID;
				}if (map.getTileId(x, y, solids) == 90){
					solid[x][y].id = Tile.fountainTR;
					tile[x][y].type = TileEnum.SOLID;
				}if (map.getTileId(x, y, solids) == 97){
					solid[x][y].id = Tile.fountainBL;
					tile[x][y].type = TileEnum.SOLID;
				}if (map.getTileId(x, y, solids) == 98){
					solid[x][y].id = Tile.fountainBR;
					tile[x][y].type = TileEnum.SOLID;
				}
				
				if (map.getTileId(x, y, solids) == 91){
					solid[x][y].id = Tile.fenceTL;
					tile[x][y].type = TileEnum.SOLID;
				}if (map.getTileId(x, y, solids) == 92){
					solid[x][y].id = Tile.fenceTR;
					tile[x][y].type = TileEnum.SOLID;
				}if (map.getTileId(x, y, solids) == 99){
					solid[x][y].id = Tile.fenceBL;
					tile[x][y].type = TileEnum.SOLID;
				}if (map.getTileId(x, y, solids) == 100){
					solid[x][y].id = Tile.fenceBR;
					tile[x][y].type = TileEnum.SOLID;
				}if (map.getTileId(x, y, solids) == 93){
					solid[x][y].id = Tile.fenceT;
					tile[x][y].type = TileEnum.SOLID;
				}if (map.getTileId(x, y, solids) == 101){
					solid[x][y].id = Tile.fenceB;
					tile[x][y].type = TileEnum.SOLID;
				}if (map.getTileId(x, y, solids) == 107){
					solid[x][y].id = Tile.fenceL;
					tile[x][y].type = TileEnum.SOLID;
				}if (map.getTileId(x, y, solids) == 108){
					solid[x][y].id = Tile.fenceR;
					tile[x][y].type = TileEnum.SOLID;
				}
				
				if (map.getTileId(x, y, solids) == 94){
					solid[x][y].id = Tile.bushH1;
					tile[x][y].type = TileEnum.SOLID;
				}if (map.getTileId(x, y, solids) == 95){
					solid[x][y].id = Tile.bushH2;
					tile[x][y].type = TileEnum.SOLID;
				}if (map.getTileId(x, y, solids) == 96){
					solid[x][y].id = Tile.bushH3;
					tile[x][y].type = TileEnum.SOLID;
				}if (map.getTileId(x, y, solids) == 104){
					solid[x][y].id = Tile.bushV1;
					tile[x][y].type = TileEnum.SOLID;
				}if (map.getTileId(x, y, solids) == 112){
					solid[x][y].id = Tile.bushV2;
					tile[x][y].type = TileEnum.SOLID;
				}if (map.getTileId(x, y, solids) == 120){
					solid[x][y].id = Tile.bushV3;
					tile[x][y].type = TileEnum.SOLID;
				}
			}
		}
		
		//creates NPC's at a predetermined location
		Entity npc = new NPC(main, 14 * Tile.size, 11 * Tile.size, Tile.size, Tile.size, 100);
		main.getEntities().add(npc);
		npc = new NPC(main, 15 * Tile.size, 50 * Tile.size, Tile.size, Tile.size, 100);
		main.getEntities().add(npc);
		npc = new NPC(main, 35 * Tile.size, 8 * Tile.size, Tile.size, Tile.size, 100);
		main.getEntities().add(npc);
		//Entity pet = new Pet(EntityEnum.PET, main, main.getPlayer().getX() + main.getCamOfSetX(), 
				//main.getPlayer().getY() + main.getCamOfSetY(), 32, 32, 100);
		//main.getEntities().add(pet);
	}
	
	/**
	 * Get sound corresponding to tile
	 * @return sound effect for the tile
	 */
	public SoundFX terrainSound(){
		TileEnum type = null;
		for(Tile[] x : tile){
			for(Tile y : x){
				if(y.colidesWith(main.getPlayer(), true))
					type = y.type;
					
			}
		}
		for(SoundFX s : main.getSounds()){
			if(type == TileEnum.GRASS && s.getClipName().equals("grass")){
				stopOtherClip(s);
				return s;
			}
			if(type == TileEnum.SAND && s.getClipName().equals("gravel")){
				stopOtherClip(s);
				return s;
			}
		}
		return null;
	}
	
/**
 * stops background tile noises 	
 * @param clip is the only sound effect that gets played
 */
	public void stopOtherClip(SoundFX clip){
		for(SoundFX s : main.getSounds())
			if(!clip.getClipName().equals(s.getClipName()) && s.isBackground())
				s.stopClip();
	}
	
	
	/**
	 * Gets a sound effect based on name
	 * @param name is the sound effect name
	 * @return clip name
	 */
	public SoundFX getClip(String name){
		for(SoundFX s : main.getSounds()){
			if(s.getClipName().equalsIgnoreCase(name))
				return s;
		}
		return null;
	}
	
	/**
	 * Checks to see if tile is solid and if it is not solid enemy can spawn
	 * @param x cord of tile
	 * @param y cord of tile
	 * @return true if tile is solid, false if tile is not solid
	 */
	public boolean tileIsSolid(int x, int y){
		if(tile[x][y].type == TileEnum.SOLID)
			return true;
		return false;
	}
	
	/**
	 * Check to see is entities collide with tile 
	 * @param e is the entity 
	 * @param isPlayer if the entity we are checking is the player
	 * @return true if there is a collision, false is there is not a collision 
	 */
	public boolean tileIsSolid(Entity e, boolean isPlayer){
		for(Tile[] x : tile){
			for(Tile y : x){
				if(y.colidesWith(e, isPlayer) && (y.type == TileEnum.SOLID)){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Removes an entity if the health is 0 or lower (entity is dead)
	 */
	public void removeEnemy(){
		for(int i = 0; i < main.getEntities().size(); i++){
			if(main.getEntities().get(i).getHealth() <= 0){
				if(main.getEntities().get(i).getType() == EntityEnum.BOSS){
					Boss b = (Boss) main.getEntities().get(i);
					b.bossKilled();
				}
				main.getEntities().remove(i);
			}
		}
		for(int i = 0; i < main.getEnemies().size(); i++){
			if(main.getEnemies().get(i).isDead())
				main.getEnemies().remove(i);
		}
	}


	public void render (Graphics g, int camX, int camY, int renX, int renY){
		for (int x = (camX/ Tile.size); x <(camX/Tile.size) + renX; x++){
			for (int y = (camY/ Tile.size); y <(camY/Tile.size) + renY; y++){
				if (x >= 0 && y >= 0 && x <width && y < height){
					bg[x][y].render(g);
					solid[x][y].render(g);
				}
			}
		}
	}

}
