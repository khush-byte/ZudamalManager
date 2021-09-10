package com.ebookfrenzy.zudamalmanager;

import static android.content.Context.KEYGUARD_SERVICE;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import com.ebookfrenzy.zudamalmanager.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public FragmentSettingsBinding binding;

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
        binding = FragmentSettingsBinding.inflate(inflater, container, false);

        SharedPreferences pref = getContext().getSharedPreferences("root_manager", 0);
        binding.regSwitch2.setChecked(pref.getBoolean("fingerCheck", false));

        return binding.getRoot();
    }

    @SuppressLint("NotifyDataSetChanged")
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

        if(!checkBiometricSupport()) {
            binding.regFingerState2.setVisibility(View.GONE);
            binding.dividerFinger.setVisibility(View.GONE);
        }

        binding.regSwitch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = getContext().getSharedPreferences("root_manager", 0).edit();
                editor.putBoolean("fingerCheck", isChecked);
                editor.apply();
            }
        });
    }

    private Boolean checkBiometricSupport(){
        KeyguardManager keyguardManager = (KeyguardManager) ((FirstActivity) getActivity()).getSystemService(KEYGUARD_SERVICE);
        PackageManager packageManager = ((FirstActivity) getActivity()).getPackageManager();

        if(!keyguardManager.isKeyguardSecure()){
            //notifyUser("Lock screen security not enabled in Settings");
            return false;
        }

        if(ActivityCompat.checkSelfPermission(((FirstActivity) getActivity()), Manifest.permission.USE_BIOMETRIC) != PackageManager.PERMISSION_GRANTED){
            //notifyUser("Fingerprint authentication permission not enabled");
            return false;
        }

        packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT);
        return true;
    }
}