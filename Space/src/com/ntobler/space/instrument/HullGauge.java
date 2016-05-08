package com.ntobler.space.instrument;

import java.awt.Color;
import java.awt.Graphics2D;

import com.ntobler.space.CustomGraphics;

public class HullGauge extends Instrument{

	private double hullFraction;
	private Color color;
	
	private boolean visible = false;
	
	public HullGauge() {
		hullFraction = 0;
	}
	
	@Override
	public void update() {
		if (ship != null) {
			hullFraction = ship.getHitPointHolder().getFraction();
			visible = true;
			return;
		}
		
		visible = false;
	}
	
	@Override
	public void draw(Graphics2D g2) {
		
		final int width = 192;
		final int height = 16;
		
		if (visible) {
			g2.setColor(color);
			CustomGraphics.drawProgressBar(g2, 0, 0, width, height, hullFraction);
			CustomGraphics.setStringAlign(CustomGraphics.HorizontalAlign.LEFT, CustomGraphics.VerticalAlign.CENTER);
			CustomGraphics.drawAlignedString(g2, width + 5, height/2 , String.format("Hull: %.0f%%", hullFraction*100));
			g2.setColor(Color.WHITE);
		}
	}
	
	@Override
	public void drawNormalOnShip(Graphics2D g2) {
		
		if (visible) {
			if (hullFraction > 0.30) 		return;
			else if (hullFraction > 0.10) 	color = Color.ORANGE;
			else 							color = Color.RED;
			
			g2.setColor(color);
			CustomGraphics.setStringAlign(CustomGraphics.HorizontalAlign.RIGHT, CustomGraphics.VerticalAlign.CENTER);
			CustomGraphics.drawAlignedString(g2, -64, 0 , "Hull Damaged");
			g2.setColor(Color.WHITE);
		}
	}
	
	
}

