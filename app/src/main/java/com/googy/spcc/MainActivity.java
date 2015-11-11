package com.googy.spcc;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

public class MainActivity extends Activity {

    AlertDialog alertDialog;
    CheckBox colorCheckbox, pointerCheckbox;
    SharedPreferences settings;
    TextView pointerTextView, colorTextView, xposedSettingTextView, mainText;
    Button changeColor, changePointer, aboutSpcc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        final Button changeColor = (Button) findViewById(R.id.changeColor);
        changeColor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ColorPickerActivity.class);
                startActivity(intent);
                finish();
            }
        });
        final Button changePointer = (Button) findViewById(R.id.changePointer);
        changePointer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PointerChooser.class);
                startActivity(intent);
                finish();
            }
        });
        setColorValue();
        setColor();
        setCheckbox();
    }

    public void about_spcc(View view) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            View dialogLayout = View.inflate(this, R.layout.dialog_main, null);

            alertDialog = new AlertDialog.Builder(this)
                    .setTitle("Spen Pointer Color Changer")

                    .setView(dialogLayout)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();

            alertDialog.findViewById(R.id.main_website).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    startActivity(new Intent(Intent.ACTION_VIEW)
                            .setData(Uri.parse("http://forum.xda-developers.com/member.php?u=4472524")));
                }
            });

            alertDialog.findViewById(R.id.support).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    startActivity(new Intent(Intent.ACTION_VIEW)
                            .setData(Uri.parse("http://forum.xda-developers.com/android/apps-games/app-spen-pointer-color-changer-t3233659")));
                }
            });

            alertDialog.findViewById(R.id.main_github).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    startActivity(new Intent(Intent.ACTION_VIEW)
                            .setData(Uri.parse("https://github.com/Edydaoud/SpenPointerColorChanger")));
                }
            });
        } else {

            MaterialDialog.Builder materialDialog = new MaterialDialog.Builder(this);
            View dialogLayout = View.inflate(this, R.layout.dialog_16_main, null);


            materialDialog.title(R.string.app_name);
            materialDialog.customView(dialogLayout, false);
            materialDialog.positiveText("OK");
            materialDialog.show();

            dialogLayout.findViewById(R.id.main_website).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    startActivity(new Intent(Intent.ACTION_VIEW)
                            .setData(Uri.parse("http://forum.xda-developers.com/member.php?u=4472524")));
                }
            });


        }

    }

    public void setColorValue() {

        SharedPreferences colorSettings = getSharedPreferences("COLOR_SETTINGS", 0);
        SharedPreferences appSettings = getSharedPreferences("APP_SETTINGS", 0);
        int red = 35;
        int green = 111;
        int blue = 130;

        String hex = String.format("#%02x%02x%02x", red, green, blue);

        if (!appSettings.contains
                ("firstStart")) {

            SharedPreferences.Editor colorEditor = colorSettings.edit();

            colorEditor.putInt("RED_COLOR", red);
            colorEditor.putInt("GREEN_COLOR", green);

            colorEditor.putInt("BLUE_COLOR", blue);
            colorEditor.putString("HEX_COLOR", hex);
            colorEditor.apply();

            SharedPreferences.Editor settingsEditor = appSettings.edit();

            settingsEditor.putBoolean("firstStart", false);
            settingsEditor.putInt("CustomPointer", 0);
            settingsEditor.putBoolean("CheckBoxColor", false).apply();
            settingsEditor.putBoolean("CheckBoxPointer", false).apply();
            settingsEditor.apply();
        }
    }

    public void setColor() {

        SharedPreferences settings = getSharedPreferences("COLOR_SETTINGS", 0);

        int red = settings.getInt("RED_COLOR", 0);
        int green = settings.getInt("GREEN_COLOR", 0);
        int blue = settings.getInt("BLUE_COLOR", 0);

        LinearLayout li = (LinearLayout) findViewById(R.id.main_layout);
        li.setBackgroundColor(Color.rgb(red, green, blue));

        changeColor = (Button) findViewById(R.id.changeColor);
        changePointer = (Button) findViewById(R.id.changePointer);
        aboutSpcc = (Button) findViewById(R.id.aboutspcc);
        xposedSettingTextView = (TextView) findViewById(R.id.xposedSettings);

        colorTextView = (TextView) findViewById(R.id.colorTextView);
        pointerTextView = (TextView) findViewById(R.id.pointerTextView);

        mainText = (TextView) findViewById(R.id.mainText);

        int color = red + green + blue;

        if (color < 650) {

            xposedSettingTextView.setTextColor(Common.colorWhite);
            colorTextView.setTextColor(Common.colorWhite);
            pointerTextView.setTextColor(Common.colorWhite);
            changePointer.setTextColor(Common.colorWhite);
            aboutSpcc.setTextColor(Common.colorWhite);
            changeColor.setTextColor(Common.colorWhite);
            mainText.setTextColor(Common.colorWhite);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Window window = this.getWindow();
            window.addFlags

                    (WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags

                    (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.rgb(red, green, blue));

        }

    }

    public void onCheckboxClicked(View view) {

        settings = getSharedPreferences("APP_SETTINGS", 0);
        SharedPreferences.Editor editor = settings.edit();


        boolean checked = ((CheckBox) view).isChecked();

        switch (view.getId()) {
            case R.id.checkBoxColor:
                if (checked) {
                    editor.putBoolean("CheckBoxColor", true).apply();
                } else
                    editor.putBoolean("CheckBoxColor", false).apply();
                break;
            case R.id.checkBoxPointer:
                if (checked) {
                    editor.putBoolean("CheckBoxPointer", true).apply();
                } else
                    editor.putBoolean("CheckBoxPointer", false).apply();
                break;
        }
        setCheckbox();

    }

    public void setCheckbox() {

        settings = getSharedPreferences("APP_SETTINGS", 0);

        colorCheckbox = (CheckBox) findViewById(R.id.checkBoxColor);
        pointerCheckbox = (CheckBox) findViewById(R.id.checkBoxPointer);
        colorTextView = (TextView) findViewById(R.id.colorTextView);
        pointerTextView = (TextView) findViewById(R.id.pointerTextView);

        if (settings.getBoolean("CheckBoxColor", true)) {
            colorCheckbox.setChecked(true);
            colorTextView.setText(R.string.xposed_color_checkbox_disable);
        } else {
            colorCheckbox.setChecked(false);
            colorTextView.setText(R.string.xposed_color_checkbox_enable);
        }
        if (settings.getBoolean("CheckBoxPointer", true)) {
            pointerCheckbox.setChecked(true);
            pointerTextView.setText(R.string.xposed_pointer_checkbox_disable);
        } else {
            pointerCheckbox.setChecked(false);
            pointerTextView.setText(R.string.xposed_pointer_checkbox_enable);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        new java.io.File(Common.appPrefs).setReadable(true, false);
    }
}
