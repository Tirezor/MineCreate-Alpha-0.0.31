package com.gamedev.minecreate.block;

import com.gamedev.minecreate.gameengine.audio.Sound;

import org.joml.*;

public class Block
{
	public int id;
	
	public Vector2i[] tex_coords;
	
	public BlockType block_type;
	
	public RenderBlockType render_block_type;
	
	public boolean[] is_collision;
	
	public boolean is_transmits_light;
	
	public boolean is_gravity;
	
	public Sound sound;
}