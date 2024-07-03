package com.danielgamer321.rotp_kw.client;

import com.danielgamer321.rotp_kw.init.InitEffects;
import com.github.standobyte.jojo.client.ControllerStand;

import com.github.standobyte.jojo.client.ui.actionshud.ActionsOverlayGui;
import com.github.standobyte.jojo.power.IPower;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.MovementInput;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class InputHandler {
    private static InputHandler instance = null;

    private Minecraft mc;

    private InputHandler(Minecraft mc) {
        this.mc = mc;
    }

    public static void init(Minecraft mc) {
        if (instance == null) {
            instance = new InputHandler(mc);
            MinecraftForge.EVENT_BUS.register(instance);
        }
    }

    public static InputHandler getInstance() {
        return instance;
    }

    public boolean isControllingStand() {
        return ControllerStand.getInstance().isControllingStand();
    }

    @SubscribeEvent
    public void handleKeyBindings(TickEvent.ClientTickEvent event) {
        if (mc.overlay != null || (mc.screen != null && !mc.screen.passEvents) || mc.level == null || mc.player.isSpectator()) {
            return;
        }

        if (event.phase == TickEvent.Phase.START) {
            tickEffects();
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void MovementInputlock(InputUpdateEvent event) {
        if (InitEffects.isLocked(mc.player)) {
            MovementInput input = event.getMovementInput();
            if (!isControllingStand()) {
                input.forwardImpulse = 0;
                input.leftImpulse = 0;
                input.jumping = false;
                input.up = false;
                input.down = false;
                input.right = false;
                input.left = false;
            }
            if (InitEffects.shiftLocked(mc.player) && (!isControllingStand() ||
                    mc.player.hasEffect(InitEffects.TRANSPORT_LOCKED.get()))) {
                input.shiftKeyDown = false;
            }
        }
    }

    private boolean wasStunned = false;
    private double prevSensitivity = 0.5;
    private static final double ZERO_SENSITIVITY = -1.0 / 3.0;
    @SubscribeEvent
    public void setMouseSensitivity(TickEvent.ClientTickEvent event) {
        if (mc.player == null) {
            if (mc.options.sensitivity <= ZERO_SENSITIVITY) {
                mc.options.sensitivity = prevSensitivity;
            }
            return;
        }

        if (mc.player.hasEffect(InitEffects.LOCKED_HELMET.get()) && !isControllingStand()) {
            if (!wasStunned) {
                prevSensitivity = mc.options.sensitivity;
                wasStunned = true;
            }
            mc.options.sensitivity = ZERO_SENSITIVITY;
            return;
        }
        else if (wasStunned) {
            mc.options.sensitivity = prevSensitivity;
            wasStunned = false;
        }
    }

    private boolean mouseButtonsLocked = false;
    private InputMappings.Input lmbKey;
    private InputMappings.Input rmbKey;
    private InputMappings.Input helpKey;

    public void mouseButtonsLockedTick() {
        if (!mouseButtonsLocked) {
            helpKey = mc.options.keySpectatorOutlines.getDefaultKey();
            lmbKey = mc.options.keyAttack.getKey();
            rmbKey = mc.options.keyUse.getKey();
            mouseButtonsLocked = true;
        }

        if (mc.options.keyAttack.getKey() != helpKey || mc.options.keyUse.getKey() != helpKey) {
            if (mc.player.hasEffect(InitEffects.LOCKED_OFF_HAND.get())) {
                mc.options.keyUse.setKey(helpKey);
            }
            if (mc.player.hasEffect(InitEffects.LOCKED_MAIN_HAND.get())) {
                mc.options.keyAttack.setKey(helpKey);
            }
            KeyBinding.resetMapping();
        }
    }

    public void mouseButtonsLockedEnd() {
        if (mouseButtonsLocked) {
            mc.options.keyAttack.setKey(lmbKey);
            mc.options.keyUse.setKey(rmbKey);
            mouseButtonsLocked = false;
            KeyBinding.resetMapping();
        }
    }

    private boolean ItemButtonsLocked = false;
    private InputMappings.Input swapKey;
    private InputMappings.Input dropKey;

    public void ItemButtonsLockedTick() {
        if (!ItemButtonsLocked) {
            helpKey = mc.options.keySpectatorOutlines.getDefaultKey();
            swapKey = mc.options.keySwapOffhand.getKey();
            dropKey = mc.options.keyDrop.getKey();
            ItemButtonsLocked = true;
        }

        if (mc.options.keySwapOffhand.getKey() != helpKey || mc.options.keyDrop.isDown()) {
            if (mc.player.hasEffect(InitEffects.LOCKED_MAIN_HAND.get())) {
                mc.options.keyDrop.setKey(helpKey);
            }
            mc.options.keySwapOffhand.setKey(helpKey);
            KeyBinding.resetMapping();
        }
    }

    public void ItemButtonsLockedEnd() {
        if (ItemButtonsLocked) {
            mc.options.keySwapOffhand.setKey(swapKey);
            mc.options.keyDrop.setKey(dropKey);
            ItemButtonsLocked = false;
            KeyBinding.resetMapping();
        }
    }

    private boolean inventoryButtonLocked = false;
    private InputMappings.Input inventory;

    public void inventoryButtonLockedTick() {
        if (!inventoryButtonLocked) {
            helpKey = mc.options.keySpectatorOutlines.getDefaultKey();
            inventory = mc.options.keyInventory.getKey();
            inventoryButtonLocked = true;
        }

        if (mc.options.keyInventory.getKey() != helpKey) {
            mc.options.keyInventory.setKey(helpKey);
            KeyBinding.resetMapping();
        }
    }

    public void inventoryButtonLockedEnd() {
        if (inventoryButtonLocked) {
            mc.options.keyInventory.setKey(inventory);
            inventoryButtonLocked = false;
            KeyBinding.resetMapping();
        }
    }

    private void tickEffects() {
        if (mc.player != null && InitEffects.lockedArms(mc.player)) {
            ActionsOverlayGui overlay = ActionsOverlayGui.getInstance();
            if (!isControllingStand() || IPower.PowerClassification.STAND != overlay.getCurrentMode()) {
                mouseButtonsLockedTick();
            }
            else {
                mouseButtonsLockedEnd();
            }
            ItemButtonsLockedTick();
        }
        else {
            mouseButtonsLockedEnd();
            ItemButtonsLockedEnd();
        }
        if (mc.player != null && InitEffects.lockedInventory(mc.player)) {
            inventoryButtonLockedTick();
        }
        else {
            inventoryButtonLockedEnd();
        }
    }
}
