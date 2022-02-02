package com.ua.riks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.ua.riks.Retrofit.IMyService;
import com.ua.riks.Retrofit.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ManagerNewPackageActivity extends AppCompatActivity {

    private ImageView imageView;

    // Uri indicates, where the image will be picked from
    private Uri filePath;
    String profilePath = "default";

    // request code
    private final int PICK_IMAGE_REQUEST = 22;

    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_new_package);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        Toolbar myToolbar = (Toolbar) findViewById(R.id.managernewpackagetoolbar);
        this.setSupportActionBar(myToolbar);


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
                Intent intent = new Intent(ManagerNewPackageActivity.this,ProfileActivity.class);
                ManagerNewPackageActivity.this.startActivity(intent);
                ManagerNewPackageActivity.this.finish();
                return true;

            case R.id.manager_menu_packages:
                Intent intent1 = new Intent(ManagerNewPackageActivity.this,ManagerPackageActivity.class);
                ManagerNewPackageActivity.this.startActivity(intent1);
                ManagerNewPackageActivity.this.finish();
                return true;
            case R.id.manager_menu_customer:
                Intent intent2 = new Intent(ManagerNewPackageActivity.this,ManagerCustomerActivity.class);
                ManagerNewPackageActivity.this.startActivity(intent2);
                ManagerNewPackageActivity.this.finish();
                return true;

            case R.id.manager_menu_employee:
                Intent intent3 = new Intent(ManagerNewPackageActivity.this,ManagerEmployeeActivity.class);
                ManagerNewPackageActivity.this.startActivity(intent3);
                ManagerNewPackageActivity.this.finish();
                return true;

            default:

                return super.onOptionsItemSelected(item);

        }
    }

    Button addbutton;
    public void addPackage(View view) {

        Retrofit retrofitClient = RetrofitClient.getInstance();
        IMyService iMyService = retrofitClient.create(IMyService.class);

        MaterialEditText addPId = (MaterialEditText) findViewById(R.id.addPId);
        MaterialEditText addPName = (MaterialEditText) findViewById(R.id.addPName);
        MaterialEditText addPDes = (MaterialEditText) findViewById(R.id.addPDes);
        addbutton = (Button) findViewById(R.id.addPBtn);

        String Id = addPId.getText().toString();
        String Name = addPName.getText().toString();
        String Des = addPDes.getText().toString();
        String profilepic = "default";



        if(TextUtils.isEmpty(Id) || TextUtils.isEmpty(Name) || TextUtils.isEmpty(Des)){
            Toast.makeText(this, "Enter all Data First !!",Toast.LENGTH_SHORT).show();
            return;
        }

        addbutton.setEnabled(false);



        Call<String> call = iMyService.addNewPackage(Id,Name,profilepic,Des);

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
                                    uploadImage(Id,getApplicationContext());
                                }
                                else{
                                    addbutton.setEnabled(true);
                                }

                                addPId.setText("");
                                addPName.setText("");
                                addPDes.setText("");
                                filePath=null;
                                profilePath = "default";
                                ImageView imageView2 = (ImageView) findViewById(R.id.addPackageProfile);
                                imageView2.setImageResource(R.drawable.ic_bpackage);
                            }else{
                                Toast.makeText(getApplicationContext(), obj.getString("msg"),Toast.LENGTH_SHORT).show();
                                addbutton.setEnabled(true);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "ERROR WHILE CONNECTING TO SERVER",Toast.LENGTH_SHORT).show();
                    }
                }
                addbutton.setEnabled(true);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "ERROR WHILE CONNECTING TO SERVER",Toast.LENGTH_SHORT).show();
                addbutton.setEnabled(true);
            }
        });
    }

    public void selectPackageImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Image from here..."),PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        ImageView imageView2 = (ImageView) findViewById(R.id.addPackageProfile);
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

    private void uploadImage(String id, Context context)
    {
        if (filePath != null) {
            StorageReference ref = storageReference.child("images/"+id);
            addbutton.setEnabled(false);
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
                                    updateProfilePic(String.valueOf(downloadUrl), id, context);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e)
                                {
                                    addbutton.setEnabled(true);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            addbutton.setEnabled(true);
                        }
                    });
        }
    }

    private void updateProfilePic(String profilePicUrl, String id, Context context) {
        Retrofit retrofitClient = RetrofitClient.getInstance();
        IMyService iMyService = retrofitClient.create(IMyService.class);

        addbutton.setEnabled(false);

        Call<String> call = iMyService.updatePackage(id,"picurl",profilePicUrl);

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
                                Toast.makeText(getApplicationContext(), "Package Addition Process Completed",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(getApplicationContext(), obj.getString("msg"),Toast.LENGTH_SHORT).show();
                            }
                            addbutton.setEnabled(true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "ERROR WHILE CONNECTING TO SERVER",Toast.LENGTH_SHORT).show();
                    }
                }
                addbutton.setEnabled(true);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "ERROR WHILE CONNECTING TO SERVER",Toast.LENGTH_SHORT).show();
                addbutton.setEnabled(true);
            }
        });
    }

    public void resetPackagePic(View view) {
        ImageView imageView2 = (ImageView) findViewById(R.id.addPackageProfile);
        filePath=null;
        profilePath = "default";
        imageView2.setImageResource(R.drawable.ic_bpackage);
    }
}