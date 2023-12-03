package com.gamedev.minecreate.gameengine.window;

import com.gamedev.minecreate.gameengine.audio.Audio;
import com.gamedev.minecreate.gameengine.camera.Camera;
import com.gamedev.minecreate.gameengine.textures.Texture;
import com.gamedev.minecreate.gameengine.time.Timer;
import com.gamedev.minecreate.particle.Particle;
import com.gamedev.minecreate.player.Player;
import com.gamedev.minecreate.resources.ResourcesMC;
import com.gamedev.minecreate.ui.UI;
import com.gamedev.minecreate.world.World;
import com.gamedev.minecreate.console.*;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.lwjgl.opengl.*;
import org.lwjgl.input.*;
import org.lwjgl.openal.*;

public class Window
{
	public String title;
	
	public int width;
	public int height;
	
	public WindowRenderer wr;
	public WindowUpdater wu;
	
	public Texture icon;

	public Camera camera;
	
	public World world;
	
	public Player player;
	
	public UI ui;
	
	public ArrayList<Audio> sounds;
	public ArrayList<Particle> particles;
	
	public int FPS;
	
	public boolean is_debug;
	
	public boolean is_exit;

	public Window(final String title, final int width, final int height)
	{
		this.title = title;
		this.width = width;
		this.height = height;
	}
	
	public void run() throws Exception
	{
		ConsoleMC.print("Welcome to " + title + "!", PrintType.INFO);
		
		init();
		loop();
		destroy();
		
		ConsoleMC.print("bye!", PrintType.INFO);
	}
	
	private void init() throws Exception
	{
		is_exit = false;
		
		Display.setDisplayMode(new DisplayMode(width, height));
		Display.setTitle(title);
		Display.setResizable(true);
		
		create_icon();
		
		Display.create();
		Keyboard.create();
		Mouse.create();
		AL.create();
		Mouse.setGrabbed(false);
		
		ResourcesMC.create();
		
		console_check();
		
		Timer.first_frame = true;
		Timer.update();
		
		wr = new WindowRenderer(this);
		wu = new WindowUpdater(this);
	}
	
	private void loop() throws Exception
	{
		while(!Display.isCloseRequested())
		{
			if(is_exit)
			{
				break;
			}
			
			wu.update();
			wr.update();
			
			Display.update();
			
			Timer.update();
			
			if(Timer.first_frame)
			{
				Timer.first_frame = false;
			}
		}
	}
	
	private void destroy() throws Exception
	{
		wu.destroy();
		wr.destroy();
		icon.destroy();
		ResourcesMC.destroy();
		AL.destroy();
		Mouse.destroy();
		Keyboard.destroy();
		Display.destroy();
	}
	
	private void create_icon()
	{
		icon = new Texture("/textures/icon.png", false);
		ByteBuffer[] bb = new ByteBuffer[2];
		bb[0] = icon.buf;
		bb[1] = icon.buf;
		
		Display.setIcon(bb);
	}
	
	private void console_check()
	{
		if(Display.isCreated())
		{
			ConsoleMC.print("display is working!", PrintType.INFO);
		}
		
		else
		{
			ConsoleMC.print("display is NOT working!", PrintType.ERROR);
		}
		
		if(Keyboard.isCreated())
		{
			ConsoleMC.print("keyboard is working!", PrintType.INFO);
		}
		
		else
		{
			ConsoleMC.print("keyboard is NOT working!", PrintType.ERROR);
		}
		
		if(Mouse.isCreated())
		{
			ConsoleMC.print("mouse is working!", PrintType.INFO);
		}
		
		else
		{
			ConsoleMC.print("mouse is NOT working!", PrintType.ERROR);
		}
		
		if(AL.isCreated())
		{
			ConsoleMC.print("openAL is working!", PrintType.INFO);
		}
		
		else
		{
			ConsoleMC.print("openAL is NOT working!", PrintType.ERROR);
		}
	}
}