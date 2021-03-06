package de.sanandrew.mods.enderstuffplus.world;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenEndIsland
    extends WorldGenerator
{
    private void createSpike(World world, Random random, int x, int y, int z, int minDepth) {
        int size = random.nextInt(8) + 16 + minDepth;
        for( int i = -2; i <= 2; i++ ) {
            for( int k = -2; k <= 2; k++ ) {
                for( int j = -size; j <= 0; j++ ) {
                    if( Math.abs(i) + Math.abs(k) + ((float) Math.abs(j) / (float) size) * 2 > 2 ) {
                        continue;
                    }
                    this.func_150515_a(world, x + i, y + j, z + k, Blocks.end_stone);
                }
            }
        }
    }

    @Override
    public boolean generate(World world, Random random, int x, int y, int z) {

        int rad = 16 + random.nextInt(16);

        int y1 = 0, x1, z1;
//        for( int i = 0; i < rad * 2; i++ ) {
            x1 = random.nextInt(rad * 2) - rad;
            z1 = random.nextInt(rad * 2) - rad;
            y1 = random.nextInt(2);

            this.generateTable(world, random, x + x1, y + y1, z + z1, 8 + random.nextInt(8));
//        }

        if( random.nextInt(32) == 0 ) {
            (new WorldGenEndLeak(true)).generate(world, random, x, world.getHeightValue(x, z), z);
        } else {
            for( int i = 0; i < 4; i++ ) {
                (new WorldGenLakes(Blocks.glowstone)).generate(world, random, x + random.nextInt(8) - 4,
                                                                      world.getHeightValue(x, z), z + random.nextInt(8) - 4);
            }
            for( int i = 0; i < 16; i++ ) {
                x1 = x + random.nextInt(24) - 12;
                z1 = z + random.nextInt(24) - 12;
                y1 = world.getHeightValue(x1, z1) - 1;
                if( world.getBlock(x1, y1, z1) == Blocks.end_stone ) {
                    (new WorldGenEndTree(false)).generate(world, random, x1, y1 + 1, z1);
                }
            }
        }

        return true;
    }

    private void generateTable(World world, Random random, int x, int y, int z, int rad) {
        for( int i = -rad; i < rad; i++ ) {
            for( int k = -rad; k < rad; k++ ) {
                for( int j = -rad; j <= 0; j++ ) {
                    if( i * i + k * k > (rad - Math.abs(j)) * (rad - Math.abs(j)) ) {
                        continue;
                    } else if( j == 0 && random.nextInt(128) == 0 ) {
//                        this.createSpike(world, random, x + i, y, z + k,
//                                         rad - Math.round((float) Math.sqrt(i * i + k * k)));
                    }

                    this.func_150515_a(world, x + i, y + j, z + k, Blocks.end_stone);
                }
            }
        }
    }
}
