package xyz.shaohui.sicilly.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import butterknife.Bind;
import butterknife.BindInt;
import butterknife.ButterKnife;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.ui.fragments.StatusDetailFragment;
import xyz.shaohui.sicilly.ui.widgets.RotateLoading;

public class StatusDetailActivity extends AppCompatActivity implements StatusDetailFragment.StatusDetailInterface {

    @Bind(R.id.tool_bar)Toolbar toolBar;
    @Bind(R.id.progress_bar)RotateLoading progressBar;

    private String statusJson;

    public static Intent newIntent(Context context, String statusJson) {
        Intent intent = new Intent(context, StatusDetailActivity.class);
        intent.putExtra("status", statusJson);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_detail);
        ButterKnife.bind(this);

        statusJson = getIntent().getStringExtra("status");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        initToolBar();

        switchDetailFragment();
    }

    private void initToolBar() {
        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle(getString(R.string.status_detail));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        progressBar.start();
    }

    private void switchDetailFragment() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.main_frame, StatusDetailFragment.newInstance(statusJson))
                .commit();
    }

    @Override
    public void update() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.stop();
            }
        }, 1000);
    }
}
