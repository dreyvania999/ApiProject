package com.example.apiproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

public class AdapterMask extends BaseAdapter {

    private Context mContext;
    List<Mask> maskList;

    public AdapterMask(Context mContext, List<Mask> maskList) {
        this.mContext = mContext;
        this.maskList = maskList;
    }

    @Override
    public int getCount() {
        return maskList.size();
    }

    @Override
    public Object getItem(int i) {
        return maskList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return maskList.get(i).getId_toothpaste();
    }

    private Bitmap getUserImage(String encodedImg) {
        byte[] bytes = Base64.decode(encodedImg, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(mContext, R.layout.item_mask, null);

        TextView NamePaste = v.findViewById(R.id.NamePaste);
        TextView Abrasiveness = v.findViewById(R.id.Abrasiveness);
        TextView Volume = v.findViewById(R.id.Volume);
        ImageView Image = v.findViewById(R.id.imageView);
        TextView Price = v.findViewById(R.id.price);
       TextView Country = v.findViewById(R.id.countryOrigin);

        Mask mask = maskList.get(position);
        NamePaste.setText(mask.getName_toothpaste());
        Abrasiveness.setText(Integer.toString(mask.getAbrasiveness_index()));
        Price.setText(Double.toString(mask.getPrice()));
        Country.setText(mask.getCountry_of_origin());
        Volume.setText(Integer.toString(mask.getVolume()));
        if (!mask.getImage().equals("null")) {
            Image.setImageBitmap(getUserImage(mask.getImage()));
        }

        return v;
    }
}

