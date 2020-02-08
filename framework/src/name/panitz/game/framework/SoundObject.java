package name.panitz.game.framework;

public class SoundObject<S> {
	String fileName;
	S sound;

	public SoundObject(String fileName) {
		this.fileName = fileName;
	}

	public void playSound(SoundTool<S> st) {
		try {
			if (sound == null) sound = st.loadSound(fileName);
			System.out.println("Playing: " + fileName);
			st.playSound(sound);
			sound = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
