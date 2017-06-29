/**
 * Entity Class - For All Game Entities (Player, Enemies, NPC's)
 * Entity.java
 * @author Nim Wijetunga
 */

package infinity.entites;

import java.awt.Graphics;
import java.awt.Rectangle;

import infinity.main.Main;


public abstract class Entity extends Rectangle{
	//x and y position of entity
	protected double x, y;
	protected double Px, Py;

	//health of entity
	protected double health, maxHealth;
	protected long effectTimer, burnTimer, dizTimer;
	protected boolean stunned, burned, dizzy;


	//Entities width and height
	protected int width;
	protected int height;
	protected int Pwidth, Pheight;

	//Object of main class and Enum for Entity Type
	protected Main main;
	protected EntityEnum type;


	/**Constructor
	 * 
	 * @param type is the type of Entity created
	 * @param main
	 * @param x is the x position of entity
	 * @param y is the y position of entity
	 * @param width the width of entity
	 * @param height the height of entity
	 * @param health the health of the entity
	 */
	public Entity(EntityEnum type, Main main, double x, double y, int width, int height, double health){
		//Specific Collision Boundaries for Player
		this.setPy(y+9);
		this.setPx(x+18);
		this.Pwidth = 64;
		this.Pheight = 64;

		//Collision Properties of all other entities
		this.setX(x);
		this.setY(y);
		this.setWidth(width);
		this.setHeight(height);
		this.health = health;
		maxHealth = health;
		this.setMain(main);
		this.setType(type);
	}

	//Abstract methods which all subclasses must implement 
	public abstract void move(double delta);
	public abstract void render(Graphics g);

	/**collidesWith to check if one entity intersects with another
	 * @param entity is the entity being checked for a collision with
	 * @param isPlayer to see if the character colliding is the player is not
	 * @return returns true if the entities intersect false otherwise 
	 */
	public boolean collidesWith(Entity entity, boolean isPlayer){
		//Special collision box for player(takes into account the camera offset
		if(isPlayer){
			this.setBounds((int) (getMain().getCamOfSetX() + getPx()), (int) (getMain().getCamOfSetY() + getPy()), (int)getWidth(), (int)getHeight());
		}else{//For other entities
			this.setBounds((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
		}
		//Collision box for the entity that we are checking for a collision with
		entity.setBounds((int)entity.getX(), (int)entity.getY(), (int)entity.getWidth(), (int)entity.getHeight());
		return this.intersects(entity);
	}
	
	/**collidesWithAhed to check if player collides with the NPC when he is stationary
	 * @param entity is the entity being checked for the collision with respect to the player
	 * @return returns true if a collision occurs, false otherwise
	 */
	public boolean collidesWithAhead(Entity entity){
		//Special collision box for player(takes into account the camera offset (plus 3 pixels so we check ahead of player)
		this.setBounds((int) (getMain().getCamOfSetX() + getPx()), (int) (getMain().getCamOfSetY() + getPy()), (int)getWidth(), (int)getHeight());
		//Collision box for the entity that we are checking for a collision with
		entity.setBounds((int)entity.getX() - 10, (int)entity.getY() - 10, 80, 80);
		return this.intersects(entity);
	}

	/**collidesWith and overloaded method to check if a given entity collides with all other entities on the map
	 * @param player to see if the entity being checked for collisions is the player
	 */
	public boolean collidesWithEntity(boolean player){
		//If the player we only want to check collisions with NPC not enemies
		if(player){
			for(Entity i : getMain().getEntities()){
				if(this.collidesWith(i, true) && !this.equals(i) && i.type == EntityEnum.NPC){
					//Check if the player is talking with NPC while colliding
					if(main.iseK())
						main.getPlayer().setTalkNPC(true);
					return true;
				}
			}
		}else{//Check all entities for collisions 
			for(Entity i : getMain().getEntities()){
				if(this.collidesWith(i, false) && !this.equals(i) && i.getType() != EntityEnum.PET){
					return true;
				}
			}
		}
		return false;
	}
	
	/**stun the enemy if attacked by lightning
	 * 
	 */
	public void stun(){
		stunned = true;
		effectTimer = System.nanoTime();
	}

	/**burn the enemy if attacked by fire
	 * 
	 */
	public void burn(){
		burned = true;
		burnTimer = System.nanoTime();
	}

	public void dizzy(){
		dizzy = true;
		dizTimer = System.nanoTime();
	}

	//Getters and setters for instance variables

	public double getHealth() {
		return health;
	}

	public void setHealth(double health) {
		this.health = health;
	}

	public double getPx() {
		return Px;
	}

	public void setPx(double Px) {
		this.Px = Px;
	}

	public double getPy() {
		return Py;
	}

	public void setPy(double Py) {
		this.Py = Py;
	}
	public double getPWidth() {
		return Pwidth;
	}

	public double getPHeight() {
		return Pheight;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public Main getMain() {
		return main;
	}

	public void setMain(Main main) {
		this.main = main;
	}

	public EntityEnum getType() {
		return type;
	}

	public void setType(EntityEnum type) {
		this.type = type;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}
}
