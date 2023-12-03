package com.gamedev.minecreate.player;

import com.gamedev.minecreate.gameengine.audio.Audio;
import com.gamedev.minecreate.gameengine.camera.Camera;
import com.gamedev.minecreate.gameengine.time.Timer;
import com.gamedev.minecreate.particle.Particle;
import com.gamedev.minecreate.resources.ResourcesMC;
import com.gamedev.minecreate.world.World;
import com.gamedev.minecreate.chunk.ChunkRenderer;
import com.gamedev.minecreate.block.*;
import com.gamedev.minecreate.physics.*;

import java.lang.Math;
import java.util.Random;
import java.util.ArrayList;

import org.lwjgl.input.*;

import org.joml.*;

public class Player
{
	public World world;
	
	public Camera camera;
	
	public Vector3f pos;
	public Vector3f last_pos;
	public Vector3f save_pos;
	public Vector3f front;
	public Vector3f move_front;
	public Vector3f Up;
	public Vector3f right;
	public Vector3f dir;
	public Vector3f move_dir;
	public Vector3f r_dir;
	
	public AABB aabb;
	
	public float speed;
	public float underwater_speed;
	public float sensitivity;
	
	public float camera_speed;
	public float camera_strength;
	
	public boolean is_ground;
	public boolean is_touch;
	public boolean is_underwater[];
	public boolean is_underground;
	
	public float velocity;
	
	public int radius;
	
	public int blck;
	
	public Block[] blocks;
	public Block[][] all_blocks;
	
	public BlockSelector selector;
	public BlockHand block_h;
	
	public float dir_x;
	public float dir_y;
	
	public boolean is_mm;
	public boolean is_inventory;
	public boolean is_pause;
	public boolean is_gw_menu;
	
	public Audio[] audio;
	
	public ArrayList<Thread> threads;;
	
	private float current_speed;
	
	private boolean[] is_click;
	
	private float walk;
	public float walks;
	
	private Block last_walk_block;
	
