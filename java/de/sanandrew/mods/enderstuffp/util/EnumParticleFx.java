/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP, SilverChiren and CliffracerX
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.mods.enderstuffp.util;

public enum EnumParticleFx
{
    FX_NIOBTOOL,
    FX_PEARL_BAIT,
    FX_PEARL_NIVIS,
    FX_PEARL_IGNIS,
    FX_TAME,
    FX_REJECT;

    public static final EnumParticleFx[] VALUES = values();

    public final byte ordinalByte() {
        return (byte) ordinal();
    }
}