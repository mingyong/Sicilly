package xyz.shaohui.sicilly.views.chat;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import me.shaohui.sicillylib.utils.ToastUtils;
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

    @BindView(R.id.btn_submit)
    TextView btnSubmit;

    @Inject
    @Named("other_user")
    User mOtherUser;

    @Inject
    EventBus mBus;

    List<Message> mDataList;

    int mPage = 1;

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

        //presenter.checkFriendShip();
    }

    @OnClick(R.id.btn_back)
    void btnBack() {
        getActivity().finish();
    }

    @OnClick(R.id.btn_submit)
    void btnSubmit() {
        if (TextUtils.isEmpty(editText.getText().toString())) {
            return;
        }
        presenter.sendMessage(editText.getText().toString().trim());
        editText.setText("");
    }

    private void initRecycler() {
        mDataList = new ArrayList<>();
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);
        ChatAdapter adapter = new ChatAdapter(mOtherUser.id().trim(), mDataList);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setOnMoreListener((total, left, current) -> {
            if (mDataList.size() > 0) {
                presenter.fetchMessageNext(++mPage, mDataList.get(mDataList.size() - 1));
            }
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

    @Override
    public void showMoreMessage(List<Message> messages) {
        mDataList.addAll(messages);
        recyclerView.notifyDataSetChanged();
    }

    @Override
    public void loadNoMore() {
        recyclerView.loadMoreFailure();
        recyclerView.removeOnMoreListener();
    }

    @Override
    public void loadMoreError() {
        mPage--;
        recyclerView.loadMoreFailure();
    }

    @Override
    public void sendMessage(Message message) {
        // 暂时省事的方法, 后期会修改
        if (message.is_success() && !mDataList.get(0).is_success() && TextUtils.equals(
                message.getText(), mDataList.get(0).getText())) {
            mDataList.remove(0);
        }
        mDataList.add(0, message);
        recyclerView.notifyDataSetChanged();
        recyclerView.getRecycler().smoothScrollToPosition(0);
    }

    @Override
    public void sendMessageFail(String text) {
        ToastUtils.showToast(getActivity(), R.string.send_message_fail);
        editText.setText(text);
        mDataList.remove(0);
        recyclerView.notifyDataSetChanged();
    }

    @Override
    public void denySendMessage() {
        new MaterialDialog.Builder(getContext())
                .content(R.string.no_follow_each_message)
                .positiveText(R.string.yes)
                .show();
        btnSubmit.setEnabled(false);
    }
}
