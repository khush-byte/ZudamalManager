package com.ebookfrenzy.zudamalmanager;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ebookfrenzy.zudamalmanager.adapters.HistoryAdapter;
import com.ebookfrenzy.zudamalmanager.databinding.FragmentFirstBinding;
import com.ebookfrenzy.zudamalmanager.request.HistoryDayRequest;

public class FirstFragment extends Fragment{
    public FragmentFirstBinding binding;
    public boolean initialized = false;
    public HistoryAdapter adapter;
    public String[] massive = {};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences pref = getContext().getSharedPreferences("root_manager", 0);
        String bal = "TJS " + pref.getString("balance", "").trim();
        String over = "TJS " + pref.getString("overdraft", "").trim();
        binding.balanceText.setText(bal);
        binding.overText.setText(over);

        HistoryDayRequest request = new HistoryDayRequest(this);
        request.execute();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.historyRecycler.setLayoutManager(layoutManager);
        adapter = new HistoryAdapter();

        OnBackPressedCallback callback = new OnBackPressedCallback(true ) {
            @Override
            public void handleOnBackPressed() {
                ((FirstActivity) getActivity()).finish();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void settingsScript(View view){
        NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.settingsFragment);
        ((FirstActivity) getActivity()).setToolbar("Настройки", false);
    }

    public void reportScript(View view){
        NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.reportsFragment);
        ((FirstActivity) getActivity()).setToolbar("Отчёты", false);
    }

    public void agentScript(View view){
        NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.SecondFragment);
        ((FirstActivity) getActivity()).setToolbar("Агенты", false);
    }

    public void pointScript(View view){
        NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.pointsFragment);
        ((FirstActivity) getActivity()).setToolbar("Пункты", false);
    }

    public void terminalScript(View view){
        NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.terminalsFragment);
        ((FirstActivity) getActivity()).setToolbar("Терминалы", false);
    }

    public void paymentScript(View view){
        NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.paymentFragment);
        ((FirstActivity) getActivity()).setToolbar("Платежи", false);
    }
}