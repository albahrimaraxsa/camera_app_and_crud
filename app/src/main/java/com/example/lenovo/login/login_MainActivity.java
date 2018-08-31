package com.example.lenovo.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.login.model.ResObj;
import com.example.lenovo.login.remote.ApiUtils;
import com.example.lenovo.login.remote.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class login_MainActivity extends AppCompatActivity {

    TextView txtUsername;
    TextView bank;
    TextView branch;

    UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.login_activity_main);
        setContentView(R.layout.login_activity_home);

        txtUsername = (TextView) findViewById(R.id.txtUsername);
        bank = (TextView) findViewById(R.id.bank);
        branch = (TextView) findViewById(R.id.branch);

        Bundle extras = getIntent().getExtras();
        String userid;

        userService = ApiUtils.getUserService();
        Call<ResObj> call = userService.login(extras.getString("username"), extras.getString("password"));

        call.enqueue(new Callback<ResObj>() {
                         @Override
                         public void onResponse(Call<ResObj> call, Response<ResObj> response) {
                             if (response.isSuccessful()) {
                                ResObj resObj = response.body();
//                                txtMessage.setText("Message: "+resObj.getMessage());
//                                txtSuccess.setText("Success: "+resObj.getSuccess());
                                txtUsername.setText("Username: "+resObj.getLogin().get(0).getUsername());
                                bank.setText("Bank: "+resObj.getLogin().get(0).getBank());
                                branch.setText("Branch: "+resObj.getLogin().get(0).getBranch());


                             }
                         }

                         @Override
                         public void onFailure(Call<ResObj> call, Throwable t) {
                             Toast.makeText(login_MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                         }
                     });

        GridView gridView = (GridView) findViewById(R.id.gridView);

        gridView.setAdapter(new Login_ImageAdapter((this)));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), Login_FullImageActivity.class);
                i.putExtra("id", position);
                startActivity(i);
            }
        });
    }
}
