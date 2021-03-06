/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.mods.enderstuffp.world;

import de.sanandrew.core.manpack.util.javatuples.Triplet;
import de.sanandrew.mods.enderstuffp.util.EnderStuffPlus;
import de.sanandrew.mods.enderstuffp.util.EnumEnderOres;
import de.sanandrew.mods.enderstuffp.util.EspBlocks;
import de.sanandrew.mods.enderstuffp.util.manager.IslandManager;
import de.sanandrew.mods.enderstuffp.util.manager.IslandManager.EnumBlockType;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.feature.WorldGenerator;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorldGenEndIsland
    extends WorldGenerator
{
    @Override
    public boolean generate(World world, Random random, int x, int y, int z) {
        EnumBlockType[][][] types = IslandManager.getRandomIslandShape(random);
        if( types == null ) {
            EnderStuffPlus.MOD_LOG.log(Level.WARN, "Couldn't generate island!");
            return false;
        }

        int xWidth = types.length;
        int zWidth = types[0][0].length;

        int maxTantal = 5;

        List<Triplet<Integer, Integer, EnumBlockType>> features = new ArrayList<>();

        for( int i = 0; i < xWidth; i++ ) {
            for( int j = 0; j < 40; j++ ) {
                for( int k = 0; k < zWidth; k++ ) {
                    EnumBlockType type = types[i][j][k];
                    if( type == EnumBlockType.STONE || (j != 0 && type == EnumBlockType.FEATURE) ) {
                        if( maxTantal > 0 && random.nextInt(2048) == 0 ) {
                            world.setBlock(x + i, y - j, z + k, EspBlocks.oreEnder, EnumEnderOres.TANTALUM.ordinal(), 2);
                            maxTantal--;
                        } else {
                            world.setBlock(x + i, y - j, z + k, Blocks.end_stone, 0, 2);
                        }
                    } else if( type == EnumBlockType.FEATURE ) {
                        world.setBlock(x + i, y - j, z + k, Blocks.end_stone, 0, 2);
                        features.add(Triplet.with(x + i, z + k, type));
                    }
                }
            }
        }

        for( Triplet<Integer, Integer, EnumBlockType> featureLoc : features ) {
            if( random.nextInt(2) != 0 ) {
                new WorldGenIslandForest().generate(world, random, featureLoc.getValue0(), y + 1, featureLoc.getValue1());
            } else {
                new WorldGenLakes(EspBlocks.endFluidBlock).generate(world, random, featureLoc.getValue0(), y + 1, featureLoc.getValue1());
            }
        }

        return true;
    }

    private static class WorldGenIslandForest
            extends WorldGenerator
    {
        @Override
        public boolean generate(World world, Random random, int x, int y, int z) {
            WorldGenEndTree genTrees = new WorldGenEndTree(false);
            for( int i = 0; i < 15; i++ ) {
                genTrees.generate(world, random, x + random.nextInt(10) - 5, y, z + random.nextInt(10) - 5);
            }

            return false;
        }
    }
}
