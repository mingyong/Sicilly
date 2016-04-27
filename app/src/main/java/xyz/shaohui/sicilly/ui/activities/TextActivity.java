package xyz.shaohui.sicilly.ui.activities;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.ui.widgets.RotateLoading;

public class TextActivity extends AppCompatActivity {

    @Bind(R.id.progress_bar)RotateLoading progressBar;
    @Bind(R.id.tool_bar)Toolbar toolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        ButterKnife.bind(this);

        initToolBar();

        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    progressBar.setLoadingColor(getResources().getColor(R.color.background));
                } else {
                    progressBar.stop();
                }
            }
        };
        handler.sendEmptyMessageDelayed(1, 3000);
        handler.sendEmptyMessageDelayed(2, 8000);

    }

    private void initToolBar() {
        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle(getString(R.string.status_detail));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        progressBar.start();
    }
}

