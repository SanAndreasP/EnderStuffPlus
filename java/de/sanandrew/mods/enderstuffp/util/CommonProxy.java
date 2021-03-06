package de.sanandrew.mods.enderstuffp.util;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import de.sanandrew.core.manpack.util.javatuples.Quartet;
import de.sanandrew.core.manpack.util.javatuples.Quintet;
import de.sanandrew.core.manpack.util.javatuples.Tuple;
import de.sanandrew.mods.enderstuffp.event.*;
import de.sanandrew.mods.enderstuffp.network.PacketManager;
import io.netty.buffer.ByteBufInputStream;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import java.io.IOException;

public class CommonProxy
        implements IGuiHandler
{
    public void preInit(FMLPreInitializationEvent event) { }

    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new EntityJoinWorldHandler());
        MinecraftForge.EVENT_BUS.register(new ArrowEventsHandler());
        MinecraftForge.EVENT_BUS.register(new EntityDropEventHandler());
        MinecraftForge.EVENT_BUS.register(new EntityHitEventHandler());
        MinecraftForge.EVENT_BUS.register(new BonemealEventHandler());
        MinecraftForge.EVENT_BUS.register(new ChunkPopulateEventHandler());

        EspEntities.registerEntities();

        //FIXME: add biome?
//        EnderStuffPlus.surfaceEnd = new BiomeGenSurfaceEnd(110);
//        //TODO: 100 is too damn high! JUST FOR TESTING PURPOSES!!!
//        BiomeManager.addBiome(BiomeType.ICY, new BiomeEntry(EnderStuffPlus.surfaceEnd, 100));
//        BiomeManager.addBiome(BiomeType.DESERT, new BiomeEntry(EnderStuffPlus.surfaceEnd, 100));
//        BiomeManager.addBiome(BiomeType.COOL, new BiomeEntry(EnderStuffPlus.surfaceEnd, 100));
//        BiomeManager.addBiome(BiomeType.WARM, new BiomeEntry(EnderStuffPlus.surfaceEnd, 100));
//
//        BiomeManager.addSpawnBiome(EnderStuffPlus.surfaceEnd);
    }

    public int addArmor(String armorId) {
        return 0;
    }

//    public void registerHandlers() {
        // TODO: reimplement event handlers
//        MinecraftForge.EVENT_BUS.register(new EnderEvents());
//        MinecraftForge.EVENT_BUS.register(new EntityInteractEventInst());
//        MinecraftForge.EVENT_BUS.register(new EnderStuffWorldGenerator());
//
//        GameRegistry.registerWorldGenerator(new EnderStuffWorldGenerator(), 10);
//    }

    public void handleParticle(EnumParticleFx particleType, double x, double y, double z, Tuple data) { }

    public void spawnParticle(EnumParticleFx particleType, double x, double y, double z, int dimensionId, Tuple data) {
        PacketManager.sendToAllAround(PacketManager.PARTICLES, dimensionId, x, y, z, 64.0D, Quintet.with(particleType.ordinalByte(), x, y, z, data));
    }

    public void syncTileData(int tileX, int tileY, int tileZ, ByteBufInputStream stream) throws IOException { }

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        return CommonGuiHandler.getGuiElement(id, player, world, x, y, z);
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    public void openGui(EntityPlayer player, EnumGui id, int x, int y, int z) {
        int guiId = id.ordinal();

        if( player instanceof EntityPlayerMP && getServerGuiElement(guiId, player, player.worldObj, x, y, z) == null ) {
            PacketManager.sendToPlayer(PacketManager.OPEN_CLIENT_GUI, (EntityPlayerMP) player, Quartet.with((byte) guiId, x, y, z));
        } else {
            FMLNetworkHandler.openGui(player, EnderStuffPlus.instance, guiId, player.worldObj, x, y, z);
        }
    }

    public World getWorld(INetHandler handler) {
        if( handler instanceof NetHandlerPlayServer ) {
            return ((NetHandlerPlayServer) handler).playerEntity.worldObj;
        }

        return null;
    }
}
