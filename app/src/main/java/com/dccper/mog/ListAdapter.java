package com.dccper.mog;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedList;

/**
 * Created by reuben.pinto2k15 on 1/18/2016.
 */
public class ListAdapter extends BaseAdapter {
    private Activity mActivity;
    private LinkedList<NavObjects> mList=new LinkedList<>();
    private static LayoutInflater mInflater=null;

    public ListAdapter(Activity mActivity, LinkedList<NavObjects> mList) {
        this.mActivity = mActivity;
        this.mList = mList;
        mInflater=(LayoutInflater)this.mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=convertView;

        if(position==0)
        {
            if(convertView == null)
                view = mInflater.inflate(R.layout.navlist_header,null);
            ImageView headerImage=(ImageView)view.findViewById(R.id.nav_header);
            headerImage.setImageResource(mList.get(position).getmImageResource());
        }
        else {
            if (convertView == null)
                view = mInflater.inflate(R.layout.nav_list, null);

            ImageView navImage = (ImageView) view.findViewById(R.id.nav_item_img);
            TextView navText = (TextView) view.findViewById(R.id.nav_item_title);
            navImage.setImageResource(mList.get(position).getmImageResource());
            //navImage.setBackgroundResource(Resources.getSystem().getIdentifier(mList.get(position).getmImageResource(),"drawable","android"));
            navText.setText(mList.get(position).getmText());
        }
        return view;
    }
}
