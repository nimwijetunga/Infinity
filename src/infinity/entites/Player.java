/**
 * Player class to control all user movement and interactions
 * Player.java
 * @author Nim Wijetunga
 */

package infinity.entites;

import java.awt.Color;

import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import infinity.drops.Armour;
import infinity.drops.DropEnum;
import infinity.drops.Inventory;
import infinity.drops.ItemDrop;
import infinity.handlers.SoundFX;
import infinity.level.Tile;
import infinity.main.Main;
import infinity.weapons.Arrow;
import infinity.weapons.Magic;
import infinity.weapons.Sword;
import infinity.weapons.WeaponEnum;


public class Player extends Entity{
	//Players movement and damage variables
	private double moveSpeed, baseDamage, regen;
	//Player state and magic variables
	private boolean moving, flinching , fired , slashed , killedBoss , spawned , fireSpell , castSpell , talkNPC 
	,earthSpell,lightingSpell,windSpell,waterSpell;

	//Player direction variables
	private boolean facingUp, facingDown,facingRight, facingLeft;
	//Player attack state variables
	private boolean attacking, rangeWeapon;
	//Player key pressed state variables
	private boolean up , down , left , right;
	//Player countable values
	private int arrowCount, manaCount;
	private ArrayList<ItemDrop> drops = new ArrayList<ItemDrop>();

	//Movement images
	private  int[][] pUp = {{0,0}, {1,0}, {2,0}, {3,0}, {4,0}, {5,0}, {6,0}, {7,0}, {8,0}};
	private  int[][] pLeft = {{0,1}, {1,1}, {2,1}, {3,1}, {4,1}, {5,1}, {6,1}, {7,1}, {8,1}};
	private  int[][] pDown = {{0,2}, {1,2}, {2,2}, {3,2}, {4,2}, {5,2}, {6,2}, {7,2}, {8,2}};
	private  int[][] pRight = {{0,3}, {1,3}, {2,3}, {3,3}, {4,3}, {5,3}, {6,3}, {7,3}, {8,3}};
	private int [][]lastImg = pDown;


	//Sword images
	private int[][] sUp = {{0,4}, {1,4}, {2,4}, {3,4},{4,4},{5,4}};
	private int[][] sLeft = {{0,5}, {1,5}, {2,5},{3,5},{4,5},{5,5}};
	private int[][] sDown = {{0,6}, {1,6}, {2,6},{3,6},{4,6},{5,6}};
	private int[][] sRight = {{0,7}, {1,7}, {2,7},{3,7},{4,7},{5,7}};

	//Bow and Arrow Images
	private int[][] rUp = {{10,0}, {11,0}, {12,0}, {13,0}, {14,0}, {15,0}, {16,0}, {17,0}, {18,0}, {19,0}, {20,0}, {21,0}, {22,0}};
	private int[][] rLeft = {{10,1}, {11,1}, {12,1}, {13,1}, {14,1}, {15,1}, {16,1}, {17,1}, {18,1}, {19,1}, {20,1}, {21,1}, {22,1}};
	private int[][] rDown = {{10,2}, {11,2}, {12,2}, {13,2}, {14,2}, {15,2}, {16,2}, {17,2}, {18,2}, {19,2}, {20,2}, {21,2}, {22,2}};
	private int[][] rRight = {{10,3}, {11,3}, {12,3}, {13,3}, {14,3}, {15,3}, {16,3}, {17,3}, {18,3}, {19,3}, {20,3}, {21,3}, {22,3}};

	//magic images
	private int[][] mUp = {{7,4}, {8,4}, {9,4}, {10,4}, {11,4}, {12,4}, {13,4}};
	private int[][] mLeft = {{7,5}, {8,5}, {9,5}, {10,5}, {11,5}, {12,5}, {13,5}};
	private int[][] mDown = {{7,6}, {8,6}, {9,6}, {10,6}, {11,6}, {12,6}, {13,6}};
	private int[][] mRight = {{7,7}, {8,7}, {9,7}, {10,7}, {11,7}, {12,7}, {13,7}};

	private int Rx, Ry;

	//Animation variables
	private int aniFrame = 0;
	private int aniTime =  5;
	private int aniDelta = 0;
	private int sFrame = 0, sTime = 7, sDelta = 0;
	private int aFrame = 0, aTime = 18, aDelta = 0;
	private int mFrame = 0, mTime = 7, mDelta = 0;

	//Timers
	private long flinchTimer, arrowTimer, slashTimer, spellDelay, regenTimer;

	/**
	 * @param main
	 * @param x position of entity
	 * @param y position of entity
	 * @param width of entity
	 * @param height of entity
	 * @param health of entity
	 */
	public Player(Main main, double x, double y, int width, int height, double health) {
		super(EntityEnum.PLAYER, main, x, y, width, height, health);
		spawned = true;
		baseDamage = 10;
		Rx = (int)( x - main.getCamOfSetX());
		Ry = (int)( y - main.getCamOfSetY());
		main.setCamOfSetY(main.getCamOfSetY() + (10 * Tile.size));
		arrowCount = 30;
		manaCount = 5;
		regen = 5;
		moveSpeed = 4.9;
	}

	/**update player movement and interactions by seeing which keys were pressed (in key handler)
	 * 
	 */
	public void updateAnimations(){
		//update player booleans
		up = main.iswK();
		down = main.issK();
		left = main.isaK();
		right = main.isdK();
		//talkNPC = main.iseK();
		if(up || down || left || right)
			moving = true;
		else
			moving = false;
		if (main.getShift()){
			moveSpeed = 6.9;
			aniTime = 1;
		}
		else {
			moveSpeed = 4.9;
			aniTime = 5;
		}

		if(main.isfK())
			rangeWeapon = true;
		if(main.isSpace())
			attacking = true;
	}

	/**move to allow for the movement of the player around the map
	 * @param delta is movement speed modifier
	 */
	public void move(double delta){
		//Update movement animations
		if(spawned){
			main.setCamOfSetX(main.getCamOfSetX() + 64);
			main.setCamOfSetY(main.getCamOfSetY() + 64);
			spawned = false;
		}
		aniDelta++; 
		if(flinchTimer == 0)
			flinchTimer++;

		if(aniDelta >= aniTime){
			aniFrame++;
			aniDelta = 0;
			if(aniFrame > 8)
				aniFrame = 0;
		}

		//Update sword and arrow animations
		sDelta++;
		if(sDelta >= sTime){
			sFrame++;
			sDelta = 0;
			if(sFrame > 5){
				sFrame = 0;
				attacking = false;
			}
		}
		//Update sword and arrow animations
		aDelta++;
		if(aDelta >= aTime){
			aFrame++;
			aDelta = 0;
			if(aFrame > 12){
				aFrame = 0;
				rangeWeapon = false;
			}
		}
		//Magic cast update
		mDelta++;
		if(mDelta >= mTime){
			mFrame++;
			mDelta = 0;
			if(mFrame > 6){
				mFrame = 0;
				castSpell = false;
			}
		}
		//call interaction methods
		if(!dizzy)
			moveXY(delta, moveSpeed);
		else
			moveXY(delta, -moveSpeed);
		interactsWithEnemy();
		swordAttack();
		arrowAttack();
		magicAttack();
	}

