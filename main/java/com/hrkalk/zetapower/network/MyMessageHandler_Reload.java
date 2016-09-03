package com.hrkalk.zetapower.network;

import com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.ReloadTrigger;
import com.hrkalk.zetapower.network.MyMessageHandler;
import com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.ReloadEveryNTicks;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.ReloadOnChange;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import com.hrkalk.zetapower.utils.loader.ReflectUtil;
import java.lang.Object;
import com.hrkalk.zetapower.utils.loader.myloader.DynamicClassReloadPrepare.DynamicReloader;
import com.hrkalk.zetapower.network.NbtNetworkMessage;
import com.hrkalk.zetapower.network.MyMessageHandler_Reload;

public class MyMessageHandler_Reload {

public MyMessageHandler thiz;

public IMessage onMessage(NbtNetworkMessage arg1, MessageContext arg2) {
    return null;
}

}
