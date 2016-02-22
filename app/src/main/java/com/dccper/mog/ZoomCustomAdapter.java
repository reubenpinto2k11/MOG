package com.dccper.mog;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by reuben.pinto2k15 on 2/11/2016.
 */
public class ZoomCustomAdapter extends PagerAdapter {
    Context context;
    List<Images> list=new ArrayList<>();

    public ZoomCustomAdapter(Context context, List<Images> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup layout=(ViewGroup)inflater.inflate(R.layout.zoom_img_lay,container,false);
        TouchImageView imageView = (TouchImageView) layout.findViewById(R.id.zoomed_img);
        TextView caption=(TextView)layout.findViewById(R.id.zoomed_img_cap);

            try {
                InputStream imgfile=context.getAssets().open(list.get(position).getmImg_path());
//                AssetFileDescriptor descriptor=context.getAssets().openFd(nList.get(position));
//                Bitmap bitmap= BitmapFactory.decodeFileDescriptor(descriptor.getFileDescriptor());
                Bitmap bitmap= BitmapFactory.decodeStream(imgfile);
                imageView.setImageBitmap(bitmap);
                caption.setText(list.get(position).getmImg_name());
            } catch (IOException e) {
                e.printStackTrace();
            }

        container.addView(layout);
        return layout;
    }

    @Override
    public int getCount() {
        return  list.size();
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
