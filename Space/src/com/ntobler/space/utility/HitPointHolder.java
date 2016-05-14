package com.ntobler.space.utility;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ntobler.space.CustomGraphics;
import com.ntobler.space.physical.Physical;

public class HitPointHolder {

	private final double width = 32;
	private final double height = 5;
	
	private int maxPoints;
	private int points;
	
	private List<HitPointListener> hitPointListeners;
	
	public HitPointHolder (int maxPoints) {
		 
		this.maxPoints = maxPoints;
		this.points = maxPoints;
		
		this.hitPointListeners = new ArrayList<HitPointListener>();
	}
	
	public interface HitPointListener {
		public void onDefeated();
		public void onHit();
	}

	public void addListener(HitPointListener hitPointListener) {
		hitPointListeners.add(hitPointListener);
	}
	
	public void removeAllListeners() {
		hitPointListeners.clear();
	}
	
	public void hit(int points) {
		
		for (HitPointListener l: hitPointListeners) {
			l.onHit();
		}
		
		if (this.points > points) {
			this.points -= points;
		}
		else {
			this.points = 0;
			
			for (HitPointListener l: hitPointListeners) {
				l.onDefeated();
			}
		}
	}
	
	public double getFraction() {
		return (double)points/ (double)maxPoints;
	}
	 
	public void paintHitBar (Graphics2D g2) {
		
		if (points != maxPoints) {
			double ratio = ((double) points) / ((double) maxPoints);
			
			g2.setPaint(Color.RED);
			CustomGraphics.drawCenteredProgressBar(g2, 0, 0, width, height, ratio);
			g2.setPaint(Color.WHITE);
		}
		
		
	}
	
}
