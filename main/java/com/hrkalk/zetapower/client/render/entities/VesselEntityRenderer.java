package com.hrkalk.zetapower.client.render.entities;

import com.hrkalk.zetapower.utils.loader.ReflectUtil;
import com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.DynamicReloader;
import com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.ReloadEveryNTicks;
import com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.ReloadOnChange;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class VesselEntityRenderer extends Render {

    private DynamicReloader reloader = new DynamicReloader(VesselEntityRenderer.class, "../bin");

    {
        reloader.reloadWhen.add(new ReloadOnChange(com.hrkalk.zetapower.client.render.entities.VesselEntityRenderer.class, "../bin"));
        reloader.reloadWhen.add(new ReloadEveryNTicks(200));

        reloader.addToBlacklist("com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.ReloadTrigger");
        reloader.addToBlacklist("net.minecraft.entity.Entity");
        reloader.addToBlacklist("com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.ReloadEveryNTicks");
        reloader.addToBlacklist("net.minecraft.client.renderer.entity.RenderManager");
        reloader.addToBlacklist("com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.ReloadOnChange");
        reloader.addToBlacklist("com.hrkalk.zetapower.client.render.entities.RideableShipRenderer");
        reloader.addToBlacklist("net.minecraft.client.renderer.entity.Render");
        reloader.addToBlacklist("net.minecraft.util.ResourceLocation");
        reloader.addToBlacklist("com.hrkalk.zetapower.utils.loader.ReflectUtil");
        reloader.addToBlacklist("com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.DynamicReloader");
        reloader.addToBlacklist("com.hrkalk.zetapower.client.render.entities.VesselEntityRenderer");
        reloader.addToBlacklist("com.hrkalk.zetapower.entities.VesselEntity");

        reloader.addToBlacklistPrefix("java");
        reloader.addToBlacklistPrefix("net.minecraft");
        reloader.addToBlacklistPrefix("net.minecraftforge");
        reloader.addToBlacklistPrefix("com.hrkalk.zetapower.dimension");
        reloader.addToBlacklistPrefix("com.hrkalk.zetapower.vessel");
    }

    public VesselEntityRenderer(RenderManager arg1) {
        super(arg1);
    }

    public RenderManager get_renderManager() {
        return renderManager;
    }

    public float get_shadowSize() {
        return shadowSize;
    }

    public void set_shadowSize(float shadowSize) {
        this.shadowSize = shadowSize;
    }

    public float get_shadowOpaque() {
        return shadowOpaque;
    }

    public void set_shadowOpaque(float shadowOpaque) {
        this.shadowOpaque = shadowOpaque;
    }

    public boolean get_renderOutlines() {
        return renderOutlines;
    }

    public void set_renderOutlines(boolean renderOutlines) {
        this.renderOutlines = renderOutlines;
    }

    public ResourceLocation call_getEntityTexture(Entity arg1) {
        return getEntityTexture(arg1);
    }

    public boolean call_bindEntityTexture(Entity arg1) {
        return bindEntityTexture(arg1);
    }

    public void call_renderLivingLabel(Entity arg1, String arg2, double arg3, double arg4, double arg5, int arg6) {
        renderLivingLabel(arg1, arg2, arg3, arg4, arg5, arg6);
    }

    public boolean call_canRenderName(Entity arg1) {
        return canRenderName(arg1);
    }

    public void call_renderEntityName(Entity arg1, double arg2, double arg3, double arg4, String arg5, double arg6) {
        renderEntityName(arg1, arg2, arg3, arg4, arg5, arg6);
    }

    public int call_getTeamColor(Entity arg1) {
        return getTeamColor(arg1);
    }

    public void call_renderName(Entity arg1, double arg2, double arg3, double arg4) {
        renderName(arg1, arg2, arg3, arg4);
    }

    @Override
    public ResourceLocation getEntityTexture(Entity arg1) {
        try {
            return (ResourceLocation) ReflectUtil.invoke("getEntityTexture", reloader.getInstance(this), arg1);
        } catch (Throwable t) {
            System.out.println("Exception while executing reloadable code.");
            t.printStackTrace(System.out);
            return null;
        }
    }

    private VesselEntityRenderer_Reload renderer = new VesselEntityRenderer_Reload();

    @Override
    public void doRender(Entity arg1, double arg2, double arg3, double arg4, float arg5, float arg6) {
        //renderer.thiz = this;
        //renderer.doRender(arg1, arg2, arg3, arg4, arg5, arg6);
        try {
            ReflectUtil.invoke("doRender", reloader.getInstance(this), arg1, arg2, arg3, arg4, arg5, arg6);
        } catch (Throwable t) {
            System.out.println("Exception while executing reloadable code.");
            t.printStackTrace(System.out);
            //Thanks for using the Zeta Power Reloadable class generator.
        } //*/
    }

}
