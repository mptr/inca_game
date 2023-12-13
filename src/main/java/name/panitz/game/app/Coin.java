package name.panitz.game.app;

import name.panitz.game.framework.FallingImage;
import name.panitz.game.framework.Vertex;


public class Coin<I> extends FallingImage<I> {
	Coin(Vertex pos, Vertex motion, double objZoom) {
		super(0, pos.mult(3*(1/objZoom)), motion, objZoom);
		setCurrentAnimationFrame((int) (Math.random() * 8));
	}
	Coin(Vertex pos, Vertex motion) {
		this(pos,motion,.4);
	}

	@Override
	public void move() {
		setVelocity(getVelocity().mult(new Vertex(.9, 1)));
		super.move();
	}
}
