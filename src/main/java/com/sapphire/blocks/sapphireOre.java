package com.sapphire.blocks;

import java.util.Random;

import com.sapphire.handlers.CreativeTabHandler;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.world.IBlockAccess;

public class sapphireOre extends Block {

	public sapphireOre(Material mat) {
		super(mat);
		this.setCreativeTab(CreativeTabHandler.tabFlash);
		this.setHarvestLevel("pickaxe", 3);
	}

	@Override
	public void registerBlockIcons(IIconRegister reg) {
		this.blockIcon = reg.registerIcon("sapphire:SapphireOre");
	}

	private Random rand = new Random();

	@Override
	public int getExpDrop(IBlockAccess iba, int par1, int par2) {
		if (this.getItemDropped(par1, rand, par2) != Item
				.getItemFromBlock(this)) {
			return rand.nextInt(5) + 1;
		}
		return 0;
	}
}
