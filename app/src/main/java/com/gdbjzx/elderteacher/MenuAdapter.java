package com.gdbjzx.elderteacher;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Administrator on 2018/2/18.
 */

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    private Context mContext;

    private List<Image> mList;

    private Intent intent;

    static class ViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            imageView = (ImageView) view.findViewById(R.id.image_view);
        }
    }

    public MenuAdapter(List<Image> list){
        mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }//获取context
        final View view = LayoutInflater.from(mContext).inflate(R.layout.menu,parent,false);//加载布局
        /*注册点击事件*/
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Image image = mList.get(position);
                switch (image.getImageLabel()){
                    case 0:
                        intent = new Intent(mContext,MainActivity.class);
                        mContext.startActivity(intent);
                        break;
                    case 1:case 2:case 3:case 4:case 6:case 7:case 8:case 9:
                        intent = new Intent(mContext,SecondMenuActivity.class);
                        intent.putExtra("imageLabel",image.getImageLabel());
                        mContext.startActivity(intent);
                        break;
                    case 5:
                        intent = new Intent(mContext,PressPracticeActivity.class);
                        mContext.startActivity(intent);
                        break;
                    case 11:
                        intent = new Intent(mContext,ChooseMusicActivity.class);
                        mContext.startActivity(intent);
                        break;
                    case 38:
                        intent = new Intent(mContext,StepTeacherActivity.class);
                        intent.putExtra("Label",38);
                        mContext.startActivity(intent);
                        break;
                    default:
                        break;
                }//通过判断image.getImageLabel，决定启动哪个活动
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
