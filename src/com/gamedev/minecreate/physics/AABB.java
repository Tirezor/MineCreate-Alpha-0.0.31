package com.gamedev.minecreate.physics;

import org.joml.*;

public class AABB
{
	public Vector3f pos;
	public Vector3f size;
	
	public AABB(final Vector3f pos, final Vector3f size)
	{
		this.pos = pos;
		this.size = size;
	}
	
	public static boolean is_collision(AABB aabb1, AABB aabb2)
	{
		return aabb1.max_x() > aabb2.min_x() && aabb1.min_x() < aabb2.max_x() && aabb1.max_y() > aabb2.min_y() && aabb1.min_y() < aabb2.max_y() && aabb1.max_z() > aabb2.min_z() && aabb1.min_z() < aabb2.max_z();
	}
	
	public float min_x()
	{
		return pos.x - size.x / 2.0f;
	}
	
	public float max_x()
	{
		return pos.x + size.x / 2.0f;
	}
	
	public float min_y()
	{
		return pos.y - size.y / 2.0f;
	}
	
	public float max_y()
	{
		return pos.y + size.y / 2.0f;
	}
	
	public float min_z()
	{
		return pos.z - size.z / 2.0f;
	}
	
	public float max_z()
	{
		return pos.z + size.z / 2.0f;
	}
}