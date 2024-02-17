package com.marm4.electric_wave.ui.main;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.marm4.electric_wave.Interface.OnFriendRequestCompleteListener;
import com.marm4.electric_wave.Interface.OnGroupChatCompleteListener;
import com.marm4.electric_wave.Interface.OnLoadCurrentUserCompleteListener;
import com.marm4.electric_wave.R;
import com.marm4.electric_wave.controller.FriendController;
import com.marm4.electric_wave.controller.GroupChatController;
import com.marm4.electric_wave.global.CurrentUser;
import com.marm4.electric_wave.ui.auth.LogInActivity;
import com.marm4.electric_wave.controller.AuthController;
import com.marm4.electric_wave.ui.main.components.GroupChatsFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView topNavView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkAuth();
    }

    private void checkAuth() {
        AuthController authController = new AuthController();

        if (!authController.isUserLoggedIn()) {
            Intent intent = new Intent(MainActivity.this, LogInActivity.class);
            startActivity(intent);
        } else {
            authController.loadCurrentUser(new OnLoadCurrentUserCompleteListener() {
                @Override
                public void onLoadCurrentUserComplete(Boolean load) {
                    loadUserData();
                }
            });
        }
    }

    private void initUI() {
        NavController navController = Navigation.findNavController(this, R.id.fragmentContainerView);
        topNavView = findViewById(R.id.topNavView);
        topNavView.setVisibility(View.VISIBLE);
        stopAnimation();
        NavigationUI.setupWithNavController(topNavView, navController);
    }

    private void stopAnimation() {
        MotionLayout motionLayout = findViewById(R.id.motionLayout);
        ConstraintLayout constraint = findViewById(R.id.constraint);
        constraint.setVisibility(View.GONE);
        motionLayout.setVisibility(View.GONE);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        Fragment fragment = navHostFragment.getChildFragmentManager().findFragmentById(R.id.fragmentContainerView);
        fragment.getView().findViewById(R.id.container).setVisibility(View.VISIBLE);
    }

    private void loadUserData() {
        FriendController friendController = new FriendController();
        friendController.getAllFriendRequests(new OnFriendRequestCompleteListener() {
            @Override
            public void onFriendRequestComplete(Boolean friendRequest) {
                friendController.getFriendList(new OnFriendRequestCompleteListener() {
                    @Override
                    public void onFriendRequestComplete(Boolean friendRequest) {
                        GroupChatController groupChatController = new GroupChatController();
                        groupChatController.getAllGroupChat(CurrentUser.getInstance().getFriendList(), new OnGroupChatCompleteListener() {
                            @Override
                            public void onGroupChatComplete() {
                                initUI();
                            }
                        });
                    }
                });

            }
        });
    }


}