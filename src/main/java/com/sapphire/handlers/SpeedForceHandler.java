/* WARNING: The code that follows will make you cry;
 * 		a safety pig is provided below for your benefit.
 * 
 *                          _
 _._ _..._ .-',     _.._(`))
'-. `     '  /-._.-'    ',/
   )         \            '.
  / _    _    |             \
 |  a    a   /              |
 \   .-.                     ;  
  '-('' ).-'       ,'       ;
     '-;           |      .'
        \           \    /
        | 7  .__  _.-\   \
        | |  |  ``/  /`  /
       /,_|  |   /,_/   /
          /,_/      '`-'
 * 
 * Feel free to use the safety pig whenever it suits you best.
 */

package com.sapphire.handlers;

import com.sapphire.armor.FlashArmor;
import com.sapphire.armor.sapphireAbstract;
import com.sapphire.main.MainRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;

import java.util.List;

/*
 * features list:
 * running speed
 * running on water
 * running on air (traditional and new way)
 * jumping higher
 * stronger
 * stronger as speed increases
 * running up walls
 * fall damage not taken
 * speed digging
 * phasing
 * slow time
 * step helper
 * tornadoes
 * vortexes
 * rapid regeneration
 * 
 * need:
 * lightning throw
 * steal speed / stop kinetic motion (in work)
 * lightning trail
 * 
 */

public class SpeedForceHandler
{
	public static int upperSpeedLimit = 64;
	public static int lowerSpeedLimit = 0;

	public static float upperJumpMoveLimit = 0.64F;
	public static float lowerJumpMoveLimit = 0.04F;
	public static float gameSpeed = SlowTime.getGameSpeed();
	public static float slowMoFactor = 0.0F;
	public static float slowMoUpperLimit = 0.9F;
	public static float sloMoLowerLimit = 0.0F;

	public static boolean waterRunningUnlocked = true;
	public static boolean wallRunningUnlocked = true;
	public static boolean tornadoesAndVortexesUnlocked = true;
	public static boolean phasingUnlocked = true;
	public static boolean flyingUnlocked = true;
	public static boolean betterFlyingUnlocked = true;
	public static boolean lightningThrowUnlocked = true;

	public static double speed;

