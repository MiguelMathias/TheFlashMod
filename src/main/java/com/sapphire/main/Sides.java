package com.sapphire.main;

import cpw.mods.fml.relauncher.Side;
import net.minecraft.world.World;

public class Sides
{
	public static Side logical(World world)
	{
		return world.isRemote ? Side.CLIENT : Side.SERVER;
	}
	
	private Sides()
	{
	}
}
