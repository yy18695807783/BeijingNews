package com.atguigu.beijingnews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.atguigu.beijingnews.activity.GuideActivity;
import com.atguigu.beijingnews.activity.MainActivity;
import com.atguigu.beijingnews.utils.CacheUtils;

public class SplashActivity extends Activity {

    private RelativeLayout rl_splash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        rl_splash = (RelativeLayout)findViewById(R.id.rl_splash);

        //透明度动画
        AlphaAnimation aa = new AlphaAnimation(0,1);
        aa.setDuration(2000);//设置动画时间
        aa.setFillAfter(true);//设施最终停留界面

        //缩放动画
        ScaleAnimation sa = new ScaleAnimation(0,1,0,1,ScaleAnimation.RELATIVE_TO_SELF,0.5f,ScaleAnimation.RELATIVE_TO_SELF,0.5f);
        sa.setDuration(2000);//设置动画时间
        sa.setFillAfter(true);//设施最终停留界面

        //旋转动画
        RotateAnimation ra = new RotateAnimation(0,360,ScaleAnimation.RELATIVE_TO_SELF,0.5f,ScaleAnimation.RELATIVE_TO_SELF,0.5f);
        ra.setDuration(2000);//设置动画时间
        ra.setFillAfter(true);//设施最终停留界面

        //动画集合
        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(aa);
        animationSet.addAnimation(sa);
        animationSet.addAnimation(ra);
        animationSet.setDuration(2000);

        //启动动画集合
        rl_splash.startAnimation(animationSet);

        animationSet.setAnimationListener(new MyAnimationListener());

    }
    class MyAnimationListener implements Animation.AnimationListener {

        /**
         * 动画开始的时候的回调
         * @param animation
         */
        @Override
        public void onAnimationStart(Animation animation) {

        }

        /**
         * 动画结束的时候到的回调
         * @param animation
         */
        @Override
        public void onAnimationEnd(Animation animation) {
//            Toast.makeText(SplashActivity.this, "欢迎界面完成", Toast.LENGTH_SHORT).show();

            //判断是否是第一次进入软件
            Boolean startMain = CacheUtils.getBoolean(SplashActivity.this,GuideActivity.START_MAIN);

            if(startMain){
                //不是第一次进入
                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);
            }else {
                //第一次进入  需要进入引导界面
                Intent intent = new Intent(SplashActivity.this,GuideActivity.class);
                startActivity(intent);
            }


            //关闭销毁当前界面
            finish();
        }

        /**
         * 当动画重复播放的时候回调这个方法
         * @param animation
         */
        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    //for receive customer msg from jpush server
//    private MessageReceiver mMessageReceiver;
//    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
//    public static final String KEY_TITLE = "title";
//    public static final String KEY_MESSAGE = "message";
//    public static final String KEY_EXTRAS = "extras";
//    public static boolean isForeground = false;
//    @Override
//    protected void onResume() {
//        isForeground = true;
//        super.onResume();
//    }
//
//
//    @Override
//    protected void onPause() {
//        isForeground = false;
//        super.onPause();
//    }

}
