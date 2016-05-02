package com.ntobler.space;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

public class DeltaVGauge {

	private double progradeVelocity;
	private double radialVelocity;
	
	private boolean lockOnPresent = false;
	
	public void tick (Ship ship) {
		
		Physical lockOn = ship.getLockOn();
		
		if (lockOn != null) {
				
			
			double distance = ship.getPos().minus(lockOn.getPos()).abs();
			double rotationVelocity = lockOn.getRotationSpeed() * distance;
			
			
			progradeVelocity = Orbit.getProgradeVelocity(ship, lockOn);// - rotationVelocity;
			//progradeVelocity = Orbit.getProgradeVelocity(ship, lockOn);
			radialVelocity = -Orbit.getRadialVelocity(ship, lockOn);

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
		
		
		
		//g2.draw(new Line2D.Double(-length, 0, length, 0));
		//g2.draw(new Line2D.Double(0, -length, 0, length));
		
		/*if (lockOnPresent) {
		
			pos = progradeVelocity * scaleFactor; 
			if (pos > length) pos = length;
			if (pos < -length) pos = -length;
			
			g2.setColor(Color.RED);
			g2.draw(new Line2D.Double(pos, -size, pos, size));
			g2.setColor(new Color(255,0,0,100));;
			if (pos > 0) {
				g2.fill(new Rectangle2D.Double(0, -size, pos, size*2));
			}
			else {
				g2.fill(new Rectangle2D.Double(pos, -size, -pos, size*2));
			}
			
			pos = radialVelocity * scaleFactor; 
			if (pos > length) pos = length;
			if (pos < -length) pos = -length;
			g2.setColor(Color.RED);
			g2.draw(new Line2D.Double(-size, pos, size, pos));
			g2.setColor(new Color(255,0,0,100));;
			if (pos > 0) {
				g2.fill(new Rectangle2D.Double(-size, 0, size*2, pos));
			}
			else {
				g2.fill(new Rectangle2D.Double(-size, pos, size*2, -pos));
			}
		}*/
		
		if (lockOnPresent) {
			
			
			double rad = radialVelocity * scaleFactor; 
			if (rad > length) rad = length;
			if (rad < -length) rad = -length;
			
			double pro = progradeVelocity * scaleFactor;
			if (pro > length) pro = length;
			if (pro < -length) pro = -length;
			
			g2.setColor(Color.RED);
			g2.draw(new Line2D.Double(0, rad, pro, rad));
			g2.draw(new Line2D.Double(pro, 0, pro, rad));
			
			double x, y, width, height;
			
			
			if (rad > 0) {
				y = 0;
				height = rad;
			}
			else{
				y = rad;
				height = -rad;
			}
			
			if (pro > 0) {
				x = 0;
				width = pro;
			}
			else{
				x = pro;
				width = -pro;
			}
			
			g2.setColor(new Color(255,0,0,100));
			g2.fill(new Rectangle2D.Double(x, y, width, height));
		}
		
	}
	
	
}
