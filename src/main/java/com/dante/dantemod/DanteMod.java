package com.dante.dantemod;
//import com.dante.dantemod.world.DungeonGenerator;
//import com.dante.dantemod.world.DungeonSavedData;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.world.level.saveddata.SavedData;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
//import net.minecraftforge.event.level.LevelEvent;


import org.slf4j.Logger;

import static net.minecraft.world.item.Items.registerItem;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(com.dante.dantemod.DanteMod.MODID)
public class DanteMod
{
    public static final String MODID = "dantemod";
    public static final String MODNAME = "Dante's Divine Comedy";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final Item MC_BOOK = ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft", "book"));


    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final ResourceKey<Level> DANTESCOMEDY = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(MODID, "dantescomedy"));
    // Creates a new Block combining the namespace and path
    public static final RegistryObject<Block> INFERNAL_STONE_BLOCK = BLOCKS.register("infernal_stone_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)));
    public static final RegistryObject<Item> INFERNAL_STONE_BLOCK_ITEM = ITEMS.register("infernal_stone_block", () -> new BlockItem(INFERNAL_STONE_BLOCK.get(), new Item.Properties()));

    public static final RegistryObject<Block> INFERNAL_BRICKS_BLOCK = BLOCKS.register("infernal_bricks_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.NETHER)));
    public static final RegistryObject<Item> INFERNAL_BRICKS_BLOCK_ITEM = ITEMS.register("infernal_bricks_block", () -> new BlockItem(INFERNAL_BRICKS_BLOCK.get(), new Item.Properties()));

    public static final RegistryObject<Block> DITE_BLOCK = BLOCKS.register("dite_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_BLUE)));
    public static final RegistryObject<Item> DITE_BLOCK_ITEM = ITEMS.register("dite_block", () -> new BlockItem(DITE_BLOCK.get(), new Item.Properties()));

    public static final RegistryObject<Item> LUCIFER_SWORD = ITEMS.register("lucifer_sword",
            () -> new SwordItem(
                    Tiers.NETHERITE, // Tipo di materiale (Tier)
                    new Item.Properties()
            )
    );

    public static final RegistryObject<CreativeModeTab> DANTE_TAB = CREATIVE_MODE_TABS.register("dante_tab", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> MC_BOOK.getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(DITE_BLOCK_ITEM.get());
                output.accept(INFERNAL_BRICKS_BLOCK_ITEM.get());
                output.accept(LUCIFER_SWORD.get());
                output.accept(INFERNAL_STONE_BLOCK_ITEM.get()); // Add the example item to the tab. For your own tabs, this method is preferred over the event
            }).build());

    public DanteMod()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.logDirtBlock)
            LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS)
            event.accept(INFERNAL_STONE_BLOCK_ITEM);
            event.accept(INFERNAL_BRICKS_BLOCK_ITEM);
            event.accept(DITE_BLOCK_ITEM);
            event.accept(LUCIFER_SWORD);
    }

    /* private boolean dungeonAlreadyGenerated(ServerLevel level) {
        DungeonSavedData data = level.getDataStorage().computeIfAbsent(
                DungeonSavedData::load,
                DungeonSavedData::new,
                DungeonSavedData.getName()
        );
        return data.isDungeonGenerated();
    }

    // Segna il dungeon come generato
    private void markDungeonAsGenerated(ServerLevel level) {
        DungeonSavedData data = level.getDataStorage().computeIfAbsent(
                DungeonSavedData::load,
                DungeonSavedData::new,
                DungeonSavedData.getName()
        );
        data.markDungeonAsGenerated();
    }*/
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    /*@SubscribeEvent
    public void onWorldLoad(LevelEvent.Load event) {
        if (event.getLevel() instanceof ServerLevel level && level.dimension() == DanteMod.DANTESCOMEDY) {
            BlockPos dungeonPosition = new BlockPos(0, 100, 0);
            if (!dungeonAlreadyGenerated(level)) {
                DungeonGenerator.generateDungeon(level, dungeonPosition);
                markDungeonAsGenerated(level);
            }
        }
    }*/


    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}
