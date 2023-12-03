package com.gamedev.minecreate.gameengine.camera;

import java.lang.Math;

import org.lwjgl.opengl.*;

import org.joml.*;

public class Camera
{
	public Matrix4f proj;
	public Matrix4f view;
	
	public float fov;
	public float near;
	public float far;
	
	public Vector3f pos;
	public Vector3f front;
	public Vector3f up;
	
	public Camera(final Vector3f pos, final float fov, final float near, final float far)
	{
		this.pos = pos;
		this.fov = fov;
		this.near = near;
		this.far = far;
		
		up = new Vector3f(0.0f, 1.0f, 0.0f);
		front = new Vector3f(0.0f, 0.0f, 0.0f);
	}
	
	public void update()
	{
		proj = new Matrix4f();
		view = new Matrix4f();
		proj.perspective((float)(Math.toRadians(fov)), (float)(Display.getWidth()) / (float)(Display.getHeight()), near, far);
		view.lookAt(pos, new Vector3f(pos.x + front.x, pos.y + front.y, pos.z + front.z), up);
	}
}