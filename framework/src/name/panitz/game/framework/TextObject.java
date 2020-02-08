package name.panitz.game.framework;

import java.awt.*;

public class TextObject<I> extends AbstractGameObject<I> {
	private String text;
	public String fontName;
	public int fontSize;
	public Color color;

	public TextObject(Vertex position, String text, String fntName, int fntSize, Color color) {
		super(0, 0, position);
		this.text = text;
		this.color = color;
		this.fontName = fntName;
		this.fontSize = fntSize;
	}
	@Override
	public void paintTo(GraphicsTool<I> g) {
		g.drawString(getPos().x, getPos().y, fontSize, fontName, text, color);
		//g.drawString(getPos().x + SimpleGame.currentVP.getV1().x, getPos().y + SimpleGame.currentVP.getV1().x, fontSize, fontName, text);
	}
}

