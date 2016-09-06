package com.hrkalk.zetapower.vessel;

import java.util.HashMap;
import java.util.Iterator;
import java.util.function.Supplier;

import com.hrkalk.zetapower.utils.NBTReader;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public abstract class IterableSpace {
    private static HashMap<String, Supplier<? extends IterableSpace>> spaces = new HashMap<>();

    static {
        spaces.put("CubeIterableSpace", CubeIterableSpace::new);
    }

    private BlockPos anchorPoint;

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setTag("anchorPoint", NBTReader.writeBLockPosWithPrefix(new NBTTagCompound(), anchorPoint, "anchor"));
        nbt.setString("className", getClassName());
        nbt.setTag("content", writeToNBT0(new NBTTagCompound()));
        return nbt;
    }

    public static IterableSpace readFromNBT(NBTTagCompound nbt) {
        String className = NBTReader.readStringOr(nbt, "className", "");
        IterableSpace instance = spaces.get(className).get();
        instance.readFromNBT0(nbt.getCompoundTag("content"));
        instance.anchorPoint = NBTReader.readBlockPosWithPrefix(nbt.getCompoundTag("anchorPoint"), "anchor");
        return instance;
    }

    public static IterableSpace createCubeSpace(BlockPos anchor, BlockPos from, BlockPos to) {
        IterableSpace space = new CubeIterableSpace(from, to);
        space.anchorPoint = anchor;
        return space;
    }

    public BlockPos getAnchorPoint() {
        return anchorPoint;
    }

    public abstract NBTTagCompound writeToNBT0(NBTTagCompound nbt);

    public abstract void readFromNBT0(NBTTagCompound nbt);

    public abstract Iterator<BlockPos> getIterator();

    public abstract String getClassName(); //coulkd be done otherwise, but I think this is better

    public static class CubeIterableSpace extends IterableSpace implements Iterator<BlockPos> {
        private BlockPos from = new BlockPos(0, 0, 0); //inclusive
        private BlockPos to = new BlockPos(0, 0, 0); //exclusive
        private BlockPos[] blocks;
        private int index; //for iteration

        public CubeIterableSpace() {

        }

        public CubeIterableSpace(BlockPos from, BlockPos to) {
            this.from = from;
            this.to = to;
            init();
        }

        @Override
        public NBTTagCompound writeToNBT0(NBTTagCompound nbt) {
            NBTReader.writeBLockPosWithPrefix(nbt, from, "from");
            NBTReader.writeBLockPosWithPrefix(nbt, to, "to");
            return nbt;
        }

        @Override
        public void readFromNBT0(NBTTagCompound nbt) {
            from = NBTReader.readBlockPosWithPrefix(nbt, "from");
            to = NBTReader.readBlockPosWithPrefix(nbt, "to");
            init();
        }

        private void init() {
            int xSize = to.getX() - from.getX();
            int ySize = to.getY() - from.getY();
            int zSize = to.getZ() - from.getZ();

            blocks = new BlockPos[xSize * ySize * zSize];

            for (int i = 0; i < xSize; i++) {
                for (int j = 0; j < ySize; j++) {
                    for (int k = 0; k < zSize; k++) {
                        blocks[(i * ySize + j) * zSize + k] = new BlockPos(from.getX() + i, from.getY() + j, from.getZ() + k);
                    }
                }
            }
        }

        @Override
        public Iterator<BlockPos> getIterator() {
            index = 0;
            return this;
        }

        @Override
        public boolean hasNext() {
            return index < blocks.length;
        }

        @Override
        public BlockPos next() {
            if (index >= blocks.length) {
                return null;
            }
            return blocks[index++];
        }

        @Override
        public String getClassName() {
            return "CubeIterableSpace";
        }

    }

}
