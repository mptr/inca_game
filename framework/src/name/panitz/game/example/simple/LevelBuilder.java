package name.panitz.game.example.simple;

import name.panitz.game.framework.ImageObject;
import name.panitz.game.framework.Vertex;

import java.util.List;

public class LevelBuilder<I, S> {
	private final SimpleGame<I, S> p;
	LevelBuilder(SimpleGame<I, S> p) {
		this.p = p;
	}
	public void makeLvl(int levelID) {
		border();
		background();
		if(levelID == 0) { // mainMenu
			p.items.add(new Door<>(new Vertex(16*9,SimpleGame.gameSize.y*16-16-8-26),() -> p.resetLvl(1)));
			p.items.add(new Door<>(new Vertex(16*11,SimpleGame.gameSize.y*16-16-8-26),() -> p.resetLvl(2)));
			p.items.add(new Door<>(new Vertex(16*7,SimpleGame.gameSize.y*16-16-8-26),() -> p.resetLvl(3)));
			p.items.add(new Door<>(new Vertex(16*13,SimpleGame.gameSize.y*16-16-8-26),() -> System.exit(0)));
			p.player.getPos().moveTo(new Vertex(16*3+50,(SimpleGame.gameSize.y*16-16*2)*3-50));
		} else if(levelID == 1) { // lv 1

		} else { // rand
			
		}
	}
	private void border() {
		// corners
		p.blocks.add(new LevelBlock<>(0, new Vertex(0,0),86));
		p.blocks.add(new LevelBlock<>(0, new Vertex((SimpleGame.gameSize.x-.5)*16,0),90));
		p.blocks.add(new LevelBlock<>(0, new Vertex(0, (SimpleGame.gameSize.y-.5)*16),87));
		p.blocks.add(new LevelBlock<>(0, new Vertex((SimpleGame.gameSize.x-.5)*16,(SimpleGame.gameSize.y-.5)*16),91));
		// sides
		for (int i = 1; i < SimpleGame.gameSize.x*2-1; i++) {
			p.blocks.add(new LevelBlock<>(0, new Vertex(i*8,0),88+(i%2)*4));
			p.blocks.add(new LevelBlock<>(0, new Vertex(i*8,SimpleGame.gameSize.y*16-8),85+(i%2)*4));
		}
		for (int i = 1; i < SimpleGame.gameSize.y*2-1; i++) {
			p.blocks.add(new LevelBlock<>(0, new Vertex(0,i*8),94+(i%2)));
			p.blocks.add(new LevelBlock<>(0, new Vertex(SimpleGame.gameSize.x*16-8,i*8),82+(i%2)));
		}
	}
	private void background() {
		for (int gameX = 0; gameX < SimpleGame.gameSize.x; gameX++) {
			for (int gameY = 0; gameY < SimpleGame.gameSize.y; gameY++) {
				p.background.add(new LevelBlock<>(2, new Vertex(gameX*16,gameY*16), 101));
				gameY++;
			}
			gameX++;
		}
	}
	/*public void makeLvl(int levelID, Player<I, S> p, List<ImageObject<I>> items, List<LevelBlock<I>> fg, List<LevelBlock<I>> cb, List<LevelBlock<I>> bg, List<PushableBlock<I>> pushables, Vertex size) {
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
		items.add(new Door<>(new Vertex(16*9,size.y*16-16-8-26),new Vertex(0,0)));
		pushables.add(new PushableBlock<>(0,new Vertex(16*24, 16*5), new Vertex(0,0)));
		pushables.add(new PushableBlock<>(0,new Vertex(16*22, 16*5), new Vertex(0,0)));
		for (int i = 0; i < 3*4; i++) {
			cb.add(new LevelBlock<>(0, new Vertex(16*7,size.y*16-16-8*i-8), new Vertex(0,0),71));
		}
		// player placing
		p.getPos().moveTo(new Vertex(16*3+50,(size.y*16-16*2)*3-50));
	}*/
}
