package com.gamedev.minecreate.player;

import com.gamedev.minecreate.block.Block;
import com.gamedev.minecreate.block.BlockType;
import com.gamedev.minecreate.chunk.ChunkRenderer;
import com.gamedev.minecreate.gameengine.camera.Camera;
import com.gamedev.minecreate.gameengine.gameobject.GameObject;
import com.gamedev.minecreate.gameengine.time.Timer;
import com.gamedev.minecreate.resources.ResourcesMC;
import com.gamedev.minecreate.world.World;

import java.lang.Math;
import java.util.*;

import org.lwjgl.input.*;

import org.joml.*;

import static org.lwjgl.opengl.GL20.*;

public class BlockHand
{
	public Player player;
	
	public GameObject obj;
	
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
	
	private Block block;
	
	private float[] rotate;
	private boolean[] is_rotate;
	
	private boolean is_click;
	
	public BlockHand(final Player player)
	{
		this.player = player;
		
		rotate = new float[2];
		is_rotate = new boolean[2];
		
		block = player.blocks[player.blck];
		
		is_render = true;
		
		vert_data = new ArrayList<Float>();
		col_data = new ArrayList<Float>();
		t_coord_data = new ArrayList<Float>();
		ind_data = new ArrayList<Integer>();
		
		count = 0;
		
		if(player.blocks[player.blck].block_type == BlockType.plant)
		{	
			vert_add(new Vector3f(-0.5f, -0.5f, 0.0f));
			vert_add(new Vector3f(0.5f, -0.5f, 0.0f));
			vert_add(new Vector3f(0.5f, 0.5f, 0.0f));
			vert_add(new Vector3f(-0.5f, 0.5f, 0.0f));
			
			t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[2].x * ChunkRenderer.ot, player.blocks[player.blck].tex_coords[2].y * ChunkRenderer.ot + ChunkRenderer.ot));
			t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[2].x * ChunkRenderer.ot + ChunkRenderer.ot, player.blocks[player.blck].tex_coords[2].y * ChunkRenderer.ot + ChunkRenderer.ot));
			t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[2].x * ChunkRenderer.ot + ChunkRenderer.ot, player.blocks[player.blck].tex_coords[2].y * ChunkRenderer.ot));
			t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[2].x * ChunkRenderer.ot, player.blocks[player.blck].tex_coords[2].y * ChunkRenderer.ot));
			
			add_col(new Vector3f(1.0f, 1.0f, 1.0f));
			
			add_ind();
			
			vert_add(new Vector3f(-0.5f, -0.5f, 0.0f));
			vert_add(new Vector3f(-0.5f, 0.5f, 0.0f));
			vert_add(new Vector3f(0.5f, 0.5f, 0.0f));
			vert_add(new Vector3f(0.5f, -0.5f, 0.0f));
			
			t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[3].x * ChunkRenderer.ot + ChunkRenderer.ot, player.blocks[player.blck].tex_coords[3].y * ChunkRenderer.ot + ChunkRenderer.ot));
			t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[3].x * ChunkRenderer.ot + ChunkRenderer.ot, player.blocks[player.blck].tex_coords[3].y * ChunkRenderer.ot));
			t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[3].x * ChunkRenderer.ot, player.blocks[player.blck].tex_coords[3].y * ChunkRenderer.ot));
			t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[3].x * ChunkRenderer.ot, player.blocks[player.blck].tex_coords[3].y * ChunkRenderer.ot + ChunkRenderer.ot));
			
			add_col(new Vector3f(1.0f, 1.0f, 1.0f));
			
			add_ind();
			
			vert_add(new Vector3f(0.0f, -0.5f, -0.5f));
			vert_add(new Vector3f(0.0f, 0.5f, -0.5f));
			vert_add(new Vector3f(0.0f, 0.5f, 0.5f));
			vert_add(new Vector3f(0.0f, -0.5f, 0.5f));
			
			t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[4].x * ChunkRenderer.ot + ChunkRenderer.ot, player.blocks[player.blck].tex_coords[4].y * ChunkRenderer.ot + ChunkRenderer.ot));
			t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[4].x * ChunkRenderer.ot + ChunkRenderer.ot, player.blocks[player.blck].tex_coords[4].y * ChunkRenderer.ot));
			t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[4].x * ChunkRenderer.ot, player.blocks[player.blck].tex_coords[4].y * ChunkRenderer.ot));
			t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[4].x * ChunkRenderer.ot, player.blocks[player.blck].tex_coords[4].y * ChunkRenderer.ot + ChunkRenderer.ot));
			
			add_col(new Vector3f(1.0f, 1.0f, 1.0f));
			
			add_ind();
			
			vert_add(new Vector3f(0.0f, -0.5f, -0.5f));
			vert_add(new Vector3f(0.0f, -0.5f, 0.5f));
			vert_add(new Vector3f(0.0f, 0.5f, 0.5f));
			vert_add(new Vector3f(0.0f, 0.5f, -0.5f));
			
			t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[5].x * ChunkRenderer.ot, player.blocks[player.blck].tex_coords[5].y * ChunkRenderer.ot + ChunkRenderer.ot));
			t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[5].x * ChunkRenderer.ot + ChunkRenderer.ot, player.blocks[player.blck].tex_coords[5].y * ChunkRenderer.ot + ChunkRenderer.ot));
			t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[5].x * ChunkRenderer.ot + ChunkRenderer.ot, player.blocks[player.blck].tex_coords[5].y * ChunkRenderer.ot));
			t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[5].x * ChunkRenderer.ot, player.blocks[player.blck].tex_coords[5].y * ChunkRenderer.ot));
			
			add_col(new Vector3f(1.0f, 1.0f, 1.0f));
			
			add_ind();
			
			convert();
		}
		
		else
		{
			vert_add(new Vector3f(-0.5f, 0.5f, -0.5f));
			vert_add(new Vector3f(-0.5f, 0.5f, 0.5f));
			vert_add(new Vector3f(0.5f, 0.5f, 0.5f));
			vert_add(new Vector3f(0.5f, 0.5f, -0.5f));
			
			t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[0].x * ChunkRenderer.ot + ChunkRenderer.ot, player.blocks[player.blck].tex_coords[0].y * ChunkRenderer.ot + ChunkRenderer.ot));
			t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[0].x * ChunkRenderer.ot + ChunkRenderer.ot, player.blocks[player.blck].tex_coords[0].y * ChunkRenderer.ot));
			t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[0].x * ChunkRenderer.ot, player.blocks[player.blck].tex_coords[0].y * ChunkRenderer.ot));
			t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[0].x * ChunkRenderer.ot, player.blocks[player.blck].tex_coords[0].y * ChunkRenderer.ot + ChunkRenderer.ot));
			
			add_col(new Vector3f(1.0f, 1.0f, 1.0f));
			
			add_ind();
			
			vert_add(new Vector3f(-0.5f, -0.5f, -0.5f));
			vert_add(new Vector3f(0.5f, -0.5f, -0.5f));
			vert_add(new Vector3f(0.5f, -0.5f, 0.5f));
			vert_add(new Vector3f(-0.5f, -0.5f, 0.5f));
			
			t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[1].x * ChunkRenderer.ot + ChunkRenderer.ot, player.blocks[player.blck].tex_coords[1].y * ChunkRenderer.ot));
			t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[1].x * ChunkRenderer.ot, player.blocks[player.blck].tex_coords[1].y * ChunkRenderer.ot));
			t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[1].x * ChunkRenderer.ot, player.blocks[player.blck].tex_coords[1].y * ChunkRenderer.ot + ChunkRenderer.ot));
			t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[1].x * ChunkRenderer.ot + ChunkRenderer.ot, player.blocks[player.blck].tex_coords[1].y * ChunkRenderer.ot + ChunkRenderer.ot));
			
			add_col(new Vector3f(1.0f, 1.0f, 1.0f));
			
			add_ind();
			
			vert_add(new Vector3f(-0.5f, -0.5f, 0.5f));
			vert_add(new Vector3f(0.5f, -0.5f, 0.5f));
			vert_add(new Vector3f(0.5f, 0.5f, 0.5f));
			vert_add(new Vector3f(-0.5f, 0.5f, 0.5f));
			
			t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[2].x * ChunkRenderer.ot, player.blocks[player.blck].tex_coords[2].y * ChunkRenderer.ot + ChunkRenderer.ot));
			t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[2].x * ChunkRenderer.ot + ChunkRenderer.ot, player.blocks[player.blck].tex_coords[2].y * ChunkRenderer.ot + ChunkRenderer.ot));
			t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[2].x * ChunkRenderer.ot + ChunkRenderer.ot, player.blocks[player.blck].tex_coords[2].y * ChunkRenderer.ot));
			t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[2].x * ChunkRenderer.ot, player.blocks[player.blck].tex_coords[2].y * ChunkRenderer.ot));
			
			add_col(new Vector3f(1.0f, 1.0f, 1.0f));
			
			add_ind();
			
			vert_add(new Vector3f(-0.5f, -0.5f, -0.5f));
			vert_add(new Vector3f(-0.5f, 0.5f, -0.5f));
			vert_add(new Vector3f(0.5f, 0.5f, -0.5f));
			vert_add(new Vector3f(0.5f, -0.5f, -0.5f));
			
			t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[3].x * ChunkRenderer.ot + ChunkRenderer.ot, player.blocks[player.blck].tex_coords[3].y * ChunkRenderer.ot + ChunkRenderer.ot));
			t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[3].x * ChunkRenderer.ot + ChunkRenderer.ot, player.blocks[player.blck].tex_coords[3].y * ChunkRenderer.ot));
			t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[3].x * ChunkRenderer.ot, player.blocks[player.blck].tex_coords[3].y * ChunkRenderer.ot));
			t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[3].x * ChunkRenderer.ot, player.blocks[player.blck].tex_coords[3].y * ChunkRenderer.ot + ChunkRenderer.ot));
			
			add_col(new Vector3f(1.0f, 1.0f, 1.0f));
			
			add_ind();
			
			vert_add(new Vector3f(0.5f, -0.5f, -0.5f));
			vert_add(new Vector3f(0.5f, 0.5f, -0.5f));
			vert_add(new Vector3f(0.5f, 0.5f, 0.5f));
			vert_add(new Vector3f(0.5f, -0.5f, 0.5f));
			
			t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[4].x * ChunkRenderer.ot + ChunkRenderer.ot, player.blocks[player.blck].tex_coords[4].y * ChunkRenderer.ot + ChunkRenderer.ot));
			t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[4].x * ChunkRenderer.ot + ChunkRenderer.ot, player.blocks[player.blck].tex_coords[4].y * ChunkRenderer.ot));
			t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[4].x * ChunkRenderer.ot, player.blocks[player.blck].tex_coords[4].y * ChunkRenderer.ot));
			t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[4].x * ChunkRenderer.ot, player.blocks[player.blck].tex_coords[4].y * ChunkRenderer.ot + ChunkRenderer.ot));
			
			add_col(new Vector3f(1.0f, 1.0f, 1.0f));
			
			add_ind();
			
			vert_add(new Vector3f(-0.5f, -0.5f, -0.5f));
			vert_add(new Vector3f(-0.5f, -0.5f, 0.5f));
			vert_add(new Vector3f(-0.5f, 0.5f, 0.5f));
			vert_add(new Vector3f(-0.5f, 0.5f, -0.5f));
			
			t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[5].x * ChunkRenderer.ot, player.blocks[player.blck].tex_coords[5].y * ChunkRenderer.ot + ChunkRenderer.ot));
			t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[5].x * ChunkRenderer.ot + ChunkRenderer.ot, player.blocks[player.blck].tex_coords[5].y * ChunkRenderer.ot + ChunkRenderer.ot));
			t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[5].x * ChunkRenderer.ot + ChunkRenderer.ot, player.blocks[player.blck].tex_coords[5].y * ChunkRenderer.ot));
			t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[5].x * ChunkRenderer.ot, player.blocks[player.blck].tex_coords[5].y * ChunkRenderer.ot));
			
			add_col(new Vector3f(1.0f, 1.0f, 1.0f));
			
			add_ind();
			
			convert();
		}
		
		obj = new GameObject(new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(1.0f, 1.0f, 1.0f), ResourcesMC.blockhand_shader, ResourcesMC.blocks_atlas, vert, col, tex_c, ind);
	}
	
	public void update(final Camera camera, final World world)
	{
		if(block != player.blocks[player.blck] && !world.is_loading)
		{
			is_rotate[1] = true;
		}
		
		if(is_rotate[1] && !world.is_loading)
		{
			rotate[1] += 350.0f * Timer.delta_time;
		}
		
		else
		{
			rotate[1] -= 350.0f * Timer.delta_time;
			
			if(rotate[1] < 0.0f)
			{
				rotate[1] = 0.0f;
			}
		}
		
		if(rotate[1] >= 55.0f && !world.is_loading)
		{
			rotate[1] = 55.0f;
			
			is_rotate[1] = false;
			
			block = player.blocks[player.blck];
			
			vert_data.clear();
			col_data.clear();
			t_coord_data.clear();
			ind_data.clear();
			
			count = 0;
			
			if(player.blocks[player.blck].block_type == BlockType.plant)
			{	
				vert_add(new Vector3f(-0.5f, -0.5f, 0.0f));
				vert_add(new Vector3f(0.5f, -0.5f, 0.0f));
				vert_add(new Vector3f(0.5f, 0.5f, 0.0f));
				vert_add(new Vector3f(-0.5f, 0.5f, 0.0f));
				
				t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[2].x * ChunkRenderer.ot, player.blocks[player.blck].tex_coords[2].y * ChunkRenderer.ot + ChunkRenderer.ot));
				t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[2].x * ChunkRenderer.ot + ChunkRenderer.ot, player.blocks[player.blck].tex_coords[2].y * ChunkRenderer.ot + ChunkRenderer.ot));
				t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[2].x * ChunkRenderer.ot + ChunkRenderer.ot, player.blocks[player.blck].tex_coords[2].y * ChunkRenderer.ot));
				t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[2].x * ChunkRenderer.ot, player.blocks[player.blck].tex_coords[2].y * ChunkRenderer.ot));
				
				add_col(new Vector3f(1.0f, 1.0f, 1.0f));
				
				add_ind();
				
				vert_add(new Vector3f(-0.5f, -0.5f, 0.0f));
				vert_add(new Vector3f(-0.5f, 0.5f, 0.0f));
				vert_add(new Vector3f(0.5f, 0.5f, 0.0f));
				vert_add(new Vector3f(0.5f, -0.5f, 0.0f));
				
				t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[3].x * ChunkRenderer.ot + ChunkRenderer.ot, player.blocks[player.blck].tex_coords[3].y * ChunkRenderer.ot + ChunkRenderer.ot));
				t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[3].x * ChunkRenderer.ot + ChunkRenderer.ot, player.blocks[player.blck].tex_coords[3].y * ChunkRenderer.ot));
				t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[3].x * ChunkRenderer.ot, player.blocks[player.blck].tex_coords[3].y * ChunkRenderer.ot));
				t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[3].x * ChunkRenderer.ot, player.blocks[player.blck].tex_coords[3].y * ChunkRenderer.ot + ChunkRenderer.ot));
				
				add_col(new Vector3f(1.0f, 1.0f, 1.0f));
				
				add_ind();
				
				vert_add(new Vector3f(0.0f, -0.5f, -0.5f));
				vert_add(new Vector3f(0.0f, 0.5f, -0.5f));
				vert_add(new Vector3f(0.0f, 0.5f, 0.5f));
				vert_add(new Vector3f(0.0f, -0.5f, 0.5f));
				
				t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[4].x * ChunkRenderer.ot + ChunkRenderer.ot, player.blocks[player.blck].tex_coords[4].y * ChunkRenderer.ot + ChunkRenderer.ot));
				t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[4].x * ChunkRenderer.ot + ChunkRenderer.ot, player.blocks[player.blck].tex_coords[4].y * ChunkRenderer.ot));
				t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[4].x * ChunkRenderer.ot, player.blocks[player.blck].tex_coords[4].y * ChunkRenderer.ot));
				t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[4].x * ChunkRenderer.ot, player.blocks[player.blck].tex_coords[4].y * ChunkRenderer.ot + ChunkRenderer.ot));
				
				add_col(new Vector3f(1.0f, 1.0f, 1.0f));
				
				add_ind();
				
				vert_add(new Vector3f(0.0f, -0.5f, -0.5f));
				vert_add(new Vector3f(0.0f, -0.5f, 0.5f));
				vert_add(new Vector3f(0.0f, 0.5f, 0.5f));
				vert_add(new Vector3f(0.0f, 0.5f, -0.5f));
				
				t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[5].x * ChunkRenderer.ot, player.blocks[player.blck].tex_coords[5].y * ChunkRenderer.ot + ChunkRenderer.ot));
				t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[5].x * ChunkRenderer.ot + ChunkRenderer.ot, player.blocks[player.blck].tex_coords[5].y * ChunkRenderer.ot + ChunkRenderer.ot));
				t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[5].x * ChunkRenderer.ot + ChunkRenderer.ot, player.blocks[player.blck].tex_coords[5].y * ChunkRenderer.ot));
				t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[5].x * ChunkRenderer.ot, player.blocks[player.blck].tex_coords[5].y * ChunkRenderer.ot));
				
				add_col(new Vector3f(1.0f, 1.0f, 1.0f));
				
				add_ind();
				
				convert();
			}
			
			else
			{
				vert_add(new Vector3f(-0.5f, 0.5f, -0.5f));
				vert_add(new Vector3f(-0.5f, 0.5f, 0.5f));
				vert_add(new Vector3f(0.5f, 0.5f, 0.5f));
				vert_add(new Vector3f(0.5f, 0.5f, -0.5f));
				
				t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[0].x * ChunkRenderer.ot + ChunkRenderer.ot, player.blocks[player.blck].tex_coords[0].y * ChunkRenderer.ot + ChunkRenderer.ot));
				t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[0].x * ChunkRenderer.ot + ChunkRenderer.ot, player.blocks[player.blck].tex_coords[0].y * ChunkRenderer.ot));
				t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[0].x * ChunkRenderer.ot, player.blocks[player.blck].tex_coords[0].y * ChunkRenderer.ot));
				t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[0].x * ChunkRenderer.ot, player.blocks[player.blck].tex_coords[0].y * ChunkRenderer.ot + ChunkRenderer.ot));
				
				add_col(new Vector3f(1.0f, 1.0f, 1.0f));
				
				add_ind();
				
				vert_add(new Vector3f(-0.5f, -0.5f, -0.5f));
				vert_add(new Vector3f(0.5f, -0.5f, -0.5f));
				vert_add(new Vector3f(0.5f, -0.5f, 0.5f));
				vert_add(new Vector3f(-0.5f, -0.5f, 0.5f));
				
				t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[1].x * ChunkRenderer.ot + ChunkRenderer.ot, player.blocks[player.blck].tex_coords[1].y * ChunkRenderer.ot));
				t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[1].x * ChunkRenderer.ot, player.blocks[player.blck].tex_coords[1].y * ChunkRenderer.ot));
				t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[1].x * ChunkRenderer.ot, player.blocks[player.blck].tex_coords[1].y * ChunkRenderer.ot + ChunkRenderer.ot));
				t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[1].x * ChunkRenderer.ot + ChunkRenderer.ot, player.blocks[player.blck].tex_coords[1].y * ChunkRenderer.ot + ChunkRenderer.ot));
				
				add_col(new Vector3f(1.0f, 1.0f, 1.0f));
				
				add_ind();
				
				vert_add(new Vector3f(-0.5f, -0.5f, 0.5f));
				vert_add(new Vector3f(0.5f, -0.5f, 0.5f));
				vert_add(new Vector3f(0.5f, 0.5f, 0.5f));
				vert_add(new Vector3f(-0.5f, 0.5f, 0.5f));
				
				t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[2].x * ChunkRenderer.ot, player.blocks[player.blck].tex_coords[2].y * ChunkRenderer.ot + ChunkRenderer.ot));
				t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[2].x * ChunkRenderer.ot + ChunkRenderer.ot, player.blocks[player.blck].tex_coords[2].y * ChunkRenderer.ot + ChunkRenderer.ot));
				t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[2].x * ChunkRenderer.ot + ChunkRenderer.ot, player.blocks[player.blck].tex_coords[2].y * ChunkRenderer.ot));
				t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[2].x * ChunkRenderer.ot, player.blocks[player.blck].tex_coords[2].y * ChunkRenderer.ot));
				
				add_col(new Vector3f(1.0f, 1.0f, 1.0f));
				
				add_ind();
				
				vert_add(new Vector3f(-0.5f, -0.5f, -0.5f));
				vert_add(new Vector3f(-0.5f, 0.5f, -0.5f));
				vert_add(new Vector3f(0.5f, 0.5f, -0.5f));
				vert_add(new Vector3f(0.5f, -0.5f, -0.5f));
				
				t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[3].x * ChunkRenderer.ot + ChunkRenderer.ot, player.blocks[player.blck].tex_coords[3].y * ChunkRenderer.ot + ChunkRenderer.ot));
				t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[3].x * ChunkRenderer.ot + ChunkRenderer.ot, player.blocks[player.blck].tex_coords[3].y * ChunkRenderer.ot));
				t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[3].x * ChunkRenderer.ot, player.blocks[player.blck].tex_coords[3].y * ChunkRenderer.ot));
				t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[3].x * ChunkRenderer.ot, player.blocks[player.blck].tex_coords[3].y * ChunkRenderer.ot + ChunkRenderer.ot));
				
				add_col(new Vector3f(1.0f, 1.0f, 1.0f));
				
				add_ind();
				
				vert_add(new Vector3f(0.5f, -0.5f, -0.5f));
				vert_add(new Vector3f(0.5f, 0.5f, -0.5f));
				vert_add(new Vector3f(0.5f, 0.5f, 0.5f));
				vert_add(new Vector3f(0.5f, -0.5f, 0.5f));
				
				t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[4].x * ChunkRenderer.ot + ChunkRenderer.ot, player.blocks[player.blck].tex_coords[4].y * ChunkRenderer.ot + ChunkRenderer.ot));
				t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[4].x * ChunkRenderer.ot + ChunkRenderer.ot, player.blocks[player.blck].tex_coords[4].y * ChunkRenderer.ot));
				t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[4].x * ChunkRenderer.ot, player.blocks[player.blck].tex_coords[4].y * ChunkRenderer.ot));
				t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[4].x * ChunkRenderer.ot, player.blocks[player.blck].tex_coords[4].y * ChunkRenderer.ot + ChunkRenderer.ot));
				
				add_col(new Vector3f(1.0f, 1.0f, 1.0f));
				
				add_ind();
				
				vert_add(new Vector3f(-0.5f, -0.5f, -0.5f));
				vert_add(new Vector3f(-0.5f, -0.5f, 0.5f));
				vert_add(new Vector3f(-0.5f, 0.5f, 0.5f));
				vert_add(new Vector3f(-0.5f, 0.5f, -0.5f));
				
				t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[5].x * ChunkRenderer.ot, player.blocks[player.blck].tex_coords[5].y * ChunkRenderer.ot + ChunkRenderer.ot));
				t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[5].x * ChunkRenderer.ot + ChunkRenderer.ot, player.blocks[player.blck].tex_coords[5].y * ChunkRenderer.ot + ChunkRenderer.ot));
				t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[5].x * ChunkRenderer.ot + ChunkRenderer.ot, player.blocks[player.blck].tex_coords[5].y * ChunkRenderer.ot));
				t_coord_add(new Vector2f(player.blocks[player.blck].tex_coords[5].x * ChunkRenderer.ot, player.blocks[player.blck].tex_coords[5].y * ChunkRenderer.ot));
				
				add_col(new Vector3f(1.0f, 1.0f, 1.0f));
				
				add_ind();
				
				convert();
			}
			
			obj.vert_data = vert;
			obj.col_data = col;
			obj.tex_c_data = tex_c;
			obj.ind_data = ind;
			
			obj.recreate();
		}
		
		if((Mouse.isButtonDown(0) || Mouse.isButtonDown(1)) && !player.is_pause && !player.is_inventory && !world.is_loading && !is_rotate[1])
		{
			if(!is_click)
			{
				is_click = true;
				
				if(rotate[0] < 24.0f)
				{
					is_rotate[0] = true;
				}
			}
		}
		
		else
		{
			is_click = false;
		}
		
		if(is_rotate[0])
		{
			rotate[0] += 250.0f * Timer.delta_time;
		}
		
		else
		{
			rotate[0] -= 250.0f * Timer.delta_time;
			
			if(rotate[0] < 0.0f)
			{
				rotate[0] = 0.0f;
			}
		}
		
		if(rotate[0] >= 24.0f)
		{
			rotate[0] = 24.0f;
			is_rotate[0] = false;
		}

		int x = (int)(Math.round(obj.pos.x));
		int y = (int)(Math.round(obj.pos.y));
		int z = (int)(Math.round(obj.pos.z));
		
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
		
		obj.rot = new Vector3f(0, -player.dir_x, player.dir_y + 10.0f + rotate[0] - rotate[1]);
		obj.pos = new Vector3f(camera.pos.x + (player.right.x * -(float)(Math.sin(player.walks * player.camera_speed / 1.5f) / player.camera_strength)), camera.pos.y + (float)(Math.sin(player.walks * player.camera_speed) / player.camera_strength), camera.pos.z + (player.right.z * -(float)(Math.sin(player.walks * player.camera_speed / 1.5f) / player.camera_strength)));;
		
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
		if(player.blocks[player.blck].block_type == BlockType.plant)
		{
			vert_data.add(vec3.x + 1.25f);
			vert_data.add(vec3.y - 0.5f);
			vert_data.add(vec3.z + 1.0f);
		}
		
		else
		{
			vert_data.add(vec3.x + 1.25f);
			vert_data.add(vec3.y - 1.25f);
			vert_data.add(vec3.z + 1.25f);
		}
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