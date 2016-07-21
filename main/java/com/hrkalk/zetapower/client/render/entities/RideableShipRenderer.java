package com.hrkalk.zetapower.client.render.entities;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import com.hrkalk.zetapower.entities.RideableShip;
import com.hrkalk.zetapower.utils.L;
import com.hrkalk.zetapower.utils.loader.FilteredClassLoader;
import com.hrkalk.zetapower.utils.loader.ReflectUtil;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RideableShipRenderer extends Render<RideableShip> {

    private String classname = "com.hrkalk.zetapower.client.render.entities.RideableShipRendererReload";
    private Object instance = null;
    private long lastLoaded = 0;
    private File file = new File("../bin/" + classname.replace('.', '/') + ".class");

    public RideableShipRenderer(RenderManager rendermanagerIn) {
        super(rendermanagerIn);
    }

    @Override
    public ResourceLocation getEntityTexture(RideableShip entity) {
        L.d("get texture");
        reloadIfNeeded();
        ResourceLocation ret = null;
        try {
            ret = (ResourceLocation) ReflectUtil.invoke("getEntityTexture", instance, this, entity);
        } catch (Throwable e) {
            L.e("Error in rendering via dynamic classload");
            e.printStackTrace(System.out);
        }
        return ret;
    }

    @Override
    public void doRender(RideableShip entity, double x, double y, double z, float entityYaw, float partialTicks) {
        L.d("do render");
        reloadIfNeeded();
        try {
            ReflectUtil.invoke("doRender", instance, this, entity, x, y, z, entityYaw, partialTicks);
        } catch (Throwable e) {
            L.e("Error in rendering via dynamic classload");
            e.printStackTrace(System.out);
        }
    }

    private void reloadIfNeeded() {
        try {
            if (file.lastModified() > lastLoaded || true) {

                lastLoaded = file.lastModified();

                Class<?> clazz = loadClass();

                instance = ReflectUtil.newInstance(clazz);
            }

        } catch (Throwable e) {
            L.e("Error in rendering via dynamic classload");
            e.printStackTrace(System.out);
        } //*/
    }

    private List<String> whitelist = Arrays.asList("RideableShipRendererReload", "zetapower.utils");
    //private List<String> blacklist = Arrays.asList("RideableShipReload", "zetapower.utils");

    private Class<?> loadClass() {
        FilteredClassLoader classLoader = new FilteredClassLoader((className) -> whitelist.stream().anyMatch(whitelisted -> {
            return className.contains(whitelisted);
        }), "../bin");
        Class<?> contextClass = classLoader.load(classname);
        return contextClass;
        //return new DynamicClassLoader("../bin").load("com.hrkalk.zetapower.entities.RideableShipReload");
    }
}
