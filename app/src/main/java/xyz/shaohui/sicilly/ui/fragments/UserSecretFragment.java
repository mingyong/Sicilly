package xyz.shaohui.sicilly.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import xyz.shaohui.sicilly.R;

/**
 * Created by kpt on 16/3/30.
 */
public class UserSecretFragment extends Fragment {

    public static UserSecretFragment newInstance() {
        UserSecretFragment fragment = new UserSecretFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_secret, container, false);
        return v;
    }


}
