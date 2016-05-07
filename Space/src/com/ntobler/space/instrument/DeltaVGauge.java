package com.ntobler.space.instrument;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import com.ntobler.space.Orbit;
import com.ntobler.space.physical.Physical;
import com.ntobler.space.physical.Ship;

public class DeltaVGauge extends Instrument{

	private double progradeVelocity;
	private double radialVelocity;
	
	private boolean visible = false;
	
	public void setShip(Ship ship) {
		this.ship = ship;
	}
	
	@Override
	public void update() {
		
		if(ship != null) {
		
			Physical lockOn = ship.getLockOn();
			
			if (lockOn != null) {
					
				
				double distance = ship.getPos().minus(lockOn.getPos()).abs();
				double rotationVelocity = lockOn.getRotationSpeed() * distance;
				
				
				//progradeVelocity = Orbit.getProgradeVelocity(ship, lockOn) - rotationVelocity;
				progradeVelocity = Orbit.getProgradeVelocity(ship, lockOn);
				radialVelocity = -Orbit.getRadialVelocity(ship, lockOn);
	
				visible = true;
				return;
			}
		}
		visible = false;
	}
	
	@Override
	public void draw(Graphics2D g2) {
		
		final double scaleFactor = 0.5;
		final double length = 100;
		
		if (visible) {
			
			
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
