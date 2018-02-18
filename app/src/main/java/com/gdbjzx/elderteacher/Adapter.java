package com.gdbjzx.elderteacher;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import javax.microedition.khronos.opengles.GL;

/**
 * Created by Administrator on 2018/2/18.
 */

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private Context mContext;

    private List<Image> mList;

    static class ViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            imageView = (ImageView) view.findViewById(R.id.image_view);
        }
    }

    public Adapter(List<Image> list){
        mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }//获取context
        final View view = LayoutInflater.from(mContext).inflate(R.layout.main_item,parent,false);//加载布局
        /*注册点击事件*/
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Image image = mList.get(position);
                Intent intent = new Intent(mContext,MainActivity.class);
               intent.putExtra("imageLable",image.getImageLable());
                mContext.startActivity(intent);
            }
        });
        /*注册点击事件*/
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Image image = mList.get(position);
        Glide.with(mContext).load(image.getImageId()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
