package xyz.shaohui.sicilly.ui.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import xyz.shaohui.sicilly.R;

public class UserPhotoFragment extends Fragment {

    private String userId;

    public static UserPhotoFragment newInstance(String userId) {
        UserPhotoFragment fragment = new UserPhotoFragment();
        Bundle args = new Bundle();
        args.putString("user_id", userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String userId = getArguments().getString("user_id");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_user_photo, container, false);
        return v;
    }




}
