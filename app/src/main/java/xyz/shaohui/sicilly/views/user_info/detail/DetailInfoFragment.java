package xyz.shaohui.sicilly.views.user_info.detail;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.data.models.User;

/**
 * Created by shaohui on 2016/11/30.
 */

@FragmentWithArgs
public class DetailInfoFragment extends Fragment {

    @Arg
    User mUser;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_user_detail_info, container, false);
        ButterKnife.bind(this, v);
        return v;
    }
}
