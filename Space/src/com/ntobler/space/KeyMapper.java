package com.ntobler.space;

import java.awt.AWTEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.HashMap;
import java.util.Map;

public class KeyMapper implements KeyListener, MouseMotionListener, MouseWheelListener, MouseListener{

	private Map<Integer, ControlEvent> eventMap;
	
	private Game game;
	
	public KeyMapper() {		
		eventMap = new HashMap<Integer, ControlEvent>();
	}
	
	public void put(int event, int type, ControlEvent controlEvent) {
		eventMap.put(event + (type * 65536), controlEvent);
	}
	
	public void setGame (Game game) {
		this.game = game;
	}
	
	private void passEvent(int event, int type, int value) {
		ControlEvent receptor = eventMap.get(event + (type * 65536));
		if (receptor != null) {
			receptor.onScaleAction(value);
		}
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		//System.out.println("mouse clicked" + e.getID());
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		//System.out.println("mouse entered" + e.getID());
	}

	@Override
	public void mouseExited(MouseEvent e) {
		//System.out.println("mouse exited" + e.getID());
	}

	@Override
	public void mousePressed(MouseEvent e) {
		//System.out.println("mouse pressed" + e.getID());
		passEvent(e.getButton(), ControlEvent.MOUSE_EVENT, 1);
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		//System.out.println("mouse released" + e.getID());
		passEvent(e.getButton(), ControlEvent.MOUSE_EVENT, 0);
		
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		System.out.println("wheel" + arg0.getWheelRotation());
		passEvent(0, ControlEvent.MOUSE_WHEEL_EVENT, arg0.getWheelRotation());
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		//System.out.println("mouse dragged" + e.getID());
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		//System.out.println("mouse moved" + e.getID());
		game.setMousePos(e.getPoint());
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		//System.out.println("key pressed" + e.getID());
		passEvent(e.getKeyCode(), ControlEvent.KEY_EVENT, ControlEvent.PRESSED);
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		//System.out.println("key released" + e.getID());
		passEvent(e.getKeyCode(), ControlEvent.KEY_EVENT, ControlEvent.RELEASED);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		//System.out.println("key typed" + e.getID());
	}
	
}
