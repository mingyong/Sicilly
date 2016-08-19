package xyz.shaohui.sicilly.views.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.data.models.User;
import xyz.shaohui.sicilly.utils.HtmlUtils;

/**
 * Created by shaohui on 16/8/19.
 */
public class IndexStatusAdapter extends RecyclerView.Adapter {

    private List<Status> dataList;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_status_index, parent, false);
        return new StatusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Status status = dataList.get(position);
        User user = status.getUser();
        StatusViewHolder viewHolder = (StatusViewHolder) holder;
        Context context = viewHolder.avatar.getContext();

        viewHolder.name.setText(user.getScreen_name());
        viewHolder.createdTime.setText("3分钟前");
        viewHolder.source.setText(HtmlUtils.cleanAllTag(status.getSource()));
        viewHolder.text.setText(HtmlUtils.cleanAllTag(status.getSource()));
        Glide.with(context)
                .load(user.getProfile_image_url_large())
                .into(viewHolder.avatar);
        if (status.getPhoto() != null) {
            Glide.with(context)
                    .load(status.getPhoto().getImageurl())
                    .into(viewHolder.image);
        }

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class StatusViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.user_avatar)ImageView avatar;
        @BindView(R.id.user_name)TextView name;
        @BindView(R.id.status_text)TextView text;
        @BindView(R.id.status_source)TextView source;
        @BindView(R.id.status_image)ImageView image;
        @BindView(R.id.status_time)TextView createdTime;
        @BindView(R.id.action_comment)ImageButton actionComment;
        @BindView(R.id.action_repost)ImageButton actionRepost;
        @BindView(R.id.action_star)ImageButton actionStar;

        public StatusViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
