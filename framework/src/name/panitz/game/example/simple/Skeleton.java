package name.panitz.game.example.simple;

import name.panitz.game.framework.FallingImage;
import name.panitz.game.framework.Vertex;

import java.util.List;

public class Skeleton<I> extends FallingImage<I> {
	int mood = 0;
	int nextMood = 0;
	private int kiTimer = 0;
	Vertex initialSize;
	Skeleton(Vertex pos, Vertex motion, double objZoom) {
		super(20, pos, motion, objZoom);
		initialSize = new Vertex(getWidth(), getHeight());
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
	public void initWalking(double speed, List<LevelBlock<I>> bs) {
		if(speed > 0) {
			for (LevelBlock<I> b:bs) {
				if(hitsLeftSideOf(b)) {
					speed = 0;
					break;
				}
			}
		} else if(speed < 0) {
			for (LevelBlock<I> b:bs) {
				if(hitsRightSideOf(b)) {
					speed = 0;
					break;
				}
			}

		}
		if(speed == 0) {
			setMood(0);
		} else {
			setMood(1);
		}
		getVelocity().moveTo(new Vertex(speed, getVelocity().y));
	}
	@Override
	public void move() {
		if(mood == 5 || mood == 3 || mood == 0) { // dont walk when dead, attacking or idle
			setVelocity(getVelocity().mult(new Vertex(0,1)));
			if(mood != 0)
				kiTimer = 0;
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
