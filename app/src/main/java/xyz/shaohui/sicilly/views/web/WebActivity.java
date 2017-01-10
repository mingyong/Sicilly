package xyz.shaohui.sicilly.views.web;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.daimajia.numberprogressbar.NumberProgressBar;
import me.shaohui.sicillylib.utils.ToastUtils;
import org.greenrobot.eventbus.EventBus;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.base.BaseActivity;
import xyz.shaohui.sicilly.utils.HtmlUtils;
import xyz.shaohui.sicilly.utils.SimpleUtils;
import xyz.shaohui.sicilly.views.share.ShareDialog;
import xyz.shaohui.sicilly.views.share.ShareDialogBuilder;

public class WebActivity extends BaseActivity {

    private String mUrl;

    @BindView(R.id.web_view)
    WebView mWebView;
    @BindView(R.id.tool_bar)
    Toolbar mToolbar;
    @BindView(R.id.progress_bar)
    NumberProgressBar mProgressBar;

    public static Intent newIntent(Context context, String url) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra("url", url);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
        if (getIntent().getData() != null) {
            mUrl = HtmlUtils.revertHttpScheme(getIntent().getData());
        } else {
            mUrl = getIntent().getStringExtra("url");
        }

        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_back_black);
        mToolbar.setNavigationOnClickListener(v -> finish());

        initView();
    }

    @Override
    public void initializeInjector() {

    }

    @Override
    public EventBus getBus() {
        return null;
    }

    private void initView() {
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mProgressBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                mToolbar.setTitle(title);
                super.onReceivedTitle(view, title);
            }
        });
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.loadUrl(mUrl);
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.web_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.open_by_browser:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mUrl));
                startActivity(intent);
                return true;
            case R.id.copy_url:
                SimpleUtils.copyText(this, mUrl);
                ToastUtils.showToast(this, R.string.copy_text_tip);
                return true;
            case R.id.share_url:
                new ShareDialogBuilder(ShareDialog.TYPE_WEB).url(mUrl)
                        .title(mWebView.getTitle())
                        .build()
                        .show(getSupportFragmentManager());
                return true;
        }
        return false;
    }
}
