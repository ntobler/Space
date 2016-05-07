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
	
	public interface ThrusterListener {
		public void onRunOutOfFuel();
	}
	
	private ThrusterListener thrusterListener;
	
	public void setListener(ThrusterListener thrusterListener) {
		this.thrusterListener = thrusterListener;
	}
	
	public void tick (double passedTime, Complex steerDir, Physical p) {
		
		if (Double.isNaN(steerDir.x) || Double.isNaN(steerDir.y)) {
			steerDir = new Complex(0,0);
		}
		double deltaV = thrust * passedTime;
		
		double fuelQuantity = deltaV;
		
		try {
			fuelTank.use(fuelQuantity);
			p.addVelocity(steerDir.scalarMultiply(deltaV));
			thrustAnimaiton.tick(passedTime, steerDir, p, thrust);
		} catch (Exception e) {
			thrusterListener.onRunOutOfFuel();
		}
	}
	
	public void draw(Graphics2D g2) {
		thrustAnimaiton.draw(g2);
	}
	
	public double getThrust() {
		return thrust;
	}
	
	public void setThrust(double thrust) {
		this.thrust = thrust;
	}
	
}
