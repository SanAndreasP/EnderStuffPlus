/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP, SilverChiren and CliffracerX
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.mods.enderstuffp.client.util;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.sanandrew.mods.enderstuffp.client.particle.EntityColoredPortalFX;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntityHeartFX;
import net.minecraft.client.particle.EntitySmokeFX;

import java.util.Random;

@SideOnly(Side.CLIENT)
final class ParticleHelper
{
    static void spawnPortalFX(double x, double y, double z, Random rand, int count, float red, float green, float blue) {
        for( int i = 0; i < count; i++ ) {
            EntityFX part = new EntityColoredPortalFX(Minecraft.getMinecraft().theWorld,
                                                      x + (rand.nextDouble() - 0.5D), y + (rand.nextDouble() - 0.25D), z + (rand.nextDouble() - 0.5D),
                                                      (rand.nextDouble() - 0.5D) * 2.0D, -rand.nextDouble(), (rand.nextDouble() - 0.5D) * 2.0D,
                                                      red, green, blue
            );

            Minecraft.getMinecraft().effectRenderer.addEffect(part);
        }
    }

    static void spawnTameFX(double x, double y, double z, Random rand) {
        for( int i = 0; i < 15; i++ ) {
            EntityFX part = new EntityHeartFX(Minecraft.getMinecraft().theWorld,
                                              x + (rand.nextDouble() - 0.5D), y + (rand.nextDouble() * 2.9D), z + (rand.nextDouble() - 0.5D),
                                              0.0D, 0.0D, 0.0D
            );

            Minecraft.getMinecraft().effectRenderer.addEffect(part);
        }
    }

    static void spawnRefuseFX(double x, double y, double z, Random rand) {
        for( int i = 0; i < 15; i++ ) {
            EntityFX part = new EntitySmokeFX(Minecraft.getMinecraft().theWorld,
                                              x + (rand.nextDouble() - 0.5D), y + (rand.nextDouble() * 2.9D), z + (rand.nextDouble() - 0.5D),
                                              0.0D, 0.0D, 0.0D
            );

            Minecraft.getMinecraft().effectRenderer.addEffect(part);
        }
    }

    static void spawnEnderBodyFX(double x, double y, double z, Random rand, float red, float green, float blue, boolean isSitting) {
        for( int i = 0; i < 1; i++ ) {
            EntityFX part = new EntityColoredPortalFX(Minecraft.getMinecraft().theWorld, x + (rand.nextDouble() - 0.5D),
                                                      y + (rand.nextDouble() * (isSitting ? 0.9D : 2.9D)), z + (rand.nextDouble() - 0.5D),
                                                      (rand.nextDouble() - 0.5D) * 2.0D, -rand.nextDouble(), (rand.nextDouble() - 0.5D) * 2.0D,
                                                      red, green, blue
            );

            Minecraft.getMinecraft().effectRenderer.addEffect(part);
        }
    }

    static void spawnEnderTeleportFX(double x, double y, double z, Random rand, float red, float green, float blue, double prevPosX, double prevPosY, double prevPosZ) {
        for( int i = 0; i < 128; i++ ) {
            double posMulti = i / 127.0D;
            double partX = prevPosX + (x - prevPosX) * posMulti + (rand.nextDouble() - 0.5D) * 2.0D;
            double partY = prevPosY + (y - prevPosY) * posMulti + rand.nextDouble() * 2.9D;
            double partZ = prevPosZ + (z - prevPosZ) * posMulti + (rand.nextDouble() - 0.5D) * 2.0D;

            EntityFX part = new EntityColoredPortalFX(Minecraft.getMinecraft().theWorld, partX, partY, partZ,
                                                      (rand.nextDouble() - 0.5D) * 2.0D, -rand.nextDouble(), (rand.nextDouble() - 0.5D) * 2.0D,
                                                      red, green, blue
            );

            Minecraft.getMinecraft().effectRenderer.addEffect(part);
        }
    }
}
