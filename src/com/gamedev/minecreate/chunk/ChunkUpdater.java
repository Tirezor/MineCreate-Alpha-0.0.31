package com.gamedev.minecreate.chunk;

import com.gamedev.minecreate.gameengine.time.Timer;
import com.gamedev.minecreate.world.World;

import java.util.*;

import org.joml.*;

public class ChunkUpdater
{
	public static ArrayList<Chunk> chunks = new ArrayList<Chunk>();
	public static HashMap<Chunk, Thread> threads = new HashMap<Chunk, Thread>();
	
	public World world;
	
	public Vector3i pos;
	
	public float time;
	public float timer;
	
	public boolean is_delete;
	
	public ChunkUpdater(final World world, final float time, final Vector3i pos)
	{	
		this.world = world;
		this.time = time;
		this.pos = pos;
		
		timer = 0.0f;
	}
	
	public static void updater()
	{
		for(int i = 0; i < chunks.size(); i++)
		{
			if(!chunks.get(i).is_update)
			{
				chunks.get(i).recreate();
				
				if(threads.containsKey(chunks.get(i)))
				{
					threads.get(chunks.get(i)).interrupt();
					threads.remove(chunks.get(i));
				}
				
				chunks.remove(i);
			}
		}
	}
	
	public void update()
	{
		if(!is_delete)
		{
			timer += Timer.delta_time;
			
			if(!chunks.contains(world.chunks.get_chunk(pos.x, pos.y, pos.z)))
			{
				if(world.chunks.get_chunk(pos.x, pos.y, pos.z) != null)
				{
					world.chunks.get_chunk(pos.x, pos.y, pos.z).is_update = true;
					chunks.add(world.chunks.get_chunk(pos.x, pos.y, pos.z));
					
					Thread thread = new Thread(() ->
					{
						world.chunks.regenerate_chunk(pos.x, pos.y, pos.z);
					});
					
					threads.put(world.chunks.get_chunk(pos.x, pos.y, pos.z), thread);
					threads.get(world.chunks.get_chunk(pos.x, pos.y, pos.z)).start();
					
					is_delete = true;
				}
			}
			
			else
			{
				is_delete = true;
			}
		}
	}
	
	public static void destroy()
	{
		chunks.clear();
		threads.clear();
	}
}