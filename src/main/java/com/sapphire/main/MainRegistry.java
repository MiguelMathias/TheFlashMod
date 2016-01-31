/*
If this comment is removed, the program will blow up 
       ,~~.
      (  6 )-_,
 (\___ )=='-'
  \ .   ) )
   \ `-' /
~'`~'`~'`~'`~
*/

package com.sapphire.main;

import com.sapphire.blocks.SapphireBlocks;
import com.sapphire.gui.SpeedGUIHandler;
import com.sapphire.handlers.CraftingHandler;
import com.sapphire.handlers.KeysHandler;
import com.sapphire.handlers.SpeedForceHandler;
import com.sapphire.items.FlashItems;
import com.sapphire.lib.Abilities;
import com.sapphire.lib.GameRules;
import com.sapphire.lib.RefStrings;
import com.sapphire.world.SapphireWorld;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.Metadata;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.potion.Potion;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.lang.reflect.Field;

@Mod(modid = RefStrings.MODID, version = RefStrings.VERSION, name = RefStrings.NAME)

public class MainRegistry
{
	@SidedProxy(clientSide = "com.sapphire.main.ClientProxy", serverSide = "com.sapphire.main.ServerProxy") public static ServerProxy proxy;
	
	// public static final SimpleNetworkWrapper NETWORK;
	
	@Metadata public static ModMetadata meta;
	
	@Instance(RefStrings.MODID) public static MainRegistry modInstance;
	
	// public static Achievement achievementFlash;
	
	public static Potion speedForce;
	public static int speedForcePotionID;
	public static Potion speedStrength;
	public static int strengthPotionID;
	public static Potion intangPotion;
	public static int speedPhasing;
	public static Potion speedJump;
	public static int jumpPotionID;
	public static Potion speedDigging;
	public static int speedDigID;
	public static Potion waterRunning;

	public static boolean usable()
	{
		return Minecraft.getMinecraft().isSingleplayer();
	}
	
	public void initConfiguration(FMLPreInitializationEvent preEvent)
	{
		Configuration config = new Configuration(new File("config/config.cfg"));
		config.load();
		speedForcePotionID = config
				.getInt("Speed Force ID", "Abilities", 80, 0, Integer.MAX_VALUE, "Speed Force Ability ID");
		strengthPotionID = config.getInt("Strength ID", "Abilities", 84, 0, Integer.MAX_VALUE, "Strength Ability ID");
		speedPhasing = config
				.getInt("Intangibility ID", "Abilities", 78, 0, Integer.MAX_VALUE, "Intangibility Ability ID");
		jumpPotionID = config.getInt("Jump ID", "Abilities", 80, 0, Integer.MAX_VALUE, "Jump Ability ID");
		speedDigID = config.getInt("Speed Dig ID", "Abilities", 80, 0, Integer.MAX_VALUE, "Speed Dig Ability ID");

		GameRules.isSpeedLimit = config
				.getBoolean("Speed Limit", "Game Rules", false, "Is there a speed limit on characters?");
		GameRules.isBalanced = config
				.getBoolean("Balanced", "Game Rules", true, "Are characters balanced with each other?");

		FlashItems.flashDurability = 6250;

		config.save();
	}
	
	@EventHandler public void PreLoad(FMLPreInitializationEvent preEvent)
	{
		this.initConfiguration(preEvent);

		SapphireBlocks.mainRegistry();
		FlashItems.mainRegistry();
		CraftingHandler.mainRegistry();
		SapphireWorld.mainRegistry();
		KeysHandler.init();

		proxy.registerRenderThings();

		boolean modEntityID = false;
		Potion[] potionTypes = null;
		for (Field f : Potion.class.getDeclaredFields())
		{
			f.setAccessible(true);
			try
			{
				if (!f.getName().equals("potionTypes") && !f.getName().equals("field_76425_a"))
					continue;
				Field modfield = Field.class.getDeclaredField("modifiers");
				modfield.setAccessible(true);
				modfield.setInt(f, f.getModifiers() & -17);
				potionTypes = (Potion[]) f.get(null);
				Potion[] newPotionTypes = new Potion[256];
				System.arraycopy(potionTypes, 0, newPotionTypes, 0, potionTypes.length);
				f.set(null, newPotionTypes);
				continue;
			}
			catch (Exception e)
			{
				System.err.println("Severe error, please report this to the mod author:");
				System.err.println(e);
			}
		}

		FMLCommonHandler.instance().bus().register(new KeysHandler());
		MinecraftForge.EVENT_BUS.register(new SpeedForceHandler());

	}
	
	@EventHandler public void load(FMLInitializationEvent event)
	{
		// proxy.registerRenderThings();

		// AchievementPage.registerAchievementPage(new AchievementPage("Flash
		// Achievements", new Achievement[]
		// { achievementFlash }));

		speedForce = (new Abilities(1, false, 0)).setPotionName("potion.speedForce")
				.func_111184_a(SharedMonsterAttributes.movementSpeed, "91AEAA56-376B-4498-935B-2F7F68070635",
						0.20000000298023224D, 2);
		speedStrength = new Abilities(strengthPotionID, false, 0).setPotionName("potion.strongPotion")
				.func_111184_a(SharedMonsterAttributes.attackDamage, "648D7064-6A60-4F59-8ABE-C2C23A6DD7A9", 3.0, 1);
		intangPotion = new Abilities(speedPhasing, false, 0).setPotionName("potion.intangPotion");

		speedJump = (new Abilities(8, false, 0)).setPotionName("potion.jumpPotion");
		speedDigging = (new Abilities(3, false, 0)).setPotionName("potion.digFast");
		waterRunning = (new Abilities(100, false, 0)).setPotionName("potion.speedWaterWalking");

		MinecraftForge.EVENT_BUS.register(new SpeedGUIHandler());
	}
	
	@EventHandler public void PostLoad(FMLPostInitializationEvent postEvent)
	{

	}
}
