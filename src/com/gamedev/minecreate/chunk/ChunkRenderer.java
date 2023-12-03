package com.gamedev.minecreate.chunk;

import com.gamedev.minecreate.world.*;
import com.gamedev.minecreate.block.*;

import java.util.*;

import org.joml.*;

public class ChunkRenderer
{
	public Chunk chunk;
	
	public ArrayList<Float> vert_data;
	public ArrayList<Float> col_data;
	public ArrayList<Float> t_coord_data;
	public ArrayList<Integer> ind_data;
	
	public float[] vert;
	public float[] col;
	public float[] tex_c;
	public int[] ind;
	
	public static float ot = 0.0625f;
	
	public static float shadow = 3.0f;
	
	private Vector2i[] t_x;
	
	private int count;
	
	public ChunkRenderer(final Chunk chunk)
	{
		this.chunk = chunk;
		this.count = 0;
		
		vert_data = new ArrayList<Float>();
		col_data = new ArrayList<Float>();
		t_coord_data = new ArrayList<Float>();
		ind_data = new ArrayList<Integer>();
		
		t_x = new Vector2i[6];
		
		t_x[0] = new Vector2i();
		t_x[1] = new Vector2i();
		t_x[2] = new Vector2i();
		t_x[3] = new Vector2i();
		t_x[4] = new Vector2i();
		t_x[5] = new Vector2i();
	}
	
