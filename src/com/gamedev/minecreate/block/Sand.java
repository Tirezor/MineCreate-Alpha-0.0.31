package com.gamedev.minecreate.block;

import com.gamedev.minecreate.resources.ResourcesMC;

import org.joml.*;

public class Sand extends Block
{
	public Sand()
	{
		this.id = 10;
		
		this.tex_coords = new Vector2i[6];
		this.tex_coords[0] = new Vector2i(11, 0);
		this.tex_coords[1] = new Vector2i(11, 0);
		this.tex_coords[2] = new Vector2i(11, 0);
		this.tex_coords[3] = new Vector2i(11, 0);
		this.tex_coords[4] = new Vector2i(11, 0);
		this.tex_coords[5] = new Vector2i(11, 0);
		
		this.block_type = BlockType.solid;
		
		this.render_block_type = RenderBlockType.render_when_air;
		
		this.is_collision = new boolean[2];
		this.is_collision[0] = true;
		this.is_collision[1] = true;
		
		this.is_transmits_light = false;
		
		this.is_gravity = true;
		
		this.sound = ResourcesMC.sand;
	}
}