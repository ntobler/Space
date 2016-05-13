package com.ntobler.space.utility;

import java.awt.Graphics2D;

import com.ntobler.space.Complex;
import com.ntobler.space.ThrustAnimation;
import com.ntobler.space.physical.Physical;

public class Thruster {

	private FuelTank fuelTank;
	
	private double thrust;
	
	private ThrustAnimation thrustAnimaiton;
	
	public Thruster (FuelTank fuelTank) {
		this.fuelTank = fuelTank;
		this.thrust = 0;
		thrustAnimaiton = new ThrustAnimation();
	}

	public void tick (double passedTime, Complex steerDir, Physical p) throws Exception {
		
		if (Double.isNaN(steerDir.x) || Double.isNaN(steerDir.y)) {
			steerDir = new Complex(0,0);
		}
		double deltaV = thrust * passedTime;
		
		double fuelQuantity = deltaV;
		
		fuelTank.use(fuelQuantity);
		p.addVelocity(steerDir.scalarMultiply(deltaV));
		thrustAnimaiton.tick(passedTime, steerDir, p, thrust);
	}
	
	public void draw(Graphics2D g2) {
		thrustAnimaiton.draw(g2);
	}
	
	public double getThrust() {
		return thrust;
	}
	
	public double getThrustFraction() {
		return thrust/150;
	}
	
	public void setThrust(double thrust) {
		this.thrust = thrust;
	}
	
}
