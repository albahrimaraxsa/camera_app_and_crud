package com.example.lenovo.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class Ci_GalleryActivity extends AppCompatActivity {
    ListView listView;
    List<String> listPhotoName;

    Ci_GalleryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ci_activity_gallery);

        listView = (ListView)findViewById(R.id.listView);

        Ci_DataHelper dataHelper = new Ci_DataHelper(this);
        listPhotoName =  dataHelper.getData();

        adapter = new Ci_GalleryAdapter(this, listPhotoName);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = view.findViewById(R.id.textView);

                String textViewvalue = (String) textView.getText();

                if (textViewvalue.substring(textViewvalue.length() - 4).equalsIgnoreCase(".jpg")) {
                    Intent intent = new Intent(Ci_GalleryActivity.this, CI_ShowImage.class);
                    intent.putExtra("filename", textView.getText());
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(Ci_GalleryActivity.this, Ci_ShowVideo.class);
                    intent.putExtra("filename", textView.getText());
                    startActivity(intent);
                }

            }
        });

    }

}
