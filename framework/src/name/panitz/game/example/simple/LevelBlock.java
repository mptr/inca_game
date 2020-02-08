package name.panitz.game.example.simple;


import name.panitz.game.framework.ImageObject;
import name.panitz.game.framework.Vertex;


public class LevelBlock<I> extends ImageObject<I> {
	LevelBlock(int layer, Vertex pos, Vertex motion, int tileId, double objZoom) {
		super(10+layer, pos, motion, objZoom, tileId);
	}
	LevelBlock(int layer, Vertex pos, Vertex motion, int tileId) {
		this(layer, pos, motion,tileId,3);
	}
	@Override
	public void animate() {
		// no animation for Blocks
	}
}
