package us.np.moodlymod.module.modules.render;

import io.netty.util.internal.ConcurrentSet;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.apache.logging.log4j.util.LambdaUtil;
import us.np.moodlymod.event.custom.render.RenderEvent;
import us.np.moodlymod.module.Module;
import us.np.moodlymod.module.ModuleType;
import us.np.moodlymod.module.option.BetterMode;
import us.np.moodlymod.module.option.OptionBetterMode;
import us.np.moodlymod.module.option.OptionBoolean;
import us.np.moodlymod.module.option.OptionDouble;
import us.np.moodlymod.util.MathUtil;
import us.np.moodlymod.util.MoodlyTessellator;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class BreakESPModule extends Module {
    public static final OptionBoolean ignoreSelf = new OptionBoolean("Ignore self", true);
    public static final OptionBoolean onlyObby = new OptionBoolean("Only obsidian", true);
    public static final OptionBoolean fade = new OptionBoolean("Fade", true);
    public static final OptionDouble red = new OptionDouble("Red", 255D, 0D, 255D);
    public static final OptionDouble green = new OptionDouble("Green", 0D, 0D, 255D);
    public static final OptionDouble blue = new OptionDouble("Blue", 0D, 0D, 255D);
    public static final OptionDouble alpha = new OptionDouble("Alpha", 70D, 0D, 255D);
    public static final OptionDouble alphaF = new OptionDouble("Full Alpha", 100D, 0D, 255D);

    public ConcurrentSet<BlockPos> breaking;
    private ConcurrentSet<BlockPos> test;
    private Map<Integer, Integer> alphaMap;
    BlockPos pos;

    public BreakESPModule() {
        super("BreakESP", null, "NONE", Color.CYAN, ModuleType.RENDER);
        addOption(ignoreSelf);
        addOption(onlyObby);
        addOption(fade);
        addOption(red);
        addOption(green);
        addOption(blue);
        addOption(alpha);
        addOption(alphaF);
        endOption();

        this.breaking = (ConcurrentSet<BlockPos>)new ConcurrentSet();
        this.test = (ConcurrentSet<BlockPos>)new ConcurrentSet();

        this.alphaMap = new HashMap<Integer, Integer>();
        this.alphaMap.put(0, 28);
        this.alphaMap.put(1, 56);
        this.alphaMap.put(2, 84);
        this.alphaMap.put(3, 112);
        this.alphaMap.put(4, 140);
        this.alphaMap.put(5, 168);
        this.alphaMap.put(6, 196);
        this.alphaMap.put(7, 224);
        this.alphaMap.put(8, 255);
        this.alphaMap.put(9, 255);
    }

    int transparency;
    IBlockState iBlockState3;
    Vec3d interp3;
    IBlockState iBlockState4;
    Vec3d interp4;
    @EventHandler
    private Listener<RenderEvent> renderEventListener = new Listener<>(event -> {
        BreakESPModule.mc.renderGlobal.damagedBlocks.forEach((integer, destroyBlockProgress) -> {
            if (destroyBlockProgress != null) {
                if (!this.ignoreSelf.getValue() || BreakESPModule.mc.world.getEntityByID((int)integer) != BreakESPModule.mc.player) {
                    if (!this.onlyObby.getValue() || BreakESPModule.mc.world.getBlockState(destroyBlockProgress.getPosition()).getBlock() == Blocks.OBSIDIAN) {
                        transparency = alpha.getValue().intValue();

                        iBlockState3 = BreakESPModule.mc.world.getBlockState(destroyBlockProgress.getPosition());
                        interp3 = MathUtil.interpolateEntity((Entity) BreakESPModule.mc.player, BreakESPModule.mc.getRenderPartialTicks());
                        MoodlyTessellator.drawFullBox(iBlockState3.getSelectedBoundingBox((World) BreakESPModule.mc.world, destroyBlockProgress.getPosition()).grow(0.0020000000949949026).offset(-interp3.x, -interp3.y, -interp3.z), destroyBlockProgress.getPosition(), 1.5f, this.red.getValue().intValue(), this.green.getValue().intValue(), this.blue.getValue().intValue(), transparency, this.alphaF.getValue().intValue());
                    }
                }
            }
        });
    });
}