package com.ntobler.space;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

public class Planet extends RotablePhysical implements Landable {
	
	public Planet() {
		
		this.setRotationSpeed(0.25);
		
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
	public void paintRotated(Graphics2D g2) {
		super.paintRotated(g2);
		
		g2.fill(CustomGraphics.circle(0, 0, radius));
		g2.setColor(Color.GRAY);
		g2.draw(new Line2D.Double(-20, 0, 20, 0));
		g2.draw(new Line2D.Double(0, -20, 0, 20));
		g2.setColor(Color.WHITE);
		
		getHitPointHolder().paintHitBar(g2);
	}

	protected void onCriticalDistancedReached(PhysicalWorkspace w, Physical trigger) {
		//trigger.destroy();
	}
	
	@Override
	protected void proximityReport(PhysicalWorkspace w, Physical trigger, double distance, double passedTime) {
		super.proximityReport(w, trigger, distance, passedTime);
		
		/*if (distance < 0) {
			//if (trigger.getVelocity() != null) {
			//	double deltaV = Orbit.getRadialVelocity(this, trigger);
				//System.out.println(deltaV);
			//	if (deltaV < 20) {
					trigger.destroy();
			//	}
			//}
		}*/
	}
	
	public void onDestroyed(PhysicalWorkspace w) {
		w.addFixedObject(new Explosion(this, 100));
	}
	
	public void setHitpoints(int hitpoints) {
		
	}

	@Override
	public void land(Physical p) {
		
		link(p);
	}

}
