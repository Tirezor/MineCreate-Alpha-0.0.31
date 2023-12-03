package com.gamedev.minecreate.world;

import com.gamedev.minecreate.gameengine.time.Timer;
import com.gamedev.minecreate.gameengine.window.Window;
import com.gamedev.minecreate.external.FastNoiseLite;
import com.gamedev.minecreate.block.*;
import com.gamedev.minecreate.chunk.*;
import com.gamedev.minecreate.console.*;

import java.util.ArrayList;
import java.util.Random;
import java.lang.Math;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.*;
import javax.swing.*;

import org.lwjgl.input.*;

import org.joml.*;

public class World
{
	public Window window;
	
	public static int chunk_size = 16;
	
	public int world_size_x;
	public int world_size_y;
	public int world_size_z;
	
	public int[] data;
	public boolean[] light;
	
	public int radius;
	
	public int water_height;
	
	public String world_size;
	public String world_type;
	
	public ChunksController chunks;
	
	public ArrayList<Thread> threads;
	public ArrayList<ChunkUpdater> updater;
	public ArrayList<BlockUpdater> update_blocks;
	
	public FastNoiseLite noise;
	
	public Ocean ocean;
	public Sky sky;
	
	public File file;
	
	public boolean is_loading;
	
	public boolean is_load;
	public boolean is_create;
	
	private float timer;
	
	public World(final Window window)
	{
		this.window = window;
		
		world_size = "MEDIUM";
		world_type = "DEFAULT";
		
		if(world_size == "TINY")
		{
			world_size_x = 2;
			world_size_y = 4;
			world_size_z = 2;
			
			radius = 4;
		}
		
		else if(world_size == "SMALL")
		{
			world_size_x = 4;
			world_size_y = 4;
			world_size_z = 4;
			
			radius = 18;
		}
		
		else if(world_size == "MEDIUM")
		{
			world_size_x = 8;
			world_size_y = 4;
			world_size_z = 8;
			
			radius = 48;
		}
		
		else if(world_size == "HUGE")
		{
			world_size_x = 16;
			world_size_y = 4;
			world_size_z = 16;
			
			radius = 120;
		}
		
		else if(world_size == "GIANT")
		{
			world_size_x = 32;
			world_size_y = 4;
			world_size_z = 32;
			
			radius = 300;
		}
		
		if(world_type == "DEFAULT")
		{
			water_height = 23;
		}
		
		else if(world_type == "SUPERFLAT")
		{
			water_height = 23;
		}
		
		else if(world_type == "ISLANDS")
		{
			water_height = 29;
		}
		
		else if(world_type == "OCEAN")
		{
			water_height = 32;
		}
		
		else if(world_type == "CAVES")
		{
			water_height = 4;
		}
		
		light = new boolean[(world_size_x * chunk_size) * (world_size_y * chunk_size) * (world_size_z * chunk_size)];
		data = new int[(world_size_x * chunk_size) * (world_size_y * chunk_size) * (world_size_z * chunk_size)];
		
		chunks = new ChunksController(this);
		
		threads = new ArrayList<Thread>();
		update_blocks = new ArrayList<BlockUpdater>();
		updater = new ArrayList<ChunkUpdater>();
		
		ocean = new Ocean(this);
		sky = new Sky(this);
		
		noise = new FastNoiseLite();
		
		is_load = false;
		
		timer = 0.0f;
	}
	
	public void generate()
	{
		create_world();
	}
	
