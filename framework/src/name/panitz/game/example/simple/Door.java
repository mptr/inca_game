package name.panitz.game.example.simple;

import name.panitz.game.framework.ImageObject;
import name.panitz.game.framework.Vertex;

public class Door<I> extends ImageObject<I> {
	private boolean open = false;
	private Runnable action;
	public Door(Vertex pos, Runnable action) {
		super(4, pos.mult(3/4.0), new Vertex(0,0), 4);
		animationSpeed = 3;
		this.action = action;
	}
	public void enter() {
		setOpen(false);
		action.run();
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
