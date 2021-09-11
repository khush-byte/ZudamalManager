package com.ebookfrenzy.zudamalmanager;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ebookfrenzy.zudamalmanager.adapters.AgentAdapter;
import com.ebookfrenzy.zudamalmanager.adapters.HistoryAdapter;
import com.ebookfrenzy.zudamalmanager.databinding.FragmentAgentsBinding;
import com.ebookfrenzy.zudamalmanager.request.AgentRequest;
import com.ebookfrenzy.zudamalmanager.request.HistoryDayRequest;
import com.ebookfrenzy.zudamalmanager.request.RequestAuth;

public class AgentsFragment extends Fragment {
    public FragmentAgentsBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentAgentsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        OnBackPressedCallback callback = new OnBackPressedCallback(true ) {
            @Override
            public void handleOnBackPressed() {
                ((FirstActivity) getActivity()).setToolbar("Zudamal Manager", true);
                ((FirstActivity) getActivity()).navController.navigate(R.id.FirstFragment);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        AgentRequest request = new AgentRequest(this);
        request.execute();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}