package name.panitz.game.framework.gwt.client;

import name.panitz.game.framework.Color;
import name.panitz.game.framework.GraphicsTool;
import name.panitz.game.framework.GameObject;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.canvas.dom.client.CssColor;

public class GWTGraphics implements GraphicsTool<ImageElement>{
  Context2d gc;
	
  public GWTGraphics(Context2d gc) {
    this.gc = gc;
  }

  @Override
  public void drawImage(ImageElement img, double x, double y, double width, double height) {
    gc.drawImage(img, (int) x, (int) y, (int) width, (int) height);
  }

  @Override
  public void drawImage(ImageElement img, double x, double y, double width, double height, int sx1, int sy1, int sx2, int sy2) {
    gc.drawImage(img, (int) x, (int) y, (int) (width + x), (int) (height + y), sx1, sy1, sx2, sy2);
  }

  @Override
  public void drawRect(double x, double y, double w, double h) {
    gc.rect(x, y, w, h);
  }

  @Override
  public void fillRect(double x, double y, double w, double h) {
    gc.fillRect(x, y, w, h);
  }

  @Override
  public void drawOval(double x, double y, double w, double h) {
    // TODO 
  }

  @Override
  public void fillOval(double x, double y, double w, double h) {
    // TODO 
  }

  @Override
  public void drawLine(double x1, double y1, double x2, double y2) {
    gc.beginPath();
    gc.lineTo(x1,y1);
    gc.lineTo(x2,y2);
    gc.stroke();
  }

  
  public void setColor(int r ,int g ,int b){
    gc.setFillStyle(CssColor.make("rgba("+r+","+g+","+b+",1)"));
  }

  @Override
  public void drawString(double x,double y,int fontSize, String fontName, String text, Color c){
    this.setColor(c.getRed(), c.getGreen(), c.getBlue());
    gc.setFont(fontSize+"pt "+fontName);
    gc.fillText(text, x, y);
  }

  @Override
  public ImageElement generateImage(String name,GameObject<ImageElement> go){
    Image image = new Image(name);
    image.addLoadHandler(new LoadHandler() {			
       @Override
       public void onLoad(LoadEvent event) {
         go.setWidth(image.getWidth());							
         go.setHeight(image.getHeight());
         RootLayoutPanel.get().remove(image);
      }
    });
	
    ImageElement imgel = ImageElement.as(image.getElement());
    RootLayoutPanel.get().add(image);
    return imgel;
  }
}

