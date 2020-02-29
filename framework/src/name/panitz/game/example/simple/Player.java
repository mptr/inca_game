package name.panitz.game.example.simple;

import name.panitz.game.framework.FallingImage;
import name.panitz.game.framework.Vertex;

public class Player<I, S> extends FallingImage<I> {
	private int collectedCoins = 0;
	private int deathTimer = 0;
	private SimpleGame<I, S> parent = null;
	private int climbing = 0; // 0 = none, 1 = down, 2 = up
	Player(Vertex pos, Vertex motion, double objZoom) {
		super(1, pos, motion, objZoom);
	}
	public void setParent(SimpleGame<I, S> p) {
		parent = p;
	}
	Player(Vertex pos, Vertex motion) {
		super(1, pos, motion);
	}
	public void setClimbing(int c) {
		climbing = c;
	}
	public void setSpeed(double s) {
		speed = s;
		if(speed < 0.01) {
			setAnimationSpeed(-1);
		} else {
			setAnimationSpeed((int) (1 / s * 4));
		}
	}
	public int getDeathTimer() {
		return deathTimer;
	}
	public int getCollectedCoins() {
		return collectedCoins;
	}
	public void setCollectedCoins(int cc) {
		parent.coinDisplay.setText("Coins " + toRoman(cc));
		collectedCoins = cc;
	}
	public static String toRoman(int v){
		if(v<0||v>15) return String.valueOf(v);
		String[] tmp = {"-","I","II","III","IV","V","VI","VII","VIII","IX","X","XI","XII","XIII","XIV","XV"};
		return tmp[v];
	}
	public void kill() {
		deathTimer++;
	}
	@Override
	public void move() {
		if(deathTimer > 0) {
			deathTimer++;
			getPos().moveTo(new Vertex(0,-10000));
		}
		if(deathTimer >= 100) {
			deathTimer = 0;
		}
		if(climbing == 2 && getCanClimbUp()) {
			if(getVelocity().y > 0) {
				isJumping = 0;
				setVelocity(getVelocity().mult(new Vertex(1, (getVelocity().y > .1 ? .9 : 0))));
			} else {
				startJump(-1.5);
			}
		} else if(climbing == 1 && getCanClimbDown()) {
			isJumping = 0;
			setVelocity(getVelocity().mult(new Vertex(1,(getVelocity().y > .1 ? .9:0))));
		}
		super.move();
	}

	@Override
	public void animate() {
		if(getVelocity().x > 0) {
			setFacing(false);
		} else if(getVelocity().x < 0) {
			setFacing(true);
		}
		if(Math.abs(getVelocity().x) > 00.1)
			super.animate();
	}
}
