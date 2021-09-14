package com.ebookfrenzy.zudamalmanager.request;

import static com.ebookfrenzy.zudamalmanager.tools.MyData.myUrl;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.ebookfrenzy.zudamalmanager.AgentsFragment;
import com.ebookfrenzy.zudamalmanager.adapters.AgentAdapter;
import com.ebookfrenzy.zudamalmanager.tools.HTTPHandler;
import com.ebookfrenzy.zudamalmanager.tools.MyData;

import java.util.ArrayList;
import java.util.List;

public class PayRequest extends AsyncTask<Void, Void, Void> {
    private String myResponse, user_name, hash;
    AgentsFragment activity;

    public PayRequest(AgentsFragment activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        SharedPreferences pref = activity.getContext().getSharedPreferences("root_manager", 0);
        user_name =  pref.getString("login", "");
        hash = pref.getString("hash", "");
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        HTTPHandler handler = new HTTPHandler();
        myResponse = handler.makeServiceCall(myUrl + "agents.aspx?act=2&un="+user_name+"&hc="+hash);
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        if (myResponse != null) {
            Log.i("Hello", myResponse);
        }
    }
}
