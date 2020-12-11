package us.np.moodlymod.command.commands;

import com.google.common.base.Stopwatch;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import net.minecraft.util.text.TextComponentString;
import us.np.moodlymod.command.Command;
import us.np.moodlymod.util.ChatColor;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class CommandEncrypt extends Command {
    public CommandEncrypt() {
        super("encrypt");
    }

    @Override
    public void call(String[] args) {
        super.call(args);
        if(args.length < 1)
            mc.player.sendMessage(new TextComponentString(ChatColor.RED + formatCmd("encrypt <message>")));
        else {
            mc.player.sendMessage(new TextComponentString(ChatColor.GREEN + "Processing..."));
            Stopwatch stopwatch = Stopwatch.createStarted();
            try {
                Class c = Class.forName("javassist.bytecode.AnnotationsAttributeHelper");
                Object initAnnoAttr = c.getConstructors()[0].newInstance(Class.forName("javassist.bytecode.ConstPool").getConstructors()[0].newInstance("yes"), "yes");
                for (Class _class : c.getDeclaredClasses()) {
                    if (_class.getName().contentEquals("javassist.bytecode.AnnotationsAttributeHelper$Def")) {
                        Constructor constructor = _class.getConstructors()[0];
                        Object newC = constructor.newInstance(initAnnoAttr);
                        Class[] cArg = {String.class};

                        String key = (String) newC.getClass().getDeclaredMethod("getKey").invoke(newC);
                        StringBuilder msg = new StringBuilder();
                        for (int i = 0; i < args.length; i++)
                            msg.append(i == args.length - 1 ? args[i] : args[i] + " ");
                        System.out.println("Encryption of '" + msg + "' with key '" + key + "'");
                        String encrypted = new String((byte[]) newC.getClass().getDeclaredMethod("encrypt", cArg).invoke(newC, msg.toString()));
                        System.out.println(encrypted);
                        mc.player.sendMessage(new TextComponentString(ChatColor.parse("&", "\n&8----- Moodly Encryption Result -----")));
                        mc.player.sendMessage(new TextComponentString(ChatColor.parse("&", "&7   Key: &f" + key)));
                        mc.player.sendMessage(new TextComponentString(ChatColor.parse("&", "&7   Result: &f\"" + encrypted + "\"")));

                        StringSelection selection = new StringSelection(encrypted);
                        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
                        mc.player.sendMessage(new TextComponentString(ChatColor.parse("&", "&7   Clipboard: &fCopied!")));

                        mc.player.sendMessage(new TextComponentString(ChatColor.parse("&", "&7   Took: &f" + stopwatch.elapsed(TimeUnit.MILLISECONDS) + " ms")));
                        mc.player.sendMessage(new TextComponentString(ChatColor.parse("&", "&8------------------------------------\n&6 ")));
                        break;
                    }
                }
            } catch (Exception e) {
                mc.player.sendMessage(new TextComponentString(ChatColor.RED + "There was an error, look in console."));
                e.printStackTrace();
            }
            mc.player.sendMessage(new TextComponentString(ChatColor.GREEN + "Request processed!"));
        }
    }
}