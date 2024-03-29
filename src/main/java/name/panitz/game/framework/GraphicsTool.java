package name.panitz.game.framework;

public interface GraphicsTool<I> {
	void drawImage(I img, double x, double y, double width, double height);

	void drawImage(I img, double x, double y, double width, double height, int sx1, int sy1, int sx2, int sy2);

	void drawRect(double x, double y, double w, double h);

	void fillRect(double x, double y, double w, double h);

	void drawOval(double x, double y, double w, double h);

	void fillOval(double x, double y, double w, double h);

	void drawLine(double x1, double y1, double x2, double y2);

	void setColor(int red, int green, int blue);

	void drawString(double x, double y, int fntsize, String fntName, String text, Color c);

	default void drawString(double x, double y, int fontSize, String text, Color c) {
		drawString(x, y, fontSize, "Helvetica", text, c);
	}

	default void drawString(double x, double y, String text) {
		drawString(x, y, 20, "Helvetica", text, new Color(0x000000));
	}

	default void drawCircle(double x, double y, double w) {
		drawOval(x, y, w, w);
	}

	default void fillCircle(double x, double y, double w) {
		fillOval(x, y, w, w);
	}

	I generateImage(String name, GameObject<I> go);
//  I generateImage(String name, GameObject<I> go, int x1, int y1, int x2, int y2);
}

