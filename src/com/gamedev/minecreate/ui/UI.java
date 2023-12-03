package com.gamedev.minecreate.ui;

import com.gamedev.minecreate.block.Block;
import com.gamedev.minecreate.gameengine.audio.Audio;
import com.gamedev.minecreate.gameengine.camera.Camera;
import com.gamedev.minecreate.gameengine.time.Timer;
import com.gamedev.minecreate.gameengine.window.Window;
import com.gamedev.minecreate.resources.ResourcesMC;
import com.gamedev.minecreate.world.World;

import java.lang.Math;
import java.util.Random;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.text.DecimalFormat;

import org.lwjgl.input.*;
import org.lwjgl.opengl.*;

import org.joml.*;

public class UI
{
	public ImageUI point;
	public ImageUI hotbar;
	public ImageUI selected_hotbar;
	public ImageUI hotbar_blocks;
	public ImageUI inventory;
	public ImageUI inventory_blocks;
	public ImageUI background;
	public TextUI text;
	public ImageUI text_background;
	public ImageUI buttons;
	public ImageUI loading;
	public ImageUI mm_background;
	public ImageUI logo;
	
	private boolean is_clicked;
	private boolean is_click;
	private boolean[] is_sound;
	
	private float mmb_offset;
	private float spsh_offset;
	
	private String splash;
	private String splashes;
	
	public UI(final Window window)
	{
		point = new ImageUI(null);
		hotbar = new ImageUI(ResourcesMC.hotbar);
		selected_hotbar = new ImageUI(ResourcesMC.selected_hotbar);
		hotbar_blocks = new ImageUI(ResourcesMC.blocks_atlas);
		inventory = new ImageUI(null);
		inventory_blocks = new ImageUI(ResourcesMC.blocks_atlas);
		background = new ImageUI(null);
		text = new TextUI(ResourcesMC.font);
		text_background = new ImageUI(null);
		buttons = new ImageUI(ResourcesMC.buttons_ui);
		loading = new ImageUI(ResourcesMC.background);
		mm_background = new ImageUI(ResourcesMC.background);
		logo = new ImageUI(ResourcesMC.logo);
		
		is_sound = new boolean[13];
		
		splash = generate_splash();
	}
	
