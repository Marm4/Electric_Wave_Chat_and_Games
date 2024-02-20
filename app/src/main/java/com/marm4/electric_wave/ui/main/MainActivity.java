package com.marm4.electric_wave.ui.main;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
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
import com.marm4.electric_wave.model.User;
import com.marm4.electric_wave.ui.auth.LogInActivity;
import com.marm4.electric_wave.controller.AuthController;
import com.marm4.electric_wave.ui.main.components.GroupChatsFragment;
import com.marm4.electric_wave.utility.DownloaderUtility;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView topNavView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(CurrentUser.getInstance().getUser() == null)
            checkAuth();
        else
            initUI();
    }

    private void checkAuth() {
        AuthController authController = new AuthController(this);

        if (!authController.isUserLoggedIn()) {
            Intent intent = new Intent(MainActivity.this, LogInActivity.class);
            startActivity(intent);
        } else {
            authController.loadCurrentUser(new OnLoadCurrentUserCompleteListener() {
                @Override
                public void onLoadCurrentUserComplete(Boolean load) {
                    authController.setAuthStateListener();
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
                                File fileCurrentUser = new File(getApplicationContext().getExternalFilesDir(null), "Electric-Wave-PP");
                                DownloaderUtility.setProfilePicture(CurrentUser.getInstance().getUser(), fileCurrentUser, getApplicationContext(), null);
                                for(User user : CurrentUser.getInstance().getFriendList()){
                                    File file = new File(getApplicationContext().getExternalFilesDir(null), "Electric-Wave-PP");
                                    DownloaderUtility.setProfilePicture(user, file, getApplicationContext(), null);
                                }
                                initUI();
                            }
                        });
                    }
                });

            }
        });
    }
}