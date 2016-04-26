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

public class Space {

	private static GUI gui;
	private static Render render;
	private static RenderTransformer rt;
	private static PhysicalWorkspace w;
	private static ControlPanel c;
	
	
	
	private static long tickPeriod;
	private static double timeFactor;
	private static double time = 0;
	
	private static int pressedKey = 0;
	
	private static Complex mousePos = new Complex(0, 0);
	
	
	private static Physical lastAddedPhysical = null;
	
	
	public static void main(String [] args) {
		
		tickPeriod = 1000/60;
		timeFactor = 1;
		
		render = new Render();
		
		gui = GUI.getInstance();
		gui.add(render);
		
		rt = new RenderTransformer(render.getSize());
		w = new PhysicalWorkspace(rt);
		//w.setGetGravitationalConstant(1e-1);
		w.setGetGravitationalConstant(1e-8);
		c = new ControlPanel(w);
		render.addPaintable(w);
		render.addPaintable(c);
		
		gui.addComponentListener(new ComponentAdapter() 
		{  
		        public void componentResized(ComponentEvent evt) {
		            Component c = (Component)evt.getSource();
		            render.setSize(c.getSize());
		        }
		});
		
		setupMouseListeners();
		
		loadSolarSystem();
		
		
		Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
            	
            	time += (double) tickPeriod / 1000;
            	
            	Complex transformedMousePos = w.getRenderTransformer().getGamePos(mousePos);
            	
            	c.tick(mousePos, pressedKey);
            	
            	Ship s = w.getShip();
            	if (s != null) {
            		
            		
            		

	            	
	            	
	            	
	            	
	            	
	            	
            	}
                w.tick(((double)tickPeriod * timeFactor) / 1000, transformedMousePos);
                
                
                
                
                //Complex pos = w.getPhysicals().get(0).getPos();
                //System.out.println(Double.toString(pos.getReal()) + "," + Double.toString(pos.getImag()));
                w.getRenderTransformer().setScreenDimension(render.getSize());
                render.revalidate();
                gui.repaint();
            }
        }, tickPeriod, tickPeriod);
        
	}
	
	public static double getTime() {
		return time;
	}
	
	private static void setupMouseListeners() {
		
		
		
		gui.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent arg0) {
				pressedKey = arg0.getKeyCode();
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				pressedKey = 0;
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		render.addMouseMotionListener(new MouseMotionListener() {

			Complex focusPos = new Complex(0,0);
			
			@Override
			public void mouseDragged(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseMoved(MouseEvent arg0) {
				
				
				
				mousePos = new Complex(arg0.getX(), arg0.getY());
			}
			
		});
		
		render.addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				// TODO Auto-generated method stub
				w.getRenderTransformer().addZoomTicks(0-arg0.getWheelRotation());
			}
			
		});
		
		render.addMouseListener(new MouseListener() {

			private Complex pos;
			private Complex velocity;
			
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

				//pos = new Complex(arg0.getX(), w.getYSize() - arg0.getY());
				pos = w.getRenderTransformer().getGamePos(new Complex(arg0.getX(), arg0.getY()));
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {

				velocity = w.getRenderTransformer().getGamePos(new Complex(arg0.getX(), arg0.getY()));
				
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
		                w.getRenderTransformer().setFocus(s);
						
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
					lastAddedPhysical = p;
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


		});
		
	}
	
	private static void loadSolarSystem() {
		
		//Sun
		Planet sun = new Planet();
		sun.setPos(Complex.ZERO);
		sun.setVelocity(Complex.ZERO);
		sun.setMass(1e16);
		sun.setRadius(256);
		w.addPhysical(sun);
		
		//Earth
		Planet earth = new AtmospherePlanet();
		earth.setPos(new Complex(0, 15000));
		earth.setMass(1e14);
		Orbit.setInOrbit(earth, sun, w, Orbit.CLOCKWHISE);
		earth.setRadius(64);
		w.addPhysical(earth);
		
		//Mars (hostile)
		HostilePlanet mars = new HostilePlanet();
		mars.setPos(new Complex(0, -22500));
		mars.setMass(1e13);
		Orbit.setInOrbit(mars, sun, w, Orbit.CLOCKWHISE);
		mars.setRadius(48);
		
		Ship s = new Ship();
		s.setPos(earth.getPos().plus(new Complex(0, 150)));
		s.setMass(1000);
		Orbit.setInOrbit(s, earth, w, Orbit.CLOCKWHISE);
		
		w.setShip(s);
		c.setShip(s);
		w.addPhysical(s);
        w.getRenderTransformer().setFocus(s);
	}
}
