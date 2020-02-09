package name.panitz.game.framework;

import name.panitz.game.example.simple.SimpleGame;

import java.awt.*;

public class TextObject<I> extends AbstractGameObject<I> {
	public void setText(String text) {
		this.text = text;
	}
	private String text;
	public String fontName;
	public int fontSize;
	public Color color;
	private boolean fixed = true;
	public TextObject(Vertex position, String text, String fntName, int fntSize, Color color) {
		super(0, 0, position);
		this.text = text;
		this.color = color;
		this.fontName = fntName;
		this.fontSize = fntSize;
	}
	public TextObject(Vertex position, String text, String fntName, int fntSize, Color color, boolean fixed) {
		this(position, text, fntName, fntSize, color);
		this.fixed = fixed;
	}
	@Override
	public void paintTo(GraphicsTool<I> g) {
		if(fixed)
			g.drawString(getPos().x, getPos().y, fontSize, fontName, text, color);
		else
			g.drawString(getPos().x + SimpleGame.currentVP.getV1().x, getPos().y + SimpleGame.currentVP.getV1().y, fontSize, fontName, text, color);
	}
}

