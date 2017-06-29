/**
 * To render and buy items from the shop
 * Shop.java
 * @author Umar Syed
 */
package infinity.drops;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

import infinity.entites.Entity;
import infinity.entites.EntityEnum;
import infinity.entites.Pet;
import infinity.handlers.MouseHandler;
import infinity.level.Tile;
import infinity.main.Main;

public class Shop{
	//Shop button areas
	private Rectangle shop[][] = new Rectangle[4][6];
	//ids of the items currently in the shop
	protected int shopID[][] = { {0, 1, 2, 3, 4, 5}, { 6, 7, 8, 9, 10, 11 },
			{ 12, 13, 14, 15, 16, 17}, { 18, 19, 20, 21, 22, -1 } };
	//price of each item
	private int shopPrice[][] = { {200, 200, 200, 200, 200, 200}, 
			{100, 100, 100, 100, 100, 100},
			{40, 40, 40, 40, 40, 40}, 
			{100, 100, 100, 100, 400, 0}};
	private Armour armour[][];
	private Armour tempArm;
	// inventory object
	public Inventory inventory;
	//Item currently holding in the shop
	private int heldID;
	//player coins
	private int coinCount = 20000;
	private Main main;

	public Shop(Main main){
		this.main = main;
		shopValues();
		this.inventory = new Inventory(main);
		armour = new Armour[shopPrice.length][shopPrice[0].length];
		setArmour();
	}

	/**set the stats of all Armour in the game
	 * 
	 */
	public void setArmour(){
		//reset the Armour array
		for(int i = 0; i < shopID.length; i++){
			for(int j = 0; j < shopID[i].length; j++){
				int id = shopID[i][j];
				if(id >=0 && id <= 5)
					armour[i][j] = new Armour(30, 20, 5, false, false, false);
				else if (id > 5 && id <= 11)
					armour[i][j] = new Armour(20, 10, 0, false, false , false);
				else if(id > 11 && id < 17)
					armour[i][j] = new Armour(10,0,0, false, false , false);
				else if(id > 17 && id <= 21)
					armour[i][j] = new Armour(0, 40, 0, false, false , false);
				else
					armour[i][j] = new Armour(0, 0, 0, false, false , false);
			}
		}
	}

	/**
	 * if the player clicks and item in to shop to buy
	 * add that item to inventory if enough money and purchasable item
	 */
	public void click() {
		for (int i = 0; i < shop.length; i++) {
			for (int j = 0; j < shop[i].length; j++) {
				// if the click is within an inventory button area
				if (shop[i][j].contains(MouseHandler.mx, MouseHandler.my)) {
					// if that shop slot holds an item
					if (shopID[i][j] != -1) {
						// if you have enough money to buy the item
						if (coinCount >= shopPrice[i][j]){ 
							if(shopID[i][j] == 22){
								Entity pet = new Pet(main, main.getPlayer().getX() + main.getCamOfSetX(), 
										main.getPlayer().getY() + main.getCamOfSetY(), 32, 32, 100);
								main.getEntities().add(pet);
							}
							heldID = shopID[i][j];// holding that item
							armour[i][j].setPurchased(true);
							tempArm = armour[i][j];
							shopID[i][j] = -1;//shop slot set to empty
							coinCount -= shopPrice[i][j];
							if(shopID[i][j] != 22)
								addToInv();
						}	
					}
				}
			}
		}
	}

	/**
	 * add item to inventory if enough space in inventory
	 * @return true if item was added to inventory otherwise false
	 */
	public boolean addToInv(){
		int buttonID[][] = inventory.getButtonID();
		for (int a = 0; a < buttonID.length; a++) {
			for (int b = 0; b < buttonID[a].length; b++) {
				//if the button slot is empty
				if (buttonID[a][b] == -1){
					buttonID[a][b] = heldID;// add the hold item to that slot
					Inventory.invArm[a][b] = tempArm;
					inventory.setButtonID(buttonID);//give that button id back to inventory
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * Draws the shop and all items within the shop if those items are still available
	 * @param g
	 */
	public void render(Graphics g){
		g.drawImage(Tile.shop, 400, 50, 400, 550, null);
		for (int i = 0; i < shop.length; i++) {
			for (int j = 0; j < shop[i].length; j++) {

				//Highlight the button if hovering over
				if (shop[i][j].contains(MouseHandler.mx, MouseHandler.my)){
					g.setColor(new Color(255, 255, 255, 150));
					g.fillRect(shop[i][j].x, shop[i][j].y, shop[i][j].width, shop[i][j].height);
				}
				// if that shop slot is not empty draw the correct item depending on the id
				if (shopID[i][j] != -1)
					g.drawImage(Tile.items, shop[i][j].x, shop[i][j].y,shop[i][j].x+ shop[i][j].width,shop[i][j].y+ shop[i][j].height,shopID[i][j]*64,0,shopID[i][j]*64+64,64, null);

				//draw the stats for each item
				if (shop[i][j].contains(MouseHandler.mx, MouseHandler.my)){
					if(shopPrice[i][j] <= coinCount)
						g.setColor(Color.GREEN);
					else
						g.setColor(Color.RED);
					g.setFont(new Font("Impact", Font.BOLD, 11));
					g.drawString("$ " + Integer.toString(shopPrice[i][j]), (int) MouseHandler.mx, (int) MouseHandler.my);
					g.drawString("Health: " + Integer.toString(armour[i][j].getHealthIncr()), (int) MouseHandler.mx, (int) MouseHandler.my + 10);
					g.drawString("Damage: " + Integer.toString(armour[i][j].getDamIncr()), (int) MouseHandler.mx, (int) MouseHandler.my + 20);
					g.drawString("Regen: " + Integer.toString(armour[i][j].getRegenFactor()), (int) MouseHandler.mx, (int) MouseHandler.my + 30);
				}
			}
		}
	}

	/**
	 * Gives bounds to each button in the shop
	 */
	private void shopValues() {
		for (int i = 0; i < shop.length; i++) {
			for (int j = 0; j < shop[i].length; j++) {
				shop[i][j] = new Rectangle(416 + ((62) * j), 370 + (55 * i), 57, 50);
			}
		}
	}

	public int getCoinCount() {
		return coinCount;
	}
	public void setCoinCount(int coinCount) {
		this.coinCount = coinCount;
	}
}
