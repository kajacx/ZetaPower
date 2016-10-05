package com.hrkalk.zetapower.vessel;

import java.util.Iterator;

import com.hrkalk.zetapower.dimension.ChunksAllocator.AllocatedSpace;
import com.hrkalk.zetapower.dimension.ZetaDimensionHandler;
import com.hrkalk.zetapower.utils.L;
import com.hrkalk.zetapower.utils.NBTReader;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class BlockCluster implements Iterable<BlockPos> {

    private World world;
    private BlockPos from;
    private BlockPos to;
    private Vec3d anchor;
    private AllocatedSpace space;

    public BlockCluster(World world, NBTTagCompound tag) {
        this.world = world;
        readFromNBT(tag);
        iterator = new BlockIterator(from, to);
    }

    public BlockCluster(World world, BlockPos from, BlockPos to) {
        this(world, from, to, null, null);
    }

    public BlockCluster(World world, BlockPos from, BlockPos to, AllocatedSpace space) {
        this(world, from, to, null, space);
    }

    public BlockCluster(World world, BlockPos from, BlockPos to, Vec3d anchor) {
        this(world, from, to, anchor, null);
    }

    public BlockCluster(World world, BlockPos from, BlockPos to, Vec3d anchor, AllocatedSpace space) {
        this.world = world;
        this.from = from;
        this.to = to;
        setAnchor(anchor);
        this.space = space;
        iterator = new BlockIterator(from, to);
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public BlockPos getFrom() {
        return from;
    }

    public void setFrom(BlockPos from) {
        this.from = from;
    }

    public BlockPos getTo() {
        return to;
    }

    public void setTo(BlockPos to) {
        this.to = to;
    }

    public Vec3d getAnchor() {
        return anchor;
    }

    public void setAnchor(Vec3d anchor) {
        if (anchor == null) {
            double x = (from.getX() + to.getX()) / 2d;
            double y = (from.getY() + to.getY()) / 2d;
            double z = (from.getZ() + to.getZ()) / 2d;
            this.anchor = new Vec3d(x, y, z);
        } else {
            this.anchor = anchor;
        }
    }

    public AllocatedSpace getSpace() {
        return space;
    }

    public void setSpace(AllocatedSpace space) {
        this.space = space;
    }

    private BlockIterator iterator;

    @Override
    public Iterator<BlockPos> iterator() {
        if (iterator.active) {
            L.w("Possibly iterating over active iterator. This is not good.");
        }
        iterator.active = true;
        iterator.index = 0;
        return iterator;
    }

    private static class BlockIterator implements Iterator<BlockPos> {
        private boolean active = false;
        private BlockPos[] blocks;
        private int index;

        private BlockIterator(BlockPos from, BlockPos to) {
            int sizeX = to.getX() - from.getX();
            int sizeY = to.getY() - from.getY();
            int sizeZ = to.getZ() - from.getZ();
            blocks = new BlockPos[sizeX * sizeY * sizeZ];
            for (int x = 0; x < sizeX; x++) {
                for (int y = 0; y < sizeY; y++) {
                    for (int z = 0; z < sizeZ; z++) {
                        blocks[(x * sizeY + y) * sizeZ + z] = new BlockPos(from.getX() + x, from.getY() + y, from.getZ() + z);
                    }
                }
            }
        }

        @Override
        public boolean hasNext() {
            return active &= index < blocks.length;
        }

        @Override
        public BlockPos next() {
            if (index >= blocks.length) {
                active = false;
                return null;
            }
            return blocks[index++];
        }

    }

    public NBTTagCompound saveToNBT(NBTTagCompound tag) {
        NBTReader.writeBLockPosWithPrefix(tag, from, "from");
        NBTReader.writeBLockPosWithPrefix(tag, to, "to");
        tag.setDouble("anchorX", anchor.xCoord);
        tag.setDouble("anchorY", anchor.yCoord);
        tag.setDouble("anchorZ", anchor.zCoord);
        if (space != null) {
            tag.setInteger("spaceXPos", space.getX()); //Musk FTW
            tag.setInteger("spaceZSize", space.getZ());
        }
        return tag;
    }

    public NBTTagCompound readFromNBT(NBTTagCompound tag) {
        from = NBTReader.readBlockPosWithPrefix(tag, "from");
        to = NBTReader.readBlockPosWithPrefix(tag, "to");
        double x = tag.getDouble("anchorX");
        double y = tag.getDouble("anchorY");
        double z = tag.getDouble("anchorZ");
        anchor = new Vec3d(x, y, z);
        if (tag.hasKey("spaceX", NBTReader.TYPE_INT)) {
            int xPos = tag.getInteger("spaceXpos");
            int zSize = tag.getInteger("spaceZSize");
            space = ZetaDimensionHandler.mallocDimension.allocator.getSavedSpace(xPos, zSize);
        }
        return tag;
    }
}
