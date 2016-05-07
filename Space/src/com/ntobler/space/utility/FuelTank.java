package com.ntobler.space.utility;

public class FuelTank {

	private static final double emptyWeight = 200;
	private static final double fuelDensity = 1;
	
	private double maxFuel;
	private double fuel;
	
	public FuelTank(double maxFuel) {
		this.maxFuel = maxFuel;
		this.fuel = maxFuel;
	}
	
	public double getFillFraction() {
		return fuel/maxFuel;
	}
	
	public double getMass() {
		return emptyWeight + (fuel * fuelDensity);
	}
	
	public void fill(double quantity) {
		fuel += quantity;
		if (fuel > maxFuel) {
			fuel = maxFuel;
		}
	}
	
	public void use(double quantity) throws Exception {
		
		if (quantity < fuel) {
			fuel -= quantity;
		}
		else {
			throw new Exception();
		}
		
	}
}
