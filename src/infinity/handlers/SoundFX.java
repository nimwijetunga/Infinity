/**
 * Sound for the game
 * SoundFX.java
 * @author Mohammed, Nim, Umar
 */

package infinity.handlers;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundFX{

	//Sound name 
	protected Clip name;
	//If sound is being played in the background 
	private boolean background, isPlaying;
	//File path for sounds 
	String filePath;

	/**
	 * @param filePath locations of the sound effect 
	 * @param background if it being played in the background
	 */
	public SoundFX(String filePath, boolean background){
		this.clipName = filePath;
		this.filePath = "res/sound/" + filePath + ".wav";
		this.background = background;
		initializeSounds();
	}

	/**
	 * initializes the sound effect 
	 */
	public void initializeSounds(){
		name = getAudio(filePath);
	}

	/**
	 * Starts the sound effect
	 * @param clip name of the sound effect 
	 */
	public void start(){
		name.start();
	}

	/**
	 * @param times number of times it being looped
	 */
	public void loop(int times){
		isPlaying = true;
		name.loop(times);
	}
	
	/**
	 *loop the clip infinitely 
	 */
	public void loop(){
		isPlaying = true;
		name.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	/**
	 * Stops sound effects 
	 */
	public void stopClip(){
		isPlaying = false;
		name.stop();
		name.flush();
		name.setFramePosition(0);
	}

	/**
	 * Gets sounds effects 
	 * @param fileName location of the sound effect
	 * @return clip with sound effect
	 */
	public Clip getAudio(String fileName){
		Clip clip = null;
		AudioInputStream audioIn;
		try {
			audioIn = AudioSystem.getAudioInputStream(new File(fileName));
			clip = AudioSystem.getClip();
			clip.open(audioIn);
		}catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {e.printStackTrace();}
		return clip;
	}

	public boolean isBackground() {
		return background;
	}

	public void setBackground(boolean background) {
		this.background = background;
	}

	public Clip getName() {
		return name;
	}

	public void setName(Clip name) {
		this.name = name;
	}

	protected String clipName;

	public String getClipName() {
		return clipName;
	}

	public void setClipName(String clipName) {
		this.clipName = clipName;
	}

	public Clip getClip() {
		return name;
	}

	public boolean isPlaying() {
		return isPlaying;
	}

	public void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}

}
