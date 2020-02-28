package name.panitz.game.example.simple;

import name.panitz.game.framework.*;

import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class LevelBuilder<I, S> {
	private final SimpleGame<I, S> p;
	LevelBuilder(SimpleGame<I, S> p) {
		this.p = p;
	}
	public void makeLvl(int levelID) {
		if(levelID == 0) { // mainMenu
			//loadFromFile("mainMenu.incg");
			deJsonify("MainMenu.inca");
		} else if(levelID == 1) { // lv 1
			//loadFromFile("level_1.incg");
			deJsonify("Level1.inca");
		} else {
			//loadFromFile("level_2.incg");
			deJsonify("Level2.inca");
		}
	}
	public void serialize() {
		try{
			FileOutputStream fos = new FileOutputStream("level_2.incg");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
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
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void jsonify() {
		try {
			File fileDir = new File("Level2.inca");
			Writer ow = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileDir), StandardCharsets.UTF_8));
			StringBuilder out = new StringBuilder();
			out.append("pl,").append(Math.round(p.player.getPos().x)).append(",").append(Math.round(p.player.getPos().y)).append("\n");
			p.blocks.forEach(b -> out.append("bl,").append(Math.round(b.getPos().x)).append(",").append(Math.round(b.getPos().y)).append(",").append(b.getCurrentAnimationFrame()).append("\n"));
			p.background.forEach(b -> out.append("bg,").append(Math.round(b.getPos().x)).append(",").append(Math.round(b.getPos().y)).append(",").append(b.getCurrentAnimationFrame()).append("\n"));
			p.climbables.forEach(b -> out.append("cl,").append(Math.round(b.getPos().x)).append(",").append(Math.round(b.getPos().y)).append(",").append(b.getCurrentAnimationFrame()).append("\n"));
			p.pushables.forEach(b -> out.append("pb,").append(Math.round(b.getPos().x)).append(",").append(Math.round(b.getPos().y)).append(",").append(b.getCurrentAnimationFrame()).append("\n"));
			for (GameObject<I> b:p.otherObjs) {
				if(b instanceof TextObject)
					out.append("oo,to,").append(Math.round(b.getPos().x)).append(",").append(Math.round(b.getPos().y)).append(",")/*.append((TextObject<I>)(b).text)*/.append(",").append("25,").append("0xff7200,").append("Times New Roman").append("\n");
				else
					System.out.println("unknown obj");
			}
			for (ImageObject<I> b:p.items) {
				if(b instanceof Skeleton)
					out.append("it,sk,").append(Math.round(b.getPos().x)).append(",").append(Math.round(b.getPos().y)).append(",").append("\n");
				if(b instanceof Coin)
					out.append("it,co,").append(Math.round(b.getPos().x)).append(",").append(Math.round(b.getPos().y)).append(",").append("\n");
				if(b instanceof Door)
					out.append("it,do,").append(Math.round(b.getPos().x)).append(",").append(Math.round(b.getPos().y)).append(",").append(/*b.getLvlToEnter*/0).append("\n");
			}
			ow.write(out.toString());
			ow.close();
			System.out.println(ow);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void deJsonify(String fName) {
		try {
			//File fileDir = new File(fName);
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(Objects.requireNonNull(getClass().getClassLoader().getResource(fName)).getFile()), StandardCharsets.UTF_8));
			String item;
			while((item = in.readLine()) != null) {
				if(item.trim().startsWith("//")) continue;
				String[] attrs = item.split(",");
				switch (attrs[0]) {
					case "pl":
						p.player.getPos().moveTo(new Vertex(Integer.parseInt(attrs[1]),Integer.parseInt(attrs[2])));
						break;
					case "bl":
						p.blocks.add(new LevelBlock<>(0, new Vertex(Integer.parseInt(attrs[1]),Integer.parseInt(attrs[2])).mult(1/3.0), Integer.parseInt(attrs[3])));
						break;
					case "bg":
						p.background.add(new LevelBlock<>(2, new Vertex(Integer.parseInt(attrs[1]),Integer.parseInt(attrs[2])).mult(1/3.0), Integer.parseInt(attrs[3])));
						break;
					case "cl":
						p.climbables.add(new LevelBlock<>(0, new Vertex(Integer.parseInt(attrs[1]),Integer.parseInt(attrs[2])).mult(1/3.0), Integer.parseInt(attrs[3])));
						break;
					case "pb":
						p.pushables.add(new PushableBlock<>(1, new Vertex(Integer.parseInt(attrs[1]),Integer.parseInt(attrs[2])).mult(1/3.0), new Vertex(0,0), Integer.parseInt(attrs[3])));
						break;
					case "it":
						switch (attrs[1]) {
							case "sk":
								p.items.add(new Skeleton<>(new Vertex(Integer.parseInt(attrs[2]),Integer.parseInt(attrs[3])).mult(1/3.0),new Vertex(0,0)));
								break;
							case "co":
								p.items.add(new Coin<>(new Vertex(Integer.parseInt(attrs[2]),Integer.parseInt(attrs[3])).mult(1/3.0),new Vertex(0,0)));
								break;
							case "do":
								p.items.add(new Door<>(new Vertex(Integer.parseInt(attrs[2]),Integer.parseInt(attrs[3])).mult(1/3.0),Integer.parseInt(attrs[4])));
								break;
						}
						break;
					case "oo":
						if(attrs[1].equals("to")) // TextObj
							p.otherObjs.add(new TextObject<>(new Vertex(Integer.parseInt(attrs[2]),Integer.parseInt(attrs[3])), attrs[4], attrs[7], Integer.parseInt(attrs[5]), new Color(Integer.parseInt(attrs[6],16)), false));
						break;
				}
				System.out.println(Arrays.toString(attrs));
			}
			in.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	@SuppressWarnings("unchecked")
	public void loadFromFile(String fName) {
		try	{
			FileInputStream fis = new FileInputStream(Objects.requireNonNull(getClass().getClassLoader().getResource(fName)).getFile());
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
