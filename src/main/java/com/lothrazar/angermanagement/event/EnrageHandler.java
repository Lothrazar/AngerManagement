package com.lothrazar.angermanagement.event;

import java.util.Random;
import com.lothrazar.angermanagement.ModAngerManagement;
import com.lothrazar.angermanagement.config.ConfigManager;
import com.lothrazar.angermanagement.util.AngerUtils;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EnrageHandler {

  private final Random rand = new Random();
  private ConfigManager config;

  public EnrageHandler(ConfigManager config) {
    this.config = config;
  }

  @SubscribeEvent
  public void onLivingUpdateEvent(LivingUpdateEvent event) {
    if (config.isPacifyIronGolems() && event.getEntityLiving() instanceof IronGolemEntity
        && event.getEntityLiving().getAttackingEntity() instanceof PlayerEntity) {
      AngerUtils.makeCalmGolem((IronGolemEntity) event.getEntityLiving());
    }
  }

  @SubscribeEvent
  public void onLivingDamageEvent(LivingDamageEvent event) {
    if (config.isPacifyIronGolems() && event.getEntityLiving() instanceof PlayerEntity && event.getSource() != null
        && event.getSource().getTrueSource() instanceof IronGolemEntity) {
      // golem attacked player
      event.setAmount(0);
      event.setCanceled(true);
      ModAngerManagement.log("set damage to zero from golem at  " + event.getSource().getTrueSource().func_233580_cy_());
    }
  }
  // 
  //
  //  @SubscribeEvent
  //  public void onPlayerHurt(LivingHurtEvent event) {
  //    if (config.isCalmingOnDeathEnabled() &&
  //        event.getEntityLiving().getHealth() - event.getAmount() <= 0 &&
  //        event.getEntityLiving() instanceof PlayerEntity) {
  //      PlayerEntity player = (PlayerEntity) event.getEntityLiving();
  //      BlockPos pos = player.func_233580_cy_();
  //      World world = player.world;
  //      // go make unhostile
  //      AxisAlignedBB region = AngerUtils.makeBoundingBox(pos.getX(), pos.getY(), pos.getZ(),
  //          config.getRangeCalmingHorizontal(), config.getRangeCalmingVertical());
  //      List<PiglinEntity> found = world.getEntitiesWithinAABB(PiglinEntity.class, region);
  //      int triggered = 0;
  //      for (PiglinEntity pz : found) {
  //        //  if (AngerUtils.isAngry(pz)) {
  //        triggered++;
  //        AngerUtils.makeCalm(player, pz);
  //        //  }
  //      }
  //      ModAngerManagement.log("is angry?  calm triggered  " + triggered);
  //      if (triggered > 0) {
  //        // particles?
  //      }
  //    }
  //  }
}
