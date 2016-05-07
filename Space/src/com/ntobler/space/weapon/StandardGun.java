package com.ntobler.space.weapon;

import com.ntobler.space.Complex;
import com.ntobler.space.Geometry;
import com.ntobler.space.Space;
import com.ntobler.space.physical.Bullet;
import com.ntobler.space.physical.Missile;
import com.ntobler.space.physical.Physical;

public class StandardGun extends Weapon{

	protected static final String NAME = "Standard Gun"; 
	protected static final double MASS = 10;
	protected static final String DESCRIBTION = "Standard automatic machine gun";
	protected static final double FIRE_RATE = 0.25;
	
	public StandardGun(int count) {
		super(count);
	}
	
	@Override
	protected Missile fire(Physical source, Physical target, Complex launchDir) throws Exception {
		return new Bullet(source, launchDir);
	}
	
	@Override
	public String getName() {
		return NAME;	
	}

	@Override
	public String getDescription() {
		return DESCRIBTION;
	}
	
	@Override
	protected double getFireRate() {
		return FIRE_RATE;
	}

}
