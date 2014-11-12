package de.sanandrew.mods.enderstuffp.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.sanandrew.core.manpack.util.helpers.SAPUtils;
import de.sanandrew.core.manpack.util.javatuples.Pair;
import de.sanandrew.core.manpack.util.javatuples.Triplet;
import de.sanandrew.mods.enderstuffp.entity.living.IEnderPet;
import de.sanandrew.mods.enderstuffp.network.EnumPacket;
import de.sanandrew.mods.enderstuffp.network.PacketProcessor;
import de.sanandrew.mods.enderstuffp.util.EnderStuffPlus;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiEnderPet
    extends GuiScreen
{
    private IEnderPet enderPet;
    private GuiTextField nameTxt;
    private EntityPlayer owner;
    private int yPos;

    public GuiEnderPet(IEnderPet pet, EntityPlayer player) {
        this.enderPet = pet;
        this.owner = player;
        this.allowUserInput = true;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partTicks) {
        int color = this.enderPet.getGuiColor();
        String petName = this.enderPet.getName().isEmpty() ? EnumChatFormatting.OBFUSCATED + "RANDOM" + EnumChatFormatting.RESET : this.enderPet.getName();
        String title = SAPUtils.translatePostFormat(this.enderPet.getGuiTitle(), petName);

        this.drawDefaultBackground();

        this.fontRendererObj.drawString(title, (this.width - this.getRealStringWidth(title)) / 2, this.yPos + 10, color);

        this.drawGradientRect((this.width - 205) / 2 - 5, 0, (this.width - 205) / 2 - 4, this.height / 2, color, 0xFF000000 + color);
        this.drawGradientRect((this.width - 205) / 2 - 5, this.height / 2, (this.width - 205) / 2 - 4, this.height, 0xFF000000 + color, color);
        this.drawGradientRect((this.width + 205) / 2 + 4, 0, (this.width + 205) / 2 + 5, this.height / 2, color, 0xFF000000 + color);
        this.drawGradientRect((this.width + 205) / 2 + 4, this.height / 2, (this.width + 205) / 2 + 5, this.height, 0xFF000000 + color, color);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.0F);

        this.fontRendererObj.drawString(SAPUtils.translate(EnderStuffPlus.MOD_ID + ".guipet.name"), (this.width - 200) / 2, 20 + this.yPos, 0xFFFFFF);

        this.nameTxt.drawTextBox();

        super.drawScreen(mouseX, mouseY, partTicks);
    }

    private int getRealStringWidth(String str) {
        String strippedString = str.replaceAll("\247.", "");
        return this.fontRendererObj.getStringWidth(strippedString);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void initGui() {
        super.initGui();

        String btnName;
        String name = this.enderPet.getName();
        int color = this.enderPet.getGuiColor();

        this.yPos = (this.height - 156) / 2;

        this.nameTxt = new GuiTextField(this.fontRendererObj, (this.width - 198) / 2, this.yPos + 30, 198, 15);
        this.nameTxt.setText(name);

        GuiButton mountBtn = new GuiButtonPetGUI(0, (this.width - 200) / 2, this.yPos + 55, 200, 15, SAPUtils.translate("mount"), color).setBGAlpha(128);
        mountBtn.enabled = this.enderPet.canMount();
        this.buttonList.add(mountBtn);

        btnName = SAPUtils.translate(EnderStuffPlus.MOD_ID + (this.enderPet.isSitting() ? ".guipet.standUp" : ".guipet.sit"));
        GuiButton sitStandBtn = new GuiButtonPetGUI(1, (this.width - 200) / 2, this.yPos + 71, 200, 15, btnName, color).setBGAlpha(128);
        this.buttonList.add(sitStandBtn);

        btnName = SAPUtils.translate(EnderStuffPlus.MOD_ID + (this.enderPet.isFollowing() ? ".guipet.stay" : ".guipet.follow"));
        GuiButton stayFollowBtn = new GuiButtonPetGUI(2, (this.width - 200) / 2, this.yPos + 87, 200, 15, btnName, color).setBGAlpha(128);
        stayFollowBtn.enabled = !this.enderPet.isSitting();
        this.buttonList.add(stayFollowBtn);

        btnName = SAPUtils.translate(EnderStuffPlus.MOD_ID + ".guipet.putIntoEgg");
        GuiButton eggBtn = new GuiButtonPetGUI(3, (this.width - 200) / 2, this.yPos + 103, 200, 15, btnName, color).setBGAlpha(128);
        eggBtn.enabled = this.owner.inventory.hasItemStack(new ItemStack(Items.egg)) || this.owner.capabilities.isCreativeMode;
        this.buttonList.add(eggBtn);

        btnName = SAPUtils.translate(EnderStuffPlus.MOD_ID + ".guipet.close");
        GuiButton closeBtn = new GuiButtonPetGUI(4, (this.width - 200) / 2, this.yPos + 139, 200, 15, btnName, 0xA0A0A0).setBGAlpha(128);
        this.buttonList.add(closeBtn);
    }

    @Override
    protected void keyTyped(char key, int keyCode) {
        this.nameTxt.textboxKeyTyped(key, keyCode);

        if( this.nameTxt.isFocused() ) {
            if( (keyCode == Keyboard.KEY_RETURN || keyCode == Keyboard.KEY_ESCAPE) ) {
                this.nameTxt.setFocused(false);
                this.writeEntityName();
            }
        } else {
            if( (keyCode == Keyboard.KEY_ESCAPE || keyCode == this.mc.gameSettings.keyBindInventory.getKeyCode()) ) {
                this.writeEntityName();
                this.mc.thePlayer.closeScreen();
            } else if( keyCode == Keyboard.KEY_A || keyCode == Keyboard.KEY_S || keyCode == Keyboard.KEY_D ) {
                this.doAction(keyCode - Keyboard.KEY_A);
                this.mc.thePlayer.closeScreen();
            }
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        this.doAction(button.id);
        this.mc.thePlayer.closeScreen();
    }

    private void doAction(int actionId) {
        if( actionId == 4 ) {
            writeEntityName();
        } else {
            PacketProcessor.sendToServer(EnumPacket.ENDERPET_ACTION, Pair.with(this.enderPet.getEntity().getEntityId(), (byte) actionId));
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseBtn) {
        super.mouseClicked(mouseX, mouseY, mouseBtn);
        this.nameTxt.mouseClicked(mouseX, mouseY, mouseBtn);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        this.nameTxt.updateCursorCounter();

        if( this.nameTxt.isFocused() && this.nameTxt.getText().equals(SAPUtils.translate(EnderStuffPlus.MOD_ID + ".guipet.rename")) ) {
            this.nameTxt.setText("");
        }
    }

    private void writeEntityName() {
        if( !this.nameTxt.getText().isEmpty() ) {
            PacketProcessor.sendToServer(EnumPacket.ENDERPET_ACTION, Triplet.with(this.enderPet.getEntity().getEntityId(), (byte) 4, this.nameTxt.getText()));
        }
    }
}