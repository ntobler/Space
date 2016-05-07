package com.ntobler.space;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
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

	private Camera camera;
	private Workspace workspace;
	private ControlPanel controlPanel;
	
	private long tickPeriod;
	private double timeFactor;
	private double time = 0;
	
	
	
	private Complex mousePos;
	
	public Game() {
		
		mousePos = Complex.ZERO;
		//pressedKey = 0;
		
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
        	
        	controlPanel.tick(mousePos);

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
		
		/*Ship s = new Ship();
		s.setPos(mars.getPos().plus(new Complex(0, 400)));
		s.setMass(1000);
		Orbit.setInOrbit(s, mars, workspace, Orbit.CLOCKWHISE);
		workspace.addPhysical(s);*/
		
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
	
	public void setMousePos(Point mousePos) {
		this.mousePos = new Complex(mousePos.getX(), mousePos.getY());
	}
	
	
	
	public ControlEvent getShootControl(){
		return new ControlEvent(){
			@Override
			public void onStateAction(boolean state){
				controlPanel.setPrimaryShooting(state);
			}
		};
	}
	
	public ControlEvent getSecondaryShootControl(){
		return new ControlEvent(){
			@Override
			public void onStateAction(boolean state){
				controlPanel.setSecondaryShooting(state);
			}
		};
	}
	
	public ControlEvent getThrustControl(){
		return new ControlEvent(){
			@Override
			public void onStateAction(boolean state){
				controlPanel.setThrusting(state);
			}
		};
	}
	
	public ControlEvent getAquiringControl(){
		return new ControlEvent(){
			@Override
			public void onStateAction(boolean state){
				controlPanel.setAquiring(state);
			}
		};
	}
	
	public ControlEvent getWeaponSelectControl(){
		return new ControlEvent(){
			@Override
			public void onStateAction(boolean state){
				controlPanel.setWeaponSelecting(state);
			}
		};
	}
	
	public ControlEvent getUtilitySelectControl(){
		return new ControlEvent(){
			@Override
			public void onStateAction(boolean state){
				controlPanel.setUtilitySelecting(state);
			}
		};
	}
	
	public ControlEvent getZoomControl() {
		return new ControlEvent(){
			@Override
			public void onScaleAction(int value){
				camera.addZoomTicks(-value);
			}
		};
	}
	
}
