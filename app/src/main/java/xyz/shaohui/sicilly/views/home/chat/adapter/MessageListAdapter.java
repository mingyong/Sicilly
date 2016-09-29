package xyz.shaohui.sicilly.views.home.chat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import java.util.List;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.data.models.Conversation;
import xyz.shaohui.sicilly.data.models.ConversationBean;

/**
 * Created by shaohui on 16/8/20.
 */
public class MessageListAdapter extends RecyclerView.Adapter {

    private static final int TYPE_HEADER = 1;
    private static final int TYPE_REQUEST = 2;
    private static final int TYPE_MESSAGE = 3;

    private List<ConversationBean> dataList;

    public MessageListAdapter(List<ConversationBean> dataList) {
        this.dataList = dataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case TYPE_HEADER:
                return new ConversationHeaderHolder(
                        inflater.inflate(R.layout.view_message_header, parent, false));
            case TYPE_MESSAGE:
                return new ConversationMessageHolder(
                        inflater.inflate(R.layout.item_message, parent, false));
            case TYPE_REQUEST:
                return new ConversationRequestHolder(
                        inflater.inflate(R.layout.view_message_friend_request, parent, false));
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else if (dataList.get(position - 1) instanceof Conversation) {
            return TYPE_MESSAGE;
        } else {
            return TYPE_REQUEST;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ConversationMessageHolder) {
            ((ConversationMessageHolder) holder).bind((Conversation) dataList.get(position - 1),
                    position);
        } else if (holder instanceof ConversationRequestHolder) {
            ((ConversationRequestHolder) holder).bind(dataList.get(position - 1), position);
        } else if (holder instanceof ConversationHeaderHolder) {
            // do nothing
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size() + 1;
    }
}