	/**check for collisions with enemy
	 * 
	 */
	public void interactsWithEnemy(){
		for(Entity i : main.getEntities()){
			if(this.collidesWith(i, true)){
				if(i.getType() == EntityEnum.ENEMY || i.getType() == EntityEnum.BOSS){//if enemy or boss take damqge
					Enemy e = (Enemy) i;
					takeDamage(e.getDamage());
				}
			}
		}
	}

	/**drop item if enemy has died
	 * 
	 */
	public void dropItem(){
		for(Enemy i : main.getEnemies()){
			if(i.health <= 0){
				DropEnum type = null;
				int id = ThreadLocalRandom.current().nextInt(1,5);
				//Switch the random id to a drop
				switch(id){
				case 1:
					type = DropEnum.COIN;
					break;
				case 2:
					type = DropEnum.HEALTH;
					break;
				case 3:
					type = DropEnum.AMMO;
					break;
				case 4:
					type = DropEnum.MANA;
					break;
				}
				getDrops().add(new ItemDrop(type,(int)i.getX(), (int)i.getY(), Tile.size / 2, Tile.size / 2, main, true));
			}
		}
		pickUpItem();
	}

	/**regenHealth if not being attacked by another entity
	 *
	 */
	public void regenHealth (){
		long elapsed = (System.nanoTime() - regenTimer) / 1000000;//get milliseconds
		//Add 5 to health given that the entity is not being atacked
		if(elapsed > 3000 && health < maxHealth){
			health += regen;
			regenTimer = System.nanoTime();
		}
		//cannot regen health if the player has full health 
		if (health >= maxHealth)
			health = maxHealth;
	}

	/**check to see if the player walks over a drop
	 * 
	 */
	public void pickUpItem(){
		//go through the drops array 
		for(int i = 0; i < getDrops().size(); i++){
			//if player collides with the enemy drop then pick it up and add the value to player
			if(getDrops().get(i).colidesWith()){
				if(getDrops().get(i).getType() == DropEnum.COIN)//coin
					main.getShop().setCoinCount((main.getShop().getCoinCount()+getDrops().get(i).value())) ;
				else if(getDrops().get(i).getType() == DropEnum.AMMO)//arrow
					addArrows(getDrops().get(i).value());
				else if(getDrops().get(i).getType() == DropEnum.HEALTH)//health
					addHealth(getDrops().get(i).value());
				else if(getDrops().get(i).getType() == DropEnum.MANA)//mana(spells)
					addMana(getDrops().get(i).value());
				getDrops().remove(i);
			}
		}
	}

	/**add Mana to player's mana count
	 * @param manaAmount is the amount of mana to be added
	 */
	public void addMana(int manaAmount){
		int maxMana = 5;
		if(manaCount  == maxMana)
			return;
		else if(manaAmount + manaCount > maxMana)
			manaCount = maxMana;
		else
			manaCount += manaAmount;
	}

	/**add health to the player
	 * @param healthAmount is the amount of health to be added
	 */
	public void addHealth(int healthAmount){
		if(health == maxHealth)
			return;
		else if(health + healthAmount > maxHealth)
			health = maxHealth;
		else
			health += healthAmount;
	}

	/**add arrows to players arsenal
	 * @param numArrow is the number of arrows picked up by the player
	 */
	public void addArrows(int numArrow){
		if(arrowCount == 30)
			return;
		else if(arrowCount + numArrow > 30)
			arrowCount = 30;
		else
			arrowCount += numArrow;
	}

	/**attack with the sword
	 * 
	 */
	public void swordAttack(){
		if(slashed)
			return;
		if(attacking && !rangeWeapon && sFrame == 5){
			//add the sword to the weapons array list
			main.getWeapons().add(new Sword(main.getCamOfSetX() + x, 
					main.getCamOfSetY() + y, baseDamage, 32, 64, 64, main, isFacingUp(), isFacingDown(),isFacingLeft(), isFacingRight(),this));
			slashed = true;
			slashTimer = System.nanoTime();
		}
		//check to see if enemy died
		dropItem();
		main.getLevel().removeEnemy();
	}

	/**increases player stats based on equipped armour
	 * 
	 */
	public void increaseStats(){
		for(Armour [] i: Inventory.invArm){
			for(Armour j : i){
				if(j != null){
					if(j.isEquipped() && !j.isIncrStat()){
						j.setIncrStat(true);
						maxHealth += j.getHealthIncr();
						baseDamage += j.getDamIncr();
						regen += j.getRegenFactor();
					}
				}
			}
		}
	}

	/**reduce player stats upon Armour removal
	 * @param health is the amount of health to deduct
	 * @param damage is the amount of base damage to deduct
	 */
	public void reduceStats(double health, double damage, double regen){
		maxHealth -= health;
		if(health > maxHealth)
			health = maxHealth;
		baseDamage -= damage;
		if(baseDamage < 5)
			baseDamage = 5;
		this.regen -= regen;
		if(this.regen < 1)
			this.regen = 1;
	}

	/**attack with bow and arrow
	 * 
	 */
	public void arrowAttack(){
		if(arrowCount < 0)
			arrowCount = 0;
		if(arrowCount == 0 || fired)
			return;
		if(rangeWeapon && !attacking && aFrame == 9){
			//add the arrow to the weapons array list
			main.getWeapons().add(new Arrow(WeaponEnum.ARROW, main.getCamOfSetX() + x, 
					main.getCamOfSetY() + y, baseDamage, 300, 24, 24, main, isFacingUp(), isFacingDown(),isFacingLeft(),isFacingRight(),this));
			fired = true;
			arrowCount--;
			arrowTimer = System.nanoTime();
		}
		//check to see if enemy died
		dropItem();
		main.getLevel().removeEnemy();
	}

	/**use magic attack
	 * 
	 */
	public void magicAttack(){
		if(castSpell)
			return;
		if(manaCount < 0)
			manaCount = 0;
		if(manaCount == 0)
			return;
		if(fireSpell || earthSpell||lightingSpell||windSpell||waterSpell){//casts the spell
			addSpell();
			manaCount--;
			castSpell = true;
			spellDelay = System.nanoTime();
		}
		dropItem();
	}

	/**determines which spell was used and add it to the magic arraylist
	 * 
	 */
	public void addSpell (){
		WeaponEnum type = null;
		if(fireSpell)//fire
			type = WeaponEnum.FIRE;
		else if(earthSpell)//earth
			type = WeaponEnum.EARTH;
		else if(lightingSpell)//lightning
			type = WeaponEnum.LIGHTNING;
		else if(windSpell)//wind
			type = WeaponEnum.WIND;
		else if(waterSpell)//water
			type = WeaponEnum.WATER;
		main.getWeapons().add(new Magic(type, main.getCamOfSetX() + x, 
				main.getCamOfSetY() + y, baseDamage, Tile.size * 5, 24, 20, main, isFacingUp(), isFacingDown(),isFacingLeft(),isFacingRight(),this));
	}

	/**takeDamge to inflict damage upon player 
	 * @param damage is the amount of damage the player receives
	 */
	public void takeDamage(double damage){
		//Return if player is dead or invulnerable to attack
		if(main.isPlayerDead() || flinching)
			return;
		if(health < 0)//health cannot go below 0
			health = 0;
		if(health <= 0){//Player has died
			main.setPlayerDead(true);
		}
		//reduce health
		health -= damage;
		//player is now flinching
		flinching = true;
		//get the current time which the player started flinching
		flinchTimer = System.nanoTime();
	}

