package com.ntobler.space;

import java.awt.Dimension;
import java.awt.geom.AffineTransform;

public class RenderTransformer {

	private Dimension screenDimension;
	private Physical focus;
	private int zoomTicks = 0;
	private double zoom = 1;
	
	public RenderTransformer (Dimension screenDimension) {
		this.screenDimension = screenDimension;
	}
	
	public AffineTransform getTransformation() {

		Complex t = getTransformVector();
		
		AffineTransform transform = null;
		
		if (t != null) {
			transform = new AffineTransform();
	        transform.translate(t.x, t.y);
	        transform.scale(zoom, zoom);
		}
		
		return transform;
	}
	
	public Complex getTransformVector () {
		
		Complex centerVector = new Complex (screenDimension.getWidth() / 2, screenDimension.getHeight() / 2);
	
		Complex transformVector = null;
		
		if (focus != null) {
			transformVector = centerVector.minus(focus.getPos().scalarMultiply(zoom));
		}
		
		return transformVector;
	}
	
	public Complex getGamePos(Complex mousePos) {
		
		Complex gamePos;
		
		if (focus != null) {
			gamePos =  mousePos.minus(getTransformVector()).scalarDivide(zoom);
		}
		else {
			gamePos = mousePos;
		}
		return gamePos;
	}
	
	public Complex getScreenPos(Complex gamePos) {
		
		Complex screenPos;
		
		if (focus != null) {
			screenPos =  gamePos.scalarMultiply(zoom).plus(getTransformVector());
		}
		else {
			screenPos = gamePos;
		}
		
		return screenPos;
	}
	
	public double getZoom() {
		return zoom;
	}
	
	public Physical getFocus() {
		return focus;
	}

	public void setFocus(Physical focus) {
		this.focus = focus;
	}
	
	public void setScreenDimension(Dimension screenDimension) {
		this.screenDimension = screenDimension;
	}
	
	public void addZoomTicks(int ticks) {
		this.zoomTicks += ticks;
		zoom = Math.pow(10, ((double) zoomTicks) / 10);
	}
	
	public void setZoomTicks(int ticks) {
		this.zoomTicks = ticks;
		zoom = Math.pow(10, ((double) zoomTicks) / 10);
	}
	
	
	
}
