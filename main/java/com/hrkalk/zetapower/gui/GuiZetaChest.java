package com.hrkalk.zetapower.gui;

import com.hrkalk.zetapower.tileentities.ZetaChest;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiZetaChest extends GuiContainer {
    private IInventory playerInv;
    private ZetaChest te;

    public GuiZetaChest(IInventory playerInv, ZetaChest te) {
        super(new ContainerZetaChest(playerInv, te));

        this.xSize = 176;
        this.ySize = 166;

        this.playerInv = playerInv;
        this.te = te;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        mc.getTextureManager().bindTexture(new ResourceLocation("zetapower", "textures/gui/zeta_chest_gui.png"));
        drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String s = this.te.getDisplayName().getUnformattedText();
        this.fontRendererObj.drawString(s, 88 - this.fontRendererObj.getStringWidth(s) / 2, 6, 0x404040);
        this.fontRendererObj.drawString(this.playerInv.getDisplayName().getUnformattedText(), 8, 72, 0x404040);
    }
}