package com.hrkalk.zetapower.client.render.helper;

import com.hrkalk.zetapower.tileentities.ZetaChest;
import com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.DynamicReloader;
import com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.ReloadEveryNTicks;
import com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.ReloadOnChange;

import net.minecraft.client.Minecraft;

public class EntityRotator_TODO extends TestHelper {

    private DynamicReloader reloader = new DynamicReloader(EntityRotator.class, "../bin");

    {
        reloader.reloadWhen.add(new ReloadOnChange(com.hrkalk.zetapower.client.render.helper.EntityRotator.class, "null"));
        reloader.reloadWhen.add(new ReloadEveryNTicks(20));
    }

    public ZetaChest get_chest() {
        return chest;
    }

    public void set_chest(ZetaChest chest) {
        this.chest = chest;
    }

    public void call_myVoid() {
        myVoid();
    }

    public Minecraft call_boo(int arg1, Minecraft arg2) {
        return boo(arg1, arg2);
    }
}
