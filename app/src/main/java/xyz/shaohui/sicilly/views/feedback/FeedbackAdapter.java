package xyz.shaohui.sicilly.views.feedback;

import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.Date;
import java.util.List;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.data.models.Feedback;
import xyz.shaohui.sicilly.utils.TimeUtils;

/**
 * Created by shaohui on 16/9/24.
 */

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder>{

    private List<Feedback> dataList;

    private static final int TYPE_RIGHT = 1;
    private static final int TYPE_LEFT = 2;

    public FeedbackAdapter(List<Feedback> dataList) {
        this.dataList = dataList;
    }

    @Override
    public int getItemViewType(int position) {
        if (!dataList.isEmpty() && dataList.get(position).is_right()) {
            return TYPE_RIGHT;
        } else {
            return TYPE_LEFT;
        }
    }

    @Override
    public FeedbackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_LEFT) {
            return new FeedbackViewHolder(inflater.inflate(R.layout.item_chat_left, parent, false));
        } else {
            return new FeedbackViewHolder(inflater.inflate(R.layout.item_chat_right, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(FeedbackViewHolder holder, int position) {
        Feedback feedback = dataList.get(position);

        holder.text.setText(feedback.text());
        holder.time.setText(TimeUtils.timeFormat(new Date(feedback.id())));
        if (feedback.send_success()) {
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

    class FeedbackViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.message_text)TextView text;
        @BindView(R.id.message_time)TextView time;
        @BindView(R.id.message_progress)ContentLoadingProgressBar progressBar;

        FeedbackViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
