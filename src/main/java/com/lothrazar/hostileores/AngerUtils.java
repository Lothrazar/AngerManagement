package com.lothrazar.hostileores;

import net.minecraft.entity.monster.ZombiePigmanEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;

public class AngerUtils {

	public static AxisAlignedBB makeBoundingBox(double x, double y, double z, int hRadius, int vRadius) {
		return new AxisAlignedBB(x - hRadius, y - vRadius, z - hRadius, x + hRadius, y + vRadius, z + hRadius);
	}

	public static void makeCalm(PlayerEntity player, ZombiePigmanEntity pz) {
		// need to set anger timer
		CompoundNBT sandbox = new CompoundNBT();
		pz.writeUnlessRemoved(sandbox);
		sandbox.putShort("Anger", (short) 0);
		sandbox.putString("HurtBy", "");
		pz.read(sandbox);
		ModAngerManagement.log("Triggered calming at  " + pz.getPosition());

		pz.addPotionEffect(new EffectInstance(Effects.GLOWING, 10, 1));
	}

	public static void makeAngry(PlayerEntity event, ZombiePigmanEntity pz) {
		// could use .becomeAngryAt() but it is private
		pz.attackEntityFrom(DamageSource.causePlayerDamage(event), 0);
		ModAngerManagement.log("Triggered anger at  " + pz.getPosition());
	}

	public static void makeCalmGolem(IronGolemEntity golem) {
		golem.setAttackTarget(null);
		golem.setRevengeTarget(null);
		golem.setLastAttackedEntity(null);
		ModAngerManagement.log("Triggered calming on IronGolem at  " + golem.getPosition());
	}

	public static boolean isAngry(ZombiePigmanEntity pz) {
		CompoundNBT sandbox = new CompoundNBT();
		pz.writeAdditional(sandbox);
		return sandbox.getShort("Anger") > 0;
	}

}
