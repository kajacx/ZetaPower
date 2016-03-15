package com.hrkalk.zetapower.client.render.tileentities;

import java.io.File;

import com.hrkalk.zetapower.tileentities.ZetaChest;
import com.hrkalk.zetapower.utils.L;
import com.hrkalk.zetapower.utils.loader.DynamicClassLoader;
import com.hrkalk.zetapower.utils.loader.ReflectUtil;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

public class ZetaTileEntityRenderer extends TileEntitySpecialRenderer<ZetaChest> {

    private static TileEntitySpecialRenderer<ZetaChest> instance = null;
    private static long lastLoaded = 0;
    private static File file = new File("../bin/com/hrkalk/zetapower/client/render/tileentities/ZetaTileEntityRendererReload.class");

    @SuppressWarnings("unchecked")
    @Override
    public void renderTileEntityAt(ZetaChest te, double x, double y, double z, float partialTicks, int destroyStage) {
        try {
            if (file.lastModified() > lastLoaded) {
                lastLoaded = file.lastModified();

                Class<?> clazz = loadClass();

                Object i = ReflectUtil.newInstance(clazz);

                instance = (TileEntitySpecialRenderer<ZetaChest>) i;
            }

            //instance.renderTileEntityAt(te, x, y, z, partialTicks, destroyStage);
            if (te.getEnumFacing() != null) {
                ReflectUtil.invoke("renderTileEntityAt2", instance, x, y, z, partialTicks, destroyStage, te.getEnumFacing().getHorizontalIndex());
            } else {
                L.e("Enum facing is null");
            }
        } catch (Exception e) {
            L.e("Error in rendering via dynamic classload");
            e.printStackTrace(System.out);
        }

    }

    private Class<?> loadClass() {
        return new DynamicClassLoader("../bin")
                .load("com.hrkalk.zetapower.client.render.tileentities.ZetaTileEntityRendererReload");
    }

}
