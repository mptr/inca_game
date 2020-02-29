package name.panitz.game.framework.gwt.client;

import name.panitz.game.framework.GraphicsTool;
import name.panitz.game.framework.GameObject;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.canvas.dom.client.CssColor;

import java.awt.*;

public class GWTGraphics implements GraphicsTool<ImageElement>{
  Context2d gc;
	
  public GWTGraphics(Context2d gc) {
    this.gc = gc;
  }
  @Override
  public void drawImage(ImageElement img, double x, double y, double width, double height, int sx1, int sy1, int sx2, int sy2) {
    gc.drawImage(img, x, y, width, height, sx1, sy1, sx2-sx1, sy2-sy1);
  }

  @Override
  public void drawLine(double x1, double y1, double x2, double y2) {
    gc.beginPath();
    gc.lineTo(x1,y1);
    gc.lineTo(x2,y2);
    gc.stroke();
  }

  @Override
  public void drawString(double x, double y, int fntsize, String fntName, String text, Color c) {
    gc.setFillStyle(CssColor.make("rgba("+c.getRed()+","+c.getGreen()+","+c.getBlue()+",1)"));
    gc.setFont(fntsize+"pt "+fntName);
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

