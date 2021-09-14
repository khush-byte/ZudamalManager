package com.ebookfrenzy.zudamalmanager.request;

import static com.ebookfrenzy.zudamalmanager.tools.MyData.myUrl;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.ebookfrenzy.zudamalmanager.AgentsFragment;
import com.ebookfrenzy.zudamalmanager.PayFragment;
import com.ebookfrenzy.zudamalmanager.adapters.AgentAdapter;
import com.ebookfrenzy.zudamalmanager.tools.HTTPHandler;
import com.ebookfrenzy.zudamalmanager.tools.MyData;

import java.util.ArrayList;
import java.util.List;

public class PayRequest extends AsyncTask<Void, Void, Void> {
    private String myResponse, user_name, hash, sum, id, password, string_hash;
    PayFragment activity;

    public PayRequest(PayFragment activity, String sum, String id) {
        this.activity = activity;
        this.sum = sum;
        this.id = id;
    }

    @Override
    protected void onPreExecute() {
        SharedPreferences pref = activity.getContext().getSharedPreferences("root_manager", 0);
        user_name =  pref.getString("login", "");
        password = pref.getString("sign", "");
        string_hash = pref.getString("hash", "");
        hash = MD5(user_name+"1"+sum+id+"1"+password);
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        HTTPHandler handler = new HTTPHandler();
        myResponse = handler.makeServiceCall(myUrl + "pay.aspx?act=1&un="+user_name+"&sm="+sum+"&txd=1&aid="+id+"&hc="+hash);
        Log.i("Debug", "pay.aspx?act=1&un="+user_name+"&sm="+sum+"&txd=1&aid="+id+"&hc="+hash);
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        if (myResponse != null) {
            Log.i("Hello", myResponse);
        }
    }

    public String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : array) {
                sb.append(Integer.toHexString((b & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException ignored) {
        }
        return null;
    }
}
