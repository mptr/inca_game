package name.panitz.game.example.simple;

import name.panitz.game.framework.GameObject;
import name.panitz.game.framework.Vertex;

import java.util.List;

public class LevelBuilder<I> {
	public void makeLvl(Player<I> p, List<GameObject<I>> items, List<LevelBlock<I>> fg, List<LevelBlock<I>> cb, List<LevelBlock<I>> bg, Vertex size) {
		// background
		for (int gameX = 0; gameX < size.x; gameX++) {
			for (int gameY = 0; gameY < size.y; gameY++) {
				bg.add(new LevelBlock<>(2, new Vertex(gameX*16,gameY*16), new Vertex(0,0), (int) (Math.random() * 9)));
				gameY++;
			}
			gameX++;
		}
		// foreground borders bottom & top
		for (int gameX = 0; gameX < size.x; gameX++) {
			fg.add(new LevelBlock<>(0, new Vertex(gameX*16, 0),new Vertex(0,0), 11));
			fg.add(new LevelBlock<>(0, new Vertex(gameX*16, size.y*16-16),new Vertex(0,0), 11));
			gameX++;
		}
		// corners
		fg.add(new LevelBlock<>(0, new Vertex(0,16),new Vertex(0,0),55));
		fg.add(new LevelBlock<>(0, new Vertex(size.x*16-16,16),new Vertex(0,0),55));
		fg.add(new LevelBlock<>(0, new Vertex(0,size.y*16-16*2),new Vertex(0,0),58));
		fg.add(new LevelBlock<>(0, new Vertex(size.x*16-16,size.y*16-16*2),new Vertex(0,0),58));
		// borders l&r
		for (int gameY = 2; gameY < size.y-2; gameY++) {
			fg.add(new LevelBlock<>(0, new Vertex(0,gameY*16),new Vertex(0,0),57));
			fg.add(new LevelBlock<>(0, new Vertex(size.x*16-16,gameY*16),new Vertex(0,0),57));
		}
		// blocks
		fg.add(new LevelBlock<>(0, new Vertex(16*8,size.y*16-16-32-32-32),new Vertex(0,0),0));
//		fg.add(new LevelBlock<>(0, new Vertex(size.x*10-16-16,size.y*16-16*2),new Vertex(0,0),17));
		//fg.add(new LevelBlock<>(0, new Vertex(size.x*10-16-16,size.y*16-16*2),new Vertex(0,0),37));
		//fg.add(new LevelBlock<>(0, new Vertex(size.x*10-16-16+16,size.y*16-16*2),new Vertex(0,0),38));
//		fg.add(new LevelBlock<>(0, new Vertex(16*8,size.y*16-16-32-32),new Vertex(0,0),0));
//		fg.add(new LevelBlock<>(0, new Vertex(16*8,size.y*16-16-32),new Vertex(0,0),0));
//		items.add(new Skeleton<>(new Vertex(300,30),new Vertex(0,0)));
		items.add(new Coin<>(new Vertex(16*8.5,30), new Vertex(0,0)));
		items.add(new PushableBlock<>(0,new Vertex(16*22, 16*5), new Vertex(0,0)));
		items.add(new PushableBlock<>(0,new Vertex(16*24, 16*5), new Vertex(0,0)));
		for (int i = 0; i < 3*4; i++) {
			cb.add(new LevelBlock<>(0, new Vertex(16*7,size.y*16-16-8*i-8), new Vertex(0,0),71));
			cb.add(new LevelBlock<>(0, new Vertex(16*7,size.y*16-16-8*i-8), new Vertex(0,0),71));
		}
		// player placing
		p.getPos().moveTo(new Vertex(16*3+50,(size.y*16-16*2)*3-50));
	}
}
