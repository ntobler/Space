package com.ntobler.space;

public class Orbit {

	public final static boolean CLOCKWHISE = false;
	public final static boolean ANTICLOCKWHISE = true;
	
	private static Complex getProgradeNormalVector(Physical orbiting, Physical center) {
		Complex radialVector = Geometry.getDirection(center.getPos(), orbiting.getPos());
		Complex radialNormalVector = radialVector.normalVector();
		return radialNormalVector;
	}
	
	private static Complex getRadialNormalVector(Physical orbiting, Physical center) {
		Complex radialVector = Geometry.getDirection(center.getPos(), orbiting.getPos());
		Complex progradeNormalVector = radialVector.sub90deg().normalVector();
		return progradeNormalVector;
	}
	
	public static Complex getRoundOrbitVelocityVector(Physical orbiting, Physical center, PhysicalWorkspace w, boolean direction) {
		
		Complex rVector = orbiting.getPos().minus(center.getPos());
		double r = rVector.abs();
		double mCenter = center.getMass();
		double mOrbiting = orbiting.getMass();
		double v = Math.sqrt(w.getGetGravitationalConstant() * (mCenter + mOrbiting) / r);
		
		if (direction == ANTICLOCKWHISE) {
			v = 0 - v;
		}
		
		return rVector.add90deg().normalVector().scalarMultiply(v);
		
	}
	
	public static void setInOrbit(Physical orbiting, Physical center, PhysicalWorkspace w, boolean direction) {
		
		Complex v = getRoundOrbitVelocityVector(orbiting, center, w, direction);
		
		orbiting.setVelocity(v);
		
	}
	
	public static Complex getRoundOrbitalDeltaV(Physical orbiting, Physical center, PhysicalWorkspace w, boolean direction) {
		
		Complex v = getRoundOrbitVelocityVector(orbiting, center, w, direction);
		Complex deltaV = v.minus(orbiting.getVelocity());
		return deltaV;
		
	}
	
	public static double getProgradeVelocity (Physical orbiting, Physical center) {
		
		Complex radialVector = Geometry.getDirection(center.getPos(), orbiting.getPos());
		Complex relativeVelocity = orbiting.getVelocity().minus(center.getVelocity());
		double angle = Geometry.getAngle(relativeVelocity, radialVector);
		double progradeVelocity = Math.sin(angle) * orbiting.getVelocity().abs();
		return progradeVelocity;
	}
	
	public static double getRadialVelocity (Physical orbiting, Physical center) {
		
		Complex radialVector = Geometry.getDirection(center.getPos(), orbiting.getPos());
		Complex relativeVelocity = orbiting.getVelocity().minus(center.getVelocity());
		double angle = Geometry.getAngle(relativeVelocity, radialVector);
		double radialVelocity = Math.cos(angle) * orbiting.getVelocity().abs();
		return radialVelocity;
	}
	
	public static Complex getProgradeVelocityVec (Physical orbiting, Physical center) {
		
		Complex radialVector = Geometry.getDirection(center.getPos(), orbiting.getPos());
		Complex relativeVelocity = orbiting.getVelocity().minus(center.getVelocity());
		double angle = Geometry.getAngle(relativeVelocity, radialVector);
		double progradeVelocity = Math.sin(angle) * orbiting.getVelocity().abs();
		Complex progradeNormalVector = radialVector.sub90deg().normalVector();
		Complex progradeVelocityVector = progradeNormalVector.scalarMultiply(progradeVelocity);

		return progradeVelocityVector;
	}
	
	public static Complex getRadialVelocityVec (Physical orbiting, Physical center) {
		Complex radialVector = Geometry.getDirection(center.getPos(), orbiting.getPos());
		Complex relativeVelocity = orbiting.getVelocity().minus(center.getVelocity());
		double angle = Geometry.getAngle(relativeVelocity, radialVector);
		double radialVelocity = Math.cos(angle) * orbiting.getVelocity().abs();
		Complex radialNormalVector = radialVector.normalVector();
		Complex radialVelocityVector = radialNormalVector.scalarMultiply(radialVelocity);

		return radialVelocityVector;
	}
	
	
}