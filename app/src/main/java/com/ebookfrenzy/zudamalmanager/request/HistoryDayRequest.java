package com.ebookfrenzy.zudamalmanager.request;

import static com.ebookfrenzy.zudamalmanager.tools.MyData.myUrl;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import com.ebookfrenzy.zudamalmanager.FirstFragment;
import com.ebookfrenzy.zudamalmanager.tools.HTTPHandler;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HistoryDayRequest extends AsyncTask<Void, Void, Void> {
    private String myResponse, user_name, hash, date;
    FirstFragment activity;

    public HistoryDayRequest(FirstFragment activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        SharedPreferences pref = activity.getContext().getSharedPreferences("root_manager", 0);
        user_name =  pref.getString("login", "");
        hash = pref.getString("hash", "");

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        date = df.format(c);
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        HTTPHandler handler = new HTTPHandler();
        //myResponse = handler.makeServiceCall(myUrl + "report.aspx?act=1&un=" + user_name + "&hc=" + hash + "&aa=1&dt1="+date+"&r=1");
        myResponse = handler.makeServiceCall(myUrl + "report.aspx?act=1&un=" + user_name + "&hc=" + hash + "&aa=1&dt1=2021-09-03&r=1");
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        if (myResponse != null) {
            SharedPreferences.Editor editor = activity.getContext().getSharedPreferences("root_manager", 0).edit();
            editor.putString("response", myResponse);
            editor.apply();

            if(!myResponse.substring(0, 1).equals("#")) {
                myResponse = myResponse.substring(0, myResponse.length() - 2);
                Log.e("Debug", myResponse);
                activity.massive = myResponse.split(";");
                activity.adapter.activity = activity;
                activity.binding.historyRecycler.setAdapter(activity.adapter);
            }
        }
    }
}
