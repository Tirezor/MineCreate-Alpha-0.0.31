package com.gamedev.minecreate.particle;

import com.gamedev.minecreate.block.BlockConverter;
import com.gamedev.minecreate.chunk.ChunkRenderer;
import com.gamedev.minecreate.gameengine.camera.Camera;
import com.gamedev.minecreate.gameengine.gameobject.GameObject;
import com.gamedev.minecreate.gameengine.time.Timer;
import com.gamedev.minecreate.physics.AABB;
import com.gamedev.minecreate.player.Player;
import com.gamedev.minecreate.resources.ResourcesMC;
import com.gamedev.minecreate.world.World;

import java.lang.Math;
import java.util.*;

import org.joml.*;

import static org.lwjgl.opengl.GL20.*;

public class Particle
{
	public GameObject obj;
	
	public Vector3f pos;
	public Vector3f front;
	
	public float speed;
	
	public ArrayList<Float> vert_data;
	public ArrayList<Float> col_data;
	public ArrayList<Float> t_coord_data;
	public ArrayList<Integer> ind_data;
	
	public float[] vert;
	public float[] col;
	public float[] tex_c;
	public int[] ind;
	
	public World world;
	
	public AABB aabb;
	
	public float size;
	
	public float timer;
	public float time;
	
	private float velocity;
	
	private boolean is_touch;
	
	private Vector2f[] tex_coords;
	
	private int count;
	
	private boolean is_delete;
	
	public Particle(final World world, final Vector3f pos, final Vector3f front, final float speed, final float time, final float timer, final Vector2f[] tex_coords, final float size)
	{
		is_delete = false;
		
		this.world = world;
		
		this.pos = pos;
		this.front = front;
		
		this.speed = speed;
		
		this.time = time;
		this.timer = timer;
		
		this.tex_coords = tex_coords;
		
		this.size = size;
	}
	
	public void create()
	{
		vert_data = new ArrayList<Float>();
		col_data = new ArrayList<Float>();
		t_coord_data = new ArrayList<Float>();
		ind_data = new ArrayList<Integer>();
		
		velocity = 0.0f;
		
		aabb = new AABB(pos, new Vector3f(size, size, size));
		
		count = 0;
		
		vert_add(new Vector3f(0.0f, -size / 2.0f, -size / 2.0f));
		vert_add(new Vector3f(0.0f, -size / 2.0f, size / 2.0f));
		vert_add(new Vector3f(0.0f, size / 2.0f, size / 2.0f));
		vert_add(new Vector3f(0.0f, size / 2.0f, -size / 2.0f));
		
		t_coord_add(tex_coords[0]);
		t_coord_add(tex_coords[1]);
		t_coord_add(tex_coords[2]);
		t_coord_add(tex_coords[3]);
		
		add_col(new Vector3f(1.0f, 1.0f, 1.0f));
		
		add_ind();
		
		convert();
		
		obj = new GameObject(pos, new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(1.0f, 1.0f, 1.0f), ResourcesMC.particle_shader, ResourcesMC.blocks_atlas, vert, col, tex_c, ind);
	}
	
	public void update(final Camera camera, final Player player)
	{
		time -= Timer.delta_time;
		
		if(time <= 0.0f)
		{
			if(!is_delete)
			{
				create();
				is_delete = true;
			}
			
			int x = (int)(Math.round(pos.x));
			int y = (int)(Math.round(pos.y));
			int z = (int)(Math.round(pos.z));
			
			if(world.get_light(x, y, z))
			{
				glUseProgram(obj.mesh.shader.program);
				glUniform1f(glGetUniformLocation(obj.mesh.shader.program, "l"), 1.0f);
				glUseProgram(0);
			}
			
			else
			{
				glUseProgram(obj.mesh.shader.program);
				glUniform1f(glGetUniformLocation(obj.mesh.shader.program, "l"), 1.0f / ChunkRenderer.shadow);
				glUseProgram(0);
			}
			
			timer -= Timer.delta_time;
			
			velocity -= 1.75f * Timer.delta_time;
			
			Vector3f up = new Vector3f(0, 1, 0);
			Vector3f offset = new Vector3f().zero();
			Vector3f vel = new Vector3f();
			Vector3f result = new Vector3f();
			Vector3f move = new Vector3f();
			
			if(speed > 0.0f)
			{
				speed -= 7.0f * Timer.delta_time;
			}
			
			if(speed < 0.0f)
			{
				speed = 0.0f;
			}
			
			if(!is_touch)
			{
				front.mul(speed * Timer.delta_time, move);
				offset.add(move);
			}
			
			up.mul(-9.81f * Timer.delta_time * -velocity, vel);
			offset.add(vel);
			offset.div(15.0f, result);
			
			for(int i = 0; i < 15; i++)
			{
				pos.add(new Vector3f(result.x, 0, 0));
				aabb.pos = pos;
				check_collision(new Vector3f(result.x, 0, 0), world);
				aabb.pos = pos;
				
				pos.add(new Vector3f(0, result.y, 0));
				aabb.pos = pos;
				check_collision(new Vector3f(0, result.y, 0), world);
				aabb.pos = pos;
				
				pos.add(new Vector3f(0, 0, result.z));
				aabb.pos = pos;
				check_collision(new Vector3f(0, 0, result.z), world);
				aabb.pos = pos;
			}
			
			obj.pos = pos;
			aabb.pos = pos;

			obj.rot = new Vector3f(0, -player.dir_x, player.dir_y);
			
			obj.update(camera, true);
		}
	}
	
	public void destroy()
	{
		obj.destroy();
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
		vert_data.add(vec3.x);
		vert_data.add(vec3.y);
		vert_data.add(vec3.z);
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
	
	private void check_collision(final Vector3f offset, final World world)
	{
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
								is_touch = true;
								pos.y = block.min_y() - aabb.size.y / 2.0f;
								aabb.pos = pos;
								break;
							}
							
							if(offset.y < 0.0f && aabb.min_y() > block.max_y() - block.size.y / 10.0f)
							{
								is_touch = true;
								velocity = 0.0f;
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