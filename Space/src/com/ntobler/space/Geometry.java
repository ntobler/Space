package com.ntobler.space;

public class Geometry {

	public static Complex getDirection(Complex src, Complex dest) {
		return dest.minus(src).normalVector();
	}
	
	public static double getAngle(Complex ba, Complex bc) {
		
		double a = ba.getAngle();
		double b = bc.getAngle();
		return a-b;
	}
	
	public static double getDistancetoPoint(Complex p1, Complex p2, Complex a) {
	
		double upper = Math.abs(((p2.y - p1.y) * a.x) + ((p2.x - p1.x) * a.y) + (p2.x * p1.y) - (p2.y * p1.x));
		double lower = Math.sqrt(Math.pow(p2.y - p1.y, 2) + Math.pow(p2.x - p1.x, 2));
		
		return upper / lower;
	}
}
