package com.ua.riks;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.Map;

public class managerCustomersAdapter extends BaseAdapter {
    private final ArrayList mData;

    public managerCustomersAdapter(Map<String, Object> map) {
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
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.manager_customers_listitem, parent, false);
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
            String name = (String) jsonvalue.get("displayname");
            String email = (String) jsonvalue.get("email");
            String cnic = (String) jsonvalue.get("cnic");
            String contactno = (String) jsonvalue.get("contactno");
            String picurl = (String) jsonvalue.get("profilepicname");

            if(!picurl.equals("default")){
                new DownloadImageTask((ImageView) result.findViewById(R.id.managerClistitemImg)).execute(picurl);
            }


            ((TextView) result.findViewById(R.id.managerClistitemDName)).setText(name);
            ((TextView) result.findViewById(R.id.managerClistitemEmail)).setText(email);
        ((TextView) result.findViewById(R.id.managerClistitemCNIC)).setText(cnic);
        ((TextView) result.findViewById(R.id.managerClistitemContactNo)).setText(contactno);

//        }
//        catch (JSONException e) {
//            e.printStackTrace();
//        }

        return result;
    }
}
