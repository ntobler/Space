package com.ntobler.space.physical;

import com.ntobler.space.Complex;
import com.ntobler.space.Workspace;
import com.ntobler.space.Space;

public class Bullet extends Missile {

	
	private static final double INITIAL_VELOCITY = 500;//200;
	private static final double MASS = 1;
	private static final int DAMAGE = 2;

	private static final double LIFE_SPAN = 30;
	private final double timeSinceLaunch;
	
	public Bullet(Physical origin, Complex launchDir) throws Exception {
		
		super(origin, launchDir.normalVector().scalarMultiply(INITIAL_VELOCITY), MASS);
		
		setDamage(DAMAGE);
		
		this.timeSinceLaunch = 0;
		
		//this.addVelocity(launchDir.normalVector().scalarMultiply(SPEED));
	}
	
	@Override
	public void tick(Workspace w, double passedTime, Complex mousePos) {	
		super.tick(w, passedTime, mousePos);
		
		if (timeSinceLaunch > LIFE_SPAN) {
			this.destroy();
		}
	}
}
