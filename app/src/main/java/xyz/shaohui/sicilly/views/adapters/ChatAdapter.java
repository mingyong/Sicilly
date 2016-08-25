package xyz.shaohui.sicilly.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.data.models.Message;

/**
 * Created by shaohui on 16/8/25.
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    public static final int TYPE_RIGHT = 1;
    public static final int TYPE_LEFT = 2;

    private List<Message> dataList;

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
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

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ChatViewHolder extends RecyclerView.ViewHolder {

        public ChatViewHolder(View itemView) {
            super(itemView);
        }
    }
}
