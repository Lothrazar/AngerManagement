package com.lothrazar.hostileores;

import org.apache.logging.log4j.Logger;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = ModHostileMiners.MODID)
public class ModHostileMiners {

  public static final String MODID = "angermanagement";
  public static final int NETHER = -1;
  private static Logger logger;
  private ConfigManager config;

  @EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    this.config = new ConfigManager();
    config.initConfig(new Configuration(event.getSuggestedConfigurationFile()));
    logger = event.getModLog();
    MinecraftForge.EVENT_BUS.register(this);
    MinecraftForge.EVENT_BUS.register(new EnrageHandler(config));
    MinecraftForge.EVENT_BUS.register(new CalmingHandler(config));
  }
}
