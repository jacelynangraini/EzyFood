package com.jacelyn.ezyfood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class HomeScreen extends AppCompatActivity {
        private Handler StartHandler = new Handler();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home_screen);

            StartHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    } catch (Exception ignored) {
                        ignored.printStackTrace();
                    }
                }
            }, 2500);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            StartHandler.removeCallbacksAndMessages(null);
        }
}