package com.ntobler.space;

public class HostilePlanet extends AtmospherePlanet{

	private static final double MISSILE_RANGE_RADIUS = 500;

	private Weapon weapon;
	private Physical firedMissile = null;
	
	public HostilePlanet() {
		super();
		
		weapon = new Weapon(Weapon.GROUND_AIR_MISSILE, 10, 5);
		
	}

	@Override
	protected void proximityReport(PhysicalWorkspace w, Physical trigger, double distance, double passedTime) {
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
				firedMissile = m;
			} catch (Exception e) {
				m = null;
			}
		}
		
		return m;
	}
	
}
