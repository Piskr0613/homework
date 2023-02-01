package com.example.redrockhomework;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;

import java.util.Timer;
import java.util.TimerTask;

public class MusicService extends Service {
    private MediaPlayer player;
    private Timer timer;
    public MusicService() {}

    @Override
    public IBinder onBind(Intent intent) {
        return new MusicControl();
    }

    @Override
    public void onCreate(){
        super.onCreate();
        //创建音乐播放器
        player=new MediaPlayer();
    }
    //添加计时器用于进度条
    public void addTimer(){
        if(timer==null){
            timer=new Timer();
            TimerTask task=new TimerTask() {
                @Override
                public void run() {
                    if(player==null) return;
                    int duration=player.getDuration();//获取歌曲总时长
                    int currentPosition=player.getCurrentPosition();//获取播放进度
                    Message msg=MusicActivity.handler.obtainMessage();
                    Bundle bundle=new Bundle();
                    bundle.putInt("duration",duration);
                    bundle.putInt("currentPosition",currentPosition);
                    msg.setData(bundle);
                    MusicActivity.handler.sendMessage(msg);
                }
            };
            timer.schedule(task,5,500);
        }
    }
    class MusicControl extends Binder{
        public void play(int i){
            Uri uri=Uri.parse("android.resource://"+getPackageName()+"/raw/"+"music"+i);
            try{
                player.reset();
                player=MediaPlayer.create(getApplicationContext(),uri);
                player.start();
                addTimer();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        public void pausePlay(){
            player.pause();
        }
        public void continuePlay(){
            player.start();
        }
        public void seekTo(int progress){
            player.seekTo(progress);
        }
    }
    //销毁多媒体播放器
    @Override
    public void onDestroy(){
        super.onDestroy();
        if(player==null) return;
        if(player.isPlaying())player.stop();
        player.release();
        player=null;
    }
}