package us.np.moodlymod.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import us.np.moodlymod.Wrapper;

import java.util.LinkedList;

public class BlockUtils {
    public static LinkedList<BlockPos> findBlocksNearEntity(EntityLivingBase entity, int blockId, int blockMeta, int distance) {
        LinkedList<BlockPos> blocks = new LinkedList<BlockPos>();

        for (int x = (int) Wrapper.getMC().player.posX - distance; x <= (int) Wrapper.getMC().player.posX + distance; ++x) {
            for (int z = (int) Wrapper.getMC().player.posZ - distance; z <= (int) Wrapper.getMC().player.posZ + distance; ++z) {

                int height = Wrapper.getMC().world.getHeight(x, z);
                block: for (int y = 0; y <= height; ++y) {

                    BlockPos blockPos = new BlockPos(x, y, z);
                    IBlockState blockState = Wrapper.getMC().world.getBlockState(blockPos);

                    if(blockId == -1 || blockMeta == -1) {
                        blocks.add(blockPos);
                        continue block;
                    }

                    int id = Block.getIdFromBlock(blockState.getBlock());
                    int meta =  blockState.getBlock().getMetaFromState(blockState);

                    if(id == blockId && meta == blockMeta) {

                        blocks.add(blockPos);
                        continue block;
                    }

                }
            }
        }
        return blocks;
    }
}
