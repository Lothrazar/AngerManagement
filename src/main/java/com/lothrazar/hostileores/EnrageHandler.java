package com.lothrazar.hostileores;

import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EnrageHandler {

  private ConfigManager config;

  public EnrageHandler(ConfigManager config) {
    this.config = config;
  }

  @SubscribeEvent
  public void onLivingUpdateEvent(LivingUpdateEvent event) {
    if (config.isPacifyIronGolems()
        && event.getEntityLiving() instanceof EntityIronGolem
        && event.getEntityLiving().getAttackingEntity() instanceof EntityPlayer) {
      AngerUtils.makeCalmGolem((EntityIronGolem) event.getEntityLiving());
    }
  }

  @SubscribeEvent
  public void onLivingDamageEvent(LivingDamageEvent event) {
    if (config.isPacifyIronGolems()
        && event.getEntityLiving() instanceof EntityPlayer &&
        event.getSource() != null &&
        event.getSource().getTrueSource() instanceof EntityIronGolem) {
      //golem attacked player  
      event.setAmount(0);
      event.setCanceled(true);
      ModAngerManagement.log("set damage to zero from golem at  " + event.getSource().getTrueSource().getPosition());
    }
  }

  @SubscribeEvent
  public void onHarvestDropsEvent(HarvestDropsEvent event) {
    if (event.getState() == null) {
      return;
    }
    IBlockState blockstate = event.getState();
    BlockPos pos = event.getPos();
    World world = event.getWorld();
    ResourceLocation blockId = blockstate.getBlock().getRegistryName();
    boolean matched = UtilString.isInList(config.getBlockIdsToTrigger(), blockstate.getBlock().getRegistryName());
    if (!matched) {
      return;
    }
    double diceRoll = world.rand.nextDouble() * 100;
    //          event.getWorld().provider.getDimension() == NETHER &&  
    ModAngerManagement.log(blockId + " did match and diceroll is  " + diceRoll + " < " + config.getPercent());
    //TODO: dimension config 
    if (diceRoll < config.getPercent()) {
      // then look for one
      AxisAlignedBB region = AngerUtils.makeBoundingBox(pos.getX(), pos.getY(), pos.getZ(),
          config.getRangeAngerHorizontal(),
          config.getRangeAngerVertical());
      List<EntityPigZombie> found = world.getEntitiesWithinAABB(EntityPigZombie.class, region);
      for (EntityPigZombie pz : found) {
        if (pz.isAngry() == false) {
          AngerUtils.makeAngry(event.getHarvester(), pz);
          break;// one will alert others, its enough 
        }
      }
    }
  }
}
