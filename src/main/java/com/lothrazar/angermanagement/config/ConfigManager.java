package com.lothrazar.angermanagement.config;

import java.nio.file.Path;
import java.util.List;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.google.common.collect.ImmutableList;
import com.lothrazar.angermanagement.ModAngerManagement;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class ConfigManager {

  private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
  private static ForgeConfigSpec COMMON_CONFIG;
  public static ForgeConfigSpec.BooleanValue calmingOnDeathEnabled;
  private static BooleanValue logSpam;
  static {
    initConfig();
  }

  private static void initConfig() {
    COMMON_BUILDER.comment("settings").push(ModAngerManagement.MODID);
    logSpam = COMMON_BUILDER.comment("If you are testing all different combinations of config values for packdev, and want to log spam lots of info turn this on ")
        .define("logSpamStuffToTest", false);
    pacifyIronGolems = COMMON_BUILDER.comment("If true, aggro from Iron Golems to players is cancelled and damage is nullified ").define("irongolem.neverAttackPlayer", true);
    // calm
    calmingOnDeathEnabled = COMMON_BUILDER.comment("Pigmen will become calm when a nearby player dies ").define("zombiepig.calming.onDeathEnabled", true);
    calmingHorizontal = COMMON_BUILDER.comment("Horizontal range to look and find things to calm when player dies").defineInRange("zombiepig.calming.rangeHorizontal", 6, 1, 16);
    calmingVertical = COMMON_BUILDER.comment("Vertical range to look and find things to calm when player dies").defineInRange("zombiepig.calming.rangeVertical", 3, 1, 16);
    //now the anger ones
    angerHorizontal = COMMON_BUILDER.comment("Horizontal range to look and find things to anger ").defineInRange("zombiepig.anger.rangeHorizontal", 16, 1, 16);
    angerVertical = COMMON_BUILDER.comment("Vertical range to look and find things to anger ").defineInRange("zombiepig.anger.rangeVertical", 3, 1, 16);
    angerPercent = COMMON_BUILDER.comment("What percent (%) chance that mining will aggro something nearby (0 zero to disable)").defineInRange("zombiepig.anger.percentChance", 25, 0, 100);
    blockIdsToTrigger = COMMON_BUILDER.comment("List of blocks that will cause anger when mined.  Uses 'net.minecraftforge.event.world.BlockEvent.BreakEvent'.")
        .defineList("zombiepig.anger.blocksMined",
            ImmutableList.of(
                "minecraft:nether_quartz_ore",
                "minecraft:nether_wart",
                "minecraft:chest"),
            obj -> obj instanceof String);
    COMMON_BUILDER.pop();
    COMMON_CONFIG = COMMON_BUILDER.build();
  }

  private static ConfigValue<List<? extends String>> blockIdsToTrigger;
  private static ForgeConfigSpec.IntValue angerPercent;
  private static IntValue calmingHorizontal;
  private static IntValue calmingVertical;
  private static IntValue angerHorizontal;
  private static IntValue angerVertical;
  private static BooleanValue pacifyIronGolems;

  public ConfigManager(Path path) {
    final CommentedFileConfig configData = CommentedFileConfig.builder(path)
        .sync()
        .autosave()
        .writingMode(WritingMode.REPLACE)
        .build();
    configData.load();
    COMMON_CONFIG.setConfig(configData);
  }

  public List<String> getBlockIdsToTrigger() {
    return (List<String>) blockIdsToTrigger.get();
  }

  public int getPercent() {
    return angerPercent.get();
  }

  public int getRangeCalmingHorizontal() {
    return calmingHorizontal.get();
  }

  public int getRangeCalmingVertical() {
    return calmingVertical.get();
  }

  public int getRangeAngerHorizontal() {
    return angerHorizontal.get();
  }

  public int getRangeAngerVertical() {
    return angerVertical.get();
  }

  public boolean isCalmingOnDeathEnabled() {
    return calmingOnDeathEnabled.get();
  }

  public boolean isLogEverything() {
    return logSpam.get();
  }

  public boolean isPacifyIronGolems() {
    return pacifyIronGolems.get();
  }
}
