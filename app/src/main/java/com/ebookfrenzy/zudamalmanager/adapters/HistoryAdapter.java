package com.ebookfrenzy.zudamalmanager.adapters;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.ebookfrenzy.zudamalmanager.FirstActivity;
import com.ebookfrenzy.zudamalmanager.FirstFragment;
import com.ebookfrenzy.zudamalmanager.R;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    public FirstFragment activity;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView phone_num, data_time, sum;
        ConstraintLayout history_item_box;
        ImageView history_icon;

        public ViewHolder(View view) {
            super(view);
            phone_num = view.findViewById(R.id.history_text);
            data_time = view.findViewById(R.id.data_time);
            history_item_box = view.findViewById(R.id.history_item_box);
            history_icon = view.findViewById(R.id.history_icon);
            sum = view.findViewById(R.id.history_sum);
        }
    }

    @Override
    public int getItemCount() {
        return activity.massive.length;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.history_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        String[] data = activity.massive[i].split("&");
        String[] agent = data[0].split(" ");
        viewHolder.data_time.setText(data[2].substring(11, data[2].length()-3));

        if(data[3].equals("1") || data[3].equals("5")){
            if(!data[4].equals(" "))
            viewHolder.phone_num.setText("Добавлено на баланс агента: "+agent[0]+". "+data[4]);
            else viewHolder.phone_num.setText("Добавлено на баланс агента: "+agent[0]);

            //viewHolder.phone_num.setTextColor(Color.parseColor("#036308"));
            viewHolder.sum.setTextColor(Color.parseColor("#036308"));
            viewHolder.sum.setText(data[1]);
            viewHolder.history_icon.setImageResource(R.drawable.ic_baseline_keyboard_double_arrow_down);
        } else if(data[3].equals("2")) {
            viewHolder.phone_num.setText("Добавлено вознаграждение агенту: " + agent[0]);
            viewHolder.sum.setText(data[1]);
            viewHolder.history_icon.setImageResource(R.drawable.ic_baseline_keyboard_double_arrow_down);
        } else if(data[3].equals("3")) {
            if(!data[4].equals(" "))
            viewHolder.phone_num.setText("Снято с баланса агента: "+agent[0]+". "+data[4]);
            else  viewHolder.phone_num.setText("Снято с баланса агента: "+agent[0]);

            //viewHolder.phone_num.setTextColor(Color.parseColor("#1814e3"));
            viewHolder.sum.setTextColor(Color.parseColor("#1814e3"));
            viewHolder.sum.setText(data[1]);
            viewHolder.history_icon.setImageResource(R.drawable.ic_baseline_keyboard_double_arrow_up);
        } else if(data[3].equals("4")) {
            viewHolder.phone_num.setText("Добавлен овердрафт агенту: " + agent[0]);
            viewHolder.sum.setTextColor(Color.parseColor("#e01626"));
            //viewHolder.phone_num.setTextColor(Color.parseColor("#e01626"));
            viewHolder.sum.setText(data[1]);
            viewHolder.history_icon.setImageResource(R.drawable.ic_baseline_keyboard_double_arrow_down);
        } else if(data[3].equals("6")) {
            viewHolder.phone_num.setText("Погашен овердрафт агента: "+agent[0]);
            viewHolder.sum.setTextColor(Color.parseColor("#045e88"));
            //viewHolder.phone_num.setTextColor(Color.parseColor("#045e88"));
            viewHolder.sum.setText(data[1]);
            viewHolder.history_icon.setImageResource(R.drawable.ic_baseline_keyboard_double_arrow_up);
        }

        ((FirstActivity) activity.getActivity()).getHistory = false;
    }
}