	public void update()
	{
		count = 0;
		
		vert_data.clear();
		col_data.clear();
		t_coord_data.clear();
		ind_data.clear();
		
		for(int y = (int)chunk.pos.y; y < (int)chunk.pos.y + World.chunk_size; y++)
		{
			for(int x = (int)chunk.pos.x; x < (int)chunk.pos.x + World.chunk_size; x++)
			{
				for(int z = (int)chunk.pos.z; z < (int)chunk.pos.z + World.chunk_size; z++)
				{
					if(BlockConverter.int_to_block(chunk.controller.world.get_block(x, y, z)).block_type == BlockType.plant)
					{
						if(BlockConverter.int_to_block(chunk.controller.world.get_block(x, y - 1, z)).block_type != BlockType.solid)
						{
							chunk.controller.world.set_block(x, y, z, BlockConverter.block_to_int(BlockData.air));
						}
					}
					
					if(BlockConverter.int_to_block(chunk.controller.world.get_block(x, y, z)) == BlockData.chamomile ||BlockConverter.int_to_block(chunk.controller.world.get_block(x, y, z)) == BlockData.rose ||BlockConverter.int_to_block(chunk.controller.world.get_block(x, y, z)) == BlockData.dandelion || BlockConverter.int_to_block(chunk.controller.world.get_block(x, y, z)) == BlockData.sapling)
					{
						if(BlockConverter.int_to_block(chunk.controller.world.get_block(x, y - 1, z)) != BlockData.grass && BlockConverter.int_to_block(chunk.controller.world.get_block(x, y - 1, z)) != BlockData.dirt)
						{
							chunk.controller.world.set_block(x, y, z, BlockConverter.block_to_int(BlockData.air));
						}
					}
					
					if(BlockConverter.int_to_block(chunk.controller.world.get_block(x, y, z)).is_gravity)
					{
						if(!BlockConverter.int_to_block(chunk.controller.world.get_block(x, y - 1, z)).is_collision[0])
						{
							Block gb = BlockConverter.int_to_block(chunk.controller.world.get_block(x, y, z));
							
							chunk.controller.world.set_block(x, y, z, BlockConverter.block_to_int(BlockData.air));
							
							for(int Y = y; Y >= 0; Y--)
							{
								if(BlockConverter.int_to_block(chunk.controller.world.get_block(x, Y, z)).is_collision[0])
								{
									chunk.controller.world.set_block(x, Y + 1, z, BlockConverter.block_to_int(gb));
									break;
								}
							}
							
							if(BlockConverter.int_to_block(chunk.controller.world.get_block(x, y + 1, z)).is_gravity)
							{
								check_gravity_block(x, y + 1, z);
							}
						}
					}
					
					if(x == 0 || z == 0 || x == chunk.controller.world.world_size_x * World.chunk_size - 1 || z == chunk.controller.world.world_size_z * World.chunk_size - 1)
					{
						if(y <= chunk.controller.world.water_height && !BlockConverter.int_to_block(chunk.controller.world.get_block(x, y, z)).is_collision[0] && BlockConverter.int_to_block(chunk.controller.world.get_block(x, y, z)).block_type != BlockType.liquid)
						{
							chunk.controller.world.set_block(x, y, z, BlockConverter.block_to_int(BlockData.water));
						}
					}
					
					if(y <= chunk.controller.world.water_height && chunk.controller.world.get_block(x, y, z) == BlockConverter.block_to_int(BlockData.water))
					{
						chunk.controller.world.set_block(x, y, z, BlockConverter.block_to_int(BlockData.water));
						
						if(x == 0)
						{
							if(!BlockConverter.int_to_block(chunk.controller.world.get_block(x + 1, y, z)).is_collision[0] && BlockConverter.int_to_block(chunk.controller.world.get_block(x + 1, y, z)).block_type != BlockType.liquid)
							{
								chunk.controller.world.set_block(x + 1, y, z, BlockConverter.block_to_int(BlockData.water));
							}
						}
						
						if(x == chunk.controller.world.world_size_x * World.chunk_size - 1)
						{
							if(!BlockConverter.int_to_block(chunk.controller.world.get_block(x - 1, y, z)).is_collision[0] && BlockConverter.int_to_block(chunk.controller.world.get_block(x - 1, y, z)).block_type != BlockType.liquid)
							{
								chunk.controller.world.set_block(x - 1, y, z, BlockConverter.block_to_int(BlockData.water));
							}
						}
						
						if(z == 0)
						{
							if(!BlockConverter.int_to_block(chunk.controller.world.get_block(x, y, z + 1)).is_collision[0] && BlockConverter.int_to_block(chunk.controller.world.get_block(x, y, z + 1)).block_type != BlockType.liquid)
							{
								chunk.controller.world.set_block(x, y, z + 1, BlockConverter.block_to_int(BlockData.water));
							}
						}
						
						if(z == chunk.controller.world.world_size_z * World.chunk_size - 1)
						{
							if(!BlockConverter.int_to_block(chunk.controller.world.get_block(x, y, z - 1)).is_collision[0] && BlockConverter.int_to_block(chunk.controller.world.get_block(x, y, z - 1)).block_type != BlockType.liquid)
							{
								chunk.controller.world.set_block(x, y, z - 1, BlockConverter.block_to_int(BlockData.water));
							}
						}
					}
					
					if(chunk.controller.world.get_block(x, y, z) == BlockConverter.block_to_int(BlockData.grass))
					{
						chunk.controller.world.update_blocks.add(new BlockUpdater("grass", x, y, z));
					}
					
					if(chunk.controller.world.get_block(x, y, z) == BlockConverter.block_to_int(BlockData.dirt))
					{
						chunk.controller.world.update_blocks.add(new BlockUpdater("dirt", x, y, z));
					}
					
					if(chunk.controller.world.get_block(x, y, z) == BlockConverter.block_to_int(BlockData.sapling))
					{
						chunk.controller.world.update_blocks.add(new BlockUpdater("sapling", x, y, z));
					}
					
					if(chunk.controller.world.get_block(x, y, z) == BlockConverter.block_to_int(BlockData.water))
					{
						if((x > 0 && x < chunk.controller.world.world_size_x * World.chunk_size - 1 && y > 0 && y < chunk.controller.world.world_size_y * World.chunk_size - 1 && z > 0 && z < chunk.controller.world.world_size_z * World.chunk_size - 1 && chunk.controller.world.get_block(x, y, z) == BlockConverter.block_to_int(BlockData.water) && BlockConverter.int_to_block(chunk.controller.world.get_block(x, y - 1, z)).block_type != BlockType.liquid && BlockConverter.int_to_block(chunk.controller.world.get_block(x, y - 1, z)).is_collision[0]) && ((BlockConverter.int_to_block(chunk.controller.world.get_block(x, y, z + 1)).block_type != BlockType.liquid && !BlockConverter.int_to_block(chunk.controller.world.get_block(x, y, z + 1)).is_collision[0]) || (BlockConverter.int_to_block(chunk.controller.world.get_block(x, y, z - 1)).block_type != BlockType.liquid && !BlockConverter.int_to_block(chunk.controller.world.get_block(x, y, z - 1)).is_collision[0]) || (BlockConverter.int_to_block(chunk.controller.world.get_block(x + 1, y, z)).block_type != BlockType.liquid && !BlockConverter.int_to_block(chunk.controller.world.get_block(x + 1, y, z)).is_collision[0]) || (BlockConverter.int_to_block(chunk.controller.world.get_block(x - 1, y, z)).block_type != BlockType.liquid && !BlockConverter.int_to_block(chunk.controller.world.get_block(x - 1, y, z)).is_collision[0])))
						{
							chunk.controller.world.update_blocks.add(new BlockUpdater("water_h", x, y, z));
						}
						
						if(y > 0 && y < chunk.controller.world.world_size_y * World.chunk_size - 1 && z > 0 && chunk.controller.world.get_block(x, y, z) == BlockConverter.block_to_int(BlockData.water) && BlockConverter.int_to_block(chunk.controller.world.get_block(x, y - 1, z)).block_type != BlockType.liquid && !BlockConverter.int_to_block(chunk.controller.world.get_block(x, y - 1, z)).is_collision[0])
						{
							chunk.controller.world.update_blocks.add(new BlockUpdater("water_v", x, y, z));
						}
					}
					
					if(chunk.controller.world.get_block(x, y, z) == BlockConverter.block_to_int(BlockData.air))
					{
						int nw = 0;
						
						if(chunk.controller.world.get_block(x, y, z + 1) == BlockConverter.block_to_int(BlockData.water))
						{
							nw++;
						}
						
						if(chunk.controller.world.get_block(x, y, z - 1) == BlockConverter.block_to_int(BlockData.water))
						{
							nw++;
						}
						
						if(chunk.controller.world.get_block(x + 1, y, z) == BlockConverter.block_to_int(BlockData.water))
						{
							nw++;
						}
						
						if(chunk.controller.world.get_block(x - 1, y, z) == BlockConverter.block_to_int(BlockData.water))
						{
							nw++;
						}
						
						if(nw >= 2)
						{
							chunk.controller.world.update_blocks.add(new BlockUpdater("water_n", x, y, z));
						}
					}
				}
			}
		}
		
		for(int x = (int)chunk.pos.x; x < (int)chunk.pos.x + World.chunk_size; x++)
		{
			for(int z = (int)chunk.pos.z; z < (int)chunk.pos.z + World.chunk_size; z++)
			{
				boolean is_light = true;
				
				for(int Y = chunk.controller.world.world_size_y * World.chunk_size - 1; Y >= 0; Y--)
				{
					if(!BlockConverter.int_to_block(chunk.controller.world.get_block(x, Y, z)).is_transmits_light)
					{
						is_light = false;
					}
					
					chunk.controller.world.set_light(x, Y, z, is_light);
				}
			}
		}
		
		for(int y = (int)chunk.pos.y; y < (int)chunk.pos.y + World.chunk_size; y++)
		{
			for(int x = (int)chunk.pos.x; x < (int)chunk.pos.x + World.chunk_size; x++)
			{
				for(int z = (int)chunk.pos.z; z < (int)chunk.pos.z + World.chunk_size; z++)
				{	
					render_block(x, y, z, false);
				}
			}
		}
		
		for(int y = (int)chunk.pos.y; y < (int)chunk.pos.y + World.chunk_size; y++)
		{
			for(int x = (int)chunk.pos.x; x < (int)chunk.pos.x + World.chunk_size; x++)
			{
				for(int z = (int)chunk.pos.z; z < (int)chunk.pos.z + World.chunk_size; z++)
				{
					render_block(x, y, z, true);
				}
			}
		}
		
		convert();
	}
	
