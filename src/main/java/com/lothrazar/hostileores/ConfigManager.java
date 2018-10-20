package com.lothrazar.hostileores;

import java.util.List;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.config.Configuration;

public class ConfigManager {

  public List<String> blockIdsToTrigger;
  public int percent;
  public int rangeCalmingHorizontal = 3;
  public int rangeCalmingVertical = 16;
  public int rangeAngerHorizontal = 3;
  public int rangeAngerVertical = 16;
  public boolean sendChat;
  public boolean calmingOnDeathEnabled;
  //future maybes?  
  //  private int maxNumberSearchedPerOre = 1;
  //  private int maxNumberTriggeredPerOre = 1; 
  //  private List<String> potionEffectWhenAngered;
  //  private List<Integer> dimensionsTrigger;

  public void initConfig(Configuration config) {
    String category = ModHostileMiners.MODID;
    sendChat = config.getBoolean("sendChatEnabled", category, true, "Toggle if chat messages appear or not");
    category = ModHostileMiners.MODID + ".anger";
    final String[] defaults = new String[] {
        "minecraft:quartz_ore",
        "minecraft:chest",
        "tconstruct:ore",
        "cyclicmagic:nether_gold_ore",
        "cyclicmagic:nether_diamond_ore",
        "cyclicmagic:nether_emerald_ore",
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
}
