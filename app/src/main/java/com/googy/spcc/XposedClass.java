package com.googy.spcc;

import android.content.res.XResources;
import android.content.res.XResources.DrawableLoader;
import android.graphics.drawable.Drawable;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;

public class XposedClass
        implements IXposedHookZygoteInit, IXposedHookInitPackageResources {

    String[] fileName = Common.fileName;
    String[] resources = Common.resources;

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        XSharedPreferences prefs = new XSharedPreferences(getClass().getPackage().getName(), "APP_SETTINGS");
        prefs.makeWorldReadable();
        boolean isColorChecked = prefs.getBoolean("CheckBoxColor", true);
        boolean isPointerChecked = prefs.getBoolean("CheckBoxPointer", true);

        if (isColorChecked) {
            for (int i = 0; i < 19; i++)
                try {
                    final Drawable myDrawable = Drawable.createFromPath(Common.filesDir + fileName[i] + ".png");
                    XResources.setSystemWideReplacement("android", "drawable", resources[i], new DrawableLoader() {
                        @Override
                        public Drawable newDrawable(XResources res, int id) throws Throwable {
                            return myDrawable;
                        }
                    });

                } catch (Throwable ignored) {

                }
        }
        if (isPointerChecked) {
            try {
                final Drawable myDrawable = Drawable.createFromPath(Common.filesDir + "pointer.png");
                XResources.setSystemWideReplacement("android", "drawable", "tw_pointer_spot_hovering_spen", new DrawableLoader() {
                    @Override
                    public Drawable newDrawable(XResources res, int id) throws Throwable {
                        return myDrawable;
                    }
                });

            } catch (Throwable ignored) {

            }
        }
    }

    @Override
    public void handleInitPackageResources(InitPackageResourcesParam resparam) throws Throwable {

    }

}
