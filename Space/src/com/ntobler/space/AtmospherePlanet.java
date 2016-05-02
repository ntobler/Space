package com.ntobler.space;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;

public class AtmospherePlanet extends Planet {

	//implement wind
	
	private double thickness;
	private double floorFrictionCoefficient;
	
	//private double windSpeed;	//rad per second

	public AtmospherePlanet() {
		super();
		this.thickness = 128;
		this.floorFrictionCoefficient = 1;
	}
	

	@Override
	protected void proximityReport(PhysicalWorkspace w, Physical trigger, double distance, double passedTime) {
		super.proximityReport(w, trigger, distance, passedTime);
		
		if (distance < thickness) {
			calcPhysical(trigger, passedTime);
		}
	}
	
	private void calcPhysical(Physical p, double passedTime) {
		
		double height = p.getPos().minus(getPos()).abs() - getRadius();
		
 		if (height < thickness) {
		
			double friction =  height / thickness * floorFrictionCoefficient;
		
			Complex deltaV = this.getVelocity().minus(p.getVelocity());
			Complex scaledDeltaV = deltaV.scalarMultiply(friction * passedTime);
			
			p.addVelocity(scaledDeltaV);
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
