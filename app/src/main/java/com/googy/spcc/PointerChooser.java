package com.googy.spcc;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class PointerChooser extends Activity {
    GridView grid;
    Context mContext;
    SharedPreferences settings;
    TypedArray resourcesId;
    ImageView preview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pointer_chooser);
        mContext = getApplicationContext();

        resourcesId = getResources().obtainTypedArray(R.array.custom_pointer);
        settings = mContext.getSharedPreferences("APP_SETTINGS", 0);

        preview = (ImageView) findViewById(R.id.CurrentPointer);

        if (settings.getBoolean("CheckBoxPointer", true)) {
            preview.setImageResource(resourcesId.getResourceId(settings.getInt("CustomPointer", 0), 0));
        } else {
            preview.setImageResource(R.drawable.pointer);
        }

        CustomGrid adapter = new CustomGrid(mContext);
        grid = (GridView) findViewById(R.id.grid);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("CustomPointer", position);
                editor.apply();

                setCurrentPointer();

                if (settings.getBoolean("CheckBoxPointer", true)) {
                    resourcesId = getResources().obtainTypedArray(R.array.custom_pointer);
                    Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),
                            resourcesId.getResourceId(position, 0)).copy(Bitmap.Config.ARGB_8888, true);

                    ColorPickerActivity.saveBitmap(mContext, bitmap, "pointer", 40, 40);

                    resourcesId.recycle();
                }

            }
        });

    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();

    }

    public void setCurrentPointer() {

        resourcesId = getResources().obtainTypedArray(R.array.custom_pointer);
        settings = mContext.getSharedPreferences("APP_SETTINGS", 0);

        preview = (ImageView) findViewById(R.id.CurrentPointer);
        if (settings.getBoolean("CheckBoxPointer", true)) {
            Toast.makeText(mContext, "OK! now reboot", Toast.LENGTH_SHORT).show();
            preview.setImageResource(resourcesId.getResourceId(settings.getInt("CustomPointer", 0), 0));
        } else {
            Toast.makeText(mContext, "Activate custom pointer first", Toast.LENGTH_SHORT).show();
        }
        resourcesId.recycle();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        new java.io.File("/data/data/com.googy.spcc/shared_prefs/APP_SETTINGS.xml").setReadable(true, false);
    }
}
