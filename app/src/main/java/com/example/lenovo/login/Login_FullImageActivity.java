package com.example.lenovo.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class Login_FullImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_full_image);

        Intent i = getIntent();
        int position = i.getExtras().getInt("id");
        Login_ImageAdapter adapter = new Login_ImageAdapter(this);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageResource(adapter.images[position]);
    }
}
