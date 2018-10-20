package com.lothrazar.hostileores;

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
}
