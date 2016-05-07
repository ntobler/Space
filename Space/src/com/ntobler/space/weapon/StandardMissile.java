package com.ntobler.space.weapon;

import com.ntobler.space.Complex;
import com.ntobler.space.physical.AimMissile;
import com.ntobler.space.physical.Missile;
import com.ntobler.space.physical.Physical;

public class StandardMissile extends Weapon {
	
	protected static final String NAME = "XG-300"; 
	protected static final double MASS = 10;
	protected static final String DESCRIBTION = "Standard Air-Ground Missile";
	protected static final double FIRE_RATE = 5;

	public StandardMissile(int count) {
		super(count);
	}
	@Override
	protected Missile fire(Physical source, Physical target, Complex launchDir) throws Exception {
		return new AimMissile(source, target, launchDir);
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
