/**
 * Enemy class for boss and regular enemies
 * Enemy.java
 * @author Nim Wijetunga
 */

package infinity.entites;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import infinity.handlers.SoundFX;
import infinity.level.Tile;
import infinity.main.Main;

public class Enemy extends Entity{

	//Enemy state varaibles
	//enemy movement and interaction booleans
	private boolean isDead, moved;
	private boolean facingUp, facingDown, facingLeft, facingRight;
	protected boolean flinching = false;
	protected boolean isHurt = false;
	private long flinchTimer, regenTimer, moveTimer;
	private double moveSpeed, pX, pY, baseDamage;

	//Enemy movement images
	private int[][] pImgUp = {{0,0}, {1,0}, {2,0}, {3,0}, {4,0}, {5,0}, {6,0}, {7,0}, {8,0}};
	private int[][] pImgLeft = {{0,1}, {1,1}, {2,1}, {3,1}, {4,1}, {5,1}, {6,1}, {7,1}, {8,1}};
	private int[][] pImgDown = {{0,2}, {1,2}, {2,2}, {3,2}, {4,2}, {5,2}, {6,2}, {7,2}, {8,2}};
	private int[][] pImgRight = {{0,3}, {1,3}, {2,3}, {3,3}, {4,3}, {5,3}, {6,3}, {7,3}, {8,3}};
	//int [][]lastImg = pImgDown;

	//Enemy animation variables
	private int aniFrame = 0;
	private int aniTime =  0;
	private int aniSpeed = 50;
	private double maxHealth;

	private double damage;

	/**
	 * @param type of Entity
	 * @param main 
	 * @param x position of enemy
	 * @param y position of enemy
	 * @param width of enemy
	 * @param height of enemy
	 * @param health of enemy
	 * @param damage of enemy
	 */
	public Enemy(EntityEnum type, Main main, double x, double y, int width, int height, double health, double damage) {
		super(type, main, x, y, width, height, health);
		this.damage = damage;
		this.setMain(main);
		this.maxHealth = health;
		baseDamage = damage;
		moveSpeed = 5;
	}

	/**move around the map according to the player's position
	 * 
	 */
	public void move(double delta){
		//get x and y of the player relative to the map
		pX = getMain().getPlayer().Px + getMain().getCamOfSetX();
		pY = getMain().getPlayer().Py + getMain().getCamOfSetY();
		if(!moved){
			if(!dizzy){
				moveX(delta, pX);
				moveY(delta, pY);
			}else{
				moveX(delta, -pX);
				moveY(delta, -pY);
			}
			moved = true;
			moveTimer = System.nanoTime();
		}
	}

	/**moveX to move the enemy in the x direction
	 * @param delta is a movement speed modifier
	 */
	public void moveX(double delta, double pX){
		//Move right if the player is to the right
		if(((int) x < (int) pX - moveSpeed) && !stunned){
			x += moveSpeed;
			facingRight = true;
			facingLeft = false;
			facingUp = false;
			facingDown = false;
		}

		//Move left if the player is to to the left
		if(((int) x > (int)pX + moveSpeed) && !stunned){
			x -= moveSpeed;
			facingRight = false;
			facingLeft = true;
			facingUp = false;
			facingDown = false;
		}
		//check for collisions 
		collisionX();
	}

	/**moveY to move the enemy in the y direction
	 * @param delta is a movement speed modifier
	 * @param y is the y position of the enemy
	 * @param pY is the y position of the player
	 */
	public void moveY(double delta, double pY){
		//move the enemy down if the player is below
		if(((int) y < (int)pY - moveSpeed) && !stunned){
			y += moveSpeed;
			facingRight = false;
			facingLeft = false;
			facingUp = false;
			facingDown = true;
		}

		//move the enemy up if the player is above
		if(((int) y > (int) pY + moveSpeed) && !stunned){ 
			y -= moveSpeed;
			facingRight = false;
			facingLeft = false;
			facingUp = true;
			facingDown = false;
		}
		//check for collisions
		collisionY();
	}

