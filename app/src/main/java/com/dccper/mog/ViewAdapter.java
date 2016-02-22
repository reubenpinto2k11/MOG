package com.dccper.mog;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by reuben.pinto2k15 on 1/21/2016.
 */
public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.RecViewHolder>{
    static List<Artist> mList;
    Context context;

    public ViewAdapter(List<Artist> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    @Override
    public RecViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_card_test,viewGroup,false);
        RecViewHolder rvh=new RecViewHolder(view,i);
        return rvh;
    }

    @Override
    public void onBindViewHolder(RecViewHolder recViewHolder, final int position) {


        recViewHolder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(),ArtistDetailActivity.class);
                intent.putExtra("artist_id",mList.get(position).getnArtistID());

                v.getContext().startActivity(intent);
            }
        });
        if((position%2)==0)
            recViewHolder.mCardView.setCardBackgroundColor(Color.parseColor("#B2EBF2"));
        else
            recViewHolder.mCardView.setCardBackgroundColor(Color.parseColor("#84FFFF"));
//        Picasso
//                .with(context)
//                .load(mList.get(position).getmImageID())
//                .placeholder(R.drawable.stub)
//                .into(recViewHolder.mArtistImage);
        try {
//            AssetFileDescriptor descriptor=context.getAssets().openFd(mList.get(position).getmImagePath());
            InputStream imgfile=context.getAssets().open(mList.get(position).getmImagePath());
//            Bitmap bitmap= BitmapFactory.decodeFileDescriptor(descriptor.getFileDescriptor());
            Bitmap bitmap=BitmapFactory.decodeStream(imgfile);
            recViewHolder.mArtistImage.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        recViewHolder.mArtistImage.setImageResource(mList.get(position).getmImageID());
        recViewHolder.mArtistName.setText(mList.get(position).getmArtistName());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class RecViewHolder extends RecyclerView.ViewHolder{
        CardView mCardView;
        ImageView mArtistImage;
        TextView mArtistName;
        int position;

        RecViewHolder(View itemView,int pos)
        {
            super(itemView);
            position=pos;
            mCardView=(CardView)itemView.findViewById(R.id.cardview);
            mCardView.setCardElevation(14.5f);
            mArtistImage=(ImageView)itemView.findViewById(R.id.artist_photo);
            mArtistName=(TextView)itemView.findViewById(R.id.artist_name);
        }
    }
}
