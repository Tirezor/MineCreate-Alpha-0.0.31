package com.gamedev.minecreate.world;

import com.gamedev.minecreate.gameengine.camera.Camera;
import com.gamedev.minecreate.gameengine.gameobject.GameObject;
import com.gamedev.minecreate.gameengine.time.Timer;
import com.gamedev.minecreate.resources.ResourcesMC;

import java.util.Random;
import java.util.*;

import org.joml.*;

import static org.lwjgl.opengl.GL20.*;

public class Sky
{
	public GameObject sky;
	public GameObject clouds;
	
	public ArrayList<Float> vert_data;
	public ArrayList<Float> col_data;
	public ArrayList<Float> t_coord_data;
	public ArrayList<Integer> ind_data;
	
	public float[] vert;
	public float[] col;
	public float[] tex_c;
	public int[] ind;
	
	private int count;
	
	private float offset;
	
	public Sky(final World world)
	{
		Random random = new Random();
		offset = random.nextFloat(0.0f, 1.0f);
		
		vert_data = new ArrayList<Float>();
		col_data = new ArrayList<Float>();
		t_coord_data = new ArrayList<Float>();
		ind_data = new ArrayList<Integer>();
		
		count = 0;
		
		vert_add(new Vector3f(-0.5f - 4096.0f, 0.0f, -0.5f - 4096.0f));
		vert_add(new Vector3f((world.world_size_x * World.chunk_size - 0.5f) + 4096.0f, 0.0f, -0.5f - 4096.0f));
		vert_add(new Vector3f((world.world_size_x * World.chunk_size - 0.5f) + 4096.0f, 0.0f, (world.world_size_z * World.chunk_size - 0.5f) + 4096.0f));
		vert_add(new Vector3f(-0.5f - 4096.0f, 0.0f, (world.world_size_z * World.chunk_size - 0.5f) + 4096.0f));
		
		t_coord_add(new Vector2f(1.0f, 1.0f));
		t_coord_add(new Vector2f(1.0f, 0.0f));
		t_coord_add(new Vector2f(0.0f, 0.0f));
		t_coord_add(new Vector2f(0.0f, 1.0f));
		
		add_col(new Vector3f(0.5f, 1.0f, 1.0f));
		
		add_ind();
		
		convert();
		
		sky = new GameObject(new Vector3f(0, 69, 0), new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(1.0f, 1.0f, 1.0f), ResourcesMC.sky_shader, null, vert, col, tex_c, ind);
		
		vert_data = new ArrayList<Float>();
		col_data = new ArrayList<Float>();
		t_coord_data = new ArrayList<Float>();
		ind_data = new ArrayList<Integer>();
		
		count = 0;
		
		vert_add(new Vector3f(-0.5f - 4096.0f, 0.0f, -0.5f - 4096.0f));
		vert_add(new Vector3f((world.world_size_x * World.chunk_size - 0.5f) + 4096.0f, 0.0f, -0.5f - 4096.0f));
		vert_add(new Vector3f((world.world_size_x * World.chunk_size - 0.5f) + 4096.0f, 0.0f, (world.world_size_z * World.chunk_size - 0.5f) + 4096.0f));
		vert_add(new Vector3f(-0.5f - 4096.0f, 0.0f, (world.world_size_z * World.chunk_size - 0.5f) + 4096.0f));
		
		t_coord_add(new Vector2f(1.0f, 1.0f));
		t_coord_add(new Vector2f(1.0f, 0.0f));
		t_coord_add(new Vector2f(0.0f, 0.0f));
		t_coord_add(new Vector2f(0.0f, 1.0f));
		
		add_col(new Vector3f(1.0f, 1.0f, 1.0f));
		
		add_ind();
		
		convert();
		
		clouds = new GameObject(new Vector3f(0, 68, 0), new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(1.0f, 1.0f, 1.0f), ResourcesMC.clouds_shader, ResourcesMC.clouds, vert, col, tex_c, ind);
	}
	
	public void update(final Camera camera)
	{
		offset += 0.0005f * Timer.delta_time;
		
		if(offset >= 1.0f)
		{
			offset -= (int)offset;
		}
		
		glUseProgram(clouds.mesh.shader.program);
		glUniform1f(glGetUniformLocation(clouds.mesh.shader.program, "offset"), offset);
		glUseProgram(0);
		
		sky.update(camera, true);
		clouds.update(camera, true);
	}
	
	public void destroy()
	{
		sky.destroy();
		clouds.destroy();
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
}