package name.panitz.game.app;

import name.panitz.game.framework.ImageObject;
import name.panitz.game.framework.Vertex;

public class Door<I> extends ImageObject<I> {
	private boolean open = false;

	public int getLvlToEnter() {
		return lvlToEnter;
	}

	private int lvlToEnter;
	public Door(Vertex pos, int lvlToEnter) {
		super(4, pos, new Vertex(0,0));
		animationSpeed = 3;
		this.lvlToEnter = lvlToEnter;
	}
	public int enter() {
		setOpen(false);
		return lvlToEnter;
	}
	public void setOpen(boolean o) {
		open = o;
	}
	public boolean isOpen() {
		return open;
	}
	@Override
	public void animate() {
		animationFrameSkip++;
		if(animationFrameSkip > animationSpeed && animationSpeed != -1) {
			if(open && currentAnimationFrame < 3) {
				currentAnimationFrame++;
			} else if(!open && currentAnimationFrame > 0) {
				currentAnimationFrame--;
			}
			cutout = animationFrames.get(currentAnimationFrame);
			animationFrameSkip = 0;
		}
	}
}
