package de.sanandrew.mods.enderstuffp.util;

import cpw.mods.fml.common.registry.GameRegistry;
import de.sanandrew.core.manpack.util.helpers.SAPUtils;
import de.sanandrew.mods.enderstuffp.block.*;
import de.sanandrew.mods.enderstuffp.item.ItemEndLeaves;
import de.sanandrew.mods.enderstuffp.item.block.ItemBlockBiomeDataCrystal;
import de.sanandrew.mods.enderstuffp.item.block.ItemBlockEnderOre;
import de.sanandrew.mods.enderstuffp.item.block.ItemBlockEnderStorage;
import de.sanandrew.mods.enderstuffp.tileentity.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public final class EspBlocks
{
    // misc
    public static Block avisEgg;
//    public static Block corruptES;
    public static Block enderLeaves;
    public static Block enderLog;
    public static Block enderPlanks;
    public static Fluid endFluid;
    public static Block endFluidBlock;
    public static BlockEnderDoor blockEndDoor;
    public static BlockSaplingEndTree sapEndTree;
    public static Block biomeDataCrystal;

    // machines
    public static Block biomeChanger;
//    public static Block duplicator;
    public static Block weatherAltar;
    public static Block oreGenerator;
    public static Block fertilizer;

    // ores
    public static Block oreEnder;
    public static Block blockOreEnder;
    public static Block oreCrocoite;
    public static Block blockCrocoite;


    public static void initialize() {
        initRegisterFluids();
        initBlocks();
        registerTileEntities();
        registerBlocks();

        Blocks.fire.setFireInfo(enderLeaves, 30, 60);
        Blocks.fire.setFireInfo(enderLog, 5, 5);
    }

    private static void initRegisterFluids() {
        endFluid = new Fluid(EnderStuffPlus.MOD_ID + ":endfluid");
        endFluid.setDensity(3000).setTemperature(150).setLuminosity(8).setViscosity(500).setRarity(EnumRarity.uncommon).setGaseous(false);

        FluidRegistry.registerFluid(endFluid);
    }

    private static void initBlocks() {
        avisEgg = new BlockAvisEgg();
        oreEnder = new BlockEnderOre();
        blockOreEnder = new BlockEnderStorage();
        biomeChanger = new BlockBiomeChanger();
//        duplicator    = new BlockDuplicator();
        weatherAltar = new BlockWeatherAltar();
        blockEndDoor = new BlockEnderDoor(Material.iron);
        enderLeaves   = new BlockEndLeaves();
        enderLog      = new BlockEndLog();
        sapEndTree    = new BlockSaplingEndTree();
        enderPlanks   = new BlockEnderWood();
//        corruptES     = new BlockCorruptEndStone();
        endFluidBlock = new BlockEndFluid(endFluid, Material.water);
        oreGenerator = new BlockOreGenerator();
        biomeDataCrystal = new BlockBiomeDataCrystal();
        fertilizer = new BlockFertilizer();
        blockCrocoite = new BlockCrocoite();
        oreCrocoite = new BlockOreCrocoite();

//        duplicator.setBlockName(ESPModRegistry.MOD_ID + ":duplicator")
//                  .setCreativeTab(ESPModRegistry.espTab)
//                  .setHardness(1.0F);
//        corruptES.setBlockName(ESPModRegistry.MOD_ID + ":corruptES")
//                 .setCreativeTab(ESPModRegistry.espTab)
//                 .setHardness(3.0F)
//                 .setResistance(15.0F)
//                 .setStepSound(Block.soundTypePiston);

        endFluid.setBlock(endFluidBlock);
    }

    private static void registerTileEntities() {
        GameRegistry.registerTileEntity(TileEntityAvisEgg.class, EnderStuffPlus.MOD_ID + ":avisEggTile");
        GameRegistry.registerTileEntity(TileEntityBiomeChanger.class, EnderStuffPlus.MOD_ID + ":biomeChangerTile");
//        GameRegistry.registerTileEntity(TileEntityDuplicator.class, "duplicatorTE");
        GameRegistry.registerTileEntity(TileEntityWeatherAltar.class, EnderStuffPlus.MOD_ID + ":weatherAltarTile");
        GameRegistry.registerTileEntity(TileEntityOreGenerator.class, EnderStuffPlus.MOD_ID + ":oreGeneratorTile");
        GameRegistry.registerTileEntity(TileEntityBiomeDataCrystal.class, EnderStuffPlus.MOD_ID + ":biomeDataCrystalTile");
        GameRegistry.registerTileEntity(TileEntityFertilizer.class, EnderStuffPlus.MOD_ID + ":fertilizer");
        GameRegistry.registerTileEntity(TileEntityCrocoite.class, EnderStuffPlus.MOD_ID + ":crocoite");
        GameRegistry.registerTileEntity(TileEntityOreCrocoite.class, EnderStuffPlus.MOD_ID + ":crocoiteOre");
    }

    private static void registerBlocks() {
        SAPUtils.registerBlocks(avisEgg, biomeChanger, weatherAltar, blockEndDoor, oreGenerator, fertilizer, enderLog, sapEndTree, enderPlanks,
                                endFluidBlock, blockCrocoite, oreCrocoite);

        GameRegistry.registerBlock(enderLeaves, ItemEndLeaves.class, "endLeaves");
        GameRegistry.registerBlock(biomeDataCrystal, ItemBlockBiomeDataCrystal.class, "biomeDataCrystal");
        GameRegistry.registerBlock(oreEnder, ItemBlockEnderOre.class, "enderOre");
        GameRegistry.registerBlock(blockOreEnder, ItemBlockEnderStorage.class, "enderStorage");

        Blocks.dragon_egg.setCreativeTab(EspCreativeTabs.ESP_TAB);
    }
}
