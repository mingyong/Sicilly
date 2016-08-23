package xyz.shaohui.sicilly.views.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.fragmentargs.FragmentArgs;

import butterknife.ButterKnife;
import butterknife.OnClick;
import xyz.shaohui.sicilly.R;

public class IndexFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_index, container, false);
        ButterKnife.bind(this, v);
        showMainFrame();
        return v;
    }

    private void showMainFrame() {
        getChildFragmentManager().beginTransaction()
                .replace(R.id.main_frame, TimelineFragment.newInstance(TimelineFragment.ACTION_INDEX))
                .commit();
    }

    @OnClick(R.id.btn_add)
    void createStatus() {

    }

    @OnClick(R.id.btn_search)
    void actionSearch() {

    }

    @OnClick(R.id.img_icon)
    void scrollTop() {

    }

}