	/**check collisions in the x direction
	 * 
	 */
	public void collisionX(){
		//cancel out right movement if colliding with entity or tile
		if(facingRight && !stunned){
			if(getMain().getLevel().tileIsSolid(this,false) || collidesWithEntity(false))
				x -= moveSpeed;
		}
		//cancel out left movement if colliding with entity or tile
		if(facingLeft && !stunned){
			if(getMain().getLevel().tileIsSolid(this,false) || collidesWithEntity(false))
				x += moveSpeed;
		}
	}

	/**check collisions in the x direction
	 * 
	 */
	public void collisionY(){
		//cancel out up movement if colliding with entity or tile
		if(facingUp && !stunned){
			if(getMain().getLevel().tileIsSolid(this,false) || collidesWithEntity(false))
				y += moveSpeed;
		}
		//cancel out down movement if colliding with entity or tile
		if(facingDown && !stunned){
			if(getMain().getLevel().tileIsSolid(this,false) || collidesWithEntity(false))
				y -= moveSpeed;
		}
	}

	/**update render animations (movement)
	 * 
	 */
	public void updateAnimations(){
		aniTime++; 
		//If the animation time equals the animation speed switch the frame
		if(aniTime >= aniSpeed){
			aniFrame++;
			aniTime = 0;
			if(aniFrame > 8){
				aniFrame = 0;
			}
		}
		if(moved){
			if(moved){
				long elapsed = (System.nanoTime() - moveTimer) / 1000000;//get milliseconds
				if(elapsed > 150){
					moved = false;
				}
			}
		}
	}

	/**regenHealth if not being attacked by another entity
	 *
	 */
	public void regenHealth (){
		long elapsed = (System.nanoTime() - regenTimer) / 1000000;//get milliseconds
		//Add 5 to health given that the entity is not being atacked
		if(elapsed > 1500 && health < maxHealth){
			health += 5;
			regenTimer = System.nanoTime();
		}
		//cannot regen health if the enemy has full health 
		if (health >= maxHealth){
			health = maxHealth;
			isHurt = false;
		}
	}

	/**get damaged by player weapons
	 * @param damage is how much damage the enemy takes
	 */
	public void getDamaged(double damage){
		if(isDead() || flinching)
			return;
		SoundFX hit = getMain().getLevel().getClip("hit");
		hit.loop(1);
		if(health - damage <= 0){
			health = 0;
			setDead(true);
			main.getSpawner().setEnemyCount(main.getSpawner().getEnemyCount() - 1);
		}
		health -= damage;
		flinching = true;
		isHurt = true;
		flinchTimer = System.nanoTime();
	}

	/**render to get enemy sprites
	 * @param g is the graphics component
	 */
	public void render(Graphics g){
		boolean b;
		//If the enemy drawn is the boss
		if(this.type == EntityEnum.BOSS)
			b = true;
		else
			b = false;
		//Flinching and regular animations
		if(flinching){
			renderEnemy (g, b);
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;//get milliseconds
			if(elapsed > 500){//0.5  sec of invunerablity after being attacked22
				flinching = false;
			}
		}
		if(!flinching){
			renderEnemy(g, b);
			if (health != maxHealth){
				long elapsed = (System.nanoTime() - flinchTimer) / 1000000;//get milliseconds
				if(elapsed > 5000)//before regeneration starts
					regenHealth();
			}
		}
		double amount = main.getPlayer().regenPerc() * 3000;
		if(stunned){
			long elapsed = (System.nanoTime() - effectTimer) / 1000000;
			if(elapsed > amount)
				stunned = false;
		}
		if(dizzy){
			long elapsed = (System.nanoTime() - dizTimer) / 1000000;
			if(elapsed > amount){
				dizzy = false;
				damage = baseDamage;
			}else{
				damage = 0;
				g.drawImage(Tile.dizzy, (int) (x - main.getCamOfSetX() + 15),(int) (y - main.getCamOfSetY()), width / 2, width / 2, null);
			}
		}
		if(burned){
			long elapsed = (System.nanoTime() - burnTimer) / 1000000;
			if(elapsed > amount)
				burned = false;
			else
				getDamaged(main.getPlayer().getBaseDamage());
		}
	}


