package com.example.lenovo.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.File;

import static com.example.lenovo.login.Ci_GetFilePath.getFilePathVideo;

public class Ci_ShowVideo extends AppCompatActivity {

    VideoView videoView;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ci_activity_show_video);

        videoView = (VideoView) findViewById(R.id.videoView);
        textView = (TextView) findViewById(R.id.textView);


        String filename = getIntent().getExtras().getString("filename");

        textView.setText(filename);
        File video = new File(getFilePathVideo(filename));

//        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//        Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(),bmOptions);

        videoView.setVideoPath(video.getAbsolutePath());
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.start();
//        imageView.setImageBitmap((bitmap));
    }
}
