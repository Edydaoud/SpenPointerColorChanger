package com.googy.spcc;


import android.content.res.XResources;
import android.graphics.drawable.Drawable;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class XposedClass implements IXposedHookZygoteInit, IXposedHookLoadPackage {


    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {

        final Drawable myDrawable = Drawable.createFromPath("/data/data/com.googy.spcc/files/pointer_hover.png");
        XResources.setSystemWideReplacement("android", "drawable", "tw_pointer_spot_hovering_spen_more", new XResources.DrawableLoader() {
            @Override
            public Drawable newDrawable(XResources res, int id) throws Throwable {
                return myDrawable;
            }
        });

        final Drawable myDrawable2 = Drawable.createFromPath("/data/data/com.googy.spcc/files/pointer.png");
        XResources.setSystemWideReplacement("android", "drawable", "tw_pointer_spot_hovering_spen", new XResources.DrawableLoader() {
            @Override
            public Drawable newDrawable(XResources res, int id) throws Throwable {
                return myDrawable2;
            }
        });
    }

    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {

    }
}
