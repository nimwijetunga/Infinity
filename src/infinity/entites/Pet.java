package infinity.entites;

import java.awt.Graphics;
import java.util.concurrent.ThreadLocalRandom;

import infinity.level.Tile;
import infinity.main.Main;
import infinity.weapons.Magic;
import infinity.weapons.WeaponEnum;

public class Pet extends Entity{

	private boolean up,down,left,right, moved, fired, isMoving;
	private double moveSpeed, pX, pY;
	private long moveTimer, fireTimer;

	private  int[][] pImgUp = {{0,0}, {1,0}, {2,0}};
	private  int[][] pImgLeft = {{0,1}, {1,1}, {2,1}};
	private  int[][] pImgRight = {{0,2}, {1,2}, {2,2}};
	private  int[][] pImgDown = {{0,3}, {1,3}, {2,3}};
	private int [][]lastImg = pImgDown;

	private int aniFrame = 0;
	private int aniTime =  0;
	private int aniSpeed = 15;

	public Pet(Main main, double x, double y, int width, int height, double health) {
		super(EntityEnum.PET, main, x, y, width, height, health);
		moveSpeed = 9;
	}

	public void move(double delta) {
		pX = getMain().getPlayer().Px + getMain().getCamOfSetX();
		pY = getMain().getPlayer().Py + getMain().getCamOfSetY();
		if(main.getPlayer().isFacingRight())
			pX += 64;
		if(main.getPlayer().isFacingLeft())
			pX -= 64;
		if(main.getPlayer().isFacingUp())
			pY -= 45;
		if(main.getPlayer().isFacingDown())
			pY += 55;
		double origX = x, origY = y;
		moveX(delta, pX);
		moveY(delta, pY);
		if(origX == x && origY == y)
			isMoving = false;
		else
			isMoving = true;
		moved = true;
		moveTimer = System.nanoTime();
		attack();
	}

	/**moveX to move the enemy in the x direction
	 * @param delta is a movement speed modifier
	 */
	public void moveX(double delta, double pX){
		//Move right if the player is to the right
		double orig = x;
		if(((int) x < (int) pX - moveSpeed) && !stunned){
			x += moveSpeed;
			right = true;
			left = false;
			up = false;
			down = false;
		}

		//Move left if the player is to to the left
		if(((int) x > (int)pX + moveSpeed) && !stunned){
			x -= moveSpeed;
			right = false;
			left = true;
			up = false;
			down = false;
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
			right = false;
			left = false;
			up = false;
			down = true;
		}

		//move the enemy up if the player is above
		if(((int) y > (int) pY + moveSpeed) && !stunned){ 
			y -= moveSpeed;
			right = false;
			left = false;
			up = true;
			down = false;
		}
		//check for collisions
		collisionY();
	}

	/**check collisions in the x direction
	 * 
	 */
	public void collisionX(){
		//cancel out right movement if colliding with entity or tile
		if(right && !stunned){
			if(getMain().getLevel().tileIsSolid(this,false) || collidesWithEntity(false))
				x -= moveSpeed;
		}
		//cancel out left movement if colliding with entity or tile
		if(left && !stunned){
			if(getMain().getLevel().tileIsSolid(this,false) || collidesWithEntity(false))
				x += moveSpeed;
		}
	}

	/**check collisions in the x direction
	 * 
	 */
	public void collisionY(){
		//cancel out up movement if colliding with entity or tile
		if(up && !stunned){
			if(getMain().getLevel().tileIsSolid(this,false) || collidesWithEntity(false))
				y += moveSpeed;
		}
		//cancel out down movement if colliding with entity or tile
		if(down && !stunned){
			if(getMain().getLevel().tileIsSolid(this,false) || collidesWithEntity(false))
				y -= moveSpeed;
		}
	}

	/**update render animations (movement)
	 * 
	 */
	public void updateAnimations(){
		if(isMoving){
			aniTime++; 
			//If the animation time equals the animation speed switch the frame
			if(aniTime >= aniSpeed){
				aniFrame++;
				aniTime = 0;
				if(aniFrame > 2){
					aniFrame = 0;
				}
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
		if(fired){
			long elapsed = (System.nanoTime() - fireTimer) / 1000000;
			if(elapsed > 5000)
				fired = false;
		}
	}

	public WeaponEnum type(){
		int rand = ThreadLocalRandom.current().nextInt(1, 5);
		switch(rand){
		case 1: return WeaponEnum.FIRE;
		case 2 : return WeaponEnum.WIND;
		case 3 : return WeaponEnum.LIGHTNING;
		case 4 : return WeaponEnum.WATER;
		}
		return null;
	}

	public void attack(){
		if(!fired){
			getMain().getWeapons().add(new Magic(type(),x, 
					y - getHeight(), 0, 200, 32, 32, getMain(), up,down,left,right,this));
			fired = true;
			fireTimer = System.nanoTime();
		}
	}

	public void render(Graphics g) {
		updateAnimations();
		if(up)
			g.drawImage(Tile.pet, (int) (x - getMain().getCamOfSetX()), (int) (y - getMain().getCamOfSetY()), (int) (x + getWidth() - getMain().getCamOfSetX()), (int) (y + getHeight() - getMain().getCamOfSetY()), pImgDown[aniFrame][0]*Tile.size, pImgDown[aniFrame][1]*Tile.size, pImgDown[aniFrame][0]*Tile.size + Tile.size,pImgDown[aniFrame][1]*Tile.size + Tile.size, null);
		if(down)
			g.drawImage(Tile.pet, (int) (x - getMain().getCamOfSetX()), (int) (y - getMain().getCamOfSetY()), (int) (x + getWidth() - getMain().getCamOfSetX()), (int) (y + getHeight() - getMain().getCamOfSetY()), pImgUp[aniFrame][0]*Tile.size, pImgUp[aniFrame][1]*Tile.size, pImgUp[aniFrame][0]*Tile.size + Tile.size,pImgUp[aniFrame][1]*Tile.size + Tile.size, null);
		if(left)
			g.drawImage(Tile.pet, (int) (x - getMain().getCamOfSetX()), (int) (y - getMain().getCamOfSetY()), (int) (x + getWidth() - getMain().getCamOfSetX()), (int) (y + getHeight() - getMain().getCamOfSetY()), pImgLeft[aniFrame][0]*Tile.size, pImgLeft[aniFrame][1]*Tile.size, pImgLeft[aniFrame][0]*Tile.size + Tile.size,pImgLeft[aniFrame][1]*Tile.size + Tile.size, null);
		if(right)
			g.drawImage(Tile.pet, (int) (x - getMain().getCamOfSetX()), (int) (y - getMain().getCamOfSetY()), (int) (x + getWidth()  - getMain().getCamOfSetX()), (int) (y + getHeight() - getMain().getCamOfSetY()), pImgRight[aniFrame][0]*Tile.size, pImgRight[aniFrame][1]*Tile.size, pImgRight[aniFrame][0]*Tile.size + Tile.size,pImgRight[aniFrame][1]*Tile.size + Tile.size, null);
	}

}
