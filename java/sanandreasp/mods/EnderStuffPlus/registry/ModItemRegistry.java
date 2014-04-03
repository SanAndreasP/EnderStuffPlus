package sanandreasp.mods.EnderStuffPlus.registry;

import sanandreasp.core.manpack.helpers.CommonUsedStuff;
import sanandreasp.mods.EnderStuffPlus.item.ItemAvisCompass;
import sanandreasp.mods.EnderStuffPlus.item.ItemCustomEnderPearl;
import sanandreasp.mods.EnderStuffPlus.item.ItemEndHorseArmor;
import sanandreasp.mods.EnderStuffPlus.item.ItemEnderFlesh;
import sanandreasp.mods.EnderStuffPlus.item.ItemEnderPetEgg;
import sanandreasp.mods.EnderStuffPlus.item.ItemNiobArmor;
import sanandreasp.mods.EnderStuffPlus.item.ItemNiobDoor;
import sanandreasp.mods.EnderStuffPlus.item.ItemRaincoat;
import sanandreasp.mods.EnderStuffPlus.item.tool.ItemNiobAxe;
import sanandreasp.mods.EnderStuffPlus.item.tool.ItemNiobBow;
import sanandreasp.mods.EnderStuffPlus.item.tool.ItemNiobHoe;
import sanandreasp.mods.EnderStuffPlus.item.tool.ItemNiobPickaxe;
import sanandreasp.mods.EnderStuffPlus.item.tool.ItemNiobShears;
import sanandreasp.mods.EnderStuffPlus.item.tool.ItemNiobShovel;
import sanandreasp.mods.EnderStuffPlus.item.tool.ItemNiobSword;
import sanandreasp.mods.EnderStuffPlus.item.tool.ItemTantalPickaxe;

import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;

import net.minecraftforge.common.EnumHelper;

import cpw.mods.fml.common.ObfuscationReflectionHelper;

public class ModItemRegistry
{
	public static EnumToolMaterial TOOL_NIOBIUM;

	public static EnumArmorMaterial ARMOR_NIOBIUM;

	public static Item espPearls;
	public static Item enderFlesh;
	public static Item avisFeather;
	public static Item avisArrow;
	public static Item avisCompass;
	public static Item enderPetStaff;
	public static Item endIngot;
	public static Item niobBow;
	public static Item rainCoat;
	public static Item niobHelmet;
	public static Item niobPlate;
	public static Item niobLegs;
	public static Item niobBoots;
	public static Item niobPick;
	public static Item niobShovel;
	public static Item niobAxe;
	public static Item niobHoe;
	public static Item niobSword;
	public static Item niobShears;
	public static Item endNugget;
	public static Item itemNiobDoor;
	public static Item enderStick;
	public static ItemEnderPetEgg enderPetEgg;
	public static Item endHorseArmor;
    public static Item tantalPick;

	public static final void init() {
		initMaterials();
		initItems();
		registerItems();
	}

	private static final void initMaterials() {
		TOOL_NIOBIUM = EnumHelper.addToolMaterial(
				"NIOBIUM",
				EnumToolMaterial.IRON.getHarvestLevel(),
				EnumToolMaterial.IRON.getMaxUses(),
				EnumToolMaterial.IRON.getEfficiencyOnProperMaterial(),
				EnumToolMaterial.IRON.getDamageVsEntity(),
				EnumToolMaterial.GOLD.getEnchantability()
		);

		ARMOR_NIOBIUM = EnumHelper.addArmorMaterial(
				"NIOBIUM",
				(Integer)ObfuscationReflectionHelper.getPrivateValue(
						EnumArmorMaterial.class, EnumArmorMaterial.IRON, "maxDamageFactor", "field_78048_f"
				),
				new int[] {
						EnumArmorMaterial.IRON.getDamageReductionAmount(0),
						EnumArmorMaterial.IRON.getDamageReductionAmount(1),
						EnumArmorMaterial.IRON.getDamageReductionAmount(2),
						EnumArmorMaterial.IRON.getDamageReductionAmount(3)
				},
				EnumArmorMaterial.GOLD.getEnchantability()
		);
	}

