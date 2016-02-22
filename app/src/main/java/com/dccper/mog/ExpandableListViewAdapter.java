package com.dccper.mog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by reuben.pinto2k15 on 2/10/2016.
 */
public class ExpandableListViewAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> head_group;
    private HashMap<String,List<String>> child_arr;

    public ExpandableListViewAdapter(Context context, List<String> head_group, HashMap<String, List<String>> child_arr) {
        this.context = context;
        this.head_group = head_group;
        this.child_arr = child_arr;
    }

    @Override
    public int getGroupCount() {
        return this.head_group.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.child_arr.get(head_group.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.head_group.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.child_arr.get(head_group.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view=convertView;
        if(view==null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(R.layout.mus_lay_head,parent,false);
        }
        String parent_text=(String)getGroup(groupPosition);
        TextView parent_head_txt=(TextView)view.findViewById(R.id.mues_lay_head_text);
        parent_head_txt.setText(parent_text);

        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view=convertView;
        if(view==null)
        {
            LayoutInflater inflater=(LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(R.layout.mus_lay_child,parent,false);
        }
        String child_text=(String)getChild(groupPosition,childPosition);
        TextView child_textview=(TextView)view.findViewById(R.id.mues_lay_child_text);
        child_textview.setText(child_text);
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
