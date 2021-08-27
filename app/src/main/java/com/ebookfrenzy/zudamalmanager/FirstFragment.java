package com.ebookfrenzy.zudamalmanager;

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
        /*binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
                ((FirstActivity) getActivity()).setToolbar("Second Fragment", false);
            }
        });*/
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
        ((FirstActivity) getActivity()).setToolbar("Настройки", false);
        NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.settingsFragment);
    }

    public void reportScript(View view){
        ((FirstActivity) getActivity()).setToolbar("Отчёты", false);
        NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.reportsFragment);
    }

    public void agentScript(View view){
        ((FirstActivity) getActivity()).setToolbar("Агенты", false);
        NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.SecondFragment);
    }

    public void pointScript(View view){
        ((FirstActivity) getActivity()).setToolbar("Пункты", false);
        NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.pointsFragment);
    }

    public void terminalScript(View view){
        ((FirstActivity) getActivity()).setToolbar("Терминалы", false);
        NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.terminalsFragment);
    }

    public void paymentScript(View view){
        ((FirstActivity) getActivity()).setToolbar("Платежи", false);
        NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.paymentFragment);
    }
}