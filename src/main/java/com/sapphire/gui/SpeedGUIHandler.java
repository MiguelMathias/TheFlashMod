package com.sapphire.gui;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class SpeedGUIHandler
{
	public static SpeedGUIHandler instance = new SpeedGUIHandler();
	private static Minecraft mc = Minecraft.getMinecraft();
	
	@SubscribeEvent
	public void RenderGameOverlayEvent(RenderGameOverlayEvent event)
	{
		
		// render everything onto the screen
		if (event.type == RenderGameOverlayEvent.ElementType.TEXT)
		{
			SpeedGUI.renderToHud();
		}
	}
}
