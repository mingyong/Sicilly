package xyz.shaohui.sicilly.ui.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.BindInt;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.SicillyFactory;
import xyz.shaohui.sicilly.data.preferences.TokenSP;
import xyz.shaohui.sicilly.data.services.auth.AuthService;

public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.username)EditText username;
    @Bind(R.id.password)EditText password;

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        initEditText();

        initProgressBar();

    }

    private void initEditText() {
        username.setText("shaohui10086@163.com");
        password.setText("hui10086");
    }

    private void initProgressBar() {
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("登录中");
        mDialog.setProgress(ProgressDialog.STYLE_SPINNER);
        mDialog.setCancelable(false);
    }

    @OnClick(R.id.main_btn)
    void btnMain() {
        AuthService auth = new AuthService();
        auth.getAccessToken(this, username.getText().toString(), password.getText().toString(), new AuthService.AuthListener() {
            @Override
            public void begin() {
                mDialog.show();
            }

            @Override
            public void end() {
                mDialog.dismiss();
                SicillyFactory.token = TokenSP.accessToken(getApplicationContext());
                startActivity(new Intent(LoginActivity.this, IndexActivity.class));
                finish();
            }
        });
    }

}
