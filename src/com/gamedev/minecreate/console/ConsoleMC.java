package com.gamedev.minecreate.console;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConsoleMC
{
	public static int count = 0;
	
	public static void print(final String text, final PrintType type)
	{
		LocalDateTime date_time = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
		String dt = date_time.format(formatter);
		
		String pt = "";
		
		if(type == PrintType.INFO)
		{
			pt = "INFO";
		}
		
		else if(type == PrintType.ERROR)
		{
			pt = "ERROR";
		}
		
		System.out.println("<" + pt + "> - " + dt + " - " + text);
		
		count++;
	}
}