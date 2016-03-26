package xyz.shaohui.sicilly.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.data.models.Message;
import xyz.shaohui.sicilly.ui.activities.UserInfoActivity;
import xyz.shaohui.sicilly.utils.TimeFormat;
import xyz.shaohui.sicilly.utils.imageUtils.CircleTransform;

/**
 * Created by kpt on 16/3/14.
 */
public class ChatAdapter extends RecyclerView.Adapter {

    private List<Message> dataList;
    private String userId;

    private final int TYPE_RIGHT = 1;
    private final int TYPE_LEFT = 2;

    public ChatAdapter(List<Message> dataList, String userId) {
        this.dataList = dataList;
        this.userId = userId;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = dataList.get(position);
        if (message.getSenderId().equals(userId)) {
            return TYPE_LEFT;
        } else {
            return TYPE_RIGHT;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case TYPE_LEFT:
                return new LeftViewHolder(inflater.inflate(R.layout.chat_left_item, parent, false));
            case TYPE_RIGHT:
                return new RightViewHolder(inflater.inflate(R.layout.chat_right_item, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Message message = dataList.get(position);
        if (holder instanceof LeftViewHolder) {

            LeftViewHolder left = (LeftViewHolder) holder;
            final Context context = left.text.getContext();

            left.text.setText(message.getText());
            left.time.setText(TimeFormat.format(message.getCreatedTime()));
            if (message.isSending()) {
                left.progressBar.setVisibility(View.VISIBLE);
            } else {
                left.progressBar.setVisibility(View.GONE);
            }
            Picasso.with(context)
                    .load(Uri.parse(message.getSender().getProfileImageUrl()))
                    .transform(new CircleTransform())
                    .into(left.avatar);
            left.avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = UserInfoActivity.newIntent(context, message.getSenderId());
                    context.startActivity(intent);
                }
            });

        } else if (holder instanceof RightViewHolder) {

            RightViewHolder right = (RightViewHolder) holder;
            final Context context = right.text.getContext();

            right.text.setText(message.getText());
            right.time.setText(TimeFormat.format(message.getCreatedTime()));
            if (message.isSending()) {
                right.progressBar.setVisibility(View.VISIBLE);
            } else {
                right.progressBar.setVisibility(View.GONE);
            }
            Picasso.with(context)
                    .load(Uri.parse(message.getSender().getProfileImageUrl()))
                    .transform(new CircleTransform())
                    .into(right.avatar);
            right.avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = UserInfoActivity.newIntent(context, message.getSenderId());
                    context.startActivity(intent);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class RightViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.user_avatar)ImageView avatar;
        @Bind(R.id.message_text)TextView text;
        @Bind(R.id.message_time)TextView time;
        @Bind(R.id.message_progress_bar)ProgressBar progressBar;

        public RightViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class LeftViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.user_avatar)ImageView avatar;
        @Bind(R.id.message_text)TextView text;
        @Bind(R.id.message_time)TextView time;
        @Bind(R.id.message_progress_bar)ProgressBar progressBar;

        public LeftViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
