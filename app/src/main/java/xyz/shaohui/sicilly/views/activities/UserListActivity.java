package xyz.shaohui.sicilly.views.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.base.BaseActivity;

public class UserListActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
    }

    @Override
    public void initializeInjector() {

    }
}
