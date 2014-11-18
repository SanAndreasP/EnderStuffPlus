package de.sanandrew.mods.enderstuffp.client.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.sanandrew.mods.enderstuffp.client.util.EnumTextures;
import de.sanandrew.mods.enderstuffp.entity.living.IEnderPet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.lwjgl.opengl.GL11;

//import de.sanandrew.mods.enderstuffplus.entity.living.EntityEnderAvis;

@SideOnly(Side.CLIENT)
public class RenderGameOverlayHandler
    extends Gui
{
    private Minecraft mc;

    public RenderGameOverlayHandler() {
        this.mc = Minecraft.getMinecraft();
    }

    @SubscribeEvent
    public void onRenderHUD(RenderGameOverlayEvent.Pre event) {
        if( event.type == RenderGameOverlayEvent.ElementType.HEALTHMOUNT ) {
            if( this.mc.thePlayer != null && this.mc.thePlayer.ridingEntity != null && this.mc.thePlayer.ridingEntity instanceof IEnderPet ) {
                ScaledResolution scaledRes = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
                int scaledWdt = scaledRes.getScaledWidth();
                int scaledHgt = scaledRes.getScaledHeight();
                IEnderPet pet = ((IEnderPet) this.mc.thePlayer.ridingEntity);

                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

                this.mc.getTextureManager().bindTexture(EnumTextures.GUI_INGAMEICONS.getResource());
                this.renderEnderStats(pet.getPetHealth(), pet.getPetMaxHealth(), scaledWdt / 2 + 91, scaledHgt - 39, 9);

//                if( this.mc.thePlayer.ridingEntity instanceof EntityEnderAvis ) {
//                    this.renderEnderStats(((EntityEnderAvis) pet).getCurrFlightCondInt(), 20, scaledWdt / 2 + 91, scaledHgt - 49, 0);
//                }
                event.setCanceled(true);
            }
        }
    }

    private void renderEnderStats(float currStat, float maxStat, int posX, int posY, int texYOff) {
        int currStatHalf = (int) (Math.floor(currStat / 2.0F) + 0.1F);
        int maxStatHalf = (int) (Math.floor(maxStat / 2.0F) + 0.1F);

        boolean addHalfCurrStat = currStat % 2 != 0;
        boolean addHalfMaxStat = maxStat % 2 != 0;

        for( int i = maxStatHalf - 1; i >= 0; i-- ) {
            int shiftedPosX = posX - i * 8 - 9 + 80 * (i / 10);
            this.drawTexturedModalRect(shiftedPosX, posY - 3 * (i / 10), 0, texYOff, 9, 9);
            if( i < currStatHalf ) {
                this.drawTexturedModalRect(shiftedPosX, posY - 3 * (i / 10), 9, texYOff, 9, 9);
            } else if( addHalfCurrStat && i == currStatHalf ) {
                this.drawTexturedModalRect(posX - (i) * 8 - 9 + 80 * ((i) / 10), posY - 3 * ((i) / 10), 18, texYOff, 9, 9);
            }
        }

        if( addHalfMaxStat ) {
            this.drawTexturedModalRect(posX - maxStatHalf * 8 - 9 + 80 * (maxStatHalf / 10), posY - 3 * (maxStatHalf / 10), 0, texYOff, 4, 9);
        }
    }
}