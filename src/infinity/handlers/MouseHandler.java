/**
 * Mouse input
 * MouseHandler.java
 * @author Mohammed, Nim, Umar
 */
package infinity.handlers;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import infinity.drops.Inventory;
import infinity.main.Main;
import infinity.main.Menu;

public class MouseHandler implements MouseListener, MouseMotionListener {

	//Mouse positions 
	public static double mx,my;

	private Main main;
	
	public MouseHandler(Main main){
		this.main = main;
	}
	
	/**
	 * if mouse has been clicked and released 
	 */
	public void mouseClicked(MouseEvent m) {
	}

	
	/**
	 * When the mouse enters an area
	 */
	public void mouseEntered(MouseEvent m) {
	}

	
	/**
	 * when mouse leaves an area 
	 */
	public void mouseExited(MouseEvent m) {
	}

	
	/**
	 * detects mouse presses 
	 */
	public void mousePressed(MouseEvent m) {
		if (main.isOpenShop()){
			main.getShop().click();
		}
		SoundFX click = main.getLevel().getClip("click");
		if (main.isiK()&& !main.getInventory().isHoldsItem())
			main.getInventory().click();
		else if (main.isiK()&& main.getInventory().isHoldsItem()){
			main.getInventory().click(main.getInventory().isHoldsItem());
		}
		if(Menu.respawn.contains(mx, my)){
			click.loop(1);
			 main.setRespawn(true);
		}if(Menu.start.contains(mx,my) && main.isMainMenu()){
			main.getBgMusic().stopClip();
			click.loop(1);
			main.setMainMenu(false);
			main.setStartGame(true);
		}
		else if(!main.isiK())
			main.setSpace(true);
		if(Menu.instructionsBack.contains(mx,my) && main.isInstructions() && !main.isMainMenu()){
			click.loop(1);
			main.setMainMenu(true);
			main.setInstructions(false);
		}
		else if(Menu.instructions.contains(mx,my) && main.isMainMenu()){
			click.loop(1);
			main.setMainMenu(false);
			main.setInstructions(true);
		}
		if(Menu.quit.contains(mx,my) && main.isMainMenu()){
			click.loop(1);
			main.setQuit(true);
		}
		if (main.getCloseShop().contains(mx,my)&& main.isOpenShop()){
			main.getPlayer().setTalkNPC(false);
			main.setOpenShop(false);
			main.setPaused(false);
		}
	}

	
	/**
	 * when mouse button is not being pressed 
	 */
	public void mouseReleased(MouseEvent m) {
		main.setSpace(false);
	}

	
	/**
	 * if mouse is being pressed and dragging 
	 */
	public void mouseDragged(MouseEvent m) {
		
	}

	
	/**
	 * Detects mouse movement
	 */
	public void mouseMoved(MouseEvent m) {
		 mx = m.getX();
		 my = m.getY();
	}
	

}
