package com.ntobler.space.weapon;

import com.ntobler.space.Complex;
import com.ntobler.space.physical.GroundAirMissile;
import com.ntobler.space.physical.Missile;
import com.ntobler.space.physical.Physical;

public class StandardDefenseMissile extends Weapon{

	protected static final String NAME = "GA-200"; 
	protected static final double MASS = 10;
	protected static final String DESCRIBTION = "Standard Ground-Air Missile";
	protected static final double FIRE_RATE = 5;

	public StandardDefenseMissile(int count) {
		super(count);
	}
	@Override
	protected Missile fire(Physical source, Physical target, Complex launchDir) throws Exception {
		return new GroundAirMissile(source, target);
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
