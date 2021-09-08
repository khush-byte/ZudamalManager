package com.ebookfrenzy.zudamalmanager.request;

import static com.ebookfrenzy.zudamalmanager.tools.MyData.myUrl;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.ebookfrenzy.zudamalmanager.FirstFragment;
import com.ebookfrenzy.zudamalmanager.tools.HTTPHandler;

public class GetBalanceRequestUpdate extends AsyncTask<Void, Void, Void> {
    private String myResponse, user_name, hash;
    Context context;
    FirstFragment activity;

    public GetBalanceRequestUpdate(Context context, FirstFragment activity) {
        this.context = context;
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        SharedPreferences pref = context.getSharedPreferences("root_manager", 0);
        user_name =  pref.getString("login", "");
        hash = pref.getString("hash", "");
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        HTTPHandler handler = new HTTPHandler();
        myResponse = handler.makeServiceCall(myUrl + "auth.aspx?act=1&un=" + user_name + "&hc=" + hash);
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        if (myResponse != null) {
            try {
                if (!myResponse.substring(0, 1).equals("#")) {
                    Log.e("Debug", myResponse);

                    String[] DelStr = myResponse.trim().split(" ");
                    Log.e("Debug", DelStr[0]);

                    SharedPreferences.Editor editor = context.getSharedPreferences("root_manager", 0).edit();
                    editor.putString("balance", DelStr[0].substring(0,DelStr[0].length()-1).replace(",","."));
                    editor.putString("overdraft", DelStr[1].replace(",","."));
                    editor.apply();

                    activity.setBalance();
                    activity.binding.pbBalance.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                Log.e("Debug", String.valueOf(e));
            }
        }
    }
}
