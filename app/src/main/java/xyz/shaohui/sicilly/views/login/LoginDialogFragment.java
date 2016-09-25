package xyz.shaohui.sicilly.views.login;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.shaohui.sicillylib.utils.ToastUtils;
import xyz.shaohui.sicilly.R;

/**
 * Created by shaohui on 16/8/11.
 */
public class LoginDialogFragment extends DialogFragment {

    @BindView(R.id.edit_username)EditText username;
    @BindView(R.id.edit_password)EditText password;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.login_dialog_layout, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @OnClick(R.id.btn_login)
    void loginAction() {
        ToastUtils.showToast(getActivity(), "登录");
    }
}
