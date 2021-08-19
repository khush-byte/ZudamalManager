package com.ebookfrenzy.zudamalmanager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.app.KeyguardManager;
import android.content.DialogInterface;
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
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;
import com.ebookfrenzy.zudamalmanager.databinding.ActivityMainBinding;
import com.ebookfrenzy.zudamalmanager.databinding.SplashScreenBinding;
import com.ebookfrenzy.zudamalmanager.databinding.UserRegBinding;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private SplashScreenBinding splash;
    private UserRegBinding reg;

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
                }
                setContentView(view[0]);
            }
        }.start();
        //--animation logo end

        binding.pinField.setVisibility(View.GONE);
        binding.fingerBtn.setVisibility(View.GONE);
        reg.regLoginField.setVisibility(View.GONE);
        reg.regSignField.setVisibility(View.GONE);
        reg.regPinField.setVisibility(View.GONE);
        reg.regBtn.setVisibility(View.GONE);

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
                new CountDownTimer(150, 100)
                {
                    @Override
                    public void onTick(long millisUntilFinished) {
                    }

                    @Override
                    public void onFinish() {
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
                                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
                                    checkBiometricSupport();
                                    binding.fingerBtn.setVisibility(View.VISIBLE);
                                    Animation animation4 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                                    binding.fingerBtn.startAnimation(animation4);
                                }
                            }
                        }.start();
                    }
                }.start();
            }
        });

        binding.pinText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if(binding.pinText.length()==4){
                    Log.i("Debug", "Good");
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                binding.pinText.setTextSize(22);
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
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
            notifyUser("Fingerprint authentication permission not enabled");
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
                notifyUser("Ошибка авторизации: " + errString);
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
            public void onAuthenticationSucceeded(
                    BiometricPrompt.AuthenticationResult result){ notifyUser("Authentication Succeeded");
                //startActivity(new Intent(MainActivity.this, HelloActivity.class));
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
    public void authenticateUser(View view){
        BiometricPrompt biometricPrompt = new BiometricPrompt.Builder(this)
                .setTitle("Авторизация")
                .setDescription("Для входа коснитесь датчика отпечатка пальца.")
                .setNegativeButton("Отмена", this.getMainExecutor(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        notifyUser("Отмена авторизации");
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
                new CountDownTimer(150, 100)
                {
                    @Override
                    public void onTick(long millisUntilFinished) {
                    }

                    @Override
                    public void onFinish() {
                        reg.regLoginField.setVisibility(View.VISIBLE);
                        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                        reg.regLoginField.startAnimation(animation1);

                        new CountDownTimer(150, 100)
                        {
                            @Override
                            public void onTick(long millisUntilFinished) {
                            }

                            @Override
                            public void onFinish() {
                                reg.regSignField.setVisibility(View.VISIBLE);
                                Animation animation2= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                                reg.regSignField.startAnimation(animation2);

                                new CountDownTimer(150, 100)
                                {
                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                    }

                                    @Override
                                    public void onFinish() {
                                        reg.regPinField.setVisibility(View.VISIBLE);
                                        Animation animation3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                                        reg.regPinField.startAnimation(animation3);

                                        new CountDownTimer(150, 100)
                                        {
                                            @Override
                                            public void onTick(long millisUntilFinished) {
                                            }

                                            @Override
                                            public void onFinish() {
                                                reg.regBtn.setVisibility(View.VISIBLE);
                                                Animation animation4 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                                                reg.regBtn.startAnimation(animation4);
                                            }
                                        }.start();
                                    }
                                }.start();
                            }
                        }.start();
                    }
                }.start();
            }
        });
    }
}