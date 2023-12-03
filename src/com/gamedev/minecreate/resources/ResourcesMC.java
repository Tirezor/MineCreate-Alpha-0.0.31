package com.gamedev.minecreate.resources;

import com.gamedev.minecreate.gameengine.audio.Sound;
import com.gamedev.minecreate.gameengine.shaders.Shader;
import com.gamedev.minecreate.gameengine.textures.Texture;

public class ResourcesMC
{
	public static Texture blocks_atlas;
	public static Texture selector;
	public static Texture ocean0;
	public static Texture ocean1;
	public static Texture clouds;
	public static Texture hotbar;
	public static Texture selected_hotbar;
	public static Texture buttons_ui;
	public static Texture background;
	public static Texture font;
	public static Texture logo;
	
	public static Shader chunk_shader;
	public static Shader ui_shader;
	public static Shader selector_shader;
	public static Shader clouds_shader;
	public static Shader sky_shader;
	public static Shader particle_shader;
	public static Shader blockhand_shader;
	
	public static Sound grass;
	public static Sound stone;
	public static Sound wood;
	public static Sound dirt;
	public static Sound sand;
	public static Sound wool;
	public static Sound metal;
	public static Sound glass;
	public static Sound glass_hit;
	public static Sound water_splash;
	public static Sound water_flow;
	public static Sound day_ambience;
	public static Sound underwater_ambience;
	public static Sound underground_ambience;
	public static Sound select;
	public static Sound click;
	public static Sound select_block;
	
	public static void create()
	{
		blocks_atlas = new Texture("/textures/blocks_atlas.png", true);
		selector = new Texture("/textures/selector.png", true);
		ocean0 = new Texture("/textures/ocean0.png", true);
		ocean1 = new Texture("/textures/ocean1.png", true);
		clouds = new Texture("/textures/clouds.png", true);
		hotbar = new Texture("/textures/hotbar.png", true);
		selected_hotbar = new Texture("/textures/selected_hotbar.png", true);
		buttons_ui = new Texture("/textures/buttons_ui.png", true);
		background = new Texture("/textures/background.png", true);
		font = new Texture("/textures/font.png", true);
		logo = new Texture("/textures/logo.png", true);
		
		chunk_shader = new Shader("/shaders/chunk/chunk.vert", "/shaders/chunk/chunk.frag");
		ui_shader = new Shader("/shaders/ui/ui.vert", "/shaders/ui/ui.frag");
		selector_shader = new Shader("/shaders/selector/selector.vert", "/shaders/selector/selector.frag");
		clouds_shader = new Shader("/shaders/clouds/clouds.vert", "/shaders/clouds/clouds.frag");
		sky_shader = new Shader("/shaders/sky/sky.vert", "/shaders/sky/sky.frag");
		particle_shader = new Shader("/shaders/particle/particle.vert", "/shaders/particle/particle.frag");
		blockhand_shader = new Shader("/shaders/blockhand/blockhand.vert", "/shaders/blockhand/blockhand.frag");
		
		grass = new Sound("/sounds/grass.wav");
		stone = new Sound("/sounds/stone.wav");
		wood = new Sound("/sounds/wood.wav");
		dirt = new Sound("/sounds/dirt.wav");
		sand = new Sound("/sounds/sand.wav");
		wool = new Sound("/sounds/wool.wav");
		metal = new Sound("/sounds/metal.wav");
		glass = new Sound("/sounds/glass.wav");
		glass_hit = new Sound("/sounds/glass_hit.wav");
		water_splash = new Sound("/sounds/water_splash.wav");
		water_flow = new Sound("/sounds/water_flow.wav");
		day_ambience = new Sound("/sounds/day_ambience.wav");
		underwater_ambience = new Sound("/sounds/underwater_ambience.wav");
		underground_ambience = new Sound("/sounds/underground_ambience.wav");
		select = new Sound("/sounds/select.wav");
		click = new Sound("/sounds/click.wav");
		select_block = new Sound("/sounds/select_block.wav");
	}
	
	public static void destroy()
	{
		blocks_atlas.destroy();
		selector.destroy();
		ocean0.destroy();
		ocean1.destroy();
		clouds.destroy();
		hotbar.destroy();
		selected_hotbar.destroy();
		buttons_ui.destroy();
		background.destroy();
		font.destroy();
		logo.destroy();
		
		chunk_shader.destroy();
		ui_shader.destroy();
		selector_shader.destroy();
		clouds_shader.destroy();
		sky_shader.destroy();
		particle_shader.destroy();
		blockhand_shader.destroy();
	}
}