	/**check for NPC interaction with player
	 * 
	 */
	public void interactsWithNPC(){
		for(Entity i : main.getEntities()){
			if(i.getType() == EntityEnum.NPC && talkNPC){//if the player presses "E" next to NPC
				NPC n = (NPC) i;
				n.openShop();//Opens shop if there is an interaction between NPC and Player
			}
		}
	}

	/**move in the x and y direction
	 * @param delta is a movement speed modifier
	 */
	public void moveXY(double delta, double moveSpeed){
		if(right && !stunned)
			main.setCamOfSetX(main.getCamOfSetX() + moveSpeed * delta);
		if(left && !stunned)
			main.setCamOfSetX(main.getCamOfSetX() - moveSpeed * delta);
		if(up && !stunned)
			main.setCamOfSetY(main.getCamOfSetY() - moveSpeed * delta);
		if(down && !stunned)
			main.setCamOfSetY(main.getCamOfSetY() + moveSpeed * delta);
		//check for collision
		collisionXY(delta, moveSpeed);
	}

	/**check for collision in the x and y direction
	 * @param delta is a movement speed modifier
	 */
	public void collisionXY(double delta, double moveSpeed){
		//check for collisions with solid tiles or entities (specifically NPC's)
		if(right){
			if(main.getLevel().tileIsSolid(this,true) || collidesWithEntity(true))
				main.setCamOfSetX(main.getCamOfSetX() - moveSpeed * delta);
		}
		if(left){
			if(main.getLevel().tileIsSolid(this,true) || collidesWithEntity(true))
				main.setCamOfSetX(main.getCamOfSetX() + moveSpeed * delta);
		}
		if(up){
			if(main.getLevel().tileIsSolid(this,true) || collidesWithEntity(true)){
				main.setCamOfSetY(main.getCamOfSetY() + moveSpeed * delta);
			}
		}
		if(down){
			if(main.getLevel().tileIsSolid(this,true) || collidesWithEntity(true))
				main.setCamOfSetY(main.getCamOfSetY() - moveSpeed * delta);
		}
	}

	/**play terrain and weapon sounds
	 * 
	 */
	public void playSounds(){
		//sounds
		SoundFX terrain = main.getLevel().terrainSound();
		SoundFX sword = main.getLevel().getClip("sword");
		SoundFX bow = main.getLevel().getClip("bow");
		if(rangeWeapon){//bow and arrow sound effect
			bow.loop(1);
			if(aFrame == 12)
				bow.stopClip();
		}
		if(attacking) // sword sound effect
			sword.loop(1);
		if (terrain != null){//walking sound effect (depends on terrain)
			if((attacking || rangeWeapon) && !moving)
				terrain.stopClip();
			if(moving)
				terrain.loop(1);
			else
				terrain.stopClip();
		}
	}

	/****************************************RENDERING*******************************************************************/

