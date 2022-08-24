package pneumax.mobilesystembygolaung.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;

import pneumax.mobilesystembygolaung.MainActivity;
import pneumax.mobilesystembygolaung.R;

public class SplashScreenActivity extends AppCompatActivity {
  ImageView ivTop,ivHeart,ivBeat,ivBottom;
  TextView  textView;
CharSequence charSequence;
int index;
long delay=200;
Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        BindWidgets();
        //Set full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
        ,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Initialize top animation



        //Initialize object animator
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(
        ivHeart,
                PropertyValuesHolder.ofFloat("ScaleX",1.2f),
                PropertyValuesHolder.ofFloat("ScaleY",1.2f)
        );
        //Set duration
        objectAnimator.setDuration(500);
        //Set repeat count
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        //Set repeat mode
        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        //Start animator
        objectAnimator.start();
        //Set animate text
        animatText("Mobile System");

        //Load GIF
//        Glide.with(this).load("https://firebasestorage.googleapis.com/v0/b/demoapp-ae96a.appspot.com/o/heart_beat.gif?alt=media&token=b21dddd8-782c-457c-babd-f2e922ba172b")
//                .asGif()
//                .



        //Initialize handler
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        },5000);

    }


    private void BindWidgets() {
        ivTop=(ImageView) findViewById(R.id.iv_top);
        ivHeart=(ImageView) findViewById(R.id.iv_heart);
        ivBeat=(ImageView) findViewById(R.id.iv_beat);
        ivBottom=(ImageView) findViewById(R.id.iv_bottom);
        textView=(TextView) findViewById(R.id.text_view);
    }


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //When tunnable is run
            //Set text
            textView.setText(charSequence.subSequence(0,index++));
            //Check condition
            if (index <= charSequence.length()){
                //When index is equal to text length
                //Run handler
                handler.postDelayed(runnable,delay);
            }
        }
    };

    //Create animated text method
    public void animatText(CharSequence cs){
        //Set text
        charSequence=cs;
        //Clear index
        index=0;
        //Clear test
        textView.setText("");
        //Remove call back
        handler.removeCallbacks(runnable);
        //Run handler
        handler.postDelayed(runnable,delay);

    }
}