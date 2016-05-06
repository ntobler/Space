package com.ntobler.space;

import java.awt.Component;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import com.ntobler.space.physical.AtmospherePlanet;
import com.ntobler.space.physical.HostilePlanet;
import com.ntobler.space.physical.Planet;
import com.ntobler.space.physical.Ship;
import com.ntobler.space.render.Camera;
import com.ntobler.space.render.Render;

public class Space {

	private static GameFrame gameFrame;
	private static Game game;
	
	public static void main(String [] args) {
		
		game = new Game();
		
		gameFrame = new GameFrame(game.getPaintable());
		
		game.setCanvas(gameFrame.getCanvas());
		
		setKeyBindings();
		
		setupMouseListeners();
	}
	
	public static double getGameTime() {
		return game.getTime();
	}

	private static void setKeyBindings() {
		
		JComponent c = gameFrame.getCanvas();
		
		InputMap m = c.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		
		m.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, 0, false), Game.FIRE_PRESS);
		m.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, 0, true), Game.FIRE_RELEASE);
		
		m.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false), Game.THRUST_PRESS);
		m.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true), Game.THRUST_RELEASE);
		
		m.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, false), Game.AQUIRE_PRESS);
		m.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, true), Game.AQUIRE_RELEASE);
		
		c.setActionMap(game.getActionMap());
	}
	
	
	private static void setupMouseListeners() {
		
		/*gameFrame.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent arg0) {
				pressedKey = arg0.getKeyCode();
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				pressedKey = 0;
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
			}
			
		});*/
		
		gameFrame.getCanvas().addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseMoved(MouseEvent arg0) {
				
				game.setMousePos(new Complex(arg0.getX(), arg0.getY()));
			}
			
		});
		
		/*gameFrame.addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				game.getCamera().addZoomTicks(0-arg0.getWheelRotation());
			}
			
		});
		
		gameFrame.addMouseListener(new MouseListener() {

			private Complex pos;
			private Complex velocity;
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				//pos = new Complex(arg0.getX(), w.getYSize() - arg0.getY());
				pos = w.getCamera().getGamePos(new Complex(arg0.getX(), arg0.getY()));
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {

				velocity = w.getCamera().getGamePos(new Complex(arg0.getX(), arg0.getY()));
				
				switch (pressedKey) {
				case 0:
					
					if (w.getShip() == null) {
						
						Ship s = new Ship();
						s.setPos(new Complex(pos));
						s.setVelocity(velocity.minus(pos));
						s.setMass(1000);
						//Orbit.setInOrbit(s, lastAddedPhysical, w, Orbit.CLOCKWHISE);
						//s.setCriticalDistance(5);
						
						w.setShip(s);
						c.setShip(s);
						w.addPhysical(s);
		                w.getCamera().setFocus(s);
						
					}
					break;
					
				case KeyEvent.VK_P:
					//pos = w.getRenderTransformer().getGamePos((new Complex(arg0.getX(), arg0.getY())));
					
					Planet p = new Planet();
					p.setPos(pos);
					p.setVelocity(velocity.minus(pos));
					p.setMass(1e10);
					p.setRadius(128);
					
					w.addPhysical(p);
					break;
					
				case KeyEvent.VK_O:
					//pos = w.getRenderTransformer().getGamePos((new Complex(arg0.getX(), arg0.getY())));
					
					HostilePlanet o = new HostilePlanet();
					o.setPos(pos);
					o.setMass(1e7);
   					Orbit.setInOrbit(o, w.getShip().getLockOn(), w, Orbit.CLOCKWHISE);
					o.setRadius(100);
					
					w.addPhysical(o);
					break;
				
				case KeyEvent.VK_I:
					//pos = w.getRenderTransformer().getGamePos((new Complex(arg0.getX(), arg0.getY())));
					
					Planet i = new Planet();
					i.setPos(pos);
					i.setMass(1e5);
					Orbit.setInOrbit(i, w.getShip().getLockOn(), w, Orbit.CLOCKWHISE);
					i.setRadius(16);
					
					w.addPhysical(i);
					break;
				case KeyEvent.VK_T:
					try {
						w = GameLoader.loadWorkspace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						System.out.println("Error while loading Workspace");
					}
					break;
				case KeyEvent.VK_Z:
					try {
						GameLoader.saveWorkspace(w);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						System.out.println("Error while saving Workspace");
					}
					break;
				}
				
			}


		});*/
		
	}
	
	
}
