package pneumax.mobilesystembygolaung.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.AsyncListDiffer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.mikhaellopez.circularimageview.CircularImageView;

import pneumax.mobilesystembygolaung.LoginActivity;
import pneumax.mobilesystembygolaung.MainActivity;
import pneumax.mobilesystembygolaung.R;

public class SplashActivity extends AppCompatActivity {
ImageView mimgSplash,mimgPneumaxLogo;
LottieAnimationView mLottieAnimation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Default Theme
        setTheme(R.style.Theme_MobileStoreSystemNoActionBar);
        setContentView(R.layout.activity_splash);

        BindWidgets();
//        mimgSplash.animate().translationY(-1600).setDuration(1000).setStartDelay(3000);
//        mimgPneumaxLogo.animate().translationY(1400).setDuration(1000).setStartDelay(3000);
//        mLottieAnimation.animate().translationY(1400).setDuration(1000).setStartDelay(3000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        },2000);
    }

    private void BindWidgets() {
        mimgSplash=(ImageView) findViewById(R.id.imgSplash);
        mimgPneumaxLogo=(ImageView) findViewById(R.id.imgPneumaxLogo);
        mLottieAnimation=(LottieAnimationView) findViewById(R.id.LottieAnimation);
    }


}