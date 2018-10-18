package com.lothrazar.hostileores;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

import org.apache.logging.log4j.Logger;

@Mod(modid = ModHostileMiners.MODID)
public class ModHostileMiners {
	public static final String MODID = "hostileores";

	public static final int nether = -1;
	private static Logger logger;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		MinecraftForge.EVENT_BUS.register(this);
	}

	public static AxisAlignedBB makeBoundingBox(double x, double y, double z, int hRadius, int vRadius) {
		return new AxisAlignedBB(x - hRadius, y - vRadius, z - hRadius, x + hRadius, y + vRadius, z + hRadius);
	}

	@SubscribeEvent
	public void onHarvestDropsEvent(HarvestDropsEvent event) {
		if (event.getState() != null) {
			IBlockState blockstate = event.getState();
			BlockPos pos = event.getPos();
			World world = event.getWorld();

			if (blockstate.getBlock() == Blocks.QUARTZ_ORE && // TODO config list
					event.getWorld().provider.getDimension() == nether && world.rand.nextDouble() < 0.2) {
				// then look for one
				AxisAlignedBB range = makeBoundingBox(pos.getX(), pos.getY(), pos.getZ(), 3, 16);
				List<EntityPigZombie> found = world.getEntitiesWithinAABB(EntityPigZombie.class, range);
				for (EntityPigZombie pz : found) {
					if (pz.isAngry() == false) {
						logger.info("ENRAGE");
						pz.attackEntityFrom(DamageSource.causePlayerDamage(event.getHarvester()), 0);
						// one enraged, is enough
						break;
					}
				}
			}
		}
	}
}
