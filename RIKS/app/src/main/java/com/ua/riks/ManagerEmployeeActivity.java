package com.ua.riks;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
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

public class ManagerEmployeeActivity extends AppCompatActivity {

    ListView customerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_employee);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.managercustomertoolbar);
        this.setSupportActionBar(myToolbar);

        customerList = (ListView) findViewById(R.id.mEmployeeList);

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
                Intent intent = new Intent(ManagerEmployeeActivity.this,ProfileActivity.class);
                ManagerEmployeeActivity.this.startActivity(intent);
                ManagerEmployeeActivity.this.finish();
                return true;

            case R.id.manager_menu_packages:
                Intent intent1 = new Intent(ManagerEmployeeActivity.this,ManagerPackageActivity.class);
                ManagerEmployeeActivity.this.startActivity(intent1);
                ManagerEmployeeActivity.this.finish();
                return true;
            case R.id.manager_menu_customer:
                Intent intent2 = new Intent(ManagerEmployeeActivity.this,ManagerCustomerActivity.class);
                ManagerEmployeeActivity.this.startActivity(intent2);
                ManagerEmployeeActivity.this.finish();
                return true;

            case R.id.manager_menu_employee:

                return true;

            default:

                return super.onOptionsItemSelected(item);

        }
    }

    public void loadList(){
        Retrofit retrofitClient = RetrofitClient.getInstance();
        IMyService iMyService = retrofitClient.create(IMyService.class);

        Call<String> call = iMyService.allEmployees();

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
                                JSONObject users = new JSONObject(String.valueOf(obj.getJSONObject("users")));
                                HashMap<String,Object> result = new Gson().fromJson(String.valueOf(users), HashMap.class);

                                managerCustomersAdapter adapter = new managerCustomersAdapter(result);
                                customerList.setAdapter(adapter);
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

    public void generateToken(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Email to generate token");
        LinearLayout linearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams params = new  LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin= 50;
        params.rightMargin= 50;
        params.topMargin = 50;
        params.bottomMargin = 50;
        EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("EMAIL");
        input.setLayoutParams(params);
        linearLayout.addView(input);
        builder.setView(linearLayout);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                Retrofit retrofitClient = RetrofitClient.getInstance();
                IMyService iMyService = retrofitClient.create(IMyService.class);

                String email = input.getText().toString();

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(getApplicationContext(), "Enter the Email First !!",Toast.LENGTH_SHORT).show();
                    return;
                }

                input.setEnabled(false);

                Call<String> call = iMyService.generateRegistrationToken(email);

                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        //Toast.makeText()
                        if (response.isSuccessful()) {
                            if (response.body() != null) {

                                String jsonresponse = response.body().toString();

                                try {
                                    JSONObject obj = new JSONObject(jsonresponse);

                                    if(obj.getBoolean("success")){
                                        Toast.makeText(getApplicationContext(), obj.getString("msg"),Toast.LENGTH_SHORT).show();
                                        String result = obj.getString("registerationtoken");

                                        TextView token = (TextView) findViewById(R.id.mEToken);
                                        token.setText(result);

                                        dialog.cancel();

                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(), obj.getString("msg"),Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            } else {
                                Toast.makeText(getApplicationContext(), "ERROR WHILE CONNECTING TO SERVER",Toast.LENGTH_SHORT).show();
                            }
                        }
                        input.setEnabled(true);
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "ERROR WHILE CONNECTING TO SERVER",Toast.LENGTH_SHORT).show();
                        input.setEnabled(true);
                    }
                });
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void copyToken(View view) {
        TextView token = (TextView) findViewById(R.id.mEToken);
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("token", token.getText().toString());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getApplicationContext(), "TOKEN COPIED TO CLIP BOARD",Toast.LENGTH_SHORT).show();
    }
}