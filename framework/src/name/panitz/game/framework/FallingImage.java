package name.panitz.game.framework;

public class FallingImage<I> extends ImageObject<I> {
	static double G = 9.81;
	double v0;
	int t = 0;
	public double speed = 1;
	public int isJumping = 0;
	private int canClimbUp = -1;
	private int canClimbDown = -1;
	int touchesWall = 0;
	public FallingImage(int gameObjectId, Vertex pos, Vertex motion, double objectZoom, int startFrame) {
		super(gameObjectId, pos, motion, objectZoom, startFrame);
		startJump(0);
	}
	public FallingImage(int gameObjectId, Vertex pos, Vertex motion, double objectZoom) {
		this(gameObjectId, pos, motion, objectZoom, 0);
	}
	public FallingImage(int gameObjectId, Vertex pos, Vertex motion) {
		this(gameObjectId, pos, motion, 3, 0);
	}
	public boolean getCanClimbUp() {
		return canClimbUp==1;
	}
	public boolean getCanClimbDown() {
		return canClimbDown==1;
	}
	public void setCanClimbUp(int ccu) {
		if(ccu == 1 && canClimbUp != -1) return; // set climb ok only if unset (-1)
		canClimbUp = ccu; // always set noClimbing & reset
	}
	public void setCanClimbDown(int ccd) {
		if(ccd == 1 && canClimbDown != -1) return;
		canClimbDown = ccd;
	}
	public void stop() {
		getPos().move(getVelocity().mult(-1.1));
		getVelocity().y = 0;
		isJumping = 0;
	}
	public void setTouchesWall(int x){
		touchesWall=x;
	}
	public void stop(double fPos) {
		getPos().moveTo(new Vertex(getPos().x,fPos-height));
		getVelocity().y = 0;
		isJumping = 0;
	}
	public void restart() {
		double oldX = getVelocity().x;
		getPos().move(getVelocity().mult(-1.1));
		getVelocity().x = -oldX;
		getVelocity().y = 0;
		isJumping = 0;
	}
	public Vertex getObjectCenter() {
		return new Vertex(position.x + width / 2, position.y + height / 2);
	}
	public void left() {
		if (isJumping > 0) {
			getVelocity().x = -1;
		}
	}
	public void right() {
		if (isJumping > 0) {
			getVelocity().x = +1;
		}
	}
	public void jump() {
		if(isJumping == 0) {
			startJump(-6);
		} else if(isJumping == 1) {
			if(touchesWall != 0 && getVelocity().y + Math.abs(getVelocity().x * speed * .5) > 1) {
				startJump(-6);
			} else {
				startJump(-4 + getVelocity().y + Math.abs(getVelocity().x * speed * .5));
			}
		}
	}

	public void startJump(double v0) {
		isJumping++;
		this.v0 = v0;
		t = 0;
	}
	@Override
	public void move() {
		if (isJumping > 0) {
			t++;
			getVelocity().y = v0 + G * t / 50;
		}
		getPos().move(getVelocity().mult(new Vertex(isJumping>0 ? (Math.max(speed - t * 0.01, 1.3)) : speed, 1)));
	}
}