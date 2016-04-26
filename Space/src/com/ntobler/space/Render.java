package com.ntobler.space;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;

import javax.swing.JPanel;

public class Render extends JPanel {

	private ArrayList<Paintable> paintables;
	
	public Render() {
		
		paintables = new ArrayList<Paintable>();
		
	}
	
	public void addPaintable(Paintable p) {
		paintables.add(p);
	}
	
	public void removePaintable(Paintable p) {
		paintables.remove(p);
	}
	
	@Override
    public Dimension getPreferredSize() {
        return new Dimension(800,1000);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.setColor(Color.WHITE);
        
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        
        for (Paintable p: paintables) {
        	
        	p.paint(g2);
        }
        
        g2.dispose();
    }

	@Override
	public void setSize(Dimension size) {
    	
    }
	
}
