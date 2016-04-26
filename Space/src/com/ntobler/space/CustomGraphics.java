package com.ntobler.space;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

public class CustomGraphics {

	public static Shape circle(double x, double y, double radius) {
	   return new Ellipse2D.Double(x-radius, y-radius, radius*2, radius*2);
	}
	
	public static void drawProgressBar(Graphics2D g2, int x, int y, int width, int height, double progress) {
		g2.draw(new Rectangle2D.Double(x, y, width, height));
		g2.fill(new Rectangle2D.Double(x, y, (int) (width * progress), height));
	}
	public static void drawCenteredProgressBar(Graphics2D g2, double x, double y, double width, double height, double progress) {
		
		g2.draw(new Rectangle2D.Double(x - (width/2), y - (height/2), width, height));
		g2.fill(new Rectangle2D.Double(x - (width/2), y - (height/2), (int) (width * progress), height));
	}
	
	public static void drawLockOn(Graphics2D g2, int x, int y, int radius) {
		
		int a = radius + 2;
		int l = 5;
		
		//upper right
		g2.drawLine(x+a, y+a, x+a-l, y+a);	//left
		g2.drawLine(x+a, y+a, x+a, y+a-l);	//down
		
		//lower right
		g2.drawLine(x+a, y-a, x+a, y-a+l);	//up	
		g2.drawLine(x+a, y-a, x+a-l, y-a);	//left
		
		//lower left
		g2.drawLine(x-a, y-a, x-a+l, y-a);	//right	
		g2.drawLine(x-a, y-a, x-a, y-a+l);	//up
		
		//upper left
		g2.drawLine(x-a, y+a, x-a, y+a-l);	//down	
		g2.drawLine(x-a, y+a, x-a+l, y+a);	//right
		
	}
	
	public static void drawVector(Graphics2D g2, double x, double y, Complex v, double scale) {
		
		double x2 = x + (v.x * scale);
		double y2 = y + (v.y * scale);
		
		g2.draw(new Line2D.Double(x, y, x2, y2));
		g2.draw(circle(x2, y2 , 5));
		
	}
	
	public static void drawMovementVectors(Graphics2D g2, Physical p) {
		
		final double x = p.getX();
		final double y = p.getY();
		
		g2.setPaint(Color.BLUE);
		Complex v = p.getVelocity();
		g2.draw(new Line2D.Double(x, y, x+ v.x, y + v.y));
		g2.setPaint(Color.RED);
		/*g2.draw(new Line2D.Double(x, y, x+ (steerDir.x*16), y+ (steerDir.y*16)));
		g2.setPaint(Color.WHITE);*/
	
	}
	
	public static void drawCenteredString(Graphics2D g2, float x, float y, String text) {
	    FontMetrics metrics = g2.getFontMetrics(g2.getFont());
	    x -= metrics.stringWidth(text) / 2;
	    y -= metrics.getAscent();
	    g2.drawString(text, x, y);
	}
}
