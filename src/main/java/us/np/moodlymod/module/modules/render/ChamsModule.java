package us.np.moodlymod.module.modules.render;

import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;
import us.np.moodlymod.MoodlyMod;
import us.np.moodlymod.event.custom.CustomEvent;
import us.np.moodlymod.event.custom.entityplayersp.PlayerUpdateEvent;
import us.np.moodlymod.event.custom.modelplayer.ModelPlayerRenderEvent;
import us.np.moodlymod.event.custom.renderlivingbase.ModelRenderEvent;
import us.np.moodlymod.event.custom.renderlivingbase.PostRenderLayersEvent;
import us.np.moodlymod.module.Module;
import us.np.moodlymod.module.ModuleType;
import us.np.moodlymod.module.option.BetterMode;
import us.np.moodlymod.module.option.OptionBetterMode;
import us.np.moodlymod.module.option.OptionBoolean;
import us.np.moodlymod.module.option.OptionDouble;
import us.np.moodlymod.util.EntityUtil;
import us.np.moodlymod.util.FriendsUtil;

import java.awt.*;

public class ChamsModule extends Module {
    public static final OptionBetterMode mode = new OptionBetterMode("Mode", 0, BetterMode.construct("ESP", "Normal", "Walls"));
    /* Walls Mode */
    public static final OptionDouble Vr = new OptionDouble("Visible Red", 255D, 0D, 255D);
    public static final OptionDouble Vg = new OptionDouble("Visible Green", 0D, 0D, 255D);
    public static final OptionDouble Vb = new OptionDouble("Visible Blue", 0D, 0D, 255D);
    public static final OptionDouble Wr = new OptionDouble("Walls Red", 0D, 0D, 255D);
    public static final OptionDouble Wg = new OptionDouble("Walls Green", 255D, 0D, 255D);
    public static final OptionDouble Wb = new OptionDouble("Walls Blue", 0D, 0D, 255D);
    /* ESP Mode */
    public static final OptionBoolean hand = new OptionBoolean("Hand", true);
    public static final OptionBoolean lines = new OptionBoolean("Lines", false);
    public static final OptionDouble width = new OptionDouble("Width", 1D, 0D, 10D);
    public static final OptionBoolean friendColor = new OptionBoolean("Friends", true);
    public static final OptionBoolean rainbow = new OptionBoolean("Rainbow", false);
    public static final OptionDouble r = new OptionDouble("Red", 0D, 0D, 255D);
    public static final OptionDouble g = new OptionDouble("Green", 255D, 0D, 255D);
    public static final OptionDouble b = new OptionDouble("Blue", 255D, 0D, 255D);
    public static final OptionDouble a = new OptionDouble("Alpha", 63D, 0D, 255D);
    /* Chams */
    public static final OptionBoolean players = new OptionBoolean("Players", true);
    public static final OptionBoolean animals = new OptionBoolean("Animals", false);
    public static final OptionBoolean mobs = new OptionBoolean("Mobs", false);
    public static final OptionBoolean crystals = new OptionBoolean("Crystals", true);


    public ChamsModule() {
        super("Chams", null, "NONE", Color.CYAN, ModuleType.RENDER);
        addOption(mode);
        /* Walls Mode */
        addOption(Vr.visibleWhen(value -> mode.getMode() == 2));
        addOption(Vg.visibleWhen(value -> mode.getMode() == 2));
        addOption(Vb.visibleWhen(value -> mode.getMode() == 2));
        addOption(Wr.visibleWhen(value -> mode.getMode() == 2));
        addOption(Wg.visibleWhen(value -> mode.getMode() == 2));
        addOption(Wb.visibleWhen(value -> mode.getMode() == 2));
        /* ESP Mode */
        addOption(hand.visibleWhen(value -> mode.getMode() == 0));
        addOption(lines.visibleWhen(value -> mode.getMode() == 0));
        addOption(width.visibleWhen(value -> mode.getMode() == 0));
        addOption(friendColor.visibleWhen(value -> mode.getMode() == 0));
        addOption(rainbow.visibleWhen(value -> mode.getMode() == 0));
        addOption(r.visibleWhen(value -> mode.getMode() == 0));
        addOption(g.visibleWhen(value -> mode.getMode() == 0));
        addOption(b.visibleWhen(value -> mode.getMode() == 0));
        addOption(a.visibleWhen(value -> mode.getMode() == 0));
        /* Chams */
        addOption(players);
        addOption(animals);
        addOption(mobs);
        addOption(crystals.visibleWhen(value -> mode.getMode() == 2 || mode.getMode() == 0));
        endOption();
    }

