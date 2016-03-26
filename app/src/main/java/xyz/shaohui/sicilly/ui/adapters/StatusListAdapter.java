package xyz.shaohui.sicilly.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
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
import xyz.shaohui.sicilly.ui.activities.CreateStatusActivity;
import xyz.shaohui.sicilly.ui.activities.PhotoActivity;
import xyz.shaohui.sicilly.ui.activities.StatusDetailActivity;
import xyz.shaohui.sicilly.ui.activities.UserInfoActivity;
import xyz.shaohui.sicilly.utils.HtmlParse;
import xyz.shaohui.sicilly.utils.MyToast;
import xyz.shaohui.sicilly.utils.TextHtmlParse;
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
        final User user = status.getUser();
        final MyViewHolder viewHolder = (MyViewHolder) holder;
        final Context context = viewHolder.text.getContext();

        viewHolder.text.setText(TextHtmlParse.updateMainText(status.getText()));
        viewHolder.text.setMovementMethod(LinkMovementMethod.getInstance());

        viewHolder.name.setText(user.getName());
        viewHolder.id.setText(user.getId());
        viewHolder.time.setText(TimeFormat.format(status.getCreatedAt()));
        viewHolder.source.setText(context.getString(R.string.status_from) + HtmlParse.cleanAllTag(status.getSource()));

        int followVisi = status.getUser().isFollowing() ? View.INVISIBLE : View.VISIBLE;
        viewHolder.follow.setVisibility(followVisi);

        if (status.isFavorited()) {
            viewHolder.favorite.setText(context.getString(R.string.status_favorited));
        } else {
            viewHolder.favorite.setText(context.getString(R.string.status_favorite));
        }

        int imgVisi = TextUtils.isEmpty(status.getImageUrl()) ? View.GONE: View.VISIBLE;
        viewHolder.img.setVisibility(imgVisi);
        if (!TextUtils.isEmpty(status.getImageUrl())) {
            Picasso.with(context)
                    .load(Uri.parse(status.getImageUrl()))
                    .into(viewHolder.img);
            viewHolder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(PhotoActivity.newIntent(context, status.getImageLargeUrl()));
                }
            });
        }

        View.OnClickListener mListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.status_text:
                    case R.id.status_item:
                        context.startActivity(StatusDetailActivity.newIntent(context, status.getId()));
                        break;
                    case R.id.status_user_name:
                    case R.id.status_user_id:
                    case R.id.status_user_avatar:
                        context.startActivity(UserInfoActivity.newIntent(context, status.getUserId()));
                        break;
                    case R.id.status_ic_star:
                        if (status.isFavorited()) {
                            destroyFavorite(status, context, viewHolder.favorite);
                        } else {
                            createFavorite(status, context, viewHolder.favorite);
                        }
                        break;
                    case R.id.status_ic_repost:
                        repostStatus(context, status, user);
                        break;
                    case R.id.status_ic_reply:
                        replyStatus(context, status, user);
                        break;
                }
            }
        };

        viewHolder.parent.setOnClickListener(mListener);
        viewHolder.profileImg.setOnClickListener(mListener);
        viewHolder.reply.setOnClickListener(mListener);
        viewHolder.repost.setOnClickListener(mListener);
        viewHolder.favorite.setOnClickListener(mListener);
        viewHolder.name.setOnClickListener(mListener);
        viewHolder.id.setOnClickListener(mListener);
        viewHolder.text.setOnClickListener(mListener);

        Picasso.with(viewHolder.profileImg.getContext())
                .load(user.getProfileImageUrl())
                .transform(new CircleTransform())
                .into(viewHolder.profileImg);

    }

    private void createFavorite(final Status status,final Context context, final TextView textView) {
        UserService.createFavorite(status.getId(), new UserService.CallBack() {
            @Override
            public void success() {
                textView.setText(context.getString(R.string.status_favorited));
                status.setFavorited(true);
            }

            @Override
            public void failure() {
                MyToast.showToast(context, "收藏失败, 请重试");
            }
        });
    }

    private void destroyFavorite(final Status status, final Context context, final TextView textView) {
        UserService.destroyFavorite(status.getId(), new UserService.CallBack() {
            @Override
            public void success() {
                textView.setText(context.getString(R.string.status_favorite));
                status.setFavorited(false);
            }

            @Override
            public void failure() {
                MyToast.showToast(context, "取消收藏失败, 请重试");
            }
        });
    }

    private void repostStatus(Context context, Status status, User user) {
        String str = HtmlParse.cleanAllTag(status.getText());
        String text = " 转@" + user.getNickName() + " " + str;
        Intent intent = CreateStatusActivity.newIntent(context,
                CreateStatusActivity.TYPE_REPOST, status.getId(), text);
        context.startActivity(intent);
    }

    private void replyStatus(Context context, Status status, User user) {
        String text = "@" + user.getNickName() + " ";
        Intent intent = CreateStatusActivity.newIntent(context,
                CreateStatusActivity.TYPE_REPLY, status.getId(), text);
        context.startActivity(intent);
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
