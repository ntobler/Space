package com.ntobler.space.weapon;

import com.ntobler.space.Complex;
import com.ntobler.space.Geometry;
import com.ntobler.space.Space;
import com.ntobler.space.physical.Bullet;
import com.ntobler.space.physical.Missile;
import com.ntobler.space.physical.Physical;
import com.ntobler.space.ui.RadialMenu;
import com.ntobler.space.ui.RadialMenu.Listable;

public abstract class Weapon implements RadialMenu.Listable {
			
	protected static final String NAME = ""; 
	protected static final String DESCRIBTION = "";
	protected static final double FIRE_RATE = 0;
	
	private int count;
	private double lastFiredTime;
	
	public Weapon (int count) {
		this.count = count;
		this.lastFiredTime = Double.NEGATIVE_INFINITY;
	}
	
	public Missile use(Physical source, Physical target, Complex launchDir) throws Exception {
		
		if (isAvailable()) {
			count--;
			lastFiredTime = Space.getGameTime();
			return fire(source, target, launchDir);
		}
		else {
			return null;
		}
	}
	
	protected Missile fire(Physical source, Physical target, Complex launchDir) throws Exception {
		return null;
	}
	
	private boolean isAvailable() {
		
		boolean notEmpty = (count > 0);
		boolean bulletReady = Space.getGameTime() > (lastFiredTime + getFireRate());
		
		if (notEmpty && bulletReady) {
			return true;
		}
		return false;
	}

	public double getLoadFraction() {
		return 0.5;
	}
	
	@Override
	public String getName() {
		return NAME;	
	}

	@Override
	public String getDescription() {
		return DESCRIBTION;
	}
	
	protected double getFireRate() {
		return FIRE_RATE;
	}
}
