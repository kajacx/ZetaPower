package com.hrkalk.zetapower.dimension;

import java.util.ArrayList;

import com.hrkalk.zetapower.utils.L;

public class ChunksAllocator {

    private ArrayList<RowAllocator> rows = new ArrayList<>();

    private RowAllocator getRowAllocator(int zSize) {
        for (int i = rows.size(); i < zSize; i++) {
            rows.add(new RowAllocator(i + 1));
        }
        return rows.get(zSize - 1);
    }

    public AllocatedSpace allocate(int xSize, int zSize) {
        if (xSize <= 0) {
            L.w("Allocating xSize 1 instead of " + xSize);
            xSize = 1;
        }
        if (zSize <= 0) {
            L.w("Allocating xSize 1 instead of " + zSize);
            zSize = 1;
        }
        return getRowAllocator(zSize).allocate(xSize);
    }

    public AllocatedSpace allocate12(int xSize, int zSize) {
        return allocate((xSize + 4 + 15) / 16, (xSize + 4 + 15) / 16); // don't ask...
    }

    public boolean unallocate(AllocatedSpace space) {
        if (space == null || space.isUnloaded()) {
            return false;
        } else {
            getRowAllocator(space.zSize).unallocate(space);
            return true;
        }
    }

    /**
     * call this when reconstructing saved space in NBT, DON'T allocate new space instead!
     * @param xs x coordinate of the desired chunk
     * @param zs z coordinate of the desired chunk
     * @return AllocatedSpace
     */
    public AllocatedSpace getSavedSpace(int xPos, int zSize) {
        AllocatedSpace ret = getRowAllocator(zSize).getAllocatedAt(xPos);
        if (ret == null) {
            //either the params are wrong or I was lazy to program this
            return allocate(zSize, zSize);
        }
        return ret;
    }

    private static class RowAllocator {
        private int zSize;
        private ArrayList<AllocatedSpace> allocated = new ArrayList<>();

        public RowAllocator(int zSize) {
            this.zSize = zSize;
        }

        protected AllocatedSpace allocate(int xSize) {
            int prevX = 0;
            for (int i = 0; i <= allocated.size(); i++) {
                if (i == allocated.size() || allocated.get(i).getX() - prevX >= xSize) {
                    AllocatedSpace newSpace = allocate(prevX, xSize);
                    allocated.add(i, newSpace);
                    return newSpace;
                } else {
                    prevX = allocated.get(i).getX() + allocated.get(i).getXSize();
                }
            }
            L.e("Allocation failed where it shouldn't");
            return null;
        }

        public AllocatedSpace getAllocatedAt(int xs) {
            for (int i = 0; i < allocated.size(); i++) {
                if (allocated.get(i).x == xs) {
                    return allocated.get(i);
                }
            }
            L.w("Trying to get allocated space that doesn't exist!");
            return null;
        }

        protected void unallocate(AllocatedSpace space) {
            allocated.remove(space);
            space.unload();
        }

        private AllocatedSpace allocate(int x, int xSize) {
            int z = zSize * (zSize - 1) / 2;
            return new AllocatedSpace(x, z, xSize, zSize);
        }
    }

    public static class AllocatedSpace {
        private int x;
        private int z;
        private int xSize;
        private int zSize;
        private boolean unloaded;

        public AllocatedSpace(int x, int z, int xSize, int zSize) {
            this.x = x;
            this.z = z;
            this.xSize = xSize;
            this.zSize = zSize;
        }

        public int getX() {
            return x;
        }

        public int getZ() {
            return z;
        }

        public int getXSize() {
            return xSize;
        }

        public int getZSize() {
            return zSize;
        }

        public int getX16() {
            return x * 16;
        }

        public int getZ16() {
            return z * 16;
        }

        public int getXSize16() {
            return xSize * 16;
        }

        public int getZSize16() {
            return zSize * 16;
        }

        public int getX12() {
            return getX16() + 2;
        }

        public int getZ12() {
            return getZ16() + 2;
        }

        public int getXSize12() {
            return getXSize16() - 4;
        }

        public int getZSize12() {
            return getZSize16() - 4;
        }

        public boolean isUnloaded() {
            return unloaded;
        }

        protected void unload() {
            unloaded = true;
        }
    }
}
