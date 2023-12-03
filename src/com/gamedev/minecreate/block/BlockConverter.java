package com.gamedev.minecreate.block;

public class BlockConverter
{
	public static int block_to_int(final Block block)
	{
		return block.id;
	}
	
	public static Block int_to_block(final int block)
	{
		switch(block)
		{
		case 0:
			return BlockData.air;
		
		case 1:
			return BlockData.stone;
		
		case 2:
			return BlockData.grass;
		
		case 3:
			return BlockData.dirt;
		
		case 4:
			return BlockData.wooden_planks;
		
		case 5:
			return BlockData.bedrock;
		
		case 6:
			return BlockData.water;
		
		case 7:
			return BlockData.cobblestone;
		
		case 8:
			return BlockData.wood;
		
		case 9:
			return BlockData.leaves;
		
		case 10:
			return BlockData.sand;
		
		case 11:
			return BlockData.chamomile;
		
		case 12:
			return BlockData.sapling;
		
		case 13:
			return BlockData.gravel;
		
		case 14:
			return BlockData.glass;
		
		case 15:
			return BlockData.coal_ore;
		
		case 16:
			return BlockData.iron_ore;
		
		case 17:
			return BlockData.gold_ore;
		
		case 18:
			return BlockData.diamond_ore;
		
		case 19:
			return BlockData.bricks;
		
		case 20:
			return BlockData.stone_bricks;
		
		case 21:
			return BlockData.bookshelf;
		
		case 22:
			return BlockData.mossy_cobblestone;
		
		case 23:
			return BlockData.clay;
		
		case 24:
			return BlockData.boletus;
		
		case 25:
			return BlockData.white_wool;
		
		case 26:
			return BlockData.gray_wool;
		
		case 27:
			return BlockData.black_wool;
		
		case 28:
			return BlockData.red_wool;
		
		case 29:
			return BlockData.green_wool;
		
		case 30:
			return BlockData.blue_wool;
		
		case 31:
			return BlockData.yellow_wool;
		
		case 32:
			return BlockData.orange_wool;
		
		case 33:
			return BlockData.brown_wool;
		
		case 34:
			return BlockData.purple_wool;
		
		case 35:
			return BlockData.pink_wool;
		
		case 36:
			return BlockData.iron_block;
		
		case 37:
			return BlockData.gold_block;
		
		case 38:
			return BlockData.diamond_block;
		
		case 39:
			return BlockData.rose;
		
		case 40:
			return BlockData.dandelion;
		
		case 41:
			return BlockData.amanita_muscaria;
		
		default:
			return null;
		}
	}
}