	public void create()
	{
		if(world_type == "DEFAULT")
		{
			Random random = new Random();
			int seed = random.nextInt();
			noise.SetSeed(seed);
			
			for(int y = 0; y < world_size_y * chunk_size; y++)
			{
				for(int x = 0; x < world_size_x * chunk_size; x++)
				{
					for(int z = 0; z < world_size_z * chunk_size; z++)
					{
						int height = get_island_height(x, z, 0.875f, 4, radius) + (int)(noise.GetNoise(x * 1.45f, z * 1.45f) * 5.0f) + (int)(noise.GetNoise(x * 2.2f, z * 2.2f) * 5.0f * (int)(noise.GetNoise(x * 1.5f, z * 1.5f) + 0.75f));
						
						if(y == height + 22)
						{
							set_block(x, y, z, BlockConverter.block_to_int(BlockData.grass));
						}
						
						else if(y == height + 21 || y == height + 20)
						{
							set_block(x, y, z, BlockConverter.block_to_int(BlockData.dirt));
						}
						
						else if(y < height + 20 && y >= 1)
						{
							set_block(x, y, z, BlockConverter.block_to_int(BlockData.stone));
						}
						
						else
						{
							set_block(x, y, z, BlockConverter.block_to_int(BlockData.air));
						}
						
						if(y <= water_height && get_block(x, y, z) == BlockConverter.block_to_int(BlockData.air))
						{
							set_block(x, y, z, BlockConverter.block_to_int(BlockData.water));
						}
					}
				}
			}
			
			for(int y = 0; y < world_size_y * chunk_size; y++)
			{
				for(int x = 0; x < world_size_x * chunk_size; x++)
				{
					for(int z = 0; z < world_size_z * chunk_size; z++)
					{
						int height = get_island_height(x, z, 0.875f, 4, radius) + (int)(noise.GetNoise(x * 1.45f, z * 1.45f) * 5.0f) + (int)(noise.GetNoise(x * 2.2f, z * 2.2f) * 5.0f * (int)(noise.GetNoise(x * 1.5f, z * 1.5f) + 0.75f));
						
						int caves = (int)(noise.GetNoise(x * 5.0f, y * 5.0f, z * 5.0f) * 10.0f * (int)(noise.GetNoise(x * 0.5f, y * 0.5f, z * 0.5f) + 0.5f));
						int beach = (int)(noise.GetNoise(x * 2.5f, y * 2.5f, z * 2.5f) * 10.0f);
						
						if(caves >= 5.0f && x != 0 && z != 0 && x != world_size_x * chunk_size - 1 && z != world_size_z * chunk_size - 1 && get_block(x, y, z) != BlockConverter.block_to_int(BlockData.bedrock) && get_block(x, y, z) != BlockConverter.block_to_int(BlockData.water) && get_block(x, y + 1, z) != BlockConverter.block_to_int(BlockData.water) && get_block(x, y, z + 1) != BlockConverter.block_to_int(BlockData.water) && get_block(x, y, z - 1) != BlockConverter.block_to_int(BlockData.water) && get_block(x + 1, y, z) != BlockConverter.block_to_int(BlockData.water) && get_block(x - 1, y, z) != BlockConverter.block_to_int(BlockData.water))
						{
							set_block(x, y, z, BlockConverter.block_to_int(BlockData.air));
						}
						
						if(y == height + 22 && y <= water_height + 2 && (get_block(x, y, z) == BlockConverter.block_to_int(BlockData.grass) || get_block(x, y, z) == BlockConverter.block_to_int(BlockData.dirt)))
						{
							if(BlockConverter.int_to_block(get_block(x, y - 1, z)).is_collision[0])
							{
								if(y >= water_height)
								{
									if(beach >= 1 && beach < 5)
									{
										set_block(x, y, z, BlockConverter.block_to_int(BlockData.sand));
									}
									
									else if(beach >= 5)
									{
										set_block(x, y, z, BlockConverter.block_to_int(BlockData.gravel));
									}
								}
								
								else
								{
									if(beach >= 1 && beach < 5)
									{
										set_block(x, y, z, BlockConverter.block_to_int(BlockData.sand));
									}
									
									else if(beach >= 5 && beach < 9)
									{
										set_block(x, y, z, BlockConverter.block_to_int(BlockData.gravel));
									}
									
									else if(beach >= 9)
									{
										set_block(x, y, z, BlockConverter.block_to_int(BlockData.clay));
									}
								}
							}
						}
					}
				}
			}
			
			Random spawn_random = new Random(seed);
			
			for(int y = 0; y < world_size_y * chunk_size; y++)
			{
				for(int x = 0; x < world_size_x * chunk_size; x++)
				{
					for(int z = 0; z < world_size_z * chunk_size; z++)
					{
						int height = get_island_height(x, z, 0.875f, 4, radius) + (int)(noise.GetNoise(x * 1.45f, z * 1.45f) * 5.0f) + (int)(noise.GetNoise(x * 2.2f, z * 2.2f) * 5.0f * (int)(noise.GetNoise(x * 1.5f, z * 1.5f) + 0.75f));
						
						int spawn = spawn_random.nextInt(1, 111);
						
						if(spawn == 1)
						{
							if(get_block(x, y, z) == BlockConverter.block_to_int(BlockData.grass) && x >= 2 && x < world_size_x * chunk_size - 2 && z >= 2 && z < world_size_z * chunk_size - 2 && y > water_height)
							{
								if(y == height + 22)
								{
									set_block(x, y, z, BlockConverter.block_to_int(BlockData.dirt));
									
									int tree_height = spawn_random.nextInt(1, 4);
									
									for(int ly = tree_height + 1; ly <= tree_height + 5; ly++)
									{
										for(int lx = -1; lx <= 1; lx++)
										{
											for(int lz = -1; lz <= 1; lz++)
											{
												if(get_block(x + lx, y + ly, z + lz) != BlockConverter.block_to_int(BlockData.wood))
												{
													set_block(x + lx, y + ly, z + lz, BlockConverter.block_to_int(BlockData.leaves));
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
												if(get_block(x + lx, y + ly, z + lz) != BlockConverter.block_to_int(BlockData.wood))
												{
													set_block(x + lx, y + ly, z + lz, BlockConverter.block_to_int(BlockData.leaves));
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
												if(get_block(x + lx, y + ly, z + lz) != BlockConverter.block_to_int(BlockData.wood))
												{
													set_block(x + lx, y + ly, z + lz, BlockConverter.block_to_int(BlockData.leaves));
												}
											}
										}
									}
									
									for(int th = 1; th <= tree_height + 4; th++)
									{
										set_block(x, y + th, z, BlockConverter.block_to_int(BlockData.wood));
									}
								}
							}
						}
						
						else if(spawn == 2)
						{
							if(get_block(x, y, z) == BlockConverter.block_to_int(BlockData.grass) && y > water_height)
							{
								if(y == height + 22)
								{
									set_block(x, y + 1, z, BlockConverter.block_to_int(BlockData.chamomile));
								}
							}
						}
						
						else if(spawn == 3)
						{
							if(get_block(x, y, z) == BlockConverter.block_to_int(BlockData.grass) && y > water_height)
							{
								if(y == height + 22)
								{
									set_block(x, y + 1, z, BlockConverter.block_to_int(BlockData.rose));
								}
							}
						}
						
						else if(spawn == 4)
						{
							if(get_block(x, y, z) == BlockConverter.block_to_int(BlockData.grass) && y > water_height)
							{
								if(y == height + 22)
								{
									set_block(x, y + 1, z, BlockConverter.block_to_int(BlockData.dandelion));
								}
							}
						}
						
						int coal = (int)(noise.GetNoise(x * 10.0f, y * 10.0f, z * 10.0f) * 10.0f);
						
						if(get_block(x, y, z) == BlockConverter.block_to_int(BlockData.stone) && coal >= 7.0f && y <= height + 21)
						{
							set_block(x, y, z, BlockConverter.block_to_int(BlockData.coal_ore));
						}
						
						int iron = (int)(noise.GetNoise(x * 11.0f, y * 11.0f, z * 11.0f) * 10.0f);
						
						if(get_block(x, y, z) == BlockConverter.block_to_int(BlockData.stone) && iron >= 8.0f && y <= height + 17)
						{
							set_block(x, y, z, BlockConverter.block_to_int(BlockData.iron_ore));
						}
						
						int gold = (int)(noise.GetNoise(x * 12.0f, y * 12.0f, z * 12.0f) * 10.0f);
						
						if(get_block(x, y, z) == BlockConverter.block_to_int(BlockData.stone) && gold >= 9.0f && y <= height + 7)
						{
							set_block(x, y, z, BlockConverter.block_to_int(BlockData.gold_ore));
						}
						
						int diamond = (int)(noise.GetNoise(x * 13.0f, y * 13.0f, z * 13.0f) * 10.0f);
						
						if(get_block(x, y, z) == BlockConverter.block_to_int(BlockData.stone) && diamond >= 9.0f && y <= height + 1)
						{
							set_block(x, y, z, BlockConverter.block_to_int(BlockData.diamond_ore));
						}
					}
				}
			}
			
			for(int x = 0; x < world_size_x * chunk_size; x++)
			{
				for(int z = 0; z < world_size_z * chunk_size; z++)
				{
					set_block(x, 0, z, BlockConverter.block_to_int(BlockData.bedrock));
				}
			}
			
			for(int x = 0; x < world_size_x * chunk_size; x++)
			{
				for(int z = 0; z < world_size_z * chunk_size; z++)
				{
					boolean is_light = true;
					
					for(int Y = world_size_y * chunk_size - 1; Y >= 0; Y--)
					{
						if(!BlockConverter.int_to_block(get_block(x, Y, z)).is_transmits_light)
						{
							is_light = false;
						}
						
						set_light(x, Y, z, is_light);
					}
				}
			}
			
			for(int y = 0; y < world_size_y * chunk_size; y++)
			{
				for(int x = 0; x < world_size_x * chunk_size; x++)
				{
					for(int z = 0; z < world_size_z * chunk_size; z++)
					{
						int spawn = spawn_random.nextInt(1, 111);
						
						if(spawn == 3)
						{
							if(get_block(x, y, z) == BlockConverter.block_to_int(BlockData.stone) && get_block(x, y + 1, z) == BlockConverter.block_to_int(BlockData.air) && !get_light(x, y + 1, z))
							{
								set_block(x, y + 1, z, BlockConverter.block_to_int(BlockData.boletus));
							}
						}
						
						else if(spawn == 4)
						{
							if(get_block(x, y, z) == BlockConverter.block_to_int(BlockData.stone) && get_block(x, y + 1, z) == BlockConverter.block_to_int(BlockData.air) && !get_light(x, y + 1, z))
							{
								set_block(x, y + 1, z, BlockConverter.block_to_int(BlockData.amanita_muscaria));
							}
						}
					}
				}
			}
			
			for(int y = world_size_y * chunk_size - 1; y >= 0; y--)
			{
				for(int x = 0; x < world_size_x * chunk_size; x++)
				{
					for(int z = 0; z < world_size_z * chunk_size; z++)
					{
						if(get_block(x, y, z) == BlockConverter.block_to_int(BlockData.dirt))
						{
							if(get_light(x, y + 1, z))
							{
								set_block(x, y, z, BlockConverter.block_to_int(BlockData.grass));
							}
						}
						
						if(get_block(x, y, z) == BlockConverter.block_to_int(BlockData.grass))
						{
							if(!get_light(x, y + 1, z))
							{
								set_block(x, y, z, BlockConverter.block_to_int(BlockData.dirt));
							}
						}
					}
				}
			}
		}
		
		else if(world_type == "SUPERFLAT")
		{
			for(int y = 0; y < world_size_y * chunk_size; y++)
			{
				for(int x = 0; x < world_size_x * chunk_size; x++)
				{
					for(int z = 0; z < world_size_z * chunk_size; z++)
					{
						if(y == 23)
						{
							set_block(x, y, z, BlockConverter.block_to_int(BlockData.grass));
						}
						
						else if(y == 22 || y == 21)
						{
							set_block(x, y, z, BlockConverter.block_to_int(BlockData.dirt));
						}
						
						else if(y < 21)
						{
							set_block(x, y, z, BlockConverter.block_to_int(BlockData.stone));
						}
						
						else
						{
							set_block(x, y, z, BlockConverter.block_to_int(BlockData.air));
						}
						
						if(y <= water_height && get_block(x, y, z) == BlockConverter.block_to_int(BlockData.air))
						{
							set_block(x, y, z, BlockConverter.block_to_int(BlockData.water));
						}
					}
				}
			}
			
			for(int x = 0; x < world_size_x * chunk_size; x++)
			{
				for(int z = 0; z < world_size_z * chunk_size; z++)
				{
					set_block(x, 0, z, BlockConverter.block_to_int(BlockData.bedrock));
				}
			}
			
			for(int x = 0; x < world_size_x * chunk_size; x++)
			{
				for(int z = 0; z < world_size_z * chunk_size; z++)
				{
					boolean is_light = true;
					
					for(int Y = world_size_y * chunk_size - 1; Y >= 0; Y--)
					{
						if(!BlockConverter.int_to_block(get_block(x, Y, z)).is_transmits_light)
						{
							is_light = false;
						}
						
						set_light(x, Y, z, is_light);
					}
				}
			}
			
			for(int y = world_size_y * chunk_size - 1; y >= 0; y--)
			{
				for(int x = 0; x < world_size_x * chunk_size; x++)
				{
					for(int z = 0; z < world_size_z * chunk_size; z++)
					{
						if(get_block(x, y, z) == BlockConverter.block_to_int(BlockData.dirt))
						{
							if(get_light(x, y + 1, z))
							{
								set_block(x, y, z, BlockConverter.block_to_int(BlockData.grass));
							}
						}
						
						if(get_block(x, y, z) == BlockConverter.block_to_int(BlockData.grass))
						{
							if(!get_light(x, y + 1, z))
							{
								set_block(x, y, z, BlockConverter.block_to_int(BlockData.dirt));
							}
						}
					}
				}
			}
		}
		
		else if(world_type == "ISLANDS")
		{
			Random random = new Random();
			int seed = random.nextInt();
			noise.SetSeed(seed);
			
			for(int y = 0; y < world_size_y * chunk_size; y++)
			{
				for(int x = 0; x < world_size_x * chunk_size; x++)
				{
					for(int z = 0; z < world_size_z * chunk_size; z++)
					{
						int height = get_island_height(x, z, 0.875f, 4, radius) + (int)(noise.GetNoise(x * 1.45f, z * 1.45f) * 5.0f) + (int)(noise.GetNoise(x * 2.2f, z * 2.2f) * 5.0f * (int)(noise.GetNoise(x * 1.5f, z * 1.5f) + 0.75f));
						
						if(y == height + 22)
						{
							set_block(x, y, z, BlockConverter.block_to_int(BlockData.grass));
						}
						
						else if(y == height + 21 || y == height + 20)
						{
							set_block(x, y, z, BlockConverter.block_to_int(BlockData.dirt));
						}
						
						else if(y < height + 20 && y >= 1)
						{
							set_block(x, y, z, BlockConverter.block_to_int(BlockData.stone));
						}
						
						else
						{
							set_block(x, y, z, BlockConverter.block_to_int(BlockData.air));
						}
						
						if(y <= water_height && get_block(x, y, z) == BlockConverter.block_to_int(BlockData.air))
						{
							set_block(x, y, z, BlockConverter.block_to_int(BlockData.water));
						}
					}
				}
			}
			
			for(int y = 0; y < world_size_y * chunk_size; y++)
			{
				for(int x = 0; x < world_size_x * chunk_size; x++)
				{
					for(int z = 0; z < world_size_z * chunk_size; z++)
					{
						int height = get_island_height(x, z, 0.875f, 4, radius) + (int)(noise.GetNoise(x * 1.45f, z * 1.45f) * 5.0f) + (int)(noise.GetNoise(x * 2.2f, z * 2.2f) * 5.0f * (int)(noise.GetNoise(x * 1.5f, z * 1.5f) + 0.75f));
						
						int caves = (int)(noise.GetNoise(x * 5.0f, y * 5.0f, z * 5.0f) * 10.0f * (int)(noise.GetNoise(x * 0.5f, y * 0.5f, z * 0.5f) + 0.5f));
						int beach = (int)(noise.GetNoise(x * 2.5f, y * 2.5f, z * 2.5f) * 10.0f);
						
						if(caves >= 5.0f && x != 0 && z != 0 && x != world_size_x * chunk_size - 1 && z != world_size_z * chunk_size - 1 && get_block(x, y, z) != BlockConverter.block_to_int(BlockData.bedrock) && get_block(x, y, z) != BlockConverter.block_to_int(BlockData.water) && get_block(x, y + 1, z) != BlockConverter.block_to_int(BlockData.water) && get_block(x, y, z + 1) != BlockConverter.block_to_int(BlockData.water) && get_block(x, y, z - 1) != BlockConverter.block_to_int(BlockData.water) && get_block(x + 1, y, z) != BlockConverter.block_to_int(BlockData.water) && get_block(x - 1, y, z) != BlockConverter.block_to_int(BlockData.water))
						{
							set_block(x, y, z, BlockConverter.block_to_int(BlockData.air));
						}
						
						if(y == height + 22 && y <= water_height + 2 && (get_block(x, y, z) == BlockConverter.block_to_int(BlockData.grass) || get_block(x, y, z) == BlockConverter.block_to_int(BlockData.dirt)))
						{
							if(BlockConverter.int_to_block(get_block(x, y - 1, z)).is_collision[0])
							{
								if(y >= water_height)
								{
									if(beach >= 1 && beach < 5)
									{
										set_block(x, y, z, BlockConverter.block_to_int(BlockData.sand));
									}
									
									else if(beach >= 5)
									{
										set_block(x, y, z, BlockConverter.block_to_int(BlockData.gravel));
									}
								}
								
								else
								{
									if(beach >= 1 && beach < 5)
									{
										set_block(x, y, z, BlockConverter.block_to_int(BlockData.sand));
									}
									
									else if(beach >= 5 && beach < 9)
									{
										set_block(x, y, z, BlockConverter.block_to_int(BlockData.gravel));
									}
									
									else if(beach >= 9)
									{
										set_block(x, y, z, BlockConverter.block_to_int(BlockData.clay));
									}
								}
							}
						}
					}
				}
			}
			
			Random spawn_random = new Random(seed);
			
			for(int y = 0; y < world_size_y * chunk_size; y++)
			{
				for(int x = 0; x < world_size_x * chunk_size; x++)
				{
					for(int z = 0; z < world_size_z * chunk_size; z++)
					{
						int height = get_island_height(x, z, 0.875f, 4, radius) + (int)(noise.GetNoise(x * 1.45f, z * 1.45f) * 5.0f) + (int)(noise.GetNoise(x * 2.2f, z * 2.2f) * 5.0f * (int)(noise.GetNoise(x * 1.5f, z * 1.5f) + 0.75f));
						
						int spawn = spawn_random.nextInt(1, 111);
						
						if(spawn == 1)
						{
							if(get_block(x, y, z) == BlockConverter.block_to_int(BlockData.grass) && x >= 2 && x < world_size_x * chunk_size - 2 && z >= 2 && z < world_size_z * chunk_size - 2 && y > water_height)
							{
								if(y == height + 22)
								{
									set_block(x, y, z, BlockConverter.block_to_int(BlockData.dirt));
									
									int tree_height = spawn_random.nextInt(1, 4);
									
									for(int ly = tree_height + 1; ly <= tree_height + 5; ly++)
									{
										for(int lx = -1; lx <= 1; lx++)
										{
											for(int lz = -1; lz <= 1; lz++)
											{
												if(get_block(x + lx, y + ly, z + lz) != BlockConverter.block_to_int(BlockData.wood))
												{
													set_block(x + lx, y + ly, z + lz, BlockConverter.block_to_int(BlockData.leaves));
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
												if(get_block(x + lx, y + ly, z + lz) != BlockConverter.block_to_int(BlockData.wood))
												{
													set_block(x + lx, y + ly, z + lz, BlockConverter.block_to_int(BlockData.leaves));
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
												if(get_block(x + lx, y + ly, z + lz) != BlockConverter.block_to_int(BlockData.wood))
												{
													set_block(x + lx, y + ly, z + lz, BlockConverter.block_to_int(BlockData.leaves));
												}
											}
										}
									}
									
									for(int th = 1; th <= tree_height + 4; th++)
									{
										set_block(x, y + th, z, BlockConverter.block_to_int(BlockData.wood));
									}
								}
							}
						}
						
						else if(spawn == 2)
						{
							if(get_block(x, y, z) == BlockConverter.block_to_int(BlockData.grass) && y > water_height)
							{
								if(y == height + 22)
								{
									set_block(x, y + 1, z, BlockConverter.block_to_int(BlockData.chamomile));
								}
							}
						}
						
						else if(spawn == 3)
						{
							if(get_block(x, y, z) == BlockConverter.block_to_int(BlockData.grass) && y > water_height)
							{
								if(y == height + 22)
								{
									set_block(x, y + 1, z, BlockConverter.block_to_int(BlockData.rose));
								}
							}
						}
						
						else if(spawn == 4)
						{
							if(get_block(x, y, z) == BlockConverter.block_to_int(BlockData.grass) && y > water_height)
							{
								if(y == height + 22)
								{
									set_block(x, y + 1, z, BlockConverter.block_to_int(BlockData.dandelion));
								}
							}
						}
						
						int coal = (int)(noise.GetNoise(x * 10.0f, y * 10.0f, z * 10.0f) * 10.0f);
						
						if(get_block(x, y, z) == BlockConverter.block_to_int(BlockData.stone) && coal >= 7.0f && y <= height + 21)
						{
							set_block(x, y, z, BlockConverter.block_to_int(BlockData.coal_ore));
						}
						
						int iron = (int)(noise.GetNoise(x * 11.0f, y * 11.0f, z * 11.0f) * 10.0f);
						
						if(get_block(x, y, z) == BlockConverter.block_to_int(BlockData.stone) && iron >= 8.0f && y <= height + 17)
						{
							set_block(x, y, z, BlockConverter.block_to_int(BlockData.iron_ore));
						}
						
						int gold = (int)(noise.GetNoise(x * 12.0f, y * 12.0f, z * 12.0f) * 10.0f);
						
						if(get_block(x, y, z) == BlockConverter.block_to_int(BlockData.stone) && gold >= 9.0f && y <= height + 7)
						{
							set_block(x, y, z, BlockConverter.block_to_int(BlockData.gold_ore));
						}
						
						int diamond = (int)(noise.GetNoise(x * 13.0f, y * 13.0f, z * 13.0f) * 10.0f);
						
						if(get_block(x, y, z) == BlockConverter.block_to_int(BlockData.stone) && diamond >= 9.0f && y <= height + 1)
						{
							set_block(x, y, z, BlockConverter.block_to_int(BlockData.diamond_ore));
						}
					}
				}
			}
			
			for(int x = 0; x < world_size_x * chunk_size; x++)
			{
				for(int z = 0; z < world_size_z * chunk_size; z++)
				{
					set_block(x, 0, z, BlockConverter.block_to_int(BlockData.bedrock));
				}
			}
			
			for(int x = 0; x < world_size_x * chunk_size; x++)
			{
				for(int z = 0; z < world_size_z * chunk_size; z++)
				{
					boolean is_light = true;
					
					for(int Y = world_size_y * chunk_size - 1; Y >= 0; Y--)
					{
						if(!BlockConverter.int_to_block(get_block(x, Y, z)).is_transmits_light)
						{
							is_light = false;
						}
						
						set_light(x, Y, z, is_light);
					}
				}
			}
			
			for(int y = 0; y < world_size_y * chunk_size; y++)
			{
				for(int x = 0; x < world_size_x * chunk_size; x++)
				{
					for(int z = 0; z < world_size_z * chunk_size; z++)
					{
						int spawn = spawn_random.nextInt(1, 111);
						
						if(spawn == 3)
						{
							if(get_block(x, y, z) == BlockConverter.block_to_int(BlockData.stone) && get_block(x, y + 1, z) == BlockConverter.block_to_int(BlockData.air) && !get_light(x, y + 1, z))
							{
								set_block(x, y + 1, z, BlockConverter.block_to_int(BlockData.boletus));
							}
						}
						
						else if(spawn == 4)
						{
							if(get_block(x, y, z) == BlockConverter.block_to_int(BlockData.stone) && get_block(x, y + 1, z) == BlockConverter.block_to_int(BlockData.air) && !get_light(x, y + 1, z))
							{
								set_block(x, y + 1, z, BlockConverter.block_to_int(BlockData.amanita_muscaria));
							}
						}
					}
				}
			}
			
			for(int y = world_size_y * chunk_size - 1; y >= 0; y--)
			{
				for(int x = 0; x < world_size_x * chunk_size; x++)
				{
					for(int z = 0; z < world_size_z * chunk_size; z++)
					{
						if(get_block(x, y, z) == BlockConverter.block_to_int(BlockData.dirt))
						{
							if(get_light(x, y + 1, z))
							{
								set_block(x, y, z, BlockConverter.block_to_int(BlockData.grass));
							}
						}
						
						if(get_block(x, y, z) == BlockConverter.block_to_int(BlockData.grass))
						{
							if(!get_light(x, y + 1, z))
							{
								set_block(x, y, z, BlockConverter.block_to_int(BlockData.dirt));
							}
						}
					}
				}
			}
		}
		
		else if(world_type == "OCEAN")
		{
			for(int y = 0; y < world_size_y * chunk_size; y++)
			{
				for(int x = 0; x < world_size_x * chunk_size; x++)
				{
					for(int z = 0; z < world_size_z * chunk_size; z++)
					{
						set_block(x, y, z, BlockConverter.block_to_int(BlockData.air));
						
						if(y <= water_height && get_block(x, y, z) == BlockConverter.block_to_int(BlockData.air))
						{
							set_block(x, y, z, BlockConverter.block_to_int(BlockData.water));
						}
					}
				}
			}
			
			for(int x = 0; x < world_size_x * chunk_size; x++)
			{
				for(int z = 0; z < world_size_z * chunk_size; z++)
				{
					set_block(x, 0, z, BlockConverter.block_to_int(BlockData.bedrock));
				}
			}
			
			for(int x = 0; x < world_size_x * chunk_size; x++)
			{
				for(int z = 0; z < world_size_z * chunk_size; z++)
				{
					boolean is_light = true;
					
					for(int Y = world_size_y * chunk_size - 1; Y >= 0; Y--)
					{
						if(!BlockConverter.int_to_block(get_block(x, Y, z)).is_transmits_light)
						{
							is_light = false;
						}
						
						set_light(x, Y, z, is_light);
					}
				}
			}
			
			for(int y = world_size_y * chunk_size - 1; y >= 0; y--)
			{
				for(int x = 0; x < world_size_x * chunk_size; x++)
				{
					for(int z = 0; z < world_size_z * chunk_size; z++)
					{
						if(get_block(x, y, z) == BlockConverter.block_to_int(BlockData.dirt))
						{
							if(get_light(x, y + 1, z))
							{
								set_block(x, y, z, BlockConverter.block_to_int(BlockData.grass));
							}
						}
						
						if(get_block(x, y, z) == BlockConverter.block_to_int(BlockData.grass))
						{
							if(!get_light(x, y + 1, z))
							{
								set_block(x, y, z, BlockConverter.block_to_int(BlockData.dirt));
							}
						}
					}
				}
			}
		}
		
		else if(world_type == "CAVES")
		{
			Random random = new Random();
			int seed = random.nextInt();
			noise.SetSeed(seed);
			
			for(int y = 0; y < world_size_y * chunk_size; y++)
			{
				for(int x = 0; x < world_size_x * chunk_size; x++)
				{
					for(int z = 0; z < world_size_z * chunk_size; z++)
					{
						set_block(x, y, z, BlockConverter.block_to_int(BlockData.stone));
						
						if(y <= water_height && get_block(x, y, z) == BlockConverter.block_to_int(BlockData.air))
						{
							set_block(x, y, z, BlockConverter.block_to_int(BlockData.water));
						}
					}
				}
			}
			
			for(int y = 0; y < world_size_y * chunk_size; y++)
			{
				for(int x = 0; x < world_size_x * chunk_size; x++)
				{
					for(int z = 0; z < world_size_z * chunk_size; z++)
					{
						int caves = (int)(noise.GetNoise(x * 5.0f, y * 5.0f, z * 5.0f) * 10.0f);
						
						if(y > water_height)
						{
							if(caves >= 1.0f && get_block(x, y, z) != BlockConverter.block_to_int(BlockData.bedrock) && get_block(x, y, z) != BlockConverter.block_to_int(BlockData.water) && get_block(x, y + 1, z) != BlockConverter.block_to_int(BlockData.water) && get_block(x, y, z + 1) != BlockConverter.block_to_int(BlockData.water) && get_block(x, y, z - 1) != BlockConverter.block_to_int(BlockData.water) && get_block(x + 1, y, z) != BlockConverter.block_to_int(BlockData.water) && get_block(x - 1, y, z) != BlockConverter.block_to_int(BlockData.water))
							{
								set_block(x, y, z, BlockConverter.block_to_int(BlockData.air));
							}
						}
						
						else
						{
							if(caves >= 1.0f && x != 0 && z != 0 && x != world_size_x * chunk_size - 1 && z != world_size_z * chunk_size - 1 && get_block(x, y, z) != BlockConverter.block_to_int(BlockData.bedrock) && get_block(x, y, z) != BlockConverter.block_to_int(BlockData.water) && get_block(x, y + 1, z) != BlockConverter.block_to_int(BlockData.water) && get_block(x, y, z + 1) != BlockConverter.block_to_int(BlockData.water) && get_block(x, y, z - 1) != BlockConverter.block_to_int(BlockData.water) && get_block(x + 1, y, z) != BlockConverter.block_to_int(BlockData.water) && get_block(x - 1, y, z) != BlockConverter.block_to_int(BlockData.water))
							{
								set_block(x, y, z, BlockConverter.block_to_int(BlockData.air));
							}
						}
					}
				}
			}
			
			Random spawn_random = new Random(seed);
			
			for(int y = 0; y < world_size_y * chunk_size; y++)
			{
				for(int x = 0; x < world_size_x * chunk_size; x++)
				{
					for(int z = 0; z < world_size_z * chunk_size; z++)
					{
						int coal = (int)(noise.GetNoise(x * 10.0f, y * 10.0f, z * 10.0f) * 10.0f);
						
						if(get_block(x, y, z) == BlockConverter.block_to_int(BlockData.stone) && coal >= 7.0f)
						{
							set_block(x, y, z, BlockConverter.block_to_int(BlockData.coal_ore));
						}
						
						int iron = (int)(noise.GetNoise(x * 11.0f, y * 11.0f, z * 11.0f) * 10.0f);
						
						if(get_block(x, y, z) == BlockConverter.block_to_int(BlockData.stone) && iron >= 8.0f)
						{
							set_block(x, y, z, BlockConverter.block_to_int(BlockData.iron_ore));
						}
						
						int gold = (int)(noise.GetNoise(x * 12.0f, y * 12.0f, z * 12.0f) * 10.0f);
						
						if(get_block(x, y, z) == BlockConverter.block_to_int(BlockData.stone) && gold >= 9.0f)
						{
							set_block(x, y, z, BlockConverter.block_to_int(BlockData.gold_ore));
						}
						
						int diamond = (int)(noise.GetNoise(x * 13.0f, y * 13.0f, z * 13.0f) * 10.0f);
						
						if(get_block(x, y, z) == BlockConverter.block_to_int(BlockData.stone) && diamond >= 9.0f)
						{
							set_block(x, y, z, BlockConverter.block_to_int(BlockData.diamond_ore));
						}
					}
				}
			}
			
			for(int x = 0; x < world_size_x * chunk_size; x++)
			{
				for(int z = 0; z < world_size_z * chunk_size; z++)
				{
					set_block(x, 0, z, BlockConverter.block_to_int(BlockData.bedrock));
				}
			}
			
			for(int x = 0; x < world_size_x * chunk_size; x++)
			{
				for(int z = 0; z < world_size_z * chunk_size; z++)
				{
					boolean is_light = true;
					
					for(int Y = world_size_y * chunk_size - 1; Y >= 0; Y--)
					{
						if(!BlockConverter.int_to_block(get_block(x, Y, z)).is_transmits_light)
						{
							is_light = false;
						}
						
						set_light(x, Y, z, is_light);
					}
				}
			}
			
			for(int y = 0; y < world_size_y * chunk_size; y++)
			{
				for(int x = 0; x < world_size_x * chunk_size; x++)
				{
					for(int z = 0; z < world_size_z * chunk_size; z++)
					{
						int spawn = spawn_random.nextInt(1, 111);
						
						if(spawn == 3)
						{
							if(get_block(x, y, z) == BlockConverter.block_to_int(BlockData.stone) && get_block(x, y + 1, z) == BlockConverter.block_to_int(BlockData.air) && !get_light(x, y + 1, z))
							{
								set_block(x, y + 1, z, BlockConverter.block_to_int(BlockData.boletus));
							}
						}
						
						else if(spawn == 4)
						{
							if(get_block(x, y, z) == BlockConverter.block_to_int(BlockData.stone) && get_block(x, y + 1, z) == BlockConverter.block_to_int(BlockData.air) && !get_light(x, y + 1, z))
							{
								set_block(x, y + 1, z, BlockConverter.block_to_int(BlockData.amanita_muscaria));
							}
						}
					}
				}
			}
			
			for(int y = world_size_y * chunk_size - 1; y >= 0; y--)
			{
				for(int x = 0; x < world_size_x * chunk_size; x++)
				{
					for(int z = 0; z < world_size_z * chunk_size; z++)
					{
						if(get_block(x, y, z) == BlockConverter.block_to_int(BlockData.dirt))
						{
							if(get_light(x, y + 1, z))
							{
								set_block(x, y, z, BlockConverter.block_to_int(BlockData.grass));
							}
						}
						
						if(get_block(x, y, z) == BlockConverter.block_to_int(BlockData.grass))
						{
							if(!get_light(x, y + 1, z))
							{
								set_block(x, y, z, BlockConverter.block_to_int(BlockData.dirt));
							}
						}
					}
				}
			}
		}
		
		ConsoleMC.print("world is created!", PrintType.INFO);
	}
	
	public void create_world()
	{
		if(world_size == "TINY")
		{
			world_size_x = 2;
			world_size_y = 4;
			world_size_z = 2;
			
			radius = 4;
		}
		
		else if(world_size == "SMALL")
		{
			world_size_x = 4;
			world_size_y = 4;
			world_size_z = 4;
			
			radius = 18;
		}
		
		else if(world_size == "MEDIUM")
		{
			world_size_x = 8;
			world_size_y = 4;
			world_size_z = 8;
			
			radius = 48;
		}
		
		else if(world_size == "HUGE")
		{
			world_size_x = 16;
			world_size_y = 4;
			world_size_z = 16;
			
			radius = 120;
		}
		
		else if(world_size == "GIANT")
		{
			world_size_x = 32;
			world_size_y = 4;
			world_size_z = 32;
			
			radius = 300;
		}
		
		if(world_type == "DEFAULT")
		{
			water_height = 23;
		}
		
		else if(world_type == "SUPERFLAT")
		{
			water_height = 23;
		}
		
		else if(world_type == "ISLANDS")
		{
			water_height = 29;
		}
		
		else if(world_type == "OCEAN")
		{
			water_height = 32;
		}
		
		else if(world_type == "CAVES")
		{
			water_height = 4;
		}
		
		light = new boolean[(world_size_x * chunk_size) * (world_size_y * chunk_size) * (world_size_z * chunk_size)];
		data = new int[(world_size_x * chunk_size) * (world_size_y * chunk_size) * (world_size_z * chunk_size)];
		
		chunks.destroy();
		ocean.destroy();
		sky.destroy();
		
		chunks = new ChunksController(this);
		
		ocean = new Ocean(this);
		sky = new Sky(this);
		
		timer = 0.0f;
		window.player.is_pause = false;
		window.player.is_mm = false;
		is_loading = true;
		is_create = true;
	}
	
	public void save()
	{
		Thread thread = new Thread(() ->
		{
			JFileChooser jfc = new JFileChooser();
			
			jfc.setDialogTitle("Save world!");
			
			int return_value = jfc.showSaveDialog(null);
			
			if(return_value == JFileChooser.APPROVE_OPTION)
			{
				file = jfc.getSelectedFile();
				
				try
				{
					FileWriter fw = new FileWriter(file);
					
					BufferedWriter bw = new BufferedWriter(fw);
					
					bw.write(Integer.toString((world_size_x + world_size_z) / 2));
					
					bw.newLine();
					
					bw.write(Integer.toString(water_height));
					
					bw.newLine();
					
					for(int x = 0; x < world_size_x * chunk_size; x++)
					{
						for(int y = 0; y < world_size_y * chunk_size; y++)
						{
							for(int z = 0; z < world_size_z * chunk_size; z++)
							{
								bw.write(Integer.toString(get_block(x, y, z)));
								bw.write(" ");
							}
						}
					}
					
					bw.close();
					fw.close();
					
					ConsoleMC.print("<" + file + "> - world is saved!", PrintType.INFO);
				}
				
				catch(Exception e)
				{
					e.printStackTrace();
				}
				
				window.player.is_pause = false;
				window.player.is_mm = false;
			}
		});
		
		thread.start();
		threads.add(thread);
	}
	
	public void load()
	{
		Thread thread = new Thread(() ->
		{
			JFileChooser jfc = new JFileChooser();
			
			jfc.setDialogTitle("Load world!");
			
			int return_value = jfc.showSaveDialog(null);
			
			if(return_value == JFileChooser.APPROVE_OPTION)
			{
				file = jfc.getSelectedFile();

				timer = 0.0f;
				window.player.is_pause = false;
				window.player.is_mm = false;
				is_loading = true;
				is_load = true;
			}
		});
		
		thread.start();
		threads.add(thread);
	}
	
	public void set_light(int x, int y, int z, boolean is_light)
	{
		if(x >= 0 && x < world_size_x * chunk_size && y >= 0 && y < world_size_y * chunk_size && z >= 0 && z < world_size_z * chunk_size)
		{
			light[x * (world_size_y * chunk_size) * (world_size_z * chunk_size) + y * (world_size_z * chunk_size) + z] = is_light;
		}
	}
	
	public boolean get_light(int x, int y, int z)
	{
		if(x >= 0 && x < world_size_x * chunk_size && y >= 0 && y < world_size_y * chunk_size && z >= 0 && z < world_size_z * chunk_size)
		{
			return light[x * (world_size_y * chunk_size) * (world_size_z * chunk_size) + y * (world_size_z * chunk_size) + z];
		}
		
		else
		{
			return true;
		}
	}
	
	public void set_block(int x, int y, int z, int block)
	{
		if(x >= 0 && x < world_size_x * chunk_size && y >= 0 && y < world_size_y * chunk_size && z >= 0 && z < world_size_z * chunk_size)
		{
			data[x * (world_size_y * chunk_size) * (world_size_z * chunk_size) + y * (world_size_z * chunk_size) + z] = block;
		}
	}
	
	public int get_block(int x, int y, int z)
	{
		if(x >= 0 && x < world_size_x * chunk_size && y >= 0 && y < world_size_y * chunk_size && z >= 0 && z < world_size_z * chunk_size)
		{
			return data[x * (world_size_y * chunk_size) * (world_size_z * chunk_size) + y * (world_size_z * chunk_size) + z];
		}
		
		else
		{
			return BlockConverter.block_to_int(BlockData.air);
		}
	}
	
	public void add_chunk_to_update(final float time, final Vector3i pos)
	{
		if(!updater.contains(new ChunkUpdater(this, time, pos)))
		{
			updater.add(new ChunkUpdater(this, time, pos));
		}
	}
	
	public void update()
	{
		loader();
		
		for(int i = 0; i < threads.size(); i++)
		{
			if(!threads.get(i).isAlive())
			{
				threads.get(i).interrupt();
				threads.remove(i);
			}
		}
		
		for(int i = 0; i < update_blocks.size(); i++)
		{
			if(update_blocks.get(i) != null)
			{
				update_blocks.get(i).update(this);
				
				if(update_blocks.get(i).is_delete)
				{
					update_blocks.remove(i);
				}
			}
			
			else
			{
				update_blocks.remove(i);
			}
		}
		
		for(int i = 0; i < updater.size(); i++)
		{
			updater.get(i).update();
			
			if(updater.get(i).is_delete)
			{
				updater.remove(i);
			}
		}
		
		ChunkUpdater.updater();
		
		chunks.update();
	}
	
	public void destroy()
	{
		ChunkUpdater.destroy();
		update_blocks.clear();
		updater.clear();
		ocean.destroy();
		sky.destroy();
		chunks.destroy();
	}
	
	private int get_island_height(final int x, final int z, final double slope, final int height, final int radius)
	{
		int center_x = world_size_x * chunk_size / 2;
		int center_z = world_size_z * chunk_size / 2;
		
		double distance = Math.sqrt((x - center_x) * (x - center_x) + (z - center_z) * (z - center_z));
		
		double h = radius - distance;
		
		if(distance <= radius)
		{
			h = height;
		}
		
		else
		{
			double s = slope;
			h = height - (distance - radius) * s;
		}
		
		return (int)h;
	}
	
	private void loader()
	{
		if(is_load)
		{
			timer += Timer.delta_time;
			
			if(timer >= 1.0f)
			{
				try
				{
					FileReader fr = new FileReader(file);
					
					BufferedReader br = new BufferedReader(fr);
					
					String ws = br.readLine();
					String wh = br.readLine();
					
					world_size_x = Integer.parseInt(ws);
					world_size_y = 4;
					world_size_z = Integer.parseInt(ws);
					
					light = new boolean[(world_size_x * chunk_size) * (world_size_y * chunk_size) * (world_size_z * chunk_size)];
					data = new int[(world_size_x * chunk_size) * (world_size_y * chunk_size) * (world_size_z * chunk_size)];
					
					water_height = Integer.parseInt(wh);
					
					chunks.destroy();
					ocean.destroy();
					sky.destroy();
					
					chunks = new ChunksController(this);
					
					ocean = new Ocean(this);
					sky = new Sky(this);
					
					String wd = br.readLine();
					String[] wbs = wd.split(" ");
					
					int c = 0;
					
					for(int x = 0; x < world_size_x * chunk_size; x++)
					{
						for(int y = 0; y < world_size_y * chunk_size; y++)
						{
							for(int z = 0; z < world_size_z * chunk_size; z++)
							{
								set_block(x, y, z, Integer.parseInt(wbs[c]));
								c++;
							}
						}
					}
					
					for(int x = 0; x < world_size_x * chunk_size; x++)
					{
						for(int z = 0; z < world_size_z * chunk_size; z++)
						{
							boolean is_light = true;
							
							for(int Y = world_size_y * chunk_size - 1; Y >= 0; Y--)
							{
								if(!BlockConverter.int_to_block(get_block(x, Y, z)).is_transmits_light)
								{
									is_light = false;
								}
								
								set_light(x, Y, z, is_light);
							}
						}
					}
					
					br.close();
					fr.close();
					
					ConsoleMC.print("<" + file + "> - world is loaded!", PrintType.INFO);
					
					for(int x = 0; x < world_size_x; x++)
					{
						for(int y = 0; y < world_size_y; y++)
						{
							for(int z = 0; z < world_size_z; z++)
							{
								chunks.generate_chunk(x, y, z);
							}
						}
					}
					
					window.player.pos = new Vector3f(world_size_x * World.chunk_size / 2.0f, 0.0f, world_size_z * World.chunk_size / 2.0f);
					
					for(int Y = world_size_y * World.chunk_size - 1; Y >= 0; Y--)
					{
						if(BlockConverter.int_to_block(get_block((int)window.player.pos.x, Y, (int)window.player.pos.z)).is_collision[0])
						{
							window.player.pos.y = Y + 0.5f + window.player.aabb.size.y / 2.0f;
							window.player.aabb.pos = window.player.pos;
							break;
						}
					}
					
					window.player.save_pos = new Vector3f(window.player.pos.x, window.player.pos.y, window.player.pos.z);
					
					window.player.blocks[0] = BlockData.stone;
					window.player.blocks[1] = BlockData.grass;
					window.player.blocks[2] = BlockData.dirt;
					window.player.blocks[3] = BlockData.wooden_planks;
					window.player.blocks[4] = BlockData.cobblestone;
					window.player.blocks[5] = BlockData.wood;
					window.player.blocks[6] = BlockData.leaves;
					window.player.blocks[7] = BlockData.sand;
					window.player.blocks[8] = BlockData.chamomile;
					
					is_loading = false;
					is_load = false;
					timer = 0.0f;
					Mouse.setGrabbed(true);
				}
				
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		
		if(is_create)
		{
			timer += Timer.delta_time;
			
			if(timer >= 1.0f)
			{
				create();
				
				for(int x = 0; x < world_size_x; x++)
				{
					for(int y = 0; y < world_size_y; y++)
					{
						for(int z = 0; z < world_size_z; z++)
						{
							chunks.generate_chunk(x, y, z);
						}
					}
				}
				
				window.player.pos = new Vector3f(world_size_x * World.chunk_size / 2.0f, 0.0f, world_size_z * World.chunk_size / 2.0f);
				
				for(int Y = world_size_y * World.chunk_size - 1; Y >= 0; Y--)
				{
					if(BlockConverter.int_to_block(get_block((int)window.player.pos.x, Y, (int)window.player.pos.z)).is_collision[0])
					{
						window.player.pos.y = Y + 0.5f + window.player.aabb.size.y / 2.0f;
						window.player.aabb.pos = window.player.pos;
						break;
					}
				}
				
				window.player.save_pos = new Vector3f(window.player.pos.x, window.player.pos.y, window.player.pos.z);
				
				window.player.blocks[0] = BlockData.stone;
				window.player.blocks[1] = BlockData.grass;
				window.player.blocks[2] = BlockData.dirt;
				window.player.blocks[3] = BlockData.wooden_planks;
				window.player.blocks[4] = BlockData.cobblestone;
				window.player.blocks[5] = BlockData.wood;
				window.player.blocks[6] = BlockData.leaves;
				window.player.blocks[7] = BlockData.sand;
				window.player.blocks[8] = BlockData.chamomile;
				
				is_loading = false;
				is_create = false;
				timer = 0.0f;
				Mouse.setGrabbed(true);
			}
		}
	}
	
	public void generate_map() throws Exception
	{
		int size = 4;
		
		int w = (world_size_x * chunk_size) * size;
		int h = (world_size_z * chunk_size) * size;
		
		BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		
		Graphics2D g2d = img.createGraphics();
		
		Color color;
		
		for(int x = 0; x < world_size_x * chunk_size; x++)
		{
			for(int z = 0; z < world_size_z * chunk_size; z++)
			{
				for(int y = world_size_y * chunk_size - 1; y >= 0; y--)
				{
					if(BlockConverter.int_to_block(get_block(x, y, z)).block_type != BlockType.air && BlockConverter.int_to_block(get_block(x, y, z)).block_type != BlockType.plant && BlockConverter.int_to_block(get_block(x, y, z)) != BlockData.glass)
					{
						if(BlockConverter.int_to_block(get_block(x, y, z)) == BlockData.stone || BlockConverter.int_to_block(get_block(x, y, z)) == BlockData.coal_ore || BlockConverter.int_to_block(get_block(x, y, z)) == BlockData.iron_ore || BlockConverter.int_to_block(get_block(x, y, z)) == BlockData.gold_ore || BlockConverter.int_to_block(get_block(x, y, z)) == BlockData.diamond_ore)
						{
							color = new Color(112, 112, 112);
						}
						
						else if(BlockConverter.int_to_block(get_block(x, y, z)) == BlockData.grass)
						{
							color = new Color(0, 223, 0);
						}
						
						else if(BlockConverter.int_to_block(get_block(x, y, z)) == BlockData.dirt)
						{
							color = new Color(128, 64, 0);
						}
						
						else if(BlockConverter.int_to_block(get_block(x, y, z)) == BlockData.wooden_planks || BlockConverter.int_to_block(get_block(x, y, z)) == BlockData.bookshelf)
						{
							color = new Color(223, 128, 0);
						}
						
						else if(BlockConverter.int_to_block(get_block(x, y, z)) == BlockData.bedrock)
						{
							color = new Color(4, 4, 4);
						}
						
						else if(BlockConverter.int_to_block(get_block(x, y, z)) == BlockData.water)
						{
							color = new Color(0, 0, 223);
						}
						
						else if(BlockConverter.int_to_block(get_block(x, y, z)) == BlockData.cobblestone)
						{
							color = new Color(48, 48, 48);
						}
						
						else if(BlockConverter.int_to_block(get_block(x, y, z)) == BlockData.wood)
						{
							color = new Color(48, 32, 0);
						}
						
						else if(BlockConverter.int_to_block(get_block(x, y, z)) == BlockData.leaves)
						{
							color = new Color(0, 255, 128);
						}
						
						else if(BlockConverter.int_to_block(get_block(x, y, z)) == BlockData.sand)
						{
							color = new Color(255, 255, 128);
						}
						
						else if(BlockConverter.int_to_block(get_block(x, y, z)) == BlockData.gravel)
						{
							color = new Color(182, 182, 182);
						}
						
						else if(BlockConverter.int_to_block(get_block(x, y, z)) == BlockData.bricks)
						{
							color = new Color(182, 0, 0);
						}
						
						else if(BlockConverter.int_to_block(get_block(x, y, z)) == BlockData.stone_bricks)
						{
							color = new Color(82, 82, 82);
						}
						
						else if(BlockConverter.int_to_block(get_block(x, y, z)) == BlockData.mossy_cobblestone)
						{
							color = new Color(48, 64, 48);
						}
						
						else if(BlockConverter.int_to_block(get_block(x, y, z)) == BlockData.clay)
						{
							color = new Color(192, 84, 0);
						}
						
						else if(BlockConverter.int_to_block(get_block(x, y, z)) == BlockData.iron_block)
						{
							color = new Color(192, 192, 192);
						}
						
						else if(BlockConverter.int_to_block(get_block(x, y, z)) == BlockData.gold_block)
						{
							color = new Color(255, 255, 192);
						}
						
						else if(BlockConverter.int_to_block(get_block(x, y, z)) == BlockData.diamond_block)
						{
							color = new Color(128, 255, 255);
						}
						
						else if(BlockConverter.int_to_block(get_block(x, y, z)) == BlockData.white_wool)
						{
							color = new Color(255, 255, 255);
						}
						
						else if(BlockConverter.int_to_block(get_block(x, y, z)) == BlockData.gray_wool)
						{
							color = new Color(128, 128, 128);
						}
						
						else if(BlockConverter.int_to_block(get_block(x, y, z)) == BlockData.black_wool)
						{
							color = new Color(0, 0, 0);
						}
						
						else if(BlockConverter.int_to_block(get_block(x, y, z)) == BlockData.red_wool)
						{
							color = new Color(255, 0, 0);
						}
						
						else if(BlockConverter.int_to_block(get_block(x, y, z)) == BlockData.green_wool)
						{
							color = new Color(0, 255, 0);
						}
						
						else if(BlockConverter.int_to_block(get_block(x, y, z)) == BlockData.blue_wool)
						{
							color = new Color(0, 0, 255);
						}
						
						else if(BlockConverter.int_to_block(get_block(x, y, z)) == BlockData.yellow_wool)
						{
							color = new Color(255, 255, 0);
						}
						
						else if(BlockConverter.int_to_block(get_block(x, y, z)) == BlockData.orange_wool)
						{
							color = new Color(255, 128, 0);
						}
						
						else if(BlockConverter.int_to_block(get_block(x, y, z)) == BlockData.brown_wool)
						{
							color = new Color(64, 32, 0);
						}
						
						else if(BlockConverter.int_to_block(get_block(x, y, z)) == BlockData.purple_wool)
						{
							color = new Color(128, 0, 128);
						}
						
						else if(BlockConverter.int_to_block(get_block(x, y, z)) == BlockData.pink_wool)
						{
							color = new Color(255, 0, 255);
						}
						
						else
						{
							color = new Color(0, 0, 0);
						}
						
						Random random = new Random();
						
						float l = random.nextFloat(0.875f, 1.0f);
						
						float r = (float)color.getRed() * l;
						float g = (float)color.getGreen() * l;
						float b = (float)color.getBlue() * l;
						
						Color result_color = new Color((int)r, (int)g, (int)b);
						
						g2d.setColor(result_color);
						g2d.fillRect(x * size, z * size, x * size + size, z * size + size);
						
						break;
					}
				}
			}
		}
		
		g2d.dispose();
		
		int img_count = 0;
		
		while(new File(System.getProperty("user.home") + "\\.minecreate\\mc_map_" + Integer.toString(img_count) + ".png").exists())
		{
			img_count++;
		}
		
		ImageIO.write(img, "png", new File(System.getProperty("user.home") + "\\.minecreate\\mc_map_" + Integer.toString(img_count) + ".png"));
		
		ConsoleMC.print("<" + new File(System.getProperty("user.home") + "\\.minecreate\\mc_map_" + Integer.toString(img_count) + ".png") + "> - map is generated!", PrintType.INFO);
	}
}