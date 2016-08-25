package xyz.shaohui.sicilly.views.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import me.shaohui.vistarecyclerview.VistaRecyclerView;
import xyz.shaohui.sicilly.R;

public class ChatActivity extends BaseActivity {

    @BindView(R.id.recycler)VistaRecyclerView recyclerView;
    @BindView(R.id.customer_avatar)ImageView customerAvatar;
    @BindView(R.id.self_avatar)ImageView selfAvatar;
    @BindView(R.id.main_edit)EditText editText;
    @BindView(R.id.chat_title)TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initRecycler();
    }

    private void initRecycler() {
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void fetchChatList() {

    }
}
