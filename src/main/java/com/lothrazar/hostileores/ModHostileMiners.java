package com.lothrazar.hostileores;

import java.util.List;
import org.apache.logging.log4j.Logger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = ModHostileMiners.MODID)
public class ModHostileMiners {

  public static final String MODID = "angermanagement";
  public static final int NETHER = -1;
  private static Logger logger;
  private List<String> blockIdsToTrigger;
  //  private List<String> potionEffectWhenAngered;
  //  private List<Integer> dimensionsTrigger;
  private int percent;
  private int rangeHorizontal = 3;
  private int rangeVertical = 16;
  //  private int maxNumberSearchedPerOre = 1;
  //  private int maxNumberTriggeredPerOre = 1;
  private boolean sendChat;
  private boolean calmingOnDeathEnabled;

  @EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    initConfig(new Configuration(event.getSuggestedConfigurationFile()));
    logger = event.getModLog();
    MinecraftForge.EVENT_BUS.register(this);
    MinecraftForge.EVENT_BUS.register(new EnrageHandler());
    MinecraftForge.EVENT_BUS.register(new CalmingHandler());
  }

  private void initConfig(Configuration config) {
    String[] defaults = new String[] {
        "minecraft:quartz_ore",
        "minecraft:chestc",
        "tconstruct:ore",
        "cyclicmagic:nether_gold_ore",
        "cyclicmagic:nether_diamond_ore",
        "cyclicmagic:nether_emerald_ore",
        "mysticalagriculture:nether_inferium_ore"
    };
    String[] conf = config.getStringList("BlocksMined", MODID, defaults, "List of blocks that will cause anger when mined.  ");
    //    this.blockIdsToTrigger 
    this.blockIdsToTrigger = NonNullList.from("", conf);
    this.percent = config.getInt("PercentChanceAnger", MODID, 50, 0, 100, "What percent (%) chance that mining will aggro something nearby (0 to disable) ");
    defaults = new String[] {
        "minecraft:slowness",
    };
    //    conf = config.getStringList("PotionOnAngered", MODID, defaults, "Potions given to angered pigmen, delete all potions to disable.  ");
    //    this.potionEffectWhenAngered = NonNullList.from("", conf);
    this.calmingOnDeathEnabled = config.getBoolean("calmingOnDeathEnabled", MODID, true, "Pigmen will become calm when a nearby player dies");
    sendChat = config.getBoolean("sendChatEnabled", MODID, true, "Toggle if chat messages appear or not");
    config.save();
    //tolist
  }

  public static AxisAlignedBB makeBoundingBox(double x, double y, double z, int hRadius, int vRadius) {
    return new AxisAlignedBB(x - hRadius, y - vRadius, z - hRadius, x + hRadius, y + vRadius, z + hRadius);
  }

  @SubscribeEvent
  public void onPlayerHurt(LivingHurtEvent event) {
    if (calmingOnDeathEnabled && event.getEntityLiving().getHealth() - event.getAmount() <= 0 &&
        event.getEntityLiving() instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) event.getEntityLiving();
      BlockPos pos = player.getPosition();
      World world = player.world;
      //  go make unhostile    
      AxisAlignedBB region = makeBoundingBox(pos.getX(), pos.getY(), pos.getZ(), rangeHorizontal, rangeVertical);
      List<EntityPigZombie> found = world.getEntitiesWithinAABB(EntityPigZombie.class, region);
      int triggered = 0;
      for (EntityPigZombie pz : found) {
        if (pz.isAngry()) {
          triggered++;
          this.makeCalm(player, pz);
        }
      }
      if (triggered > 0 && this.sendChat)
        player.sendMessage(new TextComponentTranslation("Locals have quelled their anger..."));
    }
  }

  @SubscribeEvent
  public void onHarvestDropsEvent(HarvestDropsEvent event) {
    if (event.getState() != null) {
      IBlockState blockstate = event.getState();
      BlockPos pos = event.getPos();
      World world = event.getWorld();
      String blockId = blockstate.getBlock().getRegistryName().toString();
      if (this.blockIdsToTrigger.contains(blockId) &&
      //          event.getWorld().provider.getDimension() == NETHER &&  
          world.rand.nextDouble() * 100 < this.percent) {
        // then look for one
        AxisAlignedBB region = makeBoundingBox(pos.getX(), pos.getY(), pos.getZ(), rangeHorizontal, rangeVertical);
        List<EntityPigZombie> found = world.getEntitiesWithinAABB(EntityPigZombie.class, region);
        for (EntityPigZombie pz : found) {
          if (pz.isAngry() == false) {
            //could use .becomeAngryAt() but it is private 
            makeAngry(event.getHarvester(), pz);
            break;// one will alert others, its enough 
          }
        }
      }
    }
  }

  private void makeCalm(EntityPlayer player, EntityPigZombie pz) {
    //need to set anger timer
    NBTTagCompound sandbox = new NBTTagCompound();
    pz.writeEntityToNBT(sandbox);
    sandbox.setShort("Anger", (short) 0);
    sandbox.setString("HurtBy", "");
    pz.readEntityFromNBT(sandbox);
    //    pz.attackEntityFrom(DamageSource.causePlayerDamage(event.getHarvester()), 0);
  }

  private void makeAngry(EntityPlayer event, EntityPigZombie pz) {
    pz.attackEntityFrom(DamageSource.causePlayerDamage(event), 0);
    if (this.sendChat)
      event.sendStatusMessage(new TextComponentTranslation("Locals are angry..."), true);
  }
}
