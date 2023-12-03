package com.gamedev.minecreate.player;

import com.gamedev.minecreate.gameengine.camera.Camera;
import com.gamedev.minecreate.gameengine.gameobject.GameObject;
import com.gamedev.minecreate.resources.ResourcesMC;

import java.util.*;

import org.joml.*;

public class BlockSelector
{
	public GameObject obj;
	
	public Vector3f pos;
	
	public ArrayList<Float> vert_data;
	public ArrayList<Float> col_data;
	public ArrayList<Float> t_coord_data;
	public ArrayList<Integer> ind_data;
	
	public float[] vert;
	public float[] col;
	public float[] tex_c;
	public int[] ind;
	
	public boolean is_render;
	
	private int count;
	
	public BlockSelector()
	{
		vert_data = new ArrayList<Float>();
		col_data = new ArrayList<Float>();
		t_coord_data = new ArrayList<Float>();
		ind_data = new ArrayList<Integer>();
		
		pos = new Vector3f(0, 0, 0);
		
		count = 0;
		
		vert_add(new Vector3f(-0.5f, 0.5f, -0.5f));
		vert_add(new Vector3f(-0.5f, 0.5f, 0.5f));
		vert_add(new Vector3f(0.5f, 0.5f, 0.5f));
		vert_add(new Vector3f(0.5f, 0.5f, -0.5f));
		
		t_coord_add(new Vector2f(1.0f, 1.0f));
		t_coord_add(new Vector2f(1.0f, 0.0f));
		t_coord_add(new Vector2f(0.0f, 0.0f));
		t_coord_add(new Vector2f(0.0f, 1.0f));
		
		add_col(new Vector3f(0.0f, 0.0f, 0.0f));
		
		add_ind();
		
		vert_add(new Vector3f(-0.5f, 0.5f, -0.5f));
		vert_add(new Vector3f(0.5f, 0.5f, -0.5f));
		vert_add(new Vector3f(0.5f, 0.5f, 0.5f));
		vert_add(new Vector3f(-0.5f, 0.5f, 0.5f));
		
		t_coord_add(new Vector2f(1.0f, 0.0f));
		t_coord_add(new Vector2f(0.0f, 0.0f));
		t_coord_add(new Vector2f(0.0f, 1.0f));
		t_coord_add(new Vector2f(1.0f, 1.0f));
		
		add_col(new Vector3f(0.0f, 0.0f, 0.0f));
		
		add_ind();
		
		vert_add(new Vector3f(-0.5f, -0.5f, -0.5f));
		vert_add(new Vector3f(0.5f, -0.5f, -0.5f));
		vert_add(new Vector3f(0.5f, -0.5f, 0.5f));
		vert_add(new Vector3f(-0.5f, -0.5f, 0.5f));
		
		t_coord_add(new Vector2f(1.0f, 0.0f));
		t_coord_add(new Vector2f(0.0f, 0.0f));
		t_coord_add(new Vector2f(0.0f, 1.0f));
		t_coord_add(new Vector2f(1.0f, 1.0f));
		
		add_col(new Vector3f(0.0f, 0.0f, 0.0f));
		
		add_ind();
		
		vert_add(new Vector3f(-0.5f, -0.5f, -0.5f));
		vert_add(new Vector3f(-0.5f, -0.5f, 0.5f));
		vert_add(new Vector3f(0.5f, -0.5f, 0.5f));
		vert_add(new Vector3f(0.5f, -0.5f, -0.5f));
		
		t_coord_add(new Vector2f(1.0f, 1.0f));
		t_coord_add(new Vector2f(1.0f, 0.0f));
		t_coord_add(new Vector2f(0.0f, 0.0f));
		t_coord_add(new Vector2f(0.0f, 1.0f));
		
		add_col(new Vector3f(0.0f, 0.0f, 0.0f));
		
		add_ind();
		
		vert_add(new Vector3f(-0.5f, -0.5f, 0.5f));
		vert_add(new Vector3f(0.5f, -0.5f, 0.5f));
		vert_add(new Vector3f(0.5f, 0.5f, 0.5f));
		vert_add(new Vector3f(-0.5f, 0.5f, 0.5f));
		
		t_coord_add(new Vector2f(0.0f, 1.0f));
		t_coord_add(new Vector2f(1.0f, 1.0f));
		t_coord_add(new Vector2f(1.0f, 0.0f));
		t_coord_add(new Vector2f(0.0f, 0.0f));
		
		add_col(new Vector3f(0.0f, 0.0f, 0.0f));
		
		add_ind();
		
		vert_add(new Vector3f(-0.5f, -0.5f, 0.5f));
		vert_add(new Vector3f(-0.5f, 0.5f, 0.5f));
		vert_add(new Vector3f(0.5f, 0.5f, 0.5f));
		vert_add(new Vector3f(0.5f, -0.5f, 0.5f));
		
		t_coord_add(new Vector2f(1.0f, 1.0f));
		t_coord_add(new Vector2f(1.0f, 0.0f));
		t_coord_add(new Vector2f(0.0f, 0.0f));
		t_coord_add(new Vector2f(0.0f, 1.0f));
		
		add_col(new Vector3f(0.0f, 0.0f, 0.0f));
		
		add_ind();
		
		vert_add(new Vector3f(-0.5f, -0.5f, -0.5f));
		vert_add(new Vector3f(-0.5f, 0.5f, -0.5f));
		vert_add(new Vector3f(0.5f, 0.5f, -0.5f));
		vert_add(new Vector3f(0.5f, -0.5f, -0.5f));
		
		t_coord_add(new Vector2f(1.0f, 1.0f));
		t_coord_add(new Vector2f(1.0f, 0.0f));
		t_coord_add(new Vector2f(0.0f, 0.0f));
		t_coord_add(new Vector2f(0.0f, 1.0f));
		
		add_col(new Vector3f(0.0f, 0.0f, 0.0f));
		
		add_ind();
		
		vert_add(new Vector3f(-0.5f, -0.5f, -0.5f));
		vert_add(new Vector3f(0.5f, -0.5f, -0.5f));
		vert_add(new Vector3f(0.5f, 0.5f, -0.5f));
		vert_add(new Vector3f(-0.5f, 0.5f, -0.5f));
		
		t_coord_add(new Vector2f(0.0f, 1.0f));
		t_coord_add(new Vector2f(1.0f, 1.0f));
		t_coord_add(new Vector2f(1.0f, 0.0f));
		t_coord_add(new Vector2f(0.0f, 0.0f));
		
		add_col(new Vector3f(0.0f, 0.0f, 0.0f));
		
		add_ind();
		
		vert_add(new Vector3f(0.5f, -0.5f, -0.5f));
		vert_add(new Vector3f(0.5f, 0.5f, -0.5f));
		vert_add(new Vector3f(0.5f, 0.5f, 0.5f));
		vert_add(new Vector3f(0.5f, -0.5f, 0.5f));
		
		t_coord_add(new Vector2f(1.0f, 1.0f));
		t_coord_add(new Vector2f(1.0f, 0.0f));
		t_coord_add(new Vector2f(0.0f, 0.0f));
		t_coord_add(new Vector2f(0.0f, 1.0f));
		
		add_col(new Vector3f(0.0f, 0.0f, 0.0f));
		
		add_ind();
		
		vert_add(new Vector3f(0.5f, -0.5f, -0.5f));
		vert_add(new Vector3f(0.5f, -0.5f, 0.5f));
		vert_add(new Vector3f(0.5f, 0.5f, 0.5f));
		vert_add(new Vector3f(0.5f, 0.5f, -0.5f));
		
		t_coord_add(new Vector2f(0.0f, 1.0f));
		t_coord_add(new Vector2f(1.0f, 1.0f));
		t_coord_add(new Vector2f(1.0f, 0.0f));
		t_coord_add(new Vector2f(0.0f, 0.0f));
		
		add_col(new Vector3f(0.0f, 0.0f, 0.0f));
		
		add_ind();
		
		vert_add(new Vector3f(-0.5f, -0.5f, -0.5f));
		vert_add(new Vector3f(-0.5f, -0.5f, 0.5f));
		vert_add(new Vector3f(-0.5f, 0.5f, 0.5f));
		vert_add(new Vector3f(-0.5f, 0.5f, -0.5f));
		
		t_coord_add(new Vector2f(0.0f, 1.0f));
		t_coord_add(new Vector2f(1.0f, 1.0f));
		t_coord_add(new Vector2f(1.0f, 0.0f));
		t_coord_add(new Vector2f(0.0f, 0.0f));
		
		add_col(new Vector3f(0.0f, 0.0f, 0.0f));
		
		add_ind();
		
		vert_add(new Vector3f(-0.5f, -0.5f, -0.5f));
		vert_add(new Vector3f(-0.5f, 0.5f, -0.5f));
		vert_add(new Vector3f(-0.5f, 0.5f, 0.5f));
		vert_add(new Vector3f(-0.5f, -0.5f, 0.5f));
		
		t_coord_add(new Vector2f(1.0f, 1.0f));
		t_coord_add(new Vector2f(1.0f, 0.0f));
		t_coord_add(new Vector2f(0.0f, 0.0f));
		t_coord_add(new Vector2f(0.0f, 1.0f));
		
		add_col(new Vector3f(0.0f, 0.0f, 0.0f));
		
		add_ind();
		
		convert();
		
		obj = new GameObject(pos, new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(1.01f, 1.01f, 1.01f), ResourcesMC.selector_shader, ResourcesMC.selector, vert, col, tex_c, ind);
	}
	
	public void update(final Camera camera)
	{
		obj.pos = pos;
		
		if(is_render)
		{
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
}