package com.ntobler.space;
import java.awt.Graphics2D;

public class Planet extends Physical {
	
	public Planet() {
		
		setRadius(32);
		
		HitPointHolder hph = new HitPointHolder(100);
		setHitPointHolder(hph);
		hph.setListener(new HitPointHolder.HitPointListener() {

			@Override
			public void onDefeated() {
				destroy();
			}
			
		});	
		
	}
	
	@Override
	public void paintTranslated(Graphics2D g2) {
		super.paintTranslated(g2);
		
		g2.fill(CustomGraphics.circle(0, 0, radius));
		
		getHitPointHolder().paintHitBar(g2);
	}

	protected void onCriticalDistancedReached(PhysicalWorkspace w, Physical trigger) {
		trigger.destroy();
	}
	
	@Override
	protected void proximityReport(PhysicalWorkspace w, Physical trigger, double distance, double passedTime) {
		super.proximityReport(w, trigger, distance, passedTime);
		
		if (distance < 0) {
			//if (trigger.getVelocity() != null) {
			//	double deltaV = Orbit.getRadialVelocity(this, trigger);
				//System.out.println(deltaV);
			//	if (deltaV < 20) {
					trigger.destroy();
			//	}
			//}
		}
	}
	
	public void onDestroyed(PhysicalWorkspace w) {
		w.addFixedObject(new Explosion(this, 100));
	}
	
	public void setHitpoints(int hitpoints) {
		
	}

}
