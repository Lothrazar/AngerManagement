package com.lothrazar.hostileores;
import java.util.List;

import net.minecraft.util.NonNullList;

public class ConfigManager {

  private List<String> blockIdsToTrigger;
  private int percent;
  private int rangeCalmingHorizontal = 3;
  private int rangeCalmingVertical = 3;
  private int rangeAngerHorizontal = 16;
  private int rangeAngerVertical = 3;
  private boolean calmingOnDeathEnabled = true;
  private boolean logEverything = false;
  private boolean pacifyIronGolems = true;
  //  private int[] dimensionList;
  //future maybes?
  //  private int maxNumberSearchedPerOre = 1;
  //  private int maxNumberTriggeredPerOre = 1;
  //  private List<String> potionEffectWhenAngered;
  //  private List<Integer> dimensionsTrigger;

  public List<String> getBlockIdsToTrigger() {
    return blockIdsToTrigger;
  }


  public int getPercent() {
    return percent;
  }

   public int getRangeCalmingHorizontal() {
    return rangeCalmingHorizontal;
  }

  public int getRangeCalmingVertical() {
    return rangeCalmingVertical;
  }

   public int getRangeAngerHorizontal() {
    return rangeAngerHorizontal;
  }


  public int getRangeAngerVertical() {
    return rangeAngerVertical;
  }

  public boolean isCalmingOnDeathEnabled() {
    return calmingOnDeathEnabled;
  }
   //  public void initConfig(Configuration config) {
  //    String category = ModAngerManagement.MODID;
  //    logEverything = config.getBoolean("LogAllEvents", category, false, "Log when a targeted block is mined, and when mob is angered, and probably more.  Very spammy.  Use for debugging and testing your configs.  ");
  //    category = ModAngerManagement.MODID + ".anger";
  //    final String[] defaults = new String[] {
  //        "minecraft:quartz_ore",
  //        "minecraft:chest",
  //        "tconstruct:ore",
  //        "cyclicmagic:*_ore",
  //        "mysticalagriculture:nether_inferium_ore"
  //    };
  //    final String[] conf = config.getStringList("blocksMined", category, defaults, "List of blocks that will cause anger when mined.  ");
  //    this.blockIdsToTrigger = NonNullList.from("", conf);
  //    this.percent = config.getInt("percentChanceAnger", category, 25, 0, 100, "What percent (%) chance that mining will aggro something nearby (0 to disable) ");
  //    this.rangeAngerHorizontal = config.getInt("rangeAngerHorizontal", category, 16, 0, 128,
  //        "Horizontal range to look and find things to anger");
  //    this.rangeAngerVertical = config.getInt("rangeAngerVertical", category, 3, 0, 128,
  //        "Vertical range to look and find things to anger");
  //    //now calm section
  //    category = ModAngerManagement.MODID + ".calm";
  //    this.calmingOnDeathEnabled = config.getBoolean("calmingOnDeathEnabled", category, true, "Pigmen will become calm when a nearby player dies");
  //    this.rangeCalmingHorizontal = config.getInt("rangeCalmingHorizontal", category, 16, 0, 128,
  //        "Horizontal range to look and find things to calm when player dies");
  //    this.rangeCalmingVertical = config.getInt("rangeCalmingVertical", category, 3, 0, 128,
  //        "Vertical range to look and find things to calm when player dies");
  //    category = ModAngerManagement.MODID + ".village_golem";
  //    this.pacifyIronGolems = config.getBoolean("PacifyIronGolems", category, true, "If true, aggro from Iron Golems to players is cancelled and damage is nullified");
  //    config.save();
  //    //tolist
  //  }

  public boolean isLogEverything() {
    return logEverything;
  }

  public boolean isPacifyIronGolems() {
    return pacifyIronGolems;
  }

}
