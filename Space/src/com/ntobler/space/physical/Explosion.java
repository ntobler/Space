package com.ntobler.space.physical;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import com.ntobler.space.Complex;
import com.ntobler.space.CustomGraphics;
import com.ntobler.space.MovingObject;

public class Explosion extends MovingObject {

	private final int FRAGMENT_COUNT = 10;
	
	private final int LIFE_SPAN = 1;
	
	private double passedTimeSum = 0;
	
	private List<Fragment> fragmentList;
	
	private class Fragment {
		public Complex velocity;
		public Complex pos; 
	}
	
	public Explosion(Physical p, double maxFragmentSpeed) {
		super(p.getPos(), new Complex(0, 0));
		setPos(p.getPos());
		
		fragmentList = new ArrayList<Fragment>();
		
		for (int i = 0; i < FRAGMENT_COUNT; i++) {
			
			double scatterAngle = Math.random() * 2 * Math.PI;
			double fragmentSpeed = Math.random()  * maxFragmentSpeed;
			
			Fragment f = new Fragment();
			f.pos = getPos();
			f.velocity = Complex.normalFromAngle(scatterAngle).scalarMultiply(fragmentSpeed).plus(p.getVelocity());
			fragmentList.add(f);
		}
	}
	
	@Override
	public void tick(double passedTime) {
		
		passedTimeSum += passedTime;
				
		if (passedTimeSum > LIFE_SPAN) {
			destroy();
		}
		else {
			for (Fragment f: fragmentList) {
				f.pos = f.pos.plus(f.velocity.scalarMultiply(passedTimeSum));
			}
		}
	}
	
	@Override
	public void paint(Graphics2D g2) {
		
		double fraction = (1 - (passedTimeSum / LIFE_SPAN));
		if (fraction < 0) fraction = 0;
		
		int hue = (int) (fraction * 256);
 		g2.setPaint(new Color(hue + (hue * 256) + (hue * 256 * 256)));
		
		for (Fragment f: fragmentList) {
			//g2.draw(new Line2D.Double(f.pos.x,f.pos.y, f.pos.x, f.pos.y));
			g2.fill(CustomGraphics.circle(f.pos.x, f.pos.y, fraction*5));
		}
		
		g2.setPaint(Color.WHITE);
	}
	
	
}
