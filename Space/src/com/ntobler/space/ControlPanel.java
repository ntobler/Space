package com.ntobler.space;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

import com.ntobler.space.physical.Physical;
import com.ntobler.space.physical.Ship;
import com.ntobler.space.render.Paintable;
import com.ntobler.space.ui.RadialMenu;
import com.ntobler.space.ui.RadialMenu.Listable;
import com.ntobler.space.utility.DeltaVGauge;
import com.ntobler.space.utility.Utility;
import com.ntobler.space.weapon.Weapon;

public class ControlPanel implements Paintable {

	private Workspace w;
	
	private Ship ship;
	
	private List<Listable> weapons;
	private RadialMenu weaponMenu;
	
	private List<Listable> utilities;
	private RadialMenu utilityMenu;
	
	private Utility activeUtility;
	
	private DeltaVGauge deltaVGauge;
	
	
	
	private boolean shooting = false;
	private boolean thrusting = false;
	private boolean aquiring= false;
	private boolean weaponSelecting = false;
	private boolean utilitySelecting = false;
	
	
	public ControlPanel (Workspace w) {
		this.w = w;
		
		weapons = new ArrayList<Listable>();
		weaponMenu = new RadialMenu(weapons);
		
		weapons.add(new Weapon(Weapon.AIM_MISSILE, 10, 1));
		weapons.add(new Weapon(Weapon.GUN, 1000, 0.1));
		weapons.add(new Weapon(Weapon.BOMB, 20, 5));
		
		utilities = new ArrayList<Listable>();
		utilityMenu = new RadialMenu(utilities);
		utilities.add(new Utility(Utility.NONE));
		utilities.add(new Utility(Utility.ORBIT_CALCULATOR));
		
		activeUtility = (Utility)utilities.get(0);
		
		deltaVGauge = new DeltaVGauge();
		
	}
	
	public void tick (Complex mouseScreenPos, int pressedKey) {
		
		Complex mouseGamePos = w.getCamera().getGamePos(mouseScreenPos);
		
		Complex shipPos;
		if (ship != null) {
			shipPos = w.getCamera().getScreenPos(ship.getPos());
			
			if (aquiring) {
        		ship.setLockOn(w.getNearestPhysical(mouseGamePos));
        	}
			
        	if (thrusting) {
        		ship.setThrust(100);
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
        	
        	if (shooting) {
        		
        		w.addPhysical(ship.shoot(mouseGamePos));
        	}
			
        	deltaVGauge.tick(ship);
		}
		
	}
	
	

	public Ship getShip() {
		return ship;
	}

	public void setShip(Ship ship) {
		this.ship = ship;
		
		ship.setWeapon((Weapon) weapons.get(0));
	}

	@Override
	public void paint(Graphics2D g2) {

		if (ship != null) {
			
			paintFuelBar(g2);
			paintSpeedMeter(g2);
			
			
			Physical lockOn = ship.getLockOn();
			if (lockOn != null) {
				double radius = lockOn.getRadius() * w.getCamera().getZoom();
				
				g2.setTransform(w.getCamera().getNoScaleNoRotationTransformation(lockOn));
				CustomGraphics.drawLockOn(g2, radius);
				
				AffineTransform transform = w.getCamera().getNoScaleTransformation(ship, lockOn);
				transform.rotate(- lockOn.getPos().minus(ship.getPos()).getAngle());
				g2.setTransform(transform);
				paintShipLockOnTransformed(g2);

			}
			
		}
	}
	
	public void paintShipTranslated(Graphics2D g2) {
		
		Physical lockOn = ship.getLockOn();
		if (lockOn != null) {
			
			AffineTransform translatedTransform = g2.getTransform();
			
			AffineTransform rotatedTransform = new AffineTransform(translatedTransform);
			double angle = Geometry.getDirection(ship.getPos(), lockOn.getPos()).getAngle();
			rotatedTransform.rotate(angle);
			g2.setTransform(rotatedTransform);
			
			paintShipLockOnTransformed(g2);
			
			g2.setTransform(translatedTransform);
		}
		
	}

	
	public void paintShipLockOnTransformed(Graphics2D g2) {
		deltaVGauge.paint(g2);
	}
	
	private void paintFuelBar(Graphics2D g2) {
		
		CustomGraphics.drawProgressBar(g2, 5, 5, 100, 10, ship.getFuelFraction());
		
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
			paintOrbitCalculator(g2);
			return;
		}
	}
	
	private void paintOrbitCalculator(Graphics2D g2) {
		
		Physical lockOn = ship.getLockOn();
		
		Complex shipPos = w.getCamera().getScreenPos(ship.getPos());
		
		final Font font = new Font("Courier", Font.PLAIN, 28);
		g2.setFont(font);
		
		if (lockOn != null) {
				
			double deltaV = ship.getVelocity().minus(lockOn.getVelocity()).abs(); 
			double progradeV = Orbit.getProgradeVelocity(ship, lockOn);
			double radialV = Orbit.getRadialVelocity(ship, lockOn);
			Complex roundOrbitVector = Orbit.getRoundOrbitalDeltaV(ship, lockOn, w, Orbit.CLOCKWHISE); 
			
			
			
			g2.drawString(String.format("Delta v:\t%.2f", roundOrbitVector.abs()), (int)shipPos.x + 10, (int)shipPos.y + 14);
			if (roundOrbitVector.abs() > 50) {
				roundOrbitVector = roundOrbitVector.normalVector().scalarMultiply(50);
			}
			CustomGraphics.drawVector(g2, shipPos.x, shipPos.y, roundOrbitVector, 1);
			
			
			g2.drawString(String.format("Delta v:\t%.2f", deltaV), 200, 32);
			g2.drawString(String.format("Prograde v:\t%.2f", progradeV), 200, 64);
			g2.drawString(String.format("Radial v:\t%.2f", radialV), 200, 96);
		}
		else {
			g2.drawString("NO LOCK ON", 200, 32);
		}
	}

	@Override
	public void setImageDimension(Dimension dimension) {
	}
	
	public void setShooting(boolean shooting) {
		this.shooting = shooting;
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
