package com.lothrazar.hostileores;

import java.util.List;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.config.Configuration;

public class ConfigManager {

  private List<String> blockIdsToTrigger;
  private int percent;
  private int rangeCalmingHorizontal = 3;
  private int rangeCalmingVertical = 16;
  private int rangeAngerHorizontal = 3;
  private int rangeAngerVertical = 16;
  private boolean calmingOnDeathEnabled;
  private boolean logEverything;
  //  private int[] dimensionList; 
  //future maybes?  
  //  private int maxNumberSearchedPerOre = 1;
  //  private int maxNumberTriggeredPerOre = 1; 
  //  private List<String> potionEffectWhenAngered;
  //  private List<Integer> dimensionsTrigger;

  public List<String> getBlockIdsToTrigger() {
    return blockIdsToTrigger;
  }

  public void setBlockIdsToTrigger(List<String> blockIdsToTrigger) {
    this.blockIdsToTrigger = blockIdsToTrigger;
  }

  public int getPercent() {
    return percent;
  }

  public void setPercent(int percent) {
    this.percent = percent;
  }

  public int getRangeCalmingHorizontal() {
    return rangeCalmingHorizontal;
  }

  public void setRangeCalmingHorizontal(int rangeCalmingHorizontal) {
    this.rangeCalmingHorizontal = rangeCalmingHorizontal;
  }

  public int getRangeCalmingVertical() {
    return rangeCalmingVertical;
  }

  public void setRangeCalmingVertical(int rangeCalmingVertical) {
    this.rangeCalmingVertical = rangeCalmingVertical;
  }

  public int getRangeAngerHorizontal() {
    return rangeAngerHorizontal;
  }

  public void setRangeAngerHorizontal(int rangeAngerHorizontal) {
    this.rangeAngerHorizontal = rangeAngerHorizontal;
  }

  public int getRangeAngerVertical() {
    return rangeAngerVertical;
  }

  public void setRangeAngerVertical(int rangeAngerVertical) {
    this.rangeAngerVertical = rangeAngerVertical;
  }

  public boolean isCalmingOnDeathEnabled() {
    return calmingOnDeathEnabled;
  }

  public void setCalmingOnDeathEnabled(boolean calmingOnDeathEnabled) {
    this.calmingOnDeathEnabled = calmingOnDeathEnabled;
  }

  public void initConfig(Configuration config) {
    String category = ModHostileMiners.MODID;
    logEverything = config.getBoolean("LogAllEvents", category, false, "Log when a targeted block is mined, and when mob is angered, and probably more.  Very spammy.  Use for debugging and testing your configs.  ");
    category = ModHostileMiners.MODID + ".anger";
    final String[] defaults = new String[] {
        "minecraft:quartz_ore",
        "minecraft:chest",
        "tconstruct:ore",
        "cyclicmagic:*_ore",
        "mysticalagriculture:nether_inferium_ore"
    };
    final String[] conf = config.getStringList("blocksMined", category, defaults, "List of blocks that will cause anger when mined.  ");
    this.blockIdsToTrigger = NonNullList.from("", conf);
    this.percent = config.getInt("percentChanceAnger", category, 25, 0, 100, "What percent (%) chance that mining will aggro something nearby (0 to disable) ");
    this.rangeAngerHorizontal = config.getInt("rangeAngerHorizontal", category, 16, 0, 128,
        "Horizontal range to look and find things to anger");
    this.rangeAngerVertical = config.getInt("rangeAngerVertical", category, 3, 0, 128,
        "Vertical range to look and find things to anger");
    //now calm section
    category = ModHostileMiners.MODID + ".calm";
    this.calmingOnDeathEnabled = config.getBoolean("calmingOnDeathEnabled", category, true, "Pigmen will become calm when a nearby player dies");
    this.rangeCalmingHorizontal = config.getInt("rangeCalmingHorizontal", category, 16, 0, 128,
        "Horizontal range to look and find things to calm when player dies");
    this.rangeCalmingVertical = config.getInt("rangeCalmingVertical", category, 3, 0, 128,
        "Vertical range to look and find things to calm when player dies");
    config.save();
    //tolist
  }

  public boolean isLogEverything() {
    return logEverything;
  }

  public void setLogEverything(boolean logEverything) {
    this.logEverything = logEverything;
  }
}
