package name.panitz.game.example.simple;

import name.panitz.game.framework.Vertex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SpriteGrid {
	public static final List<Rect> blockGrid = Collections.unmodifiableList(
			new ArrayList<>() {{
				// 32x32 elms
				for (int i = 0; i < 2; i++) {
					for (int j = 0; j < 5; j++) {
						add(new Rect(32*j,32*i,32*(j+1),32*(i+1)));
					}
				}
				// 32x16 elms
				for (int i = 0; i < 3; i++) {
					for (int j = 0; j < 5; j++) {
						add(new Rect(32*j,64+16*i,32*(j+1),64+16*(i+1)));
					}
				}
				// 16x16 elms
				for (int i = 0; i < 3; i++) {
					for (int j = 0; j < 10; j++) {
						add(new Rect(16*j,64+48+16*i,16*(j+1),64+48+16*(i+1)));
					}
				}
				// pillars
				for (int i = 0; i < 4; i++) {
						add(new Rect(0,64+48+48+16*i,16,64+48+48+16*(i+1)));
				}
				// 16x8 elms
				for (int i = 0; i < 4; i++) {
					add(new Rect(16, 64+48+48+8*i, 32, 64+48+48+8*(i+1)));
				}
				// 8x8 elms
				for (int i = 0; i < 2; i++) {
					for (int j = 0; j < 4; j++) {
						add(new Rect(32+8*i, 64+48+48+8*j, 32+8*(1+i), 64+48+48+8*(1+j)));
					}
				}
				// ladders
				for (int i = 0; i < 2; i++) {
					for (int j = 0; j < 5; j++) {
						add(new Rect(48+16*i, 64+48+48+8*j, 48+16*(1+i), 64+48+48+8*(1+j)));
					}
				}
				// borders
				for (int i = 0; i < 4; i++) {
					for (int j = 0; j < 4; j++) {
						add(new Rect(16+8*i,64+48+48+32+8*j,16+8*(i+1),64+48+48+32+8*(j+1)));
					}
				}
				// ropes
				// first rope pixel:  48 | 200

				add(new Rect(48+1, 200,48+1+6, 200+16));
				add(new Rect(48+1, 200+16,48+1+6, 200+16+8));
				add(new Rect(48+8+1, 200,48+8+1+6, 200+16));
				add(new Rect(48+8+1, 200+16,48+8+1+6, 200+16+8));
				// void
				add(new Rect(80,160,112,192));
				// coindisplay
				add(new Rect(96,192, 160, 224));
			}}
	);
	public static final List<Rect> coinGrid = Collections.unmodifiableList(
			new ArrayList<>() {{
				for (int i = 0; i < 8; i++) {
					add(new Rect(i*120, 0, (i+1)*120, 120));
				}
			}}
	);
	public static final List<Rect> arrowGrid = List.of(new Rect(0, 0, 12, 3));
	public static final List<Rect> playerGrid = Collections.unmodifiableList(
			new ArrayList<>() {{
				for (int i = 0; i < 4; i++) {
					add(new Rect(i*16, 0, (i+1)*16, 16));
				}
			}}
	);
	public static final List<Rect> doorGrid = Collections.unmodifiableList(
			new ArrayList<>() {{
				for (int i = 0; i < 4; i++) {
					add(new Rect(i*34, 0, (i+1)*34, 34));
				}
			}}
	);
	public static final List<List<Rect>> skeletonGrid = Collections.unmodifiableList(
			new ArrayList<>() {{
				final int[] widths  = {24,22,22,43,30,33};
				final int[] frames  = {11,11, 4,18, 8,15};
				final int[] heights = {32,33,32,37,32,32};
				for (int i = 0; i < 6; i++) {
					int finalI = i;
					add(Collections.unmodifiableList(
							new ArrayList<>() {{
								for (int j = 0; j < frames[finalI]; j++) {
									add(new Rect(j*widths[finalI], 0, (j+1)*widths[finalI], heights[finalI]));
								}
							}}
					));
				}
			}}
	);
	public static final List<Vertex> skeletonOffsets = List.of(new Vertex(0, 0), new Vertex(0, -1), new Vertex(0, 0), new Vertex(-19, -5), new Vertex(0, 0), new Vertex(0, 0));
	public static List<Rect> getListFromID(int x) {
		switch (x) {
			case 0:
				return coinGrid;
			case 1:
				return playerGrid;
			case 10:
			case 11:
			case 12:
				return blockGrid;
			case 20:
			case 21:
			case 22:
			case 23:
			case 24:
			case 25:
				x = x%10;
				return skeletonGrid.get(x);
			case 3:
				return arrowGrid;
			case 4:
				return doorGrid;
			default:
				return new ArrayList<>();
		}
	}
	public static String getFileNameFromID(int x) {
		if(x==1) return "player.png";
		int s0 = x/10;
		int s1 = x%10;
		if(s0 == 1) {
			switch (s1) {
				case 0:
					return "inca_front.png";
				case 1:
					return "inca_back.png";
				case 2:
					return "inca_back2.png";
			}
		}
		if(s0 == 2) {
			switch (s1){
				case 0:
					return "skeleton.png";
				case 1:
					return "skeleton_walk.png";
				case 2:
					return "skeleton_react.png";
				case 3:
					return "skeleton_attack.png";
				case 4:
					return "skeleton_hit.png";
				case 5:
					return "skeleton_dead.png";
			}
		}
		if(x==3) return "arrow.png";
		if(x==4) return "door.png";
		return "coin.png";
	}
	public static Vertex getCutoutOffset(int x) {
		int s0 = x / 10;
		int s1 = x % 10;
		if(s0 != 2) return new Vertex(0,0);
		return skeletonOffsets.get(s1);
	}
}