	private static final void initItems() {
		espPearls		= new ItemCustomEnderPearl(ConfigRegistry.itemIDs.get("ESP Pearls").intValue() - 256).setUnlocalizedName("esp:espPearls").setCreativeTab(ESPModRegistry.espTab);
		enderFlesh		= new ItemEnderFlesh(ConfigRegistry.itemIDs.get("Ender Flesh").intValue() - 256).setUnlocalizedName("esp:enderFlesh").setTextureName("enderstuffp:enderFlesh").setCreativeTab(ESPModRegistry.espTab);
		avisFeather		= new Item(ConfigRegistry.itemIDs.get("Avis Feather").intValue() - 256).setUnlocalizedName("esp:avisFeather").setTextureName("enderstuffp:avisFeather").setCreativeTab(ESPModRegistry.espTab);
		avisArrow		= new Item(ConfigRegistry.itemIDs.get("Avis Arrow").intValue() - 256).setUnlocalizedName("esp:avisArrow").setTextureName("enderstuffp:avisArrow").setCreativeTab(ESPModRegistry.espTab);
		avisCompass		= new ItemAvisCompass(ConfigRegistry.itemIDs.get("Avis Compass").intValue() - 256)
								.setUnlocalizedName("esp:avisCompass")
								.setTextureName("enderstuffp:avisCompass")
								.setCreativeTab(ESPModRegistry.espTab);
		enderPetEgg		= (ItemEnderPetEgg) new ItemEnderPetEgg(ConfigRegistry.itemIDs.get("Enderpet Egg").intValue() - 256)
								.setUnlocalizedName("esp:enderPetEgg")
								.setCreativeTab(ESPModRegistry.espTab);
		enderPetStaff	= new Item(ConfigRegistry.itemIDs.get("Enderpet Staff").intValue() - 256)
								.setUnlocalizedName("esp:petStaff")
								.setTextureName("enderstuffp:petStaff")
								.setCreativeTab(ESPModRegistry.espTab)
								.setFull3D();
		endIngot		= new Item(ConfigRegistry.itemIDs.get("Niobium Ingot").intValue() - 256)
								.setUnlocalizedName("esp:niobIngot")
								.setTextureName("enderstuffp:niobIngot")
								.setCreativeTab(ESPModRegistry.espTab);
		niobBow			= new ItemNiobBow(ConfigRegistry.itemIDs.get("Niobium Bow").intValue() - 256)
								.setUnlocalizedName("esp:bowNiob")
								.setTextureName("enderstuffp:bowNiob")
								.setCreativeTab(ESPModRegistry.espTab);
		rainCoat		= new ItemRaincoat(ConfigRegistry.itemIDs.get("Ender-Raincoat").intValue() - 256)
								.setUnlocalizedName("esp:rainCoat")
								.setTextureName("enderstuffp:rainCoat")
								.setCreativeTab(ESPModRegistry.espTabCoats);
		niobHelmet 		= new ItemNiobArmor(ConfigRegistry.itemIDs.get("Niobium Helmet").intValue() - 256, ARMOR_NIOBIUM, 0)
								.setUnlocalizedName("esp:niobHelmet")
								.setTextureName("enderstuffp:niobHelmet")
								.setCreativeTab(ESPModRegistry.espTab);
		niobPlate 		= new ItemNiobArmor(ConfigRegistry.itemIDs.get("Niobium Chestplate").intValue() - 256, ARMOR_NIOBIUM, 1)
								.setUnlocalizedName("esp:niobChestplate")
								.setTextureName("enderstuffp:niobChestplate")
								.setCreativeTab(ESPModRegistry.espTab);
		niobLegs 		= new ItemNiobArmor(ConfigRegistry.itemIDs.get("Niobium Leggings").intValue() - 256, ARMOR_NIOBIUM, 2)
								.setUnlocalizedName("esp:niobLeggings")
								.setTextureName("enderstuffp:niobLeggings")
								.setCreativeTab(ESPModRegistry.espTab);
		niobBoots 		= new ItemNiobArmor(ConfigRegistry.itemIDs.get("Niobium Boots").intValue() - 256, ARMOR_NIOBIUM, 3)
								.setUnlocalizedName("esp:niobBoots")
								.setTextureName("enderstuffp:niobBoots")
								.setCreativeTab(ESPModRegistry.espTab);
		niobPick		= new ItemNiobPickaxe(ConfigRegistry.itemIDs.get("Niobium Pickaxe").intValue() - 256, TOOL_NIOBIUM)
								.setUnlocalizedName("esp:niobPick")
								.setTextureName("enderstuffp:niobPick")
								.setCreativeTab(ESPModRegistry.espTab);
		niobShovel 		= new ItemNiobShovel(ConfigRegistry.itemIDs.get("Niobium Shovel").intValue() - 256, TOOL_NIOBIUM)
								.setUnlocalizedName("esp:niobShovel")
								.setTextureName("enderstuffp:niobShovel")
								.setCreativeTab(ESPModRegistry.espTab);
		niobAxe 		= new ItemNiobAxe(ConfigRegistry.itemIDs.get("Niobium Axe").intValue() - 256, TOOL_NIOBIUM)
								.setUnlocalizedName("esp:niobAxe")
								.setTextureName("enderstuffp:niobAxe")
								.setCreativeTab(ESPModRegistry.espTab);
		niobHoe 		= new ItemNiobHoe(ConfigRegistry.itemIDs.get("Niobium Hoe").intValue() - 256, TOOL_NIOBIUM)
								.setUnlocalizedName("esp:niobHoe")
								.setTextureName("enderstuffp:niobHoe")
								.setCreativeTab(ESPModRegistry.espTab);
		niobSword 		= new ItemNiobSword(ConfigRegistry.itemIDs.get("Niobium Sword").intValue() - 256, TOOL_NIOBIUM)
								.setUnlocalizedName("esp:niobSword")
								.setTextureName("enderstuffp:niobSword")
								.setCreativeTab(ESPModRegistry.espTab);
		niobShears 		= new ItemNiobShears(ConfigRegistry.itemIDs.get("Niobium Shears").intValue() - 256)
								.setUnlocalizedName("esp:niobShears")
								.setTextureName("enderstuffp:niobShears")
								.setCreativeTab(ESPModRegistry.espTab);
		endNugget 		= new Item(ConfigRegistry.itemIDs.get("Niobium Nugget").intValue() - 256)
								.setUnlocalizedName("esp:niobNugget")
								.setTextureName("enderstuffp:niobNugget")
								.setCreativeTab(ESPModRegistry.espTab);
		itemNiobDoor 	= new ItemNiobDoor(ConfigRegistry.itemIDs.get("Ender Door").intValue() - 256)
								.setUnlocalizedName("esp:doorNiob")
								.setTextureName("enderstuffp:doorNiob")
								.setCreativeTab(ESPModRegistry.espTab);
		enderStick 		= new Item(ConfigRegistry.itemIDs.get("Ender Stick").intValue() - 256)
								.setUnlocalizedName("esp:enderStick")
								.setTextureName("enderstuffp:enderStick")
								.setCreativeTab(ESPModRegistry.espTab);
        endHorseArmor   = new ItemEndHorseArmor(10240)
                                .setUnlocalizedName("esp:enderHorseArmor")
                                .setTextureName("enderstuffp:enderStick")
                                .setCreativeTab(ESPModRegistry.espTab);
        tantalPick   = new ItemTantalPickaxe(10241, TOOL_NIOBIUM)
                                .setUnlocalizedName("esp:tantalPick")
                                .setTextureName("enderstuffp:tantalPick")
                                .setCreativeTab(ESPModRegistry.espTab);
	}

	private static final void registerItems() {
		CommonUsedStuff.registerItems("enderstuffp:item",
				espPearls,		avisFeather,	avisArrow,		avisCompass,
				enderPetEgg,	enderPetStaff,	endIngot,		niobBow,
				niobHelmet,		niobPlate,		niobLegs,		niobBoots,
				niobPick,		niobShovel,		niobAxe,		niobHoe,
				niobSword,		niobShears,		enderFlesh,		rainCoat,
				itemNiobDoor,	endNugget,		enderStick,     endHorseArmor
		);
	}
}
