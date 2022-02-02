package com.ua.riks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.ua.riks.Retrofit.IMyService;
import com.ua.riks.Retrofit.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ManagerPackageActivity extends AppCompatActivity {

    ListView packageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_package);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.managerpackagetoolbar);
        this.setSupportActionBar(myToolbar);

        packageList = (ListView) findViewById(R.id.managerPackagesList);

        loadList();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_manager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.manager_menu_profile:
                Intent intent = new Intent(ManagerPackageActivity.this,ProfileActivity.class);
                ManagerPackageActivity.this.startActivity(intent);
                ManagerPackageActivity.this.finish();
                return true;

            case R.id.manager_menu_packages:

                return true;
            case R.id.manager_menu_customer:
                Intent intent1 = new Intent(ManagerPackageActivity.this,ManagerCustomerActivity.class);
                ManagerPackageActivity.this.startActivity(intent1);
                ManagerPackageActivity.this.finish();
                return true;

            case R.id.manager_menu_employee:
                Intent intent2 = new Intent(ManagerPackageActivity.this,ManagerEmployeeActivity.class);
                ManagerPackageActivity.this.startActivity(intent2);
                ManagerPackageActivity.this.finish();
                return true;

            default:

                return super.onOptionsItemSelected(item);

        }
    }
    public void loadList(){
        Retrofit retrofitClient = RetrofitClient.getInstance();
        IMyService iMyService = retrofitClient.create(IMyService.class);

        Call<String> call = iMyService.allPackages();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        String jsonresponse = response.body().toString();

                        try {
                            JSONObject obj = new JSONObject(jsonresponse);
                            Toast.makeText(getApplicationContext(), obj.getString("msg"),Toast.LENGTH_SHORT).show();

                            if(obj.getBoolean("success")){
                                JSONObject packages = new JSONObject(String.valueOf(obj.getJSONObject("packages")));
                                HashMap<String,Object> result = new Gson().fromJson(String.valueOf(packages), HashMap.class);

                                Log.e("aaaaabbbbb",result.toString());
                                managerPackagesAdapter adapter = new managerPackagesAdapter(result);
                                packageList.setAdapter(adapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "ERROR WHILE CONNECTING TO SERVER",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "ERROR WHILE CONNECTING TO SERVER",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void add(View view) {
        Intent intent = new Intent(ManagerPackageActivity.this,ManagerNewPackageActivity.class);
        ManagerPackageActivity.this.startActivity(intent);
    }
}