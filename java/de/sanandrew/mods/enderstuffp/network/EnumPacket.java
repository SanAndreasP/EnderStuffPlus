/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP, SilverChiren and CliffracerX
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.mods.enderstuffp.network;

import de.sanandrew.mods.enderstuffp.network.packet.PacketEnderPetGuiAction;
import de.sanandrew.mods.enderstuffp.network.packet.PacketParticleFX;
import de.sanandrew.mods.enderstuffp.network.packet.PacketRemoteOpenGui;

public enum EnumPacket
{
    PKG_PARTICLES(PacketParticleFX.class),
    PKG_OPEN_CLIENT_GUI(PacketRemoteOpenGui.class),
    ENDERPET_ACTION(PacketEnderPetGuiAction.class);

    public static final EnumPacket[] VALUES = values();

    public final Class<? extends IPacket> packetCls;

    private EnumPacket(Class<? extends IPacket> packetClass) {
        this.packetCls = packetClass;
    }
}