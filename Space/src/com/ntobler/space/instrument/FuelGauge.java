package com.ntobler.space.instrument;

import java.awt.Graphics2D;

import com.ntobler.space.CustomGraphics;
import com.ntobler.space.physical.Ship;
import com.ntobler.space.utility.FuelTank;

public class FuelGauge {

	private Ship ship;
	
	public FuelGauge() {
	}
	
	public void setShip(Ship ship) {
		this.ship = ship;
	}
	
	public void draw(Graphics2D g2) {
		if (ship != null) {
			CustomGraphics.drawProgressBar(g2, 0, 0, 100, 10, ship.getFuelTank().getFillFraction());
		}
	}
	
	
}
