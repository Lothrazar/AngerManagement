package com.lothrazar.hostileores;

import java.util.List;
import org.apache.logging.log4j.Logger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
    MinecraftForge.EVENT_BUS.register(new EnrageHandler());
    MinecraftForge.EVENT_BUS.register(new CalmingHandler());
  }


  public static AxisAlignedBB makeBoundingBox(double x, double y, double z, int hRadius, int vRadius) {
    return new AxisAlignedBB(x - hRadius, y - vRadius, z - hRadius, x + hRadius, y + vRadius, z + hRadius);
  }

  @SubscribeEvent
  public void onPlayerHurt(LivingHurtEvent event) {
    if (config.calmingOnDeathEnabled && event.getEntityLiving().getHealth() - event.getAmount() <= 0 &&
        event.getEntityLiving() instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) event.getEntityLiving();
      BlockPos pos = player.getPosition();
      World world = player.world;
      //  go make unhostile    
      AxisAlignedBB region = makeBoundingBox(pos.getX(), pos.getY(), pos.getZ(),
          config.rangeAngerHorizontal, config.percent);
      List<EntityPigZombie> found = world.getEntitiesWithinAABB(EntityPigZombie.class, region);
      int triggered = 0;
      for (EntityPigZombie pz : found) {
        if (pz.isAngry()) {
          triggered++;
          this.makeCalm(player, pz);
        }
      }
      if (triggered > 0 && config.sendChat)
        this.sendMessage(player, "angermanagement.calm");
    }
  }

  @SubscribeEvent
  public void onHarvestDropsEvent(HarvestDropsEvent event) {
    if (event.getState() != null) {
      IBlockState blockstate = event.getState();
      BlockPos pos = event.getPos();
      World world = event.getWorld();
      String blockId = blockstate.getBlock().getRegistryName().toString();
      if (config.blockIdsToTrigger.contains(blockId) &&
      //          event.getWorld().provider.getDimension() == NETHER &&  
          world.rand.nextDouble() * 100 < config.percent) {
        // then look for one
        AxisAlignedBB region = makeBoundingBox(pos.getX(), pos.getY(), pos.getZ(),
            config.rangeAngerHorizontal, config.rangeCalmingVertical);
        List<EntityPigZombie> found = world.getEntitiesWithinAABB(EntityPigZombie.class, region);
        for (EntityPigZombie pz : found) {
          if (pz.isAngry() == false) {
            //could use .becomeAngryAt() but it is private 
            makeAngry(event.getHarvester(), pz);
            break;// one will alert others, its enough 
          }
        }
      }
    }
  }

  private void makeCalm(EntityPlayer player, EntityPigZombie pz) {
    //need to set anger timer
    NBTTagCompound sandbox = new NBTTagCompound();
    pz.writeEntityToNBT(sandbox);
    sandbox.setShort("Anger", (short) 0);
    sandbox.setString("HurtBy", "");
    pz.readEntityFromNBT(sandbox);
  }

  private void makeAngry(EntityPlayer event, EntityPigZombie pz) {
    pz.attackEntityFrom(DamageSource.causePlayerDamage(event), 0);
    this.sendStatus(event, "angermanagement.angry");
  }

  private void sendMessage(EntityPlayer player, String string) {
    if (config.sendChat)
      player.sendMessage(AngerUtils.langTr(string));
  }

  private void sendStatus(EntityPlayer event, String string) {
    if (config.sendChat)
      event.sendStatusMessage(AngerUtils.langTr(string), true);
  }
}
