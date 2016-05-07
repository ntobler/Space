package com.ntobler.space.instrument;

import java.awt.Graphics2D;

import com.ntobler.space.CustomGraphics;
import com.ntobler.space.physical.Ship;

public class Instrument {

	protected Ship ship;
	
	public Instrument() {
	}
	
	public void setShip(Ship ship) {
		this.ship = ship;
	}
	
	public void update() {
	}
	
	public void draw(Graphics2D g2) {
	}
	
	public void drawNormalOnShip(Graphics2D g2) {
	}
	
}
