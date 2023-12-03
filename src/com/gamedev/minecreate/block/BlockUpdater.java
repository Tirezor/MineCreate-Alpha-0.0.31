package com.gamedev.minecreate.block;

import com.gamedev.minecreate.gameengine.audio.Audio;
import com.gamedev.minecreate.gameengine.time.Timer;
import com.gamedev.minecreate.resources.ResourcesMC;
import com.gamedev.minecreate.world.World;

import java.util.Random;

import org.joml.Vector3f;
import org.joml.Vector3i;

public class BlockUpdater
{
	public int x;
	public int y;
	public int z;
	
	public String update_type;
	
	public float timer;
	public float time;
	
	public boolean is_delete;
	
	public BlockUpdater(final String update_type, final int x, final int y, final int z)
	{
		is_delete = false;
		
		this.x = x;
		this.y = y;
		this.z = z;
		
		this.update_type = update_type;
		
		Random random = new Random();
		
		timer = 0.0f;
		
		if(update_type == "grass")
		{
			time = random.nextFloat(24.0f, 110.0f);
		}
		
		if(update_type == "dirt")
		{
			time = random.nextFloat(24.0f, 110.0f);
		}
		
		if(update_type == "sapling")
		{
			time = random.nextFloat(52.0f, 125.0f);
		}
		
		if(update_type == "water_h")
		{
			time = random.nextFloat(0.25f, 0.5f);
		}
		
		if(update_type == "water_v")
		{
			time = random.nextFloat(0.25f, 0.5f);
		}
		
		if(update_type == "water_n")
		{
			time = random.nextFloat(0.25f, 0.5f);
		}
	}
	
