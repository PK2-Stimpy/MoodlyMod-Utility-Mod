package us.np.moodlymod.module.modules.render;

import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.potion.PotionEffect;
import us.np.moodlymod.event.custom.CustomEvent;
import us.np.moodlymod.event.custom.entityplayersp.PlayerUpdateEvent;
import us.np.moodlymod.event.custom.networkmanager.NetworkPacketEvent;
import us.np.moodlymod.module.Module;
import us.np.moodlymod.module.ModuleType;
import us.np.moodlymod.module.option.BetterMode;
import us.np.moodlymod.module.option.OptionBetterMode;
import us.np.moodlymod.module.option.OptionBoolean;

import java.awt.*;

public class FullbrightModule extends Module {
    public static final OptionBetterMode mode = new OptionBetterMode("Mode", 0, BetterMode.construct("GAMMA", "POTION"));
    public static final OptionBoolean effects = new OptionBoolean("Effects", false);

    private float previousSetting;

    public FullbrightModule() {
        super("FullBright", null, "NONE", Color.CYAN, ModuleType.RENDER);
        addOption(mode);
        addOption(effects);
        endOption();
    }

    @Override
    public void onEnable() {
        super.onEnable();

        this.previousSetting = FullbrightModule.mc.gameSettings.gammaSetting;
    }

    @Override
    public void onDisable() {
        super.onDisable();

        if(mode.getMode() == 1) /* POTION */
            FullbrightModule.mc.player.removePotionEffect(MobEffects.NIGHT_VISION);
        FullbrightModule.mc.gameSettings.gammaSetting = this.previousSetting;
    }

    @EventHandler
    private Listener<PlayerUpdateEvent> updateEventListener = new Listener<>(event -> {
        if(mode.getMode() == 0) /* GAMMA */
            FullbrightModule.mc.gameSettings.gammaSetting = 1000.0f;
        if(mode.getMode() == 1) /* POTION */
            FullbrightModule.mc.player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 5210));
    });

    @EventHandler
    private Listener<NetworkPacketEvent> packetEventListener = new Listener<>(event -> {
        if (event.getEra() == CustomEvent.Era.PRE && event.getPacket() instanceof SPacketEntityEffect && this.effects.getValue()) {
            final SPacketEntityEffect packet = (SPacketEntityEffect) event.getPacket();
            if (FullbrightModule.mc.player != null && packet.getEntityId() == FullbrightModule.mc.player.getEntityId() && (packet.getEffectId() == 9 || packet.getEffectId() == 15))
                event.cancel();
        }
    });
}