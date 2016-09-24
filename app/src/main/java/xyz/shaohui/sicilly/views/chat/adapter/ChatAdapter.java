package xyz.shaohui.sicilly.views.chat.adapter;

import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.data.models.Message;
import xyz.shaohui.sicilly.utils.TimeUtils;

/**
 * Created by shaohui on 16/8/25.
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    public static final int TYPE_RIGHT = 1;
    public static final int TYPE_LEFT = 2;

    private List<Message> dataList;
    private String otherUserId;

    public ChatAdapter(String otherUserId, List<Message> dataList) {
        this.otherUserId = otherUserId;
        this.dataList = dataList;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = dataList.get(position);
        if (message.getSender_id().equals(otherUserId)) {
            return TYPE_LEFT;
        } else {
            return TYPE_RIGHT;
        }
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_LEFT) {
            return new ChatViewHolder(inflater.inflate(R.layout.item_chat_left, parent, false));
        } else {
            return new ChatViewHolder(inflater.inflate(R.layout.item_chat_right, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        Message message = dataList.get(position);
        holder.text.setText(message.getText());
        holder.time.setText(TimeUtils.timeFormat(message.getCreated_at()));

        if (message.is_success()) {
            holder.progressBar.hide();
        } else {
            holder.progressBar.show();
            holder.progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ChatViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.message_text)TextView text;
        @BindView(R.id.message_time)TextView time;
        @BindView(R.id.message_progress)ContentLoadingProgressBar progressBar;

        public ChatViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
