package com.danielgamer321.rotp_kw.client.ui.screen.kw;

import com.danielgamer321.rotp_kw.RotpKraftWorkAddon;
import com.danielgamer321.rotp_kw.init.InitStands;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.client.InputHandler;
import com.github.standobyte.jojo.client.ui.actionshud.ActionsOverlayGui;
import com.github.standobyte.jojo.network.PacketManager;
import com.github.standobyte.jojo.network.packets.fromclient.ClClickActionPacket;
import com.github.standobyte.jojo.power.IPower;
import com.github.standobyte.jojo.power.IPower.PowerClassification;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class AdvancedRelease extends Screen {
    static final int WINDOW_WIDTH = 184;
    static final int WINDOW_HEIGHT = 184;
    static final int WINDOW_THIN_BORDER = 10;
    static final int WINDOW_UPPER_BORDER = 10;
    public static final ResourceLocation WINDOW = new ResourceLocation(RotpKraftWorkAddon.MOD_ID, "textures/gui/admin_projectile_button.png");
    public static int screenX;
    public static int screenY;

    private static PowerClassification standPower = null;
    private IPower<?, ?> selectedPower;
    private List<IPower<?, ?>> powersPresent = new ArrayList<>();

    private AdvancedReleaseButton releaseEnderPearl;
    private AdvancedReleaseButton releaseProjectilesReady;
    private AdvancedReleaseButton releaseAllProjectiles;
    private AdvancedReleaseButton releaseProjectilesNotReady;
    private AdvancedReleaseButton releaseBeneficialItems;
    private AdvancedReleaseButton exitButton;

    public AdvancedRelease() {
        super(NarratorChatListener.NO_TITLE);
    }

    @Override
    protected void init() {
        initWidgets();

        if (standPower != null) {
            IPower.getPowerOptional(minecraft.player, standPower).ifPresent(power -> {
                if (!power.hasPower()) {
                    standPower = null;
                }
            });
        }

        powersPresent.clear();
        for (PowerClassification powerClassification : PowerClassification.values()) {
            IPower.getPowerOptional(minecraft.player, powerClassification).ifPresent(power -> {
                if (power.hasPower()) {
                    powersPresent.add(power);
                    if (selectedPower == null || powerClassification == ActionsOverlayGui.getInstance().getCurrentMode()) {
                        powersPresent(power);
                    }
                }
            });
        }

        if (standPower != null && selectedPower == null) {
            powersPresent(IPower.getPlayerPower(minecraft.player, standPower));
        }
    }

    public void removeButton(Widget button) {
        buttons.remove(button);
        children.remove(button);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (mouseInsideWindow(mouseX, mouseY) && super.mouseClicked(mouseX, mouseY, mouseButton)) {
            setDragging(false);
            return true;
        }
        setDragging(false);
        int x = windowPosX();
        int y = windowPosY();
        return false;
    }

    private void powersPresent(IPower<?, ?> power) {
        if (power != null && power.hasPower()) {
            selectedPower = power;
            standPower = power.getPowerClassification();
        }
    }
    
    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
        boolean dragging = isDragging();
        if (super.mouseReleased(mouseX, mouseY, mouseButton)) {
            return true;
        }
        setDragging(dragging);
        int x = windowPosX();
        int y = windowPosY();
        return false;
    }

    @Override
    public boolean mouseDragged(double xPos, double yPos, int mouseButton, double xMovement, double yMovement) {
        if (xMovement != 0 || yMovement != 0) {
            setDragging(true);
        }
        return super.mouseDragged(xPos, yPos, mouseButton, xMovement, yMovement);
    }
    
    boolean mouseInsideWindow(double mouseX, double mouseY) {
        int x = windowPosX();
        int y = windowPosY();
        return mouseX > x + WINDOW_THIN_BORDER && mouseX < x + WINDOW_WIDTH - WINDOW_THIN_BORDER 
                && mouseY > y + WINDOW_UPPER_BORDER && mouseY < y + WINDOW_HEIGHT - WINDOW_THIN_BORDER;
    }
    
    @Override
    public boolean keyPressed(int key, int scanCode, int modifiers) {
        if (InputHandler.getInstance().hamonSkillsWindow.matches(key, scanCode)) {
            minecraft.setScreen(null);
            minecraft.mouseHandler.grabMouse();
            return true;
        } else {
            return super.keyPressed(key, scanCode, modifiers);
        }
    }

    public boolean works() {
        return selectedPower != null && selectedPower.hasPower() &&
                (selectedPower instanceof IStandPower ||
                (selectedPower instanceof IPower));
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTick) {
        if (!works()) {
            onClose();
            return;
        }
        int x = windowPosX();
        int y = windowPosY();
        screenX = x;
        screenY = y;
        renderWindow(matrixStack, x, y);
        buttons.forEach(button -> button.render(matrixStack, mouseX, mouseY, partialTick));
        renderToolTips(matrixStack, mouseX, mouseY, x, y);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    int windowPosX() {
        return (width - WINDOW_WIDTH) / 2;
    }
    
    int windowPosY() {
        return (height - WINDOW_HEIGHT) / 2;
    }

    public void renderWindow(MatrixStack matrixStack, int windowX, int windowY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        minecraft.getTextureManager().bind(WINDOW);
        blit(matrixStack, windowX, windowY, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        RenderSystem.enableRescaleNormal();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }

    private int tooltipOffsetX;
    private int tooltipOffsetY;

    private void initWidgets() {
        int x = windowPosX();
        int y = windowPosY();

        releaseEnderPearl = addButton(new AdvancedReleaseButton(x + 5, y + 120, 32, 32,
                button -> {
                    RayTraceResult target = InputHandler.getInstance().mouseTarget;
                    ActionTarget actionTarget = ActionTarget.fromRayTraceResult(target);
                    ClClickActionPacket packet = new ClClickActionPacket(
                            standPower, InitStands.KRAFT_WORK_RELEASE_ENDER_PEARL.get(), actionTarget, false);
                    PacketManager.sendToServer(packet);
                    minecraft.setScreen(null);
                    minecraft.mouseHandler.grabMouse();
                },
                () -> {
                    return new TranslationTextComponent("manage_projectiles.release_ender_pearl");
                }, this, 96));
        releaseProjectilesReady = addButton(new AdvancedReleaseButton(x + 5, y + 32, 32, 32,
                button -> {
                    RayTraceResult target = InputHandler.getInstance().mouseTarget;
                    ActionTarget actionTarget = ActionTarget.fromRayTraceResult(target);
                    ClClickActionPacket packet = new ClClickActionPacket(
                            standPower, InitStands.KRAFT_WORK_RELEASE_PROJECTILES_R.get(), actionTarget, false);
                    PacketManager.sendToServer(packet);
                    minecraft.setScreen(null);
                    minecraft.mouseHandler.grabMouse();
                },
                () -> {
                    return new TranslationTextComponent("manage_projectiles.release_ready");
                }, this, 0));
        releaseAllProjectiles = addButton(new AdvancedReleaseButton(x + 76, y, 32, 32,
                button -> {
                    RayTraceResult target = InputHandler.getInstance().mouseTarget;
                    ActionTarget actionTarget = ActionTarget.fromRayTraceResult(target);
                    ClClickActionPacket packet = new ClClickActionPacket(
                            standPower, InitStands.KRAFT_WORK_RELEASE_ALL_PROJECTILES.get(), actionTarget, false);
                    PacketManager.sendToServer(packet);
                    minecraft.setScreen(null);
                    minecraft.mouseHandler.grabMouse();
                },
                () -> {
                    return new TranslationTextComponent("manage_projectiles.release_all");
                }, this, 64));
        releaseProjectilesNotReady = addButton(new AdvancedReleaseButton(x + 147, y + 32, 32, 32,
                button -> {
                    RayTraceResult target = InputHandler.getInstance().mouseTarget;
                    ActionTarget actionTarget = ActionTarget.fromRayTraceResult(target);
                    ClClickActionPacket packet = new ClClickActionPacket(
                            standPower, InitStands.KRAFT_WORK_RELEASE_PROJECTILES_NR.get(), actionTarget, true);
                    PacketManager.sendToServer(packet);
                    minecraft.setScreen(null);
                    minecraft.mouseHandler.grabMouse();
                },
                () -> {
                    return new TranslationTextComponent("manage_projectiles.release_not_ready");
                }, this, 32));
        releaseBeneficialItems = addButton(new AdvancedReleaseButton(x + 147, y + 120, 32, 32,
                button -> {
                    RayTraceResult target = InputHandler.getInstance().mouseTarget;
                    ActionTarget actionTarget = ActionTarget.fromRayTraceResult(target);
                    ClClickActionPacket packet = new ClClickActionPacket(
                            standPower, InitStands.KRAFT_WORK_RELEASE_BENEFICIAL.get(), actionTarget, true);
                    PacketManager.sendToServer(packet);
                    minecraft.setScreen(null);
                    minecraft.mouseHandler.grabMouse();
                },
                () -> {
                    return new TranslationTextComponent("manage_projectiles.release_beneficial_items");
                }, this, 128));
        exitButton = addButton(new AdvancedReleaseButton(x + 76, y + 152, 32, 32,
                button -> {
                    minecraft.setScreen(null);
                    minecraft.mouseHandler.grabMouse();
                },
                () -> {
                    return new TranslationTextComponent("manage_projectiles.exit_button");
                }, this, 160));
    }

    private void renderToolTips(MatrixStack matrixStack, int mouseX, int mouseY, int windowX, int windowY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    // these two overrides make the tooltip wrap at the right edge of the screen correctly
    @Override
    public void renderToolTip(MatrixStack matrixStack, List<? extends IReorderingProcessor> tooltips, 
            int mouseX, int mouseY, FontRenderer font) {
        matrixStack.translate(-tooltipOffsetX, -tooltipOffsetY, 0);
        super.renderToolTip(matrixStack, tooltips, mouseX + tooltipOffsetX, mouseY + tooltipOffsetY, font);
        matrixStack.translate(tooltipOffsetX, tooltipOffsetY, 0);
    }
    
    @Override
    public void renderWrappedToolTip(MatrixStack matrixStack, List<? extends ITextProperties> tooltips, 
            int mouseX, int mouseY, FontRenderer font) {
        matrixStack.translate(-tooltipOffsetX, -tooltipOffsetY, 0);
        super.renderWrappedToolTip(matrixStack, tooltips, mouseX + tooltipOffsetX, mouseY + tooltipOffsetY, font);
        matrixStack.translate(tooltipOffsetX, tooltipOffsetY, 0);
    }
}
