package com.ntobler.space;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import com.ntobler.space.render.Paintable;

public class GameFrame extends JFrame {
	
	private GameCanvas canvas;
	
	private Paintable paintable;
	
	public GameFrame(Paintable paintable) {
		
		this.paintable = paintable;
		
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new GUIDispatcher());

		canvas = new GameCanvas();
		add(canvas);
		
		addComponentListener(new ComponentAdapter() {  
	        public void componentResized(ComponentEvent evt) {
	            paintable.setImageDimension(canvas.getSize());
	        }
		});
		
		setVisible(true);
		
		setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
	}
	
	public class GameCanvas extends JPanel {
		
		@Override
	    protected void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        
	        g.setColor(Color.BLACK);
	        g.fillRect(0, 0, this.getWidth(), this.getHeight());
	        g.setColor(Color.WHITE);
	        
	        Graphics2D g2 = (Graphics2D) g;
	        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
	        
	        paintable.paint(g2);
	        
	        g2.dispose();
	    }
		
	}
	
	private class GUIDispatcher implements KeyEventDispatcher {
        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
        	
        	if (e.getID() == KeyEvent.KEY_PRESSED) {
        		onKeyPressed(e);
            }
            return false;
        }
    }
	
	private void onKeyPressed(KeyEvent e) {
		
	}

	public GameCanvas getCanvas() {
		return canvas;
	}
	
	
	
}
