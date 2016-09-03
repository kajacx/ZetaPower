package com.hrkalk.zetapower.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class NbtNetworkMessage implements IMessage {

    private int x, y, z;
    private int facing;

    public NbtNetworkMessage(int x, int y, int z, int facing) {
        this.facing = facing;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public int getFacing() {
        return facing;
    }

    public void setFacing(int facing) {
        this.facing = facing;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.getInt(0);
        y = buf.getInt(1);
        z = buf.getInt(2);
        facing = buf.getInt(3);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.setInt(0, x);
        buf.setInt(1, y);
        buf.setInt(2, z);
        buf.setInt(3, facing);
    }

}
