/**
 * To render, equip, and move items within inventory 
 * Shop.java
 * @author Umar Syed
 */
package infinity.drops;

import infinity.handlers.MouseHandler;
import infinity.level.Tile;
import infinity.main.Main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Inventory extends Rectangle {
	//item currently holding id
	private int heldID;
	//player equipment changed variable
	private boolean changed;
	//is currently holding an item
	private boolean holdsItem;
	//equipment slot areas
	private Rectangle equipped[] = new Rectangle[10];
	// id in each equipment slot
	private int equipID[] = {-1, -1, -1, -1, -1,-1, -1, -1, -1, -1};
	private int x,y;
	//inventory button area
	private Rectangle button[][] = new Rectangle[4][6];
	// current id in each inventory button
	public static Armour [][]invArm;
	private Armour arm;
	private Armour [] eqpArm = new Armour[10];
	private int buttonID[][] = { { -1, -1, -1, -1, -1, -1}, { -1, -1, -1, -1, -1, -1 },
			{ -1, -1, -1, -1, -1, -1}, { -1, -1, -1, -1, -1, -1 } };
	//which equipment id can go in which slot
	private int slot[][] = { { 0, 6, 10, 11, 14}, {4,8,17},
			{ 1, 7, 12, 13, 16}, { 4,8,17},{18},{5,9},{19,20},{19,20},{2,15},{3,21}};

	private Main main;

	public Inventory(Main main) {
		invArm = new Armour[buttonID.length][buttonID[0].length];
		invValues();
		equppiedValues();
		this.main = main;
		for(int i = 0; i < eqpArm.length; i++)
			eqpArm[i] = null;
	}
	/**
	 * if player clicks while holding an item place item accordingly
	 * either in inventory or equipment slots
	 * @param holdsItem is currently holding item
	 */
	public void click(boolean holdsItem) {
		for (int i = 0; i < button.length; i++) {
			for (int j = 0; j < button[i].length; j++) {
				// if the click is within an inventory button area
				if (MouseHandler.mx > button[i][j].x && MouseHandler.mx < (button[i][j].x + 57)
						&& MouseHandler.my > button[i][j].y && MouseHandler.my < button[i][j].y + 50) {
					// if that inventory slot is empty add the item
					if (buttonID[i][j] == -1) {
						buttonID[i][j] = heldID;
						invArm[i][j] = arm; 
						if(invArm[i][j].isPurchased()){
							invArm[i][j].setEquipped(false);
							invArm[i][j].setIncrStat(false);
							main.getPlayer().reduceStats(invArm[i][j].getHealthIncr(), invArm[i][j].getDamIncr()
									, invArm[i][j].getRegenFactor());
						}
						setHoldsItem(false);
					}
				}

			}
		}
		for (int i = 0; i < equipped.length; i++){
			// if the click is within an equipment slot area
			if (equipped[i].contains(MouseHandler.mx, MouseHandler.my)) {
				// if that equipment slot is empty and that item belongs in that slot add the item to the equipment slot
				if (equipID[i] == -1 && correctSlot(i,heldID)) {
					equipID[i] = heldID;
					eqpArm[i] = arm;
					setHoldsItem(false);
					setChanged(true);
					if(invArm[x][y] != null){
						if(invArm[x][y].isPurchased()){
							invArm[x][y].setEquipped(true);
						}
					}
				}

			}
		}
	}
	/**
	 * Checks if the item belongs in that specific slot
	 * @param slotNum is the equipment slot the item is trying to be placed in
	 * @param id is the id of the item
	 * @return true if correct slot otherwise false
	 */
	public boolean correctSlot(int slotNum, int id){
		for (int i =0; i < slot[slotNum].length; i++){
			if (slot[slotNum][i] == id)
				return true;
		}		
		return false;
	}
	/**
	 * if the player clicks without currently holding an item, set that item
	 * from the inventory or equipment to his heldID
	 */
	public void click() {
		for (int i = 0; i < button.length; i++) {
			for (int j = 0; j < button[i].length; j++) {
				// if the click is within the inventory button area
				if (button[i][j].contains(MouseHandler.mx, MouseHandler.my)) {
					// if that inventory slot is not empty 
					if (buttonID[i][j] != -1) {	
						heldID = buttonID[i][j]; //set that item to holding
						arm = invArm[i][j];
						x = i;
						y = j;
						buttonID[i][j] = -1;// set that slot to empty
						setHoldsItem(true);// set currently holding an item to true
					}
				}
			}
		}
		for (int i = 0; i < equipped.length; i++){
			// if the click is within an equipment slot
			if (equipped[i].contains(MouseHandler.mx, MouseHandler.my)) {
				//if that slot is not empty
				if (equipID[i] != -1) {
					heldID = equipID[i]; // set that item to holding
					arm = eqpArm[i];
					eqpArm[i] = null;
					equipID[i] = -1;// set that slot to empty
					setHoldsItem(true);// set currently holding an item to true
				}

			}
		}

	}
	/**
	 * Draws the inventory with all the items depending on the itemID and if they item is within the inventory
	 * @param g is the graphics component 
	 */
	public void render(Graphics g) {
		g.drawImage(Tile.inventory, 400, 50, 400, 550, null);
		for (int i = 0; i < button.length; i++) {
			for (int j = 0; j < button[i].length; j++) {
				g.setColor(new Color(255, 255, 255, 150));
				// if hovering over a inventory button highlight that button
				if (button[i][j].contains(MouseHandler.mx, MouseHandler.my))
					g.fillRect(button[i][j].x, button[i][j].y, button[i][j].width, button[i][j].height);

				// if there is an item in that inventory slot draw that item
				if (buttonID[i][j] != -1) {
					g.drawImage(Tile.items, button[i][j].x, button[i][j].y,button[i][j].x+ button[i][j].width,button[i][j].y+ button[i][j].height,buttonID[i][j]*64,0,buttonID[i][j]*64+64,64, null);				
				}
				
				if(button[i][j].contains(MouseHandler.mx, MouseHandler.my)){
					g.setColor(Color.GREEN);
					g.setFont(new Font("Impact", Font.BOLD, 11));
					if(invArm[i][j] != null){
						g.drawString("Health: " + Integer.toString(invArm[i][j].getHealthIncr()), (int) MouseHandler.mx, (int) MouseHandler.my + 10);
						g.drawString("Damage: " + Integer.toString(invArm[i][j].getDamIncr()), (int) MouseHandler.mx, (int) MouseHandler.my + 20);
						g.drawString("Regen: " + Integer.toString(invArm[i][j].getRegenFactor()), (int) MouseHandler.mx, (int) MouseHandler.my + 30);
					}
				}
			}
		}

		g.setColor(Color.BLACK);
		for (int i = 0; i < equipped.length; i++){
			// draws squares around equipment slot
			g.drawRect(equipped[i].x,equipped[i].y,equipped[i].width,equipped[i].height);
			// if that equipment slot is not empty draw the correct item
			if (equipID[i] != -1){
				g.drawImage(Tile.items, equipped[i].x, equipped[i].y,equipped[i].x+ equipped[i].width,equipped[i].y+ equipped[i].height,equipID[i]*64,0,equipID[i]*64+64,64, null);				

			}

		}

	}
	/**
	 * Gives bounds to each button in the inventory
	 */
	private void invValues() {
		for (int i = 0; i < button.length; i++) {
			for (int j = 0; j < button[i].length; j++) {
				button[i][j] = new Rectangle(416 + ((62) * j), 370 + (55 * i), 57, 50);
			}
		}
	}
	/**
	 * Gives bounds to each inventory slot
	 */
	private void equppiedValues(){
		equipped[0] = new Rectangle(582,87,42,35);
		equipped[1] = new Rectangle (533,120,30,27);
		equipped[2] = new Rectangle (575,130,54,51);
		equipped[3] = new Rectangle (635,120,35,30);
		equipped[4] = new Rectangle (580,190,50,15);
		equipped[5] = new Rectangle (635,185,20,28);
		equipped[6] = new Rectangle (517,195,42,50);
		equipped[7] = new Rectangle (659,197,42,50);
		equipped[8] = new Rectangle (585,225,42,50);
		equipped[9] = new Rectangle (577,279,50,57);
	}

	public boolean isHoldsItem() {
		return holdsItem;
	}

	public void setHoldsItem(boolean holdsItem) {
		this.holdsItem = holdsItem;
	}
	public int[][] getButtonID (){
		return buttonID;
	}
	public void setButtonID(int[][] buttonID) {
		this.buttonID = buttonID;
	}
	public int[] getEquipID() {
		return equipID;
	}
	public boolean isChanged() {
		return changed;
	}
	public void setChanged(boolean changed) {
		this.changed = changed;
	}

}