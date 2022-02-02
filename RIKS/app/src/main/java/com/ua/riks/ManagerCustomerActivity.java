package com.ua.riks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ua.riks.Retrofit.IMyService;
import com.ua.riks.Retrofit.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ManagerCustomerActivity extends AppCompatActivity {

    ListView customerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_customer);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.managercustomertoolbar);
        this.setSupportActionBar(myToolbar);

        customerList = (ListView) findViewById(R.id.mUserList);

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
                Intent intent = new Intent(ManagerCustomerActivity.this,ProfileActivity.class);
                ManagerCustomerActivity.this.startActivity(intent);
                ManagerCustomerActivity.this.finish();
                return true;

            case R.id.manager_menu_packages:
                Intent intent1 = new Intent(ManagerCustomerActivity.this,ManagerPackageActivity.class);
                ManagerCustomerActivity.this.startActivity(intent1);
                ManagerCustomerActivity.this.finish();
                return true;
            case R.id.manager_menu_customer:

                return true;

            case R.id.manager_menu_employee:
                Intent intent2 = new Intent(ManagerCustomerActivity.this,ManagerEmployeeActivity.class);
                ManagerCustomerActivity.this.startActivity(intent2);
                ManagerCustomerActivity.this.finish();
                return true;

            default:

                return super.onOptionsItemSelected(item);

        }
    }

    public void loadList(){
        Retrofit retrofitClient = RetrofitClient.getInstance();
        IMyService iMyService = retrofitClient.create(IMyService.class);

        Call<String> call = iMyService.allUsers();

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
                                JSONObject packages = new JSONObject(String.valueOf(obj.getJSONObject("users")));
                                HashMap<String,Object> result = new Gson().fromJson(String.valueOf(packages), HashMap.class);

                                managerCustomersAdapter adapter = new managerCustomersAdapter(result);
                                customerList.setAdapter(adapter);
                                TextView users = (TextView) findViewById(R.id.mTotalUser);
                                users.setText("Total Users    "+result.size());
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
}