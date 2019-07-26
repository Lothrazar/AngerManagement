package com.lothrazar.hostileores;

import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.entity.monster.ZombiePigmanEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EnrageHandler {

  private ConfigManager config;

  public EnrageHandler(ConfigManager config) {
    this.config = config;
  }

  @SubscribeEvent
  public void onLivingUpdateEvent(LivingUpdateEvent event) {
    if (config.isPacifyIronGolems()
        && event.getEntityLiving() instanceof IronGolemEntity
        && event.getEntityLiving().getAttackingEntity() instanceof PlayerEntity) {
      AngerUtils.makeCalmGolem((IronGolemEntity) event.getEntityLiving());
    }
  }

  @SubscribeEvent
  public void onLivingDamageEvent(LivingDamageEvent event) {
    if (config.isPacifyIronGolems()
        && event.getEntityLiving() instanceof PlayerEntity &&
        event.getSource() != null &&
        event.getSource().getTrueSource() instanceof IronGolemEntity) {
      //golem attacked player  
      event.setAmount(0);
      event.setCanceled(true);
      ModAngerManagement.log("set damage to zero from golem at  " + event.getSource().getTrueSource().getPosition());
    }
  }

  Random rand = new Random();
  @SubscribeEvent
  public void onHarvestDropsEvent(HarvestDropsEvent event) {
    if (event.getState() == null) {
      return;
    }
    BlockState blockstate = event.getState();
    BlockPos pos = event.getPos();
    IWorld world = event.getWorld();
    ResourceLocation blockId = blockstate.getBlock().getRegistryName();
    boolean matched = UtilString.isInList(config.getBlockIdsToTrigger(), blockstate.getBlock().getRegistryName());
    if (!matched) {
      return;
    }
    double diceRoll = rand.nextDouble() * 100;
    //          event.getWorld().provider.getDimension() == NETHER &&  
    ModAngerManagement.log(blockId + " did match and diceroll is  " + diceRoll + " < " + config.getPercent());
    //TODO: dimension config 
    if (diceRoll < config.getPercent()) {
      // then look for one
      AxisAlignedBB region = AngerUtils.makeBoundingBox(pos.getX(), pos.getY(), pos.getZ(),
          config.getRangeAngerHorizontal(),
          config.getRangeAngerVertical());
      List<ZombiePigmanEntity> found = world.getEntitiesWithinAABB(ZombiePigmanEntity.class, region);
      for (ZombiePigmanEntity pz : found) {
        if (AngerUtils.isAngry(pz)  == false) {
          AngerUtils.makeAngry(event.getHarvester(), pz);
          break;// one will alert others, its enough 
        }
      }
    }
  }
}
