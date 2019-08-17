package com.lothrazar.angermanagement.event;

import java.util.List;
import java.util.Random;
import com.lothrazar.angermanagement.ModAngerManagement;
import com.lothrazar.angermanagement.config.ConfigManager;
import com.lothrazar.angermanagement.util.AngerUtils;
import com.lothrazar.angermanagement.util.UtilString;
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
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.BlockEvent;
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
      ModAngerManagement
          .log("set damage to zero from golem at  " + event.getSource().getTrueSource().getPosition());
    }
  }

  @SubscribeEvent
  public void onBreak(BlockEvent.BreakEvent event) {
    ModAngerManagement.log("BreakEvent ");
    if (event.getState() == null) {
      return;
    }
    BlockState blockstate = event.getState();
    BlockPos pos = event.getPos();
    IWorld world = event.getWorld();
    PlayerEntity harvester = event.getPlayer();
    ResourceLocation blockId = blockstate.getBlock().getRegistryName();
    boolean matched = UtilString.isInList(config.getBlockIdsToTrigger(), blockstate.getBlock().getRegistryName());
    if (!matched) {
      ModAngerManagement.log(blockId + " broken not in ocnfig list   " + config.getBlockIdsToTrigger());
      return;
    }
    double diceRoll = rand.nextDouble() * 100;
    // event.getWorld().provider.getDimension() == NETHER &&
    ModAngerManagement.log(blockId + " did match and diceroll is  " + diceRoll + " < " + config.getPercent());
    // TODO: dimension config
    if (diceRoll < config.getPercent()) {
      // then look for one
      AxisAlignedBB region = AngerUtils.makeBoundingBox(pos.getX(), pos.getY(), pos.getZ(),
          config.getRangeAngerHorizontal(), config.getRangeAngerVertical());
      List<ZombiePigmanEntity> found = world.getEntitiesWithinAABB(ZombiePigmanEntity.class, region);
      for (ZombiePigmanEntity pz : found) {
        if (AngerUtils.isAngry(pz) == false) {
          AngerUtils.makeAngry(harvester, pz);
          break;// one will alert others, its enough
        }
      }
    }
  }

  @SubscribeEvent
  public void onPlayerHurt(LivingHurtEvent event) {
    if (config.isCalmingOnDeathEnabled() &&
        event.getEntityLiving().getHealth() - event.getAmount() <= 0 &&
        event.getEntityLiving() instanceof PlayerEntity) {
      PlayerEntity player = (PlayerEntity) event.getEntityLiving();
      BlockPos pos = player.getPosition();
      World world = player.world;
      // go make unhostile
      AxisAlignedBB region = AngerUtils.makeBoundingBox(pos.getX(), pos.getY(), pos.getZ(),
          config.getRangeCalmingHorizontal(), config.getRangeCalmingVertical());
      List<ZombiePigmanEntity> found = world.getEntitiesWithinAABB(ZombiePigmanEntity.class, region);
      int triggered = 0;
      for (ZombiePigmanEntity pz : found) {
        //  if (AngerUtils.isAngry(pz)) {
        triggered++;
        AngerUtils.makeCalm(player, pz);
        //  }
      }
      ModAngerManagement.log("is angry?  calm triggered  " + triggered);
      if (triggered > 0) {
        // particles?
      }
    }
  }
}
