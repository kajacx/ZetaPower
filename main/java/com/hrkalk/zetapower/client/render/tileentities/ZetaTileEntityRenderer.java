package com.hrkalk.zetapower.client.render.tileentities;

import com.hrkalk.zetapower.tileentities.ZetaChest;
import com.hrkalk.zetapower.utils.loader.ReflectUtil;
import com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.DynamicReloader;
import com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.ReloadEveryNTicks;
import com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.ReloadOnChange;

import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ZetaTileEntityRenderer extends TileEntitySpecialRenderer<ZetaChest> {

    private DynamicReloader reloader = new DynamicReloader(ZetaTileEntityRenderer.class, "../bin");

    {
        reloader.reloadWhen.add(new ReloadOnChange(com.hrkalk.zetapower.client.render.tileentities.ZetaTileEntityRenderer.class, "../bin"));
        reloader.reloadWhen.add(new ReloadEveryNTicks(20));

        reloader.addToBlacklist("com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.ReloadOnChange");
        reloader.addToBlacklist("net.minecraft.world.World");
        reloader.addToBlacklist("net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer");
        reloader.addToBlacklist("com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.DynamicReloader");
        reloader.addToBlacklist("com.hrkalk.zetapower.tileentities.ZetaChest");
        reloader.addToBlacklist("net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher");
        reloader.addToBlacklist("com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.ReloadTrigger");
        reloader.addToBlacklist("com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.ReloadEveryNTicks");
        reloader.addToBlacklist("com.hrkalk.zetapower.client.render.tileentities.ZetaTileEntityRenderer");
        reloader.addToBlacklist("net.minecraft.util.ResourceLocation");
        reloader.addToBlacklist("com.hrkalk.zetapower.utils.loader.ReflectUtil");
        reloader.addToBlacklist("net.minecraft.tileentity.TileEntity");
        reloader.addToBlacklist("java.lang.String");
        reloader.addToBlacklist("net.minecraft.util.ResourceLocation[]");

        reloader.addToBlacklistPrefix("net.minecraft");
        reloader.addToBlacklistPrefix("net.minecraftforge");
    }

    public ZetaTileEntityRenderer() {
        super();
    }

    public ResourceLocation[] get_DESTROY_STAGES() {
        return DESTROY_STAGES;
    }

    public TileEntityRendererDispatcher get_rendererDispatcher() {
        return rendererDispatcher;
    }

    public void set_rendererDispatcher(TileEntityRendererDispatcher rendererDispatcher) {
        this.rendererDispatcher = rendererDispatcher;
    }

    public void call_func_190052_a(ZetaChest arg1, String arg2, double arg3, double arg4, double arg5, int arg6) {
        func_190052_a(arg1, arg2, arg3, arg4, arg5, arg6);
    }

    public void call_func_190053_a(boolean arg1) {
        func_190053_a(arg1);
    }

    public void call_bindTexture(ResourceLocation arg1) {
        bindTexture(arg1);
    }

    public World call_getWorld() {
        return getWorld();
    }

    @Override
    public void renderTileEntityAt(ZetaChest arg1, double arg2, double arg3, double arg4, float arg5, int arg6) {
        try {
            ReflectUtil.invoke("renderTileEntityAt", reloader.getInstance(this), arg1, arg2, arg3, arg4, arg5, arg6);
        } catch (Throwable t) {
            System.out.println("Exception while executing reloadable code.");
            t.printStackTrace(System.out);
            //Thanks for using the Zeta Power Reloadable class generator.
        }
    }

}
