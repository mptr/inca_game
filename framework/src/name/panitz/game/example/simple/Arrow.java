package name.panitz.game.example.simple;

import name.panitz.game.framework.GameObject;
import name.panitz.game.framework.GraphicsTool;
import name.panitz.game.framework.ImageObject;
import name.panitz.game.framework.Vertex;

public class Arrow<I> extends ImageObject<I> {
	public int getRemoveCounter() {
		return removeCounter;
	}
	public GameObject<I> getStuckIn() {
		return stuckIn;
	}
	public void setStuckIn(GameObject<I> stuckIn) {
		this.stuckIn = stuckIn;
	}
	GameObject<I> stuckIn = null;
	int removeCounter = 0;
	public Arrow(Vertex pos, Vertex motion, boolean f) {
		super(3, pos, motion, 3);
		setFacing(f);
	}
	@Override
	public void move() {
		if(getVelocity().dist() == 0 || getStuckIn() != null)
			removeCounter++;
		if(getStuckIn() != null) {
			setVelocity(getStuckIn().getVelocity());
		}
		super.move();
	}
}
