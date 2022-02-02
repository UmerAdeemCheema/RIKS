package com.ua.riks;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.Map;

public class customerRegisteredPackagesAdapter extends BaseAdapter {
    private final ArrayList mData;

    public customerRegisteredPackagesAdapter(Map<String, Object> map) {
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
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_registered_packages_listitem, parent, false);
        } else {
            result = convertView;
        }

        Map.Entry<String, Object> item = getItem(position);
        LinkedTreeMap jsonvalue = (LinkedTreeMap) item.getValue();

//        try {
//            JSONObject obj = new JSONObject(a);
//            JSONObject objpackage = new JSONObject(obj.getString(jsonkey));
//
            String registeredPackageId = (String) jsonvalue.get("registeredPackageId");
            String packagename = (String) jsonvalue.get("packagename");
            String packageId = (String) jsonvalue.get("packageId");
        String description = (String) jsonvalue.get("description");
            String picurl = (String) jsonvalue.get("picurl");

        if(!picurl.equals("default")) {
            new DownloadImageTask((ImageView) result.findViewById(R.id.customerlistitemImg)).execute(picurl);
        }

            ((TextView) result.findViewById(R.id.customerlistitemId)).setText(registeredPackageId);
            ((TextView) result.findViewById(R.id.customerlistitemTitle)).setText(packagename+"("+packageId+")");
        ((TextView) result.findViewById(R.id.customerlistitemDes)).setText(description);

//        }
//        catch (JSONException e) {
//            e.printStackTrace();
//        }

        return result;
    }
}
