package com.lothrazar.hostileores;

import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;

public class AngerUtils {

  public static String lang(String string) {
    //if we use the clientside one, it literally does not work & crashes on serverside run
    return I18n.translateToLocal(string);
  }

  public static TextComponentTranslation langTr(String string) {
    //if we use the clientside one, it literally does not work & crashes on serverside run
    return new TextComponentTranslation(lang(string));
  }

  public static void sendMessage(EntityPlayer player, String string) {
    player.sendMessage(AngerUtils.langTr(string));
  }

  public static void sendStatus(EntityPlayer event, String string) {
    event.sendStatusMessage(AngerUtils.langTr(string), true);
  }

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
  }

  public static void makeAngry(EntityPlayer event, EntityPigZombie pz) {
    //could use .becomeAngryAt() but it is private   
    pz.attackEntityFrom(DamageSource.causePlayerDamage(event), 0);
  }
}
