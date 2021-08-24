package com.ebookfrenzy.zudamalmanager;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

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
    NavController navController;

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
        binding.constraintLayout7.setVisibility(View.INVISIBLE);
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

                    new CountDownTimer(150, 100) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                        }

                        @Override
                        public void onFinish() {
                            binding.constraintLayout7.setVisibility(View.VISIBLE);
                            Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                            binding.constraintLayout7.startAnimation(animation1);
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
                                                                binding.constraintLayout7.setVisibility(View.INVISIBLE);

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
                        }
                    }.start();
                }else if(isMenuPressed && isAnimEnd)
                {
                    binding.toolbarMenu.setImageResource(R.drawable.ic_baseline_menu);
                    binding.constraintLayout4.setVisibility(View.INVISIBLE);
                    binding.constraintLayout5.setVisibility(View.INVISIBLE);
                    binding.constraintLayout6.setVisibility(View.INVISIBLE);
                    binding.constraintLayout7.setVisibility(View.INVISIBLE);

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
                closeMenu();
                navController.popBackStack();

                if(String.valueOf(navController.getCurrentDestination().getLabel()).equals("Zudamal Manager")){
                    setToolbar(String.valueOf(navController.getCurrentDestination().getLabel()), true);
                }else{
                    setToolbar(String.valueOf(navController.getCurrentDestination().getLabel()), false);
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
            binding.constraintLayout7.setVisibility(View.INVISIBLE);

            isMenuPressed = false;
            binding.mainMenu.setVisibility(View.GONE);
        }
    }
}