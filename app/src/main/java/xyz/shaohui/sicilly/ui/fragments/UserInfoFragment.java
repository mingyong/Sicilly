package xyz.shaohui.sicilly.ui.fragments;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.SicillyFactory;
import xyz.shaohui.sicilly.data.models.User;
import xyz.shaohui.sicilly.ui.activities.EditInfoActivity;

public class UserInfoFragment extends Fragment {

    @Bind(R.id.user_info_birthday)TextView birthday;
    @Bind(R.id.user_info_description)TextView description;
    @Bind(R.id.user_info_fan_age)TextView fanAge;
    @Bind(R.id.user_info_site)TextView site;
    @Bind(R.id.user_info_modify)TextView modify;

    public static UserInfoFragment newInstance(User user) {
        UserInfoFragment fragment = new UserInfoFragment();
        Bundle args = new Bundle();
        ArrayList<String> data = new ArrayList<>();
        data.add(user.getLocation());
        data.add(user.getBirthday());
        data.add(user.getDescription());
        data.add(user.getUrl());
        data.add(user.getId());
        args.putStringArrayList("data", data);
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
        View v = inflater.inflate(R.layout.fragment_user_info, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpInfo();
    }

    private void setUpInfo() {
        ArrayList data = getArguments().getCharSequenceArrayList("data");
        if (!getArguments().getBoolean("flag", false)) {
            modify.setVisibility(View.GONE);
        }
        fanAge.setText(data.get(0).toString());
        birthday.setText(data.get(1).toString());
        description.setText(data.get(2).toString());
        site.setText(data.get(3).toString());
        if (!data.get(4).toString().equals(SicillyFactory.getCurrentUser().getId())) {
            modify.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.user_info_modify)
    void modifyInfo() {
        startActivity(new Intent(getActivity(), EditInfoActivity.class));
    }



}
