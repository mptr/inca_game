package name.panitz.game.app;

import name.panitz.game.framework.FallingImage;
import name.panitz.game.framework.Vertex;

public class PushableBlock<I> extends FallingImage<I> {
	PushableBlock(int layer, Vertex pos, Vertex motion, double objZoom, int startFrame) {
		super(10+layer, pos, motion, objZoom, startFrame);

	}
	PushableBlock(int layer, Vertex pos, Vertex motion, int startFrame) {
		this(layer, pos, motion,3, startFrame);
	}
	@Override
	public void animate() {
		// no animation for Blocks
	}

	@Override
	public void move() {
		getVelocity().x *= .8;
		super.move();
	}
}
