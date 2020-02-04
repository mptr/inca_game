package name.panitz.game.framework;

import name.panitz.game.example.simple.Rect;

import java.util.List;

import static name.panitz.game.example.simple.SimpleGame.currentVP;
import static name.panitz.game.example.simple.SpriteGrid.*;

public class ImageObject<I> extends AbstractGameObject<I> {
	int animationSpeed = 4; // higher is slower, -1 = off
	String imageFileName;
	int gameObjectId;
	I img;
	Rect cutout = null;
	Vertex cutoutOffset = new Vertex(0,0); // offset from image to actual hitbox
	private double objectZoom = 3;
	private boolean changed = true;
	List<Rect> animationFrames;
	int currentAnimationFrame = 0;
	int animationFrameSkip = 0;
	boolean facing = false; // false = right, true = left
	public ImageObject(int gameObjectId, Vertex pos, Vertex motion, double objectZoom, int startFrame) {
		super(getListFromID(gameObjectId).get(startFrame).getWidth()*objectZoom, getListFromID(gameObjectId).get(startFrame).getHeight()*objectZoom, pos.mult(objectZoom), motion);
		this.gameObjectId = gameObjectId;
		this.objectZoom = objectZoom;
		this.currentAnimationFrame = startFrame;
		this.imageFileName = getFileNameFromID(gameObjectId);
		this.animationFrames = getListFromID(gameObjectId);
		this.cutout = getListFromID(gameObjectId).get(startFrame);
	}
	public ImageObject(int gameObjectId, Vertex pos, Vertex motion, double objectZoom) {
		this(gameObjectId, pos, motion, objectZoom, 0);
	}
	public ImageObject(int gameObjectId, Vertex pos, Vertex motion) {
		this(gameObjectId, pos, motion, 3, 0);
	}
	public double getObjectZoom(){return objectZoom;}
	public void setObjectZoom(double x){objectZoom=x;}
	public void setAnimationSpeed(int s){animationSpeed=s;}
	public void setCurrentAnimationFrame(int x){
		currentAnimationFrame=x;
		setImageFileName(imageFileName,animationFrames.get(currentAnimationFrame));
	}
	public int getCurrentAnimationFrame(){return currentAnimationFrame;}
	public int getAnimationFrameSkip(){return animationFrameSkip;}
	public void setFacing(boolean x) {facing=x;}
	public void setGameObjectId(int x){
		this.imageFileName = getFileNameFromID(x);
		this.animationFrames = getListFromID(x);
		this.cutoutOffset = getCutoutOffset(x);
		cutout = animationFrames.get(0);
		gameObjectId=x;
		changed = true;
		currentAnimationFrame = 0;
		animationFrameSkip = 0;
	}
	public void setImageFileName(String imageFileName, Rect co) {
		this.imageFileName = imageFileName;
		cutout = co;
		changed = true;
	}

	@Override
	public void paintTo(GraphicsTool<I> g) {
		animate();
		if (changed) {
			img = g.generateImage(imageFileName, this);
			changed = false;
		}
		if (null != img) {
			Rect tmpCo = cutout.flip(false,false);
			if(facing) tmpCo = tmpCo.flip(true, false);
			g.drawImage(img, currentVP.getV1().x + getPos().x + cutoutOffset.x*objectZoom*(facing?1:0), currentVP.getV1().y + getPos().y + cutoutOffset.y*objectZoom, Math.abs(tmpCo.getV1().x - tmpCo.getV2().x)*objectZoom, Math.abs(tmpCo.getV1().y - tmpCo.getV2().y)*objectZoom, (int) tmpCo.getV1().x, (int) tmpCo.getV1().y, (int) tmpCo.getV2().x, (int) tmpCo.getV2().y);
		}
	}
	public void animate() {
		animationFrameSkip++;
		if(animationFrameSkip > animationSpeed && animationSpeed != -1) {
			currentAnimationFrame++;
			if(currentAnimationFrame >= animationFrames.size()) {
				currentAnimationFrame = 0;
			}
			cutout = animationFrames.get(currentAnimationFrame);
			animationFrameSkip = 0;
		}
	}
}

