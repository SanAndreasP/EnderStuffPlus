package de.sanandrew.mods.enderstuffp.client.render;

import de.sanandrew.core.manpack.util.client.helpers.ItemRenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class ItemRendererBow
        implements IItemRenderer
{
    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack stack, Object... data) {
        GL11.glPopMatrix();                                         // prevents Forge from pre-translating the item

        if( type == ItemRenderType.EQUIPPED_FIRST_PERSON ) {
            renderStack(stack);
        } else {
            GL11.glPushMatrix();

            float scale = 3.0F - (1.0F / 3.0F);                     // contra-translate the item from it's standard translation
                                                                    // also apply some more scale or else the bow is tiny
            GL11.glRotatef(-20.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(-60.0F, 0.0F, 0.0F, 1.0F);
            GL11.glScalef(scale, scale, scale);
            GL11.glTranslatef(-0.25F, -0.1875F, 0.1875F);

            scale = 0.625F;                                         // render the item as 'real' bow

            GL11.glTranslatef(0.0F, 0.125F, 0.3125F);
            GL11.glRotatef(-20.0F, 0.0F, 1.0F, 0.0F);
            GL11.glScalef(scale, -scale, scale);
            GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);

//            ItemRenderHelper.renderItem(item, 0, false);
            renderStack(stack);

            GL11.glPopMatrix();
        }

        GL11.glPushMatrix();
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return false;
    }

    private void renderStack(ItemStack stack) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        ItemRenderHelper.renderIcon(stack.getItem().getIcon(stack, 0, player, player.getItemInUse(), player.getItemInUseCount()), stack.getItemSpriteNumber(),
                                    stack.hasEffect(0), false);
    }
}
