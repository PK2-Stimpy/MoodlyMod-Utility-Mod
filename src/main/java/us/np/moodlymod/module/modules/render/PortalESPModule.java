package us.np.moodlymod.module.modules.render;

import javafx.geometry.BoundingBox;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.GL11;
import us.np.moodlymod.Wrapper;
import us.np.moodlymod.event.custom.render.RenderEvent;
import us.np.moodlymod.module.Module;
import us.np.moodlymod.module.ModuleType;
import us.np.moodlymod.module.option.BetterMode;
import us.np.moodlymod.module.option.OptionBetterMode;
import us.np.moodlymod.module.option.OptionBoolean;
import us.np.moodlymod.module.option.OptionDouble;
import us.np.moodlymod.util.RenderUtils;

import java.awt.*;
import java.util.ArrayList;

public class PortalESPModule extends Module {
    public static OptionDouble distance = new OptionDouble("Distance", 60D, 10D, 100D);
    public static OptionBoolean box = new OptionBoolean("Box", false);
    public static OptionDouble boxAlpha = new OptionDouble("Box Alpha", 125D, 0D, 255D);
    public static OptionBoolean outline = new OptionBoolean("Outline", true);
    public static OptionDouble lineWidth = new OptionDouble("Line Width", 1D, .1D, 5D);

    private int cooldownTicks = 80;
    private final ArrayList<BlockPos> blockPosArrayList = new ArrayList<BlockPos>();;
    public PortalESPModule() {
        super("PortalESP", null, "NONE", Color.BLUE, ModuleType.RENDER);
        addOption(distance);
        addOption(box);
        addOption(boxAlpha);
        addOption(outline);
        addOption(lineWidth);
        endOption();
    }

    @EventHandler
    private Listener<TickEvent.ClientTickEvent> clientTickEventListener = new Listener<>(event -> {
        if (PortalESPModule.mc.world == null) return;

        if (this.cooldownTicks < 1) {
            this.blockPosArrayList.clear();
            this.compileDL();
            this.cooldownTicks = 80;
        }
        --this.cooldownTicks;
    });

    @EventHandler
    private Listener<RenderEvent> renderEventListener = new Listener<>(event -> {
        if (PortalESPModule.mc.world == null) return;

        for (final BlockPos pos : this.blockPosArrayList) {
            AxisAlignedBB bb = new AxisAlignedBB(
                    pos.getX(),
                    pos.getY(),
                    pos.getZ(),
                    pos.getX()+1,
                    pos.getY()+1,
                    pos.getZ()+1
            );
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.disableDepth();
            GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask(false);
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
            GL11.glLineWidth(1.5f);

            RenderGlobal.drawBoundingBox(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ, 255, 20, 30, 0.50f);
            if(box.getValue()) RenderGlobal.renderFilledBox(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ, 255, 20, 30, 0.22f);

            GL11.glDisable(GL11.GL_LINE_SMOOTH);
            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    });

    private void compileDL() {
        if (PortalESPModule.mc.world == null || PortalESPModule.mc.player == null) return;

        for (int x = (int)PortalESPModule.mc.player.posX - this.distance.getValue().intValue(); x <= (int)PortalESPModule.mc.player.posX + this.distance.getValue().intValue(); ++x)
            for (int y = (int)PortalESPModule.mc.player.posZ - this.distance.getValue().intValue(); y <= (int)PortalESPModule.mc.player.posZ + this.distance.getValue().intValue(); ++y)
                for (int z = (int)Math.max(PortalESPModule.mc.player.posY - this.distance.getValue(), 0.0); z <= Math.min(PortalESPModule.mc.player.posY + this.distance.getValue(), 255.0); ++z) {
                    final BlockPos pos = new BlockPos(x, y, z);
                    final Block block = PortalESPModule.mc.world.getBlockState(pos).getBlock();
                    if (block == Blocks.PORTAL) {
                        this.blockPosArrayList.add(pos);
                    }
                }
    }
}