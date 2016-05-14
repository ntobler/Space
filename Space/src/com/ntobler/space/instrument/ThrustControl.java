package com.ntobler.space.instrument;

import java.awt.Color;
import java.awt.Graphics2D;

import com.ntobler.space.CustomGraphics;

public class ThrustControl  extends Instrument {

	private double thrust;
	private Color color;
	
	private boolean visible = false;
	
	public ThrustControl() {
		thrust = 0;
	}
	
	@Override
	public void update() {
		if (ship != null) {
			thrust = ship.getThruster().getThrustFraction();
			visible = true;
			return;
		}
		
		visible = false;
	}
	
	@Override
	public void draw(Graphics2D g2) {
		
		final int width = 32;
		final int height = 192;
		
		if (visible) {
			g2.setColor(color);
			CustomGraphics.drawVerticalProgressBar(g2, 0, 0, width, height, thrust);
			CustomGraphics.drawCenteredString(g2, width/2, height + 16 , String.format("%.0f%%", thrust*100));
			g2.setColor(Color.WHITE);
		}
	}
	
}
