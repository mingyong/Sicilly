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
import xyz.shaohui.sicilly.base.BaseDialogFragment;

/**
 * Created by shaohui on 16/8/11.
 */
public class LoginDialogFragment extends BaseDialogFragment {

    @BindView(R.id.edit_username)EditText username;
    @BindView(R.id.edit_password)EditText password;

    @Override
    public int layoutRes() {
        return R.layout.login_dialog_layout;
    }

    @Override
    public void bindView(View view) {
    }

    @OnClick(R.id.btn_login)
    void loginAction() {
        ToastUtils.showToast(getActivity(), "登录");
    }
}
