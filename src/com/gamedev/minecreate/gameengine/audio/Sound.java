package com.gamedev.minecreate.gameengine.audio;

import com.gamedev.minecreate.console.*;

import java.io.BufferedInputStream;

import org.lwjgl.util.*;

public class Sound
{
	public WaveData wave_data;
	
	public Sound(final String file_name)
	{
		try
		{
			wave_data = WaveData.create(new BufferedInputStream(Audio.class.getResourceAsStream(file_name)));
			
			ConsoleMC.print("<" + file_name + "> - sound loaded successfully!", PrintType.INFO);
		}
		
		catch(Exception e)
		{
			ConsoleMC.print("<" + file_name + "> - sound NOT loaded!", PrintType.ERROR);
			
			e.printStackTrace();
		}
	}
	
	public WaveData get_data()
	{
		return wave_data;
	}
}