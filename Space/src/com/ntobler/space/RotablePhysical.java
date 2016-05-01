package com.ntobler.space;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Iterator;

public class RotablePhysical extends Physical{

	private double rotationSpeed;
	private double rotationAngle;
	
	public RotablePhysical() {
		this.rotationAngle = 0;
		this.rotationSpeed = 0;
	}
	
	@Override
	public void tick(PhysicalWorkspace w, double passedTime, Complex mousePos) {
		super.tick(w, passedTime, mousePos);
		
		rotationAngle += rotationSpeed * passedTime;
	}
	
	@Override
	protected void updateLinked(Physical p) {
		
		double distance = this.radius + p.radius;
		Complex relativePos = Complex.normalFromAngle(p.getLinkingAngle() + this.getRotationAngle()).scalarMultiply(distance);
		p.setPos(this.getPos().plus(relativePos));
		double rotationVelocity = this.rotationSpeed * distance;
		Complex rotationVelocityVector = relativePos.add90deg().normalVector().scalarMultiply(rotationVelocity);
		p.setVelocity(this.getVelocity().plus(rotationVelocityVector));
	}
	
	@Override
	protected void paintTranslated(Graphics2D g2) {
		super.paintTranslated(g2);
		
		AffineTransform translatedTransform = g2.getTransform();
		
		AffineTransform rotatedTransform = new AffineTransform(translatedTransform);
		rotatedTransform.rotate(-rotationAngle);
    	g2.setTransform(rotatedTransform);
    	
    	paintRotated(g2);
    	
    	g2.setTransform(translatedTransform);
	}
	
	protected void paintRotated(Graphics2D g2) {
		
	}
	
	public double getRotationSpeed() {
		return rotationSpeed;
	}
	
	public void setRotationSpeed(double rotationSpeed) {
		this.rotationSpeed = rotationSpeed;
	}
	
	public double getRotationAngle() {
		return rotationAngle;
	}
	
	public void setRotationAngle(double rotationAngle) {
		this.rotationAngle = rotationAngle;
	}
	
	public void setRandomAngle() {
		this.rotationAngle = Math.PI * 2 * Math.random();
	}

	@Override
	public void link (Physical p) {
		super.link(p);
		
		p.setLinkingAngle(p.getLinkingAngle() - this.rotationAngle);
	}
	
}
