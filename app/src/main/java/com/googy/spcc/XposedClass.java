package com.googy.spcc;


import android.content.res.XResources;
import android.graphics.drawable.Drawable;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class XposedClass implements IXposedHookZygoteInit, IXposedHookLoadPackage {

	String drawable[] = {
	"down", "left", "left_down", "left_right_split",
	"left_up", "move", "plus", "pointer",
	"pointer_hover", "resize_left",
	"resize_left_right", "resize_right",
	"resize_up_down", "right", "right_down",
	"right_up", "select", "up", "up_down_split"};
	
	String replace[] = {
	"tw_pointer_scroll_hovering_spen_pointer05_dark",
	"tw_pointer_scroll_hovering_spen_pointer07_dark",
	"tw_pointer_scroll_hovering_spen_pointer06_dark",
	"tw_pointer_spot_hovering_spen_split02_dark",
	"tw_pointer_scroll_hovering_spen_pointer08_dark",
	"tw_pointer_spot_hovering_spen_move_dark", 
	"tw_pointer_pen_select_hovering_spen_pointer01_dark", 
	"tw_pointer_spot_hovering_spen", 
	"tw_pointer_spot_hovering_spen_more", 
	"tw_pointer_spot_hovering_spen_resize03_dark",
	"tw_pointer_spot_hovering_spen_resize01_dark",
	"tw_pointer_spot_hovering_spen_resize04_dark", 
	"tw_pointer_spot_hovering_spen_resize02_dark",
	"tw_pointer_scroll_hovering_spen_pointer03_dark",
	"tw_pointer_scroll_hovering_spen_pointer04_dark", 
	"tw_pointer_scroll_hovering_spen_pointer02_dark", 
	"tw_pointer_spot_hovering_spen_cursor_dark",
	"tw_pointer_scroll_hovering_spen_pointer01_dark",  
	"tw_pointer_spot_hovering_spen_split01_dark"};
    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
		for(int i = 0; i < 19; i++){
        final Drawable myDrawable = Drawable.createFromPath("/data/data/com.googy.spcc/files/" + drawable[i] +".png");
        XResources.setSystemWideReplacement("android", "drawable", replace[i], new XResources.DrawableLoader() {
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
