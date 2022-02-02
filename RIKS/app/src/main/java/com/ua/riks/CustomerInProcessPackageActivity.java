package com.ua.riks;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.ua.riks.Retrofit.IMyService;
import com.ua.riks.Retrofit.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CustomerInProcessPackageActivity extends AppCompatActivity {

    ListView packageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_in_process_package);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.customerpackagetoolbar);
        this.setSupportActionBar(myToolbar);

        packageList = (ListView) findViewById(R.id.cInPackagesList);

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
                Intent intent = new Intent(CustomerInProcessPackageActivity.this,ProfileActivity.class);
                CustomerInProcessPackageActivity.this.startActivity(intent);
                CustomerInProcessPackageActivity.this.finish();
                return true;

            case R.id.customer_menu_packages:
                Intent intent1 = new Intent(CustomerInProcessPackageActivity.this,CustomerPackagesActivity.class);
                CustomerInProcessPackageActivity.this.startActivity(intent1);
                CustomerInProcessPackageActivity.this.finish();
                return true;
            case R.id.customer_menu_registered:
                Intent intent2 = new Intent(CustomerInProcessPackageActivity.this,CustomerRegisteredPackagesActivity.class);
                CustomerInProcessPackageActivity.this.startActivity(intent2);
                CustomerInProcessPackageActivity.this.finish();
                return true;

            case R.id.customer_menu_underwork:

                return true;

            default:

                return super.onOptionsItemSelected(item);

        }
    }
    public void loadList(){
        Retrofit retrofitClient = RetrofitClient.getInstance();
        IMyService iMyService = retrofitClient.create(IMyService.class);

        Call<String> call = iMyService.allInProcessPackages(User.id,User.type);

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

                                customerInProcessPackagesAdapter adapter = new customerInProcessPackagesAdapter(result);
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


    public void uploadDocuments(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(CustomerInProcessPackageActivity.this);
        builder.setMessage("Make a PDF in which all required documents are attached and then upload it. If you haven't made such a pdf press cancel.");
        builder.setTitle("Upload Documents");
        builder.setCancelable(false);

        builder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,int which)
                    {
                        upload();
                    }
                });

        builder.setNegativeButton("Cancel",
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

    public void upload(){
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

        galleryIntent.setType("application/pdf");
        startActivityForResult(galleryIntent, 1);
    }
    Uri fileuri = null;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Button btn = (Button) findViewById(R.id.cInPBtn);
            fileuri = data.getData();

            btn.setEnabled(false);
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();

            // Here we are uploading the pdf in firebase storage with the name of current time
            final StorageReference filepath = storageReference.child("files/"+User.id + "." + "pdf");
            filepath.putFile(fileuri).continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return filepath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        // After uploading is done it progress
                        // dialog box will be dismissed
                        Uri uri = task.getResult();
                        String myurl;
                        myurl = uri.toString();

                        Retrofit retrofitClient = RetrofitClient.getInstance();
                        IMyService iMyService = retrofitClient.create(IMyService.class);

                        Call<String> call = iMyService.fileUrlUpdate(User.id,myurl);

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
                                                btn.setEnabled(true);
                                            }
                                            else{
                                                btn.setEnabled(true);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    } else {
                                        Toast.makeText(getApplicationContext(), "ERROR WHILE CONNECTING TO SERVER",Toast.LENGTH_SHORT).show();
                                        btn.setEnabled(true);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Toast.makeText(getApplicationContext(), "ERROR WHILE CONNECTING TO SERVER",Toast.LENGTH_SHORT).show();
                                btn.setEnabled(true);
                            }
                        });

                    } else {
                        Toast.makeText(CustomerInProcessPackageActivity.this, "UploadedFailed", Toast.LENGTH_SHORT).show();
                        btn.setEnabled(true);
                    }
                }
            });
        }
    }

    public void deleteDocument(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CustomerInProcessPackageActivity.this);
        builder.setMessage("Do you want to delete your documents ?");
        builder.setTitle("Upload Documents");
        builder.setCancelable(false);

        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,int which)
                    {
                        delete();
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
    public void delete(){


        Retrofit retrofitClient = RetrofitClient.getInstance();
        IMyService iMyService = retrofitClient.create(IMyService.class);
        Call<String> call = iMyService.fileUrlUpdate(User.id,"");

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
                                if(obj.getString("fileUrl").equals("")){

                                }
                                else{
                                    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

                                    StorageReference photoRef = firebaseStorage.getReference("files/"+User.id + "." + "pdf");

                                    photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(CustomerInProcessPackageActivity.this, "File Deleted", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            Toast.makeText(CustomerInProcessPackageActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }


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

    DownloadManager manager;

    public void downloadFile(View view) {

        Retrofit retrofitClient = RetrofitClient.getInstance();
        IMyService iMyService = retrofitClient.create(IMyService.class);

        Call<String> call = iMyService.getfileUrl(User.id);

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
                                if(obj.getString("fileUrl").equals("")){
                                    Toast.makeText(getApplicationContext(), "No File Uploaded",Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(getApplicationContext(), "Download Completed",Toast.LENGTH_SHORT).show();
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(obj.getString("fileUrl")));
                                    startActivity(browserIntent);
                                }
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