package com.example.redrockhomework;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import static java.lang.Integer.parseInt;
public class MusicActivity extends AppCompatActivity implements View.OnClickListener {

    //进度条
    private static SeekBar sb;
    private static TextView tv_progress,tv_total,name_song;
    //动画
    private ObjectAnimator animator;
    private MusicService.MusicControl musicControl;
    private String name;
    private Intent intent1,intent2;
    private MyServiceConn conn;
    //记录服务是否解绑，默认没有
    private boolean isUnbind=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        intent1=getIntent();
        init();
    }

    private void init(){
        //进度条上小绿点的位置
        tv_progress=(TextView) findViewById(R.id.tv_progress);
        //进度条的总长度
        tv_total=(TextView) findViewById(R.id.tv_total);
        //进度条的控件
        sb=(SeekBar) findViewById(R.id.sb);
        //显示歌曲名称的控件
        name_song=(TextView) findViewById(R.id.song_name);

        findViewById(R.id.btn_play).setOnClickListener(this);
        findViewById(R.id.btn_pause).setOnClickListener(this);
        findViewById(R.id.btn_continue_play).setOnClickListener(this);
        findViewById(R.id.btn_exit).setOnClickListener(this);

        name=intent1.getStringExtra("name");
        name_song.setText(name);
        //创建一个intent对象，从当前的Activity跳转到Service
        intent2=new Intent(this,MusicService.class);
        //创建服务连接对象
        conn=new MyServiceConn();
        //绑定服务
        bindService(intent2,conn,BIND_AUTO_CREATE);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress==seekBar.getMax()){
                    animator.pause();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                int progress=seekBar.getProgress();
                musicControl.seekTo(progress);
            }
        });
        ImageView iv_music=(ImageView) findViewById(R.id.iv_music);
        String position=intent1.getStringExtra("position");
        int i=parseInt(position);
        iv_music.setImageResource(frag1.icons[i]);
        animator=ObjectAnimator.ofFloat(iv_music,"rotation",0f,360.0f);//设置旋转动画
        animator.setDuration(10000);//动画转一周为10秒
        animator.setInterpolator(new LinearInterpolator());//匀速
        animator.setRepeatCount(-1);//-1表示设置动画无线循环
    }
    public static Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            Bundle bundle=msg.getData();
            int duration=bundle.getInt("duration");
            int currentPosition=bundle.getInt("currentPosition");
            sb.setMax(duration);
            sb.setProgress(currentPosition);
            int minute=duration/1000/60;
            int second=duration/1000%60;
            String strMinute=null;
            String strSecond=null;
            if(minute<10){
                strMinute="0"+minute;
            }else {
                strMinute=minute+"";
            }
            if(second<10){
                strSecond="0"+second;
            }else {
                strSecond=second+"";
            }
            tv_total.setText(strMinute+":"+strSecond);
            minute=currentPosition/1000/60;
            second=currentPosition/1000%60;
            if(minute<10){
                strMinute="0"+minute;
            }else {
                strMinute=minute+"";
            }
            if(second<10){
                strSecond="0"+second;
            }else {
                strSecond=second+"";
            }
            tv_progress.setText(strMinute+":"+strSecond);
        }
    };

    //用于实现连接服务
    class MyServiceConn implements ServiceConnection{
        @Override
        public void onServiceConnected(ComponentName name, IBinder service){
            musicControl=(MusicService.MusicControl) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }
    //判断服务是否被绑定
    private void unbind(boolean isUnbind){
        if(!isUnbind){
            musicControl.pausePlay();
            unbindService(conn);//解绑服务
        }
    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_play:
                String position=intent1.getStringExtra("position");
                int i=parseInt(position);
                musicControl.play(i);
                animator.start();
                break;
            case R.id.btn_pause:
                musicControl.pausePlay();
                animator.pause();
                break;
            case R.id.btn_continue_play:
                musicControl.continuePlay();
                animator.start();
                break;
            case R.id.btn_exit:
                unbind(isUnbind);
                isUnbind=true;
                finish();
                break;
        }
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        unbind(isUnbind);
    }
}