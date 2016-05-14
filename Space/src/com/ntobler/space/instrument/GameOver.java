package com.ntobler.space.instrument;

import java.awt.Color;
import java.awt.Graphics2D;

import com.ntobler.space.ControlPanel;
import com.ntobler.space.CustomGraphics;

public class GameOver {

	private boolean active;
	
	private String string;
	private char lastChar;
	
	public GameOver() {
		string = "A";
		lastChar = 'h';
		
		active = false;
	}
	
	public void tick() {
		
		if (active) {
			if (lastChar == 'h') {
				string += "a";
				lastChar = 'a';
			}
			else {
				string += "h";
				lastChar = 'h';
			}
		}

	}
	
	public void draw(Graphics2D g2) {
	}
	
	public void drawNormalOnShip(Graphics2D g2) {
		
		if (active) {
			g2.setColor(Color.RED);
			g2.setFont(ControlPanel.fontHuge);
			CustomGraphics.setStringAlign(CustomGraphics.HorizontalAlign.CENTER, CustomGraphics.VerticalAlign.CENTER);
			CustomGraphics.drawAlignedString(g2, 0, -100, "You died");
			CustomGraphics.drawAlignedString(g2, 0, 100, string);
		}
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
}
