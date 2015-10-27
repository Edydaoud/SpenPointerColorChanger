package com.googy.spcc;


import android.content.res.XResources;
import android.graphics.drawable.Drawable;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class XposedClass implements IXposedHookZygoteInit, IXposedHookLoadPackage {

    String[] fileName = GetArray.fileName;
    String[] resources = GetArray.resources;


    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        for (int i = 0; i < 19; i++) {
            final Drawable myDrawable = Drawable.createFromPath("/data/data/com.googy.spcc/files/" + fileName[i] + ".png");
            XResources.setSystemWideReplacement("android", "drawable", resources[i], new XResources.DrawableLoader() {
                @Override
                public Drawable newDrawable(XResources res, int id) throws Throwable {
                    return myDrawable;
                }
            });
        }
    }

    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {

    }
}
