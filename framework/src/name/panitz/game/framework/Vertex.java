package name.panitz.game.framework;

public class Vertex {
	public double x;
	public double y;

	public Vertex(double x, double y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return "V(" + x + ", " + y + ")";
	}

	public void move(Vertex that) {
		x += that.x;
		y += that.y;
	}
	public void moveTo(Vertex that) {
		x = that.x;
		y = that.y;
	}
	public double dist() {
		return dist(new Vertex(0,0));
	}
	public double dist(Vertex other) {
		return Math.sqrt(Math.pow(other.x-this.x,2) + Math.pow(other.y-this.y,2)); // Entfernung der 2 Punkte
	}
	public Vertex connection(Vertex other) {
		return new Vertex(other.x - this.x, other.y - this.y); // Verbindungsvektor der 2 Punkte
	}
	public Vertex mult(double d) {
		return new Vertex(d * x, d * y);
	}
	public Vertex mult(Vertex d) {
		return new Vertex(d.x * x, d.y * y);
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == this) return true;
		if(obj == null) return false;
		if(obj.getClass() != this.getClass()) return false;
		Vertex other = (Vertex)obj;
		if(other.x != this.x) return false;
		return other.y == this.y;
	}
}

