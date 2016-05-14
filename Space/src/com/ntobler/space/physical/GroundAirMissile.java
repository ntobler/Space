package com.ntobler.space.physical;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

import com.ntobler.space.Complex;
import com.ntobler.space.Geometry;
import com.ntobler.space.Orbit;
import com.ntobler.space.Workspace;
import com.ntobler.space.utility.FuelTank;
import com.ntobler.space.utility.Thruster;

public class GroundAirMissile extends Missile {
	
	private static final double MEAN_ACCELERATION = 100;
	
	private static final double MAX_THRUST = 100;
	private static final double MASS = 1;
	private static final int DAMAGE = 50;
	
	private final FuelTank fuelTank;
	private final Thruster thruster;
	private Complex steerDir;
	
	//private final Physical origin;
	private final Physical lockOn;
	
	//private Complex relativeHitPos;
	
	public GroundAirMissile (Physical origin, Physical lockOn) throws Exception {
	
		super(origin, new Complex(0, 0), MASS);
	
		if (lockOn == null) {
			throw new Exception();
		}
		
		this.lockOn = lockOn;
		//this.origin = origin;
		
		Complex lockOnDir = Geometry.getDirection(this.getPos(), lockOn.getPos());
		Complex launchOffset = lockOnDir.normalVector().scalarMultiply(origin.radius);
		this.setPos(this.getPos().plus(launchOffset));
		
		setDamage(DAMAGE);
		
		fuelTank = new FuelTank(2000);
		
		thruster = new Thruster(fuelTank);
		thruster.setMaxThrust(MAX_THRUST);
		thruster.setThrust(MAX_THRUST);
	} 
	
	@Override
	public void tick(Workspace w, double passedTime, Complex mousePos) {	
		super.tick(w, passedTime, mousePos);
		
		Complex lockOnVector = lockOn.getPos().minus(this.getPos());
		
		double lockOnVelocity = lockOn.getVelocity().minus(this.getVelocity()).abs();
		
		double hitTime = lockOnVelocity / MEAN_ACCELERATION;
		
		Complex a = lockOnVector.plus(lockOn.getVelocity().minus(this.getVelocity()).scalarMultiply(hitTime)).scalarDivide(hitTime * hitTime);
		
		steerDir = a.normalVector();
		
		double thrust = a.abs();
		
		thruster.setThrust(thrust);
	
		try {
			thruster.tick(passedTime, steerDir, this);
		} catch (Exception e) {
			destroy();
		}
		
		//Complex lockOnDistance = lockOn.getVelocity().scalarMultiply(hitTime);
		//relativeHitPos = lockOnVector.plus(lockOnDistance);
	}
	
	public void onDestroyed(Workspace w) {
		w.addFixedObject(new Explosion(this, 50));
	}
	
	@Override
	public void paintTranslated(Graphics2D g2) {
		super.paintTranslated(g2);
		
		/*g2.setPaint(Color.BLUE);
		Complex v = this.getVelocity();
		g2.draw(new Line2D.Double(0, 0, v.x, v.y));
		
		g2.setPaint(Color.RED);
		if (steerDir != null) {
			g2.draw(new Line2D.Double(0, 0, steerDir.x * 16, steerDir.y*16));
		}
		
		g2.setPaint(Color.GREEN);
		if (relativeHitPos != null) {
			g2.draw(new Line2D.Double(0, 0, relativeHitPos.x, relativeHitPos.y));
		}
		g2.setPaint(Color.WHITE);*/
		
	}
	
	@Override
	public void paintAbsolute(Graphics2D g2) {
	
		thruster.draw(g2);
		super.paintAbsolute(g2);
	} 
	
	

}
