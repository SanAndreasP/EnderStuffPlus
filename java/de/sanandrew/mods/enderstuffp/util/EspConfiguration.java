package de.sanandrew.mods.enderstuffp.util;

import com.google.common.collect.Maps;
import net.minecraftforge.common.config.Property;
import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class EspConfiguration
{
//    public static Map<CfgNames, Integer> blockIDs = Maps.newEnumMap(CfgNames.class);
//    public static Map<CfgNames, Integer> itemIDs = Maps.newEnumMap(CfgNames.class);
    public static Map<String, Integer[]> spawnConditions = Maps.newHashMap();

    public static int enchID = 128;
    public static boolean genAvisNest = true;
    public static boolean genEndlessEnd = true;
    public static boolean genLeak = true;
    public static boolean genNiob = true;
    public static boolean griefing = true;
    public static boolean useAnimations = true;
    public static boolean useNiobHDGlow = true;

    private static final String CATEGORY_SPAWNINGS = "spawnings";
    private static final String CATEGORY_WORLDGEN = "worldgen";

    static {
        spawnConditions.put("EnderNivis", new Integer[] { 1, 1, 4 });
        spawnConditions.put("EnderIgnis", new Integer[] { 1, 1, 4 });
        spawnConditions.put("EnderRay", new Integer[] { 1, 1, 4 });
        spawnConditions.put("EnderAvis", new Integer[] { 1, 1, 4 });
        spawnConditions.put("EnderMiss", new Integer[] { 1, 1, 4 });
    }

    public static void setConfig(File modCfgDir) {
        net.minecraftforge.common.config.Configuration config = new net.minecraftforge.common.config.Configuration(new File(modCfgDir, "sanandreasp/" + EnderStuffPlus.MOD_ID + ".cfg"));

        config.load();

        config.addCustomCategoryComment(CATEGORY_SPAWNINGS, "The values in this category are arrays. They represent following pattern:"
                                                            + "\n  value #1 is the minimum spawn count per spawn loop"
                                                            + "\n  value #2 is the maximum spawn count per spawn loop"
                                                            + "\n  value #3 is the spawn rate weight"
                                                            + "\n  >>The Entity can spawn #1 to #2 times per loop with"
                                                            + " a weighted probability of #3<<");
        for( Entry<String, Integer[]> spawns : spawnConditions.entrySet() ) {
            Property prop = config.get(CATEGORY_SPAWNINGS, spawns.getKey(), ArrayUtils.toPrimitive(spawns.getValue()));
            spawnConditions.put(spawns.getKey(), ArrayUtils.toObject(prop.getIntList()));
        }

        config.addCustomCategoryComment(CATEGORY_WORLDGEN, "Several settings regarding world generation");
        genNiob = config.get(CATEGORY_WORLDGEN, "Generate Niobium Ore", true).getBoolean(true);
        genLeak = config.get(CATEGORY_WORLDGEN, "Generate Ender Leaks", true).getBoolean(true);
        genEndlessEnd = config.get(CATEGORY_WORLDGEN, "Generate End Islands", true).getBoolean(true);
        genAvisNest = config.get(CATEGORY_WORLDGEN, "Generate Avis Nests", true).getBoolean(true);

        enchID = config.get(net.minecraftforge.common.config.Configuration.CATEGORY_GENERAL, "EC-Teleport Enchantment-ID", enchID).getInt();
        griefing = config.get(net.minecraftforge.common.config.Configuration.CATEGORY_GENERAL, "Can Mod-Ender-Mobs grief", true).getBoolean(true);
        useNiobHDGlow = config.get(net.minecraftforge.common.config.Configuration.CATEGORY_GENERAL, "Use HD Tool glow effect", true).getBoolean(true);
        useAnimations = config.get(net.minecraftforge.common.config.Configuration.CATEGORY_GENERAL, "Use animated textures", true).getBoolean(true);

        config.save();
    }

    public static enum CfgNames {
        AVIS_EGG,               // blocks
        NIOBIUM_ORE,
        NIOBIUM_BLOCK,
        BIOME_CHANGER,
        DUPLICATOR,
        WEATHER_ALTAR,
        ENDER_DOOR_BLOCK,
        ENDER_LEAVES,
        ENDER_LOG,
        ENDER_PLANKS,
        ENDER_SAPLING,
        CORRUPT_END_STONE,
        END_FLUID,
        ESP_PEARLS,             // items
        ENDER_FLESH,
        AVIS_FEATHER,
        AVIS_ARROW,
        AVIS_COMPASS,
        ENDERPET_EGG,
        ENDERPET_STAFF,
        NIOBIUM_INGOT,
        NIOBIUM_BOW,
        ENDER_RAINCOAT,
        NIOBIUM_HELMET,
        NIOBIUM_CHESTPLATE,
        NIOBIUM_LEGGINGS,
        NIOBIUM_BOOTS,
        NIOBIUM_PICKAXE,
        NIOBIUM_SHOVEL,
        NIOBIUM_AXE,
        NIOBIUM_HOE,
        NIOBIUM_SWORD,
        NIOBIUM_SHEARS,
        NIOBIUM_NUGGET,
        ENDER_DOOR_ITEM,
        ENDER_STICK;

        /**
         * Returns a config-friendly name of the enum value.
         *
         * @return the name of the enum value without underscores
         *         and formatted in Pascal Case
         */
        @Override
        public String toString() {
            String name = "";
            Pattern pattern = Pattern.compile("(\\w+?)(_|$)");
            Matcher matcher = pattern.matcher(super.toString());

            if( !matcher.find() ) {
                return super.toString();
            }

            do {
                name += matcher.group(1).substring(0, 1).toUpperCase() + matcher.group(1).substring(1).toLowerCase();
            } while ( matcher.find() );

            return name;
        }
    }

}
