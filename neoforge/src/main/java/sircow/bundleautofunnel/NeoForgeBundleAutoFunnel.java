package sircow.bundleautofunnel;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class NeoForgeBundleAutoFunnel {
    public NeoForgeBundleAutoFunnel(IEventBus eventBus) {
        CommonClass.init();
    }
}
