/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP, SilverChiren and CliffracerX
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.mods.enderstuffp.network.packet;

import de.sanandrew.core.manpack.util.javatuples.Tuple;
import de.sanandrew.core.manpack.network.IPacket;
import de.sanandrew.mods.enderstuffp.util.EnderStuffPlus;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import net.minecraft.network.INetHandler;
import net.minecraft.tileentity.TileEntity;

import java.io.IOException;

public class PacketTileDataSync
        implements IPacket
{
    @Override
    public void process(ByteBufInputStream stream, ByteBuf rawData, INetHandler handler) throws IOException {
        EnderStuffPlus.proxy.syncTileData(stream.readInt(), stream.readInt(), stream.readInt(), stream);
    }

    @Override
    public void writeData(ByteBufOutputStream stream, Tuple dataTuple) throws IOException {
        TileEntity tile = (TileEntity) dataTuple.getValue(0);

        stream.writeInt(tile.xCoord);
        stream.writeInt(tile.yCoord);
        stream.writeInt(tile.zCoord);

        if( tile instanceof ITileSync ) {
            ((ITileSync) tile).writeToStream(stream);
        }
    }

    public static interface ITileSync {
        public void writeToStream(ByteBufOutputStream stream) throws IOException;
        public void readFromStream(ByteBufInputStream stream) throws IOException;
    }
}
