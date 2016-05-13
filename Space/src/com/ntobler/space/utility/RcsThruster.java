package com.ntobler.space.utility;

import com.ntobler.space.physical.RotablePhysical;

public class RcsThruster {
	
	private RotablePhysical rotable;
	
	private double targetAngle;
	
	
	private double MAX_THRUST = 10;	//rad per second^2
	private double MIN_THRUST = 1;
	
	
	public RcsThruster () {
		targetAngle = 0;
	}
	
	public void setRotable(RotablePhysical rotable) {
		this.rotable = rotable;
	}

	public void tick (double passedTime) {
	
		double angle = rotable.getRotationAngle();
		double rotationSpeed = rotable.getRotationSpeed();
		
		angle = normalizeAngle(angle);
		targetAngle = normalizeAngle(targetAngle);
		
		double rotationalAcceleration;
		
		double deltaAlpha = normalizeAngle(targetAngle - angle);
		
		double vMax = Math.sqrt(MAX_THRUST * Math.abs(deltaAlpha))*0.9;
		
		if ((rotationSpeed * deltaAlpha) > 0) {			//Approaching
			if (vMax > Math.abs(rotationSpeed)) { 		//Accelerating
				rotationalAcceleration = getAcceleration(deltaAlpha);
			}
			else {										//Breaking
				rotationalAcceleration = -getAcceleration(deltaAlpha);
			}
		}
		else {											//Getting away
			rotationalAcceleration = getAcceleration(deltaAlpha);
		}
			
		
		
		rotationSpeed += rotationalAcceleration * passedTime;
		angle += rotationSpeed * passedTime;
		
		rotable.setRotationAngle(angle);
		rotable.setRotationSpeed(rotationSpeed);
	}
	
	private double getAcceleration(double deltaAlpha) {
		
		double a = deltaAlpha* 100;
				
		if (a > 0) {
			if (a > MAX_THRUST)
				a = MAX_THRUST;
			else if (a < MIN_THRUST)
				a = MIN_THRUST;
		}
		else {
			if (a < -MAX_THRUST)
				a = -MAX_THRUST;
			else if (a > -MIN_THRUST)
				a = -MIN_THRUST;
		}
		return a;
	}
	
	private double normalizeAngle(double angle) {
		if (angle > (Math.PI)) angle -=  2*Math.PI;
		if (angle < (-Math.PI)) angle +=  2*Math.PI;
		return angle;
	}
	
	
	public void setTargetAngle(double angle) {
		this.targetAngle = angle;
	}

}
