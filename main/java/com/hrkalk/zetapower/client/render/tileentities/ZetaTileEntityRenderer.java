package com.hrkalk.zetapower.client.render.tileentities;

import java.io.File;

import com.hrkalk.zetapower.tileentities.ModTileEntity;
import com.hrkalk.zetapower.utils.DynamicClassLoader;
import com.hrkalk.zetapower.utils.Log;
import com.hrkalk.zetapower.utils.loader.ReflectUtil;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

public class ZetaTileEntityRenderer extends TileEntitySpecialRenderer<ModTileEntity> {

    private static TileEntitySpecialRenderer<ModTileEntity> instance = null;
    private static long lastLoaded = 0;
    private static File file = new File("../bin/com/hrkalk/zetapower/client/render/tileentities/ZetaTileEntityRendererReload.class");

    @SuppressWarnings("unchecked")
    @Override
    public void renderTileEntityAt(ModTileEntity te, double x, double y, double z, float partialTicks, int destroyStage) {
        try {
            if (file.lastModified() > lastLoaded) {
                lastLoaded = file.lastModified();

                Class<?> clazz = loadClass();

                Object i = ReflectUtil.newInstance(clazz);

                instance = (TileEntitySpecialRenderer<ModTileEntity>) i;
            }

            //instance.renderTileEntityAt(te, x, y, z, partialTicks, destroyStage);
            ReflectUtil.invoke("renderTileEntityAt2", instance, x, y, z, partialTicks, destroyStage);
        } catch (Exception e) {
            Log.error("Error in rendering via dynamic classload");
            e.printStackTrace(System.out);
        }

    }

    private Class<?> loadClass() {
        return new DynamicClassLoader("../bin")
                .load("com.hrkalk.zetapower.client.render.tileentities.ZetaTileEntityRendererReload");
    }

}
