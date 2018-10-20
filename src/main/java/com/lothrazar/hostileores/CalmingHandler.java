package com.lothrazar.hostileores;

import java.util.List;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CalmingHandler {

  private ConfigManager config;

  public CalmingHandler(ConfigManager config) {
    this.config = config;
  }


  @SubscribeEvent
  public void onPlayerHurt(LivingHurtEvent event) {
    if (config.isCalmingOnDeathEnabled() && event.getEntityLiving().getHealth() - event.getAmount() <= 0 &&
        event.getEntityLiving() instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) event.getEntityLiving();
      BlockPos pos = player.getPosition();
      World world = player.world;
      //  go make unhostile    
      AxisAlignedBB region = AngerUtils.makeBoundingBox(pos.getX(), pos.getY(), pos.getZ(),
          config.getRangeCalmingHorizontal(),
          config.getRangeCalmingVertical());
      List<EntityPigZombie> found = world.getEntitiesWithinAABB(EntityPigZombie.class, region);
      int triggered = 0;
      for (EntityPigZombie pz : found) {
        if (pz.isAngry()) {
          triggered++;
          AngerUtils.makeCalm(player, pz);
        }
      }
      if (triggered > 0 && config.isSendChat())
        AngerUtils.sendMessage(player, "angermanagement.calm");
    }
  }
}
