package com.ntobler.space.render;

import java.awt.geom.Point2D;

import com.ntobler.space.render.Camera.View;

public interface Focusable {
	public Point2D getPosition();
	public double getRotation(View view);
}
