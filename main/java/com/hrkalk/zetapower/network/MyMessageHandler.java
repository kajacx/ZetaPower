package com.hrkalk.zetapower.network;

import com.hrkalk.zetapower.utils.loader.ReflectUtil;
import com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.DynamicReloader;
import com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.ReloadEveryNTicks;
import com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.ReloadOnChange;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MyMessageHandler extends Object {

    private DynamicReloader reloader = new DynamicReloader(MyMessageHandler.class, "../bin");

    {
        reloader.reloadWhen.add(new ReloadOnChange(com.hrkalk.zetapower.network.MyMessageHandler.class, "../bin"));
        reloader.reloadWhen.add(new ReloadEveryNTicks(200));

        reloader.addToBlacklist("com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.ReloadTrigger");
        reloader.addToBlacklist("com.hrkalk.zetapower.network.MyMessageHandler");
        reloader.addToBlacklist("com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.ReloadEveryNTicks");
        reloader.addToBlacklist("net.minecraftforge.fml.common.network.simpleimpl.MessageContext");
        reloader.addToBlacklist("com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.ReloadOnChange");
        reloader.addToBlacklist("net.minecraftforge.fml.common.network.simpleimpl.IMessage");
        reloader.addToBlacklist("com.hrkalk.zetapower.utils.loader.ReflectUtil");
        reloader.addToBlacklist("java.lang.Object");
        reloader.addToBlacklist("com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.DynamicReloader");
        reloader.addToBlacklist("com.hrkalk.zetapower.network.NbtNetworkMessage");

        reloader.addToBlacklistPrefix("net.minecraft");
        reloader.addToBlacklistPrefix("net.minecraftforge");
    }

    public MyMessageHandler() {
        super();
    }

    public IMessage onMessage(NbtNetworkMessage arg1, MessageContext arg2) {
        try {
            return (IMessage) ReflectUtil.invoke("onMessage", reloader.getInstance(this), arg1, arg2);
        } catch (Throwable t) {
            System.out.println("Exception while executing reloadable code.");
            t.printStackTrace(System.out);
            return null;
        }
    }

}
