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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

public class MainActivity extends Activity {

    AlertDialog alertDialog;

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

        setColorValue();
        setColor();

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

        SharedPreferences settings = getSharedPreferences("COLOR_SETTINGS", 0);

        int red = 35;
        int green = 111;
        int blue = 130;

        String hex = String.format("#%02x%02x%02x", red, green, blue);

        if (!settings.contains
                ("firstStart")) {

            SharedPreferences.Editor editor = settings.edit();

            editor.putInt("RED_COLOR", red);
            editor.putInt("GREEN_COLOR", green);

            editor.putInt("BLUE_COLOR", blue);
            editor.putString("HEX_COLOR", hex);

            editor.putBoolean("firstStart", true);
            editor.apply();
        }
    }

    public void setColor() {

        SharedPreferences settings = getSharedPreferences("COLOR_SETTINGS", 0);

        int red = settings.getInt("RED_COLOR", 0);
        int green = settings.getInt("GREEN_COLOR", 0);
        int blue = settings.getInt("BLUE_COLOR", 0);

        RelativeLayout li = (RelativeLayout) findViewById(R.id.main_layout);
        li.setBackgroundColor(Color.rgb(red, green, blue));

        Button changeColor = (Button) findViewById(R.id.changeColor);
        changeColor.setBackgroundColor(Color.rgb(red, green, blue));

        Button aboutspcc = (Button) findViewById(R.id.aboutspcc);
        aboutspcc.setBackgroundColor(Color.rgb(red, green, blue));

        TextView mainText = (TextView) findViewById(R.id.mainText);

        int color = red + green + blue;

        if (color < 650) {

            aboutspcc.setTextColor(Color.rgb(255, 255, 255));
            changeColor.setTextColor(Color.rgb(255, 255, 255));
            mainText.setTextColor(Color.rgb(255, 255, 255));
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

}