	public void update(final Camera camera, final Window window) throws Exception
	{
		mmb_offset += 1.0f * Timer.delta_time;
		
		if(mmb_offset >= 1.0f)
		{
			mmb_offset -= (int)(mmb_offset);
		}
		
		if(Mouse.isButtonDown(0))
		{
			if(!is_clicked)
			{
				is_clicked = true;
				is_click = false;
			}
		}
		
		else
		{
			if(is_clicked)
			{
				is_click = true;
			}
			
			else
			{
				is_click = false;
			}
			
			is_clicked = false;
		}
		
		background.enabled = window.player.is_inventory || window.player.is_pause;
		
		clear_image(background);
		
		background.vert_add(new Vector3f(-1, -1, 0.0f));
		background.vert_add(new Vector3f(1, -1, 0.0f));
		background.vert_add(new Vector3f(1, 1, 0.0f));
		background.vert_add(new Vector3f(-1, 1, 0.0f));

		background.t_coord_add(new Vector2f(0, 1));
		background.t_coord_add(new Vector2f(1, 1));
		background.t_coord_add(new Vector2f(1, 0));
		background.t_coord_add(new Vector2f(0, 0));
		
		background.add_col(new Vector4f(0, 0, 0, 0.75f));
		
		background.add_ind();
		
		background.convert();
		
		update_image(background, camera);
		
		clear_image(point);

		float s = 1.0f / 100.0f;
		
		point.vert_add(new Vector3f((0 - s) / ((float)Display.getWidth() / (float)Display.getHeight()), 0 - s, 0.0f));
		point.vert_add(new Vector3f((0 + s) / ((float)Display.getWidth() / (float)Display.getHeight()), 0 - s, 0.0f));
		point.vert_add(new Vector3f((0 + s) / ((float)Display.getWidth() / (float)Display.getHeight()), 0 + s, 0.0f));
		point.vert_add(new Vector3f((0 - s) / ((float)Display.getWidth() / (float)Display.getHeight()), 0 + s, 0.0f));

		point.t_coord_add(new Vector2f(0, 1));
		point.t_coord_add(new Vector2f(1, 1));
		point.t_coord_add(new Vector2f(1, 0));
		point.t_coord_add(new Vector2f(0, 0));
		
		point.add_col(new Vector4f(0.5f, 0.5f, 0.5f, 0.5f));
		
		point.add_ind();
		
		point.convert();
		
		update_image(point, camera);
		
		clear_image(hotbar);
		
		clear_image(hotbar_blocks);

		for(int i = 0; i < 9; i++)
		{
			hotbar.vert_add(new Vector3f(-(0.175f / ((float)Display.getWidth() / Display.getHeight()) * (4 - i)) - (0.175f / 2 / ((float)Display.getWidth() / Display.getHeight())), -1, 0.0f));
			hotbar.vert_add(new Vector3f((-(0.175f / ((float)Display.getWidth() / Display.getHeight()) * (4 - i)) - (0.175f / 2 / ((float)Display.getWidth() / Display.getHeight()))) + (0.175f / ((float)Display.getWidth() / Display.getHeight())), -1, 0.0f));
			hotbar.vert_add(new Vector3f((-(0.175f / ((float)Display.getWidth() / Display.getHeight()) * (4 - i)) - (0.175f / 2 / ((float)Display.getWidth() / Display.getHeight()))) + (0.175f / ((float)Display.getWidth() / Display.getHeight())), -1 + 0.175f, 0.0f));
			hotbar.vert_add(new Vector3f(-(0.175f / ((float)Display.getWidth() / Display.getHeight()) * (4 - i)) - (0.175f / 2 / ((float)Display.getWidth() / Display.getHeight())), -1 + 0.175f, 0.0f));

			hotbar.t_coord_add(new Vector2f(0, 1));
			hotbar.t_coord_add(new Vector2f(1, 1));
			hotbar.t_coord_add(new Vector2f(1, 0));
			hotbar.t_coord_add(new Vector2f(0, 0));
			
			hotbar.add_col(new Vector4f(1, 1, 1, 1));
			
			hotbar.add_ind();
			
			hotbar_blocks.vert_add(new Vector3f(-(0.175f / ((float)Display.getWidth() / Display.getHeight()) * (4 - i)) - (0.115f / 2 / ((float)Display.getWidth() / Display.getHeight())), -1 + 0.115f / 4, 0.0f));
			hotbar_blocks.vert_add(new Vector3f((-(0.175f / ((float)Display.getWidth() / Display.getHeight()) * (4 - i)) - (0.115f / 2 / ((float)Display.getWidth() / Display.getHeight()))) + (0.115f / ((float)Display.getWidth() / Display.getHeight())), -1 + 0.115f / 4, 0.0f));
			hotbar_blocks.vert_add(new Vector3f((-(0.175f / ((float)Display.getWidth() / Display.getHeight()) * (4 - i)) - (0.115f / 2 / ((float)Display.getWidth() / Display.getHeight()))) + (0.115f / ((float)Display.getWidth() / Display.getHeight())), (-1 + 0.115f / 4) + 0.115f, 0.0f));
			hotbar_blocks.vert_add(new Vector3f(-(0.175f / ((float)Display.getWidth() / Display.getHeight()) * (4 - i)) - (0.115f / 2 / ((float)Display.getWidth() / Display.getHeight())), -1 + 0.115f / 4 + 0.115f, 0.0f));

			float ot = 0.0625f;
			
			hotbar_blocks.t_coord_add(new Vector2f(window.player.blocks[i].tex_coords[2].x * ot, window.player.blocks[i].tex_coords[2].y * ot + ot));
			hotbar_blocks.t_coord_add(new Vector2f(window.player.blocks[i].tex_coords[2].x * ot + ot, window.player.blocks[i].tex_coords[2].y * ot + ot));
			hotbar_blocks.t_coord_add(new Vector2f(window.player.blocks[i].tex_coords[2].x * ot + ot, window.player.blocks[i].tex_coords[2].y * ot));
			hotbar_blocks.t_coord_add(new Vector2f(window.player.blocks[i].tex_coords[2].x * ot, window.player.blocks[i].tex_coords[2].y * ot));
			
			hotbar_blocks.add_col(new Vector4f(1, 1, 1, 1));
			
			hotbar_blocks.add_ind();
		}
		
		hotbar.convert();
		
		update_image(hotbar, camera);
		
		hotbar_blocks.convert();
		
		update_image(hotbar_blocks, camera);
		
		clear_image(selected_hotbar);

		selected_hotbar.vert_add(new Vector3f(-(0.175f / ((float)Display.getWidth() / Display.getHeight()) * (4 - window.player.blck)) - (0.175f / 2 / ((float)Display.getWidth() / Display.getHeight())), -1, 0.0f));
		selected_hotbar.vert_add(new Vector3f((-(0.175f / ((float)Display.getWidth() / Display.getHeight()) * (4 - window.player.blck)) - (0.175f / 2 / ((float)Display.getWidth() / Display.getHeight()))) + (0.175f / ((float)Display.getWidth() / Display.getHeight())), -1, 0.0f));
		selected_hotbar.vert_add(new Vector3f((-(0.175f / ((float)Display.getWidth() / Display.getHeight()) * (4 - window.player.blck)) - (0.175f / 2 / ((float)Display.getWidth() / Display.getHeight()))) + (0.175f / ((float)Display.getWidth() / Display.getHeight())), -1 + 0.175f, 0.0f));
		selected_hotbar.vert_add(new Vector3f(-(0.175f / ((float)Display.getWidth() / Display.getHeight()) * (4 - window.player.blck)) - (0.175f / 2 / ((float)Display.getWidth() / Display.getHeight())), -1 + 0.175f, 0.0f));

		selected_hotbar.t_coord_add(new Vector2f(0, 1));
		selected_hotbar.t_coord_add(new Vector2f(1, 1));
		selected_hotbar.t_coord_add(new Vector2f(1, 0));
		selected_hotbar.t_coord_add(new Vector2f(0, 0));
		
		selected_hotbar.add_col(new Vector4f(1, 1, 1, 1));
		
		selected_hotbar.add_ind();
		
		selected_hotbar.convert();
		
		update_image(selected_hotbar, camera);
		
		inventory.enabled = window.player.is_inventory;
		
		clear_image(inventory);

		inventory.vert_add(new Vector3f(-(0.175f / ((float)Display.getWidth() / Display.getHeight()) * 5) - (0.175f / 2 / ((float)Display.getWidth() / Display.getHeight())), 0 - (0.175f * 9) / 2, 0.0f));
		inventory.vert_add(new Vector3f((-(0.175f / ((float)Display.getWidth() / Display.getHeight()) * 5) - (0.175f / 2 / ((float)Display.getWidth() / Display.getHeight()))) + (0.175f * 11 / ((float)Display.getWidth() / Display.getHeight())), 0 - (0.175f * 9) / 2, 0.0f));
		inventory.vert_add(new Vector3f((-(0.175f / ((float)Display.getWidth() / Display.getHeight()) * 5) - (0.175f / 2 / ((float)Display.getWidth() / Display.getHeight()))) + (0.175f * 11 / ((float)Display.getWidth() / Display.getHeight())), (0 - (0.175f * 9) / 2) + (0.175f * 9), 0.0f));
		inventory.vert_add(new Vector3f(-(0.175f / ((float)Display.getWidth() / Display.getHeight()) * 5) - (0.175f / 2 / ((float)Display.getWidth() / Display.getHeight())), (0 - (0.175f * 9) / 2) + (0.175f * 9), 0.0f));

		inventory.t_coord_add(new Vector2f(0, 1));
		inventory.t_coord_add(new Vector2f(1, 1));
		inventory.t_coord_add(new Vector2f(1, 0));
		inventory.t_coord_add(new Vector2f(0, 0));
		
		inventory.add_col(new Vector4f(0.5f, 0.5f, 0.5f, 1));
		
		inventory.add_ind();
		
		inventory.convert();
		
		update_image(inventory, camera);
		
		inventory_blocks.enabled = window.player.is_inventory;
		
		clear_image(inventory_blocks);
		
		for(int x = 0; x < 11; x++)
		{
			for(int y = 0; y < 9; y++)
			{
				float mx = (2.0f * Mouse.getX() / Display.getWidth()) - 1.0f;
				float my = (2.0f * Mouse.getY() / Display.getHeight()) - 1.0f;
				
				if(window.player.all_blocks[x][y] != null && window.player.is_inventory)
				{
					if(mx >= -(0.175f / ((float)Display.getWidth() / Display.getHeight()) * (5 - x)) - (0.115f / 2 / ((float)Display.getWidth() / Display.getHeight())) && my >= (0.175f * (-4 + y)) - 0.115f / 2 && mx <= (-(0.175f / ((float)Display.getWidth() / Display.getHeight()) * (5 - x)) - (0.115f / 2 / ((float)Display.getWidth() / Display.getHeight()))) + 0.115f / ((float)Display.getWidth() / Display.getHeight()) && my <= ((0.175f * (-4 + y)) - 0.115f / 2) + 0.115f)
					{
						inventory_blocks.vert_add(new Vector3f(-(0.175f / ((float)Display.getWidth() / Display.getHeight()) * (5 - x)) - (0.145f / 2 / ((float)Display.getWidth() / Display.getHeight())), (0.175f * (-4 + y)) - 0.145f / 2, 0.0f));
						inventory_blocks.vert_add(new Vector3f((-(0.175f / ((float)Display.getWidth() / Display.getHeight()) * (5 - x)) - (0.145f / 2 / ((float)Display.getWidth() / Display.getHeight()))) + (0.145f / ((float)Display.getWidth() / Display.getHeight())), (0.175f * (-4 + y)) - 0.145f / 2, 0.0f));
						inventory_blocks.vert_add(new Vector3f((-(0.175f / ((float)Display.getWidth() / Display.getHeight()) * (5 - x)) - (0.145f / 2 / ((float)Display.getWidth() / Display.getHeight()))) + (0.145f / ((float)Display.getWidth() / Display.getHeight())), ((0.175f * (-4 + y)) - 0.145f / 2) + (0.145f), 0.0f));
						inventory_blocks.vert_add(new Vector3f(-(0.175f / ((float)Display.getWidth() / Display.getHeight()) * (5 - x)) - (0.145f / 2 / ((float)Display.getWidth() / Display.getHeight())), ((0.175f * (-4 + y)) - 0.145f / 2) + (0.145f), 0.0f));
						
						float ot = 0.0625f;
						
						inventory_blocks.t_coord_add(new Vector2f(window.player.all_blocks[x][y].tex_coords[2].x * ot, window.player.all_blocks[x][y].tex_coords[2].y * ot + ot));
						inventory_blocks.t_coord_add(new Vector2f(window.player.all_blocks[x][y].tex_coords[2].x * ot + ot, window.player.all_blocks[x][y].tex_coords[2].y * ot + ot));
						inventory_blocks.t_coord_add(new Vector2f(window.player.all_blocks[x][y].tex_coords[2].x * ot + ot, window.player.all_blocks[x][y].tex_coords[2].y * ot));
						inventory_blocks.t_coord_add(new Vector2f(window.player.all_blocks[x][y].tex_coords[2].x * ot, window.player.all_blocks[x][y].tex_coords[2].y * ot));
						
						inventory_blocks.add_col(new Vector4f(1, 1, 1, 1));
						
						inventory_blocks.add_ind();
						
						if(Mouse.isButtonDown(0))
						{
							Block b1 = window.player.blocks[window.player.blck];
							Block b2 = window.player.all_blocks[x][y];
							
							for(int o = 0; o < 9; o++)
							{
								if(window.player.blocks[o] == b2)
								{
									window.player.blocks[o] = b1;
									break;
								}
							}
							
							window.player.blocks[window.player.blck] = window.player.all_blocks[x][y];
						}
					}
					
					else
					{
						inventory_blocks.vert_add(new Vector3f(-(0.175f / ((float)Display.getWidth() / Display.getHeight()) * (5 - x)) - (0.115f / 2 / ((float)Display.getWidth() / Display.getHeight())), (0.175f * (-4 + y)) - 0.115f / 2, 0.0f));
						inventory_blocks.vert_add(new Vector3f((-(0.175f / ((float)Display.getWidth() / Display.getHeight()) * (5 - x)) - (0.115f / 2 / ((float)Display.getWidth() / Display.getHeight()))) + (0.115f / ((float)Display.getWidth() / Display.getHeight())), (0.175f * (-4 + y)) - 0.115f / 2, 0.0f));
						inventory_blocks.vert_add(new Vector3f((-(0.175f / ((float)Display.getWidth() / Display.getHeight()) * (5 - x)) - (0.115f / 2 / ((float)Display.getWidth() / Display.getHeight()))) + (0.115f / ((float)Display.getWidth() / Display.getHeight())), ((0.175f * (-4 + y)) - 0.115f / 2) + (0.115f), 0.0f));
						inventory_blocks.vert_add(new Vector3f(-(0.175f / ((float)Display.getWidth() / Display.getHeight()) * (5 - x)) - (0.115f / 2 / ((float)Display.getWidth() / Display.getHeight())), ((0.175f * (-4 + y)) - 0.115f / 2) + (0.115f), 0.0f));
						
						float ot = 0.0625f;
						
						inventory_blocks.t_coord_add(new Vector2f(window.player.all_blocks[x][y].tex_coords[2].x * ot, window.player.all_blocks[x][y].tex_coords[2].y * ot + ot));
						inventory_blocks.t_coord_add(new Vector2f(window.player.all_blocks[x][y].tex_coords[2].x * ot + ot, window.player.all_blocks[x][y].tex_coords[2].y * ot + ot));
						inventory_blocks.t_coord_add(new Vector2f(window.player.all_blocks[x][y].tex_coords[2].x * ot + ot, window.player.all_blocks[x][y].tex_coords[2].y * ot));
						inventory_blocks.t_coord_add(new Vector2f(window.player.all_blocks[x][y].tex_coords[2].x * ot, window.player.all_blocks[x][y].tex_coords[2].y * ot));
						
						inventory_blocks.add_col(new Vector4f(1, 1, 1, 1));
						
						inventory_blocks.add_ind();
					}
				}
			}
		}
		
		inventory_blocks.convert();
		
		update_image(inventory_blocks, camera);
		
		float ls = 256.0f;
		
		int X = 0;
		int Y = 0;
		
		clear_image(mm_background);
		
		mm_background.enabled = window.player.is_mm;
		
		mm_background.vert_add(new Vector3f(0 - (ls / ((float)Display.getWidth() / (float)Display.getHeight())) + ((X * ls) / ((float)Display.getWidth() / (float)Display.getHeight())), 0 - ls + (Y * ls), 0.0f));
		mm_background.vert_add(new Vector3f(0 + (ls / ((float)Display.getWidth() / (float)Display.getHeight())) + ((X * ls) / ((float)Display.getWidth() / (float)Display.getHeight())), 0 - ls + (Y * ls), 0.0f));
		mm_background.vert_add(new Vector3f(0 + (ls / ((float)Display.getWidth() / (float)Display.getHeight())) + ((X * ls) / ((float)Display.getWidth() / (float)Display.getHeight())), 0 + ls + (Y * ls), 0.0f));
		mm_background.vert_add(new Vector3f(0 - (ls / ((float)Display.getWidth() / (float)Display.getHeight())) + ((X * ls) / ((float)Display.getWidth() / (float)Display.getHeight())), 0 + ls + (Y * ls), 0.0f));

		mm_background.t_coord_add(new Vector2f(0 + mmb_offset, 4096));
		mm_background.t_coord_add(new Vector2f(4096 + mmb_offset, 4096));
		mm_background.t_coord_add(new Vector2f(4096 + mmb_offset, 0));
		mm_background.t_coord_add(new Vector2f(0 + mmb_offset, 0));
		
		mm_background.add_col(new Vector4f(0.5f, 0.5f, 0.5f, 1.0f));
		
		mm_background.add_ind();
		
		mm_background.convert();
		
		update_image(mm_background, camera);
		
		clear_image(buttons);
		
		float bs = 1.0f / 10.0f;
		
		if(window.player.is_pause)
		{
			if(window.player.is_gw_menu)
			{
				float mx = (2.0f * Mouse.getX() / Display.getWidth()) - 1.0f;
				float my = (2.0f * Mouse.getY() / Display.getHeight()) - 1.0f;
				
				buttons.vert_add(new Vector3f((0 - bs) / ((float)Display.getWidth() / (float)Display.getHeight()) - (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()), (-3 * bs) - bs, 0.0f));
				buttons.vert_add(new Vector3f((0 + bs) / ((float)Display.getWidth() / (float)Display.getHeight()) + (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()), (-3 * bs) - bs, 0.0f));
				buttons.vert_add(new Vector3f((0 + bs) / ((float)Display.getWidth() / (float)Display.getHeight()) + (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()), (-3 * bs) + bs, 0.0f));
				buttons.vert_add(new Vector3f((0 - bs) / ((float)Display.getWidth() / (float)Display.getHeight()) - (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()), (-3 * bs) + bs, 0.0f));
				
				if(mx >= (0 - bs) / ((float)Display.getWidth() / (float)Display.getHeight()) - (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()) && mx <= (0 + bs) / ((float)Display.getWidth() / (float)Display.getHeight()) + (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()) && my >= (-3 * bs) - bs && my <= (-3 * bs) + bs)
				{
					if(!is_sound[0])
					{
						is_sound[0] = true;
						
						Audio audio = new Audio(ResourcesMC.select, true, false, 1.0f, 0.25f, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
						audio.update(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
						audio.play();
						
						window.sounds.add(audio);
					}
					
					if(Mouse.isButtonDown(0))
					{
						buttons.t_coord_add(new Vector2f(0, 2 * (1.0f / 3.0f) + (1.0f / 3.0f)));
						buttons.t_coord_add(new Vector2f(1, 2 * (1.0f / 3.0f) + (1.0f / 3.0f)));
						buttons.t_coord_add(new Vector2f(1, 2 * (1.0f / 3.0f)));
						buttons.t_coord_add(new Vector2f(0, 2 * (1.0f / 3.0f)));
					}
					
					else
					{
						buttons.t_coord_add(new Vector2f(0, 1 * (1.0f / 3.0f) + (1.0f / 3.0f)));
						buttons.t_coord_add(new Vector2f(1, 1 * (1.0f / 3.0f) + (1.0f / 3.0f)));
						buttons.t_coord_add(new Vector2f(1, 1 * (1.0f / 3.0f)));
						buttons.t_coord_add(new Vector2f(0, 1 * (1.0f / 3.0f)));
					}
					
					if(is_click)
					{
						Audio audio = new Audio(ResourcesMC.click, true, false, 1.0f, 0.25f, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
						audio.update(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
						audio.play();
						
						window.sounds.add(audio);
						
						window.player.is_gw_menu = false;
						
						window.world.create_world();
					}
				}
				
				else
				{
					is_sound[0] = false;
					
					buttons.t_coord_add(new Vector2f(0, 0 * (1.0f / 3.0f) + (1.0f / 3.0f)));
					buttons.t_coord_add(new Vector2f(1, 0 * (1.0f / 3.0f) + (1.0f / 3.0f)));
					buttons.t_coord_add(new Vector2f(1, 0 * (1.0f / 3.0f)));
					buttons.t_coord_add(new Vector2f(0, 0 * (1.0f / 3.0f)));
				}
				
				buttons.add_col(new Vector4f(1, 1, 1, 1));
				
				buttons.add_ind();
				
				buttons.vert_add(new Vector3f((0 - bs) / ((float)Display.getWidth() / (float)Display.getHeight()) - (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()), (3 * bs) - bs, 0.0f));
				buttons.vert_add(new Vector3f((0 + bs) / ((float)Display.getWidth() / (float)Display.getHeight()) + (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()), (3 * bs) - bs, 0.0f));
				buttons.vert_add(new Vector3f((0 + bs) / ((float)Display.getWidth() / (float)Display.getHeight()) + (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()), (3 * bs) + bs, 0.0f));
				buttons.vert_add(new Vector3f((0 - bs) / ((float)Display.getWidth() / (float)Display.getHeight()) - (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()), (3 * bs) + bs, 0.0f));

				if(mx >= (0 - bs) / ((float)Display.getWidth() / (float)Display.getHeight()) - (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()) && mx <= (0 + bs) / ((float)Display.getWidth() / (float)Display.getHeight()) + (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()) && my >= (3 * bs) - bs && my <= (3 * bs) + bs)
				{
					if(!is_sound[1])
					{
						is_sound[1] = true;
						
						Audio audio = new Audio(ResourcesMC.select, true, false, 1.0f, 0.25f, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
						audio.update(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
						audio.play();
						
						window.sounds.add(audio);
					}
					
					if(Mouse.isButtonDown(0))
					{
						buttons.t_coord_add(new Vector2f(0, 2 * (1.0f / 3.0f) + (1.0f / 3.0f)));
						buttons.t_coord_add(new Vector2f(1, 2 * (1.0f / 3.0f) + (1.0f / 3.0f)));
						buttons.t_coord_add(new Vector2f(1, 2 * (1.0f / 3.0f)));
						buttons.t_coord_add(new Vector2f(0, 2 * (1.0f / 3.0f)));
					}
					
					else
					{
						buttons.t_coord_add(new Vector2f(0, 1 * (1.0f / 3.0f) + (1.0f / 3.0f)));
						buttons.t_coord_add(new Vector2f(1, 1 * (1.0f / 3.0f) + (1.0f / 3.0f)));
						buttons.t_coord_add(new Vector2f(1, 1 * (1.0f / 3.0f)));
						buttons.t_coord_add(new Vector2f(0, 1 * (1.0f / 3.0f)));
					}
					
					if(is_click)
					{
						Audio audio = new Audio(ResourcesMC.click, true, false, 1.0f, 0.25f, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
						audio.update(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
						audio.play();
						
						window.sounds.add(audio);
						
						if(window.world.world_size == "TINY")
						{
							window.world.world_size = "SMALL";
						}
						
						else if(window.world.world_size == "SMALL")
						{
							window.world.world_size = "MEDIUM";
						}
						
						else if(window.world.world_size == "MEDIUM")
						{
							window.world.world_size = "HUGE";
						}
						
						else if(window.world.world_size == "HUGE")
						{
							window.world.world_size = "GIANT";
						}
						
						else if(window.world.world_size == "GIANT")
						{
							window.world.world_size = "TINY";
						}
					}
				}
				
				else
				{
					is_sound[1] = false;
					
					buttons.t_coord_add(new Vector2f(0, 0 * (1.0f / 3.0f) + (1.0f / 3.0f)));
					buttons.t_coord_add(new Vector2f(1, 0 * (1.0f / 3.0f) + (1.0f / 3.0f)));
					buttons.t_coord_add(new Vector2f(1, 0 * (1.0f / 3.0f)));
					buttons.t_coord_add(new Vector2f(0, 0 * (1.0f / 3.0f)));
				}
				
				buttons.add_col(new Vector4f(1, 1, 1, 1));
				
				buttons.add_ind();
				
				buttons.vert_add(new Vector3f((0 - bs) / ((float)Display.getWidth() / (float)Display.getHeight()) - (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()), 0 - bs, 0.0f));
				buttons.vert_add(new Vector3f((0 + bs) / ((float)Display.getWidth() / (float)Display.getHeight()) + (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()), 0 - bs, 0.0f));
				buttons.vert_add(new Vector3f((0 + bs) / ((float)Display.getWidth() / (float)Display.getHeight()) + (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()), 0 + bs, 0.0f));
				buttons.vert_add(new Vector3f((0 - bs) / ((float)Display.getWidth() / (float)Display.getHeight()) - (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()), 0 + bs, 0.0f));

				if(mx >= (0 - bs) / ((float)Display.getWidth() / (float)Display.getHeight()) - (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()) && mx <= (0 + bs) / ((float)Display.getWidth() / (float)Display.getHeight()) + (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()) && my >= 0 - bs && my <= 0 + bs)
				{
					if(!is_sound[2])
					{
						is_sound[2] = true;
						
						Audio audio = new Audio(ResourcesMC.select, true, false, 1.0f, 0.25f, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
						audio.update(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
						audio.play();
						
						window.sounds.add(audio);
					}
					
					if(Mouse.isButtonDown(0))
					{
						buttons.t_coord_add(new Vector2f(0, 2 * (1.0f / 3.0f) + (1.0f / 3.0f)));
						buttons.t_coord_add(new Vector2f(1, 2 * (1.0f / 3.0f) + (1.0f / 3.0f)));
						buttons.t_coord_add(new Vector2f(1, 2 * (1.0f / 3.0f)));
						buttons.t_coord_add(new Vector2f(0, 2 * (1.0f / 3.0f)));
					}
					
					else
					{
						buttons.t_coord_add(new Vector2f(0, 1 * (1.0f / 3.0f) + (1.0f / 3.0f)));
						buttons.t_coord_add(new Vector2f(1, 1 * (1.0f / 3.0f) + (1.0f / 3.0f)));
						buttons.t_coord_add(new Vector2f(1, 1 * (1.0f / 3.0f)));
						buttons.t_coord_add(new Vector2f(0, 1 * (1.0f / 3.0f)));
					}
					
					if(is_click)
					{
						Audio audio = new Audio(ResourcesMC.click, true, false, 1.0f, 0.25f, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
						audio.update(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
						audio.play();
						
						window.sounds.add(audio);
						
						if(window.world.world_type == "DEFAULT")
						{
							window.world.world_type = "SUPERFLAT";
						}
						
						else if(window.world.world_type == "SUPERFLAT")
						{
							window.world.world_type = "ISLANDS";
						}
						
						else if(window.world.world_type == "ISLANDS")
						{
							window.world.world_type = "OCEAN";
						}
						
						else if(window.world.world_type == "OCEAN")
						{
							window.world.world_type = "CAVES";
						}
						
						else if(window.world.world_type == "CAVES")
						{
							window.world.world_type = "DEFAULT";
						}
					}
				}
				
				else
				{
					is_sound[2] = false;
					
					buttons.t_coord_add(new Vector2f(0, 0 * (1.0f / 3.0f) + (1.0f / 3.0f)));
					buttons.t_coord_add(new Vector2f(1, 0 * (1.0f / 3.0f) + (1.0f / 3.0f)));
					buttons.t_coord_add(new Vector2f(1, 0 * (1.0f / 3.0f)));
					buttons.t_coord_add(new Vector2f(0, 0 * (1.0f / 3.0f)));
				}
				
				buttons.add_col(new Vector4f(1, 1, 1, 1));
				
				buttons.add_ind();
				
				buttons.vert_add(new Vector3f((0 - bs) / ((float)Display.getWidth() / (float)Display.getHeight()) - (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()), (-6 * bs) - bs, 0.0f));
				buttons.vert_add(new Vector3f((0 + bs) / ((float)Display.getWidth() / (float)Display.getHeight()) + (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()), (-6 * bs) - bs, 0.0f));
				buttons.vert_add(new Vector3f((0 + bs) / ((float)Display.getWidth() / (float)Display.getHeight()) + (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()), (-6 * bs) + bs, 0.0f));
				buttons.vert_add(new Vector3f((0 - bs) / ((float)Display.getWidth() / (float)Display.getHeight()) - (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()), (-6 * bs) + bs, 0.0f));

				if(mx >= (0 - bs) / ((float)Display.getWidth() / (float)Display.getHeight()) - (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()) && mx <= (0 + bs) / ((float)Display.getWidth() / (float)Display.getHeight()) + (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()) && my >= (-6 * bs) - bs && my <= (-6 * bs) + bs)
				{
					if(!is_sound[3])
					{
						is_sound[3] = true;
						
						Audio audio = new Audio(ResourcesMC.select, true, false, 1.0f, 0.25f, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
						audio.update(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
						audio.play();
						
						window.sounds.add(audio);
					}
					
					if(Mouse.isButtonDown(0))
					{
						buttons.t_coord_add(new Vector2f(0, 2 * (1.0f / 3.0f) + (1.0f / 3.0f)));
						buttons.t_coord_add(new Vector2f(1, 2 * (1.0f / 3.0f) + (1.0f / 3.0f)));
						buttons.t_coord_add(new Vector2f(1, 2 * (1.0f / 3.0f)));
						buttons.t_coord_add(new Vector2f(0, 2 * (1.0f / 3.0f)));
					}
					
					else
					{
						buttons.t_coord_add(new Vector2f(0, 1 * (1.0f / 3.0f) + (1.0f / 3.0f)));
						buttons.t_coord_add(new Vector2f(1, 1 * (1.0f / 3.0f) + (1.0f / 3.0f)));
						buttons.t_coord_add(new Vector2f(1, 1 * (1.0f / 3.0f)));
						buttons.t_coord_add(new Vector2f(0, 1 * (1.0f / 3.0f)));
					}
					
					if(is_click)
					{
						Audio audio = new Audio(ResourcesMC.click, true, false, 1.0f, 0.25f, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
						audio.update(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
						audio.play();
						
						window.sounds.add(audio);
						
						window.player.is_gw_menu = false;
					}
				}
				
				else
				{
					is_sound[3] = false;
					
					buttons.t_coord_add(new Vector2f(0, 0 * (1.0f / 3.0f) + (1.0f / 3.0f)));
					buttons.t_coord_add(new Vector2f(1, 0 * (1.0f / 3.0f) + (1.0f / 3.0f)));
					buttons.t_coord_add(new Vector2f(1, 0 * (1.0f / 3.0f)));
					buttons.t_coord_add(new Vector2f(0, 0 * (1.0f / 3.0f)));
				}
				
				buttons.add_col(new Vector4f(1, 1, 1, 1));
				
				buttons.add_ind();
			}
			
			else
			{
				if(window.player.is_mm)
				{
					float mx = (2.0f * Mouse.getX() / Display.getWidth()) - 1.0f;
					float my = (2.0f * Mouse.getY() / Display.getHeight()) - 1.0f;
					
					buttons.vert_add(new Vector3f((0 - bs) / ((float)Display.getWidth() / (float)Display.getHeight()) - (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()), 0 - bs, 0.0f));
					buttons.vert_add(new Vector3f((0 + bs) / ((float)Display.getWidth() / (float)Display.getHeight()) + (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()), 0 - bs, 0.0f));
					buttons.vert_add(new Vector3f((0 + bs) / ((float)Display.getWidth() / (float)Display.getHeight()) + (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()), 0 + bs, 0.0f));
					buttons.vert_add(new Vector3f((0 - bs) / ((float)Display.getWidth() / (float)Display.getHeight()) - (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()), 0 + bs, 0.0f));
					
					if(mx >= (0 - bs) / ((float)Display.getWidth() / (float)Display.getHeight()) - (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()) && mx <= (0 + bs) / ((float)Display.getWidth() / (float)Display.getHeight()) + (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()) && my >= 0 - bs && my <= 0 + bs)
					{
						if(!is_sound[4])
						{
							is_sound[4] = true;
							
							Audio audio = new Audio(ResourcesMC.select, true, false, 1.0f, 0.25f, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
							audio.update(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
							audio.play();
							
							window.sounds.add(audio);
						}
						
						if(Mouse.isButtonDown(0))
						{
							buttons.t_coord_add(new Vector2f(0, 2 * (1.0f / 3.0f) + (1.0f / 3.0f)));
							buttons.t_coord_add(new Vector2f(1, 2 * (1.0f / 3.0f) + (1.0f / 3.0f)));
							buttons.t_coord_add(new Vector2f(1, 2 * (1.0f / 3.0f)));
							buttons.t_coord_add(new Vector2f(0, 2 * (1.0f / 3.0f)));
						}
						
						else
						{
							buttons.t_coord_add(new Vector2f(0, 1 * (1.0f / 3.0f) + (1.0f / 3.0f)));
							buttons.t_coord_add(new Vector2f(1, 1 * (1.0f / 3.0f) + (1.0f / 3.0f)));
							buttons.t_coord_add(new Vector2f(1, 1 * (1.0f / 3.0f)));
							buttons.t_coord_add(new Vector2f(0, 1 * (1.0f / 3.0f)));
						}
						
						if(is_click)
						{
							Audio audio = new Audio(ResourcesMC.click, true, false, 1.0f, 0.25f, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
							audio.update(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
							audio.play();
							
							window.sounds.add(audio);
							
							window.player.is_gw_menu = false;
							
							try
							{
								window.world.load();
							}
							
							catch (Exception e)
							{
								e.printStackTrace();
							}
						}
					}
					
					else
					{
						is_sound[4] = false;
						
						buttons.t_coord_add(new Vector2f(0, 0 * (1.0f / 3.0f) + (1.0f / 3.0f)));
						buttons.t_coord_add(new Vector2f(1, 0 * (1.0f / 3.0f) + (1.0f / 3.0f)));
						buttons.t_coord_add(new Vector2f(1, 0 * (1.0f / 3.0f)));
						buttons.t_coord_add(new Vector2f(0, 0 * (1.0f / 3.0f)));
					}
					
					buttons.add_col(new Vector4f(1, 1, 1, 1));
					
					buttons.add_ind();
					
					buttons.vert_add(new Vector3f((0 - bs) / ((float)Display.getWidth() / (float)Display.getHeight()) - (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()), (3 * bs) - bs, 0.0f));
					buttons.vert_add(new Vector3f((0 + bs) / ((float)Display.getWidth() / (float)Display.getHeight()) + (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()), (3 * bs) - bs, 0.0f));
					buttons.vert_add(new Vector3f((0 + bs) / ((float)Display.getWidth() / (float)Display.getHeight()) + (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()), (3 * bs) + bs, 0.0f));
					buttons.vert_add(new Vector3f((0 - bs) / ((float)Display.getWidth() / (float)Display.getHeight()) - (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()), (3 * bs) + bs, 0.0f));

					if(mx >= (0 - bs) / ((float)Display.getWidth() / (float)Display.getHeight()) - (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()) && mx <= (0 + bs) / ((float)Display.getWidth() / (float)Display.getHeight()) + (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()) && my >= (3 * bs) - bs && my <= (3 * bs) + bs)
					{
						if(!is_sound[5])
						{
							is_sound[5] = true;
							
							Audio audio = new Audio(ResourcesMC.select, true, false, 1.0f, 0.25f, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
							audio.update(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
							audio.play();
							
							window.sounds.add(audio);
						}
						
						if(Mouse.isButtonDown(0))
						{
							buttons.t_coord_add(new Vector2f(0, 2 * (1.0f / 3.0f) + (1.0f / 3.0f)));
							buttons.t_coord_add(new Vector2f(1, 2 * (1.0f / 3.0f) + (1.0f / 3.0f)));
							buttons.t_coord_add(new Vector2f(1, 2 * (1.0f / 3.0f)));
							buttons.t_coord_add(new Vector2f(0, 2 * (1.0f / 3.0f)));
						}
						
						else
						{
							buttons.t_coord_add(new Vector2f(0, 1 * (1.0f / 3.0f) + (1.0f / 3.0f)));
							buttons.t_coord_add(new Vector2f(1, 1 * (1.0f / 3.0f) + (1.0f / 3.0f)));
							buttons.t_coord_add(new Vector2f(1, 1 * (1.0f / 3.0f)));
							buttons.t_coord_add(new Vector2f(0, 1 * (1.0f / 3.0f)));
						}
						
						if(is_click)
						{
							Audio audio = new Audio(ResourcesMC.click, true, false, 1.0f, 0.25f, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
							audio.update(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
							audio.play();
							
							window.sounds.add(audio);
							
							window.player.is_gw_menu = true;
						}
					}
					
					else
					{
						is_sound[5] = false;
						
						buttons.t_coord_add(new Vector2f(0, 0 * (1.0f / 3.0f) + (1.0f / 3.0f)));
						buttons.t_coord_add(new Vector2f(1, 0 * (1.0f / 3.0f) + (1.0f / 3.0f)));
						buttons.t_coord_add(new Vector2f(1, 0 * (1.0f / 3.0f)));
						buttons.t_coord_add(new Vector2f(0, 0 * (1.0f / 3.0f)));
					}
					
					buttons.add_col(new Vector4f(1, 1, 1, 1));
					
					buttons.add_ind();
					
					buttons.vert_add(new Vector3f((0 - bs) / ((float)Display.getWidth() / (float)Display.getHeight()) - (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()), (-3 * bs) - bs, 0.0f));
					buttons.vert_add(new Vector3f((0 + bs) / ((float)Display.getWidth() / (float)Display.getHeight()) + (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()), (-3 * bs) - bs, 0.0f));
					buttons.vert_add(new Vector3f((0 + bs) / ((float)Display.getWidth() / (float)Display.getHeight()) + (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()), (-3 * bs) + bs, 0.0f));
					buttons.vert_add(new Vector3f((0 - bs) / ((float)Display.getWidth() / (float)Display.getHeight()) - (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()), (-3 * bs) + bs, 0.0f));

					if(mx >= (0 - bs) / ((float)Display.getWidth() / (float)Display.getHeight()) - (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()) && mx <= (0 + bs) / ((float)Display.getWidth() / (float)Display.getHeight()) + (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()) && my >= (-3 * bs) - bs && my <= (-3 * bs) + bs)
					{
						if(!is_sound[6])
						{
							is_sound[6] = true;
							
							Audio audio = new Audio(ResourcesMC.select, true, false, 1.0f, 0.25f, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
							audio.update(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
							audio.play();
							
							window.sounds.add(audio);
						}
						
						if(Mouse.isButtonDown(0))
						{
							buttons.t_coord_add(new Vector2f(0, 2 * (1.0f / 3.0f) + (1.0f / 3.0f)));
							buttons.t_coord_add(new Vector2f(1, 2 * (1.0f / 3.0f) + (1.0f / 3.0f)));
							buttons.t_coord_add(new Vector2f(1, 2 * (1.0f / 3.0f)));
							buttons.t_coord_add(new Vector2f(0, 2 * (1.0f / 3.0f)));
						}
						
						else
						{
							buttons.t_coord_add(new Vector2f(0, 1 * (1.0f / 3.0f) + (1.0f / 3.0f)));
							buttons.t_coord_add(new Vector2f(1, 1 * (1.0f / 3.0f) + (1.0f / 3.0f)));
							buttons.t_coord_add(new Vector2f(1, 1 * (1.0f / 3.0f)));
							buttons.t_coord_add(new Vector2f(0, 1 * (1.0f / 3.0f)));
						}
						
						if(is_click)
						{
							Audio audio = new Audio(ResourcesMC.click, true, false, 1.0f, 0.25f, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
							audio.update(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
							audio.play();
							
							window.sounds.add(audio);
							
							window.is_exit = true;
						}
					}
					
					else
					{
						is_sound[6] = false;
						
						buttons.t_coord_add(new Vector2f(0, 0 * (1.0f / 3.0f) + (1.0f / 3.0f)));
						buttons.t_coord_add(new Vector2f(1, 0 * (1.0f / 3.0f) + (1.0f / 3.0f)));
						buttons.t_coord_add(new Vector2f(1, 0 * (1.0f / 3.0f)));
						buttons.t_coord_add(new Vector2f(0, 0 * (1.0f / 3.0f)));
					}
					
					buttons.add_col(new Vector4f(1, 1, 1, 1));
					
					buttons.add_ind();
				}
				
				else
				{
					float mx = (2.0f * Mouse.getX() / Display.getWidth()) - 1.0f;
					float my = (2.0f * Mouse.getY() / Display.getHeight()) - 1.0f;
					
					buttons.vert_add(new Vector3f((0 - bs) / ((float)Display.getWidth() / (float)Display.getHeight()) - (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()), 0 - bs, 0.0f));
					buttons.vert_add(new Vector3f((0 + bs) / ((float)Display.getWidth() / (float)Display.getHeight()) + (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()), 0 - bs, 0.0f));
					buttons.vert_add(new Vector3f((0 + bs) / ((float)Display.getWidth() / (float)Display.getHeight()) + (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()), 0 + bs, 0.0f));
					buttons.vert_add(new Vector3f((0 - bs) / ((float)Display.getWidth() / (float)Display.getHeight()) - (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()), 0 + bs, 0.0f));
					
					if(mx >= (0 - bs) / ((float)Display.getWidth() / (float)Display.getHeight()) - (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()) && mx <= (0 + bs) / ((float)Display.getWidth() / (float)Display.getHeight()) + (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()) && my >= 0 - bs && my <= 0 + bs)
					{
						if(!is_sound[7])
						{
							is_sound[7] = true;
							
							Audio audio = new Audio(ResourcesMC.select, true, false, 1.0f, 0.25f, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
							audio.update(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
							audio.play();
							
							window.sounds.add(audio);
						}
						
						if(Mouse.isButtonDown(0))
						{
							buttons.t_coord_add(new Vector2f(0, 2 * (1.0f / 3.0f) + (1.0f / 3.0f)));
							buttons.t_coord_add(new Vector2f(1, 2 * (1.0f / 3.0f) + (1.0f / 3.0f)));
							buttons.t_coord_add(new Vector2f(1, 2 * (1.0f / 3.0f)));
							buttons.t_coord_add(new Vector2f(0, 2 * (1.0f / 3.0f)));
						}
						
						else
						{
							buttons.t_coord_add(new Vector2f(0, 1 * (1.0f / 3.0f) + (1.0f / 3.0f)));
							buttons.t_coord_add(new Vector2f(1, 1 * (1.0f / 3.0f) + (1.0f / 3.0f)));
							buttons.t_coord_add(new Vector2f(1, 1 * (1.0f / 3.0f)));
							buttons.t_coord_add(new Vector2f(0, 1 * (1.0f / 3.0f)));
						}
						
						if(is_click)
						{
							Audio audio = new Audio(ResourcesMC.click, true, false, 1.0f, 0.25f, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
							audio.update(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
							audio.play();
							
							window.sounds.add(audio);
							
							window.player.is_gw_menu = false;
							
							try
							{
								window.world.save();
							}
							
							catch (Exception e)
							{
								e.printStackTrace();
							}
						}
					}
					
					else
					{
						is_sound[7] = false;
						
						buttons.t_coord_add(new Vector2f(0, 0 * (1.0f / 3.0f) + (1.0f / 3.0f)));
						buttons.t_coord_add(new Vector2f(1, 0 * (1.0f / 3.0f) + (1.0f / 3.0f)));
						buttons.t_coord_add(new Vector2f(1, 0 * (1.0f / 3.0f)));
						buttons.t_coord_add(new Vector2f(0, 0 * (1.0f / 3.0f)));
					}
					
					buttons.add_col(new Vector4f(1, 1, 1, 1));
					
					buttons.add_ind();
					
					buttons.vert_add(new Vector3f((0 - bs) / ((float)Display.getWidth() / (float)Display.getHeight()) - (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()), (3 * bs) - bs, 0.0f));
					buttons.vert_add(new Vector3f((0 + bs) / ((float)Display.getWidth() / (float)Display.getHeight()) + (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()), (3 * bs) - bs, 0.0f));
					buttons.vert_add(new Vector3f((0 + bs) / ((float)Display.getWidth() / (float)Display.getHeight()) + (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()), (3 * bs) + bs, 0.0f));
					buttons.vert_add(new Vector3f((0 - bs) / ((float)Display.getWidth() / (float)Display.getHeight()) - (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()), (3 * bs) + bs, 0.0f));

					if(mx >= (0 - bs) / ((float)Display.getWidth() / (float)Display.getHeight()) - (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()) && mx <= (0 + bs) / ((float)Display.getWidth() / (float)Display.getHeight()) + (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()) && my >= (3 * bs) - bs && my <= (3 * bs) + bs)
					{
						if(!is_sound[8])
						{
							is_sound[8] = true;
							
							Audio audio = new Audio(ResourcesMC.select, true, false, 1.0f, 0.25f, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
							audio.update(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
							audio.play();
							
							window.sounds.add(audio);
						}
						
						if(Mouse.isButtonDown(0))
						{
							buttons.t_coord_add(new Vector2f(0, 2 * (1.0f / 3.0f) + (1.0f / 3.0f)));
							buttons.t_coord_add(new Vector2f(1, 2 * (1.0f / 3.0f) + (1.0f / 3.0f)));
							buttons.t_coord_add(new Vector2f(1, 2 * (1.0f / 3.0f)));
							buttons.t_coord_add(new Vector2f(0, 2 * (1.0f / 3.0f)));
						}
						
						else
						{
							buttons.t_coord_add(new Vector2f(0, 1 * (1.0f / 3.0f) + (1.0f / 3.0f)));
							buttons.t_coord_add(new Vector2f(1, 1 * (1.0f / 3.0f) + (1.0f / 3.0f)));
							buttons.t_coord_add(new Vector2f(1, 1 * (1.0f / 3.0f)));
							buttons.t_coord_add(new Vector2f(0, 1 * (1.0f / 3.0f)));
						}
						
						if(is_click)
						{
							Audio audio = new Audio(ResourcesMC.click, true, false, 1.0f, 0.25f, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
							audio.update(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
							audio.play();
							
							window.sounds.add(audio);
							
							window.player.is_gw_menu = true;
						}
					}
					
					else
					{
						is_sound[8] = false;
						
						buttons.t_coord_add(new Vector2f(0, 0 * (1.0f / 3.0f) + (1.0f / 3.0f)));
						buttons.t_coord_add(new Vector2f(1, 0 * (1.0f / 3.0f) + (1.0f / 3.0f)));
						buttons.t_coord_add(new Vector2f(1, 0 * (1.0f / 3.0f)));
						buttons.t_coord_add(new Vector2f(0, 0 * (1.0f / 3.0f)));
					}
					
					buttons.add_col(new Vector4f(1, 1, 1, 1));
					
					buttons.add_ind();
					
					buttons.vert_add(new Vector3f((0 - bs) / ((float)Display.getWidth() / (float)Display.getHeight()) - (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()), (6 * bs) - bs, 0.0f));
					buttons.vert_add(new Vector3f((0 + bs) / ((float)Display.getWidth() / (float)Display.getHeight()) + (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()), (6 * bs) - bs, 0.0f));
					buttons.vert_add(new Vector3f((0 + bs) / ((float)Display.getWidth() / (float)Display.getHeight()) + (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()), (6 * bs) + bs, 0.0f));
					buttons.vert_add(new Vector3f((0 - bs) / ((float)Display.getWidth() / (float)Display.getHeight()) - (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()), (6 * bs) + bs, 0.0f));

					if(mx >= (0 - bs) / ((float)Display.getWidth() / (float)Display.getHeight()) - (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()) && mx <= (0 + bs) / ((float)Display.getWidth() / (float)Display.getHeight()) + (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()) && my >= (6 * bs) - bs && my <= (6 * bs) + bs)
					{
						if(!is_sound[9])
						{
							is_sound[9] = true;
							
							Audio audio = new Audio(ResourcesMC.select, true, false, 1.0f, 0.25f, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
							audio.update(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
							audio.play();
							
							window.sounds.add(audio);
						}
						
						if(Mouse.isButtonDown(0))
						{
							buttons.t_coord_add(new Vector2f(0, 2 * (1.0f / 3.0f) + (1.0f / 3.0f)));
							buttons.t_coord_add(new Vector2f(1, 2 * (1.0f / 3.0f) + (1.0f / 3.0f)));
							buttons.t_coord_add(new Vector2f(1, 2 * (1.0f / 3.0f)));
							buttons.t_coord_add(new Vector2f(0, 2 * (1.0f / 3.0f)));
						}
						
						else
						{
							buttons.t_coord_add(new Vector2f(0, 1 * (1.0f / 3.0f) + (1.0f / 3.0f)));
							buttons.t_coord_add(new Vector2f(1, 1 * (1.0f / 3.0f) + (1.0f / 3.0f)));
							buttons.t_coord_add(new Vector2f(1, 1 * (1.0f / 3.0f)));
							buttons.t_coord_add(new Vector2f(0, 1 * (1.0f / 3.0f)));
						}
						
						if(is_click)
						{
							Audio audio = new Audio(ResourcesMC.click, true, false, 1.0f, 0.25f, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
							audio.update(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
							audio.play();
							
							window.sounds.add(audio);
							
							window.player.is_pause = false;
							window.player.is_gw_menu = false;
							Mouse.setGrabbed(true);
						}
					}
					
					else
					{
						is_sound[9] = false;
						
						buttons.t_coord_add(new Vector2f(0, 0 * (1.0f / 3.0f) + (1.0f / 3.0f)));
						buttons.t_coord_add(new Vector2f(1, 0 * (1.0f / 3.0f) + (1.0f / 3.0f)));
						buttons.t_coord_add(new Vector2f(1, 0 * (1.0f / 3.0f)));
						buttons.t_coord_add(new Vector2f(0, 0 * (1.0f / 3.0f)));
					}
					
					buttons.add_col(new Vector4f(1, 1, 1, 1));
					
					buttons.add_ind();
					
					buttons.vert_add(new Vector3f((0 - bs) / ((float)Display.getWidth() / (float)Display.getHeight()) - (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()), (-3 * bs) - bs, 0.0f));
					buttons.vert_add(new Vector3f((0 + bs) / ((float)Display.getWidth() / (float)Display.getHeight()) + (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()), (-3 * bs) - bs, 0.0f));
					buttons.vert_add(new Vector3f((0 + bs) / ((float)Display.getWidth() / (float)Display.getHeight()) + (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()), (-3 * bs) + bs, 0.0f));
					buttons.vert_add(new Vector3f((0 - bs) / ((float)Display.getWidth() / (float)Display.getHeight()) - (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()), (-3 * bs) + bs, 0.0f));

					if(mx >= (0 - bs) / ((float)Display.getWidth() / (float)Display.getHeight()) - (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()) && mx <= (0 + bs) / ((float)Display.getWidth() / (float)Display.getHeight()) + (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()) && my >= (-3 * bs) - bs && my <= (-3 * bs) + bs)
					{
						if(!is_sound[10])
						{
							is_sound[10] = true;
							
							Audio audio = new Audio(ResourcesMC.select, true, false, 1.0f, 0.25f, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
							audio.update(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
							audio.play();
							
							window.sounds.add(audio);
						}
						
						if(Mouse.isButtonDown(0))
						{
							buttons.t_coord_add(new Vector2f(0, 2 * (1.0f / 3.0f) + (1.0f / 3.0f)));
							buttons.t_coord_add(new Vector2f(1, 2 * (1.0f / 3.0f) + (1.0f / 3.0f)));
							buttons.t_coord_add(new Vector2f(1, 2 * (1.0f / 3.0f)));
							buttons.t_coord_add(new Vector2f(0, 2 * (1.0f / 3.0f)));
						}
						
						else
						{
							buttons.t_coord_add(new Vector2f(0, 1 * (1.0f / 3.0f) + (1.0f / 3.0f)));
							buttons.t_coord_add(new Vector2f(1, 1 * (1.0f / 3.0f) + (1.0f / 3.0f)));
							buttons.t_coord_add(new Vector2f(1, 1 * (1.0f / 3.0f)));
							buttons.t_coord_add(new Vector2f(0, 1 * (1.0f / 3.0f)));
						}
						
						if(is_click)
						{
							Audio audio = new Audio(ResourcesMC.click, true, false, 1.0f, 0.25f, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
							audio.update(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
							audio.play();
							
							window.sounds.add(audio);
							
							window.player.is_gw_menu = false;
							
							try
							{
								window.world.load();
							}
							
							catch (Exception e)
							{
								e.printStackTrace();
							}
						}
					}
					
					else
					{
						is_sound[10] = false;
						
						buttons.t_coord_add(new Vector2f(0, 0 * (1.0f / 3.0f) + (1.0f / 3.0f)));
						buttons.t_coord_add(new Vector2f(1, 0 * (1.0f / 3.0f) + (1.0f / 3.0f)));
						buttons.t_coord_add(new Vector2f(1, 0 * (1.0f / 3.0f)));
						buttons.t_coord_add(new Vector2f(0, 0 * (1.0f / 3.0f)));
					}
					
					buttons.add_col(new Vector4f(1, 1, 1, 1));
					
					buttons.add_ind();
					
					buttons.vert_add(new Vector3f((0 - bs) / ((float)Display.getWidth() / (float)Display.getHeight()) - (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()), (-6 * bs) - bs, 0.0f));
					buttons.vert_add(new Vector3f((0 + bs) / ((float)Display.getWidth() / (float)Display.getHeight()) + (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()), (-6 * bs) - bs, 0.0f));
					buttons.vert_add(new Vector3f((0 + bs) / ((float)Display.getWidth() / (float)Display.getHeight()) + (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()), (-6 * bs) + bs, 0.0f));
					buttons.vert_add(new Vector3f((0 - bs) / ((float)Display.getWidth() / (float)Display.getHeight()) - (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()), (-6 * bs) + bs, 0.0f));

					if(mx >= (0 - bs) / ((float)Display.getWidth() / (float)Display.getHeight()) - (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()) && mx <= (0 + bs) / ((float)Display.getWidth() / (float)Display.getHeight()) + (4 * bs) / ((float)Display.getWidth() / (float)Display.getHeight()) && my >= (-6 * bs) - bs && my <= (-6 * bs) + bs)
					{
						if(!is_sound[11])
						{
							is_sound[11] = true;
							
							Audio audio = new Audio(ResourcesMC.select, true, false, 1.0f, 0.25f, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
							audio.update(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
							audio.play();
							
							window.sounds.add(audio);
						}
						
						if(Mouse.isButtonDown(0))
						{
							buttons.t_coord_add(new Vector2f(0, 2 * (1.0f / 3.0f) + (1.0f / 3.0f)));
							buttons.t_coord_add(new Vector2f(1, 2 * (1.0f / 3.0f) + (1.0f / 3.0f)));
							buttons.t_coord_add(new Vector2f(1, 2 * (1.0f / 3.0f)));
							buttons.t_coord_add(new Vector2f(0, 2 * (1.0f / 3.0f)));
						}
						
						else
						{
							buttons.t_coord_add(new Vector2f(0, 1 * (1.0f / 3.0f) + (1.0f / 3.0f)));
							buttons.t_coord_add(new Vector2f(1, 1 * (1.0f / 3.0f) + (1.0f / 3.0f)));
							buttons.t_coord_add(new Vector2f(1, 1 * (1.0f / 3.0f)));
							buttons.t_coord_add(new Vector2f(0, 1 * (1.0f / 3.0f)));
						}
						
						if(is_click)
						{
							Audio audio = new Audio(ResourcesMC.click, true, false, 1.0f, 0.25f, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
							audio.update(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
							audio.play();
							
							window.sounds.add(audio);
							
							window.player.is_gw_menu = false;
							
							try
							{
								window.world.generate_map();
							}
							
							catch (Exception e)
							{
								e.printStackTrace();
							}
						}
					}
					
					else
					{
						is_sound[11] = false;
						
						buttons.t_coord_add(new Vector2f(0, 0 * (1.0f / 3.0f) + (1.0f / 3.0f)));
						buttons.t_coord_add(new Vector2f(1, 0 * (1.0f / 3.0f) + (1.0f / 3.0f)));
						buttons.t_coord_add(new Vector2f(1, 0 * (1.0f / 3.0f)));
						buttons.t_coord_add(new Vector2f(0, 0 * (1.0f / 3.0f)));
					}
					
					buttons.add_col(new Vector4f(1, 1, 1, 1));
					
					buttons.add_ind();
				}
			}
		}
		
		buttons.convert();
		
		update_image(buttons, camera);
		
		clear_image(loading);
		
		loading.enabled = window.world.is_loading;
		
		loading.vert_add(new Vector3f(0 - (ls / ((float)Display.getWidth() / (float)Display.getHeight())) + ((X * ls) / ((float)Display.getWidth() / (float)Display.getHeight())), 0 - ls + (Y * ls), 0.0f));
		loading.vert_add(new Vector3f(0 + (ls / ((float)Display.getWidth() / (float)Display.getHeight())) + ((X * ls) / ((float)Display.getWidth() / (float)Display.getHeight())), 0 - ls + (Y * ls), 0.0f));
		loading.vert_add(new Vector3f(0 + (ls / ((float)Display.getWidth() / (float)Display.getHeight())) + ((X * ls) / ((float)Display.getWidth() / (float)Display.getHeight())), 0 + ls + (Y * ls), 0.0f));
		loading.vert_add(new Vector3f(0 - (ls / ((float)Display.getWidth() / (float)Display.getHeight())) + ((X * ls) / ((float)Display.getWidth() / (float)Display.getHeight())), 0 + ls + (Y * ls), 0.0f));

		loading.t_coord_add(new Vector2f(0, 4096));
		loading.t_coord_add(new Vector2f(4096, 4096));
		loading.t_coord_add(new Vector2f(4096, 0));
		loading.t_coord_add(new Vector2f(0, 0));
		
		loading.add_col(new Vector4f(0.5f, 0.5f, 0.5f, 1.0f));
		
		loading.add_ind();
		
		loading.convert();
		
		update_image(loading, camera);
		
		clear_text(text);
		
		clear_image(text_background);
		
		if(window.player.is_pause)
		{
			if(window.player.is_gw_menu)
			{
				float pt = 1.0f / 25.0f;
				
				String gtwd = "GENERATE WORLD!";
				
				for(int it = 0; it < gtwd.length(); it++)
				{
					text.vert_add(new Vector3f((0 - pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((gtwd.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (5 * bs) - pt, 0.0f));
					text.vert_add(new Vector3f((0 + pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((gtwd.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (5 * bs) - pt, 0.0f));
					text.vert_add(new Vector3f((0 + pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((gtwd.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (5 * bs) + pt, 0.0f));
					text.vert_add(new Vector3f((0 - pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((gtwd.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (5 * bs) + pt, 0.0f));
					
					text.render_char(gtwd.charAt(it), true);
					
					text.add_col(new Vector4f(0.0f, 0.0f, 0.0f, 1.0f));
					
					text.add_ind();
					
					text.vert_add(new Vector3f((0 - pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((gtwd.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (5 * bs) - pt, 0.0f));
					text.vert_add(new Vector3f((0 + pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((gtwd.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (5 * bs) - pt, 0.0f));
					text.vert_add(new Vector3f((0 + pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((gtwd.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (5 * bs) + pt, 0.0f));
					text.vert_add(new Vector3f((0 - pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((gtwd.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (5 * bs) + pt, 0.0f));
					
					text.render_char(gtwd.charAt(it), false);
					
					text.add_col(new Vector4f(0.95f, 0.95f, 0.95f, 1.0f));
					
					text.add_ind();
				}
				
				pt = 1.0f / 52.0f;
				
				String g = "Generate!";
				
				for(int it = 0; it < g.length(); it++)
				{
					text.vert_add(new Vector3f((0 - pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((g.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (-3 * bs) - pt, 0.0f));
					text.vert_add(new Vector3f((0 + pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((g.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (-3 * bs) - pt, 0.0f));
					text.vert_add(new Vector3f((0 + pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((g.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (-3 * bs) + pt, 0.0f));
					text.vert_add(new Vector3f((0 - pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((g.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (-3 * bs) + pt, 0.0f));
					
					text.render_char(g.charAt(it), true);
					
					text.add_col(new Vector4f(0.0f, 0.0f, 0.0f, 1.0f));
					
					text.add_ind();
					
					text.vert_add(new Vector3f((0 - pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((g.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (-3 * bs) - pt, 0.0f));
					text.vert_add(new Vector3f((0 + pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((g.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (-3 * bs) - pt, 0.0f));
					text.vert_add(new Vector3f((0 + pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((g.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (-3 * bs) + pt, 0.0f));
					text.vert_add(new Vector3f((0 - pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((g.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (-3 * bs) + pt, 0.0f));
					
					text.render_char(g.charAt(it), false);
					
					text.add_col(new Vector4f(0.95f, 0.95f, 0.95f, 1.0f));
					
					text.add_ind();
				}
				
				String ws = "World size: " + window.world.world_size;
				
				for(int it = 0; it < ws.length(); it++)
				{
					text.vert_add(new Vector3f((0 - pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((ws.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (3 * bs) - pt, 0.0f));
					text.vert_add(new Vector3f((0 + pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((ws.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (3 * bs) - pt, 0.0f));
					text.vert_add(new Vector3f((0 + pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((ws.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (3 * bs) + pt, 0.0f));
					text.vert_add(new Vector3f((0 - pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((ws.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (3 * bs) + pt, 0.0f));
					
					text.render_char(ws.charAt(it), true);
					
					text.add_col(new Vector4f(0.0f, 0.0f, 0.0f, 1.0f));
					
					text.add_ind();
					
					text.vert_add(new Vector3f((0 - pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((ws.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (3 * bs) - pt, 0.0f));
					text.vert_add(new Vector3f((0 + pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((ws.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (3 * bs) - pt, 0.0f));
					text.vert_add(new Vector3f((0 + pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((ws.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (3 * bs) + pt, 0.0f));
					text.vert_add(new Vector3f((0 - pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((ws.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (3 * bs) + pt, 0.0f));
					
					text.render_char(ws.charAt(it), false);
					
					text.add_col(new Vector4f(0.95f, 0.95f, 0.95f, 1.0f));
					
					text.add_ind();
				}
				
				String wt = "World type: " + window.world.world_type;
				
				for(int it = 0; it < wt.length(); it++)
				{
					text.vert_add(new Vector3f((0 - pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((wt.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), 0 - pt, 0.0f));
					text.vert_add(new Vector3f((0 + pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((wt.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), 0 - pt, 0.0f));
					text.vert_add(new Vector3f((0 + pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((wt.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), 0 + pt, 0.0f));
					text.vert_add(new Vector3f((0 - pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((wt.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), 0 + pt, 0.0f));
					
					text.render_char(wt.charAt(it), true);
					
					text.add_col(new Vector4f(0.0f, 0.0f, 0.0f, 1.0f));
					
					text.add_ind();
					
					text.vert_add(new Vector3f((0 - pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((wt.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), 0 - pt, 0.0f));
					text.vert_add(new Vector3f((0 + pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((wt.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), 0 - pt, 0.0f));
					text.vert_add(new Vector3f((0 + pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((wt.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), 0 + pt, 0.0f));
					text.vert_add(new Vector3f((0 - pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((wt.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), 0 + pt, 0.0f));
					
					text.render_char(wt.charAt(it), false);
					
					text.add_col(new Vector4f(0.95f, 0.95f, 0.95f, 1.0f));
					
					text.add_ind();
				}
				
				String b = "Back";
				
				for(int it = 0; it < b.length(); it++)
				{
					text.vert_add(new Vector3f((0 - pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((b.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (-6 * bs) - pt, 0.0f));
					text.vert_add(new Vector3f((0 + pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((b.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (-6 * bs) - pt, 0.0f));
					text.vert_add(new Vector3f((0 + pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((b.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (-6 * bs) + pt, 0.0f));
					text.vert_add(new Vector3f((0 - pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((b.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (-6 * bs) + pt, 0.0f));
					
					text.render_char(b.charAt(it), true);
					
					text.add_col(new Vector4f(0.0f, 0.0f, 0.0f, 1.0f));
					
					text.add_ind();
					
					text.vert_add(new Vector3f((0 - pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((b.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (-6 * bs) - pt, 0.0f));
					text.vert_add(new Vector3f((0 + pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((b.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (-6 * bs) - pt, 0.0f));
					text.vert_add(new Vector3f((0 + pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((b.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (-6 * bs) + pt, 0.0f));
					text.vert_add(new Vector3f((0 - pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((b.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (-6 * bs) + pt, 0.0f));
					
					text.render_char(b.charAt(it), false);
					
					text.add_col(new Vector4f(0.95f, 0.95f, 0.95f, 1.0f));
					
					text.add_ind();
				}
			}
			
			else
			{
				if(window.player.is_mm)
				{
					float pt = 1.0f / 52.0f;
					
					String sw = "Load world!";
					
					for(int it = 0; it < sw.length(); it++)
					{
						text.vert_add(new Vector3f((0 - pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((sw.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), 0 - pt, 0.0f));
						text.vert_add(new Vector3f((0 + pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((sw.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), 0 - pt, 0.0f));
						text.vert_add(new Vector3f((0 + pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((sw.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), 0 + pt, 0.0f));
						text.vert_add(new Vector3f((0 - pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((sw.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), 0 + pt, 0.0f));
						
						text.render_char(sw.charAt(it), true);
						
						text.add_col(new Vector4f(0.0f, 0.0f, 0.0f, 1.0f));
						
						text.add_ind();
						
						text.vert_add(new Vector3f((0 - pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((sw.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), 0 - pt, 0.0f));
						text.vert_add(new Vector3f((0 + pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((sw.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), 0 - pt, 0.0f));
						text.vert_add(new Vector3f((0 + pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((sw.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), 0 + pt, 0.0f));
						text.vert_add(new Vector3f((0 - pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((sw.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), 0 + pt, 0.0f));
						
						text.render_char(sw.charAt(it), false);
						
						text.add_col(new Vector4f(0.95f, 0.95f, 0.95f, 1.0f));
						
						text.add_ind();
					}
					
					String gw = "Generate world!";
					
					for(int it = 0; it < gw.length(); it++)
					{
						text.vert_add(new Vector3f((0 - pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((gw.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (3 * bs) - pt, 0.0f));
						text.vert_add(new Vector3f((0 + pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((gw.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (3 * bs) - pt, 0.0f));
						text.vert_add(new Vector3f((0 + pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((gw.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (3 * bs) + pt, 0.0f));
						text.vert_add(new Vector3f((0 - pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((gw.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (3 * bs) + pt, 0.0f));
						
						text.render_char(gw.charAt(it), true);
						
						text.add_col(new Vector4f(0.0f, 0.0f, 0.0f, 1.0f));
						
						text.add_ind();
						
						text.vert_add(new Vector3f((0 - pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((gw.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (3 * bs) - pt, 0.0f));
						text.vert_add(new Vector3f((0 + pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((gw.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (3 * bs) - pt, 0.0f));
						text.vert_add(new Vector3f((0 + pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((gw.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (3 * bs) + pt, 0.0f));
						text.vert_add(new Vector3f((0 - pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((gw.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (3 * bs) + pt, 0.0f));
						
						text.render_char(gw.charAt(it), false);
						
						text.add_col(new Vector4f(0.95f, 0.95f, 0.95f, 1.0f));
						
						text.add_ind();
					}
					
					String lw = "Quit the game!";
					
					for(int it = 0; it < lw.length(); it++)
					{
						text.vert_add(new Vector3f((0 - pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((lw.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (-3 * bs) - pt, 0.0f));
						text.vert_add(new Vector3f((0 + pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((lw.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (-3 * bs) - pt, 0.0f));
						text.vert_add(new Vector3f((0 + pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((lw.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (-3 * bs) + pt, 0.0f));
						text.vert_add(new Vector3f((0 - pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((lw.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (-3 * bs) + pt, 0.0f));
						
						text.render_char(lw.charAt(it), true);
						
						text.add_col(new Vector4f(0.0f, 0.0f, 0.0f, 1.0f));
						
						text.add_ind();
						
						text.vert_add(new Vector3f((0 - pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((lw.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (-3 * bs) - pt, 0.0f));
						text.vert_add(new Vector3f((0 + pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((lw.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (-3 * bs) - pt, 0.0f));
						text.vert_add(new Vector3f((0 + pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((lw.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (-3 * bs) + pt, 0.0f));
						text.vert_add(new Vector3f((0 - pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((lw.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (-3 * bs) + pt, 0.0f));
						
						text.render_char(lw.charAt(it), false);
						
						text.add_col(new Vector4f(0.95f, 0.95f, 0.95f, 1.0f));
						
						text.add_ind();
					}
				}
				
				else
				{
					float pt = 1.0f / 25.0f;
					
					String ps = "PAUSE";
					
					for(int it = 0; it < ps.length(); it++)
					{
						text.vert_add(new Vector3f((0 - pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((ps.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (8 * bs) - pt, 0.0f));
						text.vert_add(new Vector3f((0 + pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((ps.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (8 * bs) - pt, 0.0f));
						text.vert_add(new Vector3f((0 + pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((ps.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (8 * bs) + pt, 0.0f));
						text.vert_add(new Vector3f((0 - pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((ps.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (8 * bs) + pt, 0.0f));
						
						text.render_char(ps.charAt(it), true);
						
						text.add_col(new Vector4f(0.0f, 0.0f, 0.0f, 1.0f));
						
						text.add_ind();
						
						text.vert_add(new Vector3f((0 - pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((ps.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (8 * bs) - pt, 0.0f));
						text.vert_add(new Vector3f((0 + pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((ps.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (8 * bs) - pt, 0.0f));
						text.vert_add(new Vector3f((0 + pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((ps.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (8 * bs) + pt, 0.0f));
						text.vert_add(new Vector3f((0 - pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((ps.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (8 * bs) + pt, 0.0f));
						
						text.render_char(ps.charAt(it), false);
						
						text.add_col(new Vector4f(0.95f, 0.95f, 0.95f, 1.0f));
						
						text.add_ind();
					}
					
					pt = 1.0f / 52.0f;
					
					String ctg = "Continue the game!";
					
					for(int it = 0; it < ctg.length(); it++)
					{
						text.vert_add(new Vector3f((0 - pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((ctg.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (6 * bs) - pt, 0.0f));
						text.vert_add(new Vector3f((0 + pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((ctg.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (6 * bs) - pt, 0.0f));
						text.vert_add(new Vector3f((0 + pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((ctg.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (6 * bs) + pt, 0.0f));
						text.vert_add(new Vector3f((0 - pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((ctg.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (6 * bs) + pt, 0.0f));
						
						text.render_char(ctg.charAt(it), true);
						
						text.add_col(new Vector4f(0.0f, 0.0f, 0.0f, 1.0f));
						
						text.add_ind();
						
						text.vert_add(new Vector3f((0 - pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((ctg.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (6 * bs) - pt, 0.0f));
						text.vert_add(new Vector3f((0 + pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((ctg.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (6 * bs) - pt, 0.0f));
						text.vert_add(new Vector3f((0 + pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((ctg.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (6 * bs) + pt, 0.0f));
						text.vert_add(new Vector3f((0 - pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((ctg.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (6 * bs) + pt, 0.0f));
						
						text.render_char(ctg.charAt(it), false);
						
						text.add_col(new Vector4f(0.95f, 0.95f, 0.95f, 1.0f));
						
						text.add_ind();
					}
					
					String sw = "Save world!";
					
					for(int it = 0; it < sw.length(); it++)
					{
						text.vert_add(new Vector3f((0 - pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((sw.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), 0 - pt, 0.0f));
						text.vert_add(new Vector3f((0 + pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((sw.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), 0 - pt, 0.0f));
						text.vert_add(new Vector3f((0 + pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((sw.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), 0 + pt, 0.0f));
						text.vert_add(new Vector3f((0 - pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((sw.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), 0 + pt, 0.0f));
						
						text.render_char(sw.charAt(it), true);
						
						text.add_col(new Vector4f(0.0f, 0.0f, 0.0f, 1.0f));
						
						text.add_ind();
						
						text.vert_add(new Vector3f((0 - pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((sw.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), 0 - pt, 0.0f));
						text.vert_add(new Vector3f((0 + pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((sw.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), 0 - pt, 0.0f));
						text.vert_add(new Vector3f((0 + pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((sw.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), 0 + pt, 0.0f));
						text.vert_add(new Vector3f((0 - pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((sw.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), 0 + pt, 0.0f));
						
						text.render_char(sw.charAt(it), false);
						
						text.add_col(new Vector4f(0.95f, 0.95f, 0.95f, 1.0f));
						
						text.add_ind();
					}
					
					String gw = "Generate world!";
					
					for(int it = 0; it < gw.length(); it++)
					{
						text.vert_add(new Vector3f((0 - pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((gw.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (3 * bs) - pt, 0.0f));
						text.vert_add(new Vector3f((0 + pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((gw.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (3 * bs) - pt, 0.0f));
						text.vert_add(new Vector3f((0 + pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((gw.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (3 * bs) + pt, 0.0f));
						text.vert_add(new Vector3f((0 - pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((gw.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (3 * bs) + pt, 0.0f));
						
						text.render_char(gw.charAt(it), true);
						
						text.add_col(new Vector4f(0.0f, 0.0f, 0.0f, 1.0f));
						
						text.add_ind();
						
						text.vert_add(new Vector3f((0 - pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((gw.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (3 * bs) - pt, 0.0f));
						text.vert_add(new Vector3f((0 + pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((gw.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (3 * bs) - pt, 0.0f));
						text.vert_add(new Vector3f((0 + pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((gw.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (3 * bs) + pt, 0.0f));
						text.vert_add(new Vector3f((0 - pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((gw.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (3 * bs) + pt, 0.0f));
						
						text.render_char(gw.charAt(it), false);
						
						text.add_col(new Vector4f(0.95f, 0.95f, 0.95f, 1.0f));
						
						text.add_ind();
					}
					
					String lw = "Load world!";
					
					for(int it = 0; it < lw.length(); it++)
					{
						text.vert_add(new Vector3f((0 - pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((lw.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (-3 * bs) - pt, 0.0f));
						text.vert_add(new Vector3f((0 + pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((lw.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (-3 * bs) - pt, 0.0f));
						text.vert_add(new Vector3f((0 + pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((lw.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (-3 * bs) + pt, 0.0f));
						text.vert_add(new Vector3f((0 - pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((lw.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (-3 * bs) + pt, 0.0f));
						
						text.render_char(lw.charAt(it), true);
						
						text.add_col(new Vector4f(0.0f, 0.0f, 0.0f, 1.0f));
						
						text.add_ind();
						
						text.vert_add(new Vector3f((0 - pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((lw.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (-3 * bs) - pt, 0.0f));
						text.vert_add(new Vector3f((0 + pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((lw.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (-3 * bs) - pt, 0.0f));
						text.vert_add(new Vector3f((0 + pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((lw.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (-3 * bs) + pt, 0.0f));
						text.vert_add(new Vector3f((0 - pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((lw.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (-3 * bs) + pt, 0.0f));
						
						text.render_char(lw.charAt(it), false);
						
						text.add_col(new Vector4f(0.95f, 0.95f, 0.95f, 1.0f));
						
						text.add_ind();
					}
					
					String gm = "Generate map!";
					
					for(int it = 0; it < gm.length(); it++)
					{
						text.vert_add(new Vector3f((0 - pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((gm.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (-6 * bs) - pt, 0.0f));
						text.vert_add(new Vector3f((0 + pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((gm.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (-6 * bs) - pt, 0.0f));
						text.vert_add(new Vector3f((0 + pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((gm.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (-6 * bs) + pt, 0.0f));
						text.vert_add(new Vector3f((0 - pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((gm.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (-6 * bs) + pt, 0.0f));
						
						text.render_char(gm.charAt(it), true);
						
						text.add_col(new Vector4f(0.0f, 0.0f, 0.0f, 1.0f));
						
						text.add_ind();
						
						text.vert_add(new Vector3f((0 - pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((gm.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (-6 * bs) - pt, 0.0f));
						text.vert_add(new Vector3f((0 + pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((gm.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (-6 * bs) - pt, 0.0f));
						text.vert_add(new Vector3f((0 + pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((gm.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (-6 * bs) + pt, 0.0f));
						text.vert_add(new Vector3f((0 - pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((gm.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), (-6 * bs) + pt, 0.0f));
						
						text.render_char(gm.charAt(it), false);
						
						text.add_col(new Vector4f(0.95f, 0.95f, 0.95f, 1.0f));
						
						text.add_ind();
					}
				}
			}
		}
		
		float ts = 1.0f / 25.0f;
		
		String title = Display.getTitle();
		
		for(int it = 0; it < title.length(); it++)
		{
			text.vert_add(new Vector3f(-1 + ((it * ts) / ((float)Display.getWidth() / (float)Display.getHeight())), 1 - ts, 0.0f));
			text.vert_add(new Vector3f((-1 + (ts / ((float)Display.getWidth() / (float)Display.getHeight()))) + ((it * ts) / ((float)Display.getWidth() / (float)Display.getHeight())), 1 - ts, 0.0f));
			text.vert_add(new Vector3f((-1 + (ts / ((float)Display.getWidth() / (float)Display.getHeight()))) + ((it * ts) / ((float)Display.getWidth() / (float)Display.getHeight())), 1, 0.0f));
			text.vert_add(new Vector3f(-1 + ((it * ts) / ((float)Display.getWidth() / (float)Display.getHeight())), 1, 0.0f));
			
			text.render_char(title.charAt(it), true);
			
			text.add_col(new Vector4f(0.0f, 0.0f, 0.0f, 1.0f));
			
			text.add_ind();
			
			text.vert_add(new Vector3f(-1 + ((it * ts) / ((float)Display.getWidth() / (float)Display.getHeight())), 1 - ts, 0.0f));
			text.vert_add(new Vector3f((-1 + (ts / ((float)Display.getWidth() / (float)Display.getHeight()))) + ((it * ts) / ((float)Display.getWidth() / (float)Display.getHeight())), 1 - ts, 0.0f));
			text.vert_add(new Vector3f((-1 + (ts / ((float)Display.getWidth() / (float)Display.getHeight()))) + ((it * ts) / ((float)Display.getWidth() / (float)Display.getHeight())), 1, 0.0f));
			text.vert_add(new Vector3f(-1 + ((it * ts) / ((float)Display.getWidth() / (float)Display.getHeight())), 1, 0.0f));
			
			text.render_char(title.charAt(it), false);
			
			text.add_col(new Vector4f(0.95f, 0.95f, 0.95f, 1.0f));
			
			text.add_ind();
		}
		
		if(window.is_debug)
		{
			String fps = "FPS: " + Integer.toString(window.FPS);
			
			for(int it = 0; it < fps.length(); it++)
			{
				text_background.vert_add(new Vector3f(-1 + ((it * ts) / ((float)Display.getWidth() / (float)Display.getHeight())), 1 - ts - (2 * ts), 0.0f));
				text_background.vert_add(new Vector3f((-1 + (ts / ((float)Display.getWidth() / (float)Display.getHeight()))) + ((it * ts) / ((float)Display.getWidth() / (float)Display.getHeight())), 1 - ts - (2 * ts), 0.0f));
				text_background.vert_add(new Vector3f((-1 + (ts / ((float)Display.getWidth() / (float)Display.getHeight()))) + ((it * ts) / ((float)Display.getWidth() / (float)Display.getHeight())), 1 - (2 * ts), 0.0f));
				text_background.vert_add(new Vector3f(-1 + ((it * ts) / ((float)Display.getWidth() / (float)Display.getHeight())), 1 - (2 * ts), 0.0f));
				
				text_background.t_coord_add(new Vector2f(0, 1));
				text_background.t_coord_add(new Vector2f(1, 1));
				text_background.t_coord_add(new Vector2f(1, 0));
				text_background.t_coord_add(new Vector2f(0, 0));
				
				text_background.add_col(new Vector4f(0.0f, 0.0f, 0.0f, 0.5f));
				
				text_background.add_ind();
				
				text.vert_add(new Vector3f(-1 + ((it * ts) / ((float)Display.getWidth() / (float)Display.getHeight())), 1 - ts - (2 * ts), 0.0f));
				text.vert_add(new Vector3f((-1 + (ts / ((float)Display.getWidth() / (float)Display.getHeight()))) + ((it * ts) / ((float)Display.getWidth() / (float)Display.getHeight())), 1 - ts - (2 * ts), 0.0f));
				text.vert_add(new Vector3f((-1 + (ts / ((float)Display.getWidth() / (float)Display.getHeight()))) + ((it * ts) / ((float)Display.getWidth() / (float)Display.getHeight())), 1 - (2 * ts), 0.0f));
				text.vert_add(new Vector3f(-1 + ((it * ts) / ((float)Display.getWidth() / (float)Display.getHeight())), 1 - (2 * ts), 0.0f));
				
				text.render_char(fps.charAt(it), true);
				
				text.add_col(new Vector4f(0.0f, 0.0f, 0.0f, 1.0f));
				
				text.add_ind();
				
				text.vert_add(new Vector3f(-1 + ((it * ts) / ((float)Display.getWidth() / (float)Display.getHeight())), 1 - ts - (2 * ts), 0.0f));
				text.vert_add(new Vector3f((-1 + (ts / ((float)Display.getWidth() / (float)Display.getHeight()))) + ((it * ts) / ((float)Display.getWidth() / (float)Display.getHeight())), 1 - ts - (2 * ts), 0.0f));
				text.vert_add(new Vector3f((-1 + (ts / ((float)Display.getWidth() / (float)Display.getHeight()))) + ((it * ts) / ((float)Display.getWidth() / (float)Display.getHeight())), 1 - (2 * ts), 0.0f));
				text.vert_add(new Vector3f(-1 + ((it * ts) / ((float)Display.getWidth() / (float)Display.getHeight())), 1 - (2 * ts), 0.0f));
				
				text.render_char(fps.charAt(it), false);
				
				text.add_col(new Vector4f(0.95f, 0.95f, 0.95f, 1.0f));
				
				text.add_ind();
			}
			
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);
			
			String pos = "Position: X:" + df.format(window.player.pos.x) + " Y:" + df.format(window.player.pos.y) + " Z:" + df.format(window.player.pos.z);
			
			for(int it = 0; it < pos.length(); it++)
			{
				text_background.vert_add(new Vector3f(-1 + ((it * ts) / ((float)Display.getWidth() / (float)Display.getHeight())), 1 - ts - (4 * ts), 0.0f));
				text_background.vert_add(new Vector3f((-1 + (ts / ((float)Display.getWidth() / (float)Display.getHeight()))) + ((it * ts) / ((float)Display.getWidth() / (float)Display.getHeight())), 1 - ts - (4 * ts), 0.0f));
				text_background.vert_add(new Vector3f((-1 + (ts / ((float)Display.getWidth() / (float)Display.getHeight()))) + ((it * ts) / ((float)Display.getWidth() / (float)Display.getHeight())), 1 - (4 * ts), 0.0f));
				text_background.vert_add(new Vector3f(-1 + ((it * ts) / ((float)Display.getWidth() / (float)Display.getHeight())), 1 - (4 * ts), 0.0f));
				
				text_background.t_coord_add(new Vector2f(0, 1));
				text_background.t_coord_add(new Vector2f(1, 1));
				text_background.t_coord_add(new Vector2f(1, 0));
				text_background.t_coord_add(new Vector2f(0, 0));
				
				text_background.add_col(new Vector4f(0.0f, 0.0f, 0.0f, 0.5f));
				
				text_background.add_ind();
				
				text.vert_add(new Vector3f(-1 + ((it * ts) / ((float)Display.getWidth() / (float)Display.getHeight())), 1 - ts - (4 * ts), 0.0f));
				text.vert_add(new Vector3f((-1 + (ts / ((float)Display.getWidth() / (float)Display.getHeight()))) + ((it * ts) / ((float)Display.getWidth() / (float)Display.getHeight())), 1 - ts - (4 * ts), 0.0f));
				text.vert_add(new Vector3f((-1 + (ts / ((float)Display.getWidth() / (float)Display.getHeight()))) + ((it * ts) / ((float)Display.getWidth() / (float)Display.getHeight())), 1 - (4 * ts), 0.0f));
				text.vert_add(new Vector3f(-1 + ((it * ts) / ((float)Display.getWidth() / (float)Display.getHeight())), 1 - (4 * ts), 0.0f));
				
				text.render_char(pos.charAt(it), true);
				
				text.add_col(new Vector4f(0.0f, 0.0f, 0.0f, 1.0f));
				
				text.add_ind();
				
				text.vert_add(new Vector3f(-1 + ((it * ts) / ((float)Display.getWidth() / (float)Display.getHeight())), 1 - ts - (4 * ts), 0.0f));
				text.vert_add(new Vector3f((-1 + (ts / ((float)Display.getWidth() / (float)Display.getHeight()))) + ((it * ts) / ((float)Display.getWidth() / (float)Display.getHeight())), 1 - ts - (4 * ts), 0.0f));
				text.vert_add(new Vector3f((-1 + (ts / ((float)Display.getWidth() / (float)Display.getHeight()))) + ((it * ts) / ((float)Display.getWidth() / (float)Display.getHeight())), 1 - (4 * ts), 0.0f));
				text.vert_add(new Vector3f(-1 + ((it * ts) / ((float)Display.getWidth() / (float)Display.getHeight())), 1 - (4 * ts), 0.0f));
				
				text.render_char(pos.charAt(it), false);
				
				text.add_col(new Vector4f(0.95f, 0.95f, 0.95f, 1.0f));
				
				text.add_ind();
			}
			
			String blk = "Block: X:" + Integer.toString(Math.round(window.player.pos.x)) + " Y:" + Integer.toString(Math.round(window.player.pos.y)) + " Z:" + Integer.toString(Math.round(window.player.pos.z));
			
			for(int it = 0; it < blk.length(); it++)
			{
				text_background.vert_add(new Vector3f(-1 + ((it * ts) / ((float)Display.getWidth() / (float)Display.getHeight())), 1 - ts - (6 * ts), 0.0f));
				text_background.vert_add(new Vector3f((-1 + (ts / ((float)Display.getWidth() / (float)Display.getHeight()))) + ((it * ts) / ((float)Display.getWidth() / (float)Display.getHeight())), 1 - ts - (6 * ts), 0.0f));
				text_background.vert_add(new Vector3f((-1 + (ts / ((float)Display.getWidth() / (float)Display.getHeight()))) + ((it * ts) / ((float)Display.getWidth() / (float)Display.getHeight())), 1 - (6 * ts), 0.0f));
				text_background.vert_add(new Vector3f(-1 + ((it * ts) / ((float)Display.getWidth() / (float)Display.getHeight())), 1 - (6 * ts), 0.0f));
				
				text_background.t_coord_add(new Vector2f(0, 1));
				text_background.t_coord_add(new Vector2f(1, 1));
				text_background.t_coord_add(new Vector2f(1, 0));
				text_background.t_coord_add(new Vector2f(0, 0));
				
				text_background.add_col(new Vector4f(0.0f, 0.0f, 0.0f, 0.5f));
				
				text_background.add_ind();
				
				text.vert_add(new Vector3f(-1 + ((it * ts) / ((float)Display.getWidth() / (float)Display.getHeight())), 1 - ts - (6 * ts), 0.0f));
				text.vert_add(new Vector3f((-1 + (ts / ((float)Display.getWidth() / (float)Display.getHeight()))) + ((it * ts) / ((float)Display.getWidth() / (float)Display.getHeight())), 1 - ts - (6 * ts), 0.0f));
				text.vert_add(new Vector3f((-1 + (ts / ((float)Display.getWidth() / (float)Display.getHeight()))) + ((it * ts) / ((float)Display.getWidth() / (float)Display.getHeight())), 1 - (6 * ts), 0.0f));
				text.vert_add(new Vector3f(-1 + ((it * ts) / ((float)Display.getWidth() / (float)Display.getHeight())), 1 - (6 * ts), 0.0f));
				
				text.render_char(blk.charAt(it), true);
				
				text.add_col(new Vector4f(0.0f, 0.0f, 0.0f, 1.0f));
				
				text.add_ind();
				
				text.vert_add(new Vector3f(-1 + ((it * ts) / ((float)Display.getWidth() / (float)Display.getHeight())), 1 - ts - (6 * ts), 0.0f));
				text.vert_add(new Vector3f((-1 + (ts / ((float)Display.getWidth() / (float)Display.getHeight()))) + ((it * ts) / ((float)Display.getWidth() / (float)Display.getHeight())), 1 - ts - (6 * ts), 0.0f));
				text.vert_add(new Vector3f((-1 + (ts / ((float)Display.getWidth() / (float)Display.getHeight()))) + ((it * ts) / ((float)Display.getWidth() / (float)Display.getHeight())), 1 - (6 * ts), 0.0f));
				text.vert_add(new Vector3f(-1 + ((it * ts) / ((float)Display.getWidth() / (float)Display.getHeight())), 1 - (6 * ts), 0.0f));
				
				text.render_char(blk.charAt(it), false);
				
				text.add_col(new Vector4f(0.95f, 0.95f, 0.95f, 1.0f));
				
				text.add_ind();
			}
			
			String cnk = "Chunk: X:" + Integer.toString(Math.round(window.player.pos.x) / World.chunk_size) + " Y:" + Integer.toString(Math.round(window.player.pos.y) / World.chunk_size) + " Z:" + Integer.toString(Math.round(window.player.pos.z) / World.chunk_size);
			
			for(int it = 0; it < cnk.length(); it++)
			{
				text_background.vert_add(new Vector3f(-1 + ((it * ts) / ((float)Display.getWidth() / (float)Display.getHeight())), 1 - ts - (8 * ts), 0.0f));
				text_background.vert_add(new Vector3f((-1 + (ts / ((float)Display.getWidth() / (float)Display.getHeight()))) + ((it * ts) / ((float)Display.getWidth() / (float)Display.getHeight())), 1 - ts - (8 * ts), 0.0f));
				text_background.vert_add(new Vector3f((-1 + (ts / ((float)Display.getWidth() / (float)Display.getHeight()))) + ((it * ts) / ((float)Display.getWidth() / (float)Display.getHeight())), 1 - (8 * ts), 0.0f));
				text_background.vert_add(new Vector3f(-1 + ((it * ts) / ((float)Display.getWidth() / (float)Display.getHeight())), 1 - (8 * ts), 0.0f));
				
				text_background.t_coord_add(new Vector2f(0, 1));
				text_background.t_coord_add(new Vector2f(1, 1));
				text_background.t_coord_add(new Vector2f(1, 0));
				text_background.t_coord_add(new Vector2f(0, 0));
				
				text_background.add_col(new Vector4f(0.0f, 0.0f, 0.0f, 0.5f));
				
				text_background.add_ind();
				
				text.vert_add(new Vector3f(-1 + ((it * ts) / ((float)Display.getWidth() / (float)Display.getHeight())), 1 - ts - (8 * ts), 0.0f));
				text.vert_add(new Vector3f((-1 + (ts / ((float)Display.getWidth() / (float)Display.getHeight()))) + ((it * ts) / ((float)Display.getWidth() / (float)Display.getHeight())), 1 - ts - (8 * ts), 0.0f));
				text.vert_add(new Vector3f((-1 + (ts / ((float)Display.getWidth() / (float)Display.getHeight()))) + ((it * ts) / ((float)Display.getWidth() / (float)Display.getHeight())), 1 - (8 * ts), 0.0f));
				text.vert_add(new Vector3f(-1 + ((it * ts) / ((float)Display.getWidth() / (float)Display.getHeight())), 1 - (8 * ts), 0.0f));
				
				text.render_char(cnk.charAt(it), true);
				
				text.add_col(new Vector4f(0.0f, 0.0f, 0.0f, 1.0f));
				
				text.add_ind();
				
				text.vert_add(new Vector3f(-1 + ((it * ts) / ((float)Display.getWidth() / (float)Display.getHeight())), 1 - ts - (8 * ts), 0.0f));
				text.vert_add(new Vector3f((-1 + (ts / ((float)Display.getWidth() / (float)Display.getHeight()))) + ((it * ts) / ((float)Display.getWidth() / (float)Display.getHeight())), 1 - ts - (8 * ts), 0.0f));
				text.vert_add(new Vector3f((-1 + (ts / ((float)Display.getWidth() / (float)Display.getHeight()))) + ((it * ts) / ((float)Display.getWidth() / (float)Display.getHeight())), 1 - (8 * ts), 0.0f));
				text.vert_add(new Vector3f(-1 + ((it * ts) / ((float)Display.getWidth() / (float)Display.getHeight())), 1 - (8 * ts), 0.0f));
				
				text.render_char(cnk.charAt(it), false);
				
				text.add_col(new Vector4f(0.95f, 0.95f, 0.95f, 1.0f));
				
				text.add_ind();
			}
		}
		
		if(window.world.is_loading)
		{
			String lding = "Loading";
			
			for(int it = 0; it < lding.length(); it++)
			{
				float pt = 1.0f / 24.0f;
				
				text.vert_add(new Vector3f((0 - pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((lding.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), 0 - pt, 0.0f));
				text.vert_add(new Vector3f((0 + pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((lding.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), 0 - pt, 0.0f));
				text.vert_add(new Vector3f((0 + pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((lding.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), 0 + pt, 0.0f));
				text.vert_add(new Vector3f((0 - pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((lding.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), 0 + pt, 0.0f));
				
				text.render_char(lding.charAt(it), true);
				
				text.add_col(new Vector4f(0.0f, 0.0f, 0.0f, 1.0f));
				
				text.add_ind();
				
				text.vert_add(new Vector3f((0 - pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((lding.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), 0 - pt, 0.0f));
				text.vert_add(new Vector3f((0 + pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((lding.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), 0 - pt, 0.0f));
				text.vert_add(new Vector3f((0 + pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((lding.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), 0 + pt, 0.0f));
				text.vert_add(new Vector3f((0 - pt) / ((float)Display.getWidth() / (float)Display.getHeight()) + ((lding.length() / 2.0f * -1 + 1 + it) * (pt * 1.5f)) / ((float)Display.getWidth() / (float)Display.getHeight()), 0 + pt, 0.0f));
				
				text.render_char(lding.charAt(it), false);
				
				text.add_col(new Vector4f(0.95f, 0.95f, 0.95f, 1.0f));
				
				text.add_ind();
			}
		}
		
		if(window.player.is_mm && !window.player.is_gw_menu)
		{
			if(!Timer.first_frame)
			{
				spsh_offset -= 0.125f * Timer.delta_time;
			}
			
			float spt = 1.0f / 22.5f;
			
			for(int it = 0; it < splash.length(); it++)
			{
				text.vert_add(new Vector3f(1 + spsh_offset + ((it * spt) / ((float)Display.getWidth() / (float)Display.getHeight())), 1 - spt * 2, 0.0f));
				text.vert_add(new Vector3f((1 + spsh_offset + (spt / ((float)Display.getWidth() / (float)Display.getHeight()))) + ((it * spt) / ((float)Display.getWidth() / (float)Display.getHeight())), 1 - spt * 2, 0.0f));
				text.vert_add(new Vector3f((1 + spsh_offset + (spt / ((float)Display.getWidth() / (float)Display.getHeight()))) + ((it * spt) / ((float)Display.getWidth() / (float)Display.getHeight())), 1 - spt, 0.0f));
				text.vert_add(new Vector3f(1 + spsh_offset + ((it * spt) / ((float)Display.getWidth() / (float)Display.getHeight())), 1 - spt, 0.0f));
				
				text.render_char(splash.charAt(it), true);
				
				text.add_col(new Vector4f(0.0f, 0.0f, 0.0f, 1.0f));
				
				text.add_ind();
				
				text.vert_add(new Vector3f(1 + spsh_offset + ((it * spt) / ((float)Display.getWidth() / (float)Display.getHeight())), 1 - spt * 2, 0.0f));
				text.vert_add(new Vector3f((1 + spsh_offset + (spt / ((float)Display.getWidth() / (float)Display.getHeight()))) + ((it * spt) / ((float)Display.getWidth() / (float)Display.getHeight())), 1 - spt * 2, 0.0f));
				text.vert_add(new Vector3f((1 + spsh_offset + (spt / ((float)Display.getWidth() / (float)Display.getHeight()))) + ((it * spt) / ((float)Display.getWidth() / (float)Display.getHeight())), 1 - spt, 0.0f));
				text.vert_add(new Vector3f(1 + spsh_offset + ((it * spt) / ((float)Display.getWidth() / (float)Display.getHeight())), 1 - spt, 0.0f));
				
				text.render_char(splash.charAt(it), false);
				
				text.add_col(new Vector4f(1.0f, 1.0f, 0.0f, 1.0f));
				
				text.add_ind();
				
				if(it == splash.length() - 1)
				{
					if((1 + spsh_offset + (spt / ((float)Display.getWidth() / (float)Display.getHeight()))) + ((it * spt) / ((float)Display.getWidth() / (float)Display.getHeight())) < -1.0f)
					{
						splash = generate_splash();
						spsh_offset = 0.0f;
					}
				}
			}
		}
		
		text_background.convert();
		
		update_image(text_background, camera);
		
		text.convert();
		
		update_text(text, camera);
		
		clear_image(logo);
		
		logo.enabled = (window.player.is_mm && !window.player.is_gw_menu);
		
		float lgs = 1.0f / 7.0f;;
		
		logo.vert_add(new Vector3f((0 - lgs) / ((float)Display.getWidth() / (float)Display.getHeight()) - (6 * lgs) / ((float)Display.getWidth() / (float)Display.getHeight()), (5 * lgs) - lgs, 0.0f));
		logo.vert_add(new Vector3f((0 + lgs) / ((float)Display.getWidth() / (float)Display.getHeight()) + (6 * lgs) / ((float)Display.getWidth() / (float)Display.getHeight()), (5 * lgs) - lgs, 0.0f));
		logo.vert_add(new Vector3f((0 + lgs) / ((float)Display.getWidth() / (float)Display.getHeight()) + (6 * lgs) / ((float)Display.getWidth() / (float)Display.getHeight()), (5 * lgs) + lgs, 0.0f));
		logo.vert_add(new Vector3f((0 - lgs) / ((float)Display.getWidth() / (float)Display.getHeight()) - (6 * lgs) / ((float)Display.getWidth() / (float)Display.getHeight()), (5 * lgs) + lgs, 0.0f));
		
		logo.t_coord_add(new Vector2f(0, 1));
		logo.t_coord_add(new Vector2f(1, 1));
		logo.t_coord_add(new Vector2f(1, 0));
		logo.t_coord_add(new Vector2f(0, 0));
		
		logo.add_col(new Vector4f(1, 1, 1, 1));
		
		logo.add_ind();
		
		logo.convert();
		
		update_image(logo, camera);
	}
	
	public void destroy()
	{
		point.destroy();
		hotbar.destroy();
		selected_hotbar.destroy();
		hotbar_blocks.destroy();
		inventory.destroy();
		inventory_blocks.destroy();
		background.destroy();
		text.destroy();
		text_background.destroy();
		buttons.destroy();
		loading.destroy();
		mm_background.destroy();
		logo.destroy();
	}
	
	private void update_image(final ImageUI img, final Camera camera)
	{
		img.obj.vert_data = img.vert;
		img.obj.col_data = img.col;
		img.obj.tex_c_data = img.tex_c;
		img.obj.ind_data = img.ind;
		
		img.obj.recreate();
		
		img.update(camera);
	}
	
	private void clear_image(final ImageUI img)
	{
		img.vert_data.clear();
		img.col_data.clear();
		img.t_coord_data.clear();
		img.ind_data.clear();
		
		img.count = 0;
	}
	
	private void update_text(final TextUI txt, final Camera camera)
	{
		txt.obj.vert_data = txt.vert;
		txt.obj.col_data = txt.col;
		txt.obj.tex_c_data = txt.tex_c;
		txt.obj.ind_data = txt.ind;
		
		txt.obj.recreate();
		
		txt.update(camera);
	}
	
	private void clear_text(final TextUI txt)
	{
		txt.vert_data.clear();
		txt.col_data.clear();
		txt.t_coord_data.clear();
		txt.ind_data.clear();
		
		txt.count = 0;
	}
	
	private String generate_splash()
	{
		Random random = new Random();
		
		LocalDateTime date_time = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		String dt = date_time.format(formatter);
		
		int rn = random.nextInt();
		
		boolean isny = false;;
		
		if((Integer.parseInt(date_time.format(DateTimeFormatter.ofPattern("MM"))) == 12))
		{
			if((Integer.parseInt(date_time.format(DateTimeFormatter.ofPattern("dd"))) >= 25))
			{
				isny = true;
			}
		}
		
		else if((Integer.parseInt(date_time.format(DateTimeFormatter.ofPattern("MM"))) == 00))
		{
			if((Integer.parseInt(date_time.format(DateTimeFormatter.ofPattern("dd"))) <= 5))
			{
				isny = true;
			}
		}
		
		if(isny)
		{
			splashes =
					"Happy New Year!" + System.lineSeparator();
		}
		
		else
		{
			splashes =
					"Hello World!" + System.lineSeparator() + 
					"MineCreate" + System.lineSeparator() + 
					"Alpha!" + System.lineSeparator() + 
					"Using Java and LWJGL" + System.lineSeparator() + 
					"Clone of Minecraft" + System.lineSeparator() + 
					"Mining&Creating" + System.lineSeparator() + 
					"MineCreate minecreate = new MineCreate();" + System.lineSeparator() + 
					"Random Splash Text" + System.lineSeparator() + 
					"LoL" + System.lineSeparator() + 
					":)" + System.lineSeparator() + 
					"This is Main Menu" + System.lineSeparator() + 
					"Made in Ukraine" + System.lineSeparator() + 
					"1 + 1 != 3" + System.lineSeparator() + 
					"Welcome!" + System.lineSeparator() + 
					"Enjoy)" + System.lineSeparator() + 
					"Indie" + System.lineSeparator() + 
					"Thank you Notch!" + System.lineSeparator() + 
					"Tirezor - Creator of MineCreate" + System.lineSeparator() + 
					"Created by Tirezor" + System.lineSeparator() + 
					"I love you!" + System.lineSeparator() + 
					"DateTime = " + dt + System.lineSeparator() + 
					Display.getTitle().substring(11) + System.lineSeparator() + 
					"Random number is " + Integer.toString(rn) + System.lineSeparator() + 
					"LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOONNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG" + System.lineSeparator() + 
					"What?" + System.lineSeparator() + 
					"?" + System.lineSeparator() + 
					"!" + System.lineSeparator() + 
					"Lets Play!" + System.lineSeparator() + 
					"Never Say Game Over!" + System.lineSeparator() + 
					"String" + System.lineSeparator() + 
					"itch.io" + System.lineSeparator() + 
					"Reddit" + System.lineSeparator() + 
					"r/minecreategame" + System.lineSeparator() + 
					"A B C D E F G H I J K L M N O P Q R S T U V W X Y Z" + System.lineSeparator() + 
					"0 1 2 3 4 5 6 7 8 9" + System.lineSeparator() + 
					" " + System.lineSeparator() + 
					"(O-O)" + System.lineSeparator() + 
					"Do not divide by zero!" + System.lineSeparator() + 
					"WOW!" + System.lineSeparator() + 
					"In development!" + System.lineSeparator() + 
					"Eclipse" + System.lineSeparator() + 
					"XD" + System.lineSeparator() + 
					"try catch" + System.lineSeparator() + 
					"Created in 2023" + System.lineSeparator() + 
					"hi" + System.lineSeparator() + 
					"System.out.println(new String());" + System.lineSeparator() + 
					"System.lineSeparator()" + System.lineSeparator() + 
					"x y z" + System.lineSeparator() + 
					"Videogame" + System.lineSeparator() + 
					"Building" + System.lineSeparator();
		}
		
		String[] texts = splashes.split(System.lineSeparator());
		
		String result = texts[random.nextInt(0, texts.length)];
		
		return result;
	}
}