package com.ntobler.space;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

public class DeltaVGauge {

	private double progradeVelocity;
	private double radialVelocity;
	
	private boolean lockOnPresent = false;
	
	public void tick (Ship ship) {
		
		Physical lockOn = ship.getLockOn();
		
		if (lockOn != null) {
				
			
			double distance = ship.getPos().minus(lockOn.getPos()).abs();
			double rotationVelocity = lockOn.getRotationSpeed() * distance;
			
			
			progradeVelocity = Orbit.getProgradeVelocity(ship, lockOn) - rotationVelocity;
			//progradeVelocity = Orbit.getProgradeVelocity(ship, lockOn);
			radialVelocity = Orbit.getRadialVelocity(ship, lockOn);

			lockOnPresent = true;
		}
		else {
			lockOnPresent = false;
		}
		
		
	}
	
	public void paint(Graphics2D g2) {
		
		final double scaleFactor = 0.5;
		
		final double length = 100;
		final double size = 10;
		
		double pos;
		
		g2.setColor(Color.RED);
		
		g2.draw(new Line2D.Double(-length, 0, length, 0));
		g2.draw(new Line2D.Double(0, -length, 0, length));
		
		if (lockOnPresent) {
		
			pos = progradeVelocity * scaleFactor; 
			if (pos > length) pos = length;
			if (pos < -length) pos = -length;
			
			g2.draw(new Line2D.Double(pos, -size, pos, size));
			
			
			
			
			pos = radialVelocity * scaleFactor; 
			if (pos > length) pos = length;
			if (pos < -length) pos = -length;
			
			g2.draw(new Line2D.Double(-size, pos, size, pos));
		}
		
	}
	
	
}
