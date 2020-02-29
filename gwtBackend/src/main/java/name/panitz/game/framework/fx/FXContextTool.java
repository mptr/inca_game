package name.panitz.game.framework.fx;

import name.panitz.game.framework.GraphicsTool;
import name.panitz.game.framework.GameObject;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.text.Font;

import java.awt.*;
import java.util.Objects;

public class FXContextTool implements GraphicsTool<Image> {
	GraphicsContext gc;

	public FXContextTool(GraphicsContext gc) {
		this.gc = gc;
	}

	public void drawImage(Image img, double x, double y, double width, double height, int sx1, int sy1, int sx2, int sy2) {
		gc.drawImage(img, x, y, width, height, sx1, sy1, sx2-sx1, sy2-sy1);
	}

	@Override
	public void drawLine(double x1, double y1, double x2, double y2) {
		gc.strokeLine(x1, y1, x2, y2);
	}


	@Override
	public void drawString(double x, double y, int fontSize, String fontName, String text, Color c) {
		gc.setFont(new Font(fontName, fontSize));
		gc.fillText(text, x, y);
	}

	@Override
	public Image generateImage(String name, GameObject<Image> go) {
		Image image = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(name)));
		go.setWidth(image.getWidth());
		go.setHeight(image.getHeight());
		return image;
	}
}
