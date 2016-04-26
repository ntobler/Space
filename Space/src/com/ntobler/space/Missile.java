package com.ntobler.space;
import java.awt.Graphics2D;

public class Missile extends Physical {
	
	private final Physical origin;
	
	private static int damage;
	
	public Missile(Physical origin, Complex launchVelocity, double mass) throws Exception {
		
		this.origin = origin;
		
		Physical split = origin.split(mass, launchVelocity);
		
		setPos(split.getPos());
		setVelocity(split.getVelocity()); 
		setMass(split.getMass());
		setRadius(4);
		
		damage = 50;
	}
	
	@Override
	protected void proximityReport(PhysicalWorkspace w, Physical trigger, double distance, double passedTime) {
		super.proximityReport(w, trigger, distance, passedTime);
		if (distance < 0) {
			if (trigger != origin) {
				destroy();
				trigger.hit(damage);
			}
		}
	}
	
	@Override
	public void paintTranslated(Graphics2D g2) {
		super.paintTranslated(g2);
		
		g2.fill(CustomGraphics.circle(0, 0, getRadius()));
	}

	public static void setDamage(int damage) {
		Missile.damage = damage;
	}

}
