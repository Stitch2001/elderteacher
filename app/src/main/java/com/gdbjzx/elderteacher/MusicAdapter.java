package com.gdbjzx.elderteacher;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.net.URI;
import java.util.List;

/**
 * Created by Administrator on 2018/2/18.
 */

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {

    private Context mContext;

    private List<Music> mList;

    private MediaPlayer mediaPlayer;

    private boolean isPlaying;

    private ImageView playingImageView;

    private File file;

    static class ViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        TextView musicName;
        ImageView musicPlayView;
        ImageView musicTickView;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            musicName = (TextView) view.findViewById(R.id.music_name);
            musicPlayView = (ImageView) view.findViewById(R.id.music_play);
            musicTickView = (ImageView) view.findViewById(R.id.music_tick);
        }
    }

    public MusicAdapter(List<Music> list){
        mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }//获取context

        final View view = LayoutInflater.from(mContext).inflate(R.layout.music_list,parent,false);//加载布局

        /*注册点击事件*/
        final ViewHolder holder = new ViewHolder(view);
        holder.musicPlayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Music music = mList.get(position);//获取正在播放的music
                if (isPlaying){ //如果已经在播放，则停止，并把图标恢复为播放图标。
                    mediaPlayer.reset();
                    playingImageView.setImageResource(R.drawable.music_play);
                    isPlaying = false;
                    if (holder.musicPlayView != playingImageView){ //如果点击的不是停止图标，则播放点击的音乐
                        mediaPlayer = MediaPlayer.create(mContext,music.getMusicId());
                        mediaPlayer.start();//播放音乐
                        isPlaying = true;//标记为正在播放
                        playingImageView = holder.musicPlayView;//标记正在播放的音乐的播放图标
                        holder.musicPlayView.setImageResource(R.drawable.music_stop);//更改为停止图标
                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                isPlaying = false;//音乐停止后，标记为停止播放
                                holder.musicPlayView.setImageResource(R.drawable.music_play);//图标恢复为播放图标
                            }
                        });
                    }
                } else {
                    mediaPlayer = MediaPlayer.create(mContext,music.getMusicId());
                    mediaPlayer.start();//播放音乐
                    isPlaying = true;//标记为正在播放
                    playingImageView = holder.musicPlayView;//标记正在播放的音乐的播放图标
                    holder.musicPlayView.setImageResource(R.drawable.music_stop);//更改为停止图标
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            isPlaying = false;//音乐停止后，标记为停止播放
                            holder.musicPlayView.setImageResource(R.drawable.music_play);//图标恢复为播放图标
                        }
                    });
                }
            }
        });//注册播放事件
        holder.musicTickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                final Music music = mList.get(position);
                /*确认是否设置*/
                    AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                    dialog.setTitle("设置铃声");
                    dialog.setMessage("您将设置"+music.getMusicName()+"为来电铃声，确定吗？");
                    dialog.setCancelable(true);
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            /*设置铃声（未成功开发。考虑到将来要通过网络手段获取歌曲，故不在此处做冗余研究）
                            Uri musicUri = Uri.parse("android.resource://"+mContext.getPackageName()+"/"+music.getMusicId());
                            try {
                                file = new File(new URI(musicUri.toString()));
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                            ContentValues values = new ContentValues();
                            values.put(MediaStore.MediaColumns.DATA, file.getAbsolutePath());
                            values.put(MediaStore.MediaColumns.TITLE, music.getMusicName());
                            values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/*");
                            values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
                            Uri uri = MediaStore.Audio.Media.getContentUriForPath(file.getAbsolutePath());//获取系统音频文件的Uri
                            Uri newUri = mContext.getContentResolver().insert(uri,values);//将文件插入系统媒体库，并获取新的Uri
                            RingtoneManager.setActualDefaultRingtoneUri(mContext,RingtoneManager.TYPE_RINGTONE,newUri);
                            */
                            AlertDialog.Builder okDialog = new AlertDialog.Builder(mContext);
                            okDialog.setTitle("设置铃声");
                            okDialog.setMessage("成功！");
                            okDialog.setCancelable(true);
                            okDialog.setPositiveButton("确定",null);
                            okDialog.show();
                        }
                    });
                    dialog.setNegativeButton("取消",null);
                    dialog.show();
                /*确认是否设置*/
            }
        });//注册设置事件
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                final Music music = mList.get(position);
                if (music.getMusicLabel() == 0){
                    Intent intent = new Intent(mContext,SubmitSuggestionsActivity.class);
                    mContext.startActivity(intent);
                }
            }
        });//注册反馈事件
        /*注册点击事件*/

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Music music = mList.get(position);
        holder.musicName.setText(music.getMusicName());
        if (music.getMusicLabel() == 0){
            holder.musicPlayView.setImageResource(0);
            holder.musicPlayView.setClickable(false);
            holder.musicTickView.setImageResource(0);
            holder.musicTickView.setClickable(false);
            holder.musicName.setTextSize(16);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}
