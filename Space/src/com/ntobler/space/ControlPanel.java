package com.ntobler.space;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

import com.ntobler.space.instrument.DeltaVGauge;
import com.ntobler.space.instrument.FuelGauge;
import com.ntobler.space.instrument.HullGauge;
import com.ntobler.space.instrument.OrbitCalculator;
import com.ntobler.space.instrument.ThrustControl;
import com.ntobler.space.physical.Physical;
import com.ntobler.space.physical.Ship;
import com.ntobler.space.render.Paintable;
import com.ntobler.space.ui.RadialMenu;
import com.ntobler.space.ui.RadialMenu.Listable;
import com.ntobler.space.utility.FuelTank;
import com.ntobler.space.utility.HitPointHolder.HitPointListener;
import com.ntobler.space.utility.Utility;
import com.ntobler.space.weapon.StandardDefenseMissile;
import com.ntobler.space.weapon.StandardGun;
import com.ntobler.space.weapon.StandardMissile;
import com.ntobler.space.weapon.Weapon;

public class ControlPanel implements Paintable {

	public static final Font fontBigMonospaced = new Font("Courier", Font.PLAIN, 28);
	public static final Font fontSmall = new Font("Calibri", Font.PLAIN, 16);
	
	private Workspace w;
	
	private Ship ship;
	
	private List<Listable> weapons;
	private RadialMenu weaponMenu;
	
	private List<Listable> utilities;
	private RadialMenu utilityMenu;
	
	private Utility activeUtility;
	
	private DeltaVGauge deltaVGauge;
	private FuelGauge fuelGauge;
	private HullGauge hullGauge;
	private ThrustControl thrustControl;
	private OrbitCalculator orbitCalculator;
	
	
	
	private boolean primaryShooting = false;
	private boolean secondaryShooting = false;
	private boolean thrusting = false;
	private boolean aquiring = false;
	private boolean weaponSelecting = false;
	private boolean utilitySelecting = false;
	
	
	public ControlPanel (Workspace w) {
		this.w = w;
		
		weapons = new ArrayList<Listable>();
		weaponMenu = new RadialMenu(weapons);
		
		weapons.add(new StandardGun(1000));
		weapons.add(new StandardMissile(20));
		weapons.add(new StandardDefenseMissile(20));
		
		utilities = new ArrayList<Listable>();
		utilityMenu = new RadialMenu(utilities);
		utilities.add(new Utility(Utility.NONE));
		utilities.add(new Utility(Utility.ORBIT_CALCULATOR));
		
		activeUtility = (Utility)utilities.get(0);
		
		deltaVGauge = new DeltaVGauge();
		fuelGauge = new FuelGauge();
		hullGauge = new HullGauge();
		thrustControl = new ThrustControl();
		orbitCalculator = new OrbitCalculator(w);
		
	}
	
	public void tick (Complex mouseScreenPos) {
		
		Complex mouseGamePos = w.getCamera().getGamePos(mouseScreenPos);
		
		Complex shipPos;
		if (ship != null) {
			shipPos = w.getCamera().getScreenPos(ship.getPos());
			
			if (aquiring) {
        		ship.setLockOn(w.getNearestPhysical(mouseGamePos));
        	}
			
        	if (thrusting) {
        		
        		double thrustFract = mouseScreenPos.minus(w.getCamera().getScreenPos(ship.getPos())).abs();
        		thrustFract = thrustFract / 100;
        		if (thrustFract > 1) thrustFract = 1;
        		ship.setThrustFract(thrustFract);
        	}
        	else {
        		ship.setThrust(0);
        	}
			
        	if (weaponSelecting) {
        		weaponMenu.setPos(shipPos);
    			double cursorAngle = mouseScreenPos.minus(shipPos).getAngle();
    			weaponMenu.selectItem(cursorAngle);
    			weaponMenu.setVisible(true);
    			ship.setWeapon((Weapon) weaponMenu.getSelectedItem());
        	}
        	else {
        		weaponMenu.setVisible(false);
        	}
        	
        	if (utilitySelecting) {
        		utilityMenu.setPos(shipPos);
    			double cursorAngle = mouseScreenPos.minus(shipPos).getAngle();
    			utilityMenu.selectItem(cursorAngle);
    			utilityMenu.setVisible(true);
    			activeUtility = (Utility) utilityMenu.getSelectedItem();
        	}
        	else {
        		utilityMenu.setVisible(false);
        	}
        	
        	if (primaryShooting) {
        		w.addPhysical(ship.shootPrimary(mouseGamePos));
        	}
        	if (secondaryShooting) {
        		w.addPhysical(ship.shootSecondary(mouseGamePos));
        	}
			
        	deltaVGauge.update();
        	fuelGauge.update();
        	hullGauge.update();
        	thrustControl.update();
        	orbitCalculator.update();
		}
		
	}
	
	

