/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.texture.TextureManager
 *  net.minecraft.potion.Potion
 *  net.minecraft.util.ResourceLocation
 */
package com.sapphire.lib;

import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

public class Abilities extends Potion
{
	private static final ResourceLocation texture = new ResourceLocation("sus", "textures/gui/powericons.png");
	
	public Abilities(int par1, boolean par2, int par3)
	{
		super(par1, par2, par3);
	}
	
	public boolean bindTexture()
	{
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		return true;
	}
	
	public Potion iconIndex(int par1, int par2)
	{
		super.setIconIndex(par1, par2);
		return this;
	}
}