	public Player(final World world, final Vector3f pos, final float speed, final float sensitivity, final float camera_speed, final float camera_strength)
	{
		is_pause = true;
		is_mm = true;
		
		this.world = world;
		
		this.pos = pos;
		
		this.speed = speed;
		this.sensitivity = sensitivity;
		
		this.camera_speed = camera_speed;
		this.camera_strength = camera_strength;
		
		underwater_speed = this.speed / 2.0f;
		
		radius = 50;
		
		Up = new Vector3f(0.0f, 1.0f, 0.0f);
		dir = new Vector3f();
		move_dir = new Vector3f();
		r_dir = new Vector3f();
		
		dir_x = 0.0f;
		dir_y = 0.0f;
		
		camera = new Camera(this.pos, 60.0f, 0.1f, 1000.0f);
		
		aabb = new AABB(this.pos, new Vector3f(0.75f, 1.8f, 0.75f));
		
		blocks = new Block[9];
		blocks[0] = BlockData.stone;
		blocks[1] = BlockData.grass;
		blocks[2] = BlockData.dirt;
		blocks[3] = BlockData.wooden_planks;
		blocks[4] = BlockData.cobblestone;
		blocks[5] = BlockData.wood;
		blocks[6] = BlockData.leaves;
		blocks[7] = BlockData.sand;
		blocks[8] = BlockData.chamomile;
		
		all_blocks = new Block[11][9];
		all_blocks[0][8] = BlockData.stone;
		all_blocks[1][8] = BlockData.grass;
		all_blocks[2][8] = BlockData.dirt;
		all_blocks[3][8] = BlockData.wood;
		all_blocks[4][8] = BlockData.leaves;
		all_blocks[5][8] = BlockData.sand;
		all_blocks[6][8] = BlockData.gravel;
		all_blocks[7][8] = BlockData.coal_ore;
		all_blocks[8][8] = BlockData.iron_ore;
		all_blocks[9][8] = BlockData.gold_ore;
		all_blocks[10][8] = BlockData.diamond_ore;
		all_blocks[0][7] = BlockData.wooden_planks;
		all_blocks[1][7] = BlockData.cobblestone;
		all_blocks[2][7] = BlockData.mossy_cobblestone;
		all_blocks[3][7] = BlockData.glass;
		all_blocks[4][7] = BlockData.bricks;
		all_blocks[5][7] = BlockData.stone_bricks;
		all_blocks[6][7] = BlockData.bookshelf;
		all_blocks[7][7] = BlockData.clay;
		all_blocks[8][7] = BlockData.iron_block;
		all_blocks[9][7] = BlockData.gold_block;
		all_blocks[10][7] = BlockData.diamond_block;
		all_blocks[0][6] = BlockData.white_wool;
		all_blocks[1][6] = BlockData.gray_wool;
		all_blocks[2][6] = BlockData.black_wool;
		all_blocks[3][6] = BlockData.red_wool;
		all_blocks[4][6] = BlockData.green_wool;
		all_blocks[5][6] = BlockData.blue_wool;
		all_blocks[6][6] = BlockData.yellow_wool;
		all_blocks[7][6] = BlockData.orange_wool;
		all_blocks[8][6] = BlockData.brown_wool;
		all_blocks[9][6] = BlockData.purple_wool;
		all_blocks[10][6] = BlockData.pink_wool;
		all_blocks[0][5] = BlockData.chamomile;
		all_blocks[1][5] = BlockData.rose;
		all_blocks[2][5] = BlockData.dandelion;
		all_blocks[3][5] = BlockData.sapling;
		all_blocks[4][5] = BlockData.boletus;
		all_blocks[5][5] = BlockData.amanita_muscaria;
		
		blck = 0;
		
		velocity = 0.0f;
		
		is_underwater = new boolean[3];
		
		is_click = new boolean[8];
		
		is_inventory = false;
		is_pause = false;
		
		selector = new BlockSelector();
		block_h = new BlockHand(this);
		
		audio = new Audio[3];
		
		try
		{
			audio[0] = new Audio(ResourcesMC.day_ambience, true, true, 1.0f, 0.0f, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
			audio[0].update(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
			audio[0].play();
			world.window.sounds.add(audio[0]);
			
			audio[1] = new Audio(ResourcesMC.underwater_ambience, true, true, 1.0f, 0.0f, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
			audio[1].update(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
			audio[1].play();
			world.window.sounds.add(audio[1]);
			
			audio[2] = new Audio(ResourcesMC.underground_ambience, true, true, 1.0f, 0.0f, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
			audio[2].update(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
			audio[2].play();
			world.window.sounds.add(audio[2]);
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		threads = new ArrayList<Thread>();
	}
	
	public void update()
	{
		update_threads();
		
		check_clicks();
		update_dir();
		
		if(!Timer.first_frame && !world.is_loading && !is_mm)
		{
			update_gravity();
			update_movement();
			
			try
			{
				check_blck();
			}
			
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			update_blocks();
			update_selector();
		}
		
		update_aabb_camera_pos();
		check_pos_camera();
	}
	
	public void destroy()
	{
		threads.clear();
		
		audio[0].destroy();
		audio[1].destroy();
		audio[2].destroy();
		
		block_h.destroy();
		selector.destroy();
	}
	
	private void update_threads()
	{
		for(int i = 0; i < threads.size(); i++)
		{
			if(!threads.get(i).isAlive())
			{
				threads.get(i).interrupt();
				threads.remove(i);
			}
		}
	}
	
	private void create_block_particles(final Vector3f position, final Block block)
	{
		Random random = new Random();
		
		for(int n = 0; n < random.nextInt(7, 14); n++)
		{
			Thread thread = new Thread(() ->
			{
				float s = 7.0f;
				
				Vector3f p = new Vector3f(random.nextFloat(position.x - 0.5f, position.x + 0.5f), random.nextFloat(position.y - 0.5f, position.y + 0.5f), random.nextFloat(position.z - 0.5f, position.z + 0.5f));
				
				Vector2f tc = new Vector2f(random.nextFloat(block.tex_coords[2].x * ChunkRenderer.ot, block.tex_coords[2].x * ChunkRenderer.ot + ChunkRenderer.ot - (ChunkRenderer.ot / s)), random.nextFloat(block.tex_coords[2].y * ChunkRenderer.ot, block.tex_coords[2].y * ChunkRenderer.ot + ChunkRenderer.ot - (ChunkRenderer.ot / s)));
				
				Vector2f[] tex_coods = new Vector2f[4];
				
				tex_coods[0] = new Vector2f(tc.x, tc.y + ChunkRenderer.ot / s);
				tex_coods[1] = new Vector2f(tc.x + ChunkRenderer.ot / s, tc.y + ChunkRenderer.ot / s);
				tex_coods[2] = new Vector2f(tc.x + ChunkRenderer.ot / s, tc.y);
				tex_coods[3] = new Vector2f(tc.x, tc.y);
				
				Vector3f p_pos = p;
				Vector3f explsn_pos = position;
				
				Vector3f pf = new Vector3f(p_pos).sub(explsn_pos).normalize();
				
				world.window.particles.add(new Particle(world, p, pf, 5.0f, random.nextFloat(0.01f, 0.1f), random.nextFloat(0.5f, 1.5f), tex_coods, 0.1875f));
			});
			
			thread.start();
			threads.add(thread);
		}
	}
	
	private void check_clicks()
	{
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && !world.is_loading && !is_mm)
		{
			if(!is_click[6] && !is_inventory)
			{
				is_click[6] = true;
				
				is_pause = !is_pause;
				is_gw_menu = false;
				
				Mouse.setGrabbed(!is_pause);
			}
		}
		
		else
		{
			is_click[6] = false;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_I) && !world.is_loading)
		{
			if(!is_click[4] && !is_pause)
			{
				is_click[4] = true;
				
				is_inventory = !is_inventory;
				
				Mouse.setGrabbed(!is_inventory);
			}
		}
		
		else
		{
			is_click[4] = false;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_R) && !world.is_loading)
		{
			if(!is_click[2] && !is_pause)
			{
				is_click[2] = true;
				pos = new Vector3f(save_pos.x, save_pos.y, save_pos.z);;
				velocity = 0.0f;
			}
		}
		
		else
		{
			is_click[2] = false;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_F) && !world.is_loading)
		{
			if(!is_click[3] && !is_pause)
			{
				is_click[3] = true;
				
				if(radius == 50)
				{
					radius = 8;
				}
				
				else if(radius == 8)
				{
					radius = 4;
				}
				
				else if(radius == 4)
				{
					radius = 1;
				}
				
				else if(radius == 1)
				{
					radius = 50;
				}
			}
		}
		
		else
		{
			is_click[3] = false;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_F1) && !world.is_loading && !is_mm)
		{
			if(!is_click[5])
			{
				is_click[5] = true;
				
				world.window.is_debug = !world.window.is_debug;
			}
		}
		
		else
		{
			is_click[5] = false;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_C) && !world.is_loading)
		{
			save_pos = new Vector3f(pos.x, pos.y, pos.z);
		}
	}
	
	private void update_dir()
	{
		if(!is_inventory && !is_pause && !world.is_loading)
		{
			dir_x += Mouse.getDX() * sensitivity;
			dir_y += Mouse.getDY() * sensitivity;
		}
		
		if(dir_y > 89.9f)
		{
			dir_y = 89.9f;
		}
		
		if(dir_y < -89.9f)
		{
			dir_y = -89.9f;
		}
		
		dir.x = (float)(Math.cos(Math.toRadians(dir_x)) * Math.cos(Math.toRadians(dir_y)));
		dir.y = (float)(Math.sin(Math.toRadians(dir_y)));
		dir.z = (float)(Math.sin(Math.toRadians(dir_x)) * Math.cos(Math.toRadians(dir_y)));
		front = dir.normalize();
		
		r_dir.x = (float)(Math.cos(Math.toRadians(dir_x + 90.0f)) * Math.cos(Math.toRadians(0.0f)));
		r_dir.y = (float)(Math.sin(Math.toRadians(0.0f)));
		r_dir.z = (float)(Math.sin(Math.toRadians(dir_x + 90.0f)) * Math.cos(Math.toRadians(0.0f)));
		right = r_dir.normalize();
		
		move_dir.x = (float)(Math.cos(Math.toRadians(dir_x)) * Math.cos(Math.toRadians(0.0f)));
		move_dir.y = (float)(Math.sin(Math.toRadians(0.0f)));
		move_dir.z = (float)(Math.sin(Math.toRadians(dir_x)) * Math.cos(Math.toRadians(0.0f)));
		move_front = move_dir.normalize();
	}
	
	private void update_gravity()
	{
		last_pos = pos;
		
		boolean check;
		
		if(is_touch)
		{
			check = is_underwater[1];
		}
		
		else
		{
			check = is_underwater[2];
		}
		
		if(check)
		{
			if(Keyboard.isKeyDown(Keyboard.KEY_SPACE) && !is_inventory && !is_pause && !world.is_loading)
			{
				if(velocity < 0.25f)
				{
					velocity += 2.5f * Timer.delta_time;
				}
			}
			
			else
			{
				if(velocity > -0.25f)
				{
					velocity -= 2.5f * Timer.delta_time;
				}
				
				if(velocity < -0.25f)
				{
					velocity += 2.5f * Timer.delta_time;
				}
			}
		}
		
		else
		{
			if(is_ground)
			{
				velocity = 0.0f;
				
				if(Keyboard.isKeyDown(Keyboard.KEY_SPACE) && !is_inventory && !is_pause && !world.is_loading)
				{
					velocity += 0.7f;
				}
			}
			
			else
			{
				velocity -= 1.75f * Timer.delta_time;
			}
		}
		
		if(is_underwater[1])
		{
			current_speed = underwater_speed;
		}
		
		else
		{
			current_speed = speed;
		}
	}
	
	private void update_movement()
	{
		Vector3f offset = new Vector3f();
		Vector3f vel = new Vector3f();
		Vector3f result = new Vector3f();
		Vector3f before_walk = new Vector3f();
		Vector3f after_walk = new Vector3f();
		
		if(!is_inventory && !is_pause && !world.is_loading)
		{
			if(Keyboard.isKeyDown(Keyboard.KEY_W))
			{
				Vector3f move = new Vector3f();
				move_front.mul(current_speed * Timer.delta_time, move);
				offset.add(move);
			}
			
			if(Keyboard.isKeyDown(Keyboard.KEY_S))
			{
				Vector3f move = new Vector3f();
				move_front.mul(current_speed * Timer.delta_time, move);
				offset.sub(move);
			}
			
			if(Keyboard.isKeyDown(Keyboard.KEY_A))
			{
				Vector3f move1 = new Vector3f();
				Vector3f move2 = new Vector3f();
				move_front.cross(Up, move2);
				move2.normalize();
				move2.mul(current_speed * Timer.delta_time, move1);
				offset.sub(move1);
			}
			
			if(Keyboard.isKeyDown(Keyboard.KEY_D))
			{
				Vector3f move1 = new Vector3f();
				Vector3f move2 = new Vector3f();
				move_front.cross(Up, move2);
				move2.normalize();
				move2.mul(current_speed * Timer.delta_time, move1);
				offset.add(move1);
			}
		}
		
		if(Timer.fps() < 4)
		{
			velocity = 0.0f;
		}
		
		Up.mul(-9.81f * Timer.delta_time * -velocity, vel);
		
		offset.add(vel);
		offset.div(50.0f, result);
		
		is_ground = false;
		is_touch = false;
		
		before_walk = new Vector3f(pos.x, 0.0f, pos.z);
		
		if(Timer.fps() >= 5)
		{
			for(int i = 0; i < 50; i++)
			{
				pos.add(new Vector3f(result.x, 0, 0));
				aabb.pos = pos;
				check_collision(new Vector3f(result.x, 0, 0));
				aabb.pos = pos;
				
				pos.add(new Vector3f(0, result.y, 0));
				aabb.pos = pos;
				check_collision(new Vector3f(0, result.y, 0));
				aabb.pos = pos;
				
				pos.add(new Vector3f(0, 0, result.z));
				aabb.pos = pos;
				check_collision(new Vector3f(0, 0, result.z));
				aabb.pos = pos;
			}
		}
		
		after_walk = new Vector3f(pos.x, 0.0f, pos.z);
		
		update_aabb_camera_pos();

		if(offset.x != 0 || offset.z != 0)
		{
			walk += after_walk.distance(before_walk);
			
			if(is_ground)
			{
				walks += after_walk.distance(before_walk);
			}
			
			else
			{
				walks += after_walk.distance(before_walk) / 16.0f;
			}
			
			if(walk >= 1.5f)
			{
				walk = 0.0f;
				
				int x = (int)(Math.round(pos.x));
				int y = (int)(Math.round(aabb.min_y() - 0.1f));
				int z = (int)(Math.round(pos.z));
				
				Block block = BlockConverter.int_to_block(world.get_block(x, y, z));
				
				if(block.sound != null)
				{
					try
					{
						Random random = new Random();
						
						Audio audio = new Audio(block.sound, false, false, random.nextFloat(0.85f, 1.15f), 0.25f, new Vector3f(pos.x, aabb.min_y(), pos.z), camera.pos, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), camera.front, camera.up);
						audio.update(pos, new Vector3f(0, 0, 0), camera.front, camera.up);
						audio.play();
						world.window.sounds.add(audio);
					}
					
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
				
				last_walk_block = block;
			}
			
			if(last_walk_block != null)
			{
				int x = (int)(Math.round(pos.x));
				int y = (int)(Math.round(aabb.min_y() - 0.1f));
				int z = (int)(Math.round(pos.z));
				
				Block block = BlockConverter.int_to_block(world.get_block(x, y, z));
				
				if(last_walk_block.sound == null)
				{
					walk = 0.0f;
					
					if(block.sound != null)
					{
						try
						{
							Random random = new Random();
							
							Audio audio = new Audio(block.sound, false, false, random.nextFloat(0.85f, 1.15f), 0.25f, new Vector3f(pos.x, aabb.min_y(), pos.z), camera.pos, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), camera.front, camera.up);
							audio.update(pos, new Vector3f(0, 0, 0), camera.front, camera.up);
							audio.play();
							world.window.sounds.add(audio);
						}
						
						catch(Exception e)
						{
							e.printStackTrace();
						}
					}
				}
				
				last_walk_block = block;
			}
		}
		
		else
		{
			walk = 1.5f;
			walks = 0.0f;
		}
	}
	
