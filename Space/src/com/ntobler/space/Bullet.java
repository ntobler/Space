package com.ntobler.space;

public class Bullet extends Missile {

	
	private static final double INITIAL_VELOCITY = 500;//200;
	private static final double MASS = 1;
	private static final int DAMAGE = 10;

	private static final double LIFE_SPAN = 30;
	private final double launchTime;
	
	public Bullet(Physical origin, Complex launchDir) throws Exception {
		
		super(origin, launchDir.normalVector().scalarMultiply(INITIAL_VELOCITY), MASS);
		
		setDamage(DAMAGE);
		
		this.launchTime = Space.getTime();
		
		//this.addVelocity(launchDir.normalVector().scalarMultiply(SPEED));
	}
	
	@Override
	public void tick(PhysicalWorkspace w, double passedTime, Complex mousePos) {	
		super.tick(w, passedTime, mousePos);
		
		if (Space.getTime() > (launchTime + LIFE_SPAN)) {
			this.destroy();
		}
	}
}
