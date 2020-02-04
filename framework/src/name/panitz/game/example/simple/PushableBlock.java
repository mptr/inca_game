package name.panitz.game.example.simple;

import name.panitz.game.framework.FallingImage;
import name.panitz.game.framework.Vertex;

public class PushableBlock<I> extends FallingImage<I> {
	PushableBlock(int layer, Vertex pos, Vertex motion, double objZoom) {
		super(10+layer, pos, motion, objZoom, 46);
	}
	PushableBlock(int layer, Vertex pos, Vertex motion) {
		this(layer, pos, motion,3);
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
