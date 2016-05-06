package com.ntobler.space.physical;

import com.ntobler.space.Workspace;
import com.ntobler.space.weapon.Weapon;

public class HostilePlanet extends AtmospherePlanet{

	private static final double MISSILE_RANGE_RADIUS = 1000;

	private Weapon weapon;
	private Physical firedMissile = null;
	
	public HostilePlanet() {
		super();
		weapon = new Weapon(Weapon.GROUND_AIR_MISSILE, 100, 2);
		
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
			w.addPhysical(launchGroundAirMissile(trigger));
		}
	}
	
	
	private Physical launchGroundAirMissile(Physical lockOn) {
		
		GroundAirMissile m = null;
		if ((lockOn != null) && weapon.isAvailable()) {
			try {
				m = new GroundAirMissile(this, lockOn);
				weapon.use();
				firedMissile = m;
			} catch (Exception e) {
				m = null;
			}
		}
		
		return m;
	}
	
}
