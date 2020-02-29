package name.panitz.game.example.simple;

import name.panitz.game.framework.FallingImage;
import name.panitz.game.framework.GameObject;
import name.panitz.game.framework.Vertex;

import java.util.List;

public class Skeleton<I> extends FallingImage<I> {
	int mood = 0;
	int nextMood = 0;
	private int kiTimer = 0;
	Vertex initialSize;
	public Vertex getSpawnPos() {
		return spawnPos;
	}
	Vertex spawnPos;
	Skeleton(Vertex pos, Vertex motion, double objZoom) {
		super(20, pos, motion, objZoom);
		initialSize = new Vertex(getWidth(), getHeight());
		spawnPos = pos.mult(1); // copy
	}
	Skeleton(Vertex pos, Vertex motion) {
		this(pos, motion, 3);
	}
	public int getKiTimer() {return kiTimer;}
	public void setKiTimer(int kt) {kiTimer = kt;}
	@Override
	public void animate() {
		if(getVelocity().x > 0) {
			setFacing(false);
		} else if(getVelocity().x < 0) {
			setFacing(true);
		}
		if(getVelocity().x > 1.2) {
			setAnimationSpeed(3);
		} else {
			setAnimationSpeed(4);
		}
		super.animate();
		if(getCurrentAnimationFrame() == 0 && mood != nextMood) {
			mood = nextMood;
			setGameObjectId(20 + mood);
		}
	}
	public void initWalking(double s) {
		if(Math.abs(s) > .1) {
			setMood(1);
		} else {
			setMood(0);
		}
		getVelocity().moveTo(new Vertex(s, getVelocity().y));
	}
	@Override
	public void move() {
		if(mood == 5 || mood == 3) { // dont walk when dead, attacking or idle
			setVelocity(getVelocity().mult(new Vertex(0,1)));
//			if(mood != 0)
//				kiTimer = 0;
		} /*else if(Math.abs(getVelocity().x) > 0.1) {
			setMood(1);
		} else {
			setMood(0);
		}*/
		if(Math.abs(getVelocity().x) > .1 && getMood() == 0) { // anti moonwalk
			setMood(1);
		}
		super.move();
		kiTimer++;
	}

	@Override
	public Vertex getObjectCenter() {
		return new Vertex(getPos().x + initialSize.x/2, getPos().y + initialSize.y / 2);
	}
	public int getMood(){return mood;}
	public void setMood(int x) {
		if(mood == 5 || nextMood == 5) return; // dead Skeletons don't change mood
		nextMood = x;
		if((mood != nextMood && mood != 3) || nextMood == 5) { // only on change, don't interrupt attack & death, die instant
			setCurrentAnimationFrame(0);
		}
	}
}
