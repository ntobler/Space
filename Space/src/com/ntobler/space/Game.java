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
	
	
	
	
	private Complex mousePos;
	
	public Game() {
		
		mousePos = Complex.ZERO;
		
		tickPeriod = 1000/60;
		
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
        	
        	Complex transformedMousePos = workspace.getCamera().getGamePos(mousePos);
        	
        	controlPanel.tick(mousePos);

            workspace.tick(((double)tickPeriod) / 1000, transformedMousePos);
            
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
		return workspace.getTime();
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
		
		//Distance [km] 1e-4
		//Mass [kg] 1e-7
		
		//Sun
		Planet sun = new Planet();
		sun.setPos(Complex.ZERO);
		sun.setVelocity(Complex.ZERO);
		sun.setMass(2e19);
		sun.setRadius(6900);
		workspace.addPhysical(sun);
		
		//Mercury (hostile)
		HostilePlanet mercury = new HostilePlanet();
		mercury.setPos(Complex.fromPolar(5e4, Math.toRadians(320)));
		mercury.setMass(3.3e12);	//3.3e23
		Orbit.setInOrbit(mercury, sun, workspace, Orbit.CLOCKWHISE);
		mercury.setRadius(24);	//2.4e3
		workspace.addPhysical(mercury);
		
		//Venus (hostile)
		HostilePlanet venus = new HostilePlanet();
		venus.setPos(Complex.fromPolar(10e4, Math.toRadians(320)));
		venus.setMass(4.8e13);	//4.8e24
		Orbit.setInOrbit(venus, sun, workspace, Orbit.ANTICLOCKWHISE);
		venus.setRadius(60);	//6e3
		workspace.addPhysical(venus);
		
		//Earth
		Planet earth = new AtmospherePlanet();
		earth.setPos(Complex.fromPolar(15e4, Math.toRadians(5)));
		earth.setMass(6e13);	//6e24
		Orbit.setInOrbit(earth, sun, workspace, Orbit.CLOCKWHISE);
		earth.setRadius(63);	//6.3e3
		workspace.addPhysical(earth);
		
		//Mars (hostile)
		HostilePlanet mars = new HostilePlanet();
		mars.setPos(Complex.fromPolar(22.5e4, Math.toRadians(120)));
		mars.setMass(6.4e12);	//6.4e23
		Orbit.setInOrbit(mars, sun, workspace, Orbit.CLOCKWHISE);
		mars.setRadius(34);		//3.4e3
		workspace.addPhysical(mars);
		
		//Jupiter (hostile)
		HostilePlanet jupiter = new HostilePlanet();
		jupiter.setPos(Complex.fromPolar(77e4, Math.toRadians(200)));
		jupiter.setMass(1.9e16);	//1.9e27
		Orbit.setInOrbit(jupiter, sun, workspace, Orbit.CLOCKWHISE);
		jupiter.setRadius(714);
		workspace.addPhysical(jupiter);
		
		/*Ship s = new Ship();
		s.setPos(mars.getPos().plus(Complex.fromPolar(400, Math.toRadians(50))));
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
	
	public ControlEvent getTimeWarpFasterControl(){
		return new ControlEvent(){
			@Override
			public void onStateAction(boolean state){
				if (state) workspace.addTimeWarpTicks(1);
			}
		};
	}
	
	public ControlEvent getTimeWarpSlowerControl(){
		return new ControlEvent(){
			@Override
			public void onStateAction(boolean state){
				if (state) workspace.addTimeWarpTicks(-1);
			}
		};
	}
	
}
