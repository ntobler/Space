package com.ntobler.space;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ntobler.space.physical.Physical;
import com.ntobler.space.physical.Ship;
import com.ntobler.space.render.Camera;
import com.ntobler.space.render.Paintable;

public class Workspace implements Paintable {
	
	private double gravitationalConstant;

	private List<Physical> physicals;
	private List<Physical> physicalsToAdd;
	
	private List<MovingObject> movingObjects;
	
	private Ship ship;
	
	Camera camera;
	
	private double timeFactor;
	private int timeFactorTicks;
	private double time;
	
	public Workspace(Camera camera) {
		this.camera = camera;
		
		time = 0;
		timeFactor = 1;
		timeFactorTicks = 0;

		gravitationalConstant = 1e2;
		
		physicals = new ArrayList<Physical>();
		physicalsToAdd = new ArrayList<Physical>();
		movingObjects = new ArrayList<MovingObject>();
		
	}
	
	public void addPhysical(Physical p) {
		if (p != null) {
			physicalsToAdd.add(p);
		}
	}
	
	public void addFixedObject(MovingObject f) {
		if (f != null) {
			movingObjects.add(f);
		}
	}
	

	public void tick (double tickTime, Complex mousePos) {
		
		double passedTime = timeFactor * tickTime;
		time = time + passedTime;
		
		
		for (Physical p: physicals) {
			p.tick(this, passedTime, mousePos);
		}
		
		removeDestroyedPhysicals();
		addGeneratedPhysicals();
		
		for (Physical p: physicals) {
			p.moveToNewPos(passedTime);
		}
		
		for (Physical p: physicals) {
			p.moveLinked();
		}
		
		tickFixedObjects(passedTime);
		
	}
	
	private void removeDestroyedPhysicals() {
		
		Iterator<Physical> i = physicals.iterator();
		while (i.hasNext()) {
			Physical p = i.next();
			if (p.isDestroyed()) {
				p.onDestroyed(this);
		        i.remove();
			}
		}
	}
	
	private void addGeneratedPhysicals() {
		
		Iterator<Physical> j = physicalsToAdd.iterator();
		while (j.hasNext()) {
			Physical p = j.next();
	    	physicals.add(p);
	        j.remove();
		}
	}
	
	private void tickFixedObjects(double passedTime) {
		Iterator<MovingObject> i = movingObjects.iterator();
		while (i.hasNext()) {
			MovingObject f = i.next();
	    	f.tick(passedTime);
	    	if (f.isDestroyed()) {
	    		i.remove();
	    	}
		}
	}
	
	
	public List<Physical> getPhysicals() {
		return physicals;
	}
	
	public Physical getNearestPhysical(Complex pos) {
		
		double nearestDistance = Double.POSITIVE_INFINITY;
		Physical nearestPhysical = null;
		
		for (Physical p: physicals) {
			double distance = p.getPos().minus(pos).abs();
			if (distance < nearestDistance) {
				nearestDistance = distance;
				nearestPhysical = p;
			}
		}
		
		return nearestPhysical;
	}
	
	public double getGetGravitationalConstant() {
		return gravitationalConstant;
	}

	public void setGetGravitationalConstant(double getGravitationalConstant) {
		this.gravitationalConstant = getGravitationalConstant;
	}

	@Override
	public void paint(Graphics2D g2) {
		
		AffineTransform normalTransform = g2.getTransform();
		
		AffineTransform gameTransform = camera.getTransformation();
    	g2.setTransform(gameTransform);
    	
    	for (Physical p: physicals) {
			p.paintAbsolute(g2);
		}	
		
		for (MovingObject m: movingObjects) {
			m.paint(g2);
		}
    	
    	g2.setTransform(normalTransform);
			
	}
	
	public Camera getCamera() {
		return camera;
	}
	
	public Ship getShip() {
		return ship;
	}

	public void setShip(Ship ship) {
		this.ship = ship;
	}

	@Override
	public void setImageDimension(Dimension dimension) {
	}
	
	public double getTime() {
		return time;
	}
	
	public void addTimeWarpTicks(int ticks) {
		timeFactorTicks = timeFactorTicks + ticks;
		timeFactor = Math.pow(1.41421, timeFactorTicks);
	}
	
	public double getTimeFactor() {
		return timeFactor;
	}
		
}
