package com.ntobler.space.physical;

import java.awt.Graphics2D;
import java.awt.geom.Line2D;

import com.ntobler.space.Complex;
import com.ntobler.space.Geometry;
import com.ntobler.space.Workspace;
import com.ntobler.space.utility.FuelTank;
import com.ntobler.space.utility.Thruster;
import com.ntobler.space.utility.Thruster.ThrusterListener;

public class AimMissile extends Missile {
	
	private static final double THRUST = 100;
	private static final double INITIAL_VELOCITY = 20;
	private static final double MASS = 1;
	private static final int DAMAGE = 30;
	
	private final FuelTank fuelTank;
	private final Thruster thruster;
	private Complex steerDir;
	
	private final Physical lockOn;
	
	public AimMissile (Physical origin, Physical lockOn, Complex launchDir) throws Exception {
	
		super(origin, launchDir.normalVector().scalarMultiply(INITIAL_VELOCITY), MASS);
	
		if (lockOn == null) {
			throw new Exception();
		}
		this.lockOn = lockOn;
		
		setDamage(DAMAGE);
		
		fuelTank = new FuelTank(1000);
		
		thruster = new Thruster(fuelTank);
		thruster.setThrust(THRUST);
		thruster.setListener(new Thruster.ThrusterListener() {
			@Override
			public void onRunOutOfFuel() {
				destroy();
			}
		});
	} 
	
	
	
	@Override
	public void tick(Workspace w, double passedTime, Complex mousePos) {	
		super.tick(w, passedTime, mousePos);
		
		Complex lockOnDir = Geometry.getDirection(this.getPos(), lockOn.getPos());
		
		double driftAngle = Geometry.getAngle(getVelocity(), lockOnDir);

		driftAngle -= Math.PI/2; // minus 90 degrees
		
		double deltaV = Math.cos(driftAngle) * getVelocity().abs();
		
		steerDir = lockOnDir.sub90deg().normalVector().scalarMultiply(deltaV).plus(lockOnDir.normalVector()).normalVector();

		thruster.tick(passedTime, steerDir, this);
	}
	
	
	
	@Override
	public void paintAbsolute(Graphics2D g2) {
	
		thruster.draw(g2);
		super.paintAbsolute(g2);
	}
	
	@Override
	public void onDestroyed(Workspace w) {
		w.addFixedObject(new Explosion(this, 50));
	}
}
