package xyz.shaohui.sicilly.views.chat;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import me.shaohui.vistarecyclerview.VistaRecyclerView;
import org.greenrobot.eventbus.EventBus;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.SicillyApplication;
import xyz.shaohui.sicilly.base.BaseFragment;
import xyz.shaohui.sicilly.data.models.Message;
import xyz.shaohui.sicilly.data.models.User;
import xyz.shaohui.sicilly.views.chat.adapter.ChatAdapter;
import xyz.shaohui.sicilly.views.chat.di.ChatComponent;
import xyz.shaohui.sicilly.views.chat.mvp.ChatPresenter;
import xyz.shaohui.sicilly.views.chat.mvp.ChatView;

/**
 * Created by shaohui on 16/9/23.
 */

public class ChatFragment extends BaseFragment<ChatView, ChatPresenter> implements ChatView {

    @BindView(R.id.recycler)
    VistaRecyclerView recyclerView;
    @BindView(R.id.customer_avatar)
    ImageView customerAvatar;
    @BindView(R.id.self_avatar)
    ImageView selfAvatar;
    @BindView(R.id.main_edit)
    EditText editText;
    @BindView(R.id.chat_title)
    TextView title;

    @Inject
    @Named("other_user")
    User mOtherUser;

    @Inject
    EventBus mBus;

    List<Message> mDataList;

    int mPage;

    @NonNull
    @Override
    public EventBus getBus() {
        return mBus;
    }

    @Override
    public void injectDependencies() {
        ChatComponent component = getComponent(ChatComponent.class);
        component.inject(this);
        presenter = component.chatPresenter();
    }

    @Override
    public int layoutRes() {
        return R.layout.activity_chat;
    }

    @Override
    public void bindViews(View view) {
        initRecycler();

        initUserInfo();

        presenter.fetchMessage();
    }

    private void initRecycler() {
        mDataList = new ArrayList<>();
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        ChatAdapter adapter = new ChatAdapter(mOtherUser.id(), mDataList);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setOnMoreListener((total, left, current) -> {
            recyclerView.loadNoMore();
        });
    }

    private void initUserInfo() {
        title.setText(mOtherUser.screen_name());
        Glide.with(this).load(mOtherUser.profile_image_url_large()).into(customerAvatar);
        Glide.with(this).load(SicillyApplication.currentAppUser().avatar()).into(selfAvatar);
    }

    @Override
    public void showMessage(List<Message> messages) {
        mDataList.addAll(messages);
        recyclerView.notifyDataSetChanged();
    }
}