	public void update(final World world)
	{
		timer += Timer.delta_time;
		
		if(!world.is_loading)
		{
			if(update_type == "grass")
			{
				if(world.get_block(x, y, z) == BlockConverter.block_to_int(BlockData.grass) && !world.get_light(x, y + 1, z))
				{
					if(timer >= time)
					{
						world.set_block(x, y, z, BlockConverter.block_to_int(BlockData.dirt));
						
						int X = (int)(Math.floor((float)x / (float)World.chunk_size));
						int Z = (int)(Math.floor((float)z / (float)World.chunk_size));
						
						for(int _y_ = 0; _y_ < world.world_size_y; _y_++)
						{
							world.add_chunk_to_update(0.0f, new Vector3i(X, _y_, Z));
							world.add_chunk_to_update(0.0f, new Vector3i(X + 1, _y_, Z));
							world.add_chunk_to_update(0.0f, new Vector3i(X - 1, _y_, Z));
							world.add_chunk_to_update(0.0f, new Vector3i(X, _y_, Z + 1));
							world.add_chunk_to_update(0.0f, new Vector3i(X, _y_, Z - 1));
						}
						
						is_delete = true;
					}
				}
				
				else
				{
					is_delete = true;
				}
			}
			
			if(update_type == "dirt")
			{
				if(world.get_block(x, y, z) == BlockConverter.block_to_int(BlockData.dirt) && world.get_light(x, y + 1, z))
				{
					if(timer >= time)
					{
						world.set_block(x, y, z, BlockConverter.block_to_int(BlockData.grass));
						
						int X = (int)(Math.floor((float)x / (float)World.chunk_size));
						int Z = (int)(Math.floor((float)z / (float)World.chunk_size));
						
						for(int _y_ = 0; _y_ < world.world_size_y; _y_++)
						{
							world.add_chunk_to_update(0.0f, new Vector3i(X, _y_, Z));
							world.add_chunk_to_update(0.0f, new Vector3i(X + 1, _y_, Z));
							world.add_chunk_to_update(0.0f, new Vector3i(X - 1, _y_, Z));
							world.add_chunk_to_update(0.0f, new Vector3i(X, _y_, Z + 1));
							world.add_chunk_to_update(0.0f, new Vector3i(X, _y_, Z - 1));
						}
						
						is_delete = true;
					}
				}
				
				else
				{
					is_delete = true;
				}
			}
			
			if(update_type == "sapling")
			{
				if(world.get_block(x, y, z) == BlockConverter.block_to_int(BlockData.sapling) && world.get_light(x, y + 1, z))
				{
					if(timer >= time)
					{
						Random random = new Random();
						
						world.set_block(x, y - 1, z, BlockConverter.block_to_int(BlockData.dirt));
						
						int tree_height = random.nextInt(1, 4);
						tree_height--;
						
						for(int ly = tree_height + 1; ly <= tree_height + 5; ly++)
						{
							for(int lx = -1; lx <= 1; lx++)
							{
								for(int lz = -1; lz <= 1; lz++)
								{
									if(world.get_block(x + lx, y + ly, z + lz) != BlockConverter.block_to_int(BlockData.wood))
									{
										world.set_block(x + lx, y + ly, z + lz, BlockConverter.block_to_int(BlockData.leaves));
									}
								}
							}
						}
						
						for(int ly = tree_height + 2; ly <= tree_height + 4; ly++)
						{
							for(int lx = -1; lx <= 1; lx++)
							{
								for(int lz = -2; lz <= 2; lz++)
								{
									if(world.get_block(x + lx, y + ly, z + lz) != BlockConverter.block_to_int(BlockData.wood))
									{
										world.set_block(x + lx, y + ly, z + lz, BlockConverter.block_to_int(BlockData.leaves));
									}
								}
							}
						}
						
						for(int ly = tree_height + 2; ly <= tree_height + 4; ly++)
						{
							for(int lx = -2; lx <= 2; lx++)
							{
								for(int lz = -1; lz <= 1; lz++)
								{
									if(world.get_block(x + lx, y + ly, z + lz) != BlockConverter.block_to_int(BlockData.wood))
									{
										world.set_block(x + lx, y + ly, z + lz, BlockConverter.block_to_int(BlockData.leaves));
									}
								}
							}
						}
						
						for(int th = 0; th <= tree_height + 4; th++)
						{
							world.set_block(x, y + th, z, BlockConverter.block_to_int(BlockData.wood));
						}
						
						int X = (int)(Math.floor((float)x / (float)World.chunk_size));
						int Z = (int)(Math.floor((float)z / (float)World.chunk_size));
						
						for(int _y_ = 0; _y_ < world.world_size_y; _y_++)
						{
							world.add_chunk_to_update(0.0f, new Vector3i(X, _y_, Z));
							world.add_chunk_to_update(0.0f, new Vector3i(X + 1, _y_, Z));
							world.add_chunk_to_update(0.0f, new Vector3i(X - 1, _y_, Z));
							world.add_chunk_to_update(0.0f, new Vector3i(X, _y_, Z + 1));
							world.add_chunk_to_update(0.0f, new Vector3i(X, _y_, Z - 1));
							world.add_chunk_to_update(0.0f, new Vector3i(X + 1, _y_, Z + 1));
							world.add_chunk_to_update(0.0f, new Vector3i(X - 1, _y_, Z - 1));
							world.add_chunk_to_update(0.0f, new Vector3i(X + 1, _y_, Z - 1));
							world.add_chunk_to_update(0.0f, new Vector3i(X - 1, _y_, Z + 1));
						}
						
						is_delete = true;
					}
				}
				
				else
				{
					is_delete = true;
				}
			}
			
			if(update_type == "water_h")
			{
				if((x > 0 && x < world.world_size_x * World.chunk_size - 1 && y > 0 && y < world.world_size_y * World.chunk_size - 1 && z > 0 && z < world.world_size_z * World.chunk_size - 1 && world.get_block(x, y, z) == BlockConverter.block_to_int(BlockData.water) && BlockConverter.int_to_block(world.get_block(x, y - 1, z)).block_type != BlockType.liquid && BlockConverter.int_to_block(world.get_block(x, y - 1, z)).is_collision[0]) && ((BlockConverter.int_to_block(world.get_block(x, y, z + 1)).block_type != BlockType.liquid && !BlockConverter.int_to_block(world.get_block(x, y, z + 1)).is_collision[0]) || (BlockConverter.int_to_block(world.get_block(x, y, z - 1)).block_type != BlockType.liquid && !BlockConverter.int_to_block(world.get_block(x, y, z - 1)).is_collision[0]) || (BlockConverter.int_to_block(world.get_block(x + 1, y, z)).block_type != BlockType.liquid && !BlockConverter.int_to_block(world.get_block(x + 1, y, z)).is_collision[0]) || (BlockConverter.int_to_block(world.get_block(x - 1, y, z)).block_type != BlockType.liquid && !BlockConverter.int_to_block(world.get_block(x - 1, y, z)).is_collision[0])))
				{
					if(timer >= time)
					{
						try
						{
							Random random = new Random();
							
							Audio audio = new Audio(ResourcesMC.water_flow, false, false, random.nextFloat(0.85f, 1.15f), 1.0f, new Vector3f(x, y, z), world.window.player.camera.pos, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), world.window.player.camera.front, world.window.player.camera.up);
							audio.update(world.window.camera.pos, new Vector3f(0, 0, 0), world.window.camera.front, world.window.camera.up);
							audio.play();
							world.window.sounds.add(audio);
						}
						
						catch(Exception e)
						{
							e.printStackTrace();
						}
						
						if(BlockConverter.int_to_block(world.get_block(x, y - 1, z)).block_type != BlockType.liquid && BlockConverter.int_to_block(world.get_block(x, y - 1, z)).is_collision[0])
						{
							if(BlockConverter.int_to_block(world.get_block(x, y, z + 1)).block_type != BlockType.liquid && !BlockConverter.int_to_block(world.get_block(x, y, z + 1)).is_collision[0])
							{
								world.set_block(x, y, z + 1, BlockConverter.block_to_int(BlockData.water));
							}
							
							if(BlockConverter.int_to_block(world.get_block(x, y, z - 1)).block_type != BlockType.liquid && !BlockConverter.int_to_block(world.get_block(x, y, z - 1)).is_collision[0])
							{
								world.set_block(x, y, z - 1, BlockConverter.block_to_int(BlockData.water));
							}
							
							if(BlockConverter.int_to_block(world.get_block(x + 1, y, z)).block_type != BlockType.liquid && !BlockConverter.int_to_block(world.get_block(x + 1, y, z)).is_collision[0])
							{
								world.set_block(x + 1, y, z, BlockConverter.block_to_int(BlockData.water));
							}
							
							if(BlockConverter.int_to_block(world.get_block(x - 1, y, z)).block_type != BlockType.liquid && !BlockConverter.int_to_block(world.get_block(x - 1, y, z)).is_collision[0])
							{
								world.set_block(x - 1, y, z, BlockConverter.block_to_int(BlockData.water));
							}
						}
						
						int X = (int)(Math.floor((float)x / (float)World.chunk_size));
						int Z = (int)(Math.floor((float)z / (float)World.chunk_size));
						
						for(int _y_ = 0; _y_ < world.world_size_y; _y_++)
						{
							world.add_chunk_to_update(0.0f, new Vector3i(X, _y_, Z));
							world.add_chunk_to_update(0.0f, new Vector3i(X + 1, _y_, Z));
							world.add_chunk_to_update(0.0f, new Vector3i(X - 1, _y_, Z));
							world.add_chunk_to_update(0.0f, new Vector3i(X, _y_, Z + 1));
							world.add_chunk_to_update(0.0f, new Vector3i(X, _y_, Z - 1));
						}
						
						is_delete = true;
					}
				}
				
				else
				{
					is_delete = true;
				}
			}
			
			if(update_type == "water_v")
			{
				if(y > 0 && y < world.world_size_y * World.chunk_size - 1 && z > 0 && world.get_block(x, y, z) == BlockConverter.block_to_int(BlockData.water) && BlockConverter.int_to_block(world.get_block(x, y - 1, z)).block_type != BlockType.liquid && !BlockConverter.int_to_block(world.get_block(x, y - 1, z)).is_collision[0])
				{
					if(timer >= time)
					{
						try
						{
							Random random = new Random();
							
							Audio audio = new Audio(ResourcesMC.water_flow, false, false, random.nextFloat(0.85f, 1.15f), 1.0f, new Vector3f(x, y, z), world.window.player.camera.pos, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), world.window.player.camera.front, world.window.player.camera.up);
							audio.update(world.window.camera.pos, new Vector3f(0, 0, 0), world.window.camera.front, world.window.camera.up);
							audio.play();
							world.window.sounds.add(audio);
						}
						
						catch(Exception e)
						{
							e.printStackTrace();
						}
						
						if(BlockConverter.int_to_block(world.get_block(x, y - 1, z)).block_type != BlockType.liquid && !BlockConverter.int_to_block(world.get_block(x, y - 1, z)).is_collision[0])
						{
							world.set_block(x, y - 1, z, BlockConverter.block_to_int(BlockData.water));
						}
						
						int X = (int)(Math.floor((float)x / (float)World.chunk_size));
						int Z = (int)(Math.floor((float)z / (float)World.chunk_size));
						
						for(int _y_ = 0; _y_ < world.world_size_y; _y_++)
						{
							world.add_chunk_to_update(0.0f, new Vector3i(X, _y_, Z));
							world.add_chunk_to_update(0.0f, new Vector3i(X + 1, _y_, Z));
							world.add_chunk_to_update(0.0f, new Vector3i(X - 1, _y_, Z));
							world.add_chunk_to_update(0.0f, new Vector3i(X, _y_, Z + 1));
							world.add_chunk_to_update(0.0f, new Vector3i(X, _y_, Z - 1));
						}
						
						is_delete = true;
					}
				}
				
				else
				{
					is_delete = true;
				}
			}
			
