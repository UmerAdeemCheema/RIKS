package com.ua.riks;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.ua.riks.Retrofit.IMyService;
import com.ua.riks.Retrofit.RetrofitClient;
import com.ua.riks.databinding.ActivityLoginRegisterBinding;
import com.ua.riks.ui.main.SectionsPagerAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginRegisterActivity extends AppCompatActivity {

    private ImageView imageView;

    // Uri indicates, where the image will be picked from
    private Uri filePath;
    String profilePath = "default";

    // request code
    private final int PICK_IMAGE_REQUEST = 22;

    FirebaseStorage storage;
    StorageReference storageReference;

    private ActivityLoginRegisterBinding binding;
    String typee = "";

    IMyService iMyService;

    @Override
    protected void onStop(){
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        View view = LayoutInflater.from(getApplication()).inflate(R.layout.fragment_register, null);
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
    }

    public void login(View view) {
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);
        MaterialEditText loginEmail = (MaterialEditText) findViewById(R.id.loginEmail);
        MaterialEditText loginPass = (MaterialEditText) findViewById(R.id.loginPass);
        Button loginbutton = (Button) findViewById(R.id.loginBtn);

        String email = loginEmail.getText().toString();
        String pass = loginPass.getText().toString();

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)){
            Toast.makeText(this, "Enter the Credentials First !!",Toast.LENGTH_SHORT).show();
            return;
        }

        loginbutton.setEnabled(false);

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
                            Toast.makeText(getApplicationContext(), obj.getString("msg"),Toast.LENGTH_SHORT).show();

                            if(obj.getBoolean("success")){
                                JSONObject user = new JSONObject(String.valueOf(obj.getJSONObject("user")));
                                String username = user.getString("username");
                                String displayname = user.getString("displayname");
                                String cnic = user.getString("cnic");
                                String address = user.getString("address");
                                String contactno = user.getString("contactno");
                                String type = user.getString("type");
                                String profilepicname = user.getString("profilepicname");
                                User.login(LoginRegisterActivity.this,user.getInt("userId"),email,username,displayname,cnic,address,contactno,type,profilepicname);
                                Intent mainIntent = new Intent(LoginRegisterActivity.this,ProfileActivity.class);
                                LoginRegisterActivity.this.startActivity(mainIntent);
                                LoginRegisterActivity.this.finish();
                                loginEmail.setText("");
                                loginPass.setText("");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "ERROR WHILE CONNECTING TO SERVER",Toast.LENGTH_SHORT).show();
                    }
                }
                loginbutton.setEnabled(true);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "ERROR WHILE CONNECTING TO SERVER",Toast.LENGTH_SHORT).show();
                loginbutton.setEnabled(true);
            }
        });

    }
    Button registerbutton;
    public void register(View view) {

        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        MaterialEditText registerEmail = (MaterialEditText) findViewById(R.id.registerEmail);
        MaterialEditText registerPass = (MaterialEditText) findViewById(R.id.registerPass);
        MaterialEditText registerCPass = (MaterialEditText) findViewById(R.id.registerCPass);
        MaterialEditText registerUName = (MaterialEditText) findViewById(R.id.registerUName);
        MaterialEditText registerDName = (MaterialEditText) findViewById(R.id.registerDName);
        MaterialEditText registerCNIC = (MaterialEditText) findViewById(R.id.registerCNIC);
        MaterialEditText registerAddress = (MaterialEditText) findViewById(R.id.registerAddress);
        MaterialEditText registerContactNo = (MaterialEditText) findViewById(R.id.registerContactNo);
        MaterialEditText registerToken = (MaterialEditText) findViewById(R.id.registerToken);
        RadioGroup registerType = (RadioGroup) findViewById(R.id.registerRadio);
        registerbutton = (Button) findViewById(R.id.registerBtn);

        String email = registerEmail.getText().toString();
        String pass = registerPass.getText().toString();
        String cpass = registerCPass.getText().toString();
        String uname = registerUName.getText().toString();
        String dname = registerDName.getText().toString();
        String cnic = registerCNIC.getText().toString();
        String address = registerAddress.getText().toString();
        String contactno = registerContactNo.getText().toString();
        String token = registerToken.getText().toString();
        String type = typee;
        String profilepic = "default";



        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(cpass) || TextUtils.isEmpty(uname) || TextUtils.isEmpty(dname) || TextUtils.isEmpty(cnic) || TextUtils.isEmpty(address) || TextUtils.isEmpty(contactno) || type.equals("")){
            Toast.makeText(this, "Enter all Data First !!",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(token)){
            if(type.equals("Employee") || type.equals("Manager")){
                Toast.makeText(this, "Enter the Token If you are an Employee or Manager !!",Toast.LENGTH_SHORT).show();
                return;
            }
            token = "";
        }

        if(!pass.equals(cpass)){
            Toast.makeText(this, "The Password and Confirm Password are not same !!",Toast.LENGTH_SHORT).show();
            return;
        }

        registerbutton.setEnabled(false);



        Call<String> call = iMyService.adduser(email,uname,dname,cnic,address,contactno,profilepic,pass,type,token);

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

                                if(!profilePath.equals("default")){
                                    uploadImage(uname,email,getApplicationContext());
                                }
                                else{
                                    registerbutton.setEnabled(true);
                                }

                                registerAddress.setText("");
                                registerCNIC.setText("");
                                registerContactNo.setText("");
                                registerCPass.setText("");
                                registerDName.setText("");
                                registerUName.setText("");
                                registerEmail.setText("");
                                registerPass.setText("");
                                registerToken.setText("");
                                typee="";
                                filePath=null;
                                profilePath = "default";
                                ImageView imageView2 = (ImageView) findViewById(R.id.registerProfile);
                                imageView2.setImageResource(R.drawable.ic_profilepic);
                            }else{
                                Toast.makeText(getApplicationContext(), obj.getString("msg"),Toast.LENGTH_SHORT).show();
                                registerbutton.setEnabled(true);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "ERROR WHILE CONNECTING TO SERVER",Toast.LENGTH_SHORT).show();
                    }
                }
                registerbutton.setEnabled(true);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "ERROR WHILE CONNECTING TO SERVER",Toast.LENGTH_SHORT).show();
                registerbutton.setEnabled(true);
            }
        });
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.registerRadioC:
                if (checked)
                    typee = "Customer";
                    break;
            case R.id.registerRadioE:
                if (checked)
                    typee = "Employee";
                    break;
            case R.id.registerRadioM:
                if (checked)
                    typee = "Manager";
                    break;
        }
    }

    public void selectImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Image from here..."),PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        ImageView imageView2 = (ImageView) findViewById(R.id.registerProfile);
        super.onActivityResult(requestCode,resultCode,data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            profilePath = String.valueOf(data.getData());
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                imageView2.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    private void uploadImage(String username, String email, Context context)
    {
        if (filePath != null) {
            StorageReference ref = storageReference.child("images/"+username);
            registerbutton.setEnabled(false);
            ref.putFile(filePath).addOnSuccessListener(
                    new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                        {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                            {
                                @Override
                                public void onSuccess(Uri downloadUrl)
                                {
                                    updateProfilePic(String.valueOf(downloadUrl), email, context);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e)
                                {
                                    registerbutton.setEnabled(true);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            registerbutton.setEnabled(true);
                        }
                    });
        }
    }

    private void updateProfilePic(String profilePicUrl, String email, Context context) {
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        registerbutton.setEnabled(false);

        Call<String> call = iMyService.updateattribute(email,"profilepicname",profilePicUrl);

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
                                Toast.makeText(getApplicationContext(), "Registeration Completed",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(getApplicationContext(), obj.getString("msg"),Toast.LENGTH_SHORT).show();
                            }
                            registerbutton.setEnabled(true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "ERROR WHILE CONNECTING TO SERVER",Toast.LENGTH_SHORT).show();
                    }
                }
                registerbutton.setEnabled(true);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "ERROR WHILE CONNECTING TO SERVER",Toast.LENGTH_SHORT).show();
                registerbutton.setEnabled(true);
            }
        });
    }

    public void resetProfilePic(View view){
        ImageView imageView2 = (ImageView) findViewById(R.id.registerProfile);
        filePath=null;
        profilePath = "default";
        imageView2.setImageResource(R.drawable.ic_profilepic);
    }
}