package com.ebookfrenzy.zudamalmanager.adapters;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.ebookfrenzy.zudamalmanager.AgentsFragment;
import com.ebookfrenzy.zudamalmanager.FirstActivity;
import com.ebookfrenzy.zudamalmanager.PointsFragment;
import com.ebookfrenzy.zudamalmanager.R;
import com.ebookfrenzy.zudamalmanager.tools.MyData;
import java.util.ArrayList;
import java.util.List;

public class PointAdapter extends RecyclerView.Adapter<PointAdapter.ViewHolder> {
    public List<MyData> massive = new ArrayList<>();
    private PointsFragment fragment;

    public PointAdapter(PointsFragment fragment) {
        this.fragment = fragment;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView agent_text, agent_list_id;
        ConstraintLayout agent_item_box;

        public ViewHolder(View view) {
            super(view);
            agent_text = view.findViewById(R.id.agent_text);
            agent_list_id = view.findViewById(R.id.agent_list_id);
            agent_item_box = view.findViewById(R.id.agent_item_box);
        }
    }

    @Override
    public int getItemCount() {
        return massive.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.agent_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, @SuppressLint("RecyclerView") final int i) {
        if (massive.get(i).typeID.equals("1")) {
            String number = String.format("%d.", i+1);
            String text = String.format("Точка №%s (%s), %s\nбаланс: %s, долг: %s", massive.get(i).id, massive.get(i).login, massive.get(i).name, massive.get(i).balance, massive.get(i).overdraft);
            viewHolder.agent_list_id.setText(number);
            viewHolder.agent_text.setText(text);
        }

        viewHolder.agent_item_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.i("Debug", "Hello "+i);
                String title = String.format("Точка №%s (%s)", massive.get(i).id, massive.get(i).login);
                ((FirstActivity) fragment.getActivity()).setToolbar(title, false);
                Navigation.findNavController(((FirstActivity) fragment.getActivity()), R.id.nav_host_fragment_content_first).navigate(R.id.actionFragment);
            }
        });
    }
}