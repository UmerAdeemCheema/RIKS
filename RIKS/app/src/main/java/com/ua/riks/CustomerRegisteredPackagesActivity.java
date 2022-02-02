package com.ua.riks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
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

public class CustomerRegisteredPackagesActivity extends AppCompatActivity {

    ListView packageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_registered_packages);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.customerpackagetoolbar);
        this.setSupportActionBar(myToolbar);

        packageList = (ListView) findViewById(R.id.cRegisteredPackagesList);

        loadList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_customer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.customer_menu_profile:
                Intent intent = new Intent(CustomerRegisteredPackagesActivity.this,ProfileActivity.class);
                CustomerRegisteredPackagesActivity.this.startActivity(intent);
                CustomerRegisteredPackagesActivity.this.finish();
                return true;

            case R.id.customer_menu_packages:
                Intent intent1 = new Intent(CustomerRegisteredPackagesActivity.this,CustomerPackagesActivity.class);
                CustomerRegisteredPackagesActivity.this.startActivity(intent1);
                CustomerRegisteredPackagesActivity.this.finish();
                return true;
            case R.id.customer_menu_registered:

                return true;

            case R.id.customer_menu_underwork:
                Intent intent2 = new Intent(CustomerRegisteredPackagesActivity.this,CustomerInProcessPackageActivity.class);
                CustomerRegisteredPackagesActivity.this.startActivity(intent2);
                CustomerRegisteredPackagesActivity.this.finish();
                return true;

            default:

                return super.onOptionsItemSelected(item);

        }
    }
    public void loadList(){
        Retrofit retrofitClient = RetrofitClient.getInstance();
        IMyService iMyService = retrofitClient.create(IMyService.class);

        Call<String> call = iMyService.allRegisteredPackages(User.id);

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

                                customerRegisteredPackagesAdapter adapter = new customerRegisteredPackagesAdapter(result);
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
}