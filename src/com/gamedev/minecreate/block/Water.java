package com.gamedev.minecreate.block;

import org.joml.*;

public class Water extends Block
{
	public Water()
	{
		this.id = 6;
		
		this.tex_coords = new Vector2i[6];
		this.tex_coords[0] = new Vector2i(6, 0);
		this.tex_coords[1] = new Vector2i(6, 0);
		this.tex_coords[2] = new Vector2i(6, 0);
		this.tex_coords[3] = new Vector2i(6, 0);
		this.tex_coords[4] = new Vector2i(6, 0);
		this.tex_coords[5] = new Vector2i(6, 0);
		
		this.block_type = BlockType.liquid;
		
		this.render_block_type = RenderBlockType.render_when_same_type;
		
		this.is_collision = new boolean[2];
		this.is_collision[0] = false;
		this.is_collision[1] = false;
		
		this.is_transmits_light = false;
		
		this.is_gravity = false;
		
		this.sound = null;
	}
}