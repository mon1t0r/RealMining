package com.realmining.realminingmod;

import com.realmining.realminingmod.blocks.ModBlocks;
import com.realmining.realminingmod.events.ModEvents;
import com.realmining.realminingmod.items.ModItems;
import com.realmining.realminingmod.sounds.ModSounds;
import com.realmining.realminingmod.world.features.StonesGen;
import com.realmining.realminingmod.world.features.ores.*;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(RealMiningMod.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RealMiningMod
{
    private static RealMiningMod instance;
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "realmining";

    public RealMiningMod() {
        instance=this;
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModSounds.SOUNDS.register(bus);
        ModItems.ITEMS.register(bus);
        ModBlocks.BLOCKS.register(bus);


        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, ModEvents::biomeLoadingIntercept);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, StonesGen::generateOre);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, CoalOre::generateOre);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, IronOre::generateOre);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, GoldOre::generateOre);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, DiamondOre::generateOre);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, RedstoneOre::generateOre);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, LapisOre::generateOre);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, EmeraldOre::generateOre);


    }
    public static RealMiningMod getInstance() {
        return instance;
    }
    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().options);
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some realmining code to dispatch IMC to another mod
        InterModComms.sendTo("realminingmod", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // some realmining code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("HELLO from Register Block");
        }
    }
}
