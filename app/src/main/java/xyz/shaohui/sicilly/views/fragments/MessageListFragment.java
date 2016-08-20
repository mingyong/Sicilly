package xyz.shaohui.sicilly.views.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.shaohui.vistarecyclerview.VistaRecyclerView;
import xyz.shaohui.sicilly.R;

public class MessageListFragment extends Fragment {

    @BindView(R.id.recycler)VistaRecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_message_list, container, false);
        ButterKnife.bind(this, v);
        return v;
    }
}