	private void render_block(final int x, final int y, final int z, final boolean is_water)
	{
		t_x[0] = BlockConverter.int_to_block(chunk.controller.world.get_block(x, y, z)).tex_coords[0];
		t_x[1] = BlockConverter.int_to_block(chunk.controller.world.get_block(x, y, z)).tex_coords[1];
		t_x[2] = BlockConverter.int_to_block(chunk.controller.world.get_block(x, y, z)).tex_coords[2];
		t_x[3] = BlockConverter.int_to_block(chunk.controller.world.get_block(x, y, z)).tex_coords[3];
		t_x[4] = BlockConverter.int_to_block(chunk.controller.world.get_block(x, y, z)).tex_coords[4];
		t_x[5] = BlockConverter.int_to_block(chunk.controller.world.get_block(x, y, z)).tex_coords[5];
		
		if(is_water)
		{
			if(BlockConverter.int_to_block(chunk.controller.world.get_block(x, y, z)).block_type == BlockType.liquid)
			{
				if(BlockConverter.int_to_block(chunk.controller.world.get_block(x, y + 1, z)).block_type == BlockType.air || (BlockConverter.int_to_block(chunk.controller.world.get_block(x, y + 1, z)).render_block_type != RenderBlockType.render_when_air && BlockConverter.int_to_block(chunk.controller.world.get_block(x, y + 1, z)).block_type != BlockType.liquid))
				{
					vert_add(new Vector3f(-0.5f + x, 0.5f + y, -0.5f + z));
					vert_add(new Vector3f(-0.5f + x, 0.5f + y, 0.5f + z));
					vert_add(new Vector3f(0.5f + x, 0.5f + y, 0.5f + z));
					vert_add(new Vector3f(0.5f + x, 0.5f + y, -0.5f + z));
					
					block_check(1.0f, chunk.controller.world.get_light(x, y + 1, z));
					
					t_coord_add(new Vector2f(t_x[0].x * ot + ot, t_x[0].y * ot + ot));
					t_coord_add(new Vector2f(t_x[0].x * ot + ot, t_x[0].y * ot));
					t_coord_add(new Vector2f(t_x[0].x * ot, t_x[0].y * ot));
					t_coord_add(new Vector2f(t_x[0].x * ot, t_x[0].y * ot + ot));
					
					add_ind();
					
					vert_add(new Vector3f(-0.5f + x, 0.5f + y, -0.5f + z));
					vert_add(new Vector3f(0.5f + x, 0.5f + y, -0.5f + z));
					vert_add(new Vector3f(0.5f + x, 0.5f + y, 0.5f + z));
					vert_add(new Vector3f(-0.5f + x, 0.5f + y, 0.5f + z));
					
					block_check(0.35f, chunk.controller.world.get_light(x, y, z));
					
					t_coord_add(new Vector2f(t_x[1].x * ot + ot, t_x[1].y * ot));
					t_coord_add(new Vector2f(t_x[1].x * ot, t_x[1].y * ot));
					t_coord_add(new Vector2f(t_x[1].x * ot, t_x[1].y * ot + ot));
					t_coord_add(new Vector2f(t_x[1].x * ot + ot, t_x[1].y * ot + ot));
					
					add_ind();
				}
				
				if(BlockConverter.int_to_block(chunk.controller.world.get_block(x, y - 1, z)).block_type == BlockType.air || (BlockConverter.int_to_block(chunk.controller.world.get_block(x, y - 1, z)).render_block_type != RenderBlockType.render_when_air && BlockConverter.int_to_block(chunk.controller.world.get_block(x, y - 1, z)).block_type != BlockType.liquid))
				{
					vert_add(new Vector3f(-0.5f + x, -0.5f + y, -0.5f + z));
					vert_add(new Vector3f(0.5f + x, -0.5f + y, -0.5f + z));
					vert_add(new Vector3f(0.5f + x, -0.5f + y, 0.5f + z));
					vert_add(new Vector3f(-0.5f + x, -0.5f + y, 0.5f + z));
					
					block_check(0.35f, chunk.controller.world.get_light(x, y - 1, z));
					
					t_coord_add(new Vector2f(t_x[1].x * ot + ot, t_x[1].y * ot));
					t_coord_add(new Vector2f(t_x[1].x * ot, t_x[1].y * ot));
					t_coord_add(new Vector2f(t_x[1].x * ot, t_x[1].y * ot + ot));
					t_coord_add(new Vector2f(t_x[1].x * ot + ot, t_x[1].y * ot + ot));
					
					add_ind();
					
					vert_add(new Vector3f(-0.5f + x, -0.5f + y, -0.5f + z));
					vert_add(new Vector3f(-0.5f + x, -0.5f + y, 0.5f + z));
					vert_add(new Vector3f(0.5f + x, -0.5f + y, 0.5f + z));
					vert_add(new Vector3f(0.5f + x, -0.5f + y, -0.5f + z));
					
					block_check(1.0f, chunk.controller.world.get_light(x, y, z));
					
					t_coord_add(new Vector2f(t_x[0].x * ot + ot, t_x[0].y * ot + ot));
					t_coord_add(new Vector2f(t_x[0].x * ot + ot, t_x[0].y * ot));
					t_coord_add(new Vector2f(t_x[0].x * ot, t_x[0].y * ot));
					t_coord_add(new Vector2f(t_x[0].x * ot, t_x[0].y * ot + ot));
					
					add_ind();
				}
				
				if(BlockConverter.int_to_block(chunk.controller.world.get_block(x, y, z + 1)).block_type == BlockType.air || (BlockConverter.int_to_block(chunk.controller.world.get_block(x, y, z + 1)).render_block_type != RenderBlockType.render_when_air && BlockConverter.int_to_block(chunk.controller.world.get_block(x, y, z + 1)).block_type != BlockType.liquid))
				{
					vert_add(new Vector3f(-0.5f + x, -0.5f + y, 0.5f + z));
					vert_add(new Vector3f(0.5f + x, -0.5f + y, 0.5f + z));
					vert_add(new Vector3f(0.5f + x, 0.5f + y, 0.5f + z));
					vert_add(new Vector3f(-0.5f + x, 0.5f + y, 0.5f + z));
					
					block_check(0.75f, chunk.controller.world.get_light(x, y, z + 1));
					
					t_coord_add(new Vector2f(t_x[2].x * ot, t_x[2].y * ot + ot));
					t_coord_add(new Vector2f(t_x[2].x * ot + ot, t_x[2].y * ot + ot));
					t_coord_add(new Vector2f(t_x[2].x * ot + ot, t_x[2].y * ot));
					t_coord_add(new Vector2f(t_x[2].x * ot, t_x[2].y * ot));
					
					add_ind();
					
					if(z < chunk.controller.world.world_size_z * World.chunk_size - 1)
					{
						vert_add(new Vector3f(-0.5f + x, -0.5f + y, 0.5f + z));
						vert_add(new Vector3f(-0.5f + x, 0.5f + y, 0.5f + z));
						vert_add(new Vector3f(0.5f + x, 0.5f + y, 0.5f + z));
						vert_add(new Vector3f(0.5f + x, -0.5f + y, 0.5f + z));
						
						block_check(0.7f, chunk.controller.world.get_light(x, y, z));
						
						t_coord_add(new Vector2f(t_x[3].x * ot + ot, t_x[3].y * ot + ot));
						t_coord_add(new Vector2f(t_x[3].x * ot + ot, t_x[3].y * ot));
						t_coord_add(new Vector2f(t_x[3].x * ot, t_x[3].y * ot));
						t_coord_add(new Vector2f(t_x[3].x * ot, t_x[3].y * ot + ot));
						
						add_ind();
					}
				}
				
				if(BlockConverter.int_to_block(chunk.controller.world.get_block(x, y, z - 1)).block_type == BlockType.air || (BlockConverter.int_to_block(chunk.controller.world.get_block(x, y, z - 1)).render_block_type != RenderBlockType.render_when_air && BlockConverter.int_to_block(chunk.controller.world.get_block(x, y, z - 1)).block_type != BlockType.liquid))
				{
					vert_add(new Vector3f(-0.5f + x, -0.5f + y, -0.5f + z));
					vert_add(new Vector3f(-0.5f + x, 0.5f + y, -0.5f + z));
					vert_add(new Vector3f(0.5f + x, 0.5f + y, -0.5f + z));
					vert_add(new Vector3f(0.5f + x, -0.5f + y, -0.5f + z));
					
					block_check(0.7f, chunk.controller.world.get_light(x, y, z - 1));
					
					t_coord_add(new Vector2f(t_x[3].x * ot + ot, t_x[3].y * ot + ot));
					t_coord_add(new Vector2f(t_x[3].x * ot + ot, t_x[3].y * ot));
					t_coord_add(new Vector2f(t_x[3].x * ot, t_x[3].y * ot));
					t_coord_add(new Vector2f(t_x[3].x * ot, t_x[3].y * ot + ot));
					
					add_ind();
					
					if(z > 0)
					{
						vert_add(new Vector3f(-0.5f + x, -0.5f + y, -0.5f + z));
						vert_add(new Vector3f(0.5f + x, -0.5f + y, -0.5f + z));
						vert_add(new Vector3f(0.5f + x, 0.5f + y, -0.5f + z));
						vert_add(new Vector3f(-0.5f + x, 0.5f + y, -0.5f + z));
						
						block_check(0.75f, chunk.controller.world.get_light(x, y, z));
						
						t_coord_add(new Vector2f(t_x[2].x * ot, t_x[2].y * ot + ot));
						t_coord_add(new Vector2f(t_x[2].x * ot + ot, t_x[2].y * ot + ot));
						t_coord_add(new Vector2f(t_x[2].x * ot + ot, t_x[2].y * ot));
						t_coord_add(new Vector2f(t_x[2].x * ot, t_x[2].y * ot));
						
						add_ind();
					}
				}
				
				if(BlockConverter.int_to_block(chunk.controller.world.get_block(x + 1, y, z)).block_type == BlockType.air || (BlockConverter.int_to_block(chunk.controller.world.get_block(x + 1, y, z)).render_block_type != RenderBlockType.render_when_air && BlockConverter.int_to_block(chunk.controller.world.get_block(x + 1, y, z)).block_type != BlockType.liquid))
				{
					vert_add(new Vector3f(0.5f + x, -0.5f + y, -0.5f + z));
					vert_add(new Vector3f(0.5f + x, 0.5f + y, -0.5f + z));
					vert_add(new Vector3f(0.5f + x, 0.5f + y, 0.5f + z));
					vert_add(new Vector3f(0.5f + x, -0.5f + y, 0.5f + z));
					
					block_check(0.55f, chunk.controller.world.get_light(x + 1, y, z));
					
					t_coord_add(new Vector2f(t_x[4].x * ot + ot, t_x[4].y * ot + ot));
					t_coord_add(new Vector2f(t_x[4].x * ot + ot, t_x[4].y * ot));
					t_coord_add(new Vector2f(t_x[4].x * ot, t_x[4].y * ot));
					t_coord_add(new Vector2f(t_x[4].x * ot, t_x[4].y * ot + ot));
					
					add_ind();
					
					if(x < chunk.controller.world.world_size_x * World.chunk_size - 1)
					{
						vert_add(new Vector3f(0.5f + x, -0.5f + y, -0.5f + z));
						vert_add(new Vector3f(0.5f + x, -0.5f + y, 0.5f + z));
						vert_add(new Vector3f(0.5f + x, 0.5f + y, 0.5f + z));
						vert_add(new Vector3f(0.5f + x, 0.5f + y, -0.5f + z));
						
						block_check(0.5f, chunk.controller.world.get_light(x, y, z));
						
						t_coord_add(new Vector2f(t_x[5].x * ot, t_x[5].y * ot + ot));
						t_coord_add(new Vector2f(t_x[5].x * ot + ot, t_x[5].y * ot + ot));
						t_coord_add(new Vector2f(t_x[5].x * ot + ot, t_x[5].y * ot));
						t_coord_add(new Vector2f(t_x[5].x * ot, t_x[5].y * ot));
						
						add_ind();
					}
				}
				
				if(BlockConverter.int_to_block(chunk.controller.world.get_block(x - 1, y, z)).block_type == BlockType.air || (BlockConverter.int_to_block(chunk.controller.world.get_block(x - 1, y, z)).render_block_type != RenderBlockType.render_when_air && BlockConverter.int_to_block(chunk.controller.world.get_block(x - 1, y, z)).block_type != BlockType.liquid))
				{
					vert_add(new Vector3f(-0.5f + x, -0.5f + y, -0.5f + z));
					vert_add(new Vector3f(-0.5f + x, -0.5f + y, 0.5f + z));
					vert_add(new Vector3f(-0.5f + x, 0.5f + y, 0.5f + z));
					vert_add(new Vector3f(-0.5f + x, 0.5f + y, -0.5f + z));
					
					block_check(0.5f, chunk.controller.world.get_light(x - 1, y, z));
					
					t_coord_add(new Vector2f(t_x[5].x * ot, t_x[5].y * ot + ot));
					t_coord_add(new Vector2f(t_x[5].x * ot + ot, t_x[5].y * ot + ot));
					t_coord_add(new Vector2f(t_x[5].x * ot + ot, t_x[5].y * ot));
					t_coord_add(new Vector2f(t_x[5].x * ot, t_x[5].y * ot));
					
					add_ind();
					
					if(x > 0)
					{
						vert_add(new Vector3f(-0.5f + x, -0.5f + y, -0.5f + z));
						vert_add(new Vector3f(-0.5f + x, 0.5f + y, -0.5f + z));
						vert_add(new Vector3f(-0.5f + x, 0.5f + y, 0.5f + z));
						vert_add(new Vector3f(-0.5f + x, -0.5f + y, 0.5f + z));
						
						block_check(0.55f, chunk.controller.world.get_light(x, y, z));
						
						t_coord_add(new Vector2f(t_x[4].x * ot + ot, t_x[4].y * ot + ot));
						t_coord_add(new Vector2f(t_x[4].x * ot + ot, t_x[4].y * ot));
						t_coord_add(new Vector2f(t_x[4].x * ot, t_x[4].y * ot));
						t_coord_add(new Vector2f(t_x[4].x * ot, t_x[4].y * ot + ot));
						
						add_ind();
					}
				}
			}
		}
		
		else
		{
			if(BlockConverter.int_to_block(chunk.controller.world.get_block(x, y, z)).block_type == BlockType.solid)
			{
				if(BlockConverter.int_to_block(chunk.controller.world.get_block(x, y, z)).render_block_type == RenderBlockType.render_when_same_type)
				{
					Block b = BlockConverter.int_to_block(chunk.controller.world.get_block(x, y, z));
					
					if(BlockConverter.int_to_block(chunk.controller.world.get_block(x, y + 1, z)) != b && (BlockConverter.int_to_block(chunk.controller.world.get_block(x, y + 1, z)).block_type == BlockType.air || BlockConverter.int_to_block(chunk.controller.world.get_block(x, y + 1, z)).render_block_type == RenderBlockType.render_always || BlockConverter.int_to_block(chunk.controller.world.get_block(x, y + 1, z)).render_block_type == RenderBlockType.render_when_same_type))
					{
						vert_add(new Vector3f(-0.5f + x, 0.5f + y, -0.5f + z));
						vert_add(new Vector3f(-0.5f + x, 0.5f + y, 0.5f + z));
						vert_add(new Vector3f(0.5f + x, 0.5f + y, 0.5f + z));
						vert_add(new Vector3f(0.5f + x, 0.5f + y, -0.5f + z));
						
						block_check(1.0f, chunk.controller.world.get_light(x, y + 1, z));
						
						t_coord_add(new Vector2f(t_x[0].x * ot + ot, t_x[0].y * ot + ot));
						t_coord_add(new Vector2f(t_x[0].x * ot + ot, t_x[0].y * ot));
						t_coord_add(new Vector2f(t_x[0].x * ot, t_x[0].y * ot));
						t_coord_add(new Vector2f(t_x[0].x * ot, t_x[0].y * ot + ot));
						
						add_ind();
					}
					
					if(BlockConverter.int_to_block(chunk.controller.world.get_block(x, y - 1, z)) != b && (BlockConverter.int_to_block(chunk.controller.world.get_block(x, y - 1, z)).block_type == BlockType.air || BlockConverter.int_to_block(chunk.controller.world.get_block(x, y - 1, z)).render_block_type == RenderBlockType.render_always || BlockConverter.int_to_block(chunk.controller.world.get_block(x, y - 1, z)).render_block_type == RenderBlockType.render_when_same_type))
					{
						vert_add(new Vector3f(-0.5f + x, -0.5f + y, -0.5f + z));
						vert_add(new Vector3f(0.5f + x, -0.5f + y, -0.5f + z));
						vert_add(new Vector3f(0.5f + x, -0.5f + y, 0.5f + z));
						vert_add(new Vector3f(-0.5f + x, -0.5f + y, 0.5f + z));
						
						block_check(0.35f, chunk.controller.world.get_light(x, y - 1, z));
						
						t_coord_add(new Vector2f(t_x[1].x * ot + ot, t_x[1].y * ot));
						t_coord_add(new Vector2f(t_x[1].x * ot, t_x[1].y * ot));
						t_coord_add(new Vector2f(t_x[1].x * ot, t_x[1].y * ot + ot));
						t_coord_add(new Vector2f(t_x[1].x * ot + ot, t_x[1].y * ot + ot));
						
						add_ind();
					}
					
					if(BlockConverter.int_to_block(chunk.controller.world.get_block(x, y, z + 1)) != b && (BlockConverter.int_to_block(chunk.controller.world.get_block(x, y, z + 1)).block_type == BlockType.air || BlockConverter.int_to_block(chunk.controller.world.get_block(x, y, z + 1)).render_block_type == RenderBlockType.render_always || BlockConverter.int_to_block(chunk.controller.world.get_block(x, y, z + 1)).render_block_type == RenderBlockType.render_when_same_type))
					{
						vert_add(new Vector3f(-0.5f + x, -0.5f + y, 0.5f + z));
						vert_add(new Vector3f(0.5f + x, -0.5f + y, 0.5f + z));
						vert_add(new Vector3f(0.5f + x, 0.5f + y, 0.5f + z));
						vert_add(new Vector3f(-0.5f + x, 0.5f + y, 0.5f + z));
						
						block_check(0.75f, chunk.controller.world.get_light(x, y, z + 1));
						
						t_coord_add(new Vector2f(t_x[2].x * ot, t_x[2].y * ot + ot));
						t_coord_add(new Vector2f(t_x[2].x * ot + ot, t_x[2].y * ot + ot));
						t_coord_add(new Vector2f(t_x[2].x * ot + ot, t_x[2].y * ot));
						t_coord_add(new Vector2f(t_x[2].x * ot, t_x[2].y * ot));
						
						add_ind();
					}
					
					if(BlockConverter.int_to_block(chunk.controller.world.get_block(x, y, z - 1)) != b && (BlockConverter.int_to_block(chunk.controller.world.get_block(x, y, z - 1)).block_type == BlockType.air || BlockConverter.int_to_block(chunk.controller.world.get_block(x, y, z - 1)).render_block_type == RenderBlockType.render_always || BlockConverter.int_to_block(chunk.controller.world.get_block(x, y, z - 1)).render_block_type == RenderBlockType.render_when_same_type))
					{
						vert_add(new Vector3f(-0.5f + x, -0.5f + y, -0.5f + z));
						vert_add(new Vector3f(-0.5f + x, 0.5f + y, -0.5f + z));
						vert_add(new Vector3f(0.5f + x, 0.5f + y, -0.5f + z));
						vert_add(new Vector3f(0.5f + x, -0.5f + y, -0.5f + z));
						
						block_check(0.7f, chunk.controller.world.get_light(x, y, z - 1));
						
						t_coord_add(new Vector2f(t_x[3].x * ot + ot, t_x[3].y * ot + ot));
						t_coord_add(new Vector2f(t_x[3].x * ot + ot, t_x[3].y * ot));
						t_coord_add(new Vector2f(t_x[3].x * ot, t_x[3].y * ot));
						t_coord_add(new Vector2f(t_x[3].x * ot, t_x[3].y * ot + ot));
						
						add_ind();
					}
					
					if(BlockConverter.int_to_block(chunk.controller.world.get_block(x + 1, y, z)) != b && (BlockConverter.int_to_block(chunk.controller.world.get_block(x + 1, y, z)).block_type == BlockType.air || BlockConverter.int_to_block(chunk.controller.world.get_block(x + 1, y, z)).render_block_type == RenderBlockType.render_always || BlockConverter.int_to_block(chunk.controller.world.get_block(x + 1, y, z)).render_block_type == RenderBlockType.render_when_same_type))
					{
						vert_add(new Vector3f(0.5f + x, -0.5f + y, -0.5f + z));
						vert_add(new Vector3f(0.5f + x, 0.5f + y, -0.5f + z));
						vert_add(new Vector3f(0.5f + x, 0.5f + y, 0.5f + z));
						vert_add(new Vector3f(0.5f + x, -0.5f + y, 0.5f + z));
						
						block_check(0.55f, chunk.controller.world.get_light(x + 1, y, z));
						
						t_coord_add(new Vector2f(t_x[4].x * ot + ot, t_x[4].y * ot + ot));
						t_coord_add(new Vector2f(t_x[4].x * ot + ot, t_x[4].y * ot));
						t_coord_add(new Vector2f(t_x[4].x * ot, t_x[4].y * ot));
						t_coord_add(new Vector2f(t_x[4].x * ot, t_x[4].y * ot + ot));
						
						add_ind();
					}
					
					if(BlockConverter.int_to_block(chunk.controller.world.get_block(x - 1, y, z)) != b && (BlockConverter.int_to_block(chunk.controller.world.get_block(x - 1, y, z)).block_type == BlockType.air || BlockConverter.int_to_block(chunk.controller.world.get_block(x - 1, y, z)).render_block_type == RenderBlockType.render_always || BlockConverter.int_to_block(chunk.controller.world.get_block(x - 1, y, z)).render_block_type == RenderBlockType.render_when_same_type))
					{
						vert_add(new Vector3f(-0.5f + x, -0.5f + y, -0.5f + z));
						vert_add(new Vector3f(-0.5f + x, -0.5f + y, 0.5f + z));
						vert_add(new Vector3f(-0.5f + x, 0.5f + y, 0.5f + z));
						vert_add(new Vector3f(-0.5f + x, 0.5f + y, -0.5f + z));
						
						block_check(0.5f, chunk.controller.world.get_light(x - 1, y, z));
						
						t_coord_add(new Vector2f(t_x[5].x * ot, t_x[5].y * ot + ot));
						t_coord_add(new Vector2f(t_x[5].x * ot + ot, t_x[5].y * ot + ot));
						t_coord_add(new Vector2f(t_x[5].x * ot + ot, t_x[5].y * ot));
						t_coord_add(new Vector2f(t_x[5].x * ot, t_x[5].y * ot));
						
						add_ind();
					}
				}
				
				else
				{
					if(BlockConverter.int_to_block(chunk.controller.world.get_block(x, y + 1, z)).block_type == BlockType.air || BlockConverter.int_to_block(chunk.controller.world.get_block(x, y + 1, z)).render_block_type == RenderBlockType.render_always || BlockConverter.int_to_block(chunk.controller.world.get_block(x, y + 1, z)).render_block_type == RenderBlockType.render_when_same_type)
					{
						vert_add(new Vector3f(-0.5f + x, 0.5f + y, -0.5f + z));
						vert_add(new Vector3f(-0.5f + x, 0.5f + y, 0.5f + z));
						vert_add(new Vector3f(0.5f + x, 0.5f + y, 0.5f + z));
						vert_add(new Vector3f(0.5f + x, 0.5f + y, -0.5f + z));
						
						block_check(1.0f, chunk.controller.world.get_light(x, y + 1, z));
						
						t_coord_add(new Vector2f(t_x[0].x * ot + ot, t_x[0].y * ot + ot));
						t_coord_add(new Vector2f(t_x[0].x * ot + ot, t_x[0].y * ot));
						t_coord_add(new Vector2f(t_x[0].x * ot, t_x[0].y * ot));
						t_coord_add(new Vector2f(t_x[0].x * ot, t_x[0].y * ot + ot));
						
						add_ind();
					}
					
					if(BlockConverter.int_to_block(chunk.controller.world.get_block(x, y - 1, z)).block_type == BlockType.air || BlockConverter.int_to_block(chunk.controller.world.get_block(x, y - 1, z)).render_block_type == RenderBlockType.render_always || BlockConverter.int_to_block(chunk.controller.world.get_block(x, y - 1, z)).render_block_type == RenderBlockType.render_when_same_type)
					{
						vert_add(new Vector3f(-0.5f + x, -0.5f + y, -0.5f + z));
						vert_add(new Vector3f(0.5f + x, -0.5f + y, -0.5f + z));
						vert_add(new Vector3f(0.5f + x, -0.5f + y, 0.5f + z));
						vert_add(new Vector3f(-0.5f + x, -0.5f + y, 0.5f + z));
						
						block_check(0.35f, chunk.controller.world.get_light(x, y - 1, z));
						
						t_coord_add(new Vector2f(t_x[1].x * ot + ot, t_x[1].y * ot));
						t_coord_add(new Vector2f(t_x[1].x * ot, t_x[1].y * ot));
						t_coord_add(new Vector2f(t_x[1].x * ot, t_x[1].y * ot + ot));
						t_coord_add(new Vector2f(t_x[1].x * ot + ot, t_x[1].y * ot + ot));
						
						add_ind();
					}
					
					if(BlockConverter.int_to_block(chunk.controller.world.get_block(x, y, z + 1)).block_type == BlockType.air || BlockConverter.int_to_block(chunk.controller.world.get_block(x, y, z + 1)).render_block_type == RenderBlockType.render_always || BlockConverter.int_to_block(chunk.controller.world.get_block(x, y, z + 1)).render_block_type == RenderBlockType.render_when_same_type)
					{
						vert_add(new Vector3f(-0.5f + x, -0.5f + y, 0.5f + z));
						vert_add(new Vector3f(0.5f + x, -0.5f + y, 0.5f + z));
						vert_add(new Vector3f(0.5f + x, 0.5f + y, 0.5f + z));
						vert_add(new Vector3f(-0.5f + x, 0.5f + y, 0.5f + z));
						
						block_check(0.75f, chunk.controller.world.get_light(x, y, z + 1));
						
						t_coord_add(new Vector2f(t_x[2].x * ot, t_x[2].y * ot + ot));
						t_coord_add(new Vector2f(t_x[2].x * ot + ot, t_x[2].y * ot + ot));
						t_coord_add(new Vector2f(t_x[2].x * ot + ot, t_x[2].y * ot));
						t_coord_add(new Vector2f(t_x[2].x * ot, t_x[2].y * ot));
						
						add_ind();
					}
					
					if(BlockConverter.int_to_block(chunk.controller.world.get_block(x, y, z - 1)).block_type == BlockType.air || BlockConverter.int_to_block(chunk.controller.world.get_block(x, y, z - 1)).render_block_type == RenderBlockType.render_always || BlockConverter.int_to_block(chunk.controller.world.get_block(x, y, z - 1)).render_block_type == RenderBlockType.render_when_same_type)
					{
						vert_add(new Vector3f(-0.5f + x, -0.5f + y, -0.5f + z));
						vert_add(new Vector3f(-0.5f + x, 0.5f + y, -0.5f + z));
						vert_add(new Vector3f(0.5f + x, 0.5f + y, -0.5f + z));
						vert_add(new Vector3f(0.5f + x, -0.5f + y, -0.5f + z));
						
						block_check(0.7f, chunk.controller.world.get_light(x, y, z - 1));
						
						t_coord_add(new Vector2f(t_x[3].x * ot + ot, t_x[3].y * ot + ot));
						t_coord_add(new Vector2f(t_x[3].x * ot + ot, t_x[3].y * ot));
						t_coord_add(new Vector2f(t_x[3].x * ot, t_x[3].y * ot));
						t_coord_add(new Vector2f(t_x[3].x * ot, t_x[3].y * ot + ot));
						
						add_ind();
					}
					
					if(BlockConverter.int_to_block(chunk.controller.world.get_block(x + 1, y, z)).block_type == BlockType.air || BlockConverter.int_to_block(chunk.controller.world.get_block(x + 1, y, z)).render_block_type == RenderBlockType.render_always || BlockConverter.int_to_block(chunk.controller.world.get_block(x + 1, y, z)).render_block_type == RenderBlockType.render_when_same_type)
					{
						vert_add(new Vector3f(0.5f + x, -0.5f + y, -0.5f + z));
						vert_add(new Vector3f(0.5f + x, 0.5f + y, -0.5f + z));
						vert_add(new Vector3f(0.5f + x, 0.5f + y, 0.5f + z));
						vert_add(new Vector3f(0.5f + x, -0.5f + y, 0.5f + z));
						
						block_check(0.55f, chunk.controller.world.get_light(x + 1, y, z));
						
						t_coord_add(new Vector2f(t_x[4].x * ot + ot, t_x[4].y * ot + ot));
						t_coord_add(new Vector2f(t_x[4].x * ot + ot, t_x[4].y * ot));
						t_coord_add(new Vector2f(t_x[4].x * ot, t_x[4].y * ot));
						t_coord_add(new Vector2f(t_x[4].x * ot, t_x[4].y * ot + ot));
						
						add_ind();
					}
					
					if(BlockConverter.int_to_block(chunk.controller.world.get_block(x - 1, y, z)).block_type == BlockType.air || BlockConverter.int_to_block(chunk.controller.world.get_block(x - 1, y, z)).render_block_type == RenderBlockType.render_always || BlockConverter.int_to_block(chunk.controller.world.get_block(x - 1, y, z)).render_block_type == RenderBlockType.render_when_same_type)
					{
						vert_add(new Vector3f(-0.5f + x, -0.5f + y, -0.5f + z));
						vert_add(new Vector3f(-0.5f + x, -0.5f + y, 0.5f + z));
						vert_add(new Vector3f(-0.5f + x, 0.5f + y, 0.5f + z));
						vert_add(new Vector3f(-0.5f + x, 0.5f + y, -0.5f + z));
						
						block_check(0.5f, chunk.controller.world.get_light(x - 1, y, z));
						
						t_coord_add(new Vector2f(t_x[5].x * ot, t_x[5].y * ot + ot));
						t_coord_add(new Vector2f(t_x[5].x * ot + ot, t_x[5].y * ot + ot));
						t_coord_add(new Vector2f(t_x[5].x * ot + ot, t_x[5].y * ot));
						t_coord_add(new Vector2f(t_x[5].x * ot, t_x[5].y * ot));
						
						add_ind();
					}
				}
			}
			
			if(BlockConverter.int_to_block(chunk.controller.world.get_block(x, y, z)).block_type == BlockType.plant)
			{	
				vert_add(new Vector3f(-0.5f + x, -0.5f + y, z));
				vert_add(new Vector3f(0.5f + x, -0.5f + y, z));
				vert_add(new Vector3f(0.5f + x, 0.5f + y, z));
				vert_add(new Vector3f(-0.5f + x, 0.5f + y, z));
				
				block_check(0.75f, chunk.controller.world.get_light(x, y, z));
				
				t_coord_add(new Vector2f(t_x[2].x * ot, t_x[2].y * ot + ot));
				t_coord_add(new Vector2f(t_x[2].x * ot + ot, t_x[2].y * ot + ot));
				t_coord_add(new Vector2f(t_x[2].x * ot + ot, t_x[2].y * ot));
				t_coord_add(new Vector2f(t_x[2].x * ot, t_x[2].y * ot));
				
				add_ind();
				
				vert_add(new Vector3f(-0.5f + x, -0.5f + y, z));
				vert_add(new Vector3f(-0.5f + x, 0.5f + y, z));
				vert_add(new Vector3f(0.5f + x, 0.5f + y, z));
				vert_add(new Vector3f(0.5f + x, -0.5f + y, z));
				
				block_check(0.7f, chunk.controller.world.get_light(x, y, z));
				
				t_coord_add(new Vector2f(t_x[3].x * ot + ot, t_x[3].y * ot + ot));
				t_coord_add(new Vector2f(t_x[3].x * ot + ot, t_x[3].y * ot));
				t_coord_add(new Vector2f(t_x[3].x * ot, t_x[3].y * ot));
				t_coord_add(new Vector2f(t_x[3].x * ot, t_x[3].y * ot + ot));
				
				add_ind();
				
				vert_add(new Vector3f(x, -0.5f + y, -0.5f + z));
				vert_add(new Vector3f(x, 0.5f + y, -0.5f + z));
				vert_add(new Vector3f(x, 0.5f + y, 0.5f + z));
				vert_add(new Vector3f(x, -0.5f + y, 0.5f + z));
				
				block_check(0.75f, chunk.controller.world.get_light(x, y, z));
				
				t_coord_add(new Vector2f(t_x[4].x * ot + ot, t_x[4].y * ot + ot));
				t_coord_add(new Vector2f(t_x[4].x * ot + ot, t_x[4].y * ot));
				t_coord_add(new Vector2f(t_x[4].x * ot, t_x[4].y * ot));
				t_coord_add(new Vector2f(t_x[4].x * ot, t_x[4].y * ot + ot));
				
				add_ind();
				
				vert_add(new Vector3f(x, -0.5f + y, -0.5f + z));
				vert_add(new Vector3f(x, -0.5f + y, 0.5f + z));
				vert_add(new Vector3f(x, 0.5f + y, 0.5f + z));
				vert_add(new Vector3f(x, 0.5f + y, -0.5f + z));
				
				block_check(0.7f, chunk.controller.world.get_light(x, y, z));
				
				t_coord_add(new Vector2f(t_x[5].x * ot, t_x[5].y * ot + ot));
				t_coord_add(new Vector2f(t_x[5].x * ot + ot, t_x[5].y * ot + ot));
				t_coord_add(new Vector2f(t_x[5].x * ot + ot, t_x[5].y * ot));
				t_coord_add(new Vector2f(t_x[5].x * ot, t_x[5].y * ot));
				
				add_ind();
			}
		}
	}
	
