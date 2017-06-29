/**
 * Keyboard input
 * KeyHandler.java
 * @author Mohammed, Nim, Umar (We all used it)
 */
package infinity.handlers;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import infinity.entites.Player;
import infinity.main.Main;

public class KeyHandler implements KeyListener {
	private Main main;
	public KeyHandler(Main main){
		this.main = main;
	}

	/**
	 * If key is being pressed 
	 */
	public void keyPressed(KeyEvent e) {

		int key = e.getKeyCode();

		if(!main.isPaused()){
			if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) {
				main.getPlayer().setFacingUp(true);
				main.getPlayer().setFacingDown(false);
				main.getPlayer().setFacingRight(false);
				main.getPlayer().setFacingLeft(false);
				main.setwK(true);
			}
			if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) {
				main.getPlayer().setFacingUp(false);
				main.getPlayer().setFacingDown(false);
				main.getPlayer().setFacingRight(false);
				main.getPlayer().setFacingLeft(true);
				main.setaK(true);
			}
			if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) {
				main.getPlayer().setFacingUp(false);
				main.getPlayer().setFacingDown(true);
				main.getPlayer().setFacingRight(false);
				main.getPlayer().setFacingLeft(false);
				main.setsK(true);
			}
			if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) {
				main.getPlayer().setFacingUp(false);
				main.getPlayer().setFacingDown(false);
				main.getPlayer().setFacingRight(true);
				main.getPlayer().setFacingLeft(false);
				main.setdK(true);
			}
			if(key == KeyEvent.VK_E){
				main.seteK(true);
			}
			if(key == KeyEvent.VK_SPACE){
				main.setSpace(true);
			}
			if(key == KeyEvent.VK_F){
				main.setfK(true);
			}
			if(key == KeyEvent.VK_G){
				main.setgK(true);
			}
			if (key == KeyEvent.VK_SHIFT){
				main.setShift(true);
			}
			if (key == KeyEvent.VK_SHIFT){
				main.setShift(true);
			}
			if(key == KeyEvent.VK_1){
				main.getPlayer().setFireSpell(true);
			}
			if(key == KeyEvent.VK_2){
				main.getPlayer().setEarthSpell(true);
			}
			if(key == KeyEvent.VK_3){
				main.getPlayer().setLightingSpell(true);
			}
			if(key == KeyEvent.VK_4){
				main.getPlayer().setWindSpell(true);
			}
			if(key == KeyEvent.VK_5){
				main.getPlayer().setWaterSpell(true);
			}
		}
		if(key == KeyEvent.VK_E && main.isOpenShop()){
			main.getPlayer().setTalkNPC(false);
			main.setOpenShop(false);
			main.setPaused(false);
		}
		if (key == KeyEvent.VK_I && !main.isiK()){
			main.setiK(true);
			main.setPaused(true);
		}
		else if (key == KeyEvent.VK_I && main.isiK()){
			main.setiK(false);
			main.setPaused(false);
		}

	}

	/**
	 * When key is released 
	 */
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) {
			main.setwK(false);
		}
		if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) {
			main.setaK(false);
		}
		if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) {
			main.setsK(false);
		}
		if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) {
			main.setdK(false);
		}
		if(key == KeyEvent.VK_E){
			main.seteK(false);
		}
		if(key == KeyEvent.VK_SPACE){
			main.setSpace(false);
		}
		if(key == KeyEvent.VK_F){
			main.setfK(false);
		}
		if(key == KeyEvent.VK_G){
			main.setgK(true);
		}
		if (key == KeyEvent.VK_SHIFT){
			main.setShift(false);
		}
		if(key == KeyEvent.VK_1){
			main.getPlayer().setFireSpell(false);
		}
		if(key == KeyEvent.VK_2){
			main.getPlayer().setEarthSpell(false);
		}
		if(key == KeyEvent.VK_3){
			main.getPlayer().setLightingSpell(false);
		}
		if(key == KeyEvent.VK_4){
			main.getPlayer().setWindSpell(false);
		}
		if(key == KeyEvent.VK_5){
			main.getPlayer().setWaterSpell(false);
		}
	}

	/**
	 * When key is pressed but excludes keys such as shift, ctrl, alt
	 */
	public void keyTyped(KeyEvent e) {

	}

}
