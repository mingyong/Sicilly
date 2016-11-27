package xyz.shaohui.sicilly.views.feed.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.shaohui.sicilly.R;

/**
 * Created by shaohui on 2016/11/27.
 */

public class BaseFeedViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.status_text)
    TextView text;
    @BindView(R.id.status_image)
    ImageView image;
    @BindView(R.id.status_time)
    TextView createdTime;
    @BindView(R.id.flag_gif)
    TextView gif;

    public BaseFeedViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
