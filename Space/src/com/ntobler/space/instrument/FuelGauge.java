package com.ntobler.space.instrument;

import java.awt.Color;
import java.awt.Graphics2D;

import com.ntobler.space.ControlPanel;
import com.ntobler.space.CustomGraphics;
import com.ntobler.space.physical.Ship;
import com.ntobler.space.utility.FuelTank;

public class FuelGauge extends Instrument{

	private double fillFraction;
	private Color color;
	
	private boolean visible = false;
	
	public FuelGauge() {
		fillFraction = 0;
	}
	
	@Override
	public void update() {
		if (ship != null) {
			fillFraction = ship.getFuelTank().getFillFraction();
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
			CustomGraphics.drawProgressBar(g2, 0, 0, width, height, fillFraction);
			CustomGraphics.setStringAlign(CustomGraphics.HorizontalAlign.LEFT, CustomGraphics.VerticalAlign.CENTER);
			CustomGraphics.drawAlignedString(g2, width + 5, height/2, String.format("Fuel: %.0f%%", fillFraction*100));
			g2.setColor(Color.WHITE);
		}
	}
	
	@Override
	public void drawNormalOnShip(Graphics2D g2) {
		
		if (visible) {
			if (fillFraction > 0.30) 		return;
			else if (fillFraction > 0.10) 	color = Color.ORANGE;
			else 							color = Color.RED;
			
			g2.setColor(color);
			g2.setFont(ControlPanel.fontBig);
			CustomGraphics.setStringAlign(CustomGraphics.HorizontalAlign.LEFT, CustomGraphics.VerticalAlign.CENTER);
			CustomGraphics.drawAlignedString(g2, 64, 0 , "Low Fuel");
			g2.setColor(Color.WHITE);
		}
	}
	
	
}
