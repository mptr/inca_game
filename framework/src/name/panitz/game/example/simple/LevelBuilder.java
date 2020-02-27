package name.panitz.game.example.simple;

import name.panitz.game.framework.*;

import java.io.*;
import java.util.ArrayList;

public class LevelBuilder<I, S> {
	private final SimpleGame<I, S> p;
	LevelBuilder(SimpleGame<I, S> p) {
		this.p = p;
	}
	public void makeLvl(int levelID) {
		if(levelID == 0) { // mainMenu
			loadFromFile("framework\\levelfiles\\mainMenu.incg");
		} else if(levelID == 1) { // lv 1
			loadFromFile("framework\\levelfiles\\level_1.incg");
		} else {
			loadFromFile("framework\\levelfiles\\level_2.incg");
		}
	}
	public void serialize() {
		try{
			FileOutputStream fos= new FileOutputStream("framework\\levelfiles\\level_2.incg");
			ObjectOutputStream oos= new ObjectOutputStream(fos);
			oos.writeObject(p.player.getPos());
			oos.writeObject(p.background);
			oos.writeObject(p.items);
			oos.writeObject(p.blocks);
			oos.writeObject(p.climbables);
			oos.writeObject(p.pushables);
			oos.writeObject(p.otherObjs);
			oos.close();
			fos.close();
			System.out.println(fos);
		}catch(Exception ioe){
			ioe.printStackTrace();
		}
	}
	@SuppressWarnings("unchecked")
	public void loadFromFile(String fName) {
		try	{
			FileInputStream fis = new FileInputStream(fName);
			ObjectInputStream ois = new ObjectInputStream(fis);
			p.player.getPos().moveTo((Vertex) ois.readObject());
			// TODO safe cast
			p.background.addAll((ArrayList<LevelBlock<I>>) ois.readObject());
			p.items.addAll((ArrayList<ImageObject<I>>) ois.readObject());
			p.blocks.addAll((ArrayList<LevelBlock<I>>) ois.readObject());
			p.climbables.addAll((ArrayList<LevelBlock<I>>) ois.readObject());
			p.pushables.addAll((ArrayList<PushableBlock<I>>) ois.readObject());
			p.otherObjs.addAll((ArrayList<GameObject<I>>) ois.readObject());
			ois.close();
			fis.close();
		}catch(Exception ioe){
			ioe.printStackTrace();
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
