package com.ntobler.space;

import java.awt.Graphics2D;

public class Thruster {

	private double fuel;
	private double thrust;
	
	private boolean runOutOfFuel;
	
	private ThrustAnimation thrustAnimaiton;
	
	public Thruster () {
		this.thrust = 0;
		this.runOutOfFuel = false;
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
		
		if (!runOutOfFuel) {
		
			double deltaV = thrust * passedTime;
			
			if (fuel < deltaV) {
				deltaV = fuel;
				fuel = 0;
				runOutOfFuel = true;
				thrusterListener.onRunOutOfFuel();
			}
			else {
				fuel -= deltaV;
			}
			
			p.addVelocity(steerDir.scalarMultiply(deltaV));
		}
		
		thrustAnimaiton.tick(passedTime, steerDir, p, thrust);
	}
	
	public void draw(Graphics2D g2) {
		thrustAnimaiton.draw(g2);
	}
	
	public void setFuel(double fuel) {
		this.fuel = fuel;
	}

	public double getFuel() {
		return fuel;
	}
	
	public double getThrust() {
		return thrust;
	}
	
	public void setThrust(double thrust) {
		this.thrust = thrust;
	}
	
}
