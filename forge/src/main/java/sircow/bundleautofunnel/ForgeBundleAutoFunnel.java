package sircow.bundleautofunnel;

import net.minecraftforge.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class ForgeBundleAutoFunnel {
    public ForgeBundleAutoFunnel() {
        Constants.LOG.info("Hello Forge world!");
        CommonClass.init();
    }
}