	private void check_blck() throws Exception
	{
		if(!is_pause && !is_mm)
		{
			if(Keyboard.isKeyDown(Keyboard.KEY_1) && blck != 0 && !is_click[7])
			{
				is_click[7] = true;
				
				Audio audio = new Audio(ResourcesMC.select_block, true, false, 1.0f, 0.25f, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
				audio.update(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
				audio.play();
				
				world.window.sounds.add(audio);
				
				blck = 0;
			}
			
			if(Keyboard.isKeyDown(Keyboard.KEY_2) && blck != 1 && !is_click[7])
			{
				is_click[7] = true;
				
				Audio audio = new Audio(ResourcesMC.select_block, true, false, 1.0f, 0.25f, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
				audio.update(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
				audio.play();
				
				world.window.sounds.add(audio);
				
				blck = 1;
			}
			
			if(Keyboard.isKeyDown(Keyboard.KEY_3) && blck != 2 && !is_click[7])
			{
				is_click[7] = true;
				
				Audio audio = new Audio(ResourcesMC.select_block, true, false, 1.0f, 0.25f, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
				audio.update(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
				audio.play();
				
				world.window.sounds.add(audio);
				
				blck = 2;
			}
			
			if(Keyboard.isKeyDown(Keyboard.KEY_4) && blck != 3 && !is_click[7])
			{
				is_click[7] = true;
				
				Audio audio = new Audio(ResourcesMC.select_block, true, false, 1.0f, 0.25f, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
				audio.update(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
				audio.play();
				
				world.window.sounds.add(audio);
				
				blck = 3;
			}
			
			if(Keyboard.isKeyDown(Keyboard.KEY_5) && blck != 4 && !is_click[7])
			{
				is_click[7] = true;
				
				Audio audio = new Audio(ResourcesMC.select_block, true, false, 1.0f, 0.25f, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
				audio.update(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
				audio.play();
				
				world.window.sounds.add(audio);
				
				blck = 4;
			}
			
			if(Keyboard.isKeyDown(Keyboard.KEY_6) && blck != 5 && !is_click[7])
			{
				is_click[7] = true;
				
				Audio audio = new Audio(ResourcesMC.select_block, true, false, 1.0f, 0.25f, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
				audio.update(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
				audio.play();
				
				world.window.sounds.add(audio);
				
				blck = 5;
			}
			
			if(Keyboard.isKeyDown(Keyboard.KEY_7) && blck != 6 && !is_click[7])
			{
				is_click[7] = true;
				
				Audio audio = new Audio(ResourcesMC.select_block, true, false, 1.0f, 0.25f, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
				audio.update(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
				audio.play();
				
				world.window.sounds.add(audio);
				
				blck = 6;
			}
			
			if(Keyboard.isKeyDown(Keyboard.KEY_8) && blck != 7 && !is_click[7])
			{
				is_click[7] = true;
				
				Audio audio = new Audio(ResourcesMC.select_block, true, false, 1.0f, 0.25f, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
				audio.update(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
				audio.play();
				
				world.window.sounds.add(audio);
				
				blck = 7;
			}
			
			if(Keyboard.isKeyDown(Keyboard.KEY_9) && blck != 8 && !is_click[7])
			{
				is_click[7] = true;
				
				Audio audio = new Audio(ResourcesMC.select_block, true, false, 1.0f, 0.25f, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
				audio.update(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
				audio.play();
				
				world.window.sounds.add(audio);
				
				blck = 8;
			}
			
			if(!Keyboard.isKeyDown(Keyboard.KEY_1) && !Keyboard.isKeyDown(Keyboard.KEY_2) && !Keyboard.isKeyDown(Keyboard.KEY_3) && !Keyboard.isKeyDown(Keyboard.KEY_4) && !Keyboard.isKeyDown(Keyboard.KEY_5) && !Keyboard.isKeyDown(Keyboard.KEY_6) && !Keyboard.isKeyDown(Keyboard.KEY_7) && !Keyboard.isKeyDown(Keyboard.KEY_8) && !Keyboard.isKeyDown(Keyboard.KEY_9))
			{
				is_click[7] = false;
			}
			
			int msw = Mouse.getDWheel();
			
			if(msw < 0)
			{
				Audio audio = new Audio(ResourcesMC.select_block, true, false, 1.0f, 0.25f, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
				audio.update(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
				audio.play();
				
				world.window.sounds.add(audio);
				
				if(blck == 8)
				{
					blck = 0;
				}
				
				else
				{
					blck++;
				}
			}
			
			if(msw > 0)
			{
				Audio audio = new Audio(ResourcesMC.select_block, true, false, 1.0f, 0.25f, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
				audio.update(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
				audio.play();
				
				world.window.sounds.add(audio);
				
				if(blck == 0)
				{
					blck = 8;
				}
				
				else
				{
					blck--;
				}
			}
		}
	}
	
	private void update_selector()
	{
		Raycast ray = new Raycast(this);
		ray.hit(camera.pos, camera.front, 3.0f, 5);
		
		selector.is_render = ray.is_hit;
		
		if(ray.is_hit)
		{
			int X = (int)(Math.round((ray.pos.x - ray.normal.x / 2.0f)));
			int Y = (int)(Math.round((ray.pos.y - ray.normal.y / 2.0f)));
			int Z = (int)(Math.round((ray.pos.z - ray.normal.z / 2.0f)));
				
			selector.pos = new Vector3f(X, Y, Z);
		}
	}
	
	private void update_blocks()
	{
		if(Mouse.isButtonDown(0) && !is_inventory && !is_pause && !world.is_loading)
		{
			if(!is_inventory && !is_pause)
			{
				Mouse.setGrabbed(true);
			}
			
			if(!is_click[0])
			{	
				is_click[0] = true;
				
				Raycast ray = new Raycast(this);
				ray.hit(camera.pos, camera.front, 3.0f, 100);
				
				if(ray.is_hit)
				{
					int X = (int)(Math.round((ray.pos.x - ray.normal.x / 2.0f)));
					int Y = (int)(Math.round((ray.pos.y - ray.normal.y / 2.0f)));
					int Z = (int)(Math.round((ray.pos.z - ray.normal.z / 2.0f)));
					
					if(world.get_block(X, Y, Z) != BlockConverter.block_to_int(BlockData.bedrock))
					{
						Block block = BlockConverter.int_to_block(world.get_block(X, Y, Z));
						
						try
						{
							if(block.sound != null)
							{
								if(block.sound == ResourcesMC.glass)
								{
									Random random = new Random();
									
									Audio audio = new Audio(ResourcesMC.glass_hit, false, false, random.nextFloat(0.9f, 1.1f), 1.0f, new Vector3f(X, Y, Z), camera.pos, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), camera.front, camera.up);
									audio.update(camera.pos, new Vector3f(0, 0, 0), camera.front, camera.up);
									audio.play();
									world.window.sounds.add(audio);
								}
								
								else
								{
									Random random = new Random();
									
									Audio audio = new Audio(block.sound, false, false, random.nextFloat(0.9f, 1.1f), 1.0f, new Vector3f(X, Y, Z), camera.pos, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), camera.front, camera.up);
									audio.update(camera.pos, new Vector3f(0, 0, 0), camera.front, camera.up);
									audio.play();
									world.window.sounds.add(audio);
								}
							}
						}
						
						catch(Exception e)
						{
							e.printStackTrace();
						}
						
						world.set_block(X, Y, Z, BlockConverter.block_to_int(BlockData.air));
						
						int x = (int)(Math.floor((float)X / (float)World.chunk_size));
						int z = (int)(Math.floor((float)Z / (float)World.chunk_size));

						for(int _y_ = 0; _y_ < world.world_size_y; _y_++)
						{
							world.add_chunk_to_update(0.0f, new Vector3i(x, _y_, z));
							world.add_chunk_to_update(0.0f, new Vector3i(x + 1, _y_, z));
							world.add_chunk_to_update(0.0f, new Vector3i(x - 1, _y_, z));
							world.add_chunk_to_update(0.0f, new Vector3i(x, _y_, z + 1));
							world.add_chunk_to_update(0.0f, new Vector3i(x, _y_, z - 1));
						}
						
						create_block_particles(new Vector3f(X, Y, Z), block);
					}
				}
			}
		}
		
		else
		{
			is_click[0] = false;
		}
		
		if(Mouse.isButtonDown(1) && !is_inventory && !is_pause && !world.is_loading)
		{
			if(!is_inventory && !is_pause)
			{
				Mouse.setGrabbed(true);
			}
			
			if(!is_click[1])
			{
				is_click[1] = true;
				
				Raycast ray = new Raycast(this);
				ray.hit(camera.pos, camera.front, 3.0f, 100);
				
				if(ray.is_hit)
				{
					int X = (int)(Math.round((ray.pos.x + ray.normal.x / 2.0f)));
					int Y = (int)(Math.round((ray.pos.y + ray.normal.y / 2.0f)));
					int Z = (int)(Math.round((ray.pos.z + ray.normal.z / 2.0f)));
					
					if(blocks[blck].is_collision[0])
					{
						AABB b = new AABB(new Vector3f(X, Y - 1.0f / 100000.0f, Z), new Vector3f(1.0f, 1.0f, 1.0f));
						
						if(!AABB.is_collision(aabb, b))
						{
							try
							{
								if(blocks[blck].sound != null)
								{
									Random random = new Random();
									
									Audio audio = new Audio(blocks[blck].sound, false, false, random.nextFloat(0.9f, 1.1f), 1.0f, new Vector3f(X, Y, Z), camera.pos, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), camera.front, camera.up);
									audio.update(camera.pos, new Vector3f(0, 0, 0), camera.front, camera.up);
									audio.play();
									world.window.sounds.add(audio);
								}
							}
							
							catch(Exception e)
							{
								e.printStackTrace();
							}
							
							world.set_block(X, Y, Z, BlockConverter.block_to_int(blocks[blck]));
							
							int x = (int)(Math.floor((float)X / (float)World.chunk_size));
							int z = (int)(Math.floor((float)Z / (float)World.chunk_size));
							
							for(int _y_ = 0; _y_ < world.world_size_y; _y_++)
							{
								world.add_chunk_to_update(0.0f, new Vector3i(x, _y_, z));
								world.add_chunk_to_update(0.0f, new Vector3i(x + 1, _y_, z));
								world.add_chunk_to_update(0.0f, new Vector3i(x - 1, _y_, z));
								world.add_chunk_to_update(0.0f, new Vector3i(x, _y_, z + 1));
								world.add_chunk_to_update(0.0f, new Vector3i(x, _y_, z - 1));
							}
						}
					}
					
					else
					{
						try
						{
							if(blocks[blck].sound != null)
							{
								Random random = new Random();
								
								Audio audio = new Audio(blocks[blck].sound, false, false, random.nextFloat(0.9f, 1.1f), 1.0f, new Vector3f(X, Y, Z), camera.pos, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), camera.front, camera.up);
								audio.update(camera.pos, new Vector3f(0, 0, 0), camera.front, camera.up);
								audio.play();
								world.window.sounds.add(audio);
							}
						}
						
						catch(Exception e)
						{
							e.printStackTrace();
						}
						
						world.set_block(X, Y, Z, BlockConverter.block_to_int(blocks[blck]));
						
						int x = (int)(Math.floor((float)X / (float)World.chunk_size));
						int z = (int)(Math.floor((float)Z / (float)World.chunk_size));
						
						for(int _y_ = 0; _y_ < world.world_size_y; _y_++)
						{
							world.add_chunk_to_update(0.0f, new Vector3i(x, _y_, z));
							world.add_chunk_to_update(0.0f, new Vector3i(x + 1, _y_, z));
							world.add_chunk_to_update(0.0f, new Vector3i(x - 1, _y_, z));
							world.add_chunk_to_update(0.0f, new Vector3i(x, _y_, z + 1));
							world.add_chunk_to_update(0.0f, new Vector3i(x, _y_, z - 1));
						}
					}
				}
			}
		}
		
		else
		{
			is_click[1] = false;
		}
	}
	
	private void check_pos_camera()
	{
		int _x = (int)(Math.round(camera.pos.x));
		int _y = (int)(Math.round(camera.pos.y));
		int _z = (int)(Math.round(camera.pos.z));
		
		int x_ = (int)(Math.round(pos.x));
		int y_ = (int)(Math.round(aabb.min_y() + 0.1f));
		int z_ = (int)(Math.round(pos.z));
		
		int _x_ = (int)(Math.round(pos.x));
		int _y_ = (int)(Math.round(pos.y));
		int _z_ = (int)(Math.round(pos.z));
		
		boolean[] last_underwater = new boolean[3];
		last_underwater[0] = is_underwater[0];
		last_underwater[1] = is_underwater[1];
		last_underwater[2] = is_underwater[2];
		
		AABB cam = new AABB(camera.pos, new Vector3f(0.0f, 0.0f, 0.0f));
		is_underwater[0] = (AABB.is_collision(new AABB(new Vector3f(_x, _y, _z), new Vector3f(1.0f, 1.0f, 1.0f)), cam) && world.get_block(_x, _y, _z) == (byte)6);
		
		AABB water_check1 = new AABB(new Vector3f(pos.x, aabb.min_y() + 0.1f, pos.z), new Vector3f(0.125f, 0.125f, 0.125f));
		is_underwater[1] = (AABB.is_collision(new AABB(new Vector3f(x_, y_, z_), new Vector3f(1.0f, 1.0f, 1.0f)), water_check1) && world.get_block(x_, y_, z_) == (byte)6);
		
		AABB water_check2 = new AABB(pos, new Vector3f(0.0f, 0.0f, 0.0f));
		is_underwater[2] = (AABB.is_collision(new AABB(new Vector3f(_x_, _y_, _z_), new Vector3f(1.0f, 1.0f, 1.0f)), water_check2) && world.get_block(_x_, _y_, _z_) == (byte)6);
		
		if(!last_underwater[1] && is_underwater[1])
		{
			try
			{
				Random random = new Random();
				
				Audio audio = new Audio(ResourcesMC.water_splash, false, false, random.nextFloat(0.85f, 1.15f), 1.0f, new Vector3f(pos.x, aabb.min_y() + 0.1f, pos.z), world.window.player.camera.pos, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), world.window.player.camera.front, world.window.player.camera.up);
				audio.update(world.window.camera.pos, new Vector3f(0, 0, 0), world.window.camera.front, world.window.camera.up);
				audio.play();
				world.window.sounds.add(audio);
			}
			
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		if(!last_underwater[2] && is_underwater[2])
		{
			try
			{
				Random random = new Random();
				
				Audio audio = new Audio(ResourcesMC.water_splash, false, false, random.nextFloat(0.85f, 1.15f), 1.0f, new Vector3f(pos.x, pos.y, pos.z), world.window.player.camera.pos, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), world.window.player.camera.front, world.window.player.camera.up);
				audio.update(world.window.camera.pos, new Vector3f(0, 0, 0), world.window.camera.front, world.window.camera.up);
				audio.play();
				world.window.sounds.add(audio);
			}
			
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		check_ambience(_x, _y, _z);
	}
	
	private void check_ambience(final int x, final int y, final int z)
	{
		if(!world.is_loading && !is_mm)
		{
			if(is_underwater[0])
			{
				audio[0].set_gain(0.0f);
				audio[1].set_gain(100000000000000000000000000000000000000.0f);
				audio[2].set_gain(0.0f);
			}
			
			else
			{
				int size = 4;
				boolean is_ug = true;
				
				for(int X = -size; X <= size; X++)
				{
					for(int Z = -size; Z <= size; Z++)
					{
						if(world.get_light(x + X, y, z + Z))
						{
							is_ug = false;
						}
					}
				}
				
				is_underground = is_ug;
				
				if(is_ug)
				{
					audio[0].set_gain(0.0f);
					audio[1].set_gain(0.0f);
					audio[2].set_gain(100000000000000000000000000000000000000.0f);
				}
				
				else
				{
					audio[0].set_gain(100000000000000000000000000000000000000.0f);
					audio[1].set_gain(0.0f);
					audio[2].set_gain(0.0f);
				}
			}
		}
		
		else
		{
			audio[0].set_gain(0.0f);
			audio[1].set_gain(0.0f);
			audio[2].set_gain(0.0f);
		}
	}
	
	private void update_aabb_camera_pos()
	{
		aabb.pos = pos;
		camera.pos = new Vector3f(pos.x + (right.x * -(float)(Math.sin(walks * camera_speed / 1.5f) / camera_strength)), aabb.max_y() - 0.25f + (float)(Math.sin(walks * camera_speed) / camera_strength), pos.z + (right.z * -(float)(Math.sin(walks * camera_speed / 1.5f) / camera_strength)));
		camera.front = front;
	}
	
	private void check_collision(final Vector3f offset)
	{
		if(offset.x > 0.0f && aabb.max_x() > world.world_size_x * World.chunk_size - 0.5f)
		{
			pos.x = (world.world_size_x * World.chunk_size  - 0.5f) - aabb.size.x / 2.0f;
			aabb.pos = pos;
		}
		
		if(offset.x < 0.0f && aabb.min_x() < -0.5f)
		{
			pos.x = -0.5f + aabb.size.x / 2.0f;
			aabb.pos = pos;
		}
		
		if(offset.z > 0.0f && aabb.max_z() > world.world_size_z * World.chunk_size - 0.5f)
		{
			pos.z = (world.world_size_z * World.chunk_size - 0.5f) - aabb.size.z / 2.0f;
			aabb.pos = pos;
		}
		
		if(offset.z < 0.0f && aabb.min_z() < -0.5f)
		{
			pos.z = -0.5f + aabb.size.z / 2.0f;
			aabb.pos = pos;
		}
		
		for(int y = (int)(aabb.min_y()) - 2; y < (int)(aabb.max_y()) + 2; y++)
		{
			for(int x = (int)(aabb.min_x()) - 2; x < (int)(aabb.max_x()) + 2; x++)
			{
				for(int z = (int)(aabb.min_z()) - 2; z < (int)(aabb.max_z()) + 2; z++)
				{
					if(BlockConverter.int_to_block(world.get_block(x, y, z)).is_collision[0])
					{
						AABB block;
						
						if(pos.y >= world.world_size_y * World.chunk_size + 0.25f)
						{
							block = new AABB(new Vector3f(x, y, z), new Vector3f(1.0f, 1.0f, 1.0f));
						}
						
						else
						{
							block = new AABB(new Vector3f(x, y - 1.0f / 100000.0f, z), new Vector3f(1.0f, 1.0f, 1.0f));
						}
						
						if(AABB.is_collision(aabb, block))
						{
							if(offset.x > 0.0f && aabb.max_x() < block.min_x() + block.size.x / 10.0f)
							{
								is_touch = true;
								pos.x = block.min_x() - aabb.size.x / 2.0f;
								aabb.pos = pos;
								break;
							}
							
							if(offset.x < 0.0f && aabb.min_x() > block.max_x() - block.size.x / 10.0f)
							{
								is_touch = true;
								pos.x = block.max_x() + aabb.size.x / 2.0f;
								aabb.pos = pos;
								break;
							}
							
							if(offset.y > 0.0f && aabb.max_y() < block.min_y() + block.size.y / 10.0f)
							{
								if(velocity > 0.0f)
								{
									velocity = 0.0f;
								}
								
								pos.y = block.min_y() - aabb.size.y / 2.0f;
								aabb.pos = pos;
								break;
							}
							
							if(offset.y < 0.0f && aabb.min_y() > block.max_y() - block.size.y / 10.0f)
							{
								is_ground = true;
								pos.y = block.max_y() + aabb.size.y / 2.0f;
								aabb.pos = pos;
								break;
							}
							
							if(offset.z > 0.0f && aabb.max_z() < block.min_z() + block.size.z / 10.0f)
							{
								is_touch = true;
								pos.z = block.min_z() - aabb.size.z / 2.0f;
								aabb.pos = pos;
								break;
							}
							
							if(offset.z < 0.0f && aabb.min_z() > block.max_z() - block.size.z / 10.0f)
							{
								is_touch = true;
								pos.z = block.max_z() + aabb.size.z / 2.0f;
								aabb.pos = pos;
								break;
							}
						}
					}
				}
			}
		}
	}
}