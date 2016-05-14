package com.ntobler.space.physical;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

import com.ntobler.space.Complex;
import com.ntobler.space.CustomGraphics;
import com.ntobler.space.Geometry;
import com.ntobler.space.Orbit;
import com.ntobler.space.Workspace;
import com.ntobler.space.render.Focusable;
import com.ntobler.space.utility.FuelTank;
import com.ntobler.space.utility.HitPointHolder;
import com.ntobler.space.utility.Thruster;
import com.ntobler.space.utility.RcsThruster;
import com.ntobler.space.weapon.Weapon;

public class Ship extends RotablePhysical implements Focusable {

	private final double MAX_FUEL = 10000;
	private final int HEALTH = 1000;
	private final double RADIUS = 6;
	
	private final double MAX_COLISION_V_RADIAL = 50;
	private final double MAX_COLISION_V_PROGRADE = 25;
	private final double COLISION_HIT_FACTOR = 5;
	
	private Complex steerDir;
	
	private Physical lockOn;
	
	private Weapon primaryWeapon;
	private Weapon secondaryWeapon;
	
	private FuelTank fuelTank;
	private Thruster thruster;
	
	private RcsThruster rcsThruster;
	
	public Ship () {
		setRadius(RADIUS);
		
		steerDir = new Complex(1, 0);
		
		fuelTank = new FuelTank(MAX_FUEL);
		
		thruster = new Thruster(fuelTank);
		thruster.setMaxThrust(150);
		
		rcsThruster = new RcsThruster();
		rcsThruster.setRotable(this);
		
		HitPointHolder hph = new HitPointHolder(HEALTH);
		setHitPointHolder(hph);
		hph.addListener(new HitPointHolder.HitPointListener() {

			@Override
			public void onDefeated() {
				destroy();
			}

			@Override
			public void onHit() {
			}
			
		});	
	}
	
	@Override
	public void tick(Workspace w, double passedTime, Complex mousePos) {
		super.tick(w, passedTime, mousePos);
		
		steerDir = Geometry.getDirection(this.getPos(), mousePos);
		
		if (Double.isNaN(steerDir.x) || Double.isNaN(steerDir.y)) {
			steerDir = new Complex(0,0);
		
		}
		
		rcsThruster.setTargetAngle(steerDir.getAngle());
		rcsThruster.tick(passedTime);
		
		try {
			thruster.tick(passedTime, steerDir, this);
		} catch (Exception e) {
			destroy();
		}
	}
	
	@Override
	protected void proximityReport(Workspace w, Physical trigger, double distance, double passedTime) {
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
	public void onDestroyed(Workspace w) {
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
		
		thruster.drawAbsolute(g2);
		super.paintAbsolute(g2);
	}
	
	public FuelTank getFuelTank() {
		return fuelTank;
	}
	
	public Thruster getThruster() {
		return thruster;
	}
	
	public void setThrust(double thrust) {
		thruster.setThrust(thrust);
	}
	
	public void setThrustFract(double thrustFract) {
		thruster.setThrustFract(thrustFract);
	}
	
	public void setLockOn(Physical lockOn) {
		this.lockOn = lockOn;
	}
	
	public Physical getLockOn() {
		return lockOn;
	}

	public Physical shootPrimary(Complex pos) {
		try {
			return primaryWeapon.use(this, lockOn, Complex.normalFromAngle(getRotationAngle()));
		} catch (Exception e) {
			return null;
		}
		
	}
	
	public Physical shootSecondary(Complex pos) {
		try {
			return secondaryWeapon.use(this, lockOn, Complex.normalFromAngle(getRotationAngle()));
		} catch (Exception e) {
			return null;
		}
		
	}

	public void setWeapon(Weapon weapon) {
		this.primaryWeapon = weapon;
	}
	
	public void setSecondaryWeapon(Weapon secondaryWeapon) {
		this.secondaryWeapon = secondaryWeapon;
	}
	

	@Override
	public Point2D getPosition() {
		return new Point2D.Double(getX(), getY());
	}

	@Override
	public double getRotation() {

		if (lockOn != null) {
			return 0;//Geometry.getDirection(getPos(), lockOn.getPos()).getAngle();
		}
		else {
			return 0;
		}
	}
	
	public int hitColisionDamage(Physical trigger) {
		
		double distance = trigger.getPos().minus(getPos()).abs();
		
		double rotationVelocity = 0;
		if (trigger instanceof RotablePhysical) {
			rotationVelocity = ((RotablePhysical)trigger).getRotationSpeed() * distance;
		}
		  
		double radV = -Orbit.getRadialVelocity(trigger, this);
		double proV = Math.abs(Orbit.getProgradeVelocity(trigger, this) - rotationVelocity);
		
		radV -= MAX_COLISION_V_RADIAL;
		proV -= MAX_COLISION_V_PROGRADE;
		
		if (radV > 0) {
			hit((int) (radV*COLISION_HIT_FACTOR));
		}
		if (proV > 0) {
			hit((int) (proV*COLISION_HIT_FACTOR));
		}

		return 0;
	}
}
