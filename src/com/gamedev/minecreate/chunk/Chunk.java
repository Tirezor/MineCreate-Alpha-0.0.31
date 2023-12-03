package com.gamedev.minecreate.chunk;

import com.gamedev.minecreate.world.World;
import com.gamedev.minecreate.gameengine.camera.Camera;
import com.gamedev.minecreate.gameengine.gameobject.GameObject;
import com.gamedev.minecreate.resources.ResourcesMC;
import com.gamedev.minecreate.console.*;

import org.joml.*;

public class Chunk
{
	public ChunksController controller;
	public ChunkRenderer renderer;
	
	public GameObject obj;
	
	public Vector3f pos;
	
	public boolean is_update;
	
	public Chunk(final Vector3f pos, final ChunksController controller)
	{
		this.controller = controller;
		this.pos = new Vector3f(pos.x * World.chunk_size, pos.y * World.chunk_size, pos.z * World.chunk_size);
		renderer = new ChunkRenderer(this);
		
		ConsoleMC.print("chunk " + (int)pos.x + " " + (int)pos.y + " " + (int)pos.z + " has been generated!", PrintType.INFO);
	}
	
	public void generate()
	{
		renderer.update();
		obj = new GameObject(pos, new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(1.0f, 1.0f, 1.0f), ResourcesMC.chunk_shader, ResourcesMC.blocks_atlas, renderer.vert, renderer.col, renderer.tex_c, renderer.ind);
	}
	
	public void regenerate()
	{
		is_update = true;
		
		renderer.update();
		
		obj.vert_data = renderer.vert;
		obj.col_data = renderer.col;
		obj.tex_c_data = renderer.tex_c;
		obj.ind_data = renderer.ind;
		
		is_update = false;
	}
	
	public void recreate()
	{
		obj.recreate();
	}
	
	public void update(final Camera camera)
	{
		Vector3f c_pos = new Vector3f(pos.x + World.chunk_size / 2.0f - 0.5f, pos.y + World.chunk_size / 2.0f - 0.5f, pos.z + World.chunk_size / 2.0f - 0.5f);
		float distance = c_pos.distance(controller.world.window.player.pos);
		
		obj.update(camera, obj.mesh.frustum.testAab(pos.x - 4, pos.y - 4, pos.z - 4, pos.x + World.chunk_size + 4, pos.y + World.chunk_size + 4, pos.z + World.chunk_size + 4) && distance < controller.world.window.player.radius * World.chunk_size + World.chunk_size);
	}
	
	public void destroy()
	{
		obj.destroy();
	}
}