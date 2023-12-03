package com.gamedev.minecreate.gameengine.time;

public class Timer
{
	public static long last_time;
	public static float delta_time;
	public static boolean first_frame;
	
	public static long get_time()
	{
		return System.nanoTime();
	}
	
	public static int fps()
	{
		return (int)(1.0f / delta_time);
	}
	
	public static void update()
	{
		long now = get_time();
		
		long result = now - last_time;
		
		last_time = now;
		
		delta_time = (float)(result / 1000000000.0f);
	}
}