			if(update_type == "water_n")
			{
				int c = 0;
				
				if(!BlockConverter.int_to_block(world.get_block(x, y, z)).is_collision[0])
				{
					if(world.get_block(x, y, z + 1) == BlockConverter.block_to_int(BlockData.water))
					{
						c++;
					}
					
					if(world.get_block(x, y, z - 1) == BlockConverter.block_to_int(BlockData.water))
					{
						c++;
					}
					
					if(world.get_block(x + 1, y, z) == BlockConverter.block_to_int(BlockData.water))
					{
						c++;
					}
					
					if(world.get_block(x - 1, y, z) == BlockConverter.block_to_int(BlockData.water))
					{
						c++;
					}
				}
				
				if(y > 0 && y < world.world_size_y * World.chunk_size - 1 && z > 0 && c >= 2)
				{
					if(timer >= time)
					{
						try
						{
							Random random = new Random();
							
							Audio audio = new Audio(ResourcesMC.water_flow, false, false, random.nextFloat(0.85f, 1.15f), 1.0f, new Vector3f(x, y, z), world.window.player.camera.pos, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), world.window.player.camera.front, world.window.player.camera.up);
							audio.update(world.window.camera.pos, new Vector3f(0, 0, 0), world.window.camera.front, world.window.camera.up);
							audio.play();
							world.window.sounds.add(audio);
						}
						
						catch(Exception e)
						{
							e.printStackTrace();
						}
						
						int nw = 0;
						
						if(!BlockConverter.int_to_block(world.get_block(x, y, z)).is_collision[0])
						{
							if(world.get_block(x, y, z + 1) == BlockConverter.block_to_int(BlockData.water))
							{
								nw++;
							}
							
							if(world.get_block(x, y, z - 1) == BlockConverter.block_to_int(BlockData.water))
							{
								nw++;
							}
							
							if(world.get_block(x + 1, y, z) == BlockConverter.block_to_int(BlockData.water))
							{
								nw++;
							}
							
							if(world.get_block(x - 1, y, z) == BlockConverter.block_to_int(BlockData.water))
							{
								nw++;
							}
						}
						
						if(nw >= 2)
						{
							world.set_block(x, y, z, BlockConverter.block_to_int(BlockData.water));
						}
						
						int X = (int)(Math.floor((float)x / (float)World.chunk_size));
						int Z = (int)(Math.floor((float)z / (float)World.chunk_size));
						
						for(int _y_ = 0; _y_ < world.world_size_y; _y_++)
						{
							world.add_chunk_to_update(0.0f, new Vector3i(X, _y_, Z));
							world.add_chunk_to_update(0.0f, new Vector3i(X + 1, _y_, Z));
							world.add_chunk_to_update(0.0f, new Vector3i(X - 1, _y_, Z));
							world.add_chunk_to_update(0.0f, new Vector3i(X, _y_, Z + 1));
							world.add_chunk_to_update(0.0f, new Vector3i(X, _y_, Z - 1));
						}
						
						is_delete = true;
					}
				}
				
				else
				{
					is_delete = true;
				}
			}
		}
	}
}