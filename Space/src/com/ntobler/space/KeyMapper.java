package com.ntobler.space;

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

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
		
	
	/*private Map<Integer, Controlable> eventMap;
	
	public KeyMapper() {
		
		eventMap = new HashMap<Integer, Controlable>();
		
		
		
		
		
	}
	
	public void put(int event, Controlable controlable) {
		eventMap.put(event, controlable);
	}

	private void passEventStart(int event) {
		Controlable receptor = eventMap.get(event);
		receptor.onEventStart(event);
	}
	
	private void passEventStop(int event) {
		Controlable receptor = eventMap.get(event);
		receptor.onEventStart(event);
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		passEventStart(MouseEvent.MOUSE_PRESSED);
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		passEventStop(MouseEvent.MOUSE_RELEASED);
		
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		passEventStart(MouseWheelEvent.MOUSE_WHEEL);
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		passEventStart(MouseEvent.MOUSE_MOVED);
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		passEventStart(e.getKeyCode());
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		passEventStop(e.getKeyCode());
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}*/
	
}
