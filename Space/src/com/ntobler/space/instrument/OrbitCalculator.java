package com.ntobler.space.instrument;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import com.ntobler.space.Complex;
import com.ntobler.space.ControlPanel;
import com.ntobler.space.CustomGraphics;
import com.ntobler.space.Geometry;
import com.ntobler.space.Orbit;
import com.ntobler.space.Workspace;
import com.ntobler.space.physical.Physical;

public class OrbitCalculator extends Instrument {

	private Workspace workspace;
	
	private double progradeVelocity;
	private double radialVelocity;
	
	Complex roundOrbitVector;
	
	private boolean visible = false;
	
	public OrbitCalculator(Workspace workspace) {
		this.workspace = workspace;
	}
	
	
	@Override
	public void update() {
		
		if(ship != null) {
		
			Physical lockOn = ship.getLockOn();
			
			if (lockOn != null) {
					
				
				roundOrbitVector = Orbit.getRoundOrbitalDeltaV(ship, lockOn, workspace, Orbit.CLOCKWHISE); 
				roundOrbitVector = roundOrbitVector.plus(lockOn.getVelocity());
				
				double angle = Geometry.getDirection(lockOn.getPos(), ship.getPos()).getAngle();
				roundOrbitVector.addAngle(-angle);
				
				
				/*g2.drawString(String.format("Delta v:\t%.2f", roundOrbitVector.abs()), (int)shipPos.x + 10, (int)shipPos.y + 14);
				if (roundOrbitVector.abs() > 50) {
					roundOrbitVector = roundOrbitVector.normalVector().scalarMultiply(50);
				}
				CustomGraphics.drawVector(g2, shipPos.x, shipPos.y, roundOrbitVector, 1);
				*/
				
				progradeVelocity = roundOrbitVector.x;
				radialVelocity = roundOrbitVector.y;
				
				
				/*Physical p = new Physical();
				p.setPos(ship.getPos());
				p.setVelocity(ship.getVelocity().plus(roundOrbitVector));
				
				progradeVelocity = -Orbit.getProgradeVelocity(p, lockOn);
				radialVelocity = -Orbit.getRadialVelocity(p, lockOn);*/
	
				visible = true;
				return;
			}
		}
		visible = false;
	}
	
	@Override
	public void drawNormalOnShip(Graphics2D g2) {
		
		final double scaleFactor = 0.5;
		final double length = 50;
		final double radius = 3;
		
		if (visible) {
			
			g2.setColor(Color.BLUE);
			
			g2.setFont(ControlPanel.fontSmall);
			String str = String.format("\u0394v:\t%.2f", roundOrbitVector.abs());
			
			CustomGraphics.setStringAlign(CustomGraphics.HorizontalAlign.CENTER, CustomGraphics.VerticalAlign.TOP);
			CustomGraphics.drawAlignedString(g2, 0, 64 , str);

			if (roundOrbitVector.abs() > length) {
				roundOrbitVector = roundOrbitVector.normalVector().scalarMultiply(50);
			}

			
			g2.draw(new Line2D.Double(0, 0, roundOrbitVector.x, roundOrbitVector.y));
			

			Shape s = CustomGraphics.circle(roundOrbitVector.x, roundOrbitVector.y, radius);
			g2.setColor(new Color(0,0,255,100));
			g2.fill(s);
			g2.setColor(Color.BLUE);
			g2.draw(s);
			
		}
		
	}
	
}
