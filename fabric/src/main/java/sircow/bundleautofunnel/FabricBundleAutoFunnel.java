package sircow.bundleautofunnel;

import net.fabricmc.api.ModInitializer;

public class FabricBundleAutoFunnel implements ModInitializer {
    @Override
    public void onInitialize() {
        Constants.LOG.info("Hello Fabric world!");
        CommonClass.init();
    }
}
