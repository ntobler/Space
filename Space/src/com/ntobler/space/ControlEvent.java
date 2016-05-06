package com.ntobler.space;

public class ControlEvent {

	
	public static final int KEY_EVENT = 1;
	public static final int MOUSE_EVENT = 2;
	public static final int MOUSE_WHEEL_EVENT = 3;
	
	public static final int PRESSED = 1;
	public static final int RELEASED = 0;
	
	
	public void onScaleAction(int value) {
		onStateAction(value > 0);
	}
	
	public void onStateAction(boolean state){
		if (state) onAction();
	}
	
	public void onAction() {
		
	}
}
