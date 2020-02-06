package name.panitz.game.example.simple;

import name.panitz.game.framework.*;
import name.panitz.game.framework.swing.SwingGame;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;

public class SimpleGame<I, S> extends AbstractGame<I, S> {
	// Objektlisten
	List<LevelBlock<I>> background = new ArrayList<>(); // no collisioncheck
	List<GameObject<I>> fixedFg = new ArrayList<>(); // fixed front elements
	List<LevelBlock<I>> blocks = new ArrayList<>(); // collisioncheck
	List<LevelBlock<I>> climbables = new ArrayList<>(); // ladders & ropes
	List<GameObject<I>> items = new ArrayList<>(); // collisioncheck
	List<PushableBlock<I>> pushables = new ArrayList<>();
	List<GameObject<I>> toDel = new ArrayList<>(); // list modifiers
	List<GameObject<I>> toAdd = new ArrayList<>();
	Player<I> player;
	public static boolean muteSound = true;
	public static final Vertex gameSize = new Vertex(37,18); // game size (# of 16px blocks in x and y)
	public static final Vertex windowSize = new Vertex(34*16*3,18*16*3); // game size (# of 16px blocks in x and y)
	public static Rect currentVP = new Rect(0,0, windowSize.x, windowSize.y);
	public SimpleGame() {
		// init with player
		super(new Player<>(new Vertex(0, 0), new Vertex(0, 0),2.9), windowSize.x, windowSize.y);
		player = (Player<I>) super.player;
		buttons.add(new Button("Reset Level", this::resetLvl));
		buttons.add(new Button("toggle Sound", this::toggleMute));
		resetLvl();
		// setup
		fixedFg.add(new TextObject<>(new Vertex(10, 30), "Stats", "DejaVu Sans Mono", 30));
		getGOss().add(background);
		getGOss().add(climbables);
		getGOss().add(items);
		getGOss().add(pushables);
		getGOss().add(blocks);
		getGOss().add(fixedFg);
	}
	public void toggleMute() {
		muteSound = !muteSound;
		System.out.println(muteSound);
	}
	public void pSound(String fname) {
		if(muteSound) return;
		playSound(new SoundObject<>(fname));
	}
	public void resetLvl() {
		System.out.println(currentVP);
		items.clear();
		blocks.clear();
		pushables.clear();
		player.setCollectedCoins(0);
		climbables.clear();
		background.clear();
		new LevelBuilder<I>().makeLvl(player, items, blocks, climbables, background, pushables, gameSize);
		pSound("pop.wav");
	}
	@Override
	public void doChecks() {
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
		for (GameObject<I> item: items) {
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
			}
			if(item instanceof Arrow) {
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
			}
		}
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
		} else if(player.getAnimationFrameSkip() == 0 && (player.getCurrentAnimationFrame() == 0 || player.getCurrentAnimationFrame() == 2)&& player.isJumping == 0 && player.getVelocity().y == 0 && Math.abs(player.getVelocity().x) > 0.5) {
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
			toAdd.add(new Coin<I>(player.getPos().mult(1/3.0),new Vertex((Math.random()-.5)*10,(Math.random()-.3)*5)));
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
				if(c.getCurrentAnimationFrame() < 98) {// rope
					go.setCanClimbUp(1);
				}
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

					break;
				default:
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
				default:
					;
			}
		}
	}
	public static void main(String[] args) {
		SwingGame.startGame(new SimpleGame<>());
	}
}

