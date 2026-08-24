package sircow.bundleautofunnel;

import sircow.bundleautofunnel.platform.Services;

public class CommonClass {
    public static void init() {
        if (Services.PLATFORM.isModLoaded("bundleautofunnel")) {
            Constants.LOG.info("Initialising {}", Constants.MOD_NAME);
        }
    }
}
