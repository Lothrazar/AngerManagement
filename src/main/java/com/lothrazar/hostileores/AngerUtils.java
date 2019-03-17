package com.lothrazar.hostileores;

import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;

public class AngerUtils {

  public static AxisAlignedBB makeBoundingBox(double x, double y, double z, int hRadius, int vRadius) {
    return new AxisAlignedBB(x - hRadius, y - vRadius, z - hRadius, x + hRadius, y + vRadius, z + hRadius);
  }

  public static void makeCalm(EntityPlayer player, EntityPigZombie pz) {
    //need to set anger timer
    NBTTagCompound sandbox = new NBTTagCompound();
    pz.writeEntityToNBT(sandbox);
    sandbox.setShort("Anger", (short) 0);
    sandbox.setString("HurtBy", "");
    pz.readEntityFromNBT(sandbox);
    ModHostileMiners.log("Triggered calming at  " + pz.getPosition());
  }

  public static void makeAngry(EntityPlayer event, EntityPigZombie pz) {
    //could use .becomeAngryAt() but it is private   
    pz.attackEntityFrom(DamageSource.causePlayerDamage(event), 0);
    ModHostileMiners.log("Triggered anger at  " + pz.getPosition());
  }
}
