package xyz.shaohui.sicilly.views.home.chat.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.data.models.Conversation;
import xyz.shaohui.sicilly.data.models.ConversationBean;
import xyz.shaohui.sicilly.views.friend_request.FriendRequestActivity;

/**
 * Created by shaohui on 16/9/23.
 */

public class ConversationRequestHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.message_count)TextView count;

    private Context mContext = itemView.getContext();

    public ConversationRequestHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(ConversationBean conversationBean, int position) {
        count.setText(String.valueOf(conversationBean.msg_num));

        itemView.setOnClickListener(v -> mContext.startActivity(new Intent(mContext,
                FriendRequestActivity.class)));
    }
}
