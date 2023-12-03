package com.gamedev.minecreate.block;

import com.gamedev.minecreate.resources.ResourcesMC;

import org.joml.*;

public class Dandelion extends Block
{
	public Dandelion()
	{
		this.id = 40;
		
		this.tex_coords = new Vector2i[6];
		this.tex_coords[0] = new Vector2i(2, 3);
		this.tex_coords[1] = new Vector2i(2, 3);
		this.tex_coords[2] = new Vector2i(2, 3);
		this.tex_coords[3] = new Vector2i(2, 3);
		this.tex_coords[4] = new Vector2i(2, 3);
		this.tex_coords[5] = new Vector2i(2, 3);
		
		this.block_type = BlockType.plant;
		
		this.render_block_type = RenderBlockType.render_always;
		
		this.is_collision = new boolean[2];
		this.is_collision[0] = false;
		this.is_collision[1] = true;
		
		this.is_transmits_light = true;
		
		this.is_gravity = false;
		
		this.sound = ResourcesMC.grass;
	}
}