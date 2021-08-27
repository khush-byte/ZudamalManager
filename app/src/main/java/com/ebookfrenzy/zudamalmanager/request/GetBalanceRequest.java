package com.ebookfrenzy.zudamalmanager.request;

import static com.ebookfrenzy.zudamalmanager.tools.MyData.myUrl;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import com.ebookfrenzy.zudamalmanager.tools.HTTPHandler;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class GetBalanceRequest extends AsyncTask<Void, Void, Void> {
    private String myResponse, myPassword, user_name, hash, user;
    Context context;

    public GetBalanceRequest(String user, String myPassword, Context context) {
        this.myPassword = myPassword;
        this.user = user;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        try {
            user_name = URLEncoder.encode(user, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        hash = MD5("sdfSDEKMQ@#!" + user_name + MD5(myPassword));
        hash = hash.substring(8, 24) + hash.substring(0, 8) + hash.substring(24, 32);
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        HTTPHandler handler = new HTTPHandler();
        myResponse = handler.makeServiceCall(myUrl + "auth.aspx?act=1&un=" + user_name + "&hc=" + hash);
        Log.i("Debug", myResponse);
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        if (myResponse != null) {
            try {
                if (!myResponse.substring(0, 1).equals("#")) {
                    Log.e("Debug", myResponse);

                    String[] DelStr = myResponse.trim().split(",");
                    Log.e("Debug", DelStr[0]);

                    SharedPreferences.Editor editor = context.getSharedPreferences("root_manager", 0).edit();
                    editor.putString("balance", DelStr[0]);
                    editor.putString("overdraft", DelStr[1]);
                    editor.apply();
                }
            } catch (Exception e) {
                Log.e("Debug", String.valueOf(e));
            }
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
