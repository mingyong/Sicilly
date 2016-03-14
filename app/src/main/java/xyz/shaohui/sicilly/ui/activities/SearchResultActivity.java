package xyz.shaohui.sicilly.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import xyz.shaohui.sicilly.R;

public class SearchResultActivity extends AppCompatActivity {

    @Bind(R.id.tool_bar)Toolbar toolbar;
    @Bind(R.id.main_frame)FrameLayout mainFrame;

    private String q;

    public static Intent newIntent(Context context, String q) {
        Intent intent = new Intent(context, SearchResultActivity.class);
        intent.putExtra("q", q);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        ButterKnife.bind(this);

        initToolbar();

        q = getIntent().getStringExtra("q");
        toolbar.setTitle("\"" + q + "\"" + "相关");
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
