package com.ebookfrenzy.zudamalmanager;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ebookfrenzy.zudamalmanager.adapters.HistoryAdapter;
import com.ebookfrenzy.zudamalmanager.databinding.FragmentFirstBinding;
import com.ebookfrenzy.zudamalmanager.request.GetBalanceRequestUpdate;
import com.ebookfrenzy.zudamalmanager.request.HistoryDayRequest;

public class FirstFragment extends Fragment{
    public FragmentFirstBinding binding;
    public HistoryAdapter adapter;
    public String[] massive = {};
    boolean isScrolled = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setBalance();
        binding.mainMenuBtn2.setVisibility(View.GONE);
        binding.pbBalance.setVisibility(View.GONE);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.historyRecycler.setLayoutManager(layoutManager);
        adapter = new HistoryAdapter();

        if(((FirstActivity) getActivity()).getHistory) {
            HistoryDayRequest request = new HistoryDayRequest(this);
            request.execute();
        }else{
            SharedPreferences pref = getContext().getSharedPreferences("root_manager", 0);
            String myResponse =  pref.getString("response", "");

            if(!myResponse.substring(0, 1).equals("#")) {
                myResponse = myResponse.substring(0, myResponse.length() - 2);
                massive = myResponse.split(";");
                adapter.activity = this;
                binding.historyRecycler.setAdapter(adapter);
            }
        }

        binding.historyRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 10 && !isScrolled) {
                    // Scrolling up
                    new CountDownTimer(100, 100)
                    {
                        @Override
                        public void onTick(long millisUntilFinished) {}

                        @Override
                        public void onFinish() {
                            binding.mainMenuBtn.setVisibility(View.GONE);
                            binding.mainMenuBtn2.setVisibility(View.VISIBLE);

                            new CountDownTimer(800, 800)
                            {
                                @Override
                                public void onTick(long millisUntilFinished) {}

                                @Override
                                public void onFinish() {
                                    isScrolled = true;
                                }
                            }.start();
                        }
                    }.start();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
                    if(firstVisiblePosition == 0 && isScrolled){
                        new CountDownTimer(100, 100)
                        {
                            @Override
                            public void onTick(long millisUntilFinished) {}

                            @Override
                            public void onFinish() {
                                binding.mainMenuBtn.setVisibility(View.VISIBLE);
                                binding.mainMenuBtn2.setVisibility(View.GONE);
                                new CountDownTimer(800, 800)
                                {
                                    @Override
                                    public void onTick(long millisUntilFinished) {}

                                    @Override
                                    public void onFinish() {
                                        isScrolled = false;
                                    }
                                }.start();
                            }
                        }.start();
                    }
                } else if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    // Do something
                } else {
                    // Do something
                }

                if (!recyclerView.canScrollVertically(1) && isScrolled) {
                    new CountDownTimer(100, 100)
                    {
                        @Override
                        public void onTick(long millisUntilFinished) {}

                        @Override
                        public void onFinish() {
                            binding.mainMenuBtn.setVisibility(View.GONE);
                            binding.mainMenuBtn2.setVisibility(View.VISIBLE);

                            new CountDownTimer(800, 800)
                            {
                                @Override
                                public void onTick(long millisUntilFinished) {}

                                @Override
                                public void onFinish() {
                                    isScrolled = true;
                                }
                            }.start();
                        }
                    }.start();
                }
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true ) {
            @Override
            public void handleOnBackPressed() {
                ((FirstActivity) getActivity()).finish();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        //balance field
        binding.balanceField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.pbBalance.setVisibility(View.VISIBLE);
                GetBalanceRequestUpdate request = new GetBalanceRequestUpdate(getContext(), FirstFragment.this);
                request.execute();
            }
        });
    }

    public void setBalance(){
        SharedPreferences pref = getContext().getSharedPreferences("root_manager", 0);
        String bal = "TJS " + pref.getString("balance", "").trim();
        String over = "TJS " + pref.getString("overdraft", "").trim();
        binding.balanceText.setText(bal);
        binding.overText.setText(over);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}