package com.sapphire.world;

import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.registry.GameRegistry;

public class SapphireWorld {

	public static void mainRegistry() {
		initWorldGen();
	}

	public static void initWorldGen() {
		registerWorldGen(new WorldGenSapphire(), 1);
	}

	private static void registerWorldGen(IWorldGenerator worldGenClass,
			int weightedProbability) {
		GameRegistry.registerWorldGenerator(worldGenClass, weightedProbability);
	}

}
