package com.marm4.electric_wave.ui.main.components;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.marm4.electric_wave.Adapter.NotificationsRecyclerViewAdapter;
import com.marm4.electric_wave.Interface.OnProfilePictureCompleteListener;
import com.marm4.electric_wave.R;
import com.marm4.electric_wave.controller.AuthController;
import com.marm4.electric_wave.global.CurrentAdapters;
import com.marm4.electric_wave.global.CurrentChat;
import com.marm4.electric_wave.global.CurrentUser;
import com.marm4.electric_wave.model.User;
import com.marm4.electric_wave.utility.DownloaderUtility;
import com.marm4.electric_wave.utility.ImageUtility;

import java.io.File;
import java.util.List;

public class ProfileFragment extends Fragment {

    private View root;
    private ImageView profileIV;
    private TextView userNameTV;
    private TextView singOutTV;
    private final int PICK_IMAGE_REQUEST = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_profile, container, false);
        initUI();
        return root;
    }

    private void initUI() {
        Log.i("ProfileFragment", "InitUI");
        profileIV = root.findViewById(R.id.profilePicture);
        userNameTV = root.findViewById(R.id.userName);
        singOutTV = root.findViewById(R.id.singOut);
        userNameTV.setText(CurrentUser.getInstance().getUser().getUserName());

        Uri profilePicture = CurrentUser.getInstance().getUser().getProfilePicture();
        if(profilePicture!=null)
            profileIV.setImageURI(profilePicture);

        singOutTV.setOnClickListener(view -> {
            singOut();
        });

        showNotificacionts();
        changeProfilePicture();
    }



    private void showNotificacionts() {
        List<User> requestList = CurrentUser.getInstance().getRequestList();

        if (requestList != null && !requestList.isEmpty()){
            Log.i("ProfileFragment", "Creating RV for notifications");
            RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
            NotificationsRecyclerViewAdapter adapter = new NotificationsRecyclerViewAdapter(CurrentUser.getInstance().getRequestList());
            recyclerView.setAdapter(adapter);
        }
    }

    private void changeProfilePicture() {
        profileIV.setOnClickListener(view -> {
            Log.i("ProfileFragment", "Changing profile picture");
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            View constraint = root.findViewById(R.id.constraint);
            constraint.setVisibility(View.VISIBLE);
            BottomNavigationView topNavView = getActivity().findViewById(R.id.topNavView);
            topNavView.setVisibility(View.INVISIBLE);
            Uri uriReduce = ImageUtility.reduceSize(getContext(), data.getData(), 500);
            AuthController authController = new AuthController(getContext());
            authController.changeProfilePicture(uriReduce, new OnProfilePictureCompleteListener() {
                @Override
                public void onProfilePictureCompleteListener() {
                    File file = new File(getContext().getExternalFilesDir(null), "Electric-Wave-PP");
                    DownloaderUtility.setProfilePicture(CurrentUser.getInstance().getUser(), file, getContext(), new OnProfilePictureCompleteListener() {
                        @Override
                        public void onProfilePictureCompleteListener() {
                            Log.i("ProfileFragment", "Success changing profile picture");
                            profileIV.setImageURI(CurrentUser.getInstance().getUser().getProfilePicture());
                            constraint.setVisibility(View.GONE);
                            topNavView.setVisibility(View.VISIBLE);
                        }
                    });
                }
            });
        }
    }

    private void singOut() {
        FirebaseAuth.getInstance().signOut();
        CurrentUser.getInstance().destroy();
        CurrentChat.getInstance().destroy();
        CurrentAdapters.getInstance().destroy();
        getActivity().finish();
    }
}
