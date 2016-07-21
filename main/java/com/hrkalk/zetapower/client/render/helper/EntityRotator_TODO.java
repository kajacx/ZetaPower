package com.hrkalk.zetapower.client.render.helper;

import com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.ReloadOnChange;
import com.hrkalk.zetapower.client.render.helper.EntityRotator;
import com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.DynamicReloader;
import com.hrkalk.zetapower.tileentities.ZetaChest;
import java.lang.Integer;
import com.hrkalk.zetapower.client.render.helper.TestHelper;
import net.minecraft.client.renderer.BlockFluidRenderer;
import com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.ReloadTrigger;
import com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.ReloadEveryNTicks;
import net.minecraft.client.Minecraft;
import com.hrkalk.zetapower.utils.loader.ReflectUtil;
import com.hrkalk.zetapower.entities.RideableShip;
import java.lang.String;
import com.hrkalk.zetapower.client.render.helper.EntityRotator_Reload;

public class EntityRotator_TODO extends TestHelper {

private DynamicReloader reloader = new DynamicReloader(EntityRotator.class, "../bin");

{
reloader.reloadWhen.add(new ReloadOnChange(com.hrkalk.zetapower.client.render.helper.EntityRotator.class, "../bin"));
reloader.reloadWhen.add(new ReloadEveryNTicks(20));
}

public ZetaChest get_chest() { return chest; }

public void set_chest(ZetaChest chest) { this.chest = chest; }

public void call_myVoid() { myVoid(); }

public Minecraft call_boo(int arg1, Minecraft arg2) { return boo(arg1, arg2); }

public void testtest(int arg1, Integer arg2, String arg3, RideableShip arg4, BlockFluidRenderer arg5) {
    try {
        ReflectUtil.invoke("testtest", reloader.getInstance(this), arg1, arg2, arg3, arg4, arg5);
    } catch(Throwable t) {
        System.out.println("Exception while executing reloadable code.");
        t.printStackTrace(System.out);
    }
}

}
