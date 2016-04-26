package com.ntobler.space;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

public class GroundAirMissile extends Missile {
	
	private static final double MAX_THRUST = 500;
	private static final double MASS = 1;
	private static final int DAMAGE = 50;
	
	private final Thruster thruster;
	private Complex steerDir;
	
	private final Physical origin;
	private final Physical lockOn;
	
	public GroundAirMissile (Physical origin, Physical lockOn) throws Exception {
	
		super(origin, new Complex(0, 0), MASS);
	
		this.origin = origin;
		
		Complex lockOnDir = Geometry.getDirection(this.getPos(), lockOn.getPos());
		Complex launchOffset = lockOnDir.normalVector().scalarMultiply(origin.radius);
		this.setPos(this.getPos().plus(launchOffset));
		
		this.lockOn = lockOn;
		
		setDamage(DAMAGE);
		
 		thruster = new Thruster();
		thruster.setFuel(1000);
		thruster.setThrust(MAX_THRUST);
		thruster.setListener(new Thruster.ThrusterListener() {
			@Override
			public void onRunOutOfFuel() {
				destroy();
			}
		});
	} 
	
	public void tick(PhysicalWorkspace w, double passedTime, Complex mousePos) {	
		super.tick(w, passedTime, mousePos);
		
		/*lockOnDir = Geometry.getDirection(this.getPos(), lockOn.getPos());
		double deltaV = Orbit.getRadialVelocity(this, origin);
		if(deltaV < -5) {*/
		
		Complex radialDir = Geometry.getDirection(this.getPos(), lockOn.getPos());
		Complex progradeDir = radialDir.sub90deg();
		double vRadial = Orbit.getRadialVelocity(this, lockOn);
		double vPrograde = Orbit.getProgradeVelocity(this, lockOn);
		
		double progradeFactor = 0.5;
		double radialFactor = 1;
		
		if (vPrograde > 50) {
			vPrograde = 50;
		}
		if (vPrograde < -50) {
			vPrograde = -50;
		}
		
		steerDir = radialDir.scalarMultiply(radialFactor).plus(progradeDir.scalarMultiply(progradeFactor)).normalVector();
		
		if(vRadial < -5) {
			thruster.setThrust(MAX_THRUST);
		}
		else {
			thruster.setThrust(0);
		}
		
		thruster.tick(passedTime, steerDir, this);
	}
	
	public void onDestroyed(PhysicalWorkspace w) {
		w.addFixedObject(new Explosion(this, 50));
	}
	
	@Override
	public void paintTranslated(Graphics2D g2) {
		super.paintTranslated(g2);
		
		g2.setPaint(Color.BLUE);
		Complex v = this.getVelocity();
		g2.draw(new Line2D.Double(0, 0, v.x, v.y));
		g2.setPaint(Color.RED);
		
		if (steerDir != null) {
			g2.draw(new Line2D.Double(0, 0, steerDir.x * 16, steerDir.y*16));
			g2.setPaint(Color.WHITE);
		}
	}
	
	@Override
	public void paintAbsolute(Graphics2D g2) {
	
		thruster.draw(g2);
		super.paintAbsolute(g2);
	} 
	
	

}
