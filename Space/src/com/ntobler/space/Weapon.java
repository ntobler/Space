package com.ntobler.space;

public class Weapon implements RadialMenu.Listable {
		
	public static final int GUN = 1;
	public static final int AIM_MISSILE = 2;
	public static final int BOMB = 3;
	public static final int GROUND_AIR_MISSILE = 4;
	
	private final int id;
	private int count;
	private double fireRatePeriode;
	private double lastFiredTime;
	
	public Weapon (int id, int count, double fireRatePeriode) {
		this.id = id;
		this.count = count;
		this.fireRatePeriode = fireRatePeriode;
		this.lastFiredTime = Double.NEGATIVE_INFINITY;
	}
	
	public void use() {
		count--;
		lastFiredTime = Space.getTime();
	}
	
	public boolean isAvailable() {
		
		boolean notEmpty = (count > 0);
		boolean bulletReady = Space.getTime() > (lastFiredTime + fireRatePeriode);
		
		if (notEmpty && bulletReady) {
			lastFiredTime = Space.getTime();
			return true;
		}
		
		return false;
	}

	public int getId() {
		return id;
	}

	@Override
	public String getName() {
		switch (id) {
			case GUN: return "Gun";
			case AIM_MISSILE: return "Aim Missile";
			case BOMB: return "Bomb";
			case GROUND_AIR_MISSILE: return "Ground Air Missile";
		}
		return "";
			
	}

	@Override
	public String getDescription() {
		return "";
	}

	
}