package name.panitz.game.framework;

import java.awt.*;

public interface GraphicsTool<I> {
	void drawImage(I img, double x, double y, double width, double height, int sx1, int sy1, int sx2, int sy2);

	void drawLine(double x1, double y1, double x2, double y2);

	void drawString(double x, double y, int fntsize, String fntName, String text, Color c);

	I generateImage(String name, GameObject<I> go);
//  I generateImage(String name, GameObject<I> go, int x1, int y1, int x2, int y2);
}

