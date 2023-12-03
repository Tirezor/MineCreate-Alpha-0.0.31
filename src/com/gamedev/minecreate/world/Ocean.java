package com.gamedev.minecreate.world;

import com.gamedev.minecreate.chunk.ChunkRenderer;
import com.gamedev.minecreate.gameengine.camera.Camera;
import com.gamedev.minecreate.gameengine.gameobject.GameObject;
import com.gamedev.minecreate.resources.ResourcesMC;

import java.util.*;

import org.joml.*;

public class Ocean
{
	public GameObject[] ocean;
	
	public ArrayList<Float> vert_data;
	public ArrayList<Float> col_data;
	public ArrayList<Float> t_coord_data;
	public ArrayList<Integer> ind_data;
	
	public float[] vert;
	public float[] col;
	public float[] tex_c;
	public int[] ind;
	
	private int count;
	
	public Ocean(final World world)
	{
		ocean = new GameObject[2];
		
		vert_data = new ArrayList<Float>();
		col_data = new ArrayList<Float>();
		t_coord_data = new ArrayList<Float>();
		ind_data = new ArrayList<Integer>();
		
		count = 0;
		
		vert_add(new Vector3f(-0.5f - 4096.0f, -0.5f, -0.5f - 4096.0f));
		vert_add(new Vector3f(-0.5f - 4096.0f, -0.5f, -0.5f));
		vert_add(new Vector3f(-0.5f, -0.5f, -0.5f));
		vert_add(new Vector3f(-0.5f, -0.5f, -0.5f - 4096.0f));
		
		t_coord_add(new Vector2f(4096.0f, 4096.0f));
		t_coord_add(new Vector2f(4096.0f, 0.0f));
		t_coord_add(new Vector2f(0.0f, 0.0f));
		t_coord_add(new Vector2f(0.0f, 4096.0f));
		
		add_col(new Vector3f(1.0f / ChunkRenderer.shadow, 1.0f / ChunkRenderer.shadow, 1.0f / ChunkRenderer.shadow));
		
		add_ind();
		
		vert_add(new Vector3f((world.world_size_x * World.chunk_size - 0.5f), -0.5f, (world.world_size_z * World.chunk_size - 0.5f)));
		vert_add(new Vector3f((world.world_size_x * World.chunk_size - 0.5f), -0.5f, (world.world_size_z * World.chunk_size - 0.5f) + 4096.0f));
		vert_add(new Vector3f((world.world_size_x * World.chunk_size - 0.5f) + 4096.0f, -0.5f, (world.world_size_z * World.chunk_size - 0.5f) + 4096.0f));
		vert_add(new Vector3f((world.world_size_x * World.chunk_size - 0.5f) + 4096.0f, -0.5f, (world.world_size_z * World.chunk_size - 0.5f)));
		
		t_coord_add(new Vector2f(4096.0f, 4096.0f));
		t_coord_add(new Vector2f(4096.0f, 0.0f));
		t_coord_add(new Vector2f(0.0f, 0.0f));
		t_coord_add(new Vector2f(0.0f, 4096.0f));
		
		add_col(new Vector3f(1.0f / ChunkRenderer.shadow, 1.0f / ChunkRenderer.shadow, 1.0f / ChunkRenderer.shadow));
		
		add_ind();
		
		vert_add(new Vector3f(-0.5f - 4096.0f, -0.5f, (world.world_size_z * World.chunk_size - 0.5f)));
		vert_add(new Vector3f(-0.5f - 4096.0f, -0.5f, (world.world_size_z * World.chunk_size - 0.5f) + 4096.0f));
		vert_add(new Vector3f(-0.5f, -0.5f, (world.world_size_z * World.chunk_size - 0.5f) + 4096.0f));
		vert_add(new Vector3f(-0.5f, -0.5f, (world.world_size_z * World.chunk_size - 0.5f)));
		
		t_coord_add(new Vector2f(4096.0f, 4096.0f));
		t_coord_add(new Vector2f(4096.0f, 0.0f));
		t_coord_add(new Vector2f(0.0f, 0.0f));
		t_coord_add(new Vector2f(0.0f, 4096.0f));
		
		add_col(new Vector3f(1.0f / ChunkRenderer.shadow, 1.0f / ChunkRenderer.shadow, 1.0f / ChunkRenderer.shadow));
		
		add_ind();
		
		vert_add(new Vector3f((world.world_size_x * World.chunk_size - 0.5f), -0.5f, -0.5f - 4096.0f));
		vert_add(new Vector3f((world.world_size_x * World.chunk_size - 0.5f), -0.5f, -0.5f));
		vert_add(new Vector3f((world.world_size_x * World.chunk_size - 0.5f) + 4096.0f, -0.5f, -0.5f));
		vert_add(new Vector3f((world.world_size_x * World.chunk_size - 0.5f) + 4096.0f, -0.5f, -0.5f - 4096.0f));
		
		t_coord_add(new Vector2f(4096.0f, 4096.0f));
		t_coord_add(new Vector2f(4096.0f, 0.0f));
		t_coord_add(new Vector2f(0.0f, 0.0f));
		t_coord_add(new Vector2f(0.0f, 4096.0f));
		
		add_col(new Vector3f(1.0f / ChunkRenderer.shadow, 1.0f / ChunkRenderer.shadow, 1.0f / ChunkRenderer.shadow));
		
		add_ind();
		
		vert_add(new Vector3f(-0.5f - 4096.0f, -0.5f, -0.5f));
		vert_add(new Vector3f(-0.5f - 4096.0f, -0.5f, (world.world_size_z * World.chunk_size - 0.5f)));
		vert_add(new Vector3f(-0.5f, -0.5f, (world.world_size_z * World.chunk_size - 0.5f)));
		vert_add(new Vector3f(-0.5f, -0.5f, -0.5f));
		
		t_coord_add(new Vector2f(4096.0f, world.world_size_z * World.chunk_size));
		t_coord_add(new Vector2f(4096.0f, 0.0f));
		t_coord_add(new Vector2f(0.0f, 0.0f));
		t_coord_add(new Vector2f(0.0f, world.world_size_z * World.chunk_size));
		
		add_col(new Vector3f(1.0f / ChunkRenderer.shadow, 1.0f / ChunkRenderer.shadow, 1.0f / ChunkRenderer.shadow));
		
		add_ind();
		
		vert_add(new Vector3f(-0.5f, -0.5f, -0.5f - 4096.0f));
		vert_add(new Vector3f(-0.5f, -0.5f, -0.5f));
		vert_add(new Vector3f((world.world_size_x * World.chunk_size - 0.5f), -0.5f, -0.5f));
		vert_add(new Vector3f((world.world_size_x * World.chunk_size - 0.5f), -0.5f, -0.5f - 4096.0f));
		
		t_coord_add(new Vector2f(world.world_size_x * World.chunk_size, 4096.0f));
		t_coord_add(new Vector2f(world.world_size_x * World.chunk_size, 0.0f));
		t_coord_add(new Vector2f(0.0f, 0.0f));
		t_coord_add(new Vector2f(0.0f, 4096.0f));
		
		add_col(new Vector3f(1.0f / ChunkRenderer.shadow, 1.0f / ChunkRenderer.shadow, 1.0f / ChunkRenderer.shadow));
		
		add_ind();
		
		vert_add(new Vector3f((world.world_size_x * World.chunk_size - 0.5f), -0.5f, -0.5f));
		vert_add(new Vector3f((world.world_size_x * World.chunk_size - 0.5f), -0.5f, (world.world_size_z * World.chunk_size - 0.5f)));
		vert_add(new Vector3f((world.world_size_x * World.chunk_size - 0.5f) + 4096.0f, -0.5f, (world.world_size_z * World.chunk_size - 0.5f)));
		vert_add(new Vector3f((world.world_size_x * World.chunk_size - 0.5f) + 4096.0f, -0.5f, -0.5f));
		
		t_coord_add(new Vector2f(4096.0f, world.world_size_z * World.chunk_size));
		t_coord_add(new Vector2f(4096.0f, 0.0f));
		t_coord_add(new Vector2f(0.0f, 0.0f));
		t_coord_add(new Vector2f(0.0f, world.world_size_z * World.chunk_size));
		
		add_col(new Vector3f(1.0f / ChunkRenderer.shadow, 1.0f / ChunkRenderer.shadow, 1.0f / ChunkRenderer.shadow));
		
		add_ind();
		
		vert_add(new Vector3f(-0.5f, -0.5f, (world.world_size_z * World.chunk_size - 0.5f)));
		vert_add(new Vector3f(-0.5f, -0.5f, (world.world_size_z * World.chunk_size - 0.5f) + 4096.0f));
		vert_add(new Vector3f((world.world_size_x * World.chunk_size - 0.5f), -0.5f, (world.world_size_z * World.chunk_size - 0.5f) + 4096.0f));
		vert_add(new Vector3f((world.world_size_x * World.chunk_size - 0.5f), -0.5f, (world.world_size_z * World.chunk_size - 0.5f)));
		
		t_coord_add(new Vector2f(world.world_size_x * World.chunk_size, 4096.0f));
		t_coord_add(new Vector2f(world.world_size_x * World.chunk_size, 0.0f));
		t_coord_add(new Vector2f(0.0f, 0.0f));
		t_coord_add(new Vector2f(0.0f, 4096.0f));
		
		add_col(new Vector3f(1.0f / ChunkRenderer.shadow, 1.0f / ChunkRenderer.shadow, 1.0f / ChunkRenderer.shadow));
		
		add_ind();
		
		convert();
		
		ocean[0] = new GameObject(new Vector3f(0, 1, 0), new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(1.0f, 1.0f, 1.0f), ResourcesMC.chunk_shader, ResourcesMC.ocean0, vert, col, tex_c, ind);
		
		vert_data = new ArrayList<Float>();
		col_data = new ArrayList<Float>();
		t_coord_data = new ArrayList<Float>();
		ind_data = new ArrayList<Integer>();
		
		count = 0;
		
		vert_add(new Vector3f(-0.5f - 4096.0f, -0.5f, -0.5f - 4096.0f));
		vert_add(new Vector3f(-0.5f - 4096.0f, -0.5f, -0.5f));
		vert_add(new Vector3f(-0.5f, -0.5f, -0.5f));
		vert_add(new Vector3f(-0.5f, -0.5f, -0.5f - 4096.0f));
		
		t_coord_add(new Vector2f(4096.0f, 4096.0f));
		t_coord_add(new Vector2f(4096.0f, 0.0f));
		t_coord_add(new Vector2f(0.0f, 0.0f));
		t_coord_add(new Vector2f(0.0f, 4096.0f));
		
		add_col(new Vector3f(1.0f, 1.0f, 1.0f));
		
		add_ind();
		
		vert_add(new Vector3f((world.world_size_x * World.chunk_size - 0.5f), -0.5f, (world.world_size_z * World.chunk_size - 0.5f)));
		vert_add(new Vector3f((world.world_size_x * World.chunk_size - 0.5f), -0.5f, (world.world_size_z * World.chunk_size - 0.5f) + 4096.0f));
		vert_add(new Vector3f((world.world_size_x * World.chunk_size - 0.5f) + 4096.0f, -0.5f, (world.world_size_z * World.chunk_size - 0.5f) + 4096.0f));
		vert_add(new Vector3f((world.world_size_x * World.chunk_size - 0.5f) + 4096.0f, -0.5f, (world.world_size_z * World.chunk_size - 0.5f)));
		
		t_coord_add(new Vector2f(4096.0f, 4096.0f));
		t_coord_add(new Vector2f(4096.0f, 0.0f));
		t_coord_add(new Vector2f(0.0f, 0.0f));
		t_coord_add(new Vector2f(0.0f, 4096.0f));
		
		add_col(new Vector3f(1.0f, 1.0f, 1.0f));
		
		add_ind();
		
		vert_add(new Vector3f(-0.5f - 4096.0f, -0.5f, (world.world_size_z * World.chunk_size - 0.5f)));
		vert_add(new Vector3f(-0.5f - 4096.0f, -0.5f, (world.world_size_z * World.chunk_size - 0.5f) + 4096.0f));
		vert_add(new Vector3f(-0.5f, -0.5f, (world.world_size_z * World.chunk_size - 0.5f) + 4096.0f));
		vert_add(new Vector3f(-0.5f, -0.5f, (world.world_size_z * World.chunk_size - 0.5f)));
		
		t_coord_add(new Vector2f(4096.0f, 4096.0f));
		t_coord_add(new Vector2f(4096.0f, 0.0f));
		t_coord_add(new Vector2f(0.0f, 0.0f));
		t_coord_add(new Vector2f(0.0f, 4096.0f));
		
		add_col(new Vector3f(1.0f, 1.0f, 1.0f));
		
		add_ind();
		
		vert_add(new Vector3f((world.world_size_x * World.chunk_size - 0.5f), -0.5f, -0.5f - 4096.0f));
		vert_add(new Vector3f((world.world_size_x * World.chunk_size - 0.5f), -0.5f, -0.5f));
		vert_add(new Vector3f((world.world_size_x * World.chunk_size - 0.5f) + 4096.0f, -0.5f, -0.5f));
		vert_add(new Vector3f((world.world_size_x * World.chunk_size - 0.5f) + 4096.0f, -0.5f, -0.5f - 4096.0f));
		
		t_coord_add(new Vector2f(4096.0f, 4096.0f));
		t_coord_add(new Vector2f(4096.0f, 0.0f));
		t_coord_add(new Vector2f(0.0f, 0.0f));
		t_coord_add(new Vector2f(0.0f, 4096.0f));
		
		add_col(new Vector3f(1.0f, 1.0f, 1.0f));
		
		add_ind();
		
		vert_add(new Vector3f(-0.5f - 4096.0f, -0.5f, -0.5f));
		vert_add(new Vector3f(-0.5f - 4096.0f, -0.5f, (world.world_size_z * World.chunk_size - 0.5f)));
		vert_add(new Vector3f(-0.5f, -0.5f, (world.world_size_z * World.chunk_size - 0.5f)));
		vert_add(new Vector3f(-0.5f, -0.5f, -0.5f));
		
		t_coord_add(new Vector2f(4096.0f, world.world_size_z * World.chunk_size));
		t_coord_add(new Vector2f(4096.0f, 0.0f));
		t_coord_add(new Vector2f(0.0f, 0.0f));
		t_coord_add(new Vector2f(0.0f, world.world_size_z * World.chunk_size));
		
		add_col(new Vector3f(1.0f, 1.0f, 1.0f));
		
		add_ind();
		
		vert_add(new Vector3f(-0.5f, -0.5f, -0.5f - 4096.0f));
		vert_add(new Vector3f(-0.5f, -0.5f, -0.5f));
		vert_add(new Vector3f((world.world_size_x * World.chunk_size - 0.5f), -0.5f, -0.5f));
		vert_add(new Vector3f((world.world_size_x * World.chunk_size - 0.5f), -0.5f, -0.5f - 4096.0f));
		
		t_coord_add(new Vector2f(world.world_size_x * World.chunk_size, 4096.0f));
		t_coord_add(new Vector2f(world.world_size_x * World.chunk_size, 0.0f));
		t_coord_add(new Vector2f(0.0f, 0.0f));
		t_coord_add(new Vector2f(0.0f, 4096.0f));
		
		add_col(new Vector3f(1.0f, 1.0f, 1.0f));
		
		add_ind();
		
		vert_add(new Vector3f((world.world_size_x * World.chunk_size - 0.5f), -0.5f, -0.5f));
		vert_add(new Vector3f((world.world_size_x * World.chunk_size - 0.5f), -0.5f, (world.world_size_z * World.chunk_size - 0.5f)));
		vert_add(new Vector3f((world.world_size_x * World.chunk_size - 0.5f) + 4096.0f, -0.5f, (world.world_size_z * World.chunk_size - 0.5f)));
		vert_add(new Vector3f((world.world_size_x * World.chunk_size - 0.5f) + 4096.0f, -0.5f, -0.5f));
		
		t_coord_add(new Vector2f(4096.0f, world.world_size_z * World.chunk_size));
		t_coord_add(new Vector2f(4096.0f, 0.0f));
		t_coord_add(new Vector2f(0.0f, 0.0f));
		t_coord_add(new Vector2f(0.0f, world.world_size_z * World.chunk_size));
		
		add_col(new Vector3f(1.0f, 1.0f, 1.0f));
		
		add_ind();
		
		vert_add(new Vector3f(-0.5f, -0.5f, (world.world_size_z * World.chunk_size - 0.5f)));
		vert_add(new Vector3f(-0.5f, -0.5f, (world.world_size_z * World.chunk_size - 0.5f) + 4096.0f));
		vert_add(new Vector3f((world.world_size_x * World.chunk_size - 0.5f), -0.5f, (world.world_size_z * World.chunk_size - 0.5f) + 4096.0f));
		vert_add(new Vector3f((world.world_size_x * World.chunk_size - 0.5f), -0.5f, (world.world_size_z * World.chunk_size - 0.5f)));
		
		t_coord_add(new Vector2f(world.world_size_x * World.chunk_size, 4096.0f));
		t_coord_add(new Vector2f(world.world_size_x * World.chunk_size, 0.0f));
		t_coord_add(new Vector2f(0.0f, 0.0f));
		t_coord_add(new Vector2f(0.0f, 4096.0f));
		
		add_col(new Vector3f(1.0f, 1.0f, 1.0f));
		
		add_ind();
		
		convert();
		
		ocean[1] = new GameObject(new Vector3f(0, world.water_height + 1, 0), new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(1.0f, 1.0f, 1.0f), ResourcesMC.chunk_shader, ResourcesMC.ocean1, vert, col, tex_c, ind);
	}
	
	public void update(final Camera camera)
	{
		ocean[0].update(camera, true);
		
		ocean[1].update(camera, true);
	}
	
	public void destroy()
	{
		ocean[0].destroy();
		ocean[1].destroy();
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