	@SubscribeEvent public void onEntityUpdate(LivingEvent.LivingUpdateEvent event)
	{
		if (event.entityLiving.isPotionActive(MainRegistry.speedForce) && event.entity instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) event.entity;

			// speed
			speed = player.motionY;

			World world1 = player.worldObj;
			int x1 = MathHelper.floor_double(player.posX);
			int y1 = MathHelper.floor_double(player.boundingBox.minY);
			int z1 = MathHelper.floor_double(player.posZ);

			// water running
			if (world1.getBlock(x1, y1 - 1, z1).getMaterial() == Material.water && player
					.isPotionActive(MainRegistry.waterRunning) && FlashArmor.flashFactor >= 1
					|| player.isInWater() && SpeedForceHandler.waterRunningUnlocked)
			{
				player.motionY = 0.0D;
				player.capabilities.isFlying = true;
				player.isInWater();

				if (KeysHandler.Z_KEY.getIsKeyPressed() && !Minecraft.getMinecraft().ingameGUI.getChatGUI()
						.getChatOpen() && Minecraft.getMinecraft().currentScreen == null)
				{
					player.motionY = -0.4D;
				}
				if (KeysHandler.R_KEY.getIsKeyPressed() && !Minecraft.getMinecraft().ingameGUI.getChatGUI()
						.getChatOpen() && Minecraft.getMinecraft().currentScreen == null)
				{
					player.jump();
				}
			}
			else
			{
				player.capabilities.isFlying = false;
			}
			// speed fallling while in slo-mo
			if (slowMoFactor > 0 && player.motionY <= -0.000000000000001 || player.isSneaking())
			{
				player.motionY -= slowMoFactor * 1.111111111111111111111111111111;
			}

			// rapid regeneration
			if (player.getHealth() < player.getMaxHealth())
			{
				player.heal((float) (player.getActivePotionEffect(MainRegistry.speedStrength).getAmplifier() * 0.0125));
				player.hurtTime = 0;
				player.maxHurtTime = 0;
			}
			if (player.getCurrentArmor(3) != null && player.getCurrentArmor(2) != null
					&& player.getCurrentArmor(1) != null && player.getCurrentArmor(0) != null && player
					.getCurrentArmor(3).getItem() instanceof sapphireAbstract && player.getCurrentArmor(2)
					.getItem() instanceof sapphireAbstract && player.getCurrentArmor(1)
					.getItem() instanceof sapphireAbstract && player.getCurrentArmor(0)
					.getItem() instanceof sapphireAbstract)
			{
				if (player.worldObj.isRemote)
				{
					// step helper
					player.stepHeight = player.isCollidedHorizontally && player.onGround ? 1.5f : 0.5f;

					// increase speed
					if (KeysHandler.UP_KEY.getIsKeyPressed() && !Minecraft.getMinecraft().ingameGUI.getChatGUI()
							.getChatOpen() && Minecraft.getMinecraft().currentScreen == null)
					{
						if (FlashArmor.flashFactor < upperSpeedLimit)
						{
							FlashArmor.flashFactor += 1;
						}

						if (FlashArmor.jumpFactor < upperJumpMoveLimit)
						{
							FlashArmor.jumpFactor += 0.02F;
						}

						if (FlashArmor.jumpFactor >= upperJumpMoveLimit)
						{
							FlashArmor.jumpFactor = upperJumpMoveLimit;
						}

					}
					// decrease speed
					if (KeysHandler.DOWN_KEY.getIsKeyPressed() && !Minecraft.getMinecraft().ingameGUI.getChatGUI()
							.getChatOpen() && Minecraft.getMinecraft().currentScreen == null)
					{
						if (FlashArmor.flashFactor >= lowerSpeedLimit)
						{
							FlashArmor.flashFactor -= 1;
						}
						if (FlashArmor.flashFactor <= lowerSpeedLimit || FlashArmor.flashFactor < 1
								|| FlashArmor.flashFactor == -1)
						{
							FlashArmor.flashFactor = lowerSpeedLimit;
						}

						if (FlashArmor.jumpFactor > lowerJumpMoveLimit)
						{
							FlashArmor.jumpFactor -= 0.02F;
						}

						if (FlashArmor.jumpFactor <= lowerJumpMoveLimit)
						{
							FlashArmor.jumpFactor = lowerJumpMoveLimit;
						}
					}
					// speed max
					if (KeysHandler.PGUP_KEY.getIsKeyPressed())
					{
						FlashArmor.flashFactor = upperSpeedLimit;
						FlashArmor.jumpFactor = upperJumpMoveLimit;
					}
					// speed min
					if (KeysHandler.PGDOWN_KEY.getIsKeyPressed())
					{
						FlashArmor.flashFactor = lowerSpeedLimit + 1;
						FlashArmor.jumpFactor = lowerJumpMoveLimit + 0.02F;
					}
					// wall running
					if (FlashArmor.flashFactor >= 1 && player.isCollidedHorizontally && KeysHandler.R_KEY
							.getIsKeyPressed() && !Minecraft.getMinecraft().ingameGUI.getChatGUI().getChatOpen()
							&& Minecraft.getMinecraft().currentScreen == null)
					{
						player.motionY += 0.175;
					}
					//extinguish all fires by vibration
					if (FlashArmor.flashFactor >= 1 && player.isBurning())
					{
						player.extinguish();
					}
					if (KeysHandler.F_KEY.getIsKeyPressed() && !Minecraft.getMinecraft().ingameGUI.getChatGUI()
							.getChatOpen() && Minecraft.getMinecraft().currentScreen == null)
					{
						player.addPotionEffect(new PotionEffect(MainRegistry.intangPotion.id, 200, 0));
					}
					if (!KeysHandler.F_KEY.getIsKeyPressed() && !Minecraft.getMinecraft().ingameGUI.getChatGUI()
							.getChatOpen() && Minecraft.getMinecraft().currentScreen == null)
					{
						player.removePotionEffect(MainRegistry.intangPotion.id);
					}
				}
				// slow time perspective
				float speed = Float.valueOf(Float.toString(this.gameSpeed - this.slowMoFactor).substring(0, 3))
						.floatValue();
				if (KeysHandler.C_KEY.getIsKeyPressed() && !Minecraft.getMinecraft().ingameGUI.getChatGUI()
						.getChatOpen() && Minecraft.getMinecraft().currentScreen == null)
				{
					slowMoFactor += 0.04F;
					if (slowMoFactor >= slowMoUpperLimit)
					{
						slowMoFactor = slowMoUpperLimit;
					}
					// increase fall speed while in slo-mo (in progress)

					SlowTime.setGameSpeed(speed);
					Minecraft.getMinecraft().currentScreen = null;
					player.arrowHitTimer = (int) (SlowTime.getGameSpeed() * 50.0F);
				}
				if (KeysHandler.X_KEY.getIsKeyPressed() && !Minecraft.getMinecraft().ingameGUI.getChatGUI()
						.getChatOpen() && Minecraft.getMinecraft().currentScreen == null)
				{
					slowMoFactor -= 0.04F;
					if (slowMoFactor <= sloMoLowerLimit)
					{
						slowMoFactor = sloMoLowerLimit;
					}

					SlowTime.setGameSpeed(speed);
					Minecraft.getMinecraft().currentScreen = null;
				}
				// phase
				if (player.isCollidedHorizontally && player.isPotionActive(MainRegistry.intangPotion) && phasingUnlocked
						&& FlashArmor.flashFactor >= 1)
				{
					double direction = MathHelper.floor_double(player.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;

					World world2 = player.worldObj;
					Block block;

					if (direction >= 0 && direction < 1)
					{
						block = world2.getBlock(x1, y1, z1 + 1);
						if (block.getMaterial() != Material.air)
						{
							block = world2.getBlock(x1, y1, z1 + 2);
							if (block.getMaterial() != Material.air)
							{
								block = world2.getBlock(x1, y1, z1 + 3);
								if (block.getMaterial() != Material.air)
								{
									block = world2.getBlock(x1, y1, z1 + 4);
									if (block.getMaterial() != Material.air)
									{
										block = world2.getBlock(x1, y1, z1 + 5);
										if (block.getMaterial() != Material.air)
										{
											block = world2.getBlock(x1, y1, z1 + 6);
											if (block.getMaterial() != Material.air)
											{
												block = world2.getBlock(x1, y1, z1 + 7);
												if (block.getMaterial() != Material.air)
												{
													block = world2.getBlock(x1, y1, z1 + 8);
													if (block.getMaterial() != Material.air)
													{
														block = world2.getBlock(x1, y1, z1 + 9);
														if (block.getMaterial() != Material.air)
														{
															block = world2.getBlock(x1, y1, z1 + 10);
														}
														else if (block.getMaterial() == Material.air)
														{
															player.setPosition(player.posX, player.posY,
																	player.posZ + 9);
														}
													}
													else if (block.getMaterial() == Material.air)
													{
														player.setPosition(player.posX, player.posY, player.posZ + 8);
													}
												}
												else if (block.getMaterial() == Material.air)
												{
													player.setPosition(player.posX, player.posY, player.posZ + 7);
												}
											}
											else if (block.getMaterial() == Material.air)
											{
												player.setPosition(player.posX, player.posY, player.posZ + 6);
											}
										}
										else if (block.getMaterial() == Material.air)
										{
											player.setPosition(player.posX, player.posY, player.posZ + 5);
										}
									}
									else if (block.getMaterial() == Material.air)
									{
										player.setPosition(player.posX, player.posY, player.posZ + 4);
									}
								}
								else if (block.getMaterial() == Material.air)
								{
									player.setPosition(player.posX, player.posY, player.posZ + 3);
								}
							}
							else if (block.getMaterial() == Material.air)
							{
								player.setPosition(player.posX, player.posY, player.posZ + 2);
							}
						}
						else if (block.getMaterial() == Material.air)
						{
							player.setPosition(player.posX, player.posY, player.posZ + 1);
						}
					}
					else if (direction >= 1 && direction < 2)
					{
						block = world2.getBlock(x1 - 1, y1, z1);
						if (block.getMaterial() != Material.air)
						{
							block = world2.getBlock(x1 - 2, y1, z1);
							if (block.getMaterial() != Material.air)
							{
								block = world2.getBlock(x1 - 3, y1, z1);
								if (block.getMaterial() != Material.air)
								{
									block = world2.getBlock(x1 - 4, y1, z1);
									if (block.getMaterial() != Material.air)
									{
										block = world2.getBlock(x1 - 5, y1, z1);
										if (block.getMaterial() != Material.air)
										{
											block = world2.getBlock(x1 - 6, y1, z1);
											if (block.getMaterial() != Material.air)
											{
												block = world2.getBlock(x1 - 7, y1, z1);
												if (block.getMaterial() != Material.air)
												{
													block = world2.getBlock(x1 - 8, y1, z1);
													if (block.getMaterial() != Material.air)
													{
														block = world2.getBlock(x1 - 9, y1, z1);
														if (block.getMaterial() != Material.air)
														{
															block = world2.getBlock(x1 - 10, y1, z1);
														}
														else if (block.getMaterial() == Material.air)
														{
															player.setPosition(player.posX - 9, player.posY,
																	player.posZ);
														}
													}
													else if (block.getMaterial() == Material.air)
													{
														player.setPosition(player.posX - 8, player.posY, player.posZ);
													}
												}
												else if (block.getMaterial() == Material.air)
												{
													player.setPosition(player.posX - 7, player.posY, player.posZ);
												}
											}
											else if (block.getMaterial() == Material.air)
											{
												player.setPosition(player.posX - 6, player.posY, player.posZ);
											}
										}
										else if (block.getMaterial() == Material.air)
										{
											player.setPosition(player.posX - 5, player.posY, player.posZ);
										}
									}
									else if (block.getMaterial() == Material.air)
									{
										player.setPosition(player.posX - 4, player.posY, player.posZ);
									}
								}
								else if (block.getMaterial() == Material.air)
								{
									player.setPosition(player.posX - 3, player.posY, player.posZ);
								}
							}
							else if (block.getMaterial() == Material.air)
							{
								player.setPosition(player.posX - 2, player.posY, player.posZ);
							}
						}
						else if (block.getMaterial() == Material.air)
						{
							player.setPosition(player.posX - 1, player.posY, player.posZ);
						}
					}
					else if (direction >= 2 && direction < 3)
					{
						block = world2.getBlock(x1, y1, z1 - 1);
						if (block.getMaterial() != Material.air)
						{
							block = world2.getBlock(x1, y1, z1 - 2);
							if (block.getMaterial() != Material.air)
							{
								block = world2.getBlock(x1, y1, z1 - 3);
								if (block.getMaterial() != Material.air)
								{
									block = world2.getBlock(x1, y1, z1 - 4);
									if (block.getMaterial() != Material.air)
									{
										block = world2.getBlock(x1, y1, z1 - 5);
										if (block.getMaterial() != Material.air)
										{
											block = world2.getBlock(x1, y1, z1 - 6);
											if (block.getMaterial() != Material.air)
											{
												block = world2.getBlock(x1, y1, z1 - 7);
												if (block.getMaterial() != Material.air)
												{
													block = world2.getBlock(x1, y1, z1 - 8);
													if (block.getMaterial() != Material.air)
													{
														block = world2.getBlock(x1, y1, z1 - 9);
														if (block.getMaterial() != Material.air)
														{
															block = world2.getBlock(x1, y1, z1 - 10);
														}
														else if (block.getMaterial() == Material.air)
														{
															player.setPosition(player.posX, player.posY,
																	player.posZ - 9);
														}
													}
													else if (block.getMaterial() == Material.air)
													{
														player.setPosition(player.posX, player.posY, player.posZ - 8);
													}
												}
												else if (block.getMaterial() == Material.air)
												{
													player.setPosition(player.posX, player.posY, player.posZ - 7);
												}
											}
											else if (block.getMaterial() == Material.air)
											{
												player.setPosition(player.posX, player.posY, player.posZ - 6);
											}
										}
										else if (block.getMaterial() == Material.air)
										{
											player.setPosition(player.posX, player.posY, player.posZ - 5);
										}
									}
									else if (block.getMaterial() == Material.air)
									{
										player.setPosition(player.posX, player.posY, player.posZ - 4);
									}
								}
								else if (block.getMaterial() == Material.air)
								{
									player.setPosition(player.posX, player.posY, player.posZ - 3);
								}
							}
							else if (block.getMaterial() == Material.air)
							{
								player.setPosition(player.posX, player.posY, player.posZ - 2);
							}
						}
						else if (block.getMaterial() == Material.air)
						{
							player.setPosition(player.posX, player.posY, player.posZ - 1);
						}
					}
					else if (direction >= 3 && direction < 4)
					{
						block = world2.getBlock(x1 + 1, y1, z1);
						if (block.getMaterial() != Material.air)
						{
							block = world2.getBlock(x1 + 2, y1, z1);
							if (block.getMaterial() != Material.air)
							{
								block = world2.getBlock(x1 + 3, y1, z1);
								if (block.getMaterial() != Material.air)
								{
									block = world2.getBlock(x1 + 4, y1, z1);
									if (block.getMaterial() != Material.air)
									{
										block = world2.getBlock(x1 + 5, y1, z1);
										if (block.getMaterial() != Material.air)
										{
											block = world2.getBlock(x1 + 6, y1, z1);
											if (block.getMaterial() != Material.air)
											{
												block = world2.getBlock(x1 + 7, y1, z1);
												if (block.getMaterial() != Material.air)
												{
													block = world2.getBlock(x1 + 8, y1, z1);
													if (block.getMaterial() != Material.air)
													{
														block = world2.getBlock(x1 + 9, y1, z1);
														if (block.getMaterial() != Material.air)
														{
															block = world2.getBlock(x1 + 10, y1, z1);
														}
														else if (block.getMaterial() == Material.air)
														{
															player.setPosition(player.posX + 9, player.posY,
																	player.posZ);
														}
													}
													else if (block.getMaterial() == Material.air)
													{
														player.setPosition(player.posX + 8, player.posY, player.posZ);
													}
												}
												else if (block.getMaterial() == Material.air)
												{
													player.setPosition(player.posX + 7, player.posY, player.posZ);
												}
											}
											else if (block.getMaterial() == Material.air)
											{
												player.setPosition(player.posX + 6, player.posY, player.posZ);
											}
										}
										else if (block.getMaterial() == Material.air)
										{
											player.setPosition(player.posX + 5, player.posY, player.posZ);
										}
									}
									else if (block.getMaterial() == Material.air)
									{
										player.setPosition(player.posX + 4, player.posY, player.posZ);
									}
								}
								else if (block.getMaterial() == Material.air)
								{
									player.setPosition(player.posX + 3, player.posY, player.posZ);
								}
							}
							else if (block.getMaterial() == Material.air)
							{
								player.setPosition(player.posX + 2, player.posY, player.posZ);
							}
						}
						else if (block.getMaterial() == Material.air)
						{
							player.setPosition(player.posX + 1, player.posY, player.posZ);
						}
					}
				}
				// phase down
				if (player.isSneaking() && KeysHandler.F_KEY.getIsKeyPressed() && FlashArmor.flashFactor >= 1
						&& player.isCollidedVertically)
				{
					World world2 = player.worldObj;
					Block block;

					block = world2.getBlock(x1, y1 - 1, z1);
					if (block.getMaterial() != Material.air)
					{
						block = world2.getBlock(x1, y1 - 2, z1);
						if (block.getMaterial() != Material.air)
						{
							block = world2.getBlock(x1, y1 - 3, z1);
							if (block.getMaterial() != Material.air)
							{
								block = world2.getBlock(x1, y1 - 4, z1);
								if (block.getMaterial() != Material.air)
								{
									block = world2.getBlock(x1, y1 - 5, z1);
									if (block.getMaterial() != Material.air)
									{
										block = world2.getBlock(x1, y1 - 6, z1);
										if (block.getMaterial() != Material.air)
										{
											block = world2.getBlock(x1, y1 - 7, z1);
											if (block.getMaterial() != Material.air)
											{
												block = world2.getBlock(x1, y1 - 8, z1);
												if (block.getMaterial() != Material.air)
												{
													block = world2.getBlock(x1, y1 - 9, z1);
													if (block.getMaterial() != Material.air)
													{
														block = world2.getBlock(x1, y1 - 10, z1);
													}
													else if (block.getMaterial() == Material.air)
													{
														player.setPosition(player.posX, player.posY - 10, player.posZ);
													}
												}
												else if (block.getMaterial() == Material.air)
												{
													player.setPosition(player.posX, player.posY - 9, player.posZ);
												}
											}
											else if (block.getMaterial() == Material.air)
											{
												player.setPosition(player.posX, player.posY - 8, player.posZ);
											}
										}
										else if (block.getMaterial() == Material.air)
										{
											player.setPosition(player.posX, player.posY - 7, player.posZ);
										}
									}
									else if (block.getMaterial() == Material.air)
									{
										player.setPosition(player.posX, player.posY - 6, player.posZ);
									}
								}
								else if (block.getMaterial() == Material.air)
								{
									player.setPosition(player.posX, player.posY - 5, player.posZ);
								}
							}
							else if (block.getMaterial() == Material.air)
							{
								player.setPosition(player.posX, player.posY - 4, player.posZ);
							}
						}
						else if (block.getMaterial() == Material.air)
						{
							player.setPosition(player.posX, player.posY - 3, player.posZ);
						}
					}
					else if (block.getMaterial() == Material.air)
					{
						player.setPosition(player.posX, player.posY - 2, player.posZ);
					}
				}
				// fly
				if (KeysHandler.R_KEY.getIsKeyPressed() && FlashArmor.flashFactor >= 1 && flyingUnlocked && !Minecraft
						.getMinecraft().ingameGUI.getChatGUI().getChatOpen()
						&& Minecraft.getMinecraft().currentScreen == null && !player.isCollidedHorizontally)
				{
					if (FlashArmor.flashFactor >= 1 && betterFlyingUnlocked)
					{
						player.capabilities.isFlying = true;
					}
					else
					{
						player.motionY += 0.175D;
					}
				}
				// steal speed
				if (KeysHandler.G_KEY.getIsKeyPressed())
				{
					//get better list
					List<Entity> entities = player.worldObj.loadedEntityList;
					AxisAlignedBB aabb = event.entity.boundingBox;
					List<Entity> entitiesHit = player.worldObj
							.getEntitiesWithinAABB(Entity.class, aabb);

					for (int i = 0; i < entities.size(); i++)
					{
						Entity entity = entities.get(i);
						if (entity.hitByEntity(player))
						{
							entitiesHit.add(entity);
						}

						for (int j = 0; j < entitiesHit.size(); j++)
						{
							Entity entity1 = entitiesHit.get(j);
							if (entity1.hitByEntity(player))
							{
								entity.performHurtAnimation();

								entity.motionX = 0;
								entity.motionY = 0;
								entity.motionZ = 0;

								entity.setPosition(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ);

								entity.rotationYaw = entity.prevRotationYaw;
								entity.rotationPitch = entity.prevRotationPitch;
							}
						}
					}
				}
				/*
				if (KeysHandler.G_KEY.getIsKeyPressed())
				{
					World world = player.worldObj;
					int x = 3;
					int y = 3;
					int z = 3;

					List<Entity> entityList = world.getEntitiesWithinAABB(Entity.class, AxisAlignedBB
							.getBoundingBox(player.posX - x, player.posY - y, player.posZ - z, player.posX + x,
									player.posY + y, player.posZ + z));

					if (entityList.size() > 0)
					{
						for (int i = 0; i < entityList.size(); i++)
						{
							Entity entity = entityList.get(i);

							if (entity instanceof EntityPlayer)
							{

							}
							// catching arrows
							else if (entity instanceof EntityArrow)
							{
								entity.motionX = 0;
								entity.motionY = 0;
								entity.motionZ = 0;

								entity.setPosition(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ);

								entity.rotationYaw = entity.prevRotationYaw;
								entity.rotationPitch = entity.prevRotationPitch;

								entity.setDead();
								player.inventory.addItemStackToInventory(new ItemStack(Items.arrow));
							}
							else
							{
								entity.motionX = 0;
								entity.motionY = 0;
								entity.motionZ = 0;

								entity.setPosition(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ);

								entity.rotationYaw = entity.prevRotationYaw;
								entity.rotationPitch = entity.prevRotationPitch;
							}

						}
					}
				}
				*/
				// tornado blowback
				if (KeysHandler.V_KEY.getIsKeyPressed() && FlashArmor.flashFactor >= 1 && tornadoesAndVortexesUnlocked)
				{
					World world = player.worldObj;
					int x = 16;
					int y = 16;
					int z = 16;

					List<Entity> entityList = world.getEntitiesWithinAABB(Entity.class, AxisAlignedBB
							.getBoundingBox(player.posX - x, player.posY - y / 3, player.posZ - z, player.posX + x,
									player.posY + y, player.posZ + z));

					if (player.isSneaking())
					{
						if (entityList.size() > 0)
						{
							for (int i = 0; i < entityList.size(); i++)
							{
								Entity entity = entityList.get(i);

								double direction =
										MathHelper.floor_double(player.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
								int j = 1;
								if (entity instanceof EntityPlayer)
								{

								}
								else
								{
									if (direction >= 0 && direction < 1)
									{
										entity.motionZ = -j;
										entity.motionY = -j / 1.5;
										entity.rotationYaw = entity.prevRotationYaw;
										entity.rotationPitch = entity.prevRotationPitch;
									}
									else if (direction >= 1 && direction < 2)
									{
										entity.motionX = j;
										entity.motionY = -j / 1.5;
										entity.rotationYaw = entity.prevRotationYaw;
										entity.rotationPitch = entity.prevRotationPitch;
									}
									else if (direction >= 2 && direction < 3)
									{
										entity.motionZ = j;
										entity.motionY = -j / 1.5;
										entity.rotationYaw = entity.prevRotationYaw;
										entity.rotationPitch = entity.prevRotationPitch;
									}
									else if (direction >= 3 && direction < 4)
									{
										entity.motionX = -j;
										entity.motionY = -j / 1.5;
										entity.rotationYaw = entity.prevRotationYaw;
										entity.rotationPitch = entity.prevRotationPitch;
									}
								}
							}
						}
					}
					else
					{
						if (entityList.size() > 0)
						{
							for (int i = 0; i < entityList.size(); i++)
							{
								Entity entity = entityList.get(i);

								double direction =
										MathHelper.floor_double(player.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
								double j = 1;
								if (entity instanceof EntityPlayer)
								{

								}
								else
								{
									if (direction >= 0 && direction < 1)
									{
										entity.motionZ = j;
										entity.motionY = j / 1.5;
										entity.rotationYaw = entity.prevRotationYaw;
										entity.rotationPitch = entity.prevRotationPitch;
									}
									else if (direction >= 1 && direction < 2)
									{
										entity.motionX = -j;
										entity.motionY = j / 1.5;
										entity.rotationYaw = entity.prevRotationYaw;
										entity.rotationPitch = entity.prevRotationPitch;
									}
									else if (direction >= 2 && direction < 3)
									{
										entity.motionZ = -j;
										entity.motionY = j / 1.5;
										entity.rotationYaw = entity.prevRotationYaw;
										entity.rotationPitch = entity.prevRotationPitch;
									}
									else if (direction >= 3 && direction < 4)
									{
										entity.motionX = j;
										entity.motionY = j / 1.5;
										entity.rotationYaw = entity.prevRotationYaw;
										entity.rotationPitch = entity.prevRotationPitch;
									}
								}
							}
						}
					}
				}
				// speed force lightning throw
				if (KeysHandler.T_KEY.getIsKeyPressed())
				{
					double range = 0.0D;
					World world = player.worldObj;
					// world.spawnEntityInWorld(new EntityLightningThrow(world,
					// player, player.posX, player.posY + 1.0D,
					// player.posZ, player.rotationYaw, player.rotationPitch,
					// range / 16.0D));
				}

			}
		}
	}
}
