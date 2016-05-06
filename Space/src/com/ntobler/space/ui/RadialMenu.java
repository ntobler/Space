package com.ntobler.space.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RadialGradientPaint;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import com.ntobler.space.Complex;
import com.ntobler.space.CustomGraphics;

public class RadialMenu {

	private List<Listable> items;
	private int selectedIndex = 0;
	
	private double radius;
	
	private Complex pos;
	
	private boolean visible;
	
	public interface Listable {
		public String getName();
		public String getDescription();
	}
	
	
	public RadialMenu(List<Listable> items) {
		this.items = items;
		radius = 100;
	}
	
	public  void draw(Graphics2D g2) {
		
		if (visible) {
			
			final double x = pos.x;
			final double y = pos.y;
			
			double angleIncrement = Math.PI * 2 / items.size();
			double startAngle = (angleIncrement / 2);
			
			double angle = startAngle;
			for (Listable item: items) {
				Complex vector = Complex.normalFromAngle(angle).scalarMultiply(radius);	
				CustomGraphics.drawCenteredString(g2, (float) (vector.x + x), (float) (vector.y + y), item.getName());
				angle += angleIncrement;
			}
			
			
			Complex p1 = Complex.normalFromAngle(angleIncrement * selectedIndex).scalarMultiply(2*radius);
			Complex p2 = Complex.normalFromAngle((angleIncrement * selectedIndex) + startAngle).scalarMultiply(2*radius);
			Complex p3 = Complex.normalFromAngle(angleIncrement * (selectedIndex + 1)).scalarMultiply(2*radius);	
			Path2D path = new Path2D.Double();
			path.moveTo(x, y);
			path.lineTo(x + p1.x, y + p1.y);
			path.lineTo(x + p2.x, y + p2.y);
			path.lineTo(x + p3.x, y + p3.y);
			
			Point2D center = new Point2D.Double(x, y);
			float[] dist = {0.0f, 1.0f};
			Color[] colors = {new Color(255, 255, 255, 100), new Color(255, 255, 255, 0)};
			RadialGradientPaint paint = new RadialGradientPaint(center, (float) radius, dist, colors);
			
			g2.setPaint(paint);
			g2.fill(path);
			g2.setPaint(Color.WHITE);
		}
	}
	
	public void setPos (Complex pos) {
		this.pos = pos;
	}
	
	public void selectItem(double angle) {
		
		double angleIncrement = Math.PI * 2 / items.size();
		
		if (angle < 0) angle += Math.PI * 2;
		
		selectedIndex = (int) (angle / angleIncrement);	
	}
	
	public Listable getSelectedItem() {
		
		return items.get(selectedIndex);
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
}
