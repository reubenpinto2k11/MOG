package com.dccper.mog;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by reuben.pinto2k15 on 1/19/2016.
 */
public class CustomAdapter extends android.support.v4.view.PagerAdapter {
    Context context;
    ArrayList<Integer> list=new ArrayList<Integer>();
    ArrayList<Images> nList=new ArrayList<>();

    public CustomAdapter(Context context, ArrayList<Integer> list,ArrayList<Images> sList) {
        this.context = context;
        this.list = list;
        this.nList = sList;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup layout=(ViewGroup)inflater.inflate(R.layout.image_lay,container,false);
        ImageView imageView = (ImageView) layout.findViewById(R.id.pager_image);
        if(list != null)
            imageView.setImageResource(list.get(position));
        if(nList != null)
        {
            try {
                InputStream imgfile=context.getAssets().open(nList.get(position).getmImg_path());
//                AssetFileDescriptor descriptor=context.getAssets().openFd(nList.get(position));
//                Bitmap bitmap= BitmapFactory.decodeFileDescriptor(descriptor.getFileDescriptor());
                Bitmap bitmap=BitmapFactory.decodeStream(imgfile);
                imageView.setImageBitmap(bitmap);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent zoomIntent=new Intent(context,ImageExpandActivity.class);
                        zoomIntent.putExtra("curr_item",position);
                        zoomIntent.putParcelableArrayListExtra("img_array",nList);
                        context.startActivity(zoomIntent);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        container.addView(layout);
        return layout;
    }

    @Override
    public int getCount() {
        if(list != null)
            return list.size();
        else
            return nList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }
}
