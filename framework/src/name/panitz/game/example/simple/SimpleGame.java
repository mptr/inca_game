package name.panitz.game.example.simple;

import name.panitz.game.framework.*;
import name.panitz.game.framework.Button;
import name.panitz.game.framework.swing.SwingGame;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;

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
	Player<I, S> player;
	TextObject<I> coinDisplay = new TextObject<>(new Vertex(10, 30), "Coins: 0", "DejaVu Sans Mono", 30, new Color(0x001B37));
	public static boolean muteSound = true;
	public static final Vertex gameSize = new Vertex(37,18); // game size (# of 16px blocks in x and y)
	public static final Vertex windowSize = new Vertex(34*16*3,18*16*3); // game size (# of 16px blocks in x and y)
	public static Rect currentVP = new Rect(0,0, windowSize.x, windowSize.y);
	public SimpleGame() {
		// init with player
		super(new Player<>(new Vertex(0, 0), new Vertex(0, 0),2.9), windowSize.x, windowSize.y);
		player = (Player<I, S>) super.player;
		player.setParent(this);
		buttons.add(new Button("Reset Level", () -> resetLvl(0)));
		buttons.add(new Button("toggle Sound", this::toggleMute));
		buttons.add(new Button("toggle LevelBuilder", () -> levelbuilder = (levelbuilder==-1)?blocks.size()-1:-1));
		resetLvl(0);
		// setup
		otherObjs.add(new TextObject<>(new Vertex(0,29), "███▋", "DejaVu Sans Mono", 42, new Color(0xE3E2E1)));
		otherObjs.add(coinDisplay);
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
		items.clear();
		blocks.clear();
		pushables.clear();
		player.setCollectedCoins(0);
		climbables.clear();
		background.clear();
		new LevelBuilder<>(this).makeLvl(levelID);
		pSound("pop.wav");
	}
	@Override
	public void doChecks() {
		if(levelbuilder > -1) return;
		// spawn arrows
		for(LevelBlock<I> b: blocks) {
			if(Math.random() > .005) continue; // randomize
			if(b.getCurrentAnimationFrame() == 37) { // <-- o
				items.add(new Arrow<>(new Vertex(b.getPos().x/3 -12, b.getPos().y/3 + b.getHeight()/2/3-1 /*obj Zoom*/), new Vertex(-6,0),false));
			} else if(b.getCurrentAnimationFrame() == 38) { // o -->
				items.add(new Arrow<>(new Vertex(b.getPos().x/3 + b.getWidth()/3, b.getPos().y/3 + b.getHeight()/2/3-1 /*obj Zoom*/), new Vertex(6,0),true));
			}
		}
		obstacleCollisionCheck(player);
		boolean doorsOpen = true;
		for (ImageObject<I> item: items) {
			if(item.touches(player)){
				if(item instanceof Coin) { // Coin touched?
					toDel.add(item);
					player.setCollectedCoins(player.getCollectedCoins() + 1);
					pSound("coin.wav");
				} else if(item instanceof Arrow) {
					if (item.getVelocity().dist() > 0 && ((Arrow<I>) item).getStuckIn() == null) {
						dropCoins();
						player.kill();
						toDel.add(item);
						pSound("arrow.wav");
					}
				}
			}
			if(item instanceof FallingImage) {
				obstacleCollisionCheck((FallingImage<I>) item);
			}
			if(item instanceof Skeleton) {
				if(!skeletonKi((Skeleton<I>)item)) {
					toDel.add(item);
					toAdd.add(new Skeleton<>(new Vertex(13*gameSize.x, gameSize.y*16-100), new Vertex(0,0)));
				}
			} else if(item instanceof Arrow) {
				for (LevelBlock<I> b: blocks) {
					if(b.touches(item)) {
						if(item.getVelocity().dist() > 0) {
							pSound("arrow.wav");
							((Arrow<I>) item).setStuckIn(b);
						}
					}
				}
				for (GameObject<I> i: items) {
					if(item.touches(i) && (i instanceof PushableBlock) && item.getVelocity().dist() > 0) {
						pSound("arrow.wav");
						((Arrow<I>) item).setStuckIn(i);
					}
					if (i.touches(item) && ((Arrow<I>) item).getStuckIn() == null && (i instanceof Skeleton)) {
						((Skeleton<I>)i).setMood(5);
					}
				}
				if(((Arrow<I>)item).getRemoveCounter() > 200) {
					toDel.add(item);
				}
			} else if(item instanceof Door) {
				if(item.getCurrentAnimationFrame() == 3 && player.getObjectCenter().dist(item.getObjectCenter()) < 30) {
					player.setSpeed(0);
					((Door<I>) item).enter();
				}
			} else if(item instanceof Coin) {
				doorsOpen = false;
			}
		}
		boolean finalDoorsOpen = doorsOpen;
		items.forEach(i -> {
			if(i instanceof Door){
				//((Door<I>) i).setOpen(finalDoorsOpen);
			}
		});
		items.removeAll(toDel);
		toDel.clear();
		items.addAll(toAdd);
		toAdd.clear();
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
		if(player.getPos().y > gameSize.y*16*3+200 && player.getDeathTimer() == 0) {
			player.getPos().moveTo(new Vertex(16*3+50,(gameSize.y*16-16*3)*3));
			player.getVelocity().moveTo(new Vertex(0,0));
			player.startJump(0);
			System.out.println("reset Player");
			pSound("pop.wav");
		} else //noinspection StatementWithEmptyBody
			if(player.getAnimationFrameSkip() == 0 && (player.getCurrentAnimationFrame() == 0 || player.getCurrentAnimationFrame() == 2)&& player.isJumping == 0 && player.getVelocity().y == 0 && Math.abs(player.getVelocity().x) > 0.5) {
//			pSound("walking.wav");
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
				if(player.fallingOnTopOf(s) && player.getVelocity().y > 7.2) { // stomp power needed
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
				s.initWalking(0,blocks);
			} else {
				// walking & chasing Player
				if(player.isJumping > 0 && Math.abs(toPlayer.x) < 30) {
					s.initWalking(.5 * Math.abs(player.getVelocity().x) * Math.signum(toPlayer.x), blocks); // follow little slower than player is jumping
				} else {
					s.initWalking(1.5 * Math.signum(toPlayer.x), blocks);
				}
			}
		} else {
			// walk arround
			if(s.getKiTimer() > 500 && s.getVelocity().x == 0) {
				s.initWalking(.7 * Math.signum(Math.random()-.5), blocks);
			} else if(s.getKiTimer() < 500) {
				s.initWalking(0,blocks);
			}
		}
		return true;
	}
	public void obstacleCollisionCheck(FallingImage<I> go) {
		boolean entityHasYCollision = false;
		go.setTouchesWall(0);
		go.setCanClimbUp(-1); // reset
		go.setCanClimbDown(-1);
		for (LevelBlock<I> c: climbables) {
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
				System.out.println(go.getCanClimbUp());
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
//		for (GameObject<I> i: items) {
//			if(i instanceof Coin) {
//				return false;
//			}
//		}
//		System.out.println("game finished");
		return false;
	}
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
					player.setSpeed(2.5);
					break;
				case VK_S:
					player.setClimbing(1);
					break;
				case VK_L:
					((Door<I>)items.get(items.size()-1)).setOpen(false);
					break;
				case VK_O:
					((Door<I>)items.get(items.size()-1)).setOpen(true);
					break;
				default:
			}
			if(levelbuilder > -1) {
				switch(keycode) {
					case NUM_2: // mv down
						blocks.get(levelbuilder).getPos().move(new Vertex(0,8*3));
						break;
					case NUM_4: // mv left
						blocks.get(levelbuilder).getPos().move(new Vertex(-8*3,0));
						break;
					case NUM_6: // mv right
						blocks.get(levelbuilder).getPos().move(new Vertex(8*3,0));
						break;
					case NUM_8: // mv up
						blocks.get(levelbuilder).getPos().move(new Vertex(0,-8*3));
						break;
					case VK_Y: // generate new fg
						blocks.add(new LevelBlock<>(0, blocks.get(blocks.size() - 1).getPos().mult(1 / 3.0),levelbuilder < blocks.size() ? blocks.get(levelbuilder).getCurrentAnimationFrame() : 0));
						levelbuilder = blocks.size()-1;
						break;
					case VK_X: // generate fg2
						blocks.add(new LevelBlock<>(1, blocks.get(blocks.size() - 1).getPos().mult(1 / 3.0),levelbuilder < blocks.size() ? blocks.get(levelbuilder).getCurrentAnimationFrame() : 0));
						levelbuilder = blocks.size()-1;
						break;
					case VK_C: // generate new bg
						blocks.add(new LevelBlock<>(2, blocks.get(blocks.size() - 1).getPos().mult(1 / 3.0),levelbuilder < blocks.size() ? blocks.get(levelbuilder).getCurrentAnimationFrame() : 0));
						levelbuilder = blocks.size()-1;
						break;
					case NUM_3: // prev Frame
						blocks.get(levelbuilder).setCurrentAnimationFrame(blocks.get(levelbuilder).getCurrentAnimationFrame()-1);
						break;
					case NUM_9: // next frame
						blocks.get(levelbuilder).setCurrentAnimationFrame(blocks.get(levelbuilder).getCurrentAnimationFrame()+1);
						break;
					case NUM_1: // prev Block
						levelbuilder--;
						break;
					case NUM_7: // next Block
						levelbuilder = Math.min(levelbuilder+1,blocks.size()-1);
						break;
					case NUM_0: // delete
						blocks.remove(levelbuilder);
						levelbuilder--;
						break;
					case NUM_5: // export
						int counter = 0;
						// List<LevelBlock<I>> allBlocks
						// TODO summarize
						for (LevelBlock<I> lb:blocks) {
							counter++;
							if(counter <= 216) continue; //border skip
							if((lb.gameObjectId-10) == 2){
								System.out.println("p.background.add(new LevelBlock<>(" + (lb.gameObjectId-10) + ", new Vertex("
										+ lb.getPos().x/3 + "," + lb.getPos().y/3 + "), " + lb.getCurrentAnimationFrame() + "));");
							}else if(lb.getCurrentAnimationFrame() >= 71 && lb.getCurrentAnimationFrame() <= 80 || lb.getCurrentAnimationFrame() >= 98){
								System.out.println("p.climbables.add(new LevelBlock<>(" + (lb.gameObjectId-10) + ", new Vertex("
										+ lb.getPos().x/3 + "," + lb.getPos().y/3 + "), " + lb.getCurrentAnimationFrame() + "));");
							}else if(lb.getCurrentAnimationFrame() >= 45 && lb.getCurrentAnimationFrame() <= 54){
								System.out.println("p.pushables.add(new PushableBlock<>(" + (lb.gameObjectId-10) + ", new Vertex("
										+ lb.getPos().x/3 + "," + lb.getPos().y/3 + "), new Vertex(0, 0), " + lb.getCurrentAnimationFrame() + "));");
							}else {
								System.out.println("p.blocks.add(new LevelBlock<>(" + (lb.gameObjectId - 10) + ", new Vertex("
										+ lb.getPos().x / 3 + "," + lb.getPos().y / 3 + "), " + lb.getCurrentAnimationFrame() + "));");
							}
						}
						/*for (LevelBlock<I> lb:background) {
							if(lb.getCurrentAnimationFrame() == 101) continue;
							System.out.println("p.background.add(new LevelBlock<>(2, new Vertex("
									+ lb.getPos().x + "," + lb.getPos().y + "), " + lb.getCurrentAnimationFrame() + "));");
						}*/
						//System.out.println("p.player.getPos().moveTo(new Vertex(" + player.getPos().x + "," + player.getPos().y + "));");
						levelbuilder = -1;
						break;
					case DOWN_ARROW:
						player.getPos().move(new Vertex(0,8));
						break;
					case UP_ARROW:
						player.getPos().move(new Vertex(0,-8));
						break;
					case LEFT_ARROW:
						player.getPos().move(new Vertex(-8,0));
						break;
					case RIGHT_ARROW:
						player.getPos().move(new Vertex(8,0));
						break;
				}
				System.out.println(levelbuilder);
			}
		}
	}
	@Override
	public void keyReleasedReaction(KeyCode keycode) {
		if (keycode != null) {
			switch (keycode) {
				case VK_W:
					player.setSpeed(1.25);
				case VK_S:
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

