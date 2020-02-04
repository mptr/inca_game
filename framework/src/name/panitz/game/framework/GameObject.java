package name.panitz.game.framework;

public interface GameObject<I> extends Movable, Paintable<I> {
	double getWidth();

	double getHeight();

	void setWidth(double w);

	void setHeight(double h);

	Vertex getPos();

	Vertex getVelocity();

	void setVelocity(Vertex v);


	default boolean isLeftOf(GameObject<?> that) {
		return this.getPos().x + this.getWidth() < that.getPos().x+7;
	}

	default boolean isRightOf(GameObject<?> that) {
		return that.isLeftOf(this);
	}


	default boolean isAbove(GameObject<?> that) {
		return getPos().y + getHeight() <= that.getPos().y+3;
	}
	default boolean isUnderneath(GameObject<?> that) {
		return that.isAbove(this);
	}

	default boolean touches(GameObject<?> that) {
		if (this.isLeftOf(that)) return false;
		if (that.isLeftOf(this)) return false;
		if (this.isAbove(that)) return false;
		if (that.isAbove(this)) return false;
		return true;
	}
	default boolean fallingOnTopOf(GameObject<?> that) {
		return !(isLeftOf(that) || isRightOf(that)) && getPos().y + getHeight() >= that.getPos().y + 3 && getPos().y + getHeight() < that.getPos().y + that.getHeight() && getVelocity().y >= 0.1;
	}
	default boolean standingOnTopOf(GameObject<?> that) {
		return !(isLeftOf(that) || isRightOf(that)) && getPos().y + getHeight() >= that.getPos().y && getPos().y + getHeight() < that.getPos().y + that.getHeight() && getVelocity().y == 0;
	}
	default boolean hitsLeftSideOf(GameObject<?> that) {
		if(isAbove(that) || isUnderneath(that)) return false;
		return getPos().x + getWidth() >= that.getPos().x && getPos().x + getWidth() < that.getPos().x + that.getWidth(); // left side:  p|obstacle
	}
	default boolean hitsRightSideOf(GameObject<?> that) {
		if(isAbove(that) || isUnderneath(that)) return false;
		return getPos().x <= that.getPos().x + that.getWidth() && getPos().x > that.getPos().x;	// right side:  obstacle|p
	}
	default boolean hitsBottomOf(GameObject<?> that) {
		return !(isLeftOf(that) || isRightOf(that)) && getPos().y <= that.getPos().y + that.getHeight() + 3 && getPos().y > that.getPos().y && getVelocity().y <= 0;
	}
	default void move() {
		getPos().move(getVelocity());
	}

	default double size() {
		return getWidth() * getHeight();
	}

	default boolean isLargerThan(GameObject<?> that) {
		return size() > that.size();
	}
}

