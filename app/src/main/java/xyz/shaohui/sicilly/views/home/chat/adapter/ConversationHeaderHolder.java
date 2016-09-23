package xyz.shaohui.sicilly.views.home.chat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import me.shaohui.sicillylib.utils.ToastUtils;

/**
 * Created by shaohui on 16/9/23.
 */

public class ConversationHeaderHolder extends RecyclerView.ViewHolder {
    public ConversationHeaderHolder(View itemView) {
        super(itemView);
        itemView.setVisibility(View.GONE);
    }
}
