package com.ntobler.space.render;

import java.awt.Dimension;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

import com.ntobler.space.Complex;
import com.ntobler.space.Geometry;
import com.ntobler.space.physical.Physical;
import com.ntobler.space.physical.Ship;

public class Camera {

	private Dimension screenDimension;
	private Focusable focus;
	
	private int zoomTicks;
	private double zoom;
	
	public Camera (Dimension screenDimension) {
		
		this.screenDimension = screenDimension;
		
		zoom = 1;
		zoomTicks = 0;
	}
	
	public AffineTransform getTransformation() {
		
		Point2D t;
		
		AffineTransform transform = new AffineTransform();

		t = getCenterVector();
		transform.translate(t.getX(), t.getY());
		
		transform.scale(zoom, zoom);
		
		transform.rotate(focus.getRotation());
		
		t = focus.getPosition();
		transform.translate(-t.getX(), -t.getY());
		
		return transform;
	}
	
	public AffineTransform getNoScaleNoRotationTransformation(Physical p) {
		
		AffineTransform transform = getTransformation();
		transform.translate(p.getPos().x, p.getPos().y);
		transform.rotate(-focus.getRotation());
		transform.scale(1/zoom, 1/zoom);
		
		return transform;
	}
	
	public AffineTransform getNoScaleTransformation(Physical p, Physical dir) {
		
		double angle = 0;
		
		if(dir != null) {
			angle = dir.getPos().minus(p.getPos()).getAngle();
		}
		
		AffineTransform transform = getTransformation();
		transform.translate(p.getPos().x, p.getPos().y);
		
		transform.scale(1/zoom, 1/zoom);
		
		return transform;
	}
	
	private Point2D getCenterVector() {
	
		Complex centerVector = new Complex (screenDimension.getWidth() / 2, screenDimension.getHeight() / 2);
		Point2D p = new Point2D.Double(centerVector.x, centerVector.y); 
		return p;
	}
	
	public Complex getGamePos(Complex mousePos) {
		
		Complex gamePos;
		
		Point2D mousePoint = new Point2D.Double(mousePos.x, mousePos.y);
		Point2D gamePoint;
		try {
			gamePoint = getTransformation().inverseTransform(mousePoint, null);
		} catch (NoninvertibleTransformException e) {
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
	
	public Focusable getFocus() {
		return focus;
	}

	public void setFocus(Focusable focus) {
		this.focus = focus;
	}
	
	public void setScreenDimension(Dimension screenDimension) {
		this.screenDimension = screenDimension;
	}
	
	public void setZoom(double zoom) {
		this.zoom = zoom;
	}
	
	public double getZoom() {
		return zoom;
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
