/**
 * To initialize and render
 * Main.java
 * @author Mohammed Siddiqui 
 */
package infinity.main;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import infinity.drops.Armour;
import infinity.drops.Inventory;
import infinity.drops.ItemDrop;
import infinity.drops.Shop;
import infinity.entites.*;
import infinity.handlers.*;
import infinity.level.EnemySpawner;
import infinity.level.Level;
import infinity.level.Tile;
import infinity.weapons.Magic;
import infinity.weapons.Weapon;


public class Main extends Applet implements Runnable {

	//Variables need to create FPS counter (Set game at 60 FPS)
	private final int TARGET_FPS = 100; 
	private final long optimalTime = 1000000000/  TARGET_FPS; 
	private long lastFPS = 0;
	private int FPS = 0;
	private int renderFPS = 0;
	
	//Array to store sounds
	private ArrayList<SoundFX> sounds;
	private SoundFX bgMusic;
	//Array to store Weapons
	private ArrayList<Weapon> weapons = new ArrayList<Weapon>();
	//Array to store magic attacks 
	private ArrayList<Magic> spells = new ArrayList<Magic>();

	//Initializing Level
	private Level level; 
	private EnemySpawner spawner;
	private Menu menu;

	//Initializing Shop
	private Shop shop = new Shop(this);	
	//Initializing main
	private static Main main; 
	//Initializing entities
	private Player player;

	//Array Lists to keep track of entities added and removed
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	private ArrayList<NPC> npcList = new ArrayList<NPC>();
	private ArrayList<Enemy> enemies = new ArrayList<Enemy>();

	//Initializing JFrame
	private static JFrame display; 

	//Initializing Image that will be displayed on sceen
	private Image displayImg; 

	//Player movement and Interaction
	private boolean wK,sK,aK,dK,space,fK,eK,gK,shift = false, spell1, respawn;
	protected boolean  startGame;
	private boolean instructions;
	protected boolean quit;
	private boolean openShop;
	private final Rectangle closeShop = new Rectangle (740, 50, 60, 57);
	private boolean mainMenu = true;
	protected boolean playerDead;

	public void setMainMenu(boolean mainMenu) {
		this.mainMenu = mainMenu;
	}

	// Inventory 
	private boolean iK = false;

	//Player is centered in the middle of the screen
	private double camOfSetX = 0, camOfSetY = 0;

	//If game is running
	private boolean runGame; 

	//Menu Booleans 
	private boolean paused = false; 

	//Storing width and height as one object
	private Dimension size = new Dimension (1200, 650);

	public Main(){
		//Set Applet size 
		setPreferredSize(size); 

		//Add keyboard input
		addKeyListener (new KeyHandler(this)); 
		//Add mouse input
		addMouseListener (new MouseHandler(this)); 
		addMouseMotionListener (new MouseHandler(this)); 
		
		setMenu(new Menu(this));

		//Initializing entities
		initPlayer(); 
		
		//Initializing sound Array
		sounds = new ArrayList<SoundFX>();

	}

	public static void main(String[] args) {

		//Allowing game to start once JFrame is created
		main = new Main(); 

		//Creating JFrame
		display = new JFrame();
		display.add(main);
		display.pack();
		display.setTitle("Infinity");
		display.setResizable(false);
		display.setLocationRelativeTo(null);
		ImageIcon img = new ImageIcon("res/world/icon.png");
		display.setIconImage(img.getImage());
		display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		display.setVisible(true);
		main.start();

	}

	public void start(){
		//Creating Level
		setLevel(new Level (this, 1));
		Tile.loadImages();
		
		//Initializing enemy spawner 
		spawner = new EnemySpawner(this);
		
		//Adding sounds 
		sounds.add(new SoundFX("grass",true));
		sounds.add(new SoundFX("gravel",true));
		String [] sfx = {"fire", "lightning", "water", "wind", "earth", "hit", "bow","new_wave","sword","click","wavebg"
				,"bossbg", "bgmusic"};
		for(String s: sfx)
			sounds.add(new SoundFX(s, false));
		
		//Allowing game to run
		runGame = true;

		//Creating a thread
		new Thread (this).start();
		requestFocus();
		bgMusic = main.getLevel().getClip("wavebg");
		bgMusic.loop();
	}
	