	public Ship getShip() {
		return ship;
	}

	public void setShip(Ship ship) {
		this.ship = ship;
		
		ship.setWeapon((Weapon) weapons.get(1));
		ship.setSecondaryWeapon((Weapon) weapons.get(0));
		
		deltaVGauge.setShip(ship);
		fuelGauge.setShip(ship);
		hullGauge.setShip(ship);
		thrustControl.setShip(ship);
		orbitCalculator.setShip(ship);
		
		ship.getHitPointHolder().addListener(new HitPointListener() {
			
			@Override
			public void onHit() {
				hullGauge.reportHit();
			}
			
			@Override
			public void onDefeated() {
			}
		});
	}

	@Override
	public void paint(Graphics2D g2) {

		if (ship != null) {
			
			paintIfShipPresent(g2);
			g2.setTransform(w.getCamera().getNoScaleNoRotationTransformation(ship));
			
			paintOnShipNormal(g2);
			
			Physical lockOn = ship.getLockOn();
			if (lockOn != null) {
				
				g2.setTransform(w.getCamera().getNoScaleNoRotationTransformation(lockOn));
				paintOnLockOn(g2, ship, lockOn);
				
				AffineTransform transform = w.getCamera().getNoScaleTransformation(ship);
				transform.rotate(- lockOn.getPos().minus(ship.getPos()).getAngle());
				g2.setTransform(transform);
				
				paintOnShipLockOnRotated(g2);
			}
		}
	}
	
	private void paintIfShipPresent(Graphics2D g2) {
		
		paintSpeedMeter(g2);
		
		g2.translate(5, 5);
		fuelGauge.draw(g2);
		g2.translate(0, 20);
		hullGauge.draw(g2);
		
		g2.translate(0, 20);
		thrustControl.draw(g2);
	}
	
	
	private void paintOnLockOn(Graphics2D g2, Physical ship, Physical lockOn) {
		
		float radius = (float) (lockOn.getRadius() * w.getCamera().getZoom());
		
		double distance = ship.getPos().minus(lockOn.getPos()).abs();
		
		g2.setFont(fontSmall);
		g2.setColor(Color.WHITE);
		CustomGraphics.setStringAlign(CustomGraphics.HorizontalAlign.RIGHT, CustomGraphics.VerticalAlign.TOP);
		CustomGraphics.drawAlignedString(g2, -radius - 5, -radius, String.format("d: %.2f", distance));
		CustomGraphics.drawLockOn(g2, radius);
	}
	
	private void paintOnShipNormal(Graphics2D g2) {
		fuelGauge.drawNormalOnShip(g2);
		hullGauge.drawNormalOnShip(g2);
		orbitCalculator.drawNormalOnShip(g2);
		deltaVGauge.drawNormalOnShip(g2);
	}
	
	public void paintOnShipLockOnRotated(Graphics2D g2) {
		deltaVGauge.draw(g2);
	}
	
	private void paintSpeedMeter(Graphics2D g2) {
		
		weaponMenu.draw(g2);
		utilityMenu.draw(g2);
		
		paintUtility(g2);
	}
	
	private void paintUtility(Graphics2D g2) {
		
		switch (activeUtility.getId()) {
		case Utility.NONE:
			return;
		case Utility.ORBIT_CALCULATOR:
			return;
		}
	}


	@Override
	public void setImageDimension(Dimension dimension) {
	}
	
	public void setPrimaryShooting(boolean shooting) {
		this.primaryShooting = shooting;
	}
	
	public void setSecondaryShooting(boolean secondaryShooting) {
		this.secondaryShooting = secondaryShooting;
	}
	
	public void setThrusting(boolean thrusting) {
		this.thrusting = thrusting;
	}

	public void setAquiring(boolean aquiring) {
		this.aquiring = aquiring;
	}

	public void setWeaponSelecting(boolean weaponSelecting) {
		this.weaponSelecting = weaponSelecting;
		
	}

	public void setUtilitySelecting(boolean utilitySelecting) {
		this.utilitySelecting = utilitySelecting;
	}


	
	
	
	
}
