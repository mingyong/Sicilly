package xyz.shaohui.sicilly.views.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.data.network.auth.AuthService;
import xyz.shaohui.sicilly.data.network.auth.OAuthHelper;

public class LoginActivity extends AppCompatActivity {
    private final String TAG = "loginActivity";

    @BindView(R.id.edit_username)EditText username;
    @BindView(R.id.edit_password)EditText password;

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activty);
        ButterKnife.bind(this);

        mDialog = new ProgressDialog(this);
        mDialog.setProgress(ProgressDialog.STYLE_SPINNER);
        mDialog.setCancelable(false);
        mDialog.setMessage("登录中...");
    }

    @OnClick(R.id.btn_login)
    void actionLogin() {
        Log.i(TAG, "登录");
        AuthService service = new AuthService();
        service.getAccessToken(this,
                username.getText().toString(),
                password.getText().toString(),
                new AuthService.AuthListener() {
                    @Override
                    public void begin() {
                        mDialog.show();
                    }

                    @Override
                    public void end() {
                        mDialog.dismiss();
                        startIndex();
                    }

                    @Override
                    public void failure() {
                        mDialog.dismiss();
                    }
                });
    }

    private void startIndex() {
        Intent intent = new Intent(this, IndexActivity.class);
        startActivity(intent);
        finish();
    }

}
