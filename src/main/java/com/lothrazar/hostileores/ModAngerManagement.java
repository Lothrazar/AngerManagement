package com.lothrazar.hostileores;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import sun.security.krb5.Config;

@Mod(ModAngerManagement.MODID)
public class ModAngerManagement {

  String certificateFingerprint = "@FINGERPRINT@";
  public static final String MODID = "angermanagement";
  public static final int NETHER = -1;
  public static final Logger LOGGER = LogManager.getLogger();
  public static ConfigManager config;

  public ModAngerManagement() {
     config = new ConfigManager();
    //    config.initConfig(new Configuration(event.getSuggestedConfigurationFile()));
    //    logger = event.getModLog();
    // Register the setup method for modloading
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    //only for server starting
    MinecraftForge.EVENT_BUS.register(this);
    MinecraftForge.EVENT_BUS.register(new EnrageHandler(config));
    MinecraftForge.EVENT_BUS.register(new CalmingHandler(config));
  }

  private void setup(FMLCommonSetupEvent event) {
    TileEntity bob;
  }

  @SubscribeEvent
  public void onFingerprintViolation(FMLFingerprintViolationEvent event) {
    // https://tutorials.darkhax.net/tutorials/jar_signing/
    String source = (event.getSource() == null) ? "" : event.getSource().getName() + " ";
    String msg = MODID + " Invalid fingerprint detected! The file " + source + "may have been tampered with. This version will NOT be supported by the author!";
    if (LOGGER == null) {
      System.out.println(msg);
    }
    else {
      LOGGER.error(msg);
    }
  }

  public static void log(String string) {
if(config.isLogEverything())
      LOGGER.info(string);

  }
}
