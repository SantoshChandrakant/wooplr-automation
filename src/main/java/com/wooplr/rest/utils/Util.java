package com.wooplr.rest.utils;

public class Util {

	/**
	 * Waits explicitly for given number of seconds
	 * 
	 * @param seconds
	 */
	public static void expliciWait(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