	/**
	 * Draws the main menu
	 * @param g is the graphics components 
	 */
	public void mainMenu(Graphics g){
		menu.mainMenu(g);
	}

	/**
	 * Stops the game 
	 */
	public void stop(){
		runGame = false; 
	}

	/**
	 * updates the game 
	 * @param delta used to compensate for fps drops 
	 */
	public void update(double delta){
		if (shop.inventory.isChanged()){
			Tile.loadImages();
			shop.inventory.setChanged(false);
			}
		display.pack(); 
		for(int i = 0; i < getEntities().size(); i++){
			getEntities().get(i).move(delta);
		}
	}

	/**
	 * Creates the player
	 */
	public void initPlayer(){
		setPlayer(new Player(this,(size.width / 2) - (Tile.size / 2) + getCamOfSetX(),
				(size.height / 2) - (Tile.size / 2) + getCamOfSetY(), 30, 48, 100));
		getEntities().add(getPlayer());
	}
	
	/**
	 * Checks to see if the player is dead
	 * @return true if player's health is below 0, false if it is not
	 */
	public boolean playerDead(){
		if(player.getHealth() <= 0){
			playerDead = true;
			return true;
		}
		return false;
	}
	
	/**
	 * resets all the arrays on player death 
	 */
	public void resetArrays(){
		weapons.clear();
		spells.clear();
		entities.clear();
		enemies.clear();
		npcList.clear();
	}
	
	/**
	 * resets the game once player has died 
	 * @param g is the graphics components 
	 */
	public void reset(Graphics g){
		getMenu().respawn(g);
		if(playerDead() && respawn){
			camOfSetX = 0;
			camOfSetY = 0;
			playerDead = false;
			resetArrays();
			initPlayer();
			setLevel(new Level(this, 1));
			spawner = new EnemySpawner(this);
			shop = new Shop(this);
			respawn = false;
		}
	}
	
	/**
	 * rendering images 
	 */
	public void render(){
		Graphics g;
		g = displayImg.getGraphics();
		if(!isMainMenu() && !isInstructions() && startGame){
			playerDead();
			getLevel().render(g, (int) getCamOfSetX(), (int) getCamOfSetY(), (size.width / Tile.size) + 2, (size.height / Tile.size) + 2);
			if(!paused){
				//Render enemy drops
				for(ItemDrop i: getPlayer().getDrops())
					i.render(g);

				//Render weapons 
				for(Weapon i : getWeapons())
					i.attack(g);

				//Render entities 
				for(int i = 0 ; i < getEntities().size(); i++){
					if(entities.get(i).getType() == EntityEnum.BOSS){
						Boss b = (Boss) entities.get(i);
						b.specialAttack();
					}
					getEntities().get(i).render(g);
				}

				//Removes weapons 
				for(int i = 0; i < getWeapons().size(); i++){
					if(getWeapons().get(i).removeWeapon()){
						getWeapons().remove(i);
					}
				}
				//FPS counter 
				g.setColor(Color.BLACK);
				g.setFont(new Font("Garamond", Font.BOLD, 18));
				g.drawString("FPS: " + renderFPS, 1100,25);

			}else{
				if (iK)
					getInventory().render(g);
				if (openShop)
					getShop().render(g);
			}
			reset(g);
		}else
			mainMenu(g);
		g = this.getGraphics();

		g.drawImage(displayImg, 0, 0, size.width, size.height, 0, 0, size.width, size.height, null);

		g.dispose();
	}

