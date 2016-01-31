package com.sapphire.main;

import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayer;

public class ServerProxy
{
	public void registerRenderThings()
	{
	
	}
	
	public int addArmor(String Armor)
	{
		return 0;
	}
	
	public void registerHandlers()
	{
	}
	
	public EntityPlayer getPlayerEntity(MessageContext ctx)
	{
		return ctx.getServerHandler().playerEntity;
	}
	
	public void openBook()
	{
	}
}
