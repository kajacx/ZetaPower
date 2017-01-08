package com.hrkalk.zetapower.client.render.vessel;

import com.hrkalk.zetapower.utils.loader.ReflectUtil;
import com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.DynamicReloader;
import com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.ReloadEveryNTicks;
import com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.ReloadOnChange;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.chunk.ChunkCompileTaskGenerator;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.World;

public class FakeRenderChunk extends RenderChunk {

    private DynamicReloader reloader = new DynamicReloader(FakeRenderChunk.class, "../bin");

    {
        reloader.reloadWhen.add(new ReloadOnChange(com.hrkalk.zetapower.client.render.vessel.FakeRenderChunk.class, "../bin"));
        reloader.reloadWhen.add(new ReloadEveryNTicks(200));

        reloader.addToBlacklist("com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.ReloadTrigger");
        reloader.addToBlacklist("com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.ReloadEveryNTicks");
        reloader.addToBlacklist("com.hrkalk.zetapower.client.render.vessel.FakeRenderChunk");
        reloader.addToBlacklist("net.minecraft.world.ChunkCache");
        reloader.addToBlacklist("com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.ReloadOnChange");
        reloader.addToBlacklist("net.minecraft.world.World");
        reloader.addToBlacklist("net.minecraft.client.renderer.chunk.ChunkCompileTaskGenerator");
        reloader.addToBlacklist("com.hrkalk.zetapower.utils.loader.ReflectUtil");
        reloader.addToBlacklist("net.minecraft.client.renderer.chunk.RenderChunk");
        reloader.addToBlacklist("com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.DynamicReloader");
        reloader.addToBlacklist("net.minecraft.util.math.BlockPos");

        reloader.addToBlacklistPrefix("net.minecraft");
        reloader.addToBlacklistPrefix("net.minecraftforge");
        reloader.addToBlacklistPrefix("com.hrkalk.zetapower.dimension");
    }

    public Iterable<BlockPos> iter;

    public FakeRenderChunk(World world, RenderGlobal renderGlobal, int index, Iterable<BlockPos> iter) {
        super(world, renderGlobal, index);

        this.iter = iter;
    }

    public ChunkCache call_createRegionRenderCache(World arg1, BlockPos arg2, BlockPos arg3, int arg4) {
        return createRegionRenderCache(arg1, arg2, arg3, arg4);
    }

    public void call_finishCompileTask() {
        finishCompileTask();
    }

    public double call_getDistanceSq() {
        return getDistanceSq();
    }

    public void super_rebuildChunk(float arg1, float arg2, float arg3, ChunkCompileTaskGenerator arg4) {
        super.rebuildChunk(arg1, arg2, arg3, arg4);
    }

    @Override
    public void rebuildChunk(float arg1, float arg2, float arg3, ChunkCompileTaskGenerator arg4) {
        try {
            ReflectUtil.invoke("rebuildChunk", reloader.getInstance(this), arg1, arg2, arg3, arg4);
        } catch (Throwable t) {
            System.out.println("Exception while executing reloadable code.");
            t.printStackTrace(System.out);
            //Thanks for using the Zeta Power Reloadable class generator.
        }
    }

}
