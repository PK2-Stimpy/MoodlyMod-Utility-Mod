package us.np.moodlymod.event.custom.world;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import us.np.moodlymod.event.custom.CustomEvent;

public class WorldSetBlockStateEvent extends CustomEvent {
    public BlockPos pos;
    public IBlockState newState;
    public int flags;

    public WorldSetBlockStateEvent(BlockPos pos, IBlockState newState, int flags) {
        super();
        this.pos = pos;
        this.newState = newState;
        this.flags = flags;
    }
}