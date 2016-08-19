package xyz.shaohui.sicilly.views.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import xyz.shaohui.sicilly.R;


public class TimelineFragment extends BaseFragment {

    private int action;

    public static final int ACTION_INDEX = 1;

    public static TimelineFragment newInstance(int action) {
        TimelineFragment fragment = new TimelineFragment();
        Bundle args = new Bundle();
        args.putInt("action", action);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        action = getArguments().getInt("action");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_timeline, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @OnClick(R.id.btn_main)
    void mainBtn() {
        Log.i("TAG", "cesh");
        LoginDialogFragment dialogFragment = new LoginDialogFragment();
        dialogFragment.show(getChildFragmentManager(), "login");
    }

    @OnClick(R.id.btn_ceshi)
    void mainCe() {
        Log.i("TAG", "cesh");
        LoginDialogFragment dialogFragment = new LoginDialogFragment();
        dialogFragment.show(getChildFragmentManager(), "login");
    }


}