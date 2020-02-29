package name.panitz.game.example.simple;

import name.panitz.game.framework.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.Objects;

public class LevelBuilder<I, S> {
	private final SimpleGame<I, S> p;
	LevelBuilder(SimpleGame<I, S> p) {
		this.p = p;
	}
	public void makeLvl(int levelID) {
		if(levelID == 0) { // mainMenu
			importLvl("MainMenu.inca");
		} else if(levelID == 1) { // lv 1
			importLvl("Level1.inca");
		} else if(levelID == 2) { // lvl 2
			importLvl("Level2.inca");
		} else {
			importLvl("");
		}
	}
	public void exportLvl() {
		try {
			File fileDir = new File("framework\\levelfiles\\Level2.inca");
			Writer ow = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileDir), StandardCharsets.UTF_8));
			StringBuilder out = new StringBuilder();
			out.append("pl,").append(Math.round(p.player.getPos().x)).append(",").append(Math.round(p.player.getPos().y)).append("\n");
			p.blocks.forEach(b -> out.append("bl,").append(Math.round(b.getPos().x)).append(",").append(Math.round(b.getPos().y)).append(",").append(b.getCurrentAnimationFrame()).append("\n"));
			p.background.forEach(b -> out.append("bg,").append(Math.round(b.getPos().x)).append(",").append(Math.round(b.getPos().y)).append(",").append(b.getCurrentAnimationFrame()).append("\n"));
			p.climbables.forEach(b -> out.append("cl,").append(Math.round(b.getPos().x)).append(",").append(Math.round(b.getPos().y)).append(",").append(b.getCurrentAnimationFrame()).append("\n"));
			p.pushables.forEach(b -> out.append("pb,").append(Math.round(b.getPos().x)).append(",").append(Math.round(b.getPos().y)).append(",").append(b.getCurrentAnimationFrame()).append("\n"));
			for (GameObject<I> b:p.otherObjs) {
				if(b instanceof TextObject)
					out.append("oo,to,").append(Math.round(b.getPos().x)).append(",").append(Math.round(b.getPos().y)).append(",").append(((TextObject<I>) b).getText()).append(",").append(((TextObject<I>) b).getFontSize()).append(",").append(((TextObject<I>) b).getColor()).append(",").append(((TextObject<I>) b).getFontName()).append("\n");
				else
					System.out.println("unknown obj");
			}
			for (ImageObject<I> b:p.items) {
				if(b instanceof Skeleton)
					out.append("it,sk,").append(Math.round(b.getPos().x)).append(",").append(Math.round(b.getPos().y)).append("\n");
				if(b instanceof Coin)
					out.append("it,co,").append(Math.round(b.getPos().x)).append(",").append(Math.round(b.getPos().y)).append("\n");
				if(b instanceof Door)
					out.append("it,do,").append(Math.round(b.getPos().x)).append(",").append(Math.round(b.getPos().y)).append(",").append(((Door<I>) b).getLvlToEnter()).append("\n");
			}
			ow.write(out.toString());
			ow.close();
			System.out.println(ow);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void importLvl(String fName) {
		try {
			BufferedReader in;
			if(fName.equals("")) {
				final JFileChooser fc = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("INCA-LevelFiles (*.inca)", "inca");
				fc.setFileFilter(filter);
				int returnVal = fc.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File fileDir = fc.getSelectedFile();
					in = new BufferedReader(new InputStreamReader(new FileInputStream(fileDir), StandardCharsets.UTF_8));
				} else {
					p.resetLvl(0);
					return;
				}
			} else {
				in = new BufferedReader(new InputStreamReader(new FileInputStream(Objects.requireNonNull(getClass().getClassLoader().getResource(fName)).getFile()), StandardCharsets.UTF_8));
			}
			String item;
			while((item = in.readLine()) != null) {
				if(item.trim().startsWith("//")) continue;
				String[] attrs = item.split(",");
				switch (attrs[0]) {
					case "pl":
						p.player.getPos().moveTo(new Vertex(Integer.parseInt(attrs[1]),Integer.parseInt(attrs[2])));
						p.spawnPos = new Vertex(Integer.parseInt(attrs[1]),Integer.parseInt(attrs[2]));
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
			}
			p.items.sort((i1,i2) -> {
				if((i1 instanceof Door)) return -1;
				return 0;
			});
			in.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
