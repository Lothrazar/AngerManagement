package com.lothrazar.hostileores;

import org.apache.logging.log4j.Logger;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = ModAngerManagement.MODID, certificateFingerprint = "@FINGERPRINT@", updateJSON = "https://raw.githubusercontent.com/Lothrazar/HostileOres/master/update.json")
public class ModAngerManagement {

  @Instance(value = ModAngerManagement.MODID)
  public static ModAngerManagement instance;
  public static final String MODID = "angermanagement";
  public static final int NETHER = -1;
  public static Logger logger;
  public ConfigManager config;

  @EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    this.config = new ConfigManager();
    config.initConfig(new Configuration(event.getSuggestedConfigurationFile()));
    logger = event.getModLog();
    MinecraftForge.EVENT_BUS.register(this);
    MinecraftForge.EVENT_BUS.register(new EnrageHandler(config));
    MinecraftForge.EVENT_BUS.register(new CalmingHandler(config));
  }

  @EventHandler
  public void onFingerprintViolation(FMLFingerprintViolationEvent event) {
    // https://tutorials.darkhax.net/tutorials/jar_signing/
    String source = (event.getSource() == null) ? "" : event.getSource().getName() + " ";
    String msg = MODID + " Invalid fingerprint detected! The file " + source + "may have been tampered with. This version will NOT be supported by the author!";
    if (logger == null) {
      System.out.println(msg);
    }
    else {
      logger.error(msg);
    }
  }

  public static void log(String string) {
    if (instance.config.isLogEverything()) {
      logger.info(string);
    }
  }
}
