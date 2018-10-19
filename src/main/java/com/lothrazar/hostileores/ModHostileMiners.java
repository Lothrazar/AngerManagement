package com.lothrazar.hostileores;

import java.util.List;
import org.apache.logging.log4j.Logger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = ModHostileMiners.MODID)
public class ModHostileMiners {
	public static final String MODID = "hostileores";

	public static final int nether = -1;
	private static Logger logger;

	private String[] blockIdsToTrigger;
  private String[] potionEffectWhenAngered;
  private int percent;
  private int rangeHorizontal = 3;
  private int rangeVertical = 16;
  private int maxNumberPerSearchedOre = 1;
  private int maxNumberPerTriggeredOre = 1;
 
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		initConfig(new Configuration(event.getSuggestedConfigurationFile()));
		   
		logger = event.getModLog();
		MinecraftForge.EVENT_BUS.register(this);
	}

	private void initConfig(Configuration config) {
		
		String[] defaults= new String[] {
				"minecraft:quartz_ore",
				"cyclicmagic:nether_gold_ore"
				//tinkers cobaalt
				//tinkers a 
				// nether ore from plant mod
				
				
		};
    this.blockIdsToTrigger = config.getStringList("blocks mined", MODID, defaults, "List of blocks that will cause anger when mined.  ");
    this.percent = config.getInt("Percent chance of anger", MODID, 50, 1, 100, "What percent (%) chance each mob has to be angered, when breaking ore is noticed by all nearby");
    defaults = new String[] {
        "minecraft:slowness", };
    this.potionEffectWhenAngered = config.getStringList("blocks mined", MODID, defaults, "List of blocks that will cause anger when mined.  ");
    config.save();
	//tolist
		
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
			String blockId = blockstate.getBlock().getRegistryName().toString();
// blockId if contains
			if (blockstate.getBlock() == Blocks.QUARTZ_ORE && // TODO config list
          event.getWorld().provider.getDimension() == nether && world.rand.nextDouble() * 100 < this.percent) {
				// then look for one
        AxisAlignedBB region = makeBoundingBox(pos.getX(), pos.getY(), pos.getZ(), rangeHorizontal, rangeVertical);
				List<EntityPigZombie> found = world.getEntitiesWithinAABB(EntityPigZombie.class, region);
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
