package com.lothrazar.hostileores;

import java.util.List;

import net.minecraft.entity.monster.ZombiePigmanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CalmingHandler {

	private ConfigManager config;

	public CalmingHandler(ConfigManager config) {
		this.config = config;
	}

	@SubscribeEvent
	public void onPlayerHurt(LivingHurtEvent event) {
		if (config.isCalmingOnDeathEnabled() && // event.getEntityLiving().getHealth()
												// - event.getAmount() <= 0 &&
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
				if (AngerUtils.isAngry(pz)) {
					triggered++;
					AngerUtils.makeCalm(player, pz);
					// pz.dir
				} else {
					pz.removePotionEffect(Effects.GLOWING);
				}
			}
			if (triggered > 0) {
				ModAngerManagement.log("is angry?  calm triggered  " + triggered);
				// particles?
			}
		}
	}
}