	/**
	 * Runs constantly to update game 
	 */
	public void run() {

		displayImg = createVolatileImage(size.width, size.height);      

		long lastLoopTime = System.nanoTime(); 

		while (runGame){
			//FPS was inspired by http://stackoverflow.com/questions/34010255/fps-counter-in-java
			long now = System.nanoTime();
			long updateLength = now - lastLoopTime;
			lastLoopTime = now;
			
			double delta = updateLength / (double) optimalTime;
			lastFPS += updateLength;
			FPS++;
			
			if(lastFPS >= 1000000000){
				renderFPS = FPS;
				FPS = 0;
				lastFPS = 0;
			}
			update(delta);
			render();
			
			try{
				Thread.sleep((lastLoopTime - System.nanoTime() + optimalTime) / 1000000);
			}catch(Exception e){
				
			}

		}
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public ArrayList<Entity> getEntities() {
		return entities;
	}

	public void setEntities(ArrayList<Entity> entities) {
		this.entities = entities;
	}

	public ArrayList<Enemy> getEnemies() {
		return enemies;
	}

	public void setEnemies(ArrayList<Enemy> enemies) {
		this.enemies = enemies;
	}

	public ArrayList<NPC> getNpcList() {
		return npcList;
	}

	public void setNpcList(ArrayList<NPC> npcList) {
		this.npcList = npcList;
	}

	public boolean iswK() {
		return wK;
	}

	public void setwK(boolean wK) {
		this.wK = wK;
	}

	public boolean isaK() {
		return aK;
	}

	public void setaK(boolean aK) {
		this.aK = aK;
	}

	public boolean issK() {
		return sK;
	}

	public void setsK(boolean sK) {
		this.sK = sK;
	}

	public boolean isdK() {
		return dK;
	}

	public void setdK(boolean dK) {
		this.dK = dK;
	}

	public boolean iseK() {
		return eK;
	}

	public void seteK(boolean eK) {
		this.eK = eK;
	}

	public boolean isSpace() {
		return space;
	}

	public void setSpace(boolean space) {
		this.space = space;
	}

	public boolean isfK() {
		return fK;
	}

	public void setfK(boolean fK) {
		this.fK = fK;
	}

	public double getCamOfSetX() {
		return camOfSetX;
	}

	public void setCamOfSetX(double camOfSetX) {
		this.camOfSetX = camOfSetX;
	}

	public double getCamOfSetY() {
		return camOfSetY;
	}

	public void setCamOfSetY(double camOfSetY) {
		this.camOfSetY = camOfSetY;
	}

	public boolean isiK() {
		return iK;
	}

	public void setiK(boolean iK) {
		this.iK = iK;
	}

	public Inventory getInventory() {
		return shop.inventory;
	}

	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}
	
	public boolean isgK() {
		return gK;
	}

	public void setgK(boolean gK) {
		this.gK = gK;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public boolean getShift() {
		return shift;
	}

	public void setShift(boolean shift) {
		this.shift = shift;
	}

	public ArrayList<Weapon> getWeapons() {
		return weapons;
	}

	public void setWeapons(ArrayList<Weapon> weapons) {
		this.weapons = weapons;
	}
	
	public EnemySpawner getSpawner() {
		return spawner;
	}

	public void setSpawner(EnemySpawner spawner) {
		this.spawner = spawner;
	}
	
	public Dimension getSize() {
		return size;
	}

	public void setSize(Dimension size) {
		this.size = size;
	}

	public ArrayList<Magic> getSpells() {
		return spells;
	}

	public void setSpells(ArrayList<Magic> spells) {
		this.spells = spells;
	}
	public ArrayList<SoundFX> getSounds() {
		return sounds;
	}

	public void setSounds(ArrayList<SoundFX> sounds) {
		this.sounds = sounds;
	}
	
	public boolean isPlayerDead() {
		return playerDead;
	}

	public void setPlayerDead(boolean playerDead) {
		this.playerDead = playerDead;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}
	
	public boolean isRespawn() {
		return respawn;
	}

	public void setRespawn(boolean respawn) {
		this.respawn = respawn;
	}

	public boolean isSpell1() {
		return spell1;
	}

	public void setSpell1(boolean spell1) {
		this.spell1 = spell1;
	}
	
	public void setStartGame(boolean startGame) {
		this.startGame = startGame;
	}

	public void setInstructions(boolean instructions) {
		this.instructions = instructions;
	}

	public void setQuit(boolean quit) {
		this.quit = quit;
	}

	public boolean isMainMenu() {
		return mainMenu;
	}

	public boolean isInstructions() {
		return instructions;
	}

	public SoundFX getBgMusic() {
		return bgMusic;
	}

	public void setBgMusic(SoundFX bgMusic) {
		this.bgMusic = bgMusic;
	}
	public Shop getShop (){
		return shop;
	}
	public boolean isOpenShop() {
		return openShop;
	}

	public void setOpenShop(boolean openShop) {
		this.openShop = openShop;
	}
	public Rectangle getCloseShop() {
		return closeShop;
	}
}
