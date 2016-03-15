package xyz.shaohui.sicilly.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.data.models.Message;
import xyz.shaohui.sicilly.data.models.MessageList;
import xyz.shaohui.sicilly.data.models.User;
import xyz.shaohui.sicilly.ui.activities.ChatActivity;
import xyz.shaohui.sicilly.ui.activities.UserInfoActivity;
import xyz.shaohui.sicilly.utils.TimeFormat;
import xyz.shaohui.sicilly.utils.imageUtils.CircleTransform;

/**
 * Created by kpt on 16/3/13.
 */
public class MessageListAdapter extends RecyclerView.Adapter {

    private List<MessageList> dataList;

    public MessageListAdapter(List<MessageList> dataList) {
        this.dataList = dataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new MyViewHolder(inflater.inflate(R.layout.message_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MessageList item = dataList.get(position);
        Message message = item.getMessage();
        final MyViewHolder viewHolder = (MyViewHolder) holder;
        final Context context = viewHolder.avatar.getContext();

        int visibility = item.isHaveNew() ? View.VISIBLE:View.INVISIBLE;
        viewHolder.hasNew.setVisibility(visibility);

        final User user = message.getSender().getId()
                .equals(item.getOtherId()) ? message.getSender() : message.getRecipient();
        viewHolder.name.setText(user.getNickName());
        viewHolder.text.setText(message.getText());
        viewHolder.time.setText(TimeFormat.format(message.getCreatedTime()));
        Picasso.with(context)
                .load(Uri.parse(user.getProfileImageUrl()))
                .transform(new CircleTransform())
                .into(viewHolder.avatar);

        viewHolder.avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = UserInfoActivity.newIntent
                        (context, user.getId());
                context.startActivity(intent);
            }
        });
        viewHolder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ChatActivity.newIntent(context, item.getOtherId(), user.getName());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.parent)LinearLayout parent;
        @Bind(R.id.message_text)TextView text;
        @Bind(R.id.message_time)TextView time;
        @Bind(R.id.user_avatar)ImageView avatar;
        @Bind(R.id.user_name)TextView name;
        @Bind(R.id.message_new)TextView hasNew;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