	/**drawHealth to draw enemy health bar
	 * @param g is graphics component
	 */
	public void drawHealth(Graphics g){
		// health bar render
		if (isHurt){ 
			double percent = health / maxHealth;
			//Set the color of the health bar according to health percentage
			if(percent < 0)
				percent = 0;
			if(percent <= 0.25)
				g.setColor(Color.RED);
			else if(percent > 0.25 && percent < 0.5)
				g.setColor(Color.YELLOW);
			else
				g.setColor(Color.GREEN);
			g.drawRect((int) (x - getMain().getCamOfSetX()), (int) (y - getMain().getCamOfSetY()),(int) getWidth(), 5);
			g.fillRect((int) (x - getMain().getCamOfSetX()), (int) (y - getMain().getCamOfSetY()),(int)(percent*getWidth()), 5);
		}
	}

	/**
	 * @param g
	 * @param boss to see if we are rendering boss
	 */
	public void renderEnemy (Graphics g, boolean boss){
		BufferedImage t;
		//Get proper image
		if(boss)
			t = Tile.boss1;
		else
			t = Tile.enemies;
		updateAnimations();
		if (flinching){
			g.drawImage(t, (int) (x - getMain().getCamOfSetX()), (int) (y - getMain().getCamOfSetY()), (int) (x + getWidth() - getMain().getCamOfSetX()), (int) (y + getHeight() - getMain().getCamOfSetY()), 3*Tile.size, 0, 3*Tile.size + Tile.size , Tile.size, null);
		}
		else{
			if(facingDown)
				g.drawImage(t, (int) (x - getMain().getCamOfSetX()), (int) (y - getMain().getCamOfSetY()), (int) (x + getWidth() - getMain().getCamOfSetX()), (int) (y + getHeight() - getMain().getCamOfSetY()), pImgDown[aniFrame][0]*Tile.size, pImgDown[aniFrame][1]*Tile.size, pImgDown[aniFrame][0]*Tile.size + Tile.size,pImgDown[aniFrame][1]*Tile.size + Tile.size, null);
			if(facingUp)
				g.drawImage(t, (int) (x - getMain().getCamOfSetX()), (int) (y - getMain().getCamOfSetY()), (int) (x + getWidth() - getMain().getCamOfSetX()), (int) (y + getHeight() - getMain().getCamOfSetY()), pImgUp[aniFrame][0]*Tile.size, pImgUp[aniFrame][1]*Tile.size, pImgUp[aniFrame][0]*Tile.size + Tile.size,pImgUp[aniFrame][1]*Tile.size + Tile.size, null);
			if(facingLeft)
				g.drawImage(t, (int) (x - getMain().getCamOfSetX()), (int) (y - getMain().getCamOfSetY()), (int) (x + getWidth() - getMain().getCamOfSetX()), (int) (y + getHeight() - getMain().getCamOfSetY()), pImgLeft[aniFrame][0]*Tile.size, pImgLeft[aniFrame][1]*Tile.size, pImgLeft[aniFrame][0]*Tile.size + Tile.size,pImgLeft[aniFrame][1]*Tile.size + Tile.size, null);
			if(facingRight)
				g.drawImage(t, (int) (x - getMain().getCamOfSetX()), (int) (y - getMain().getCamOfSetY()), (int) (x + getWidth()  - getMain().getCamOfSetX()), (int) (y + getHeight() - getMain().getCamOfSetY()), pImgRight[aniFrame][0]*Tile.size, pImgRight[aniFrame][1]*Tile.size, pImgRight[aniFrame][0]*Tile.size + Tile.size,pImgRight[aniFrame][1]*Tile.size + Tile.size, null);
		}
		drawHealth(g);

	}

	//Getters and setters for instance variables

	public boolean isDead() {
		return isDead;
	}

	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}

	public double getDamage() {
		return damage;
	}

	public void setDamage(double damage) {
		this.damage = damage;
	}

}
