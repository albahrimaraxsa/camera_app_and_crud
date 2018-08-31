package com.example.lenovo.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lenovo.login.model.ResObj;
import com.example.lenovo.login.remote.ApiUtils;
import com.example.lenovo.login.remote.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Lenovo on 16/08/2018.
 */

public class Login_LoginActivity extends AppCompatActivity {
    EditText edtUsername;
    EditText edtPassword;
    Button btnLogin;
    UserService userService;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_login);

        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        btnLogin    = (Button) findViewById(R.id.btnLogin);
        userService = ApiUtils.getUserService();

        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();
                //validate form

                if (validateLogin(username, password)) {
                    //do login
                    doLogin(username, password);
                    
                }
            }
        });
    }

    private boolean validateLogin(String username, String password) {
        if (username == null || username.trim().length() == 0) {
            Toast.makeText(this, "Username is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password == null || password.trim().length() == 0) {
            Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void doLogin(final String username, final String password){
        Call<ResObj> call = userService.login(username, password);
        call .enqueue(new Callback<ResObj>() {
            @Override
            public void onResponse(Call<ResObj> call, Response<ResObj> response) {
                if (response.isSuccessful()) {
                    ResObj resObj = response.body();
                    if(resObj.getMessage().equals("success")){
                        //login start main activity
//                        Intent intent = new Intent(Login_LoginActivity.this, login_MainActivity.class);
//                        intent.putExtra("username", username);
//                        intent.putExtra("password", password);
//                        startActivity(intent);


//                      toHome Activity
                        Intent intent = new Intent(Login_LoginActivity.this, Login_HomeActivity.class);
                        intent.putExtra("username", username);
                        intent.putExtra("password", password);
                        startActivity(intent);
                    }else{
                        Toast.makeText(Login_LoginActivity.this, "The username or password is incorect", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(Login_LoginActivity.this, "Error! Silakan coba lagi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResObj> call, Throwable t) {
                Toast.makeText(Login_LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
