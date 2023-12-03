package com.gamedev.minecreate.main;

import com.gamedev.minecreate.gameengine.window.Window;

public class Main
{
	public static void main(String[] args)
	{
		Window window = new Window("MineCreate Alpha 0.0.31", 1200, 768);
		
		try
		{
			window.run();
		}
		
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}