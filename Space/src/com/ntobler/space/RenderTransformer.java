package com.ntobler.space;

import java.awt.Dimension;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

public class RenderTransformer {

	private Dimension screenDimension;
	private Physical focus;
	private int zoomTicks = 0;
	private double zoom = 1;
	
	public RenderTransformer (Dimension screenDimension) {
		this.screenDimension = screenDimension;
	}
	
	public AffineTransform getTransformation() {
		
		Complex t;
		
		AffineTransform transform = new AffineTransform();
		
		
		t = getCenterVector();
		transform.translate(t.x, t.y);
		
		
		transform.rotate(getTransformRotation());
		
		t = getTransformVector();
		
		transform.scale(zoom, zoom);
		transform.translate(t.x, t.y);
		
		return transform;
	}
	
	public AffineTransform getNoScaleTransformation() {
		
		AffineTransform transform = getTransformation();
		Complex t = getTransformVector();
		transform.translate(-t.x, -t.y);
		transform.scale(1/zoom, 1/zoom);
		transform.rotate(-getTransformRotation());
		
		return transform;
	}
	
	public AffineTransform getNoScaleTransformation(Physical p, Physical dir) {
		
		double angle = dir.getPos().minus(p.getPos()).getAngle();
		
		AffineTransform transform = getTransformation();
		transform.translate(p.getPos().x, p.getPos().y);
		transform.scale(1/zoom, 1/zoom);
		transform.rotate(angle-getTransformRotation());
		
		return transform;
	}
	
	private Complex getTransformVector() {
		
		Complex centerVector = new Complex (screenDimension.getWidth() / 2, screenDimension.getHeight() / 2);
	
		Complex transformVector = Complex.ZERO;
		
		if (focus != null) {
			transformVector = focus.getPos().scalarMultiply(-1);
		}
		return transformVector;
	}
	
	private Complex getCenterVector() {
	
		Complex centerVector = new Complex (screenDimension.getWidth() / 2, screenDimension.getHeight() / 2);
		return centerVector;
	}
	
	private double getTransformRotation() {
		
		if (focus != null) {
			//if (focus.getClass().isAssignableFrom(Ship.class)) {
				Ship ship = (Ship) focus;
				Physical lockOn = ship.getLockOn();
				if (lockOn!= null) {
					return Geometry.getDirection(ship.getPos(), lockOn.getPos()).getAngle();
				}
				
			//}
		}
		
		return 0;
	}
	
	public Complex getGamePos(Complex mousePos) {
		
		Complex gamePos;
		
		Point2D mousePoint = new Point2D.Double(mousePos.x, mousePos.y);
		Point2D gamePoint;
		try {
			gamePoint = getTransformation().inverseTransform(mousePoint, null);
		} catch (NoninvertibleTransformException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			gamePoint = new Point2D.Double(0, 0);
		}
		
		gamePos = new Complex(gamePoint.getX(), gamePoint.getY());

		return gamePos;
	}
	
	public Complex getScreenPos(Complex gamePos) {
		
		Complex mousePos;
		
		Point2D gamePoint = new Point2D.Double(gamePos.x, gamePos.y);
		Point2D mousePoint;

		mousePoint = getTransformation().transform(gamePoint, null);
		
		mousePos = new Complex(mousePoint.getX(), mousePoint.getY());

		return mousePos;
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
