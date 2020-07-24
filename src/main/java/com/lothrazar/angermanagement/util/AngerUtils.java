package com.lothrazar.angermanagement.util;

import com.lothrazar.angermanagement.ModAngerManagement;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;

public class AngerUtils {

  public static AxisAlignedBB makeBoundingBox(double x, double y, double z, int hRadius, int vRadius) {
    return new AxisAlignedBB(x - hRadius, y - vRadius, z - hRadius, x + hRadius, y + vRadius, z + hRadius);
  }

  public static void makeCalm(PlayerEntity player, PiglinEntity pz) {
    // need to set anger timer
    pz.setAttackTarget(null);
    pz.setRevengeTarget(null);
    pz.setLastAttackedEntity(null);
    CompoundNBT sandbox = new CompoundNBT();
    pz.writeUnlessRemoved(sandbox);
    sandbox.putBoolean("CannotHunt", true);
    //    sandbox.putString("HurtBy", "");
    pz.read(sandbox);
    ModAngerManagement.log(pz.world.isRemote + "=isRemote ; Triggered calming at  " + pz.func_233580_cy_());
    //    pz.addPotionEffect(new EffectInstance(Effects.GLOWING, 60 * 20, 1));
  }

  public static void makeAngry(PlayerEntity event, PiglinEntity pz) {
    // could use .becomeAngryAt() but it is private
    pz.attackEntityFrom(DamageSource.causePlayerDamage(event), 0);
    ModAngerManagement.log("Triggered anger at  " + pz.func_233580_cy_());
  }

  public static void makeCalmGolem(IronGolemEntity golem) {
    golem.setAttackTarget(null);
    golem.setRevengeTarget(null);
    golem.setLastAttackedEntity(null);
    ModAngerManagement.log("Triggered calming on IronGolem at  " + golem.func_233580_cy_());
  }

  public static boolean isAngry(PiglinEntity pz) {
    ZombifiedPiglinEntity y;
    CompoundNBT sandbox = new CompoundNBT();
    pz.writeAdditional(sandbox);
    //    ModAngerManagement.log("sandbox.getShort(\"Anger\")   " + sandbox.getShort("Anger"));
    return !sandbox.getBoolean("CannotHunt");
  }
}
