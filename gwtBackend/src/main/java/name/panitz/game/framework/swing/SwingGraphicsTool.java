package name.panitz.game.framework.swing;

import java.awt.*;
import java.util.Objects;
import javax.swing.ImageIcon;

import name.panitz.game.framework.GraphicsTool;
import name.panitz.game.framework.GameObject;

public class SwingGraphicsTool implements GraphicsTool<Image> {
	Graphics g;

	public SwingGraphicsTool(Graphics g) {
		this.g = g;
	}

	@Override
	public void drawImage(Image img, double x, double y, double width, double height, int sx1, int sy1, int sx2, int sy2) {
		g.drawImage(img, (int) x, (int) y, (int) (width + x), (int) (height + y), sx1, sy1, sx2, sy2, null);
	}


	@Override
	public void drawLine(double x1, double y1, double x2, double y2) {
		g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
	}

	@Override
	public void drawString(double x, double y, int fontSize, String fontName, String text, Color c) {
		g.setFont(new Font(fontName, Font.PLAIN, fontSize));
		g.setColor(c);
		g.drawString(text, (int) x, (int) y);
	}

	@Override
	public Image generateImage(String name, GameObject<Image> go) {
		ImageIcon image = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource(name)));
		go.setWidth(image.getIconWidth());
		go.setHeight(image.getIconHeight());
		return image.getImage();
	}
}

