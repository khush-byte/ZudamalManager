package com.ebookfrenzy.zudamalmanager;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.ebookfrenzy.zudamalmanager.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment{
    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences pref = getContext().getSharedPreferences("root_manager", 0);
        String bal = "TJS " + pref.getString("balance", "");
        String over = "TJS " + pref.getString("overdraft", "");
        binding.balanceText.setText(bal);
        binding.overText.setText(over);

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