package com.danielgamer321.rotp_kw.client;

import com.danielgamer321.rotp_kw.RotpKraftWorkAddon;
import com.danielgamer321.rotp_kw.client.ui.screen.kw.AdvancedRelease;
import com.github.standobyte.jojo.util.general.MathUtil;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

@SuppressWarnings("resource")
public class ClientUtil {
    public static final ResourceLocation ADDITIONAL_UI = new ResourceLocation(RotpKraftWorkAddon.MOD_ID, "textures/gui/additional.png");
    
    public static void openScreen(Screen screen) {
        Minecraft.getInstance().setScreen(screen);
    }

    public static void openAdvancedReleaseUi() {
        Minecraft.getInstance().setScreen(new AdvancedRelease());
    }
    
    
    public static PosOnScreen posOnScreen(Vector3d posInWorld, ActiveRenderInfo camera, MatrixStack matrixStack, Matrix4f projection) {
        Vector3d cameraPos = camera.getPosition();
        Vector3d vecToEntity = posInWorld.subtract(cameraPos);
        
        Matrix4f projectionMatrix = projection.copy();
        Matrix4f viewMatrix = matrixStack.last().pose();
        projectionMatrix.multiply(viewMatrix);
        Vector3f clip = MathUtil.multiplyPoint(projectionMatrix, vecToEntity);
        
        Vector2f posOnScreen = new Vector2f(clip.x() * 0.5F + 0.5F, clip.y() * 0.5F + 0.5F);
        boolean isOnScreen = MathHelper.abs(clip.x()) < 1 && MathHelper.abs(clip.y()) < 1 && clip.z() < 1;
        return new PosOnScreen(posOnScreen, isOnScreen);
    }
    
    public static class PosOnScreen {
        public static final PosOnScreen SCREEN_CENTER = new PosOnScreen(new Vector2f(0.5F, 0.5F), true);
        
        public final Vector2f pos;
        public final boolean isOnScreen;
        
        private PosOnScreen(Vector2f pos, boolean isOnScreen) {
            this.pos = pos;
            this.isOnScreen = isOnScreen;
        }
    }
}
