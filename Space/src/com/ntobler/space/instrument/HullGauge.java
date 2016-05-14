package com.ntobler.space.instrument;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;

import com.ntobler.space.CustomGraphics;

public class HullGauge extends Instrument{

	private double hullFraction;
	private Color color;
	
	
	private boolean hitActive;
	private int alpha;
	private static final double FACTOR = 1-(Math.pow(Math.E, -2));
	
	
	
	private boolean visible = false;
	
	public HullGauge() {
		hullFraction = 0;
		hitActive = false;
		alpha = 0;
	}
	
	@Override
	public void update() {
		
		if (hitActive) {
			alpha = ((int)(alpha * FACTOR));
			if (alpha == 0)
				hitActive = false;
		}
		
		if (ship != null) {
			hullFraction = ship.getHitPointHolder().getFraction();
			visible = true;
			return;
		}
		
		
		
		visible = false;
	}
	
	public void reportHit() {
		
		alpha = 255;
		hitActive = true;
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
			if (hullFraction < 0.30) {
				
				if (hullFraction > 0.10) 	color = Color.ORANGE;
				else 							color = Color.RED;
				
				g2.setColor(color);
				CustomGraphics.setStringAlign(CustomGraphics.HorizontalAlign.RIGHT, CustomGraphics.VerticalAlign.CENTER);
				CustomGraphics.drawAlignedString(g2, -64, 0 , "Hull Damaged");
				g2.setColor(Color.WHITE);
			}
		}
		
		if (hitActive) {
			Point2D center = new Point2D.Double(0, 0);
			float[] dist = {0.0f, 1.0f};
			Color[] colors = {new Color(255, 0, 0, 0), new Color(255, 0, 0, alpha)};
			RadialGradientPaint paint = new RadialGradientPaint(center, 1000, dist, colors);
			
			g2.setPaint(paint);
			g2.fill(CustomGraphics.circle(0, 0, 1000));
			g2.setPaint(Color.WHITE);
		}
		
		
		
	}
	
	
}

