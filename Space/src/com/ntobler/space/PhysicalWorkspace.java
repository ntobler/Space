package com.ntobler.space;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PhysicalWorkspace implements Paintable {

	//private float xSize;
	//private float ySize;
	
	private double getGravitationalConstant;

	
	
	private List<Physical> physicals;
	private List<Physical> physicalsToAdd;
	
	private List<MovingObject> movingObjects;
	
	private Ship ship;
	
	RenderTransformer renderTransformer;
	
	public PhysicalWorkspace(RenderTransformer renderTransformer) {
		this.renderTransformer = renderTransformer;

		getGravitationalConstant = 1e2;
		
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
	

	public void tick (double passedTime, Complex mousePos) {
		
		for (Physical p: physicals) {
			p.tick(this, passedTime, mousePos);
		}
		
		removeDestroyedPhysicals();
		addGeneratedPhysicals();
		
		for (Physical p: physicals) {
			p.moveToNewPos(passedTime);
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
		return getGravitationalConstant;
	}

	public void setGetGravitationalConstant(double getGravitationalConstant) {
		this.getGravitationalConstant = getGravitationalConstant;
	}

	@Override
	public void paint(Graphics2D g2) {
		// TODO Auto-generated method stub
		
		AffineTransform normalTransform = g2.getTransform();
		
		AffineTransform gameTransform = renderTransformer.getTransformation();
    	g2.setTransform(gameTransform);
    	
    	for (Physical p: physicals) {
			p.paintAbsolute(g2);
		}	
		
		for (MovingObject m: movingObjects) {
			m.paint(g2);
		}
    	
    	g2.setTransform(normalTransform);
			
	}
	
	public RenderTransformer getRenderTransformer() {
		return renderTransformer;
	}
	
	public Ship getShip() {
		return ship;
	}

	public void setShip(Ship ship) {
		this.ship = ship;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	

	

	

	
	
	
	
}
