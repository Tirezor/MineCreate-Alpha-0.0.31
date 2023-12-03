package com.gamedev.minecreate.chunk;

import com.gamedev.minecreate.world.*;

import java.util.*;

import org.joml.*;

public class ChunksController
{
	public World world;
	public ChunksData data;
	
	public ChunksController(final World world)
	{
		this.world = world;
		data = new ChunksData(this);
	}
	
	public void generate_chunk(final int x, final int y, final int z)
	{
		if(x >= 0 && x < world.world_size_x && y >= 0 && y < world.world_size_y && z >= 0 && z < world.world_size_z && !data.chunks.containsKey(new Vector3f(x, y, z)))
		{
			Chunk chunk = new Chunk(new Vector3f(x, y, z), this);
			data.chunks.put(new Vector3f(x, y, z), chunk);
			data.chunks.get(new Vector3f(x, y, z)).generate();
		}
	}
	
	public void regenerate_chunk(final int x, final int y, final int z)
	{
		if(x >= 0 && x < world.world_size_x && y >= 0 && y < world.world_size_y && z >= 0 && z < world.world_size_z && data.chunks.containsKey(new Vector3f(x, y, z)))
		{
			data.chunks.get(new Vector3f(x, y, z)).regenerate();
		}
	}
	
	public void recreate_chunk(final int x, final int y, final int z)
	{
		if(x >= 0 && x < world.world_size_x && y >= 0 && y < world.world_size_y && z >= 0 && z < world.world_size_z && data.chunks.containsKey(new Vector3f(x, y, z)))
		{
			data.chunks.get(new Vector3f(x, y, z)).recreate();
		}
	}
	
	public Chunk get_chunk(final int x, final int y, final int z)
	{
		if(x >= 0 && x < world.world_size_x && y >= 0 && y < world.world_size_y && z >= 0 && z < world.world_size_z && data.chunks.containsKey(new Vector3f(x, y, z)))
		{
			return data.chunks.get(new Vector3f(x, y, z));
		}
		
		else
		{
			return null;
		}
	}
	
	public void update()
	{
		List<Chunk> chunks_to_render = new ArrayList<>();
		
		for(int y = 0; y < world.world_size_y; y++)
		{
			for(int x = 0; x < world.world_size_x; x++)
			{
				for(int z = 0; z < world.world_size_z; z++)
				{
					chunks_to_render.add(data.chunks.get(new Vector3f(x, y, z)));
				}
			}
		}
		
		Collections.sort(chunks_to_render, new Comparator<Chunk>()
		{
			@Override
			public int compare(Chunk chunk1, Chunk chunk2)
			{
				if(chunk1 != null && chunk2 != null)
				{
					float distance1 = world.window.player.pos.distance(new Vector3f(chunk1.pos.x + World.chunk_size / 2.0f - 0.5f, chunk1.pos.y + World.chunk_size / 2.0f - 0.5f, chunk1.pos.z + World.chunk_size / 2.0f - 0.5f));
					float distance2 = world.window.player.pos.distance(new Vector3f(chunk2.pos.x + World.chunk_size / 2.0f - 0.5f, chunk2.pos.y + World.chunk_size / 2.0f - 0.5f, chunk2.pos.z + World.chunk_size / 2.0f - 0.5f));
					
					return Float.compare(distance2, distance1);
				}
				
				else
				{
					float distance1 = 0;
					float distance2 = 0;
					
					return Float.compare(distance2, distance1);
				}
			}
		}
		);
		
		for(Chunk c: chunks_to_render)
		{
			if(c != null)
			{
				if(c.obj.ind_data.length > 0)
				{
					c.update(world.window.camera);
				}
			}
		}
	}
	
	public void destroy()
	{
		data.chunks.clear();
	}
}