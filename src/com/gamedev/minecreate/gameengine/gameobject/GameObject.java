package com.gamedev.minecreate.gameengine.gameobject;

import com.gamedev.minecreate.gameengine.camera.Camera;
import com.gamedev.minecreate.gameengine.renderer.Mesh;
import com.gamedev.minecreate.gameengine.shaders.Shader;
import com.gamedev.minecreate.gameengine.textures.Texture;

import java.lang.Math;

import org.joml.*;

public class GameObject
{
	public Matrix4f model;
	
	public Vector3f pos;
	public Vector3f rot;
	public Vector3f scl;
	
	public Mesh mesh;
	
	public float[] vert_data;
	public float[] col_data;
	public float[] tex_c_data;
	public int[] ind_data;

	public GameObject(final Vector3f pos, final Vector3f rot, final Vector3f scl, final Shader shader, final Texture texture, final float[] vert_data, final float[] col_data, final float[] tex_c_data, final int[] ind_data)
	{
		this.pos = pos;
		this.rot = rot;
		this.scl = scl;
		
		this.vert_data = vert_data;
		this.col_data = col_data;
		this.tex_c_data = tex_c_data;
		this.ind_data = ind_data;
		
		mesh = new Mesh(this, shader, texture);
	}
	
	public void recreate()
	{
		mesh.recreate();
	}
	
	public void update(final Camera camera, final boolean is_render)
	{
		model = new Matrix4f();
		model.translate(pos);
		model.rotate((float)Math.toRadians(rot.x), new Vector3f(1.0f, 0.0f, 0.0f));
		model.rotate((float)Math.toRadians(rot.y), new Vector3f(0.0f, 1.0f, 0.0f));
		model.rotate((float)Math.toRadians(rot.z), new Vector3f(0.0f, 0.0f, 1.0f));
		model.scale(scl);
		
		mesh.pv.set(camera.proj).mul(camera.view);
		mesh.frustum.set(mesh.pv);
		
		if(is_render)
		{
			mesh.update(camera);
		}
	}
	
	public void destroy()
	{
		mesh.destroy();
	}
}