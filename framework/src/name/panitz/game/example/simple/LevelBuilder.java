package name.panitz.game.example.simple;

import name.panitz.game.framework.ImageObject;
import name.panitz.game.framework.TextObject;
import name.panitz.game.framework.Vertex;

import java.awt.*;
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
			for (int i = 0; i < 3; i++) {
				p.background.add(new LevelBlock<>(2, new Vertex(1.5*16, 12.5*16 + 16 * (4-i)), 58-i));
				p.background.add(new LevelBlock<>(2, new Vertex(19*16, 12.5*16 + 16 * (4-i)), 58-i));
			}
			for (int j = 0; j < 4; j++) {
				p.background.add(new LevelBlock<>(2, new Vertex(13*16, 12.5*16 + 16 * (4-j)), 58-j));
				p.blocks.add(new LevelBlock<>(0, new Vertex(7*16, 12.5*16 + 16 * (4-j)), 58-j));
			}
			int[] randLadders = {72,72,72,73,73,78,78,74,79,75};
			for (int i = 0; i < 11; i++) {
				p.climbables.add(new LevelBlock<>(0, new Vertex(88,272 - i*8),randLadders[(int) (Math.random() * randLadders.length)]));
			}
			p.blocks.add(new LevelBlock<>(0, new Vertex(112, 264), 58));
			p.blocks.add(new LevelBlock<>(0, new Vertex(112, 248), 57));
			p.blocks.add(new LevelBlock<>(0, new Vertex(112, 232), 56));
			p.blocks.add(new LevelBlock<>(0, new Vertex(112, 216), 55));
			p.blocks.add(new LevelBlock<>(0, new Vertex(152, 200), 11));
			p.blocks.add(new LevelBlock<>(0, new Vertex(184, 200), 26));
			p.blocks.add(new LevelBlock<>(0, new Vertex(136, 200), 26));
			p.blocks.add(new LevelBlock<>(0, new Vertex(200, 200), 14));
			p.blocks.add(new LevelBlock<>(0, new Vertex(104, 200), 14));
			p.climbables.add(new LevelBlock<>(0, new Vertex(175, 216), 99));
			p.climbables.add(new LevelBlock<>(0, new Vertex(153, 216), 99));
			p.blocks.add(new LevelBlock<>(0, new Vertex(152, 224), 20));
			p.blocks.add(new LevelBlock<>(0, new Vertex(424, 264), 58));
			p.blocks.add(new LevelBlock<>(0, new Vertex(424, 248), 57));
			p.blocks.add(new LevelBlock<>(0, new Vertex(424, 232), 56));
			p.blocks.add(new LevelBlock<>(0, new Vertex(424, 216), 55));
			p.blocks.add(new LevelBlock<>(0, new Vertex(416, 200), 23));
			p.background.add(new LevelBlock<>(2, new Vertex(424, 184), 58));
			p.background.add(new LevelBlock<>(2, new Vertex(424, 168), 57));
			p.background.add(new LevelBlock<>(2, new Vertex(424, 152), 57));
			p.background.add(new LevelBlock<>(2, new Vertex(424, 136), 57));
			p.background.add(new LevelBlock<>(2, new Vertex(424, 120), 56));
			p.background.add(new LevelBlock<>(2, new Vertex(424, 104), 55));
			p.blocks.add(new LevelBlock<>(0, new Vertex(408, 88), 21));
			p.blocks.add(new LevelBlock<>(0, new Vertex(456, 88), 17));
			p.blocks.add(new LevelBlock<>(0, new Vertex(504, 88), 16));
			p.blocks.add(new LevelBlock<>(0, new Vertex(440, 88), 32));
			p.blocks.add(new LevelBlock<>(0, new Vertex(488, 88), 36));
			p.blocks.add(new LevelBlock<>(0, new Vertex(536, 88), 35));
			p.blocks.add(new LevelBlock<>(0, new Vertex(552, 88), 6));
			p.blocks.add(new LevelBlock<>(0, new Vertex(520, 248), 1));
			p.background.add(new LevelBlock<>(2, new Vertex(568, 120), 27));
			p.climbables.add(new LevelBlock<>(0, new Vertex(480, 104), 99));
			p.climbables.add(new LevelBlock<>(0, new Vertex(480, 120), 97));
			p.climbables.add(new LevelBlock<>(0, new Vertex(480, 136), 97));
			p.climbables.add(new LevelBlock<>(0, new Vertex(480, 152), 97));
			p.climbables.add(new LevelBlock<>(0, new Vertex(480, 168), 98));
			p.climbables.add(new LevelBlock<>(0, new Vertex(416.0,216.0), 99));
			p.pushables.add(new PushableBlock<>(1, new Vertex(488.0,264.0), new Vertex(0, 0), 49));
			p.items.add(new Door<>(new Vertex(9.4*16, 245.5),null));
			p.otherObjs.add(new TextObject<>(new Vertex(475, 704),"Level 1","Times New Roman",20, new Color(0xE3A569), false));
			p.player.getPos().moveTo(new Vertex(16*3+50,(SimpleGame.gameSize.y*16-16*2)*3-50));
		} else if(levelID == 1) { // lv 1
/*  LAST EXPORT
p.blocks.add(new LevelBlock<>(0, new Vertex(112.0,264.0), 58));
p.blocks.add(new LevelBlock<>(0, new Vertex(112.0,248.0), 57));
p.blocks.add(new LevelBlock<>(0, new Vertex(112.0,232.0), 56));
p.blocks.add(new LevelBlock<>(0, new Vertex(112.0,216.0), 55));
p.blocks.add(new LevelBlock<>(0, new Vertex(112.0,264.0), 58));
p.blocks.add(new LevelBlock<>(0, new Vertex(112.0,248.0), 57));
p.blocks.add(new LevelBlock<>(0, new Vertex(112.0,232.0), 56));
p.blocks.add(new LevelBlock<>(0, new Vertex(112.0,216.0), 55));
p.blocks.add(new LevelBlock<>(0, new Vertex(152.0,200.0), 11));
p.blocks.add(new LevelBlock<>(0, new Vertex(184.0,200.0), 26));
p.blocks.add(new LevelBlock<>(0, new Vertex(136.0,200.0), 26));
p.blocks.add(new LevelBlock<>(0, new Vertex(200.0,200.0), 14));
p.blocks.add(new LevelBlock<>(0, new Vertex(104.0,200.0), 14));
p.blocks.add(new LevelBlock<>(0, new Vertex(152.0,224.0), 20));
p.blocks.add(new LevelBlock<>(0, new Vertex(424.0,264.0), 58));
p.blocks.add(new LevelBlock<>(0, new Vertex(424.0,248.0), 57));
p.blocks.add(new LevelBlock<>(0, new Vertex(424.0,232.0), 56));
p.blocks.add(new LevelBlock<>(0, new Vertex(424.0,216.0), 55));
p.blocks.add(new LevelBlock<>(0, new Vertex(416.0,200.0), 23));
p.blocks.add(new LevelBlock<>(0, new Vertex(408.0,88.0), 21));
p.blocks.add(new LevelBlock<>(0, new Vertex(456.0,88.0), 17));
p.blocks.add(new LevelBlock<>(0, new Vertex(504.0,88.0), 16));
p.blocks.add(new LevelBlock<>(0, new Vertex(440.0,88.0), 32));
p.blocks.add(new LevelBlock<>(0, new Vertex(488.0,88.0), 36));
p.blocks.add(new LevelBlock<>(0, new Vertex(536.0,88.0), 35));
p.blocks.add(new LevelBlock<>(0, new Vertex(552.0,88.0), 6));
p.blocks.add(new LevelBlock<>(0, new Vertex(520.0,248.0), 1));
p.climbables.add(new LevelBlock<>(0, new Vertex(464.0,104.0), 99));
p.blocks.add(new LevelBlock<>(0, new Vertex(464.0,120.0), 97));
p.blocks.add(new LevelBlock<>(0, new Vertex(464.0,136.0), 97));
p.climbables.add(new LevelBlock<>(0, new Vertex(464.0,152.0), 98));
p.climbables.add(new LevelBlock<>(0, new Vertex(448.0,104.0), 99));
p.blocks.add(new LevelBlock<>(0, new Vertex(448.0,120.0), 97));
p.climbables.add(new LevelBlock<>(0, new Vertex(448.0,136.0), 98));
p.blocks.add(new LevelBlock<>(0, new Vertex(360.0,136.0), 23));
p.blocks.add(new LevelBlock<>(0, new Vertex(328.0,136.0), 24));
p.blocks.add(new LevelBlock<>(0, new Vertex(296.0,136.0), 23));
p.background.add(new LevelBlock<>(2, new Vertex(304.0,8.0), 99));
p.background.add(new LevelBlock<>(2, new Vertex(304.0,24.0), 97));
p.background.add(new LevelBlock<>(2, new Vertex(304.0,40.0), 97));
p.background.add(new LevelBlock<>(2, new Vertex(304.0,56.0), 97));
p.background.add(new LevelBlock<>(2, new Vertex(304.0,72.0), 97));
p.background.add(new LevelBlock<>(2, new Vertex(304.0,88.0), 97));
p.background.add(new LevelBlock<>(2, new Vertex(304.0,104.0), 97));
p.background.add(new LevelBlock<>(2, new Vertex(304.0,120.0), 97));
p.background.add(new LevelBlock<>(2, new Vertex(376.0,120.0), 97));
p.background.add(new LevelBlock<>(2, new Vertex(376.0,104.0), 97));
p.background.add(new LevelBlock<>(2, new Vertex(376.0,88.0), 97));
p.background.add(new LevelBlock<>(2, new Vertex(376.0,72.0), 97));
p.background.add(new LevelBlock<>(2, new Vertex(376.0,56.0), 97));
p.background.add(new LevelBlock<>(2, new Vertex(376.0,40.0), 97));
p.background.add(new LevelBlock<>(2, new Vertex(376.0,8.0), 99));
p.background.add(new LevelBlock<>(2, new Vertex(376.0,24.0), 97));
-1

Process finished with exit code 0

* */
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
}
