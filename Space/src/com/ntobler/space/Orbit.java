package com.ntobler.space;

import com.ntobler.space.physical.Physical;

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
	
	public static Complex getRelativeRoundOrbitVelocityVector(Physical orbiting, Physical center, Workspace w, boolean direction) {
		
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
	
	public static void setInOrbit(Physical orbiting, Physical center, Workspace w, boolean direction) {
		
		Complex v = getRelativeRoundOrbitVelocityVector(orbiting, center, w, direction);
		v = v.plus(center.getVelocity());
		orbiting.setVelocity(v);
		
	}
	
	public static Complex getRoundOrbitalDeltaV(Physical orbiting, Physical center, Workspace w, boolean direction) {
		
		Complex v = getRelativeRoundOrbitVelocityVector(orbiting, center, w, direction);
		Complex deltaV = v.minus(orbiting.getVelocity());
		return deltaV;
		
	}
	
	public static double getProgradeVelocity (Physical orbiting, Physical center) {
		
		Complex radialVector = orbiting.getPos().minus(center.getPos());
		Complex relativeVelocity = orbiting.getVelocity().minus(center.getVelocity());
		double angle = Geometry.getAngle(relativeVelocity, radialVector);
		double progradeVelocity = Math.sin(angle) * relativeVelocity.abs();
		return progradeVelocity;
	}
	
	public static double getRadialVelocity (Physical orbiting, Physical center) {
		
		Complex radialVector = Geometry.getDirection(center.getPos(), orbiting.getPos());
		Complex relativeVelocity = orbiting.getVelocity().minus(center.getVelocity());
		double angle = Geometry.getAngle(relativeVelocity, radialVector);
		double radialVelocity = Math.cos(angle) * relativeVelocity.abs();
		return radialVelocity;
	}
	
	public static Complex getProgradeVelocityVec (Physical orbiting, Physical center) {
		
		Complex radialVector = Geometry.getDirection(center.getPos(), orbiting.getPos());
		Complex relativeVelocity = orbiting.getVelocity().minus(center.getVelocity());
		double angle = Geometry.getAngle(relativeVelocity, radialVector);
		double progradeVelocity = Math.sin(angle) * relativeVelocity.abs();
		Complex progradeNormalVector = radialVector.sub90deg().normalVector();
		Complex progradeVelocityVector = progradeNormalVector.scalarMultiply(progradeVelocity);

		return progradeVelocityVector;
	}
	
	public static Complex getRadialVelocityVec (Physical orbiting, Physical center) {
		Complex radialVector = Geometry.getDirection(center.getPos(), orbiting.getPos());
		Complex relativeVelocity = orbiting.getVelocity().minus(center.getVelocity());
		double angle = Geometry.getAngle(relativeVelocity, radialVector);
		double radialVelocity = Math.cos(angle) * relativeVelocity.abs();
		Complex radialNormalVector = radialVector.normalVector();
		Complex radialVelocityVector = radialNormalVector.scalarMultiply(radialVelocity);

		return radialVelocityVector;
	}
	
	
}
