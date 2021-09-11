package com.ebookfrenzy.zudamalmanager.request;

import static com.ebookfrenzy.zudamalmanager.tools.MyData.myUrl;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.ebookfrenzy.zudamalmanager.AgentsFragment;
import com.ebookfrenzy.zudamalmanager.adapters.AgentAdapter;
import com.ebookfrenzy.zudamalmanager.adapters.PointAdapter;
import com.ebookfrenzy.zudamalmanager.tools.HTTPHandler;
import com.ebookfrenzy.zudamalmanager.tools.MyData;
import java.util.ArrayList;
import java.util.List;

public class AgentRequest extends AsyncTask<Void, Void, Void> {
    private String myResponse, user_name, hash;
    AgentsFragment activity;
    public String[] massive = {};
    List<MyData> massive_final = new ArrayList<>();

    public AgentRequest(AgentsFragment activity) {
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
            if(!myResponse.substring(0,1).equals("#")) {
                myResponse = myResponse.substring(0, myResponse.length()-2);
                massive = myResponse.split(";");
                //Log.e("Debug", massive[0]);
                for(int i = 0; i < massive.length; i++){
                    String[] mid = massive[i].split("&");

                    if(mid[3].equals("0")){
                        MyData data = new MyData();
                        String[] b = mid[0].split(" - ");
                        data.name = b[1];
                        data.login = mid[1];
                        String[] a = mid[2].split(" ");
                        data.balance = a[0].substring(0, a[0].length()-1).replace(",",".");
                        data.overdraft = a[1].replace(",",".");
                        data.typeID = mid[3];
                        data.id = mid[4];
                        massive_final.add(data);
                    }
                }

                LinearLayoutManager layoutManager = new LinearLayoutManager(activity.getContext(), LinearLayoutManager.VERTICAL, false);
                activity.binding.agentList.setLayoutManager(layoutManager);
                AgentAdapter adapter = new AgentAdapter();

                adapter.massive = massive_final;
                activity.binding.agentList.setAdapter(adapter);
            }
        }
    }
}