	/**draw player
	 * @param g is the graphics component
	 */
	public void paintReg(Graphics g){
		//regular player images
		if(!attacking && !rangeWeapon && !castSpell){
			if(down){
				g.drawImage(Tile.characters, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), pDown[aniFrame][0]*Tile.size, pDown[aniFrame][1]*Tile.size, pDown[aniFrame][0]*Tile.size + Tile.size,pDown[aniFrame][1]*Tile.size + Tile.size, null);				
				g.drawImage(Tile.charactersHead, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), pDown[aniFrame][0]*Tile.size, pDown[aniFrame][1]*Tile.size, pDown[aniFrame][0]*Tile.size + Tile.size,pDown[aniFrame][1]*Tile.size + Tile.size, null);				
				g.drawImage(Tile.charactersTop, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), pDown[aniFrame][0]*Tile.size, pDown[aniFrame][1]*Tile.size, pDown[aniFrame][0]*Tile.size + Tile.size,pDown[aniFrame][1]*Tile.size + Tile.size, null);				
				g.drawImage(Tile.charactersBottom, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), pDown[aniFrame][0]*Tile.size, pDown[aniFrame][1]*Tile.size, pDown[aniFrame][0]*Tile.size + Tile.size,pDown[aniFrame][1]*Tile.size + Tile.size, null);				
				g.drawImage(Tile.charactersShoulder, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), pDown[aniFrame][0]*Tile.size, pDown[aniFrame][1]*Tile.size, pDown[aniFrame][0]*Tile.size + Tile.size,pDown[aniFrame][1]*Tile.size + Tile.size, null);				
				g.drawImage(Tile.charactersHand, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), pDown[aniFrame][0]*Tile.size, pDown[aniFrame][1]*Tile.size, pDown[aniFrame][0]*Tile.size + Tile.size,pDown[aniFrame][1]*Tile.size + Tile.size, null);				
				g.drawImage(Tile.charactersShoes, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), pDown[aniFrame][0]*Tile.size, pDown[aniFrame][1]*Tile.size, pDown[aniFrame][0]*Tile.size + Tile.size,pDown[aniFrame][1]*Tile.size + Tile.size, null);				
				g.drawImage(Tile.charactersBelt, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), pDown[aniFrame][0]*Tile.size, pDown[aniFrame][1]*Tile.size, pDown[aniFrame][0]*Tile.size + Tile.size,pDown[aniFrame][1]*Tile.size + Tile.size, null);				

				lastImg = pDown; 
			}
			else if(up){
				g.drawImage(Tile.characters, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), pUp[aniFrame][0]*Tile.size, pUp[aniFrame][1]*Tile.size, pUp[aniFrame][0]*Tile.size + Tile.size,pUp[aniFrame][1]*Tile.size + Tile.size, null);				
				g.drawImage(Tile.charactersHead, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), pUp[aniFrame][0]*Tile.size, pUp[aniFrame][1]*Tile.size, pUp[aniFrame][0]*Tile.size + Tile.size,pUp[aniFrame][1]*Tile.size + Tile.size, null);				
				g.drawImage(Tile.charactersTop, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), pUp[aniFrame][0]*Tile.size, pUp[aniFrame][1]*Tile.size, pUp[aniFrame][0]*Tile.size + Tile.size,pUp[aniFrame][1]*Tile.size + Tile.size, null);				
				g.drawImage(Tile.charactersBottom, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), pUp[aniFrame][0]*Tile.size, pUp[aniFrame][1]*Tile.size, pUp[aniFrame][0]*Tile.size + Tile.size,pUp[aniFrame][1]*Tile.size + Tile.size, null);				
				g.drawImage(Tile.charactersShoulder, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), pUp[aniFrame][0]*Tile.size, pUp[aniFrame][1]*Tile.size, pUp[aniFrame][0]*Tile.size + Tile.size,pUp[aniFrame][1]*Tile.size + Tile.size, null);				
				g.drawImage(Tile.charactersHand, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), pUp[aniFrame][0]*Tile.size, pUp[aniFrame][1]*Tile.size, pUp[aniFrame][0]*Tile.size + Tile.size,pUp[aniFrame][1]*Tile.size + Tile.size, null);				
				g.drawImage(Tile.charactersShoes, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), pUp[aniFrame][0]*Tile.size, pUp[aniFrame][1]*Tile.size, pUp[aniFrame][0]*Tile.size + Tile.size,pUp[aniFrame][1]*Tile.size + Tile.size, null);				
				g.drawImage(Tile.charactersBelt, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), pUp[aniFrame][0]*Tile.size, pUp[aniFrame][1]*Tile.size, pUp[aniFrame][0]*Tile.size + Tile.size,pUp[aniFrame][1]*Tile.size + Tile.size, null);				

				lastImg = pUp;

			}
			else if(left){
				g.drawImage(Tile.characters, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), pLeft[aniFrame][0]*Tile.size, pLeft[aniFrame][1]*Tile.size, pLeft[aniFrame][0]*Tile.size + Tile.size,pLeft[aniFrame][1]*Tile.size + Tile.size, null);				
				g.drawImage(Tile.charactersHead, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), pLeft[aniFrame][0]*Tile.size, pLeft[aniFrame][1]*Tile.size, pLeft[aniFrame][0]*Tile.size + Tile.size,pLeft[aniFrame][1]*Tile.size + Tile.size, null);				
				g.drawImage(Tile.charactersTop, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), pLeft[aniFrame][0]*Tile.size, pLeft[aniFrame][1]*Tile.size, pLeft[aniFrame][0]*Tile.size + Tile.size,pLeft[aniFrame][1]*Tile.size + Tile.size, null);				
				g.drawImage(Tile.charactersBottom, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), pLeft[aniFrame][0]*Tile.size, pLeft[aniFrame][1]*Tile.size, pLeft[aniFrame][0]*Tile.size + Tile.size,pLeft[aniFrame][1]*Tile.size + Tile.size, null);				
				g.drawImage(Tile.charactersShoulder, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), pLeft[aniFrame][0]*Tile.size, pLeft[aniFrame][1]*Tile.size, pLeft[aniFrame][0]*Tile.size + Tile.size,pLeft[aniFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersHand, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), pLeft[aniFrame][0]*Tile.size, pLeft[aniFrame][1]*Tile.size, pLeft[aniFrame][0]*Tile.size + Tile.size,pLeft[aniFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersShoes, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), pLeft[aniFrame][0]*Tile.size, pLeft[aniFrame][1]*Tile.size, pLeft[aniFrame][0]*Tile.size + Tile.size,pLeft[aniFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersBelt, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), pLeft[aniFrame][0]*Tile.size, pLeft[aniFrame][1]*Tile.size, pLeft[aniFrame][0]*Tile.size + Tile.size,pLeft[aniFrame][1]*Tile.size + Tile.size, null);

				lastImg = pLeft;

			}
			else if(right){
				g.drawImage(Tile.characters, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), pRight[aniFrame][0]*Tile.size, pRight[aniFrame][1]*Tile.size, pRight[aniFrame][0]*Tile.size + Tile.size,pRight[aniFrame][1]*Tile.size + Tile.size, null);				
				g.drawImage(Tile.charactersHead, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), pRight[aniFrame][0]*Tile.size, pRight[aniFrame][1]*Tile.size, pRight[aniFrame][0]*Tile.size + Tile.size,pRight[aniFrame][1]*Tile.size + Tile.size, null);				
				g.drawImage(Tile.charactersTop, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), pRight[aniFrame][0]*Tile.size, pRight[aniFrame][1]*Tile.size, pRight[aniFrame][0]*Tile.size + Tile.size,pRight[aniFrame][1]*Tile.size + Tile.size, null);				
				g.drawImage(Tile.charactersBottom, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), pRight[aniFrame][0]*Tile.size, pRight[aniFrame][1]*Tile.size, pRight[aniFrame][0]*Tile.size + Tile.size,pRight[aniFrame][1]*Tile.size + Tile.size, null);				
				g.drawImage(Tile.charactersShoulder, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), pRight[aniFrame][0]*Tile.size, pRight[aniFrame][1]*Tile.size, pRight[aniFrame][0]*Tile.size + Tile.size,pRight[aniFrame][1]*Tile.size + Tile.size, null);				
				g.drawImage(Tile.charactersHand, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), pRight[aniFrame][0]*Tile.size, pRight[aniFrame][1]*Tile.size, pRight[aniFrame][0]*Tile.size + Tile.size,pRight[aniFrame][1]*Tile.size + Tile.size, null);				
				g.drawImage(Tile.charactersShoes, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), pRight[aniFrame][0]*Tile.size, pRight[aniFrame][1]*Tile.size, pRight[aniFrame][0]*Tile.size + Tile.size,pRight[aniFrame][1]*Tile.size + Tile.size, null);				
				g.drawImage(Tile.charactersBelt, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), pRight[aniFrame][0]*Tile.size, pRight[aniFrame][1]*Tile.size, pRight[aniFrame][0]*Tile.size + Tile.size,pRight[aniFrame][1]*Tile.size + Tile.size, null);				

				lastImg = pRight;
			}else{
				g.drawImage(Tile.characters, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), sDown[0][0]*Tile.size, lastImg[0][1]*Tile.size, lastImg[0][0]*Tile.size + Tile.size,lastImg[0][1]*Tile.size + Tile.size, null);			
				g.drawImage(Tile.charactersHead, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), sDown[0][0]*Tile.size, lastImg[0][1]*Tile.size, lastImg[0][0]*Tile.size + Tile.size,lastImg[0][1]*Tile.size + Tile.size, null);			
				g.drawImage(Tile.charactersTop, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), sDown[0][0]*Tile.size, lastImg[0][1]*Tile.size, lastImg[0][0]*Tile.size + Tile.size,lastImg[0][1]*Tile.size + Tile.size, null);			
				g.drawImage(Tile.charactersBottom, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), sDown[0][0]*Tile.size, lastImg[0][1]*Tile.size, lastImg[0][0]*Tile.size + Tile.size,lastImg[0][1]*Tile.size + Tile.size, null);			
				g.drawImage(Tile.charactersShoulder, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), sDown[0][0]*Tile.size, lastImg[0][1]*Tile.size, lastImg[0][0]*Tile.size + Tile.size,lastImg[0][1]*Tile.size + Tile.size, null);			
				g.drawImage(Tile.charactersHand, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), sDown[0][0]*Tile.size, lastImg[0][1]*Tile.size, lastImg[0][0]*Tile.size + Tile.size,lastImg[0][1]*Tile.size + Tile.size, null);			
				g.drawImage(Tile.charactersShoes, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), sDown[0][0]*Tile.size, lastImg[0][1]*Tile.size, lastImg[0][0]*Tile.size + Tile.size,lastImg[0][1]*Tile.size + Tile.size, null);			
				g.drawImage(Tile.charactersBelt, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), sDown[0][0]*Tile.size, lastImg[0][1]*Tile.size, lastImg[0][0]*Tile.size + Tile.size,lastImg[0][1]*Tile.size + Tile.size, null);			

			}
		}
		//sword attacking player
		else if(attacking){
			if(isFacingDown()){
				g.drawImage(Tile.characters, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), sDown[sFrame][0]*Tile.size, sDown[sFrame][1]*Tile.size, sDown[sFrame][0]*Tile.size + Tile.size,sDown[sFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersHead, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), sDown[sFrame][0]*Tile.size, sDown[sFrame][1]*Tile.size, sDown[sFrame][0]*Tile.size + Tile.size,sDown[sFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersTop, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), sDown[sFrame][0]*Tile.size, sDown[sFrame][1]*Tile.size, sDown[sFrame][0]*Tile.size + Tile.size,sDown[sFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersBottom, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), sDown[sFrame][0]*Tile.size, sDown[sFrame][1]*Tile.size, sDown[sFrame][0]*Tile.size + Tile.size,sDown[sFrame][1]*Tile.size + Tile.size, null);			
				g.drawImage(Tile.weaponSword, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), sDown[sFrame][0]*Tile.size, sDown[sFrame][1]*Tile.size, sDown[sFrame][0]*Tile.size + Tile.size,sDown[sFrame][1]*Tile.size + Tile.size, null);			
				g.drawImage(Tile.charactersShoulder, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), sDown[sFrame][0]*Tile.size, sDown[sFrame][1]*Tile.size, sDown[sFrame][0]*Tile.size + Tile.size,sDown[sFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersHand, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), sDown[sFrame][0]*Tile.size, sDown[sFrame][1]*Tile.size, sDown[sFrame][0]*Tile.size + Tile.size,sDown[sFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersShoes, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), sDown[sFrame][0]*Tile.size, sDown[sFrame][1]*Tile.size, sDown[sFrame][0]*Tile.size + Tile.size,sDown[sFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersBelt, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), sDown[sFrame][0]*Tile.size, sDown[sFrame][1]*Tile.size, sDown[sFrame][0]*Tile.size + Tile.size,sDown[sFrame][1]*Tile.size + Tile.size, null);

			}else if(isFacingUp()){
				g.drawImage(Tile.characters, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), sUp[sFrame][0]*Tile.size, sUp[sFrame][1]*Tile.size, sUp[sFrame][0]*Tile.size + Tile.size,sUp[sFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersHead, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), sUp[sFrame][0]*Tile.size, sUp[sFrame][1]*Tile.size, sUp[sFrame][0]*Tile.size + Tile.size,sUp[sFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersTop, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), sUp[sFrame][0]*Tile.size, sUp[sFrame][1]*Tile.size, sUp[sFrame][0]*Tile.size + Tile.size,sUp[sFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersBottom, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), sUp[sFrame][0]*Tile.size, sUp[sFrame][1]*Tile.size, sUp[sFrame][0]*Tile.size + Tile.size,sUp[sFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.weaponSword, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), sUp[sFrame][0]*Tile.size, sUp[sFrame][1]*Tile.size, sUp[sFrame][0]*Tile.size + Tile.size,sUp[sFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersShoulder, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), sUp[sFrame][0]*Tile.size, sUp[sFrame][1]*Tile.size, sUp[sFrame][0]*Tile.size + Tile.size,sUp[sFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersHand, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), sUp[sFrame][0]*Tile.size, sUp[sFrame][1]*Tile.size, sUp[sFrame][0]*Tile.size + Tile.size,sUp[sFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersShoes, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), sUp[sFrame][0]*Tile.size, sUp[sFrame][1]*Tile.size, sUp[sFrame][0]*Tile.size + Tile.size,sUp[sFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersBelt, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), sUp[sFrame][0]*Tile.size, sUp[sFrame][1]*Tile.size, sUp[sFrame][0]*Tile.size + Tile.size,sUp[sFrame][1]*Tile.size + Tile.size, null);

			}else if(isFacingLeft()){
				g.drawImage(Tile.characters, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), sLeft[sFrame][0]*Tile.size, sLeft[sFrame][1]*Tile.size, sLeft[sFrame][0]*Tile.size + Tile.size,sLeft[sFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersHead, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), sLeft[sFrame][0]*Tile.size, sLeft[sFrame][1]*Tile.size, sLeft[sFrame][0]*Tile.size + Tile.size,sLeft[sFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersTop, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), sLeft[sFrame][0]*Tile.size, sLeft[sFrame][1]*Tile.size, sLeft[sFrame][0]*Tile.size + Tile.size,sLeft[sFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersBottom, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), sLeft[sFrame][0]*Tile.size, sLeft[sFrame][1]*Tile.size, sLeft[sFrame][0]*Tile.size + Tile.size,sLeft[sFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.weaponSword, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), sLeft[sFrame][0]*Tile.size, sLeft[sFrame][1]*Tile.size, sLeft[sFrame][0]*Tile.size + Tile.size,sLeft[sFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersShoulder, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), sLeft[sFrame][0]*Tile.size, sLeft[sFrame][1]*Tile.size, sLeft[sFrame][0]*Tile.size + Tile.size,sLeft[sFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersHand, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), sLeft[sFrame][0]*Tile.size, sLeft[sFrame][1]*Tile.size, sLeft[sFrame][0]*Tile.size + Tile.size,sLeft[sFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersShoes, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), sLeft[sFrame][0]*Tile.size, sLeft[sFrame][1]*Tile.size, sLeft[sFrame][0]*Tile.size + Tile.size,sLeft[sFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersBelt, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), sLeft[sFrame][0]*Tile.size, sLeft[sFrame][1]*Tile.size, sLeft[sFrame][0]*Tile.size + Tile.size,sLeft[sFrame][1]*Tile.size + Tile.size, null);

			}else if(isFacingRight()){
				g.drawImage(Tile.characters, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), sRight[sFrame][0]*Tile.size, sRight[sFrame][1]*Tile.size, sRight[sFrame][0]*Tile.size + Tile.size,sRight[sFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersHead, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), sRight[sFrame][0]*Tile.size, sRight[sFrame][1]*Tile.size, sRight[sFrame][0]*Tile.size + Tile.size,sRight[sFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersTop, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), sRight[sFrame][0]*Tile.size, sRight[sFrame][1]*Tile.size, sRight[sFrame][0]*Tile.size + Tile.size,sRight[sFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersBottom, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), sRight[sFrame][0]*Tile.size, sRight[sFrame][1]*Tile.size, sRight[sFrame][0]*Tile.size + Tile.size,sRight[sFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.weaponSword, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), sRight[sFrame][0]*Tile.size, sRight[sFrame][1]*Tile.size, sRight[sFrame][0]*Tile.size + Tile.size,sRight[sFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersShoulder, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), sRight[sFrame][0]*Tile.size, sRight[sFrame][1]*Tile.size, sRight[sFrame][0]*Tile.size + Tile.size,sRight[sFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersHand, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), sRight[sFrame][0]*Tile.size, sRight[sFrame][1]*Tile.size, sRight[sFrame][0]*Tile.size + Tile.size,sRight[sFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersShoes, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), sRight[sFrame][0]*Tile.size, sRight[sFrame][1]*Tile.size, sRight[sFrame][0]*Tile.size + Tile.size,sRight[sFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersBelt, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), sRight[sFrame][0]*Tile.size, sRight[sFrame][1]*Tile.size, sRight[sFrame][0]*Tile.size + Tile.size,sRight[sFrame][1]*Tile.size + Tile.size, null);

			}
		}
		//bow and arrow with player
		else if(rangeWeapon){
			if(isFacingDown()){
				g.drawImage(Tile.characters, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), rDown[aFrame][0]*Tile.size, rDown[aFrame][1]*Tile.size, rDown[aFrame][0]*Tile.size + Tile.size,rDown[aFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersHead, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), rDown[aFrame][0]*Tile.size, rDown[aFrame][1]*Tile.size, rDown[aFrame][0]*Tile.size + Tile.size,rDown[aFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersTop, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), rDown[aFrame][0]*Tile.size, rDown[aFrame][1]*Tile.size, rDown[aFrame][0]*Tile.size + Tile.size,rDown[aFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersBottom, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), rDown[aFrame][0]*Tile.size, rDown[aFrame][1]*Tile.size, rDown[aFrame][0]*Tile.size + Tile.size,rDown[aFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.weaponBow, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), rDown[aFrame][0]*Tile.size, rDown[aFrame][1]*Tile.size, rDown[aFrame][0]*Tile.size + Tile.size,rDown[aFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.weaponArrow, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), rDown[aFrame][0]*Tile.size, rDown[aFrame][1]*Tile.size, rDown[aFrame][0]*Tile.size + Tile.size,rDown[aFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersShoulder, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), rDown[aFrame][0]*Tile.size, rDown[aFrame][1]*Tile.size, rDown[aFrame][0]*Tile.size + Tile.size,rDown[aFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersHand, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), rDown[aFrame][0]*Tile.size, rDown[aFrame][1]*Tile.size, rDown[aFrame][0]*Tile.size + Tile.size,rDown[aFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersShoes, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), rDown[aFrame][0]*Tile.size, rDown[aFrame][1]*Tile.size, rDown[aFrame][0]*Tile.size + Tile.size,rDown[aFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersBelt, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), rDown[aFrame][0]*Tile.size, rDown[aFrame][1]*Tile.size, rDown[aFrame][0]*Tile.size + Tile.size,rDown[aFrame][1]*Tile.size + Tile.size, null);

			}else if(isFacingUp()){
				g.drawImage(Tile.characters, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), rUp[aFrame][0]*Tile.size, rUp[aFrame][1]*Tile.size, rUp[aFrame][0]*Tile.size + Tile.size,rUp[aFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersHead, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), rUp[aFrame][0]*Tile.size, rUp[aFrame][1]*Tile.size, rUp[aFrame][0]*Tile.size + Tile.size,rUp[aFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersTop, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), rUp[aFrame][0]*Tile.size, rUp[aFrame][1]*Tile.size, rUp[aFrame][0]*Tile.size + Tile.size,rUp[aFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersBottom, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), rUp[aFrame][0]*Tile.size, rUp[aFrame][1]*Tile.size, rUp[aFrame][0]*Tile.size + Tile.size,rUp[aFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.weaponBow, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), rUp[aFrame][0]*Tile.size, rUp[aFrame][1]*Tile.size, rUp[aFrame][0]*Tile.size + Tile.size,rUp[aFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.weaponArrow, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), rUp[aFrame][0]*Tile.size, rUp[aFrame][1]*Tile.size, rUp[aFrame][0]*Tile.size + Tile.size,rUp[aFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersShoulder, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), rUp[aFrame][0]*Tile.size, rUp[aFrame][1]*Tile.size, rUp[aFrame][0]*Tile.size + Tile.size,rUp[aFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersHand, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), rUp[aFrame][0]*Tile.size, rUp[aFrame][1]*Tile.size, rUp[aFrame][0]*Tile.size + Tile.size,rUp[aFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersShoes, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), rUp[aFrame][0]*Tile.size, rUp[aFrame][1]*Tile.size, rUp[aFrame][0]*Tile.size + Tile.size,rUp[aFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersBelt, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), rUp[aFrame][0]*Tile.size, rUp[aFrame][1]*Tile.size, rUp[aFrame][0]*Tile.size + Tile.size,rUp[aFrame][1]*Tile.size + Tile.size, null);

			}else if(isFacingLeft()){
				g.drawImage(Tile.characters, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), rLeft[aFrame][0]*Tile.size, rLeft[aFrame][1]*Tile.size, rLeft[aFrame][0]*Tile.size + Tile.size,rLeft[aFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersHead, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), rLeft[aFrame][0]*Tile.size, rLeft[aFrame][1]*Tile.size, rLeft[aFrame][0]*Tile.size + Tile.size,rLeft[aFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersTop, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), rLeft[aFrame][0]*Tile.size, rLeft[aFrame][1]*Tile.size, rLeft[aFrame][0]*Tile.size + Tile.size,rLeft[aFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersBottom, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), rLeft[aFrame][0]*Tile.size, rLeft[aFrame][1]*Tile.size, rLeft[aFrame][0]*Tile.size + Tile.size,rLeft[aFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.weaponBow, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), rLeft[aFrame][0]*Tile.size, rLeft[aFrame][1]*Tile.size, rLeft[aFrame][0]*Tile.size + Tile.size,rLeft[aFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.weaponArrow, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), rLeft[aFrame][0]*Tile.size, rLeft[aFrame][1]*Tile.size, rLeft[aFrame][0]*Tile.size + Tile.size,rLeft[aFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersShoulder, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), rLeft[aFrame][0]*Tile.size, rLeft[aFrame][1]*Tile.size, rLeft[aFrame][0]*Tile.size + Tile.size,rLeft[aFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersHand, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), rLeft[aFrame][0]*Tile.size, rLeft[aFrame][1]*Tile.size, rLeft[aFrame][0]*Tile.size + Tile.size,rLeft[aFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersShoes, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), rLeft[aFrame][0]*Tile.size, rLeft[aFrame][1]*Tile.size, rLeft[aFrame][0]*Tile.size + Tile.size,rLeft[aFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersBelt, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), rLeft[aFrame][0]*Tile.size, rLeft[aFrame][1]*Tile.size, rLeft[aFrame][0]*Tile.size + Tile.size,rLeft[aFrame][1]*Tile.size + Tile.size, null);

			}else if(isFacingRight()){
				g.drawImage(Tile.characters, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), rRight[aFrame][0]*Tile.size, rRight[aFrame][1]*Tile.size, rRight[aFrame][0]*Tile.size + Tile.size,rRight[aFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersHead, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), rRight[aFrame][0]*Tile.size, rRight[aFrame][1]*Tile.size, rRight[aFrame][0]*Tile.size + Tile.size,rRight[aFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersTop, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), rRight[aFrame][0]*Tile.size, rRight[aFrame][1]*Tile.size, rRight[aFrame][0]*Tile.size + Tile.size,rRight[aFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersBottom, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), rRight[aFrame][0]*Tile.size, rRight[aFrame][1]*Tile.size, rRight[aFrame][0]*Tile.size + Tile.size,rRight[aFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.weaponBow, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), rRight[aFrame][0]*Tile.size, rRight[aFrame][1]*Tile.size, rRight[aFrame][0]*Tile.size + Tile.size,rRight[aFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.weaponArrow, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), rRight[aFrame][0]*Tile.size, rRight[aFrame][1]*Tile.size, rRight[aFrame][0]*Tile.size + Tile.size,rRight[aFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersShoulder, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), rRight[aFrame][0]*Tile.size, rRight[aFrame][1]*Tile.size, rRight[aFrame][0]*Tile.size + Tile.size,rRight[aFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersHand, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), rRight[aFrame][0]*Tile.size, rRight[aFrame][1]*Tile.size, rRight[aFrame][0]*Tile.size + Tile.size,rRight[aFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersShoes, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), rRight[aFrame][0]*Tile.size, rRight[aFrame][1]*Tile.size, rRight[aFrame][0]*Tile.size + Tile.size,rRight[aFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersBelt, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), rRight[aFrame][0]*Tile.size, rRight[aFrame][1]*Tile.size, rRight[aFrame][0]*Tile.size + Tile.size,rRight[aFrame][1]*Tile.size + Tile.size, null);

			}
		}else if(castSpell){
			if(isFacingDown()){
				g.drawImage(Tile.characters, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), mDown[mFrame][0]*Tile.size, mDown[mFrame][1]*Tile.size, mDown[mFrame][0]*Tile.size + Tile.size,mDown[mFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersHead, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), mDown[mFrame][0]*Tile.size, mDown[mFrame][1]*Tile.size, mDown[mFrame][0]*Tile.size + Tile.size,mDown[mFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersTop, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), mDown[mFrame][0]*Tile.size, mDown[mFrame][1]*Tile.size, mDown[mFrame][0]*Tile.size + Tile.size,mDown[mFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersBottom, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), mDown[mFrame][0]*Tile.size, mDown[mFrame][1]*Tile.size, mDown[mFrame][0]*Tile.size + Tile.size,mDown[mFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.weaponBow, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), mDown[mFrame][0]*Tile.size, mDown[mFrame][1]*Tile.size, mDown[mFrame][0]*Tile.size + Tile.size,mDown[mFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.weaponArrow, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), mDown[mFrame][0]*Tile.size, mDown[mFrame][1]*Tile.size, mDown[mFrame][0]*Tile.size + Tile.size,mDown[mFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersShoulder, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), mDown[mFrame][0]*Tile.size, mDown[mFrame][1]*Tile.size, mDown[mFrame][0]*Tile.size + Tile.size,mDown[mFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersHand, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), mDown[mFrame][0]*Tile.size, mDown[mFrame][1]*Tile.size, mDown[mFrame][0]*Tile.size + Tile.size,mDown[mFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersShoes, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), mDown[mFrame][0]*Tile.size, mDown[mFrame][1]*Tile.size, mDown[mFrame][0]*Tile.size + Tile.size,mDown[mFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersBelt, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), mDown[mFrame][0]*Tile.size, mDown[mFrame][1]*Tile.size, mDown[mFrame][0]*Tile.size + Tile.size,mDown[mFrame][1]*Tile.size + Tile.size, null);

			}else if(isFacingUp()){
				g.drawImage(Tile.characters, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), mUp[mFrame][0]*Tile.size, mUp[mFrame][1]*Tile.size, mUp[mFrame][0]*Tile.size + Tile.size,mUp[mFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersHead, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), mUp[mFrame][0]*Tile.size, mUp[mFrame][1]*Tile.size, mUp[mFrame][0]*Tile.size + Tile.size,mUp[mFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersTop, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), mUp[mFrame][0]*Tile.size, mUp[mFrame][1]*Tile.size, mUp[mFrame][0]*Tile.size + Tile.size,mUp[mFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersBottom, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), mUp[mFrame][0]*Tile.size, mUp[mFrame][1]*Tile.size, mUp[mFrame][0]*Tile.size + Tile.size,mUp[mFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.weaponBow, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), mUp[mFrame][0]*Tile.size, mUp[mFrame][1]*Tile.size, mUp[mFrame][0]*Tile.size + Tile.size,mUp[mFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.weaponArrow, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), mUp[mFrame][0]*Tile.size, mUp[mFrame][1]*Tile.size, mUp[mFrame][0]*Tile.size + Tile.size,mUp[mFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersShoulder, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), mUp[mFrame][0]*Tile.size, mUp[mFrame][1]*Tile.size, mUp[mFrame][0]*Tile.size + Tile.size,mUp[mFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersHand, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), mUp[mFrame][0]*Tile.size, mUp[mFrame][1]*Tile.size, mUp[mFrame][0]*Tile.size + Tile.size,mUp[mFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersShoes, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), mUp[mFrame][0]*Tile.size, mUp[mFrame][1]*Tile.size, mUp[mFrame][0]*Tile.size + Tile.size,mUp[mFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersBelt, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), mUp[mFrame][0]*Tile.size, mUp[mFrame][1]*Tile.size, mUp[mFrame][0]*Tile.size + Tile.size,mUp[mFrame][1]*Tile.size + Tile.size, null);

			}else if(isFacingLeft()){
				g.drawImage(Tile.characters, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), mLeft[mFrame][0]*Tile.size, mLeft[mFrame][1]*Tile.size, mLeft[mFrame][0]*Tile.size + Tile.size,mLeft[mFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersHead, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), mLeft[mFrame][0]*Tile.size, mLeft[mFrame][1]*Tile.size, mLeft[mFrame][0]*Tile.size + Tile.size,mLeft[mFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersTop, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), mLeft[mFrame][0]*Tile.size, mLeft[mFrame][1]*Tile.size, mLeft[mFrame][0]*Tile.size + Tile.size,mLeft[mFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersBottom, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), mLeft[mFrame][0]*Tile.size, mLeft[mFrame][1]*Tile.size, mLeft[mFrame][0]*Tile.size + Tile.size,mLeft[mFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.weaponBow, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), mLeft[mFrame][0]*Tile.size, mLeft[mFrame][1]*Tile.size, mLeft[mFrame][0]*Tile.size + Tile.size,mLeft[mFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.weaponArrow, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), mLeft[mFrame][0]*Tile.size, mLeft[mFrame][1]*Tile.size, mLeft[mFrame][0]*Tile.size + Tile.size,mLeft[mFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersShoulder, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), mLeft[mFrame][0]*Tile.size, mLeft[mFrame][1]*Tile.size, mLeft[mFrame][0]*Tile.size + Tile.size,mLeft[mFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersHand, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), mLeft[mFrame][0]*Tile.size, mLeft[mFrame][1]*Tile.size, mLeft[mFrame][0]*Tile.size + Tile.size,mLeft[mFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersShoes, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), mLeft[mFrame][0]*Tile.size, mLeft[mFrame][1]*Tile.size, mLeft[mFrame][0]*Tile.size + Tile.size,mLeft[mFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersBelt, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), mLeft[mFrame][0]*Tile.size, mLeft[mFrame][1]*Tile.size, mLeft[mFrame][0]*Tile.size + Tile.size,mLeft[mFrame][1]*Tile.size + Tile.size, null);

			}else if(isFacingRight()){
				g.drawImage(Tile.characters, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), mRight[mFrame][0]*Tile.size, mRight[mFrame][1]*Tile.size, mRight[mFrame][0]*Tile.size + Tile.size,mRight[mFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersHead, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), mRight[mFrame][0]*Tile.size, mRight[mFrame][1]*Tile.size, mRight[mFrame][0]*Tile.size + Tile.size,mRight[mFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersTop, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), mRight[mFrame][0]*Tile.size, mRight[mFrame][1]*Tile.size, mRight[mFrame][0]*Tile.size + Tile.size,mRight[mFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersBottom, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), mRight[mFrame][0]*Tile.size, mRight[mFrame][1]*Tile.size, mRight[mFrame][0]*Tile.size + Tile.size,mRight[mFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.weaponBow, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), mRight[mFrame][0]*Tile.size, mRight[mFrame][1]*Tile.size, mRight[mFrame][0]*Tile.size + Tile.size,mRight[mFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.weaponArrow, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), mRight[mFrame][0]*Tile.size, mRight[mFrame][1]*Tile.size, mRight[mFrame][0]*Tile.size + Tile.size,mRight[mFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersShoulder, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), mRight[mFrame][0]*Tile.size, mRight[mFrame][1]*Tile.size, mRight[mFrame][0]*Tile.size + Tile.size,mRight[mFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersHand, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), mRight[mFrame][0]*Tile.size, mRight[mFrame][1]*Tile.size, mRight[mFrame][0]*Tile.size + Tile.size,mRight[mFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersShoes, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), mRight[mFrame][0]*Tile.size, mRight[mFrame][1]*Tile.size, mRight[mFrame][0]*Tile.size + Tile.size,mRight[mFrame][1]*Tile.size + Tile.size, null);
				g.drawImage(Tile.charactersBelt, Rx, Ry, (int) (Rx + Pwidth), (int) (Ry + Pheight), mRight[mFrame][0]*Tile.size, mRight[mFrame][1]*Tile.size, mRight[mFrame][0]*Tile.size + Tile.size,mRight[mFrame][1]*Tile.size + Tile.size, null);

			}
		}
	}

	/**update timers and sounds
	 * @param g is the graphics component
	 */
	public void update(Graphics g){
		for(Entity i : main.getEntities()){
			if(this.collidesWithAhead(i) && !i.equals(this) && i.getType() == EntityEnum.NPC && main.iseK())
				talkNPC = true;
		}
		increaseStats();
		interactsWithNPC();
		playSounds();
		if(flinching){
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;//get milliseconds
			if(elapsed > 1000){//1 sec of invinsibilty after being attack
				flinching = false;
			}
			if((System.nanoTime() / 1000000) % 5 == 0){
				paintReg(g);
			}
		}
		if(!flinching){
			if (health != maxHealth){
				long elapsed = (System.nanoTime() - flinchTimer) / 1000000;//get milliseconds
				if(elapsed > 5000)//before regeneration starts
					regenHealth();
			}
		}
		if(fired){
			long elapsed = (System.nanoTime() - arrowTimer) / 1000000;//get milliseconds
			if(elapsed > 1000){
				fired = false;
			}
		}
		if(slashed){
			long elapsed = (System.nanoTime() - slashTimer) / 1000000;//get milliseconds
			if(elapsed > 250){
				slashed = false;
			}
		}
		if(castSpell){
			long elapsed = (System.nanoTime() - spellDelay) / 1000000;//get milliseconds
			if(elapsed > 500){
				castSpell = false;
			}
		}
		if(stunned){
			long elapsed = (System.nanoTime() - effectTimer) / 1000000;
			if(elapsed > 1500)
				stunned = false;
		}
		if(dizzy){
			long elapsed = (System.nanoTime() - dizTimer) / 1000000;
			if(elapsed > 3000){
				moveSpeed = 0.1; 
				dizzy = false;
			}else{
				moveSpeed = 4.9;
				g.drawImage(Tile.dizzy, (int) (Rx + 25),(int) (Ry), (int) (width / 1.5), height / 2, null);
			}
		}
		if(burned){
			long elapsed = (System.nanoTime() - burnTimer) / 1000000;
			if(elapsed > 3000)
				burned = false;
			else
				takeDamage(5);
		}
		//reset stats to full if the boss has been defeated
		if(killedBoss){
			health = maxHealth;
			manaCount = 5;
			arrowCount = 30;
			main.getShop().setCoinCount((main.getShop().getCoinCount()+40));
			killedBoss = false;
		}
		main.getSpawner().newWave(g);
	}

	/**draw health bar (gradient)
	 * @param g is the graphics component
	 */
	public void drawHealth(Graphics g){
		g.setColor(Color.BLACK);
		g.setFont(new Font("Impact", Font.BOLD, 22));
		g.drawString(Integer.toString((int)arrowCount) + "/30", Rx - 420, Ry - 175);
		double percent = health / maxHealth;
		g.drawImage(Tile.arrowDisp, Rx - 522, Ry - 230, 100, 100, null);
		if(percent < 0)
			percent = 0;
		if(percent > 1)
			percent = 1;
		//Get the amount of red and green to draw as a value out of 255(max)
		int red = (int) (255 * (1 - percent));
		int green = (int) (255 * percent);
		g.setColor(new Color(red,green,0));
		g.drawRect((int) (Rx - 522), (int) (Ry - 230), 200, 25);
		g.fillRect((int) (Rx - 522), (int) (Ry - 230),(int) (percent * 200), 25);
		g.setColor(Color.BLACK);
		g.drawString(Integer.toString((int)health) + "/" + Integer.toString((int)maxHealth), Rx - 460, Ry - 207);
	}


	/**render everything regarding the player
	 * @param g is the graphics comppnent
	 */
	public void render(Graphics g){
		updateAnimations();
		update(g);
		drawHealth(g);
		//g.fillRect((int)Px, (int)Py, (int)width, (int)height);
		g.drawImage(Tile.coin, (int) Rx + 500, (int) Ry + 250, (int) Tile.size / 2, (int) Tile.size / 2, null);
		g.drawImage(Tile.mana, (int) Rx + 500, (int) Ry + 210, (int) Tile.size / 2, (int) Tile.size / 2, null);
		g.drawString(Integer.toString(main.getShop().getCoinCount()), (int) Rx + 540, (int) Ry + 280);
		g.drawString(Integer.toString(manaCount), (int) Rx + 540, (int) Ry + 240);

		if(!flinching)
			paintReg(g);
	}

	//getters and setter for the player

	public boolean isFacingRight() {
		return facingRight;
	}

	public void setFacingRight(boolean facingRight) {
		this.facingRight = facingRight;
	}

	public boolean isFacingDown() {
		return facingDown;
	}

	public void setFacingDown(boolean facingDown) {
		this.facingDown = facingDown;
	}

	public boolean isFacingUp() {
		return facingUp;
	}

	public void setFacingUp(boolean facingUp) {
		this.facingUp = facingUp;
	}

	public boolean isFacingLeft() {
		return facingLeft;
	}

	public void setFacingLeft(boolean facingLeft) {
		this.facingLeft = facingLeft;
	}

	public ArrayList<ItemDrop> getDrops() {
		return drops;
	}

	public void setDrops(ArrayList<ItemDrop> drops) {
		this.drops = drops;
	}

	public boolean isKilledBoss() {
		return killedBoss;
	}

	public void setKilledBoss(boolean killedBoss) {
		this.killedBoss = killedBoss;
	}

	public boolean isCastSpell() {
		return castSpell;
	}

	public void setCastSpell(boolean castSpell) {
		this.castSpell = castSpell;
	}

	public void setTalkNPC(boolean talkNPC) {
		this.talkNPC = talkNPC;
	}

	public boolean isTalkNPC() {
		return talkNPC;
	}

	public void setFireSpell(boolean fireSpell) {
		this.fireSpell = fireSpell;
	}
	public void setEarthSpell(boolean earthSpell) {
		this.earthSpell = earthSpell;
	}
	public void setLightingSpell(boolean lightingSpell) {
		this.lightingSpell = lightingSpell;
	}
	public void setWindSpell(boolean windSpell) {
		this.windSpell = windSpell;
	}
	public void setWaterSpell(boolean waterSpell) {
		this.waterSpell = waterSpell;
	}
	public double getBaseDamage() {
		return baseDamage;
	}
	public double regenPerc(){
		return (regen / 5);
	}
}
