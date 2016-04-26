package com.ntobler.space;

import java.awt.Graphics2D;

public class MovingObject {

	private Complex pos;
	private Complex velocity;
	
	private boolean destroyed;
	
	public MovingObject(Complex pos, Complex velocity) {
		this.pos = pos;
		this.velocity = velocity;
		this.destroyed = false;
	}
	
	public void tick(double passedTime) {
		
	}
	
	public void paint(Graphics2D g2) {
		
	}

	public Complex getPos() {
		return pos;
	}

	public void setPos(Complex pos) {
		this.pos = pos;
	}
	
	public Complex getVelocity() {
		return velocity;
	}

	public void setVelocity(Complex velocity) {
		this.velocity = velocity;
	}

	public boolean isDestroyed() {
		return destroyed;
	}

	protected void destroy() {
		this.destroyed = true;
	}
}
