package com.gamedev.minecreate.gameengine.window;

import com.gamedev.minecreate.gameengine.time.Timer;
import com.gamedev.minecreate.world.World;

import org.lwjgl.opengl.Display;

import org.newdawn.slick.*;

import static org.lwjgl.opengl.GL11.*;

public class WindowRenderer
{
	public Window window;
	
	public static Color color;
	
	public static float fog_start;
	public static float fog_end;
	
	private float light;
	
	public WindowRenderer(final Window window)
	{
		this.window = window;
		
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LESS);
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		light = 1.0f;
	}
	
	public void update()
	{
		update_fog();
		
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glClearColor(color.r, color.g, color.b, color.a);
		glViewport(0, 0, Display.getWidth(), Display.getHeight());
		
		window.world.sky.update(window.camera);
		window.world.ocean.update(window.camera);
		
		update_particles();
		
		window.player.selector.update(window.camera);
		
		window.world.update();
		
		glDisable(GL_DEPTH_TEST);
		window.player.block_h.update(window.camera, window.world);
		
		try
		{
			window.ui.update(window.camera, window);
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		glEnable(GL_DEPTH_TEST);
	}
	
	public void destroy()
	{
		for(int i = 0; i < window.particles.size(); i++)
		{
			window.particles.remove(i);
		}
	}
	
	private void update_fog()
	{
		if(window.player.is_underground)
		{
			light -= 1.0f * Timer.delta_time;
			
			if(window.player.radius == 50)
			{
				if(light < 1.0f)
				{
					light = 1.0f;
				}
			}
			
			if(window.player.radius == 8)
			{
				if(light < (1.0f / 3.0f) * 2)
				{
					light = (1.0f / 3.0f) * 2;
				}
			}
			
			if(window.player.radius == 4)
			{
				if(light < (1.0f / 3.0f))
				{
					light = (1.0f / 3.0f);
				}
			}
			
			if(window.player.radius == 1)
			{
				if(light < 0.0f)
				{
					light = 0.0f;
				}
			}
		}
		
		else
		{
			light += 1.0f * Timer.delta_time;
			
			if(light > 1.0f)
			{
				light = 1.0f;
			}
		}
		
		if(window.player.is_underwater[0])
		{
			color = new Color(0.0f, 0.0f, 0.25f, 1.0f);
			
			fog_start = 0.1f;
			fog_end = 8.0f;
		}
		
		else
		{
			color = new Color(light, light, light, 1.0f);
			
			fog_start = window.player.radius * World.chunk_size - World.chunk_size * window.player.radius;
			fog_end = window.player.radius * World.chunk_size;
		}
	}
	
	private void update_particles()
	{
		for(int i = 0; i < window.particles.size(); i++)
		{
			window.particles.get(i).update(window.camera, window.player);
			
			if(window.particles.get(i).timer <= 0.0f)
			{
				window.particles.remove(i);
			}
		}
	}
}