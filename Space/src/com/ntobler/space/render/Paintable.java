package com.ntobler.space.render;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public interface Paintable {
	public void paint (Graphics2D g2);
	public void setImageDimension (Dimension dimension);
}
