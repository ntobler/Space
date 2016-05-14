package com.ntobler.space;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ntobler.space.physical.Physical;

public class ThrustAnimation {

	//Thruster
	final double dither = 0.2;
	final double particlePeriodScale = 0.001;
	final double particleVanishTimeScale = 0.02;
	final double particleVelocityScale = 0.05;
	final double radius = 3;


	private class Particle {
		Complex pos;
		Complex velocity;
		double vanishTime;
		double passedTimeSum;
		double radius;
		double fraction;
	}

	private double lastAddedTimeSum = 0;
	private List<Particle> particles;
	
	public ThrustAnimation() {
		particles = new ArrayList<Particle>();
	}
	
	public void tick(double timePassed, Complex direction, Physical physical, double thrust) {

		lastAddedTimeSum += timePassed;
		
		if (((thrust * particlePeriodScale) < lastAddedTimeSum) &&
			(thrust > 0)) {
			
			lastAddedTimeSum = 0;
		
			double ditherValue = 1 + ((Math.random() - 0.5) * dither);
			double velocity = thrust * particleVelocityScale * ditherValue;
			double deviation = (Math.random() - 0.5) * physical.getRadius();
			
			Particle p = new Particle();
			
			p.vanishTime = thrust * particleVanishTimeScale * ditherValue;
			p.passedTimeSum = 0;
			p.radius = radius;
			p.pos = physical.getPos().plus(direction.add90deg().normalVector().scalarMultiply(deviation));
			p.velocity = physical.getVelocity().plus(direction.normalVector().scalarMultiply(velocity));
			
 			particles.add(p);
		}

		Iterator<Particle> i = particles.iterator();
		while (i.hasNext()) {
			Particle p = i.next();
			
			p.passedTimeSum += timePassed;
			
	    	if (p.passedTimeSum > p.vanishTime) {
	    		i.remove();
	    	}
	    	else {
	    		p.pos = p.pos.plus(p.velocity.scalarMultiply(timePassed));
				p.fraction = 1 - (p.passedTimeSum / p.vanishTime);
	    	}
		}
	}

	public void draw(Graphics2D g2) {

 		for (Particle p: particles) {
			int alpha = (int) (p.fraction * 256);
	 		g2.setColor(new Color(255, 255, 255, alpha));
			g2.fill(CustomGraphics.circle(p.pos.x, p.pos.y, p.fraction * p.radius));
		}
 		g2.setPaint(Color.WHITE);
	}
	
}
