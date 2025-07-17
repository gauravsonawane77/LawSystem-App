package com.example.lawsystem;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class splashActivity extends AppCompatActivity {
    private ImageView imageViewLogo;
    private TextView textViewAppName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imageViewLogo = findViewById(R.id.imageViewLogo);
        textViewAppName = findViewById(R.id.textViewAppName);

        String[] userInfo = MyPreferences.getUserInfo(splashActivity.this);
        String email = userInfo[1];
        String role = userInfo[3];

        Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        // Set animation listener to make the views visible when animation ends
        fadeInAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                imageViewLogo.setVisibility(android.view.View.VISIBLE);
                textViewAppName.setVisibility(android.view.View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        // Start animation for logo and app name
        imageViewLogo.startAnimation(fadeInAnimation);
        textViewAppName.startAnimation(fadeInAnimation);

        // Post a delayed action to transition to the next activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start the next activity
                if(email.isEmpty()){
                    Intent intent = new Intent(splashActivity.this, loginActivity.class);
                    startActivity(intent);
                    finish(); // Close the splash activity
                }else{
                    if(role.equals("user")) {
                        startActivity(new Intent(splashActivity.this, MainActivity.class));
                        finish();
                    }else {
                        startActivity(new Intent(splashActivity.this, LawyerHomeActivity.class));
                        finish();
                    }
                }

            }
        }, 3000);
    }
}