	private void check_gravity_block(final int x, final int y, final int z)
	{
		if(!BlockConverter.int_to_block(chunk.controller.world.get_block(x, y - 1, z)).is_collision[0])
		{
			Block gb = BlockConverter.int_to_block(chunk.controller.world.get_block(x, y, z));
			
			chunk.controller.world.set_block(x, y, z, BlockConverter.block_to_int(BlockData.air));
			
			for(int Y = y; Y >= 0; Y--)
			{
				if(BlockConverter.int_to_block(chunk.controller.world.get_block(x, Y, z)).is_collision[0])
				{
					chunk.controller.world.set_block(x, Y + 1, z, BlockConverter.block_to_int(gb));
					break;
				}
			}
			
			if(BlockConverter.int_to_block(chunk.controller.world.get_block(x, y + 1, z)).is_gravity)
			{
				check_gravity_block(x, y + 1, z);
			}
		}
	}
	
	private void convert()
	{
		vert = new float[vert_data.size()];
		for(int i = 0; i < vert_data.size(); i++)
		{
			vert[i] = vert_data.get(i);
		}
		
		col = new float[col_data.size()];
		for(int i = 0; i < col_data.size(); i++)
		{
			col[i] = col_data.get(i);
		}
		
		tex_c = new float[t_coord_data.size()];
		for(int i = 0; i < t_coord_data.size(); i++)
		{
			tex_c[i] = t_coord_data.get(i);
		}
		
		ind = new int[ind_data.size()];
		for(int i = 0; i < ind_data.size(); i++)
		{
			ind[i] = ind_data.get(i);
		}
	}
	
