package com.ua.riks;

import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.gson.internal.LinkedTreeMap;
import com.ua.riks.Retrofit.IMyService;
import com.ua.riks.Retrofit.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class customerInProcessPackagesAdapter extends BaseAdapter {
    private final ArrayList mData;

    public customerInProcessPackagesAdapter(Map<String, Object> map) {
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
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_inprocess_packages_listitem, parent, false);
        } else {
            result = convertView;
        }

        Map.Entry<String, Object> item = getItem(position);
        LinkedTreeMap jsonvalue = (LinkedTreeMap) item.getValue();

//        try {
//            JSONObject obj = new JSONObject(a);
//            JSONObject objpackage = new JSONObject(obj.getString(jsonkey));
//
            String userId = (String) jsonvalue.get("userId");
        String employeeId = (String) jsonvalue.get("employeeId");
            String inprocessPackageId = (String) jsonvalue.get("inprocessPackageId");
            String packagename = (String) jsonvalue.get("packagename");
            String packageId = (String) jsonvalue.get("packageId");
            String picurl = (String) jsonvalue.get("picurl");

        if(!picurl.equals("default")) {
            new DownloadImageTask((ImageView) result.findViewById(R.id.customerlistitemImg)).execute(picurl);
        }

            ((TextView) result.findViewById(R.id.customerlistitemID)).setText(inprocessPackageId);
            ((TextView) result.findViewById(R.id.customerlistitemTitle)).setText(packagename+"("+packageId+")");
            ((LinearLayout) result).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(User.type.equals("Employee")){
                        Intent intent = new Intent(parent.getContext(), EmployeeChatActivity.class);
                        intent.putExtra("employeeId", employeeId);
                        intent.putExtra("userId", userId);
                        intent.putExtra("packageId", inprocessPackageId);
                        parent.getContext().startActivity(intent);
                    }
                    else{
                        Intent intent = new Intent(parent.getContext(), ChatActivity.class);
                        intent.putExtra("userId", userId);
                        parent.getContext().startActivity(intent);
                    }

                }
            });

//        }
//        catch (JSONException e) {
//            e.printStackTrace();
//        }

        return result;
    }
}
