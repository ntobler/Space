package com.ntobler.space.physical;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ntobler.space.Complex;
import com.ntobler.space.Geometry;
import com.ntobler.space.Orbit;
import com.ntobler.space.Workspace;
import com.ntobler.space.utility.HitPointHolder;

public class Physical {

	private Complex oldPos;
	private Complex pos;
	private Complex newPos;
	
	private Complex velocity;
	private Complex newVelocity;
	
	protected double mass;
	
	protected double radius;
	
	private boolean destroyed;
	
	private HitPointHolder hitPointHolder;
	
	private List<Physical> linkedPhysicals;
	private boolean linked;
	private double linkingAngle;
	
	
	public Physical() {
		this.pos = null;
		this.velocity = null;
		this.destroyed = false;
		this.newVelocity = Complex.ZERO;
		
		this.linked = false;
		this.linkedPhysicals = new ArrayList<Physical>();
		
	}
	
	public Physical(Complex pos) {
		
		this.pos = pos;
		this.setVelocity(Complex.ZERO);
		this.destroyed = false;
		this.newVelocity = Complex.ZERO;
	}
	
	public Physical split(double mass, Complex velocity) throws Exception {
		
		if (mass > this.mass) {
			throw new Exception("Too much mass split");
		}
		Physical split = new Physical();
		this.mass -= mass;
		split.mass = mass;
		
		split.setPos(this.getPos());
		
		//Complex velocityNormalVector = this.getVelocity().normalVector();
		split.setVelocity(this.getVelocity().plus(velocity));
		this.addVelocity(velocity.scalarMultiply(0 - (split.mass / this.mass)));
		
		return split;
	}
	
	public void tick(Workspace w, double passedTime, Complex mousePos) {
		
		if (velocity != null) {
			
			for (Physical p: w.getPhysicals()) {
				if (p == this) continue;
				
				
				Complex rVector = p.pos.minus(pos);
				double r = rVector.abs();
				proximityReport(w, p, r - p.radius - this.radius, passedTime);
				
				if (this.mass > 0) {
				
					Complex normalVector = rVector.normalVector();
					double f;
					if (r <= 1) {	//Prevent div by zero
						f = 0;
					}
					else {
						f = w.getGetGravitationalConstant() * this.mass * p.mass / (r*r);
					}
					Complex a = normalVector.scalarMultiply(f/mass);
					addVelocity(a.scalarMultiply(passedTime));
					
				}
			}
		}
	}
	
	public void moveToNewPos(double passedTime) {
		
		if (velocity != null) {
			velocity = newVelocity;
			oldPos = pos;
			pos = newPos.plus(velocity.scalarMultiply(passedTime));
			newPos = pos;
		}
	}
	
	public void moveLinked() {
		
		Iterator<Physical> j = linkedPhysicals.iterator();
		while (j.hasNext()) {
			Physical p = j.next();
			
			double radialVelocity = Orbit.getRadialVelocity(p, this);
			if (radialVelocity > 0) {
				p.linked = false;
				j.remove();
			}
			else {
				updateLinked(p);
			}
		}
	}
	
	protected void updateLinked(Physical p) {
		double distance = this.radius + p.radius;
		Complex relativePos = Complex.normalFromAngle(p.linkingAngle).scalarMultiply(distance);
		p.setPos(this.pos.plus(relativePos));
		p.setVelocity(this.velocity);
		
		if (p instanceof RotablePhysical) {
			((RotablePhysical) p).setRotationAngle(p.linkingAngle);
		}
	}

	
	protected void onCriticalDistancedReached(Workspace w, Physical trigger) {
		
	}
	
	protected void proximityReport(Workspace w, Physical trigger, double distance, double passedTime) {
		
	}
	
	protected void destroy() {
		this.destroyed = true;
		
	}
	

	protected void hit(int damage) {
		if (hitPointHolder != null) {
			hitPointHolder.hit(damage);
		}
	}
	
	public void onDestroyed(Workspace w) {
		
	}
	
	public boolean isDestroyed() {
		return destroyed;
	}

	public void paintAbsolute(Graphics2D g2) {
		
		AffineTransform absolutTransform = g2.getTransform();
		
		AffineTransform translatedTransform = new AffineTransform(absolutTransform);
		translatedTransform.translate(pos.x, pos.y);
    	g2.setTransform(translatedTransform);
    	
    	paintTranslated(g2);
    	
    	g2.setTransform(absolutTransform);
	}

	protected void paintTranslated(Graphics2D g2) {
		
	}
	
	public double getX() {
		return pos.x;
	}
	
	public double getY() {
		return pos.y;
	}
	
	public Complex getPos() {
		return pos;
	}

	public void setPos(Complex pos) {
		this.pos = pos;
		this.newPos = pos;
	}
	
	public void addPos(Complex pos) {
		this.newPos = this.newPos.plus(pos);
	}
	
	public Complex getVelocity() {
		return velocity;
	}

	public void setVelocity(Complex velocity) {
		this.velocity = velocity;
		this.newVelocity = velocity;
	}
	
	public void addVelocity(Complex velocity) {
		this.newVelocity = this.newVelocity.plus(velocity);
	}
	
	public void scaleVelocity(double factor) {
		this.newVelocity = this.newVelocity.scalarMultiply(factor);
	}
	
	public double getMass() {
		return mass;
	}

	public void setMass(double mass) {
		this.mass = mass;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}
	
	public HitPointHolder getHitPointHolder() {
		return hitPointHolder;
	}

	public void setHitPointHolder(HitPointHolder hitPointHolder) {
		this.hitPointHolder = hitPointHolder;
	}
	
	public void link (Physical p) {
		
			linkedPhysicals.add(p);
			p.linked = true;
			p.linkingAngle = Geometry.getDirection(this.pos, p.pos).getAngle();
	}
	
	public boolean isLinked() {
		return linked;
	}
	
	public double getLinkingAngle() {
		return linkingAngle;
	}

	public void setLinkingAngle(double linkingAngle) {
		this.linkingAngle = linkingAngle;
	}
	
	public double getRotationSpeed() {
		return 0;
	}
	
 }
