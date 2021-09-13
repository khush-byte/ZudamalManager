package com.ebookfrenzy.zudamalmanager.adapters;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ebookfrenzy.zudamalmanager.R;
import com.ebookfrenzy.zudamalmanager.tools.MyData;
import java.util.ArrayList;

public class CountAdapter extends RecyclerView.Adapter<CountAdapter.ViewHolder> {
    public ArrayList<MyData> massive;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text_date, textUser, textSum, textType;

        public ViewHolder(View view) {
            super(view);
            text_date = view.findViewById(R.id.textDate);
            textUser = view.findViewById(R.id.textUser);
            textSum = view.findViewById(R.id.textSum);
            textType = view.findViewById(R.id.textType);
        }
    }

    @Override
    public int getItemCount() {
        return massive.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.turn_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        viewHolder.text_date.setText(massive.get(i).date_time);
        viewHolder.textUser.setText(massive.get(i).name);
        viewHolder.textSum.setText(massive.get(i).money);

        switch (massive.get(i).type) {
            case 4:
                viewHolder.textType.setText("Овер.");
                viewHolder.textType.setTextColor(Color.parseColor("#e01626"));
                viewHolder.text_date.setTextColor(Color.parseColor("#e01626"));
                viewHolder.textUser.setTextColor(Color.parseColor("#e01626"));
                viewHolder.textSum.setTextColor(Color.parseColor("#e01626"));
                break;
            case 0: case 1:
                viewHolder.textType.setText("Добав.нал.");
                viewHolder.textType.setTextColor(Color.parseColor("#036308"));
                viewHolder.text_date.setTextColor(Color.parseColor("#036308"));
                viewHolder.textUser.setTextColor(Color.parseColor("#036308"));
                viewHolder.textSum.setTextColor(Color.parseColor("#036308"));
                break;
            case 3:
                viewHolder.textType.setText("Снято");
                viewHolder.textType.setTextColor(Color.parseColor("#1814e3"));
                viewHolder.text_date.setTextColor(Color.parseColor("#1814e3"));
                viewHolder.textUser.setTextColor(Color.parseColor("#1814e3"));
                viewHolder.textSum.setTextColor(Color.parseColor("#1814e3"));
                break;
            case 2:
                viewHolder.textType.setText("Вознаг.");
                viewHolder.textType.setTextColor(Color.DKGRAY);
                viewHolder.text_date.setTextColor(Color.DKGRAY);
                viewHolder.textUser.setTextColor(Color.DKGRAY);
                viewHolder.textSum.setTextColor(Color.DKGRAY);
                break;
            case 7:
                viewHolder.textType.setText("Погаш.овер.");
                viewHolder.textType.setTextColor(Color.parseColor("#045e88"));
                viewHolder.text_date.setTextColor(Color.parseColor("#045e88"));
                viewHolder.textUser.setTextColor(Color.parseColor("#045e88"));
                viewHolder.textSum.setTextColor(Color.parseColor("#045e88"));
                break;
            case 5:
                viewHolder.textType.setText("Возврат");
                viewHolder.textType.setTextColor(Color.parseColor("#7d0e0e"));
                viewHolder.text_date.setTextColor(Color.parseColor("#7d0e0e"));
                viewHolder.textUser.setTextColor(Color.parseColor("#7d0e0e"));
                viewHolder.textSum.setTextColor(Color.parseColor("#7d0e0e"));
                break;
            default:
                viewHolder.textType.setTextColor(Color.DKGRAY);
                viewHolder.text_date.setTextColor(Color.DKGRAY);
                viewHolder.textUser.setTextColor(Color.DKGRAY);
                viewHolder.textSum.setTextColor(Color.DKGRAY);
        }
    }
}