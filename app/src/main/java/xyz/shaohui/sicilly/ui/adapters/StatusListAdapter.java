package xyz.shaohui.sicilly.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.data.models.User;
import xyz.shaohui.sicilly.data.services.user.UserService;
import xyz.shaohui.sicilly.ui.activities.StatusDetailActivity;
import xyz.shaohui.sicilly.ui.activities.UserInfoActivity;
import xyz.shaohui.sicilly.utils.HtmlParse;
import xyz.shaohui.sicilly.utils.MyToast;
import xyz.shaohui.sicilly.utils.TimeFormat;
import xyz.shaohui.sicilly.utils.imageUtils.CircleTransform;

/**
 * Created by kpt on 16/2/23.
 */
public class StatusListAdapter extends RecyclerView.Adapter {

    private List<Status> dataList;

    private Picasso picasso;

    public StatusListAdapter(List dataList) {
        this.dataList = dataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        picasso = Picasso.with(parent.getContext());

        return new MyViewHolder(inflater.inflate(R.layout.status_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final Status status = dataList.get(position);
        User user = status.getUser();
        final MyViewHolder viewHolder = (MyViewHolder) holder;
        final Context context = viewHolder.text.getContext();

        viewHolder.text.setText(status.getText());
        viewHolder.name.setText(user.getName());
        viewHolder.id.setText(user.getId());
        viewHolder.time.setText(TimeFormat.format(status.getCreatedAt()));
        viewHolder.source.setText(context.getString(R.string.status_from) + HtmlParse.cleanAllTag(status.getSource()));

        int imgVisibility = status.getUser().isFollowing() ? View.INVISIBLE : View.VISIBLE;
        viewHolder.follow.setVisibility(imgVisibility);

        if (status.isFavorited()) {
            viewHolder.favorite.setText(context.getString(R.string.status_favorited));
        } else {
            viewHolder.favorite.setText(context.getString(R.string.status_favorite));
        }

        try {

        } catch (Exception e) {

        }

        viewHolder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(StatusDetailActivity.newIntent(context, status.getId()));
            }
        });

        viewHolder.profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(UserInfoActivity.newIntent(context, status.getUserId()));
            }
        });

        viewHolder.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status.isFavorited()) {
                    destroyFavorite(status, context, viewHolder.favorite);
                } else {
                    createFavorite(status, context, viewHolder.favorite);
                }
            }
        });

        Picasso.with(viewHolder.profileImg.getContext())
                .load(user.getProfileImageUrl())
                .transform(new CircleTransform())
                .into(viewHolder.profileImg);

    }

    private void createFavorite(Status status,final Context context, final TextView textView) {
        UserService.destroyFavorite(status.getId(), new UserService.CallBack() {
            @Override
            public void success() {
                textView.setText(context.getString(R.string.status_favorite));
            }

            @Override
            public void failure() {
                MyToast.showToast(context, "取消收藏失败, 请重试");
            }
        });
    }

    private void destroyFavorite(Status status, final Context context, final TextView textView) {
        UserService.createFavorite(status.getId(), new UserService.CallBack() {
            @Override
            public void success() {
                textView.setText(context.getString(R.string.status_favorited));
            }

            @Override
            public void failure() {
                MyToast.showToast(context, "收藏失败, 请重试");
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.status_item)View parent;
        @Bind(R.id.status_user_avatar)ImageView profileImg;
        @Bind(R.id.status_img)ImageView img;
        @Bind(R.id.status_user_name)TextView name;
        @Bind(R.id.status_user_id)TextView id;
        @Bind(R.id.status_time)TextView time;
        @Bind(R.id.status_resource)TextView source;
        @Bind(R.id.status_text)TextView text;
//
        @Bind(R.id.status_ic_follow)ImageView follow;
        @Bind(R.id.status_ic_reply)TextView reply;
        @Bind(R.id.status_ic_star)TextView favorite;
        @Bind(R.id.status_ic_repost)TextView repost;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
