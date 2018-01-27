package br.com.joinersa.contaeletrica.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import br.com.joinersa.contaeletrica.R;

public class SplashScreenActivity extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.pb_splash);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

        imageView = (ImageView) findViewById(R.id.iv_logo_splash);
        try {
            YoYo.with(Techniques.FadeIn).
                    duration(1000).
                    playOn(imageView.findViewById(R.id.iv_logo_splash));
        } catch (Exception e) { e.printStackTrace(); }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                startActivity(new Intent(SplashScreenActivity.this, InicialActivity.class));
                finish();
            }
        }, 1300);
    }
}
