/**
 * Enemy spawner for wave based functionality
 * EnemySpawner.java
 * @author Nim Wijetunga
 */
package infinity.level;

import java.awt.Color;

import java.awt.Font;
import java.awt.Graphics;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import infinity.entites.Boss;
import infinity.entites.Enemy;
import infinity.entites.Entity;
import infinity.entites.EntityEnum;
import infinity.entites.MagicBoss;
import infinity.entites.SkeletonBoss;
import infinity.entites.SpecialBoss;
import infinity.handlers.SoundFX;
import infinity.main.Main;
import infinity.weapons.WeaponEnum;

public class EnemySpawner {

	//Wave properties
	private int wave, enemyCount, totalEnemies;
	private double scaleFactor;
	private boolean wait, alreadySpawned;
	private long waveDelay; 
	private Main main;
	private int n = 1, x = 5;
	private SoundFX bossMusic, waveMusic;

	/**
	 * @param main
	 */
	public EnemySpawner(Main main){
		scaleFactor = 1;
		wave = 0;
		this.main = main;
	}

	/**newWave renders a new wave of enemies
	 * @param g is the graphics object
	 */
	public void newWave(Graphics g){
		g.setFont(new Font("Impact", Font.BOLD, 22));
		update(g);
		//new wave sound fx
		SoundFX nextWave = main.getLevel().getClip("new_wave");
		bossMusic = main.getLevel().getClip("bossbg");
		waveMusic = main.getLevel().getClip("bgmusic");
		//Delay inbetween 2 waves
		if(wait){
			//Stop bg music
			if(bossMusic.isPlaying() || waveMusic.isPlaying()){
				bossMusic.stopClip();
				waveMusic.stopClip();
			}
			g.setColor(Color.RED);
			g.fillRect(main.getSize().width / 2 + 32, main.getSize().height / 2 + 32, 128, 32);
			g.setColor(Color.ORANGE);
			g.drawString("Wave " + Integer.toString(wave), main.getSize().width / 2 + 55, main.getSize().height / 2 + 55);
			nextWave.loop(1);
			//spawn a new wave of enemies
		}else if(!wait && !alreadySpawned){
			nextWave.stopClip();
			alreadySpawned = true;
			//every even wave a boss spawns
			if(wave % 2 == 0){
				if(waveMusic.isPlaying())
					waveMusic.stopClip();
				bossMusic.loop();
				Boss b = getBoss();
				main.getEntities().add(b);
				main.getEnemies().add(b);
			}else{
				if(bossMusic.isPlaying())
					bossMusic.stopClip();
				waveMusic.loop();
			}
			spawnEnemies();				
		}else if(!wait){
			g.setColor(Color.BLACK);
			g.drawString("Wave " + Integer.toString(wave), (int) (main.getSize().width / 1.1), 50);	
			g.drawString("Enemies Remaining " + Integer.toString(enemyCount) + "/" + Integer.toString(totalEnemies), (int) (main.getSize().width / 2.2), 40);
		}
	}

	public Boss getBoss(){
		WeaponEnum type = null;
		Random rand = new Random();
		int  randInt = rand.nextInt(6) + 1;
		switch(randInt){
		case 2:
			type = WeaponEnum.WATER;
			break;
		case 3:
			type = WeaponEnum.WIND;
			break;
		case 4:
			type = WeaponEnum.LIGHTNING;
			break;
		case 5:
			type = WeaponEnum.FIRE;
			break;
		case 6:
			return new SkeletonBoss(main, 49 * 36, 21 * 36, 32 * 5, 32 * 5, 200 * scaleFactor, 8 * scaleFactor);
		}
		return new MagicBoss(main, 49 * 36, 21 * 36, 32 * 5, 32 * 5, 200 * scaleFactor, 8 * scaleFactor, type);
	}

	/**reset player stats if the boss has been killed
	 * 
	 */
	public void resetStats(){
		for(Entity i : main.getEntities()){
			if(i.getType() == EntityEnum.BOSS){
				Boss b  = (Boss) i;
				b.bossKilled();
			}
		}
	}

	/**spawn a new wave of enemies in random locations
	 * 
	 */
	public void spawnEnemies(){
		increaseScale();
		//x and y positions of enemies
		int x = 0, y = 0;
		Random rand = new Random();
		//loop through the number of enemies that should spawn per wave
		for(int i = 0; i < numEnemies(); i++){
			//do while to keep running until the enemy spawns on a non-solid tile
			do{
				x = ThreadLocalRandom.current().nextInt(12, 51);
				y = ThreadLocalRandom.current().nextInt(12, 50);
			}while(main.getLevel().tileIsSolid(x, y) || main.getLevel().tileIsSolid(x + 1, y) || main.getLevel().tileIsSolid(x - 1, y)
					|| main.getLevel().tileIsSolid(x, y + 1) || main.getLevel().tileIsSolid(x, y - 1));
			//add enemy to array lists
			Enemy e = new Enemy(EntityEnum.ENEMY, main, x * Tile.size, y * Tile.size, 64, 64, 50 * scaleFactor, (rand.nextInt(4) + 1) * scaleFactor);
			main.getEnemies().add(e);
			main.getEntities().add(e);
		}
	}

	/**delay between two waves
	 * 
	 */
	public void waveDelay(){
		if(wait)
			return;
		if(allDead()){//if all the enemies have been killed a new wave can occur
			alreadySpawned = false;
			wave++;
			wait = true;
			waveDelay = System.nanoTime();
		}
	}

	/**Wave Delay (Show's time until the next wave)
	 * @param g is the Graphics object
	 */
	public void update(Graphics g){
		waveDelay();
		if(wait){
			long elapsed = (System.nanoTime() - waveDelay) / 1000000;
			if(elapsed > (1000 * n)){
				n++;
				x--;
			}
			//Draws time until next wave
			g.setColor(Color.ORANGE);
			g.setFont(new Font("Impact", Font.BOLD, 22));
			g.drawString(Integer.toString(x), main.getSize().width / 2 + 85, main.getSize().height / 2 + 85);
			if(elapsed > 5000){
				wait = false;
				n = 1;
				x = 6;
			}
		}
	}

	/**checks to see if all enemies in the current wave has been killed
	 * @return true if all enemies are dead false otherwise
	 */
	public boolean allDead(){
		for(Enemy i : main.getEnemies()){
			if(!i.isDead())
				return false;
		}
		return true;
	}

	/**get the number of enemies that should spawn in the wave
	 * @return the number of enemies in the next wave
	 */
	public int numEnemies(){
		//always the wave number + 2 enemies spawn
		if(wave + 2 < 20){
			if(wave % 2 == 0){
				enemyCount = wave + 3;
				totalEnemies = wave + 3;
			}else{
				totalEnemies = wave + 2;
				enemyCount = wave + 2;
			}
			return wave + 2;
		}
		return 20;		
	}

	/**increase damage and health of entities as waves increase
	 * 
	 */
	public void increaseScale(){
		//increase scale factor
		scaleFactor += 0.3;
		//Cap the scale at 4
		if(scaleFactor > 4)
			scaleFactor = 4;
	}

	public int getEnemyCount() {
		return enemyCount;
	}

	public void setEnemyCount(int enemyCount) {
		this.enemyCount = enemyCount;
	}
}
