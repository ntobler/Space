package com.ntobler.space;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;

public class Ship extends RotablePhysical {

	private final double MAX_FUEL = 10000;
	private final double RADIUS = 6;
	
	private Complex steerDir;
	
	private Physical lockOn;
	
	private Weapon weapon;
	
	private Thruster thruster;
	
	public Ship () {
		
		setRadius(RADIUS);
		
		steerDir = new Complex(1, 0);
		
		thruster = new Thruster();
		thruster.setFuel(MAX_FUEL);
		thruster.setListener(new Thruster.ThrusterListener() {
			@Override
			public void onRunOutOfFuel() {
				destroy();
			}
		});
		
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
	public void tick(PhysicalWorkspace w, double passedTime, Complex mousePos) {
		super.tick(w, passedTime, mousePos);
		
		
		
		steerDir = Geometry.getDirection(this.getPos(), mousePos);
		
		if (Double.isNaN(steerDir.x) || Double.isNaN(steerDir.y)) {
			steerDir = new Complex(0,0);
		
		}
		
		setRotationAngle(steerDir.getAngle());
		
		thruster.tick(passedTime, steerDir, this);
	}
	
	@Override
	protected void proximityReport(PhysicalWorkspace w, Physical trigger, double distance, double passedTime) {
		super.proximityReport(w, trigger, distance, passedTime);
		
		if (distance < 0) {
			land(trigger);
		}
		
		
	}
	
	private void land(Physical p) {
		if (p instanceof Landable) {
			if (!isLinked()) {
				((Landable) p).land(this);
			}
		}
	}
	
	@Override
	public void onDestroyed(PhysicalWorkspace w) {
		w.addFixedObject(new Explosion(this, 50));
		w.setShip(null);
	}

	@Override
	public void paintRotated(Graphics2D g2) {
		super.paintRotated(g2);
		
		double r  = 10;
     	g2.fill(CustomGraphics.circle(0, 0, getRadius()));	
		g2.draw(new Line2D.Double(0,0, 0, r));
		
		drawLandStrut(g2);
	}
	
	private void drawLandStrut(Graphics2D g2) {
		
		Path2D path = new Path2D.Double();
		path.moveTo(-6, -6);
		path.lineTo(-4, -6);
		path.lineTo(0, 0);
		path.lineTo(4, -6);
		path.lineTo(6, -6);
		g2.draw(path);
	}
	
	@Override
	public void paintAbsolute(Graphics2D g2) {
		
		thruster.draw(g2);
		super.paintAbsolute(g2);
	}
	
	public double getFuelFraction() {
		return thruster.getFuel()/MAX_FUEL;
	}
	
	public void setThrust(double thrust) {
		thruster.setThrust(thrust);
	}
	
	public void setLockOn(Physical lockOn) {
		this.lockOn = lockOn;
	}
	
	public Physical getLockOn() {
		return lockOn;
	}

	public Physical shoot(Complex pos) {
		switch (weapon.getId()) {
		case Weapon.GUN:
			return shootGun(pos);
		case Weapon.AIM_MISSILE:
			return launchAimMissile(pos);
		case Weapon.BOMB:
			return null;
		}
		return null;
	}
	
	private Physical launchAimMissile(Complex pos) {
		
		AimMissile a = null;
		if ((lockOn != null) && weapon.isAvailable()) {
			Complex launchDir = Geometry.getDirection(this.getPos(), pos);
			try {
				a = new AimMissile(this, lockOn, launchDir);
			} catch (Exception e) {
			}
		}
		
		return a;
	}
	
	private Physical shootGun(Complex pos) {
		
		Missile m = null;
		if (weapon.isAvailable()) {
			Complex launchDir = Geometry.getDirection(this.getPos(), pos);
			try {
				m = new Bullet(this, launchDir);
			} catch (Exception e) {
			}
		}

		return m;		
	}

	public Weapon getWeapon() {
		return weapon;
	}

	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}
	
	
}
