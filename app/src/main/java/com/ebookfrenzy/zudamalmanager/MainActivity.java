package com.ebookfrenzy.zudamalmanager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.Toast;
import com.ebookfrenzy.zudamalmanager.databinding.ActivityMainBinding;
import com.ebookfrenzy.zudamalmanager.databinding.SplashScreenBinding;
import com.ebookfrenzy.zudamalmanager.databinding.UserRegBinding;
import com.ebookfrenzy.zudamalmanager.request.GetBalanceRequest;
import com.ebookfrenzy.zudamalmanager.request.RequestAuth;
import com.ebookfrenzy.zudamalmanager.tools.NetworkManager;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private SplashScreenBinding splash;
    public UserRegBinding reg;
    boolean isFingerChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        splash = SplashScreenBinding.inflate(getLayoutInflater());
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        reg = UserRegBinding.inflate(getLayoutInflater());
        final View[] view = {splash.getRoot()};
        setContentView(view[0]);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("root_manager", 0);
        String myPin = pref.getString("pin", "");
        boolean isFingerprintActive = pref.getBoolean("fingerCheck", false);

        //--animation logo start
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
        splash.mainLogo.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationStart(Animation arg0) {
                ObjectAnimator anim = (ObjectAnimator) AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.flipping);
                anim.setTarget(splash.mainLogo);
                anim.start();
            }
            @Override
            public void onAnimationRepeat(Animation arg0) {
            }
            @Override
            public void onAnimationEnd(Animation arg0) {
                splash.mainLogo.setVisibility(View.GONE);
            }
        });

        new CountDownTimer(1200, 1000)
        {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                if(myPin.length()<4){
                    view[0] = reg.getRoot();
                    RegActivity();
                }else {
                    view[0] = binding.getRoot();

                    SharedPreferences pref = getApplicationContext().getSharedPreferences("root_manager", 0);
                    if (NetworkManager.isNetworkAvailable(getApplicationContext())) {
                        GetBalanceRequest request = new GetBalanceRequest(pref.getString("login", ""), pref.getString("sign", ""), getApplicationContext());
                        request.execute();
                    }else{
                        Toast.makeText(getBaseContext(), "Нет подключения к интернету!", Toast.LENGTH_SHORT).show();
                    }
                }
                setContentView(view[0]);
            }
        }.start();
        //--animation logo end

        binding.pinField.setVisibility(View.GONE);
        binding.fingerImageBtn.setVisibility(View.GONE);
        reg.regLoginField.setVisibility(View.GONE);
        reg.regSignField.setVisibility(View.GONE);
        reg.regPinField.setVisibility(View.GONE);
        reg.regBtn.setVisibility(View.GONE);
        reg.regFingerState.setVisibility(View.GONE);

        Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        binding.logoPin.startAnimation(animation2);
        animation2.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationStart(Animation arg0) {
            }
            @Override
            public void onAnimationRepeat(Animation arg0) {
            }
            @Override
            public void onAnimationEnd(Animation arg0) {
                binding.pinField.setVisibility(View.VISIBLE);
                Animation animation3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                binding.pinField.startAnimation(animation3);

                new CountDownTimer(150, 100)
                {
                    @Override
                    public void onTick(long millisUntilFinished) {
                    }

                    @Override
                    public void onFinish() {
                        if(!isFingerprintActive) {
                            binding.pinField.requestFocus();
                            InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                        }

                        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
                            checkBiometricSupport();
                            if(isFingerprintActive){
                                authenticateUser();
                            }
                        }
                    }
                }.start();
            }
        });

        binding.pinText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if(binding.pinText.length()==4){
                    if(binding.pinText.getText().toString().equals(myPin)){
                        Intent myIntent = new Intent(getApplicationContext(), FirstActivity.class);
                        startActivity(myIntent);
                        finish();
                    }
                    else{
                        Toast.makeText(getBaseContext(), "ПИН код не совпадает!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                binding.pinText.setTextSize(22);
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        binding.fingerImageBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View v) {
                authenticateUser();
            }
        });
    }

    private Boolean checkBiometricSupport(){
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        PackageManager packageManager = this.getPackageManager();

        if(!keyguardManager.isKeyguardSecure()){
            notifyUser("Lock screen security not enabled in Settings");

            return false;
        }

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_BIOMETRIC) != PackageManager.PERMISSION_GRANTED){
            //notifyUser("Fingerprint authentication permission not enabled");
            return false;
        }

        if(packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)){
            return true;
        }

        return true;
    }

    private void notifyUser(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private BiometricPrompt.AuthenticationCallback getAuthenticationCallback(){
        return new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString){
                //notifyUser("Ошибка авторизации: " + errString);
                notifyUser("Отмена авторизации");
                binding.fingerImageBtn.setVisibility(View.VISIBLE);
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationHelp(int helpCode, CharSequence helpString){
                super.onAuthenticationHelp(helpCode, helpString);
            }

            @Override
            public void onAuthenticationFailed(){
                super.onAuthenticationFailed();
            }

            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result){
                //notifyUser("Authentication Succeeded");

                Intent myIntent = new Intent(getApplicationContext(), FirstActivity.class);
                startActivity(myIntent);
                finish();

                super.onAuthenticationSucceeded(result);
            }
        };
    }

    private CancellationSignal getCancellationSignal(){
        CancellationSignal cancellationSignal = new CancellationSignal();
        cancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener() {
            @Override
            public void onCancel() {
                notifyUser("Cancelled via signal");
            }
        });
        return cancellationSignal;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void authenticateUser(){
        BiometricPrompt biometricPrompt = new BiometricPrompt.Builder(this)
                .setTitle("Авторизация")
                .setDescription("Для входа коснитесь датчика отпечатка пальца.")
                .setNegativeButton("Отмена", this.getMainExecutor(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        notifyUser("Отмена авторизации");
                        binding.fingerImageBtn.setVisibility(View.VISIBLE);
                    }
                })
                .build();

        biometricPrompt.authenticate(getCancellationSignal(), getMainExecutor(), getAuthenticationCallback());
    }

    private void RegActivity(){
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        reg.regLogoPin.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationStart(Animation arg0) {
            }
            @Override
            public void onAnimationRepeat(Animation arg0) {
            }
            @Override
            public void onAnimationEnd(Animation arg0) {
                reg.regLoginField.setVisibility(View.VISIBLE);
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                reg.regLoginField.startAnimation(animation1);

                new CountDownTimer(150, 100) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                    }

                    @Override
                    public void onFinish() {
                        reg.regSignField.setVisibility(View.VISIBLE);
                        Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                        reg.regSignField.startAnimation(animation2);

                        new CountDownTimer(150, 100) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                            }

                            @Override
                            public void onFinish() {
                                reg.regPinField.setVisibility(View.VISIBLE);
                                Animation animation3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                                reg.regPinField.startAnimation(animation3);

                                if(checkBiometricSupport()) {
                                    new CountDownTimer(150, 100) {
                                        @Override
                                        public void onTick(long millisUntilFinished) {
                                        }

                                        @Override
                                        public void onFinish() {
                                            reg.regFingerState.setVisibility(View.VISIBLE);
                                            Animation animation4 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                                            reg.regFingerState.startAnimation(animation4);

                                            new CountDownTimer(150, 100) {
                                                @Override
                                                public void onTick(long millisUntilFinished) {
                                                }

                                                @Override
                                                public void onFinish() {
                                                    reg.regBtn.setVisibility(View.VISIBLE);
                                                    Animation animation5 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                                                    reg.regBtn.startAnimation(animation5);
                                                }
                                            }.start();
                                        }
                                    }.start();

                                    reg.regSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                            isFingerChecked = isChecked;
                                        }
                                    });
                                }else{
                                    new CountDownTimer(150, 100) {
                                        @Override
                                        public void onTick(long millisUntilFinished) {
                                        }

                                        @Override
                                        public void onFinish() {
                                            reg.regBtn.setVisibility(View.VISIBLE);
                                            Animation animation5 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                                            reg.regBtn.startAnimation(animation5);
                                        }
                                    }.start();
                                }
                            }
                        }.start();
                    }
                }.start();
            }
        });
    }

    public void userSign(View view){
        if (!reg.regLoginText.getText().toString().equals("") && !reg.regSignText.getText().toString().equals("") && !reg.regPinText.getText().toString().equals("")) {
            if (reg.regPinText.getText().toString().length() != 4) {
                Toast.makeText(getBaseContext(), "ПИН код должен состоять из 4-х цифр!", Toast.LENGTH_LONG).show();
            } else {
                if (NetworkManager.isNetworkAvailable(getApplicationContext())) {
                    RequestAuth request = new RequestAuth(reg.regLoginText.getText().toString(), reg.regSignText.getText().toString(), reg.regPinText.getText().toString(), getApplicationContext(), isFingerChecked, MainActivity.this);
                    request.execute();
                }else{
                    Toast.makeText(getBaseContext(), "Нет подключения к интернету!", Toast.LENGTH_SHORT).show();
                }
            }
        }else{
            Toast.makeText(getBaseContext(), "Введите все данные пожалуйста!", Toast.LENGTH_LONG).show();
        }
    }
}