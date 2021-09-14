package com.ebookfrenzy.zudamalmanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ebookfrenzy.zudamalmanager.databinding.FragmentPayBinding;
import com.ebookfrenzy.zudamalmanager.request.GetBalanceRequestUpdate;
import com.ebookfrenzy.zudamalmanager.request.PayRequest;
import com.ebookfrenzy.zudamalmanager.tools.DecimalDigitsInputFilter;

public class PayFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2, agentID;
    FragmentPayBinding binding;

    public PayFragment() {
        // Required empty public constructor
    }

    public static PayFragment newInstance(String param1, String param2) {
        PayFragment fragment = new PayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPayBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.payField.requestFocus();
        binding.payField.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                binding.payField.setTextSize(22);
                binding.payField.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(5,2)});
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
        binding.actionTitle.setText(((FirstActivity) getActivity()).title);

        SharedPreferences pref = getContext().getSharedPreferences("root_manager", 0);
        String agent_info = pref.getString("agent_info", "");
        Log.i("Debug", agent_info);
        String[] text = agent_info.split("&");

        binding.agentIdField.setText("ID"+text[0]);
        binding.agentBalField.setText(text[2]+", "+text[3]);
        agentID = text[0];

        binding.actionPayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.payField.getText().toString().length()>0 && !binding.payField.getText().toString().equals("0")) {
                    /*PayRequest request = new PayRequest(PayFragment.this, binding.payField.getText().toString(), agentID);
                    request.execute();*/
                    Toast.makeText(getContext(), "Приложение не закончено!", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(((FirstActivity) getActivity()), R.id.nav_host_fragment_content_first).navigate(R.id.actionFragment);
                }else{
                    Toast.makeText(getContext(), "Не правильная сумма", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}