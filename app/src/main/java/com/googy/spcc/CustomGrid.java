package com.googy.spcc;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class CustomGrid extends BaseAdapter {
    private static Context mContext;
    private final TypedArray resourcesId;

    public CustomGrid(Context c) {
        mContext = c;
        this.resourcesId = mContext.getResources().obtainTypedArray(R.array.custom_pointer);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return resourcesId.length();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            new View(mContext);
            grid = inflater.inflate(R.layout.grid_single, null);
            ImageView imageView = (ImageView) grid.findViewById(R.id.grid_image);
            imageView.setImageResource(resourcesId.getResourceId(position, 0));
        } else {
            grid = convertView;
        }

        return grid;
    }
}
