package com.ntobler.space;

public class TikTok {
	private static long startTime;
	
	public static void tik() {
		startTime = System.currentTimeMillis();
	}

	public static void tok() {
		long stopTime = System.currentTimeMillis();
	    long elapsedTime = stopTime - startTime;
	    System.out.println(elapsedTime);
	}

    
}
