package com.googy.spcc;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ColorPickerActivity extends Activity implements SeekBar.OnSeekBarChangeListener {


    View colorView;
    SeekBar redSeekBar, greenSeekBar, blueSeekBar;
    TextView redToolTip, greenToolTip, blueToolTip;
    Button buttonSelector;
    ClipboardManager clipBoard;
    ClipData clip;
    Window window;
    Display display;
    int red, green, blue, seekBarLeft;
    Rect thumbRect;
    AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Context mContext = getApplicationContext();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setContentView(R.layout.layout_color_picker);
        } else {
            setContentView(R.layout.layout_16);
        }

        display = ((WindowManager) this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        final SharedPreferences settings = getSharedPreferences("COLOR_SETTINGS", 0);
        final SharedPreferences appSettings = getSharedPreferences("APP_SETTINGS", 0);
        red = settings.getInt("RED_COLOR", 0);
        green = settings.getInt("GREEN_COLOR", 0);
        blue = settings.getInt("BLUE_COLOR", 0);

        clipBoard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        colorView = findViewById(R.id.colorView);
        window = getWindow();

        redSeekBar = (SeekBar) findViewById(R.id.redSeekBar);
        greenSeekBar = (SeekBar) findViewById(R.id.greenSeekBar);
        blueSeekBar = (SeekBar) findViewById(R.id.blueSeekBar);

        seekBarLeft = redSeekBar.getPaddingLeft();

        redToolTip = (TextView) findViewById(R.id.redToolTip);
        greenToolTip = (TextView) findViewById(R.id.greenToolTip);
        blueToolTip = (TextView) findViewById(R.id.blueToolTip);

        buttonSelector = (Button) findViewById(R.id.buttonSelector);

        redSeekBar.setOnSeekBarChangeListener(this);
        greenSeekBar.setOnSeekBarChangeListener(this);
        blueSeekBar.setOnSeekBarChangeListener(this);

        redSeekBar.setProgress(red);
        greenSeekBar.setProgress(green);
        blueSeekBar.setProgress(blue);

        //Setting View, Status bar & button color & hex codes

        colorView.setBackgroundColor(Color.rgb(red, green, blue));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            if (display.getRotation() != Surface.ROTATION_90 && display.getRotation() != Surface.ROTATION_270)
                window.setStatusBarColor(Color.rgb(red, green, blue));

        }

        //Set's color hex on Button
        buttonSelector.setText(String.format("#%02x%02x%02x", red, green, blue));

        final Button applyButton = (Button) findViewById(R.id.applyButton);
        applyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (appSettings.getBoolean("CheckBoxColor", true))
                    onApply();
                else {
                    Toast.makeText(mContext, "Activate pointer color first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final Button cancelButyon = (Button) findViewById(R.id.cancelButton);
        cancelButyon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        thumbRect = redSeekBar.getThumb().getBounds();

        redToolTip.setX(seekBarLeft + thumbRect.left);
        if (red < 10)
            redToolTip.setText("  " + red);
        else if (red < 100)
            redToolTip.setText(" " + red);
        else
            redToolTip.setText(red + "");

        thumbRect = greenSeekBar.getThumb().getBounds();

        greenToolTip.setX(seekBarLeft + thumbRect.left);
        if (green < 10)
            greenToolTip.setText("  " + green);
        else if (red < 100)
            greenToolTip.setText(" " + green);
        else
            greenToolTip.setText(green + "");

        thumbRect = blueSeekBar.getThumb().getBounds();

        blueToolTip.setX(seekBarLeft + thumbRect.left);
        if (blue < 10)
            blueToolTip.setText("  " + blue);
        else if (blue < 100)
            blueToolTip.setText(" " + blue);
        else
            blueToolTip.setText(blue + "");

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        if (seekBar.getId() == R.id.redSeekBar) {

            red = progress;
            thumbRect = seekBar.getThumb().getBounds();

            redToolTip.setX(seekBarLeft + thumbRect.left);

            if (progress < 10)
                redToolTip.setText("  " + red);
            else if (progress < 100)
                redToolTip.setText(" " + red);
            else
                redToolTip.setText(red + "");

        } else if (seekBar.getId() == R.id.greenSeekBar) {

            green = progress;
            thumbRect = seekBar.getThumb().getBounds();

            greenToolTip.setX(seekBar.getPaddingLeft() + thumbRect.left);
            if (progress < 10)
                greenToolTip.setText("  " + green);
            else if (progress < 100)
                greenToolTip.setText(" " + green);
            else
                greenToolTip.setText(green + "");

        } else if (seekBar.getId() == R.id.blueSeekBar) {

            blue = progress;
            thumbRect = seekBar.getThumb().getBounds();

            blueToolTip.setX(seekBarLeft + thumbRect.left);
            if (progress < 10)
                blueToolTip.setText("  " + blue);
            else if (progress < 100)
                blueToolTip.setText(" " + blue);
            else
                blueToolTip.setText(blue + "");

        }

        colorView.setBackgroundColor(Color.rgb(red, green, blue));

        String hex1 = String.format("#%02x%02x%02x", red, green, blue);

        int tint = Color.parseColor(hex1);
        PorterDuff.Mode mode = PorterDuff.Mode.SRC_ATOP;

        ImageView preview = (ImageView) findViewById(R.id.preview);

        preview.setColorFilter(tint, mode);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            if (display.getRotation() == Surface.ROTATION_0)
                window.setStatusBarColor(Color.rgb(red, green, blue));

        }

        //Setting the button hex color
        buttonSelector.setText(String.format("#%02x%02x%02x", red, green, blue));

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    public void colorSelect(View view) {

        //Copies color to Clipboard
        clip = ClipData.newPlainText("clip", buttonSelector.getText());
        clipBoard.setPrimaryClip(clip);

        Toast.makeText(this, "Color " + buttonSelector.getText() + " copied to clipboard", Toast.LENGTH_SHORT).show();

    }

    public void showDetails(View view) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            View dialogLayout = View.inflate(this, R.layout.dialog, null);

            alertDialog = new AlertDialog.Builder(this)
                    .setTitle("Material Color Picker")

                    .setView(dialogLayout)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();

            alertDialog.findViewById(R.id.website).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    startActivity(new Intent(Intent.ACTION_VIEW)
                            .setData(Uri.parse("http://www.anjithsasindran.in")));
                }
            });

            alertDialog.findViewById(R.id.github).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    startActivity(new Intent(Intent.ACTION_VIEW)
                            .setData(Uri.parse("https://github.com/4k3R/material-color-picker")));
                }
            });

            alertDialog.findViewById(R.id.instagram).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Uri uri = Uri.parse("http://instagram.com/_u/anjithsasindran");
                    Intent insta = new Intent(Intent.ACTION_VIEW, uri);
                    insta.setPackage("com.instagram.android");

                    PackageManager packageManager = getBaseContext().getPackageManager();
                    List<ResolveInfo> list = packageManager.queryIntentActivities(insta, PackageManager.MATCH_DEFAULT_ONLY);

                    if (list.size() > 0) {
                        startActivity(insta);
                    } else {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/anjithsasindran")));
                    }

                }
            });

            alertDialog.findViewById(R.id.dribbble).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    startActivity(new Intent(Intent.ACTION_VIEW)
                            .setData(Uri.parse("https://dribbble.com/shots/1858968-Material-Design-colorpicker?list=users&offset=2")));
                }
            });

        } else {

            MaterialDialog.Builder materialDialog = new MaterialDialog.Builder(this);
            View dialogLayout = View.inflate(this, R.layout.dialog_16, null);


            materialDialog.title(R.string.app_name);
            materialDialog.customView(dialogLayout, false);
            materialDialog.positiveText("OK");
            materialDialog.show();

            dialogLayout.findViewById(R.id.website).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    startActivity(new Intent(Intent.ACTION_VIEW)
                            .setData(Uri.parse("http://www.anjithsasindran.in")));
                }
            });

            dialogLayout.findViewById(R.id.github).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    startActivity(new Intent(Intent.ACTION_VIEW)
                            .setData(Uri.parse("https://github.com/4k3R/material-color-picker")));
                }
            });

            dialogLayout.findViewById(R.id.instagram).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Uri uri = Uri.parse("http://instagram.com/_u/anjithsasindran");
                    Intent insta = new Intent(Intent.ACTION_VIEW, uri);
                    insta.setPackage("com.instagram.android");

                    PackageManager packageManager = getBaseContext().getPackageManager();
                    List<ResolveInfo> list = packageManager.queryIntentActivities(insta, PackageManager.MATCH_DEFAULT_ONLY);

                    if (list.size() > 0) {
                        startActivity(insta);
                    } else {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/anjithsasindran")));
                    }

                }
            });

            dialogLayout.findViewById(R.id.dribbble).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    startActivity(new Intent(Intent.ACTION_VIEW)
                            .setData(Uri.parse("https://dribbble.com/shots/1858968-Material-Design-colorpicker?list=users&offset=2")));
                }
            });

        }

    }

    public void onApply() {

        String hex = String.format("#%02x%02x%02x", red, green, blue);
        //Storing values of red, green & blue in SharedPreferences

        SharedPreferences settings = getSharedPreferences("COLOR_SETTINGS", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("RED_COLOR", redSeekBar.getProgress());
        editor.putInt("GREEN_COLOR", greenSeekBar.getProgress());
        editor.putInt("BLUE_COLOR", blueSeekBar.getProgress());
        editor.putString("HEX_COLOR", hex);

        editor.apply();

        for (int i = 0; i < 19; i++) getBitmap(i, hex);

        Toast.makeText(this, "Don't forget to reboot", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //Properly dismissing dialog to prevent errors while changing orientation
        try {
            if (alertDialog.isShowing()) {
                alertDialog.dismiss();
            }
        } catch (NullPointerException e) {
            //do nothing
        }

        new java.io.File(Common.appPrefs).setReadable(true, false);

    }

    public void getBitmap(int i, String color) {
        TypedArray resourcesId = getResources().obtainTypedArray(R.array.pointer);
        TypedArray customPointerId = getResources().obtainTypedArray(R.array.custom_pointer);
        Context mContext = getApplicationContext();

        String name = mContext.getResources().getResourceEntryName(resourcesId.getResourceId(i, 0));
        SharedPreferences settings = getSharedPreferences("APP_SETTINGS", 0);

        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),
                resourcesId.getResourceId(i, 0)).copy(Bitmap.Config.ARGB_8888, true);

        Paint paint = new Paint();
        ColorFilter filter = new PorterDuffColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_IN);
        paint.setColorFilter(filter);

        Canvas canvasPointer = new Canvas(bitmap);
        canvasPointer.drawBitmap(bitmap, 0, 0, paint);

        if (name.equals("pointer")) {
            i = settings.getInt("CustomPointer", 0);
            if (settings.getBoolean("CheckBoxPointer", true)) {
                bitmap = BitmapFactory.decodeResource(mContext.getResources(),
                        customPointerId.getResourceId(i, 0)).copy(Bitmap.Config.ARGB_8888, true);
                saveBitmap(mContext, bitmap, name, 40, 40);
            } else {
                saveBitmap(mContext, bitmap, name, 40, 40);
            }
        } else if (name.contains("pointer_hover") || name.contains("plus")) {
            saveBitmap(mContext, bitmap, name, 40, 40);
        } else {
            saveBitmap(mContext, bitmap, name, 90, 90);
        }
        resourcesId.recycle();
        customPointerId.recycle();
    }

    public static void saveBitmap(Context mContext, Bitmap bitmap, String name, int h, int w) {
        String appPath = mContext.getFilesDir().getAbsolutePath();

        File storagePath = new File(appPath);
        File fBitmap = new File(storagePath, File.separator + name + ".png");
        FileOutputStream out = null;
        Bitmap scaledBitmap1 = Bitmap.createScaledBitmap(bitmap, h, w, false);

        try {
            out = new FileOutputStream(fBitmap);
            scaledBitmap1.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
