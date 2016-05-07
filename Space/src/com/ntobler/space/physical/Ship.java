package com.ntobler.space.physical;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

import com.ntobler.space.Complex;
import com.ntobler.space.CustomGraphics;
import com.ntobler.space.Geometry;
import com.ntobler.space.Workspace;
import com.ntobler.space.render.Focusable;
import com.ntobler.space.utility.FuelTank;
import com.ntobler.space.utility.HitPointHolder;
import com.ntobler.space.utility.Thruster;
import com.ntobler.space.utility.HitPointHolder.HitPointListener;
import com.ntobler.space.utility.Thruster.ThrusterListener;
import com.ntobler.space.weapon.Weapon;

public class Ship extends RotablePhysical implements Focusable {

	private final double MAX_FUEL = 10000;
	private final int HEALTH = 1000;
	private final double RADIUS = 6;
	
	private Complex steerDir;
	
	private Physical lockOn;
	
	private Weapon primaryWeapon;
	private Weapon secondaryWeapon;
	
	private FuelTank fuelTank;
	private Thruster thruster;
	
	public Ship () {
		setRadius(RADIUS);
		
		steerDir = new Complex(1, 0);
		
		fuelTank = new FuelTank(MAX_FUEL);
		
		thruster = new Thruster(fuelTank);
		thruster.setListener(new Thruster.ThrusterListener() {
			@Override
			public void onRunOutOfFuel() {
				destroy();
			}
		});
		
		HitPointHolder hph = new HitPointHolder(HEALTH);
		setHitPointHolder(hph);
		hph.setListener(new HitPointHolder.HitPointListener() {

			@Override
			public void onDefeated() {
				destroy();
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
		
		setRotationAngle(steerDir.getAngle());
		
		thruster.tick(passedTime, steerDir, this);
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
		
		thruster.draw(g2);
		super.paintAbsolute(g2);
	}
	
	public FuelTank getFuelTank() {
		return fuelTank;
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

	public Physical shootPrimary(Complex pos) {
		try {
			return primaryWeapon.use(this, lockOn, this.steerDir);
		} catch (Exception e) {
			return null;
		}
		
	}
	
	
	public Physical shootSecondary(Complex pos) {
		try {
			return secondaryWeapon.use(this, lockOn, this.steerDir);
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
}
