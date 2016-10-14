package xyz.shaohui.sicilly.views.friend_list;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import xyz.shaohui.sicilly.R;

public class FriendListActivity extends AppCompatActivity {

    public static final int TYPE_TINY = 1;
    public static final int TYPE_FULL = 2;

    public static Intent newIntent(Context context, int type) {
        Intent intent = new Intent(context, FriendListActivity.class);
        intent.putExtra("type", type);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
    }
}
