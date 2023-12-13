package name.panitz.game.framework.swing;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Font;
import javax.swing.ImageIcon;

import name.panitz.game.framework.Color;
import name.panitz.game.framework.GraphicsTool;
import name.panitz.game.framework.GameObject;

public class SwingGraphicsTool implements GraphicsTool<Image>{
  Graphics g;
	
  public SwingGraphicsTool(Graphics g) {
    this.g = g;
  }

  @Override
  public void drawImage(Image img, double x, double y, double width, double height) {
    g.drawImage(img, (int) x, (int) y, (int) width, (int) height, null);
  }

  @Override
  public void drawImage(Image img, double x, double y, double width, double height, int sx1, int sy1, int sx2, int sy2) {
    g.drawImage(img, (int) x, (int) y, (int) (width + x), (int) (height + y), sx1, sy1, sx2, sy2, null);
  }

  @Override
  public void drawRect(double x, double y, double w, double h) {
    g.drawRect((int)x, (int)y, (int)w, (int)h);
  }
  
  @Override
  public void fillRect(double x, double y, double w, double h) {
    g.fillRect((int)x, (int)y, (int)w, (int)h);
  }

  @Override
  public void drawOval(double x, double y, double w, double h) {
    g.drawOval((int)x, (int)y, (int)w, (int)h);		
  }

  @Override
  public void fillOval(double x, double y, double w, double h) {
    g.fillOval((int)x, (int)y, (int)w, (int)h);
  }

  @Override
  public void drawLine(double x1, double y1, double x2, double y2) {
    g.drawLine((int)x1,(int)y1, (int)x2, (int)y2); 
  }

  @Override
  public void drawString(double x,double y,int fontSize, String fontName, String text, Color c){
    g.setColor(new java.awt.Color(c.getRed(), c.getGreen(), c.getBlue()));
    g.setFont(new Font(fontName, Font.PLAIN, fontSize));
    g.drawString(text, (int)x, (int)y);
  }

  @Override
  public  Image generateImage(String name, GameObject<Image> go){
    ImageIcon image = new ImageIcon(getClass().getClassLoader().getResource(name));
    go.setWidth(image.getIconWidth());
    go.setHeight(image.getIconHeight());
    return image.getImage();
  }

  @Override
  public void setColor(int r, int gr, int b) {
    g.setColor(new java.awt.Color(r, gr, b));
  }
}

