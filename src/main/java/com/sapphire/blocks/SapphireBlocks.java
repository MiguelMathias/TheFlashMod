package com.sapphire.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class SapphireBlocks
{
	public static void mainRegistry()
	{
		initBlocks();
		registerBlocks();
	}
	
	public static Block sapphireOre;
	public static Block sapphireChest;
	
	public static void initBlocks()
	{
		sapphireOre = new sapphireOre(Material.rock).setHardness(50.0F).setBlockName("sapphireOre");
	}
	
	public static void registerBlocks()
	{
		GameRegistry.registerBlock(sapphireOre, "sapphireOre");
		
	}
}
