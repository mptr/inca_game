package name.panitz.game.example.simple;

import name.panitz.game.framework.*;
import name.panitz.game.framework.Button;
import name.panitz.game.framework.swing.SwingGame;

import javax.swing.*;
import java.awt.*;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class SimpleGame<I, S> extends AbstractGame<I, S> {
	// Objektlisten
	int levelbuilder = -1;
	List<LevelBlock<I>> background = new ArrayList<>(); // no collisioncheck
	List<GameObject<I>> otherObjs = new ArrayList<>(); // front elements
	List<LevelBlock<I>> blocks = new ArrayList<>(); // collisioncheck
	List<LevelBlock<I>> climbables = new ArrayList<>(); // ladders & ropes
	List<ImageObject<I>> items = new ArrayList<>(); // collisioncheck
	List<PushableBlock<I>> pushables = new ArrayList<>();
	List<ImageObject<I>> toDel = new ArrayList<>(); // list modifiers
	List<ImageObject<I>> toAdd = new ArrayList<>();
	Vertex spawnPos = new Vertex(16*3+50,(gameSize.y*16-16*3)*3);
	Player<I, S> player;
	int aktLvl = 0;
	TextObject<I> coinDisplay = new TextObject<>(new Vertex(10, 32), "", "Times New Roman Bold", 30, new Color(0xE3A569));
	LevelBlock<I> coinDisplayBg = new LevelBlock<>(0, new Vertex(-7, -14), 102, 3, true);
	public static boolean muteSound = true;
	public static final Vertex gameSize = new Vertex(37,18); // game size (# of 16px blocks in x and y)
	public static final Vertex windowSize = new Vertex(34*16*3,18*16*3); // game size (# of 16px blocks in x and y)
	public static Rect currentVP = new Rect(0,0, windowSize.x, windowSize.y);
	public SimpleGame() {
		// init with player
		super(new Player<>(new Vertex(0, 0), new Vertex(0, 0),2.9), windowSize.x, windowSize.y);
		player = (Player<I, S>) super.player;
		player.setParent(this);
		buttons.add(new Button("Reset Level", () -> resetLvl(aktLvl)));
		buttons.add(new Button("Sound an/aus", this::toggleMute));
		/*buttons.add(new Button("LevelBuilder an/aus", () -> {
			resetLvl(5);
			levelbuilder = (levelbuilder==-1)?blocks.size()-1:-1;
			currentEdited=blocks.get(0);
			otherObjs.remove(coinDisplay);
			otherObjs.remove(coinDisplayBg);
		}));*/
		buttons.add(new Button("Hauptmenü",() -> resetLvl(0)));
		resetLvl(0);
		// setup
		getGOss().add(background);
		getGOss().add(climbables);
		getGOss().add(blocks);
		getGOss().add(items);
		getGOss().add(pushables);
		getGOss().add(otherObjs);

	}
	public void toggleMute() {
		muteSound = !muteSound;
		System.out.println(muteSound);
	}
	public void pSound(String fname) {
		if(muteSound) return;
		playSound(new SoundObject<>(fname));
	}
	public void resetLvl(int levelID) {
		if(levelID == -1) System.exit(0);
		items.clear();
		blocks.clear();
		pushables.clear();
		player.setCollectedCoins(0);
		climbables.clear();
		background.clear();
		otherObjs.clear();
		if(levelID == -2) return;
		if(levelID != 0) {
			otherObjs.add(coinDisplayBg);
			otherObjs.add(coinDisplay);
		}
		new LevelBuilder<>(this).makeLvl(levelID);
		aktLvl = levelID;
		pSound("pop.wav");
	}
	@Override
	public void doChecks() {
		if (levelbuilder > -1) return;
		Door<I> toEnter = null;
		// spawn arrows
		for (LevelBlock<I> b : blocks) {
			if (Math.random() > .005) continue; // randomize
			if (b.getCurrentAnimationFrame() == 37) { // <-- o
				items.add(new Arrow<>(new Vertex(b.getPos().x / 3 - 12, b.getPos().y / 3 + b.getHeight() / 2 / 3 - 1 /*obj Zoom*/), new Vertex(-6, 0), false));
			} else if (b.getCurrentAnimationFrame() == 38) { // o -->
				items.add(new Arrow<>(new Vertex(b.getPos().x / 3 + b.getWidth() / 3, b.getPos().y / 3 + b.getHeight() / 2 / 3 - 1 /*obj Zoom*/), new Vertex(6, 0), true));
			}
		}
		obstacleCollisionCheck(player);
		boolean doorsOpen = true;
		for (ImageObject<I> item : items) {
			if (item.touches(player)) {
				if (item instanceof Coin) { // Coin touched?
					toDel.add(item);
					player.setCollectedCoins(player.getCollectedCoins() + 1);
					pSound("coin.wav");
				} else if (item instanceof Arrow) {
					if (item.getVelocity().dist() > 0 && ((Arrow<I>) item).getStuckIn() == null) {
						dropCoins();
						player.kill();
						toDel.add(item);
						pSound("arrow.wav");
					}
				}
			}
			if (item instanceof Skeleton) {
				if(item.getPos().x > 10000 || item.getPos().x < -100) { // entity out of view -> respawn
					item.getPos().moveTo(((Skeleton<I>) item).getSpawnPos());
				}
				if (!skeletonKi((Skeleton<I>) item) || item.getPos().x + item.getPos().y > 10000) {
					toDel.add(item);
					new java.util.Timer().schedule(
							new java.util.TimerTask() {
								@Override
								public void run() {
									toAdd.add(new Skeleton<>(((Skeleton<I>) item).getSpawnPos(), new Vertex(0, 0)));
								}
							},
							1000
					); // respawn after 1s
				}
			}
			if (item instanceof FallingImage) {
				obstacleCollisionCheck((FallingImage<I>) item);
			}
			if (item instanceof Arrow) {
				List<GameObject<I>> tmp = new ArrayList<>(blocks);
				tmp.addAll(pushables);
				for (GameObject<I> b : tmp) {
					if (b.touches(item)) {
						if (item.getVelocity().dist() > 0) {
							pSound("arrow.wav");
							((Arrow<I>) item).setStuckIn(b);
						}
					}
				}
				for (GameObject<I> i : items) {
					if (item.touches(i) && (i instanceof PushableBlock) && item.getVelocity().dist() > 0) {
						pSound("arrow.wav");
						((Arrow<I>) item).setStuckIn(i);
					}
					if (i.touches(item) && ((Arrow<I>) item).getStuckIn() == null && (i instanceof Skeleton)) {
						((Skeleton<I>) i).setMood(5);
					}
				}
				if (((Arrow<I>) item).getRemoveCounter() > 200) {
					toDel.add(item);
				}
			} else if (item instanceof Door) {
				if (item.getCurrentAnimationFrame() == 3 && player.getObjectCenter().dist(item.getObjectCenter()) < 30) {
					player.setSpeed(0);
					toEnter = ((Door<I>) item);
				}
			} else if (item instanceof Coin) {
				doorsOpen = false;
			}
		}
		boolean finalDoorsOpen = doorsOpen;
		items.forEach(i -> {
			if (i instanceof Door) {
				((Door<I>) i).setOpen(finalDoorsOpen);
			}
		});
		items.removeAll(toDel);
		toDel.clear();
		items.addAll(toAdd);
		toAdd.clear();
		if (toEnter != null) {
			resetLvl(toEnter.enter());
			return;
		}
		pushables.sort((pb1, pb2) -> {
			double delta = pb1.getObjectCenter().dist(player.getObjectCenter()) - pb2.getObjectCenter().dist(player.getObjectCenter());
			if (delta < 0) return 1;
			else if (delta == 0) return 0;
			return -1;
		});
		for (int i = 0; i < pushables.size(); i++) {
			obstacleCollisionCheck(pushables.get(i));
			// pushcheck entity
			for (int j = i+1; j < pushables.size(); j++) {
				if(pushables.get(i).touches(pushables.get(j))) {
					pushables.get(i).getVelocity().x = pushables.get(j).getVelocity().x + 3*Math.signum(pushables.get(j).getVelocity().x);
				}
			}
		}
		if(player.getPos().y < -100 && player.getDeathTimer() == 0) {
			player.getPos().moveTo(spawnPos);
			player.getVelocity().moveTo(new Vertex(0,0));
			player.startJump(0);
			System.out.println("reset Player");
			pSound("pop.wav");
		} else //noinspection StatementWithEmptyBody
			if(player.getAnimationFrameSkip() == 0 && (player.getCurrentAnimationFrame() == 0 || player.getCurrentAnimationFrame() == 2)&& player.isJumping == 0 && player.getVelocity().y == 0 && Math.abs(player.getVelocity().x) > 0.5) {
			pSound("walking.wav"); // ggf auskommentieren in Linux
		}
		moveViewport();
	}
	public void moveViewport() {
		if(player.getDeathTimer() < 70 && player.getDeathTimer() > 0) return;
		currentVP.moveTo(new Vertex(
				Math.max(
						Math.min(player.getPos().x - currentVP.getWidth()/2,
								gameSize.x*3*16-currentVP.getWidth()),
						0),
				Math.max(
						Math.min(player.getPos().y - currentVP.getHeight()/2,
								gameSize.y*3*16-currentVP.getHeight())
						,0)
		).mult(-1));
	}
	public void dropCoins() {
		for (int i = 0; i < player.getCollectedCoins(); i++) {
			toAdd.add(new Coin<>(player.getPos().mult(1 / 3.0), new Vertex((Math.random() - .5) * 10, (Math.random() - .3) * 5)));
		}
		if(player.getCollectedCoins() > 0) {
			pSound("coins.wav");
		}
		player.setCollectedCoins(0);
	}
	public boolean skeletonKi(Skeleton<I> s) { // return false to remove
		if(s.getMood() == 5) { // dies
			return s.getCurrentAnimationFrame() != 14;
		}
		if(s.getMood() == 3 && s.getCurrentAnimationFrame() == 4 && s.getAnimationFrameSkip() == 0) {
			pSound("axe" + (int)(Math.random()*4+1) + ".wav");
		}
		Vertex toPlayer = s.getObjectCenter().connection(player.getObjectCenter());
		if(toPlayer.dist() < 400) {
			if (Math.abs(toPlayer.x) < 100 && !s.isAbove(player) && !s.isUnderneath(player)) {
				// hitting Player
				s.setMood(3);
				s.setFacing(player.getObjectCenter().connection(s.getObjectCenter()).x > 0);
				// is player stomping
				if(player.fallingOnTopOf(s) && player.getVelocity().y > 8.5) { // stomp power needed
					System.out.println("StompPower = " + player.getVelocity().y);
					s.setMood(5);
				} else {
					s.getVelocity().moveTo(new Vertex(0, 0));
					// kills Player?
					if (s.getMood() == 3 && s.getCurrentAnimationFrame() > 6 && s.getCurrentAnimationFrame() < 13 && Math.abs(player.getObjectCenter().connection(s.getObjectCenter()).mult(new Vertex(1, .5)).dist()) < 60 * s.getObjectZoom()) {
						dropCoins();
						player.kill();
						player.setCollectedCoins(0);
						s.setMood(0);
					}
				}
			} else if (Math.abs(toPlayer.x) < 20) {
				// waiting inPlace
				s.initWalking(0);
			} else {
				// walking & chasing Player
				if(player.isJumping > 0 && Math.abs(toPlayer.x) < 30) {
					s.initWalking(.5 * Math.abs(player.getVelocity().x) * Math.signum(toPlayer.x)); // follow
				} else {
					s.initWalking(1.5 * Math.signum(toPlayer.x));
				}
			}
		} else {
			// walk arround
			double walkSpeed = s.getVelocity().x;
			if (s.getKiTimer() > 500 && Math.random() < .01) {
				if(Math.random() < .2) {
					walkSpeed = 0;
					s.setKiTimer(300);
				} else {
					walkSpeed = .7 * Math.signum(Math.random() - .5);
					s.setKiTimer(0);
				}
			}
			Vertex probe = null;
			if (walkSpeed < 0) {
				probe = new Vertex(s.getPos().x + 2, s.getPos().y + s.getHeight() + 2);
			} else if (walkSpeed > 0) {
				probe = new Vertex(s.getPos().x + s.getWidth() + 2, s.getPos().y + s.getHeight() + 2);
			}
			if(probe != null) {
				boolean voidAhead = true;
				for (LevelBlock<I> b:blocks) {
					if(b.containsPoint(probe)) {
						voidAhead = false;
						s.setKiTimer(500);
						break;
					}
				}
				walkSpeed = voidAhead?0:walkSpeed;
			}
			s.initWalking(walkSpeed);
		}
		return true;
	}
	public void obstacleCollisionCheck(FallingImage<I> go) {
		boolean entityHasYCollision = false;
		go.setTouchesWall(0);
		go.setCanClimbUp(-1); // reset
		go.setCanClimbDown(-1);
		for (LevelBlock<I> c: climbables) {
			if(c.getCurrentAnimationFrame() == 98 || c.getCurrentAnimationFrame() == 100) continue; // no climb on rope end
			if(go.touches(c) || go.standingOnTopOf(c)) {
				go.setCanClimbUp(1);
				/*if(c.getCurrentAnimationFrame() < 97) {// rope
					go.setCanClimbUp(1);
				}*/ // climb any
				go.setCanClimbDown(1);
				entityHasYCollision = true;
			}
		}
		for (LevelBlock<I> b: blocks) {
			if (go.fallingOnTopOf(b)) {
				entityHasYCollision = true;
				go.stop(b.getPos().y);
			} else if (go.hitsLeftSideOf(b)) {
				go.setTouchesWall(-1);
				go.getPos().moveTo(new Vertex(b.getPos().x - go.getWidth(), go.getPos().y));
			} else if (go.hitsRightSideOf(b)) {
				go.setTouchesWall(1);
				go.getPos().moveTo(new Vertex(b.getPos().x + b.getWidth(), go.getPos().y));
			}
			if (go.standingOnTopOf(b)) {
				entityHasYCollision = true;
				go.setCanClimbDown(0);
			}
			if (go.hitsBottomOf(b)) {
				System.out.println("header");
				go.setCanClimbUp(0);
				go.startJump(0.1);
				entityHasYCollision = true;
			}
		}
		if(!(go instanceof PushableBlock)) {
			for (PushableBlock<I> pb : pushables) {
				if (go.standingOnTopOf(pb) || go.fallingOnTopOf(pb)) {
					go.setCanClimbDown(0);
					go.stop(pb.getPos().y);
					entityHasYCollision = true;
				} else if (go.hitsLeftSideOf(pb)) {
					pb.setVelocity(new Vertex(go.getVelocity().x + 3, 0));
				} else if (go.hitsRightSideOf(pb)) {
					pb.setVelocity(new Vertex(go.getVelocity().x - 3, 0));
				}
			}
		}
		if(!entityHasYCollision && go.isJumping == 0) {
			go.startJump(0);
		}
	}
	@Override
	public boolean isStopped() {
		return levelbuilder > -1;
	}
	private GameObject<I> currentEdited;
	private int moveFactor = 8;
	@Override
	public void keyPressedReaction(KeyCode keycode) {
		// listener
		if (keycode != null) {
			switch (keycode) {
				case VK_D:
					player.getVelocity().moveTo(new Vertex(1.5, 0));
					break;
				case VK_A:
					player.getVelocity().moveTo(new Vertex(-1.5, 0));
					break;
				case VK_SPACE:
					player.jump();
					break;
				case VK_K:
					dropCoins();
					player.kill();
					break;
				case VK_W:
					player.setClimbing(2);
					break;
				case VK_SHIFT:
					player.setSpeed(2.5);
					break;
				case VK_S:
					player.setClimbing(1);
					break;
				default:
			}
			if(levelbuilder > -1) {
				switch(keycode) {
					case VK_F:
						moveFactor = moveFactor==8?1:8;
						break;
					case NUM_2: // mv down
						currentEdited.getPos().move(new Vertex(0, moveFactor* 3));
						break;
					case NUM_4: // mv left
						currentEdited.getPos().move(new Vertex(-moveFactor * 3, 0));
						break;
					case NUM_6: // mv right
						currentEdited.getPos().move(new Vertex(moveFactor * 3, 0));
						break;
					case NUM_8: // mv up
						currentEdited.getPos().move(new Vertex(0, -moveFactor * 3));
						break;
					case VK_Y: // generate new fg
						blocks.add(new LevelBlock<>(0, blocks.size()>0?blocks.get(blocks.size() - 1).getPos().mult(1 / 3.0):new Vertex(0,0), currentEdited instanceof ImageObject ? ((ImageObject<I>) currentEdited).getCurrentAnimationFrame() : 0));
						currentEdited = blocks.get(blocks.size() - 1);
						break;
					case VK_X: // generate fg2
						pushables.add(new PushableBlock<>(1, pushables.size()>0?pushables.get(pushables.size() - 1).getPos().mult(1 / 3.0):new Vertex(0,0), new Vertex(0, 0), currentEdited instanceof ImageObject ? ((ImageObject<I>) currentEdited).getCurrentAnimationFrame() : 0));
						currentEdited = pushables.get(pushables.size() - 1);
						break;
					case VK_C: // generate new bg
						background.add(new LevelBlock<>(2, background.size()>0?background.get(background.size() - 1).getPos().mult(1 / 3.0):new Vertex(0,0), currentEdited instanceof ImageObject ? ((ImageObject<I>) currentEdited).getCurrentAnimationFrame() : 0));
						currentEdited = background.get(background.size() - 1);
						break;
					case VK_M: // generate new climbable
						climbables.add(new LevelBlock<>(0, climbables.size()>0?climbables.get(climbables.size() - 1).getPos().mult(1 / 3.0):new Vertex(0,0), currentEdited instanceof ImageObject ? ((ImageObject<I>) currentEdited).getCurrentAnimationFrame() : 0));
						currentEdited = climbables.get(climbables.size() - 1);
						break;
					case VK_V:
						items.add(new Coin<>(new Vertex(0,0), new Vertex(0,0)));
						currentEdited = items.get(items.size()-1);
						break;
					case VK_B:
						items.add(new Skeleton<>(new Vertex(0,0), new Vertex(0,0)));
						currentEdited = items.get(items.size()-1);
						break;
					case VK_N:
						items.add(new Door<>(new Vertex(0,14),0));
						currentEdited = items.get(items.size()-1);
						break;
					case VK_L:
						otherObjs.add(new TextObject<>(new Vertex(100,100), (String) JOptionPane.showInputDialog(
								null,
								"Text für Text-Item:",
								"",
								JOptionPane.PLAIN_MESSAGE,
								null,
								null,
								""), (String) JOptionPane.showInputDialog(
								null,
								"Schriftart für Text-Item:",
								"",
								JOptionPane.PLAIN_MESSAGE,
								null,
								List.of("Times New Roman","Times New Roman Bold").toArray(),
								"Times New Roman"),Integer.parseInt((String) JOptionPane.showInputDialog(
								null,
								"Schriftgröße für Text-Item (Zahl):",
								"",
								JOptionPane.PLAIN_MESSAGE,
								null,
								null,
								"")),new Color(Integer.parseInt((String) JOptionPane.showInputDialog(
								null,
								"HEX-Farbe für Text-Item (ohne # oder 0x):",
								"",
								JOptionPane.PLAIN_MESSAGE,
								null,
								null,
								""),16))));
						currentEdited = otherObjs.get(otherObjs.size()-1);
						break;
					case NUM_3: // prev Frame
						if (currentEdited instanceof ImageObject)
							((ImageObject<I>) currentEdited).setCurrentAnimationFrame(((ImageObject<I>) currentEdited).getCurrentAnimationFrame() - 1);
						break;
					case NUM_9: // next frame
						if (currentEdited instanceof ImageObject)
							((ImageObject<I>) currentEdited).setCurrentAnimationFrame(((ImageObject<I>) currentEdited).getCurrentAnimationFrame() + 1);
						break;
					case NUM_1: // prev Block
					{
						GameObject<I> prev = currentEdited;
						outer:for (List<? extends GameObject<I>> a : getGOss()) {
							for (GameObject<I> g : a) {
								if (g.equals(currentEdited)) {
									currentEdited = prev;
									break outer;
								}
								prev = g;
							}
						}
						break;
					}
					case NUM_7: // next Block
					{
						boolean next = false;
						outer:for (List<? extends GameObject<I>> a : getGOss()) {
							for (GameObject<I> g : a) {
								if(next) {
									currentEdited = g;
									break outer;
								}
								if (g.equals(currentEdited)) {
									next = true;
								}
							}
						}
						break;
					}
					case VK_U:
						currentEdited = blocks.get(blocks.size()-1);
						break;
					case VK_Z:
						currentEdited = background.get(background.size()-1);
						break;
					case VK_I:
						currentEdited = items.get(items.size()-1);
						break;
					case VK_P:
						currentEdited = pushables.get(pushables.size()-1);
						break;
					case VK_O:
						currentEdited = climbables.get(climbables.size()-1);
						break;
					case VK_T:
						currentEdited = otherObjs.get(otherObjs.size()-1);
						break;
					case NUM_0: // delete
						if(currentEdited instanceof LevelBlock)
							blocks.remove(currentEdited);
						if(currentEdited instanceof LevelBlock)
							climbables.remove(currentEdited);
						if(currentEdited instanceof LevelBlock)
							background.remove(currentEdited);
						if(currentEdited instanceof ImageObject)
							items.remove(currentEdited);
						if(currentEdited instanceof PushableBlock)
							pushables.remove(currentEdited);
						otherObjs.remove(currentEdited);
						GameObject<I> prev = currentEdited;
						{
							outer:
							for (List<? extends GameObject<I>> a : getGOss()) {
								for (GameObject<I> g : a) {
									if (g.equals(currentEdited)) {
										currentEdited = prev;
										break outer;
									}
									prev = g;
								}
							}
						}
						break;
					case NUM_5: // export
						new LevelBuilder<>(this).exportLvl();
						levelbuilder = -1;
						break;
					case DOWN_ARROW:
						player.getPos().move(new Vertex(0,8));
						break;
					case UP_ARROW:
						player.getPos().move(new Vertex(0,-8));
						break;
					case LEFT_ARROW:
						player.getPos().move(new Vertex(-16,0));
					case RIGHT_ARROW:
						player.getPos().move(new Vertex(8,0));
						moveViewport();
						break;
				}
			}
		}
	}
	@Override
	public void keyReleasedReaction(KeyCode keycode) {
		if (keycode != null) {
			switch (keycode) {
				case VK_SHIFT:
					player.setSpeed(1.25);
				case VK_S:
				case VK_W:
					player.setClimbing(0);
					if(player.getCanClimbUp() || player.getCanClimbDown())
						player.startJump(0);
					break;
				case VK_A:
				case VK_D:
					player.getVelocity().moveTo(player.getVelocity().mult(0));
					break;
			}
		}
	}
	public static void main(String[] args) {
		SwingGame.startGame(new SimpleGame<>());
	}
}

