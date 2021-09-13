package com.ebookfrenzy.zudamalmanager.request;

import static java.lang.Double.parseDouble;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.ebookfrenzy.zudamalmanager.PaymentFragment;
import com.ebookfrenzy.zudamalmanager.adapters.CountAdapter;
import com.ebookfrenzy.zudamalmanager.tools.HTTPHandler;
import com.ebookfrenzy.zudamalmanager.tools.MyData;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import fr.arnaudguyon.xmltojsonlib.XmlToJson;

public class CountRequest extends AsyncTask<Void, Void, Void> {
    PaymentFragment fragment;
    String d1, d2;
    int radio;

    public CountRequest(PaymentFragment fragment, String d1, String d2, int radio) {
        this.fragment = fragment;
        this.d1 = d1;
        this.d2 = d2;
        this.radio = radio;
    }

    @SuppressLint("StaticFieldLeak")
    private String myResponse, users;
    double sum = 0.0;
    private final ArrayList<MyData> massive = new ArrayList<>();

    @Override
    protected void onPreExecute() {
        SharedPreferences pref = fragment.getContext().getSharedPreferences("root_manager", 0);
        users = pref.getString("login", "");
        fragment.binding.progressBarCount.setVisibility(View.VISIBLE);
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        HTTPHandler handler = new HTTPHandler();
        String url = "http://79.170.188.130:83/xml/android.aspx?hc=1626934865&un=E31I1yeI&act=10&Enterpass=1234&MachineID=d81b2fb5fb48fc9cc657d03b2c1cfdf7ba5d1914ecf837c59a3dc5c91d198f1&dt1="+d1+"&dt2="+d2+"&pt="+radio+"&agent="+users;
        Log.e("", url);
        myResponse = handler.makeServiceCall(url);
        return null;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onPostExecute(Void result) {
        fragment.binding.progressBarCount.setVisibility(View.GONE);

        if (myResponse != null) {
            Log.e("Debug", myResponse);

            XmlToJson xmlToJson = new XmlToJson.Builder(myResponse).build();
            JSONObject jsonObject = xmlToJson.toJson();
            assert jsonObject != null;
            String jsonString = jsonObject.toString();

            if(!jsonString.equals("{}")){
                try {
                    JSONObject my_json = jsonObject.getJSONObject("r");

                    if(jsonString.contains("[")) {
                        JSONArray array = my_json.getJSONArray("t");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject fromJson = array.getJSONObject(i);
                            MyData data = new MyData();
                            data.name = fromJson.getString("cr");
                            data.date_time = fromJson.getString("d");
                            data.type = fromJson.getInt("t");
                            data.money = fromJson.getString("sm");
                            massive.add(data);
                            sum += parseDouble(fromJson.getString("sm").replace(',', '.'));
                        }

                        LinearLayoutManager layoutManager = new LinearLayoutManager(fragment.getContext(), LinearLayoutManager.VERTICAL, false);
                        fragment.binding.recyclerCount.setLayoutManager(layoutManager);

                        CountAdapter adapter = new CountAdapter();
                        adapter.massive = massive;
                        fragment.binding.recyclerCount.setAdapter(adapter);

                        fragment.binding.countTotal.setText(round(sum, 2) + " сом.");
                    }else{
                        JSONObject my_json2 = my_json.getJSONObject("t");
                        Log.i("Debug2", my_json2.toString());

                        MyData data = new MyData();
                        data.name = my_json2.getString("cr");
                        data.date_time = my_json2.getString("d");
                        data.type = my_json2.getInt("t");
                        data.money = my_json2.getString("sm");
                        massive.add(data);
                        sum += parseDouble(my_json2.getString("sm").replace(',', '.'));
                    }

                    LinearLayoutManager layoutManager = new LinearLayoutManager(fragment.getContext(), LinearLayoutManager.VERTICAL, false);
                    fragment.binding.recyclerCount.setLayoutManager(layoutManager);

                    CountAdapter adapter = new CountAdapter();
                    adapter.massive = massive;
                    fragment.binding.recyclerCount.setAdapter(adapter);

                    fragment.binding.countTotal.setText(round(sum, 2) + " сом.");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                Log.i("Debug2", "Empty Json!");

                MyData data = new MyData();
                data.money = "";
                data.name = "";
                data.date_time = "Нет данных";
                data.type = 9;
                massive.add(data);

                LinearLayoutManager layoutManager = new LinearLayoutManager(fragment.getContext(), LinearLayoutManager.VERTICAL, false);
                fragment.binding.recyclerCount.setLayoutManager(layoutManager);

                CountAdapter adapter = new CountAdapter();
                adapter.massive = massive;
                fragment.binding.recyclerCount.setAdapter(adapter);

                fragment.binding.countTotal.setText("0.0 сом.");
            }
        } else {
            Toast.makeText(fragment.getContext(), "Сервер не отвечает!!", Toast.LENGTH_SHORT).show();
        }
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}

