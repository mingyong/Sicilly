package xyz.shaohui.sicilly.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import xyz.shaohui.sicilly.R;

public class UserListFragment extends Fragment {

    @Bind(R.id.swipe_refresh)SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.recycler)RecyclerView recyclerView;

    private String q;

    public static UserListFragment newInstance(String q) {
        UserListFragment fragment = new UserListFragment();
        Bundle args = new Bundle();
        args.putString("q", q);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_list, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

}
