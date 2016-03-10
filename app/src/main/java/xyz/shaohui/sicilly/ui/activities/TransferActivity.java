package xyz.shaohui.sicilly.ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.SicillyFactory;
import xyz.shaohui.sicilly.utils.MyToast;
import xyz.shaohui.sicilly.utils.Validate;

public class TransferActivity extends AppCompatActivity {

    private String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        Intent i = getIntent();
        data = i.getData().toString();

        if (Validate.isUserUrl(data)) {
            //打开用户界面
            startUserActivity(data);

        } else if (Validate.isTrendUrl(data)) {
            //打开话题界面
            startTrendActivity(data);

        } else if (Validate.isWebUrl(data)) {
            //打开web界面
            startWebActivity(data);

        }

        finish();
    }

    private String toUserId(String data) {
        String userId = data.substring(SicillyFactory.PREFIX_USER.length());
        return userId;
    }

    private String toTrend(String data) {
        String trend = data.substring(SicillyFactory.PREFIX_TREND.length());
        return trend;
    }

    private String toUrl(String data) {
        String url = data.substring(SicillyFactory.PREFIX_WEB.length());
        return url;
    }

    private void startUserActivity(String data) {
        String userId = toUserId(data);
        Intent intent = UserInfoActivity.newIntent(this, userId);
        startActivity(intent);
    }

    private void startTrendActivity(String data) {
        String trend = toTrend(data);
        MyToast.showToast(this, "TODO" + trend);
    }

    private void startWebActivity(String data) {
        String url = toUrl(data);
        Intent intent = WebViewActivity.newIntent(this, url);
        startActivity(intent);
    }

}
