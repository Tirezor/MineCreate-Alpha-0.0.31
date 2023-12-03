package com.gamedev.minecreate.ui;

import com.gamedev.minecreate.gameengine.camera.Camera;
import com.gamedev.minecreate.gameengine.gameobject.GameObject;
import com.gamedev.minecreate.gameengine.textures.Texture;
import com.gamedev.minecreate.resources.ResourcesMC;

import java.util.ArrayList;

import org.joml.*;

import static org.lwjgl.opengl.GL20.*;

public class ImageUI
{
	public GameObject obj;
	
	public ArrayList<Float> vert_data;
	public ArrayList<Float> col_data;
	public ArrayList<Float> t_coord_data;
	public ArrayList<Integer> ind_data;
	
	public float[] vert;
	public float[] col;
	public float[] tex_c;
	public int[] ind;
	
	public boolean enabled;
	
	public int count;
	
	public boolean with_texture;
	
	public float alpha;
	
	public ImageUI(final Texture texture)
	{
		vert_data = new ArrayList<Float>();
		col_data = new ArrayList<Float>();
		t_coord_data = new ArrayList<Float>();
		ind_data = new ArrayList<Integer>();
		
		count = 0;
		
		vert_add(new Vector3f(0, 0, 0));
		vert_add(new Vector3f(0, 1, 0));
		vert_add(new Vector3f(1, 1, 0));
		vert_add(new Vector3f(1, 0, 0));
		
		t_coord_add(new Vector2f(0, 0));
		t_coord_add(new Vector2f(0, 1));
		t_coord_add(new Vector2f(1, 1));
		t_coord_add(new Vector2f(1, 0));
		
		add_col(new Vector4f(0, 0, 0, 1));
		
		add_ind();
		
		convert();
		
		enabled = true;
		
		with_texture = (texture != null);
		
		if(with_texture)
		{
			obj = new GameObject(new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(1.0f, 1.0f, 1.0f), ResourcesMC.ui_shader, texture, vert, col, tex_c, ind);
		}
		
		else
		{
			obj = new GameObject(new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(1.0f, 1.0f, 1.0f), ResourcesMC.ui_shader, null, vert, col, tex_c, ind);
		}
	}
	
	public void update(final Camera camera)
	{
		glUseProgram(obj.mesh.shader.program);
		glUniform1f(glGetUniformLocation(obj.mesh.shader.program, "alpha"), alpha);
		glUseProgram(0);
		
		if(with_texture)
		{
			glUseProgram(obj.mesh.shader.program);
			glUniform1f(glGetUniformLocation(obj.mesh.shader.program, "with_texture"), 1);
			glUseProgram(0);
		}
		
		else
		{
			glUseProgram(obj.mesh.shader.program);
			glUniform1f(glGetUniformLocation(obj.mesh.shader.program, "with_texture"), 0);
			glUseProgram(0);
		}
		
		if(enabled)
		{
			obj.update(camera, true);
		}
	}
	
	public void destroy()
	{
		obj.destroy();
	}
	
	public void convert()
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
	
	public void add_col(final Vector4f vec4)
	{
		col_add(new Vector3f(vec4.x, vec4.y, vec4.z));
		col_add(new Vector3f(vec4.x, vec4.y, vec4.z));
		col_add(new Vector3f(vec4.x, vec4.y, vec4.z));
		col_add(new Vector3f(vec4.x, vec4.y, vec4.z));
		alpha = vec4.w;
	}
	
	public void add_ind()
	{
		ind_add(0 + 4 * count, 1 + 4 * count, 2 + 4 * count);
		ind_add(0 + 4 * count, 2 + 4 * count, 3 + 4 * count);
		count++;
	}
	
	public void vert_add(final Vector3f vec3)
	{
		vert_data.add(vec3.x);
		vert_data.add(vec3.y);
		vert_data.add(vec3.z);
	}
	
	public void col_add(final Vector3f vec3)
	{
		col_data.add(vec3.x);
		col_data.add(vec3.y);
		col_data.add(vec3.z);
	}
	
	public void t_coord_add(final Vector2f vec2)
	{
		t_coord_data.add(vec2.x);
		t_coord_data.add(vec2.y);
	}
	
	public void ind_add(final int i0, final int i1, final int i2)
	{
		ind_data.add((i0));
		ind_data.add((i1));
		ind_data.add((i2));
	}
}