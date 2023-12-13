package name.panitz.game.framework;

import java.io.Serializable;

public class Rect implements Serializable {
	private Vertex v1;
	private Vertex v2;

	Rect(Vertex _v1, Vertex _v2) {
		v1 = _v1;
		v2 = _v2;
	}

	public Rect(double x1, double y1, double x2, double y2) {
		v1 = new Vertex(x1, y1);
		v2 = new Vertex(x2, y2);
	}
	public void move(Vertex mv) {
		v1.x += mv.x;
		v1.y += mv.y;
		v2.x += mv.x;
		v2.y += mv.y;
	}
	public void moveTo(Vertex dest) {
		move(v1.connection(dest));
	}
	public double getWidth() {return Math.abs(v2.x-v1.x);}
	public double getHeight() {return Math.abs(v2.y-v1.y);}
	public Vertex getV1() {
		return v1;
	}

	public Vertex getV2() {
		return v2;
	}
	public Rect flip(boolean vert, boolean hor) { // rechteck an der Achse spiegeln
		if(!vert && !hor) return this;
		Rect ret;
		if(vert && hor) {
			ret = new Rect(new Vertex(v2.x,v2.y),new Vertex(v1.x,v1.y));
		} else if(vert) {
			ret = new Rect(new Vertex(v2.x,v1.y),new Vertex(v1.x,v2.y));
		} else {
			ret = new Rect(new Vertex(v1.x,v2.y),new Vertex(v2.x,v1.y));
		}
		return ret;
	}

	@Override
	public String toString() {
		return "R(" + v1.toString() + "\t, " + v2.toString() + ")";
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == this) return true;
		if(obj == null) return false;
		if(obj.getClass() != this.getClass()) return false;
		Rect other = (Rect)obj;
		if(!other.v1.equals(this.v1)) return false;
		return other.v2.equals(this.v2);
	}
}
