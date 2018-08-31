package com.example.lenovo.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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

public class Login_HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView txtUsername;
    TextView bank;
    TextView branch;

    UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                Toast.makeText(Login_HomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            Intent intent = new Intent(Login_HomeActivity.this, Ci_MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
//            Intent intent = new Intent(Login_HomeActivity.this, login_MainActivity.class);
//            startActivity(intent);
            Intent intent = new Intent(Login_HomeActivity.this, Login_HomeActivity.class);
            intent.putExtra("username", 1117);
            intent.putExtra("password", 1117);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(Login_HomeActivity.this, Login_HalamanDepanActivity.class);
            startActivity(intent);
        } /*else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
