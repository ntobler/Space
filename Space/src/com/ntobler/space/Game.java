package com.ntobler.space;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.JComponent;

import com.ntobler.space.physical.AtmospherePlanet;
import com.ntobler.space.physical.HostilePlanet;
import com.ntobler.space.physical.Planet;
import com.ntobler.space.physical.Ship;
import com.ntobler.space.render.Camera;
import com.ntobler.space.render.Paintable;
import com.ntobler.space.render.Render;

public class Game {

	private static Render render;
	private Camera camera;
	private Workspace workspace;
	private ControlPanel controlPanel;
	
	private long tickPeriod;
	private double timeFactor;
	private double time = 0;
	
	
	
	private Complex mousePos;
	private int pressedKey;
	
	public Game() {
		
		mousePos = Complex.ZERO;
		pressedKey = 0;
		
		tickPeriod = 1000/60;
		timeFactor = 1;
		
		camera = new Camera(new Dimension(0,0));
		workspace = new Workspace(camera);
		workspace.setGetGravitationalConstant(1e-8);
		
		controlPanel = new ControlPanel(workspace);
		
		loadSolarSystem();
		
		Timer timer = new Timer();
        timer.schedule(new GameTick(), tickPeriod, tickPeriod);
	}
	
	private JComponent canvas;
	
	public void setCanvas(JComponent canvas) {
		this.canvas = canvas;
	}
	
	private class GameTick extends TimerTask {

		@Override
		public void run() {
			time += (double) tickPeriod / 1000;
        	
        	Complex transformedMousePos = workspace.getCamera().getGamePos(mousePos);
        	
        	controlPanel.tick(mousePos, pressedKey);

            workspace.tick(((double)tickPeriod * timeFactor) / 1000, transformedMousePos);
            
            if (canvas != null) {
            	canvas.revalidate();
            	canvas.repaint();
            }
		}
		
	}
	
	private void setShip(Ship ship) {
		
		workspace.setShip(ship);
		controlPanel.setShip(ship);
		camera.setFocus(ship);
	}
	
	public double getTime() {
		return time;
	}
	
	public Paintable getPaintable() {
		return new Paintable() {
			@Override
			public void paint(Graphics2D g2) {
				workspace.paint(g2);
				controlPanel.paint(g2);
			}

			@Override
			public void setImageDimension(Dimension dimension) {
				camera.setScreenDimension(dimension);
			}
		};
	}
	
	private void loadSolarSystem() {
		
		//Sun
		Planet sun = new Planet();
		sun.setPos(Complex.ZERO);
		sun.setVelocity(Complex.ZERO);
		sun.setMass(1e16);
		sun.setRadius(256);
		workspace.addPhysical(sun);
		
		//Earth
		Planet earth = new AtmospherePlanet();
		earth.setPos(new Complex(0, 15000));
		earth.setMass(1e14);
		Orbit.setInOrbit(earth, sun, workspace, Orbit.CLOCKWHISE);
		earth.setRadius(96);
		workspace.addPhysical(earth);
		
		//Mars (hostile)
		HostilePlanet mars = new HostilePlanet();
		mars.setPos(new Complex(0, -22500));
		mars.setMass(1e13);
		Orbit.setInOrbit(mars, sun, workspace, Orbit.CLOCKWHISE);
		mars.setRadius(64);
		workspace.addPhysical(mars);
		
		Ship s = new Ship();
		s.setPos(earth.getPos().plus(new Complex(0, 400)));
		s.setMass(1000);
		Orbit.setInOrbit(s, earth, workspace, Orbit.CLOCKWHISE);
		workspace.addPhysical(s);
		
		/*Ship s = new Ship();
		s.setPos(sun.getPos().plus(new Complex(0, 400)));
		s.setMass(1000);
		Orbit.setInOrbit(s, sun, w, Orbit.CLOCKWHISE);
		workspace.addPhysical(s);*/
		
		setShip(s);
	}
	
	public void setMousePos(Complex mousePos) {
		this.mousePos = mousePos;
	}
	
	private ActionMap actionMap;
	
	public static final String FIRE_PRESS = "Fire";
	public static final String FIRE_RELEASE = "Fire Release";
	
	public static final String THRUST_PRESS = "Thrust";
	public static final String THRUST_RELEASE = "Thrust Release";
	
	public static final String AQUIRE_PRESS = "Aquire";
	public static final String AQUIRE_RELEASE = "Aquire Release";
	
	public ActionMap getActionMap() {
		
		actionMap = new ActionMap();
		
		actionMap.put(FIRE_PRESS, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controlPanel.setShooting(true);
			}
			
		});
		
		actionMap.put(FIRE_RELEASE, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controlPanel.setShooting(false);
			}
			
		});
		
		actionMap.put(THRUST_PRESS, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controlPanel.setThrusting(true);
			}
			
		});
		
		actionMap.put(THRUST_RELEASE, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controlPanel.setThrusting(false);
			}
			
		});
		
		actionMap.put(AQUIRE_PRESS, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controlPanel.setAquiring(true);
			}
			
		});
		
		actionMap.put(AQUIRE_RELEASE, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controlPanel.setAquiring(false);
			}
			
		});

		return actionMap;
	}
}
