package com.gamedev.minecreate.gameengine.window;

import com.gamedev.minecreate.gameengine.audio.Audio;
import com.gamedev.minecreate.gameengine.time.Timer;
import com.gamedev.minecreate.particle.Particle;
import com.gamedev.minecreate.player.Player;
import com.gamedev.minecreate.ui.UI;
import com.gamedev.minecreate.world.World;

import java.util.ArrayList;

import org.joml.*;

public class WindowUpdater
{
	public Window window;
	
	private float fps_timer;
	
	private boolean[] is_create;
	
	public WindowUpdater(final Window window)
	{
		this.window = window;
		
		window.is_debug = false;
		
		window.sounds = new ArrayList<Audio>();
		window.particles = new ArrayList<Particle>();
		
		window.world = new World(window);
		
		window.player = new Player(window.world, new Vector3f(window.world.world_size_x * World.chunk_size / 2.0f, 0.0f, window.world.world_size_z * World.chunk_size / 2.0f), 3.5f, 0.325f, 8.0f, 20.0f);
		
		window.world.is_loading = false;
		
		window.ui = new UI(window);
		
		window.camera = window.player.camera;
		
		window.FPS = Timer.fps();
		fps_timer = 0.0f;
		
		is_create = new boolean[2];
		is_create[0] = false;
		is_create[1] = true;
	}
	
	public void update()
	{
		update_FPS();
		
		check();
		
		window.player.update();
		window.camera = window.player.camera;
		window.camera.update();
		
		update_sounds();
	}
	
	public void destroy()
	{
		for(int i = 0; i < window.sounds.size(); i++)
		{
			window.sounds.get(i).destroy();
			window.sounds.remove(i);
		}
		
		window.sounds.clear();
		window.particles.clear();
		window.ui.destroy();
		window.player.destroy();
		window.world.destroy();
	}
	
	private void check()
	{
		if(window.world.is_loading || window.player.is_mm)
		{
			window.is_debug = false;
		}
		
		if(window.player.is_mm)
		{
			window.player.is_pause = true;
		}
	}
	
	private void update_FPS()
	{
		fps_timer += Timer.delta_time;
		
		if(fps_timer >= 0.5f)
		{
			fps_timer = 0.0f;
			window.FPS = Timer.fps();
		}
	}
	
	private void update_sounds()
	{
		for(int i = 0; i < window.sounds.size(); i++)
		{
			if(window.sounds.get(i).is_static)
			{
				window.sounds.get(i).update(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
			}
			
			else
			{
				window.sounds.get(i).update(window.camera.pos, new Vector3f(0, 0, 0), window.camera.front, window.camera.up);
			}
			
			if(window.sounds.get(i).is_delete)
			{
				window.sounds.get(i).destroy();
				window.sounds.remove(i);
			}
		}
	}
}