    public static class Rainbow {
        public static int rgb;
        public static int a;
        public static int r;
        public static int g;
        public static int b;
        static float hue;

        public static void updateRainbow() {
            Rainbow.rgb = Color.HSBtoRGB(Rainbow.hue, 24.0f / 255.0f, 100.0f / 255.0f);
            Rainbow.a = (Rainbow.rgb >>> 24 & 0xFF);
            Rainbow.r = (Rainbow.rgb >>> 16 & 0xFF);
            Rainbow.g = (Rainbow.rgb >>> 8 & 0xFF);
            Rainbow.b = (Rainbow.rgb & 0xFF);
            Rainbow.hue += 100.0f / 100000.0f;
            if (Rainbow.hue > 1.0f) {
                --Rainbow.hue;
            }
        }

        static {
            Rainbow.hue = 0.01f;
        }
    }

    @EventHandler
    private Listener<PlayerUpdateEvent> updateEventListener = new Listener<>(event -> { Rainbow.updateRainbow(); });

    public static boolean renderChams(final Entity entity) { return mode.getMode() != 0 && ((entity instanceof EntityPlayer) ? players.getValue() : (EntityUtil.isPassive(entity) ? animals.getValue() : ((boolean)mobs.getValue()))); }

    @EventHandler
    private Listener<ModelRenderEvent> modelRenderEventListener = new Listener<>(event -> {
        if(event.getEra() != CustomEvent.Era.PRE) return;

        if(mode.getMode() == 2) {
            if (event.entity instanceof EntityOtherPlayerMP && !players.getValue()) return;
            if (EntityUtil.isPassive(event.entity) && !animals.getValue()) return;
            if (!EntityUtil.isPassive(event.entity) && !mobs.getValue()) return;

            GlStateManager.pushMatrix();
            GL11.glDisable(2929);
            GL11.glColor4f(this.Wr.getValue().floatValue() / 255.0f, this.Wg.getValue().floatValue() / 255.0f, this.Wb.getValue().floatValue() / 255.0f, 1.0f);
            GL11.glDisable(3553);
            event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.ageInTicks, event.netHeadYaw, event.headPitch, event.scaleFactor);
            GL11.glEnable(2929);
            GL11.glColor4f(this.Vr.getValue().floatValue() / 255.0f, this.Vg.getValue().floatValue() / 255.0f, this.Vb.getValue().floatValue() / 255.0f, 1.0f);
            event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.ageInTicks, event.netHeadYaw, event.headPitch, event.scaleFactor);
            GL11.glEnable(3553);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.popMatrix();
            event.cancel();
        } else if(mode.getMode() == 0) {
            if((event.entity instanceof EntityOtherPlayerMP)) return;

            final Color c = (this.friendColor.getValue() && MoodlyMod.friendsUtil.isFriend(event.entity.getName())) ? new Color(0.27f, 0.7f, 0.92f) : (this.rainbow.getValue() ? new Color(Rainbow.rgb) : new Color(this.r.getValue().intValue(), this.g.getValue().intValue(), this.b.getValue().intValue()));
            if (EntityUtil.isPassive(event.entity) && animals.getValue()) {
                GL11.glPushMatrix();
                GL11.glEnable(32823);
                GL11.glPolygonOffset(1.0f, -100000.0f);
                GL11.glPushAttrib(1048575);

                if (!lines.getValue())
                    GL11.glPolygonMode(1028, 6914);
                else
                    GL11.glPolygonMode(1028, 6913);
                GL11.glDisable(3553);
                GL11.glDisable(2896);
                GL11.glDisable(2929);
                GL11.glEnable(2848);
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, 771);
                GL11.glColor4f(c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f, a.getValue().intValue() / 255.0f);

                if (lines.getValue())
                    GL11.glLineWidth(width.getValue().floatValue());
                event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.ageInTicks, event.netHeadYaw, event.headPitch, event.scaleFactor);
                GL11.glPopAttrib();
                GL11.glPolygonOffset(1.0f, 100000.0f);
                GL11.glDisable(32823);
                GL11.glPopMatrix();
                event.cancel();
            } else if (!EntityUtil.isPassive(event.entity) && mobs.getValue()) {
                GL11.glPushMatrix();
                GL11.glEnable(32823);
                GL11.glPolygonOffset(1.0f, -100000.0f);
                GL11.glPushAttrib(1048575);

                if (!lines.getValue())
                    GL11.glPolygonMode(1028, 6914);
                else
                    GL11.glPolygonMode(1028, 6913);
                GL11.glDisable(3553);
                GL11.glDisable(2896);
                GL11.glDisable(2929);
                GL11.glEnable(2848);
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, 771);
                GL11.glColor4f(c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f, a.getValue().intValue() / 255.0f);

                if (lines.getValue())
                    GL11.glLineWidth(width.getValue().floatValue());
                event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.ageInTicks, event.netHeadYaw, event.headPitch, event.scaleFactor);
                GL11.glPopAttrib();
                GL11.glPolygonOffset(1.0f, 100000.0f);
                GL11.glDisable(32823);
                GL11.glPopMatrix();
                event.cancel();
            }
        }
    });

    private Listener<PostRenderLayersEvent> layersEventListener = new Listener<>(event -> {
        if(mode.getMode() == 0) {
            /* if (!event.renderer.bindEntityTexture((Entity)event.entity)) return; */

            final Color c = (friendColor.getValue() && MoodlyMod.friendsUtil.isFriend(event.entity.getName())) ? new Color(0.27f, 0.7f, 0.92f) : (this.rainbow.getValue() ? new Color(Rainbow.rgb) : new Color(r.getValue().intValue(), g.getValue().intValue(), b.getValue().intValue()));
            if (event.getEra() == CustomEvent.Era.PRE && event.entity instanceof EntityOtherPlayerMP && players.getValue()) {
                GL11.glPushMatrix();
                GL11.glEnable(32823);
                GL11.glPolygonOffset(1.0f, -100000.0f);
                GL11.glPushAttrib(1048575);

                if (!lines.getValue())
                    GL11.glPolygonMode(1028, 6914);
                else
                    GL11.glPolygonMode(1028, 6913);
                GL11.glDisable(3553);
                GL11.glDisable(2896);
                GL11.glDisable(2929);
                GL11.glEnable(2848);
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, 771);
                GL11.glColor4f(c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f, a.getValue().intValue() / 255.0f / 2.0f);

                if (lines.getValue())
                    GL11.glLineWidth(width.getValue().intValue());
                event.modelBase.render((Entity)event.entity, event.limbSwing, event.limbSwingAmount, event.ageInTicks, event.netHeadYaw, event.headPitch, event.scaleIn);
                GL11.glPopAttrib();
                GL11.glPolygonOffset(1.0f, 100000.0f);
                GL11.glDisable(32823);
                GL11.glPopMatrix();
            }
        }
    });

    @EventHandler
    private Listener<ModelPlayerRenderEvent> modelPlayerRenderEventListener = new Listener<>(event -> {
        if(mode.getMode() == 0 && players.getValue()) {
            final Color c = (friendColor.getValue() && MoodlyMod.friendsUtil.isFriend(event.entity.getName())) ? new Color(0.27f, 0.7f, 0.92f) : (rainbow.getValue() ? new Color(Rainbow.rgb) : new Color(r.getValue().intValue(), g.getValue().intValue(), b.getValue().intValue()));
            switch (event.getEra()) {
                case PRE: {
                    GL11.glPushMatrix();
                    GL11.glEnable(32823);
                    GL11.glPolygonOffset(1.0f, -1.0E7f);
                    GL11.glPushAttrib(1048575);
                    if (!lines.getValue())
                        GL11.glPolygonMode(1028, 6914);
                    else
                        GL11.glPolygonMode(1028, 6913);
                    GL11.glDisable(3553);
                    GL11.glDisable(2896);
                    GL11.glDisable(2929);
                    GL11.glEnable(2848);
                    GL11.glEnable(3042);
                    GL11.glBlendFunc(770, 771);
                    GL11.glColor4f(c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f, a.getValue().intValue() / 255.0f / 2.0f);
                    if (this.lines.getValue()) {
                        GL11.glLineWidth(width.getValue().intValue());
                        break;
                    }
                    break;
                }
                case POST: {
                    GL11.glPopAttrib();
                    GL11.glPolygonOffset(1.0f, 1.0E7f);
                    GL11.glDisable(32823);
                    GL11.glPopMatrix();
                    break;
                }
                default:
                    break;
            }
        }
    });
}