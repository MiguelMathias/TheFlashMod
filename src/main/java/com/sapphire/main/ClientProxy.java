package com.sapphire.main;

import com.sapphire.lib.ModelFlash;

import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.player.EntityPlayer;

public class ClientProxy extends ServerProxy
{
	private static final ModelBiped bipedBase = new ModelBiped(0.2f);
	private static final ModelFlash flash_armor = new ModelFlash(0.2f);
	
	@Override
	public void registerRenderThings()
	{
	
	}
	
	public static ModelBiped getArmorModel(int id)
	{
		switch (id)
		{
		case 7:
		{
			return flash_armor;
		}
		}
		return bipedBase;
	}
	
	@Override
	public EntityPlayer getPlayerEntity(MessageContext ctx)
	{
		return ctx.side.isClient() ? Minecraft.getMinecraft().thePlayer : super.getPlayerEntity(ctx);
	}
	
	@Override
	public void openBook()
	{
	}
}
