package com.ntobler.space.utility;

import com.ntobler.space.ui.RadialMenu;
import com.ntobler.space.ui.RadialMenu.Listable;

public class Utility implements RadialMenu.Listable {

	public static final int NONE = 0;
	public static final int ORBIT_CALCULATOR = 1;
	
	private final int id;
	
	public Utility (int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	@Override
	public String getName() {
		switch (id) {
			case NONE: return "None";
			case ORBIT_CALCULATOR: return "Orbit Calculator";
		}
		return "";
			
	}

	@Override
	public String getDescription() {
		return "";
	}
	
}
