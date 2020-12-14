package us.np.moodlymod.event;

import static us.np.moodlymod.MoodlyMod.*;
import static us.np.moodlymod.command.CommandReturnStatus.*;

import com.maxwell.kmeth.utilities.Wrapper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import us.np.moodlymod.MoodlyMod;
import us.np.moodlymod.command.CommandManager;
import us.np.moodlymod.command.CommandReturnStatus;
import us.np.moodlymod.event.custom.entityplayer.PlayerDisconnectEvent;
import us.np.moodlymod.event.custom.render.RenderEvent;
import us.np.moodlymod.module.Module;

public class EventProcessor {
    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (event.isCanceled()) return;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.disableDepth();

        GlStateManager.glLineWidth(1f);
        EVENT_BUS.post(new RenderEvent(event.getPartialTicks()));
        GlStateManager.glLineWidth(1f);

        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.enableCull();
    }

    @SubscribeEvent
    public void keyInput(InputEvent.KeyInputEvent event) {
        MoodlyMod.EVENT_BUS.post(event);

        boolean eventKeyState = Keyboard.getEventKeyState();
        int eventKey = Keyboard.getEventKey();
        if (Wrapper.getMinecraft().player != null) {
            if (!eventKeyState) return;
            for (Module module : MoodlyMod.moduleManager.getModules()) {
                int key = Keyboard.getKeyIndex(module.key);
                if (key == 0) continue;
                if (key == eventKey)
                    module.toggle(true);
            }
        }
    }

    @SubscribeEvent
    public void onChat(ClientChatEvent event) {
        MoodlyMod.EVENT_BUS.post(event);

        CommandReturnStatus returnStatus = MoodlyMod.commandManager.gotMessage(event.getMessage());
        if(returnStatus != COMMAND_INVALID_SYNTAX) event.setCanceled(true);
        if(returnStatus == COMMAND_INVALID) Wrapper.getMinecraft().player.sendMessage(new TextComponentString("\u00a7cInvalid command, do " + CommandManager.prefix + "help"));
        if(returnStatus == COMMAND_ERROR) Wrapper.getMinecraft().player.sendMessage(new TextComponentString("\u00a7cThere was an error executing the command."));
    }

    @SubscribeEvent public void onClientTickEvent(TickEvent.ClientTickEvent event) { EVENT_BUS.post(event); }
    @SubscribeEvent public void onRenderGameOverlayTextEvent(RenderGameOverlayEvent.Text event) { EVENT_BUS.post(event); }
    @SubscribeEvent public void onRenderWorldLast(RenderWorldLastEvent event) { EVENT_BUS.post(event); }
    @SubscribeEvent public void onPlace(BlockEvent.PlaceEvent event) { EVENT_BUS.post(event); }
    @SubscribeEvent public void onPlayerLogOut(PlayerEvent.PlayerLoggedOutEvent event) { EVENT_BUS.post(event); }
    @SubscribeEvent public void onQuitClientServer(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) { EVENT_BUS.post(event); EVENT_BUS.post(new PlayerDisconnectEvent());}
    @SubscribeEvent public void onQuitServerClient(FMLNetworkEvent.ServerDisconnectionFromClientEvent event) { EVENT_BUS.post(event); EVENT_BUS.post(new PlayerDisconnectEvent());}
    @SubscribeEvent public void onPlayerTick(TickEvent.PlayerTickEvent event) { EVENT_BUS.post(event); }
    @SubscribeEvent public void onEntityJoinWorldEvent(EntityJoinWorldEvent event) { EVENT_BUS.post(event); }
}