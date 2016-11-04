package xyz.shaohui.sicilly.views.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import me.shaohui.vistarecyclerview.VistaRecyclerView;
import org.greenrobot.eventbus.EventBus;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.SicillyApplication;
import xyz.shaohui.sicilly.base.BaseActivity;
import xyz.shaohui.sicilly.base.HasComponent;
import xyz.shaohui.sicilly.data.models.Message;
import xyz.shaohui.sicilly.data.models.User;
import xyz.shaohui.sicilly.utils.ErrorUtils;
import xyz.shaohui.sicilly.views.chat.adapter.ChatAdapter;
import xyz.shaohui.sicilly.views.chat.di.ChatComponent;
import xyz.shaohui.sicilly.views.chat.di.ChatModule;
import xyz.shaohui.sicilly.views.chat.di.DaggerChatComponent;

public class ChatActivity extends BaseActivity implements HasComponent<ChatComponent> {

    ChatComponent mChatComponent;

    User mOtherUser;

    @Inject
    EventBus mBus;

    public static Intent newIntent(Context context, User user) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("user", user);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.content, new ChatFragment(), "chat")
                    .commit();
        }
    }

    @Override
    public void initializeInjector() {
        mOtherUser = getIntent().getParcelableExtra("user");
        mChatComponent = DaggerChatComponent.builder()
                .appComponent(getAppComponent())
                .chatModule(new ChatModule(mOtherUser))
                .build();
        mChatComponent.inject(this);
    }

    @Override
    public EventBus getBus() {
        return mBus;
    }

    @Override
    public ChatComponent getComponent() {
        return mChatComponent;
    }
}
