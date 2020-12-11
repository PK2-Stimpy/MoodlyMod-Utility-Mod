package us.np.moodlymod.module.modules.ui;

import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.world.DimensionType;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.lwjgl.opengl.GL11;
import us.np.moodlymod.MoodlyMod;
import us.np.moodlymod.Wrapper;
import us.np.moodlymod.module.Module;
import us.np.moodlymod.module.ModuleType;
import us.np.moodlymod.module.option.Option;
import us.np.moodlymod.module.option.OptionBoolean;
import us.np.moodlymod.util.ChatColor;
import us.np.moodlymod.util.ColorUtils;
import us.np.moodlymod.util.RenderUtils;

import java.awt.*;

public class HUDModule extends Module {
    public static final OptionBoolean watermark = new OptionBoolean("Watermark", true);
    public static final OptionBoolean position = new OptionBoolean("Position", true);
    public static final OptionBoolean fps = new OptionBoolean("FPS", true);
    public static final OptionBoolean arraylist = new OptionBoolean("ArrayList", true);

    public HUDModule() {
        super("HUD", new String[] {""}, "NONE", Color.YELLOW, ModuleType.UI);
        if(!isEnabled()) toggle(true);
        addOption(watermark);
        addOption(position);
        addOption(fps);
        addOption(arraylist);
        endOption();
    }

    @EventHandler
    private Listener<RenderGameOverlayEvent.Text> textListener = new Listener<>(event -> {
        if(watermark.getValue()) {
            GL11.glPushMatrix();
            GL11.glScalef(1.5f, 1.5f, 1.5f);
            Wrapper.getMC().fontRenderer.drawStringWithShadow(MoodlyMod.NAME + " " + MoodlyMod.VERSION, 4, 4, Color.WHITE.getRGB());
            GL11.glPopMatrix();
        }

        int colorRect = ColorUtils.color(0.0F, 0.0F, 0.0F, 0.0F);
        int colorRect2 = ColorUtils.color(0.0F, 0.0F, 0.0F, 0.5F);
        boolean isChatOpen = Wrapper.getMC().currentScreen instanceof GuiChat;
        ScaledResolution sr = new ScaledResolution(Wrapper.getMC());
        boolean nether = Wrapper.getMC().world.provider.isNether();
        boolean end = Wrapper.getMC().world.provider.getDimensionType() == DimensionType.THE_END;
        if(position.getValue()) {
            double x = Wrapper.getMC().player.posX;
            double y = Wrapper.getMC().player.posY;
            double z = Wrapper.getMC().player.posZ;
            String coords = String.format("\u00a77X: \u00a7f%s \u00a77Y: \u00a7f%s \u00a77Z: \u00a7f%s", RenderUtils.DF((float)x, 1), RenderUtils.DF((float)y, 1), RenderUtils.DF((float)z, 1));
            if(nether) {
                double tX = x*8;
                double tZ = z*8;
                coords = String.format("\u00a77X: \u00a7f%s \u00a77Y: \u00a7f%s \u00a77Z: \u00a7f%s \u00a77(\u00a7f%s \u00a7f%s\u00a77)", RenderUtils.DF((float)x, 1), RenderUtils.DF((float)y, 1), RenderUtils.DF((float)z, 1), RenderUtils.DF((float)tX, 1), RenderUtils.DF((float)tZ, 1));
            } else if(!nether && !end) {
                double tX = x/8;
                double tZ = z/8;
                coords = String.format("\u00a77X: \u00a7f%s \u00a77Y: \u00a7f%s \u00a77Z: \u00a7f%s \u00a77(\u00a7f%s \u00a7f%s\u00a77)", RenderUtils.DF((float)x, 1), RenderUtils.DF((float)y, 1), RenderUtils.DF((float)z, 1), RenderUtils.DF((float)tX, 1), RenderUtils.DF((float)tZ, 1));
            }

            int heightCoords = isChatOpen ? sr.getScaledHeight() - 25 : sr.getScaledHeight() - 10;

            RenderUtils.drawStringWithRect(coords, 4, heightCoords, Color.WHITE.getRGB(),
                    colorRect, colorRect2);
        }
        if(fps.getValue()) {
            int heightFPS = isChatOpen ? sr.getScaledHeight() - 37 : sr.getScaledHeight() - 22;
            RenderUtils.drawStringWithRect("\u00a77FPS: \u00a7f" + Wrapper.getMC().getDebugFPS(), 4, heightFPS, Color.WHITE.getRGB(),
                    colorRect, colorRect2);
        }
        if(arraylist.getValue()) {
            int yPos = 24;
            int xPos = 4;
            for(Module module : MoodlyMod.moduleManager.getModules()) {
                if(!module.isEnabled() || !module.showInArray) continue;
                RenderUtils.drawStringWithRect(
                        module.displayName + ((module.getMeta().contentEquals("") ? "" : (" " + ChatColor.parse("&", "&7" + module.getMeta())))),
                        xPos,
                        yPos,
                        module.getColor().getRGB(),
                        colorRect,
                        colorRect2);
                yPos += 12;
            }
        }
    });
}