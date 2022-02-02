package com.ua.riks;

import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.ua.riks.Retrofit.IMyService;
import com.ua.riks.Retrofit.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class managerPackagesAdapter extends BaseAdapter {
    private final ArrayList mData;

    public managerPackagesAdapter(Map<String, Object> map) {
        mData = new ArrayList();
        mData.addAll(map.entrySet());
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Map.Entry<String, Object> getItem(int position) {
        return (Map.Entry) mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO implement you own logic with ID
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View result;

        if (convertView == null) {
            if(User.type.equals("Customer")){
                result = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_packages_listitem, parent, false);
            }
            else{
                result = LayoutInflater.from(parent.getContext()).inflate(R.layout.manager_packages_listitem, parent, false);
            }
        } else {
            result = convertView;
        }

        Map.Entry<String, Object> item = getItem(position);
        String a = item.toString();
        String jsonkey = item.getKey();
        LinkedTreeMap jsonvalue = (LinkedTreeMap) item.getValue();

//        try {
//            JSONObject obj = new JSONObject(a);
//            JSONObject objpackage = new JSONObject(obj.getString(jsonkey));
//
            String packageId = (String) jsonvalue.get("packageId");
            String packagename = (String) jsonvalue.get("packagename");
            String description = (String) jsonvalue.get("description");
            String picurl = (String) jsonvalue.get("picurl");

        if(!picurl.equals("default")) {
            new DownloadImageTask((ImageView) result.findViewById(R.id.managerlistitemImg)).execute(picurl);
        }
        if(User.type.equals("Customer")){
            ((TextView) result.findViewById(R.id.managerlistitemTitle)).setText(packagename);
            ((TextView) result.findViewById(R.id.managerlistitemID)).setText(packageId);
            ((TextView) result.findViewById(R.id.managerlistitemDes)).setText(description);
            ((ImageView) result.findViewById(R.id.managerlistitemAdd)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());
                    builder.setMessage("Are you Sure, You want to Register for this Package ?");
                    builder.setTitle("Logout");
                    builder.setCancelable(false);

                    builder.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,int which)
                                {


                                    Retrofit retrofitClient = RetrofitClient.getInstance();
                                    IMyService iMyService = retrofitClient.create(IMyService.class);

                                    Call<String> call = iMyService.addinProcessPackage(((TextView) result.findViewById(R.id.managerlistitemID)).getText().toString(), User.id);

                                    call.enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(Call<String> call, Response<String> response) {
                                            //Toast.makeText()
                                            if (response.isSuccessful()) {
                                                if (response.body() != null) {

                                                    String jsonresponse = response.body().toString();

                                                    try {
                                                        JSONObject obj = new JSONObject(jsonresponse);
                                                        Toast.makeText(parent.getContext(), obj.getString("msg"),Toast.LENGTH_SHORT).show();

                                                        if(obj.getBoolean("success")){
                                                        }
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }

                                                } else {
                                                    Toast.makeText(parent.getContext(), "ERROR WHILE CONNECTING TO SERVER",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<String> call, Throwable t) {
                                            Toast.makeText(parent.getContext(), "ERROR WHILE CONNECTING TO SERVER",Toast.LENGTH_SHORT).show();
                                        }
                                    });
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
            });
        }
        else{
            ((TextView) result.findViewById(R.id.managerlistitemTitle)).setText(packagename+"("+packageId+")");
            ((TextView) result.findViewById(R.id.managerlistitemDes)).setText(description);
        }

//        }
//        catch (JSONException e) {
//            e.printStackTrace();
//        }

        return result;
    }
}
