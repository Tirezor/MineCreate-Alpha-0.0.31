package com.gamedev.minecreate.physics;

import com.gamedev.minecreate.block.*;
import com.gamedev.minecreate.player.Player;

import org.joml.*;

public class Raycast
{
public Player player;
	
	public Vector3f pos;
	public Vector3f dir;
	public Vector3f normal;
	
	public boolean is_hit;
	
	public Raycast(final Player player)
	{
		this.player = player;
	}
	
	public void hit(final Vector3f pos, final Vector3f dir, final float max_distance, final int distance_offset)
	{
		this.pos = pos;
		this.dir = dir;
		
		normal = new Vector3f();
		
		is_hit = false;
		
		Vector3f offset = new Vector3f();
		Vector3f move = new Vector3f();
		Vector3f result = new Vector3f();
		
		dir.mul(max_distance, move);
		offset.add(move);
		offset.div(max_distance * distance_offset, result);
		
		for(int i = 0; i < max_distance * distance_offset; i++)
		{
			pos.add(new Vector3f(0, result.y, 0));
			check_collision(new Vector3f(0, result.y, 0));
			
			if(is_hit)
			{
				break;
			}
			
			pos.add(new Vector3f(result.x, 0, 0));
			check_collision(new Vector3f(result.x, 0, 0));
			
			if(is_hit)
			{
				break;
			}
			
			pos.add(new Vector3f(0, 0, result.z));
			check_collision(new Vector3f(0, 0, result.z));
			
			if(is_hit)
			{
				break;
			}
		}
	}
	
	private void check_collision(final Vector3f offset)
	{
		for(int y = (int)(pos.y) - 2; y < (int)(pos.y) + 2; y++)
		{
			for(int x = (int)(pos.x) - 2; x < (int)(pos.x) + 2; x++)
			{
				for(int z = (int)(pos.z) - 2; z < (int)(pos.z) + 2; z++)
				{
					if(BlockConverter.int_to_block(player.world.get_block(x, y, z)).is_collision[1])
					{
						AABB block = new AABB(new Vector3f(x, y, z), new Vector3f(1.0f, 1.0f, 1.0f));
						AABB point = new AABB(pos, new Vector3f(0.0f, 0.0f, 0.0f));
						
						if(AABB.is_collision(point, block))
						{
							if(offset.y > 0.0f && pos.y < block.min_y() + block.size.y / 10.0f)
							{
								normal = new Vector3f(0, -1, 0);
								is_hit = true;
								break;
							}
							
							if(offset.y < 0.0f && pos.y > block.max_y() - block.size.y / 10.0f)
							{
								normal = new Vector3f(0, 1, 0);
								is_hit = true;
								break;
							}
							
							if(offset.x > 0.0f && pos.x < block.min_x() + block.size.x / 10.0f)
							{
								normal = new Vector3f(-1, 0, 0);
								is_hit = true;
								break;
							}
							
							if(offset.x < 0.0f && pos.x > block.max_x() - block.size.x / 10.0f)
							{
								normal = new Vector3f(1, 0, 0);
								is_hit = true;
								break;
							}
							
							if(offset.z > 0.0f && pos.z < block.min_z() + block.size.z / 10.0f)
							{
								normal = new Vector3f(0, 0, -1);
								is_hit = true;
								break;
							}
							
							if(offset.z < 0.0f && pos.z > block.max_z() - block.size.z / 10.0f)
							{
								normal = new Vector3f(0, 0, 1);
								is_hit = true;
								break;
							}
							
							is_hit = true;
							break;
						}
					}
				}
			}
		}
	}
}