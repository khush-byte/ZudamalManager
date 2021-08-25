package com.ebookfrenzy.zudamalmanager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.ebookfrenzy.zudamalmanager.databinding.ActivityFirstBinding;

public class FirstActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    private ActivityFirstBinding binding;
    private boolean isMenuPressed = false;
    boolean isAnimEnd = true;
    public NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFirstBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        binding.toobarBack.setVisibility(View.GONE);
        binding.mainMenu.setVisibility(View.GONE);
        binding.constraintLayout4.setVisibility(View.INVISIBLE);
        binding.constraintLayout5.setVisibility(View.INVISIBLE);
        binding.constraintLayout6.setVisibility(View.INVISIBLE);
        setSupportActionBar(binding.toolbar);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_first);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.toolbarMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isMenuPressed && isAnimEnd){
                    binding.toolbarMenu.setImageResource(R.drawable.ic_baseline_menu_open);
                    binding.mainMenu.setVisibility(View.VISIBLE);
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.menu_on);
                    binding.mainMenu.startAnimation(animation);
                    isAnimEnd = false;


                            new CountDownTimer(30, 30) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                }

                                @Override
                                public void onFinish() {
                                    binding.constraintLayout6.setVisibility(View.VISIBLE);
                                    Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                                    binding.constraintLayout6.startAnimation(animation1);
                                    binding.divider5.setVisibility(View.VISIBLE);

                                    new CountDownTimer(30, 30) {
                                        @Override
                                        public void onTick(long millisUntilFinished) {
                                        }

                                        @Override
                                        public void onFinish() {
                                            binding.constraintLayout5.setVisibility(View.VISIBLE);
                                            Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                                            binding.constraintLayout5.startAnimation(animation1);

                                            new CountDownTimer(30, 30) {
                                                @Override
                                                public void onTick(long millisUntilFinished) {
                                                }

                                                @Override
                                                public void onFinish() {
                                                    binding.constraintLayout4.setVisibility(View.VISIBLE);
                                                    Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                                                    binding.constraintLayout4.startAnimation(animation1);
                                                    isMenuPressed = true;
                                                    isAnimEnd = true;

                                                    binding.fieldMenuClose.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            if(isAnimEnd) {
                                                                binding.toolbarMenu.setImageResource(R.drawable.ic_baseline_menu);

                                                                binding.constraintLayout4.setVisibility(View.INVISIBLE);
                                                                binding.constraintLayout5.setVisibility(View.INVISIBLE);
                                                                binding.constraintLayout6.setVisibility(View.INVISIBLE);

                                                                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.menu_off);
                                                                binding.mainMenu.startAnimation(animation);
                                                                binding.mainMenu.setVisibility(View.GONE);
                                                                binding.divider5.setVisibility(View.GONE);
                                                                isMenuPressed = false;
                                                            }
                                                        }
                                                    });
                                                }
                                            }.start();
                                        }
                                    }.start();
                                }
                            }.start();

                }else if(isMenuPressed && isAnimEnd)
                {
                    binding.toolbarMenu.setImageResource(R.drawable.ic_baseline_menu);
                    binding.constraintLayout4.setVisibility(View.INVISIBLE);
                    binding.constraintLayout5.setVisibility(View.INVISIBLE);
                    binding.constraintLayout6.setVisibility(View.INVISIBLE);

                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.menu_off);
                    binding.mainMenu.startAnimation(animation);
                    binding.mainMenu.setVisibility(View.GONE);
                    binding.divider5.setVisibility(View.GONE);
                    isMenuPressed = false;
                }
            }
        });

        binding.toobarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Debug", String.valueOf(navController.getCurrentDestination().getLabel()));

                if(String.valueOf(navController.getCurrentDestination().getLabel()).equals("TODO")) {
                    navController.navigate(R.id.FirstFragment);
                    setToolbar("TODO", false);
                }
                else {
                        navController.navigate(R.id.FirstFragment);
                        setToolbar(String.valueOf(navController.getCurrentDestination().getLabel()), true);
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_first);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void setToolbar(String title, boolean isLogo){
        binding.toolbarTitle.setText(title);
        closeMenu();

        if(isLogo){
            binding.toobarBack.setVisibility(View.GONE);
            binding.toolbarImage.setVisibility(View.VISIBLE);
        }else{
            binding.toobarBack.setVisibility(View.VISIBLE);
            binding.toolbarImage.setVisibility(View.GONE);
        }
    }

    private void closeMenu(){
        if(isMenuPressed) {
            binding.toolbarMenu.setImageResource(R.drawable.ic_baseline_menu);
            binding.constraintLayout4.setVisibility(View.INVISIBLE);
            binding.constraintLayout5.setVisibility(View.INVISIBLE);
            binding.constraintLayout6.setVisibility(View.INVISIBLE);

            isMenuPressed = false;
            binding.mainMenu.setVisibility(View.GONE);
        }
    }

    public void settingsScript(View view){
        setToolbar("Настройки", false);
        Navigation.findNavController(this, R.id.nav_host_fragment_content_first).navigate(R.id.settingsFragment);
    }

    public void reportScript(View view){
        Log.i("Debug", "Report");
        binding.toolbarTitle.setText("Report");
    }

    public void agentScript(View view){
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") final View popupView = inflater.inflate(R.layout.popup_exit, null);

        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

        Button no_exit_btn = popupView.findViewById(R.id.no_exit_btn);
        Button yes_exit_btn = popupView.findViewById(R.id.yes_exit_btn);

        // show the popup window
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        no_exit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        yes_exit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("root_manager", 0).edit();
                editor.putString("login", "");
                editor.putString("pin", "");
                editor.apply();

                popupWindow.dismiss();

                Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(myIntent);
                finish();
            }
        });
    }

    public void pointScript(View view){
        setToolbar("Пункты", false);
        Navigation.findNavController(this, R.id.nav_host_fragment_content_first).navigate(R.id.pointsFragment);
    }

    public void terminalScript(View view){
        Log.i("Debug", "Terminal");
        binding.toolbarTitle.setText("Terminal");
    }

    public void paymentScript(View view){
        Log.i("Debug", "Payment");
        binding.toolbarTitle.setText("Payment");
    }
}