	private void block_check(final float light, final boolean is_light)
	{
		if(is_light)
		{
			add_col(new Vector3f(light, light, light));
		}
		
		else
		{
			add_col(new Vector3f(light / shadow, light / shadow, light / shadow));
		}
	}
	
	private void add_col(final Vector3f vec3)
	{
		col_add(vec3);
		col_add(vec3);
		col_add(vec3);
		col_add(vec3);
	}
	
	private void add_ind()
	{
		ind_add(0 + 4 * count, 1 + 4 * count, 2 + 4 * count);
		ind_add(0 + 4 * count, 2 + 4 * count, 3 + 4 * count);
		count++;
	}
	
	private void vert_add(final Vector3f vec3)
	{
		vert_data.add(vec3.x - chunk.pos.x);
		vert_data.add(vec3.y - chunk.pos.y);
		vert_data.add(vec3.z - chunk.pos.z);
	}
	
	private void col_add(final Vector3f vec3)
	{
		col_data.add(vec3.x);
		col_data.add(vec3.y);
		col_data.add(vec3.z);
	}
	
	private void t_coord_add(final Vector2f vec2)
	{
		t_coord_data.add(vec2.x);
		t_coord_data.add(vec2.y);
	}
	
	private void ind_add(final int i0, final int i1, final int i2)
	{
		ind_data.add((i0));
		ind_data.add((i1));
		ind_data.add((i2));
	}
}