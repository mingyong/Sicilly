package xyz.shaohui.sicilly.views.home.chat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.data.models.Conversation;
import xyz.shaohui.sicilly.data.models.Message;
import xyz.shaohui.sicilly.data.models.User;
import xyz.shaohui.sicilly.utils.TimeUtils;
import xyz.shaohui.sicilly.views.chat.ChatActivity;
import xyz.shaohui.sicilly.views.user_info.UserActivity;

/**
 * Created by shaohui on 16/9/23.
 */

public class ConversationMessageHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.user_avatar)
    RoundedImageView avatar;
    @BindView(R.id.user_name)
    TextView name;
    @BindView(R.id.message_text)
    TextView text;
    @BindView(R.id.message_count)
    TextView count;
    @BindView(R.id.message_time)
    TextView time;

    Context mContext = itemView.getContext();

    public ConversationMessageHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(Conversation conversation, int position) {
        final Message message = conversation.getDm();
        final User otherUser;

        if (TextUtils.equals(conversation.getOtherid(), message.getSender_id())) {
            otherUser = message.getSender();
        } else {
            otherUser = message.getRecipient();
        }

        text.setText(message.getText());
        name.setText(otherUser.screen_name());
        time.setText(TimeUtils.timeFormat(message.getCreated_at()));
        count.setText(String.valueOf(conversation.getMsg_num()));
        Glide.with(mContext).load(otherUser.profile_image_url_large()).into(avatar);
        avatar.setOnClickListener(
                v -> mContext.startActivity(UserActivity.newIntent(mContext, otherUser.id())));
        itemView.setOnClickListener(
                v -> mContext.startActivity(ChatActivity.newIntent(mContext, otherUser)));
    }
}
