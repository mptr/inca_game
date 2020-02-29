package name.panitz.game.example.simple;


import name.panitz.game.framework.ImageObject;
import name.panitz.game.framework.Vertex;

import java.io.Serializable;


public class LevelBlock<I> extends ImageObject<I> {
	LevelBlock(int layer, Vertex pos, int tileId, double objZoom) {
		super(10+layer, pos, new Vertex(0,0), objZoom, tileId);
	}
	LevelBlock(int layer, Vertex pos, int tileId, double objZoom, boolean fixed) {
		super(10+layer, pos, new Vertex(0,0), objZoom, tileId);
		setFixed(fixed);
	}
	LevelBlock(int layer, Vertex pos, int tileId) {
		this(layer, pos,tileId,3);
	}

	@Override
	public void animate() {
		// no animation for Blocks
	}
}
