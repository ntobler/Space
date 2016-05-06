package com.ntobler.space.utility;

import java.awt.Color;
import java.awt.Graphics2D;

import com.ntobler.space.CustomGraphics;

public class HitPointHolder {

	private final double width = 32;
	private final double height = 5;
	
	private int maxPoints;
	private int points;
	
	private HitPointListener hitPointListener;
	
	public HitPointHolder (int maxPoints) {
		 
		this.maxPoints = maxPoints;
		this.points = maxPoints;
	}
	
	public interface HitPointListener {
		public void onDefeated();
	}

	public void setListener(HitPointListener hitPointListener) {
		this.hitPointListener = hitPointListener;
	}
	
	public void hit(int points) {
		if (this.points > points) {
			this.points -= points;
		}
		else {
			this.points = 0;
			hitPointListener.onDefeated();
		}
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
