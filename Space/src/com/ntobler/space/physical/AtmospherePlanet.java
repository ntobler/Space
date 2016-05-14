package com.ntobler.space.physical;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;

import com.ntobler.space.Complex;
import com.ntobler.space.CustomGraphics;
import com.ntobler.space.Geometry;
import com.ntobler.space.Workspace;

public class AtmospherePlanet extends Planet {

	//implement wind
	
	private double thickness;
	private double windSpeed;
	private double floorFrictionCoefficient;
	
	//private double windSpeed;	//rad per second

	public AtmospherePlanet() {
		super();
		this.thickness = 64;
		this.floorFrictionCoefficient = 1;
		
		this.windSpeed = 20;
	}
	

	@Override
	protected void proximityReport(Workspace w, Physical trigger, double distance, double passedTime) {
		super.proximityReport(w, trigger, distance, passedTime);
		
		if (distance < thickness) {
			calcPhysical(trigger, passedTime);
		}
	}
	
	private void calcPhysical(Physical p, double passedTime) {
		
		double distance = p.getPos().minus(getPos()).abs();
		double height = distance - getRadius();
		
 		if (height < thickness) {
		
			double friction =  height / thickness * floorFrictionCoefficient;
		
			Complex deltaV = this.getVelocity().minus(p.getVelocity());		
			double atmosphereSpeed = getRotationSpeed() * distance;
			
			Complex atmosphereV = Geometry.getDirection(this.getPos(), p.getPos()).add90deg().scalarMultiply(atmosphereSpeed + windSpeed);
						
			p.addVelocity(deltaV.scalarMultiply(friction * passedTime));
			p.addVelocity(atmosphereV.scalarMultiply(friction * passedTime));
		}
	}
	
	@Override
	public void paintAbsolute(Graphics2D g2) {
		super.paintAbsolute(g2);
		
		final double x = getX();
		final double y = getY();

		Point2D center = new Point2D.Double(x, y);
		float rAtmosphere = (float) (getRadius() + thickness);
		float[] dist = {(float) (getRadius() / rAtmosphere), 1.0f};
		Color[] colors = {new Color(255, 255, 255, 100), new Color(255, 255, 255, 0)};
		RadialGradientPaint paint = new RadialGradientPaint(center, rAtmosphere, dist, colors);
		
		g2.setPaint(paint);
		g2.fill(CustomGraphics.circle(x, y, getRadius() + thickness));
		g2.setPaint(Color.WHITE);
		
		super.paintAbsolute(g2);
	}
	
}
