package xyz.shaohui.sicilly.views.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.shaohui.scrollablelayout.ScrollableLayout;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.utils.HtmlUtils;

public class WebActivity extends BaseActivity {

    @BindView(R.id.web_title)TextView title;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        url = HtmlUtils.revertHttpScheme(getIntent().getData());
        ButterKnife.bind(this);

        title.setText(url);
    }
}
