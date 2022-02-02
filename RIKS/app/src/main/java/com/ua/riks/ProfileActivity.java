package com.ua.riks;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.ua.riks.Retrofit.IMyService;
import com.ua.riks.Retrofit.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProfileActivity extends AppCompatActivity {

    TextView email;
    TextView dname;
    TextView cnic;
    TextView address;
    TextView contactno;
    ImageView profilepic;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(User.type.equals("Manager")){
            getMenuInflater().inflate(R.menu.menu_manager, menu);
        }
        else if(User.type.equals("Employee")){
            getMenuInflater().inflate(R.menu.menu_employee, menu);
        }
        else if(User.type.equals("Customer")){
            getMenuInflater().inflate(R.menu.menu_customer, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.manager_menu_profile:
//                Intent intent = new Intent(ProfileActivity.this,ProfileActivity.class);
//                ProfileActivity.this.startActivity(intent);
//                ProfileActivity.this.finish();
                return true;

            case R.id.employee_menu_profile:
//                Intent intent = new Intent(ProfileActivity.this,ProfileActivity.class);
//                ProfileActivity.this.startActivity(intent);
//                ProfileActivity.this.finish();
                return true;

            case R.id.customer_menu_profile:
//                Intent intent = new Intent(ProfileActivity.this,ProfileActivity.class);
//                ProfileActivity.this.startActivity(intent);
//                ProfileActivity.this.finish();
                return true;

            case R.id.manager_menu_packages:
                Intent intent = new Intent(ProfileActivity.this,ManagerPackageActivity.class);
                ProfileActivity.this.startActivity(intent);
                ProfileActivity.this.finish();
                return true;
            case R.id.manager_menu_customer:
                Intent intent1 = new Intent(ProfileActivity.this,ManagerCustomerActivity.class);
                ProfileActivity.this.startActivity(intent1);
                ProfileActivity.this.finish();
                return true;

            case R.id.manager_menu_employee:
                Intent intent2 = new Intent(ProfileActivity.this,ManagerEmployeeActivity.class);
                ProfileActivity.this.startActivity(intent2);
                ProfileActivity.this.finish();
                return true;

            case R.id.employee_menu_underwork:
                Intent intent6 = new Intent(ProfileActivity.this,EmployeePackageActivity.class);
                ProfileActivity.this.startActivity(intent6);
                ProfileActivity.this.finish();
                return true;

            case R.id.customer_menu_packages:
                Intent intent3 = new Intent(ProfileActivity.this,CustomerPackagesActivity.class);
                ProfileActivity.this.startActivity(intent3);
                ProfileActivity.this.finish();
                return true;
            case R.id.customer_menu_registered:
                Intent intent4 = new Intent(ProfileActivity.this,CustomerRegisteredPackagesActivity.class);
                ProfileActivity.this.startActivity(intent4);
                ProfileActivity.this.finish();
                return true;

            case R.id.customer_menu_underwork:
                Intent intent5 = new Intent(ProfileActivity.this,CustomerInProcessPackageActivity.class);
                ProfileActivity.this.startActivity(intent5);
                ProfileActivity.this.finish();
                return true;

            default:

                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.profiletoolbar);
        this.setSupportActionBar(myToolbar);



        address = (TextView) findViewById(R.id.mProfileAddress);
        contactno = (TextView) findViewById(R.id.mProfileContactNo);
        cnic = (TextView) findViewById(R.id.mProfileCNIC);
        dname = (TextView) findViewById(R.id.mProfileName);
        email = (TextView) findViewById(R.id.mProfileEmail);
        profilepic = (ImageView) findViewById(R.id.mProfilePic);

        address.setText(User.address);
        contactno.setText(User.contactno);
        cnic.setText(User.cnic);
        dname.setText(User.displayname);
        email.setText(User.email);

        if(User.profilepicname.equals("default")){
            profilepic.setImageResource(R.drawable.ic_profilepic);
        }
        else{
            new DownloadImageTask((ImageView) findViewById(R.id.mProfilePic)).execute(User.profilepicname);
        }
    }

    public void logout(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setMessage("Do you want to LogOut ?");
        builder.setTitle("Logout");
        builder.setCancelable(false);

        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog,int which)
            {
                User.logout(getApplicationContext());
                Intent mainIntent = new Intent(ProfileActivity.this,LoginRegisterActivity.class);
                ProfileActivity.this.startActivity(mainIntent);
                ProfileActivity.this.finish();
            }
        });

        builder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void deleteAccount(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Password to delete account");
        LinearLayout linearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams params = new  LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin= 50;
        params.rightMargin= 50;
        params.topMargin = 50;
        params.bottomMargin = 50;
        EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        input.setHint("PASSWORD");
        input.setLayoutParams(params);
        linearLayout.addView(input);
        builder.setView(linearLayout);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                Retrofit retrofitClient = RetrofitClient.getInstance();
                IMyService iMyService = retrofitClient.create(IMyService.class);

                String email = User.email;
                String pass = input.getText().toString();

                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)){
                    Toast.makeText(getApplicationContext(), "Enter the Password First !!",Toast.LENGTH_SHORT).show();
                    return;
                }

                input.setEnabled(false);

                Call<String> call = iMyService.authenticate(email,pass);

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

                                        Call<String> deletecall = iMyService.deleteUser(email);

                                        deletecall.enqueue(new Callback<String>() {
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

                                                                User.logout(getApplicationContext());
                                                                Intent mainIntent = new Intent(ProfileActivity.this,LoginRegisterActivity.class);
                                                                ProfileActivity.this.startActivity(mainIntent);
                                                                ProfileActivity.this.finish();
                                                                input.setText("");
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

    public void updateAttribute(View view) {
        String attributeName = "";
        String attributeParamName = "";
        switch(view.getId()) {
            case R.id.mProfileEEmail:
                attributeName = "Email";
                attributeParamName = "email";
                break;
            case R.id.mProfileEName:
                attributeName = "Name";
                attributeParamName = "displayname";
                break;
            case R.id.mProfileECNIC:
                attributeName = "CNIC";
                attributeParamName = "cnic";
                break;
            case R.id.mProfileEAddress:
                attributeName = "Address";
                attributeParamName = "address";
                break;
            case R.id.mProfileEContactNo:
                attributeName = "ContactNo";
                attributeParamName = "contactno";
                break;
            case R.id.mProfileEPass:
                attributeName = "New Password";
                attributeParamName = "password";
                break;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update "+attributeName);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new  LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin= 50;
        params.rightMargin= 50;
        params.topMargin = 50;
        params.bottomMargin = 50;
        EditText input = new EditText(this);
        EditText oldPass = null;
        if (attributeName.equals("CNIC") || attributeName.equals("ContactNo")){
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        else if(attributeName.equals("New Password")){
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            oldPass = new EditText(this);
            oldPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            oldPass.setHint("Old Password");
            oldPass.setLayoutParams(params);
            linearLayout.addView(oldPass);
        }
        else{
            input.setInputType(InputType.TYPE_CLASS_TEXT);
        }
        input.setHint(attributeName);
        input.setLayoutParams(params);
        linearLayout.addView(input);
        builder.setView(linearLayout);
        String finalAttributeName = attributeParamName;
        EditText finaloldpass = oldPass;
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Retrofit retrofitClient = RetrofitClient.getInstance();
                IMyService iMyService = retrofitClient.create(IMyService.class);
                final String[] b = {""};
                if(finalAttributeName.equals("password")){
                    Call<String> call = iMyService.authenticate(User.email,finaloldpass.getText().toString());
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
                                            b[0] = "true";
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
                        }
                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "ERROR WHILE CONNECTING TO SERVER",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                if(b.equals("") && finalAttributeName.equals("password")){
                    return;
                }
                Call<String> callupdate = iMyService.updateattribute(User.email, finalAttributeName,input.getText().toString());

                callupdate.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        //Toast.makeText()
                        if (response.isSuccessful()) {
                            if (response.body() != null) {

                                String jsonresponse = response.body().toString();

                                try {
                                    JSONObject obj = new JSONObject(jsonresponse);

                                    if(obj.getBoolean("success")){
                                        if(finalAttributeName.equals("email")){
                                            User.setEmail(getApplicationContext(),input.getText().toString());
                                            email.setText(input.getText().toString());
                                        }
                                        else if(finalAttributeName.equals("displayname")){
                                            User.setDisplayName(getApplicationContext(),input.getText().toString());
                                            dname.setText(input.getText().toString());
                                        }
                                        else if(finalAttributeName.equals("contactno")){
                                            User.setContactNo(getApplicationContext(),input.getText().toString());
                                            contactno.setText(input.getText().toString());
                                        }
                                        else if(finalAttributeName.equals("cnic")){
                                            User.setCNIC(getApplicationContext(),input.getText().toString());
                                            cnic.setText(input.getText().toString());
                                        }
                                        else if(finalAttributeName.equals("address")){
                                            User.setCNIC(getApplicationContext(),input.getText().toString());
                                            address.setText(input.getText().toString());
                                        }

                                        Toast.makeText(getApplicationContext(), obj.getString("msg"),Toast.LENGTH_SHORT).show();
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
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "ERROR WHILE CONNECTING TO SERVER",Toast.LENGTH_SHORT).show();
                    }
                });

            }});
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
            builder.show();
    }
}

class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}