package name.panitz.game.example.simple;

import name.panitz.game.framework.Vertex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SpriteGrid {
	public static final List<Rect> blockGrid = List.of(
			new Rect(0,0,32,32),
			new Rect(32,0,64,32),
			new Rect(64,0,96,32),
			new Rect(96,0,128,32),
			new Rect(128,0,160,32),
			new Rect(0,32,32,64),
			new Rect(32,32,64,64),
			new Rect(64,32,96,64),
			new Rect(96,32,128,64),
			new Rect(128,32,160,64),
			new Rect(0,64,32,80),
			new Rect(32,64,64,80),
			new Rect(64,64,96,80),
			new Rect(96,64,128,80),
			new Rect(128,64,160,80),
			new Rect(0,80,32,96),
			new Rect(32,80,64,96),
			new Rect(64,80,96,96),
			new Rect(96,80,128,96),
			new Rect(128,80,160,96),
			new Rect(0,96,32,112),
			new Rect(32,96,64,112),
			new Rect(64,96,96,112),
			new Rect(96,96,128,112),
			new Rect(128,96,160,112),
			new Rect(0,112,16,128),
			new Rect(16,112,32,128),
			new Rect(32,112,48,128),
			new Rect(48,112,64,128),
			new Rect(64,112,80,128),
			new Rect(80,112,96,128),
			new Rect(96,112,112,128),
			new Rect(112,112,128,128),
			new Rect(128,112,144,128),
			new Rect(144,112,160,128),
			new Rect(0,128,16,144),
			new Rect(16,128,32,144),
			new Rect(32,128,48,144),
			new Rect(48,128,64,144),
			new Rect(64,128,80,144),
			new Rect(80,128,96,144),
			new Rect(96,128,112,144),
			new Rect(112,128,128,144),
			new Rect(128,128,144,144),
			new Rect(144,128,160,144),
			new Rect(0,144,16,160),
			new Rect(16,144,32,160),
			new Rect(32,144,48,160),
			new Rect(48,144,64,160),
			new Rect(64,144,80,160),
			new Rect(80,144,96,160),
			new Rect(96,144,112,160),
			new Rect(112,144,128,160),
			new Rect(128,144,144,160),
			new Rect(144,144,160,160),
			new Rect(0,160,16,176),
			new Rect(0,176,16,192),
			new Rect(0,192,16,208),
			new Rect(0,208,16,224),
			new Rect(16,160,32,168),
			new Rect(16,168,32,176),
			new Rect(16,176,32,184),
			new Rect(16,184,32,192),
			new Rect(32,160,40,168),
			new Rect(32,168,40,176),
			new Rect(32,176,40,184),
			new Rect(32,184,40,192),
			new Rect(40,160,48,168),
			new Rect(40,168,48,176),
			new Rect(40,176,48,184),
			new Rect(40,184,48,192),
			new Rect(48,160,64,168),
			new Rect(48,168,64,176),
			new Rect(48,176,64,184),
			new Rect(48,184,64,192),
			new Rect(48,192,64,200),
			new Rect(64,160,80,168),
			new Rect(64,168,80,176),
			new Rect(64,176,80,184),
			new Rect(64,184,80,192),
			new Rect(64,192,80,200),
			new Rect(16,192,24,200),
			new Rect(16,200,24,208),
			new Rect(16,208,24,216),
			new Rect(16,216,24,224),
			new Rect(24,192,32,200),
			new Rect(24,200,32,208),
			new Rect(24,208,32,216),
			new Rect(24,216,32,224),
			new Rect(32,192,40,200),
			new Rect(32,200,40,208),
			new Rect(32,208,40,216),
			new Rect(32,216,40,224),
			new Rect(40,192,48,200),
			new Rect(40,200,48,208),
			new Rect(40,208,48,216),
			new Rect(40,216,48,224),
			new Rect(49,200,55,216),
			new Rect(49,216,55,224),
			new Rect(57,200,63,216),
			new Rect(57,216,63,224),
			new Rect(80,160,112,192),
			new Rect(96,192,160,224)
	);
	public static final List<Rect> coinGrid = List.of(
			new Rect(0,0,120,120),
			new Rect(120,0,240,120),
			new Rect(240,0,360,120),
			new Rect(360,0,480,120),
			new Rect(480,0,600,120),
			new Rect(600,0,720,120),
			new Rect(720,0,840,120),
			new Rect(840,0,960,120)
	);
	public static final List<Rect> playerGrid = List.of(
			new Rect(0,0,16,16),
			new Rect(16,0,32,16),
			new Rect(32,0,48,16),
			new Rect(48,0,64,16)
	);
	public static final List<Rect> doorGrid = List.of(
			new Rect(0,0,34,34),
			new Rect(34,0,68,34),
			new Rect(68,0,102,34),
			new Rect(102,0,136,34)
	);
	public static final List<List<Rect>> skeletonGrid = List.of(
			List.of(
					new Rect(0,0,24,32),
					new Rect(24,0,48,32),
					new Rect(48,0,72,32),
					new Rect(72,0,96,32),
					new Rect(96,0,120,32),
					new Rect(120,0,144,32),
					new Rect(144,0,168,32),
					new Rect(168,0,192,32),
					new Rect(192,0,216,32),
					new Rect(216,0,240,32),
					new Rect(240,0,264,32)
			),
			List.of(
					new Rect(0,0,22,33),
					new Rect(22,0,44,33),
					new Rect(44,0,66,33),
					new Rect(66,0,88,33),
					new Rect(88,0,110,33),
					new Rect(110,0,132,33),
					new Rect(132,0,154,33),
					new Rect(154,0,176,33),
					new Rect(176,0,198,33),
					new Rect(198,0,220,33),
					new Rect(220,0,242,33)
			),
			List.of(
					new Rect(0,0,22,32),
					new Rect(22,0,44,32),
					new Rect(44,0,66,32),
					new Rect(66,0,88,32)
			),
			List.of(
					new Rect(0,0,43,37),
					new Rect(43,0,86,37),
					new Rect(86,0,129,37),
					new Rect(129,0,172,37),
					new Rect(172,0,215,37),
					new Rect(215,0,258,37),
					new Rect(258,0,301,37),
					new Rect(301,0,344,37),
					new Rect(344,0,387,37),
					new Rect(387,0,430,37),
					new Rect(430,0,473,37),
					new Rect(473,0,516,37),
					new Rect(516,0,559,37),
					new Rect(559,0,602,37),
					new Rect(602,0,645,37),
					new Rect(645,0,688,37),
					new Rect(688,0,731,37),
					new Rect(731,0,774,37)
			),
			List.of(
					new Rect(0,0,30,32),
					new Rect(30,0,60,32),
					new Rect(60,0,90,32),
					new Rect(90,0,120,32),
					new Rect(120,0,150,32),
					new Rect(150,0,180,32),
					new Rect(180,0,210,32),
					new Rect(210,0,240,32)
			),
			List.of(
					new Rect(0,0,33,32),
					new Rect(33,0,66,32),
					new Rect(66,0,99,32),
					new Rect(99,0,132,32),
					new Rect(132,0,165,32),
					new Rect(165,0,198,32),
					new Rect(198,0,231,32),
					new Rect(231,0,264,32),
					new Rect(264,0,297,32),
					new Rect(297,0,330,32),
					new Rect(330,0,363,32),
					new Rect(363,0,396,32),
					new Rect(396,0,429,32),
					new Rect(429,0,462,32),
					new Rect(462,0,495,32)
			)
	);
	public static final List<Rect> arrowGrid = List.of(new Rect(0, 0, 12, 3));
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
