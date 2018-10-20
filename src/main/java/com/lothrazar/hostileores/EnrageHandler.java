package com.lothrazar.hostileores;

import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EnrageHandler {

  private ConfigManager config;

  public EnrageHandler(ConfigManager config) {
    this.config = config;
  }

  @SubscribeEvent
  public void onHarvestDropsEvent(HarvestDropsEvent event) {
    if (event.getState() != null) {
      IBlockState blockstate = event.getState();
      BlockPos pos = event.getPos();
      World world = event.getWorld();
      String blockId = blockstate.getBlock().getRegistryName().toString();
      if (config.getBlockIdsToTrigger().contains(blockId) &&
      //          event.getWorld().provider.getDimension() == NETHER &&  
          world.rand.nextDouble() * 100 < config.getPercent()) {
        // then look for one
        AxisAlignedBB region = AngerUtils.makeBoundingBox(pos.getX(), pos.getY(), pos.getZ(),
            config.getRangeAngerHorizontal(),
            config.getRangeAngerVertical());
        List<EntityPigZombie> found = world.getEntitiesWithinAABB(EntityPigZombie.class, region);
        for (EntityPigZombie pz : found) {
          if (pz.isAngry() == false) {
            AngerUtils.makeAngry(event.getHarvester(), pz);
            if (config.isSendChat()) {
              AngerUtils.sendStatus(event.getHarvester(), "angermanagement.angry");
            }
            break;// one will alert others, its enough 
          }
        }
      }
    }
  }
}
