package name.panitz.game.framework.swing;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import javafx.scene.media.AudioClip;
import name.panitz.game.framework.SoundTool;

public class JavaSoundTool implements SoundTool<AudioInputStream> {
	AudioClip ac;	@Override
	public AudioInputStream loadSound(String fileName) {
		try {
			InputStream src = getClass().getClassLoader().getResourceAsStream(fileName);
			InputStream bufferedIn = new BufferedInputStream(Objects.requireNonNull(src));
			// File laden
			return AudioSystem.getAudioInputStream(bufferedIn);
		} catch (UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
		}
		System.out.println("ups?");
		return null;
	}

	@Override
	public void playSound(AudioInputStream sound) {
		Clip clip;
		try {
			clip = AudioSystem.getClip();
			clip.open(sound);
			clip.start();
		} catch (LineUnavailableException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
