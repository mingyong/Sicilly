package xyz.shaohui.sicilly.views.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.data.models.Conversation;
import xyz.shaohui.sicilly.data.models.FriendRequest;
import xyz.shaohui.sicilly.data.models.Message;
import xyz.shaohui.sicilly.data.models.User;
import xyz.shaohui.sicilly.utils.TimeUtils;
import xyz.shaohui.sicilly.views.activities.ChatActivity;

/**
 * Created by shaohui on 16/8/20.
 */
public class MessageListAdapter extends RecyclerView.Adapter {
    public static final int TYPE_HEADER = 1;
    public static final int TYPE_REQUEST = 2;
    public static final int TYPE_MESSAGE = 3;

    private List dataList;

    public MessageListAdapter(List dataList) {
        this.dataList = dataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case TYPE_HEADER:
                return new MessageHeaderViewHolder(
                        inflater.inflate(R.layout.view_message_header, parent, false));
            case TYPE_MESSAGE:
                return new MessageViewHolder(
                        inflater.inflate(R.layout.item_message, parent, false));
            case TYPE_REQUEST:
                return new FriendRequestViewHolder(
                        inflater.inflate(R.layout.view_message_friend_request, parent, false));
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else if (dataList.get(position - 1) instanceof FriendRequest) {
            return TYPE_REQUEST;
        } else {
            return TYPE_MESSAGE;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MessageViewHolder) {
            Conversation conversation = (Conversation) dataList.get(position - 1);
            MessageViewHolder viewHolder = (MessageViewHolder) holder;
            final Context context = viewHolder.itemView.getContext();

            final User otherUser;
            if (conversation.getOtherid().equals(conversation.getDm().getRecipient_id())) {
                otherUser = conversation.getDm().getRecipient();
                viewHolder.name.setText(conversation.getDm().getRecipient_screen_name());
            } else {
                otherUser = conversation.getDm().getSender();
                viewHolder.name.setText(conversation.getDm().getSender_screen_name());
            }
            Glide.with(context)
                    .load(otherUser.profile_image_url_large())
                    .into(viewHolder.avatar);
            viewHolder.text.setText(conversation.getDm().getText());
            viewHolder.time.setText(TimeUtils.simpleFormat(conversation.getDm().getCreated_at()));
            if (conversation.isNew_conv()) {
                viewHolder.count.setVisibility(View.VISIBLE);
                viewHolder.count.setText(String.valueOf(3));
            } else {
                viewHolder.count.setVisibility(View.GONE);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(ChatActivity.newIntent(context, otherUser));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size() + 1;
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.user_avatar)RoundedImageView avatar;
        @BindView(R.id.user_name)TextView name;
        @BindView(R.id.message_text)TextView text;
        @BindView(R.id.message_count)TextView count;
        @BindView(R.id.message_time)TextView time;

        public MessageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class MessageHeaderViewHolder extends RecyclerView.ViewHolder {

        private View parent;

        public MessageHeaderViewHolder(View itemView) {
            super(itemView);
            parent = itemView;
        }
    }

    class FriendRequestViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.message_count)TextView count;
        @BindView(R.id.message_text)TextView text;
        @BindView(R.id.message_time)TextView time;

        public FriendRequestViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
