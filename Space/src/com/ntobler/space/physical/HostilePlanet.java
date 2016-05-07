package com.ntobler.space.physical;

import com.ntobler.space.Complex;
import com.ntobler.space.Workspace;
import com.ntobler.space.weapon.StandardDefenseMissile;
import com.ntobler.space.weapon.Weapon;

public class HostilePlanet extends AtmospherePlanet{

	private static final double MISSILE_RANGE_RADIUS = 1000;

	private Weapon weapon;
	
	public HostilePlanet() {
		super();
		weapon = new StandardDefenseMissile(100);
		
	}

	@Override
	protected void proximityReport(Workspace w, Physical trigger, double distance, double passedTime) {
		super.proximityReport(w, trigger, distance, passedTime);
		
		/*if (distance < 0) {
			if ((trigger.getVelocity() != null) &&
					(trigger != firedMissile)) {
				double deltaV = Orbit.getRadialVelocity(this, trigger);
				if (deltaV < 20) {
					trigger.destroy();
				}
			}
		}*/
		
		if (distance < MISSILE_RANGE_RADIUS) {
			try {
				w.addPhysical(weapon.use(this, trigger, Complex.ZERO));
			} catch (Exception e) {
			}
		}
	}
	
}
