package xyz.shaohui.sicilly.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.data.services.user.UserService;
import xyz.shaohui.sicilly.utils.MyToast;

public class CreateStatusActivity extends AppCompatActivity {

    @Bind(R.id.tool_bar)Toolbar toolbar;
    @Bind(R.id.main_edit)EditText mainEdit;
    @Bind(R.id.main_send)TextView mainSend;
    
    private String rId;
    private String rText;

    public static Intent newIntent(Context context, String repostId, String repostText) {
        Intent intent = new Intent(context, CreateStatusActivity.class);
        intent.putExtra("r_id", repostId);
        intent.putExtra("r_text", repostText);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_status);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        initToolbar();

        getRePostData();

        showInput();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitEdit();
            }
        });
    }

    private void getRePostData() {
        Intent intent = getIntent();
        if (!TextUtils.isEmpty(intent.getStringExtra("r_id"))) {
            rId = intent.getStringExtra("r_id");
            rText = intent.getStringExtra("r_text");

            mainEdit.setText(rText);
//            mainEdit.setSelection(0);
        }
    }

    private void showInput() {
        final InputMethodManager inputMethodManager =
                (InputMethodManager) mainEdit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                inputMethodManager.showSoftInput(mainEdit, 0);
            }
        },100);
    }

    private void quitEdit() {
        if (!TextUtils.isEmpty(mainEdit.getText())) {
            MyToast.showToast(this, "todo:提醒是否保存草稿");
        } else {
            finish();
        }
    }

    @OnClick(R.id.main_send)
    void clickSend() {
        if (TextUtils.isEmpty(rId)) {
            createStatus();
        } else {
            repostStatus();
        }
    }

    private void repostStatus() {
        UserService.repostStatus(mainEdit.getText().toString(), rId, new UserService.CallBack() {
            @Override
            public void success() {
                MyToast.showToast(getApplicationContext(), "成功");
            }

            @Override
            public void failure() {
                MyToast.showToast(getApplicationContext(), "发送失败");
            }
        });
    }

    private void createStatus() {
        UserService.createStatus(mainEdit.getText().toString(), new UserService.CallBack() {
            @Override
            public void success() {
                MyToast.showToast(getApplicationContext(), "发送成功" + mainEdit.getText().toString());
            }

            @Override
            public void failure() {
                MyToast.showToast(getApplicationContext(), "失败");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_status, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
