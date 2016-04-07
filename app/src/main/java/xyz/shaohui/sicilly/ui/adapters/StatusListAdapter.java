package xyz.shaohui.sicilly.ui.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
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

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.SicillyFactory;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.data.models.User;
import xyz.shaohui.sicilly.data.services.RetrofitService;
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

        if (status.getUser().isFollowing()
                || status.getUserId().equals(SicillyFactory.getCurrentUser().getId())) {
            viewHolder.follow.setVisibility(View.INVISIBLE);
            viewHolder.more.setVisibility(View.VISIBLE);
        } else {
            viewHolder.follow.setVisibility(View.VISIBLE);
            viewHolder.more.setVisibility(View.INVISIBLE);
        }

        if (status.isFavorited()) {
            viewHolder.favorite.setImageResource(R.drawable.ic_stared);
        } else {
            viewHolder.favorite.setImageResource(R.drawable.ic_star);
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
                        context.startActivity(StatusDetailActivity.newIntent(context, new Gson().toJson(status)));
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
                    case R.id.status_ic_follow:
                        followUser(context, viewHolder, status);
                        break;
                    case R.id.status_ic_more:
                        showDialogMore(context, status);
                        break;
                }
            }
        };

        viewHolder.parent.setOnClickListener(mListener);
        viewHolder.profileImg.setOnClickListener(mListener);
        viewHolder.reply.setOnClickListener(mListener);
        viewHolder.repost.setOnClickListener(mListener);
        viewHolder.favorite.setOnClickListener(mListener);
        viewHolder.follow.setOnClickListener(mListener);
        viewHolder.name.setOnClickListener(mListener);
        viewHolder.id.setOnClickListener(mListener);
        viewHolder.text.setOnClickListener(mListener);
        viewHolder.more.setOnClickListener(mListener);

        Picasso.with(viewHolder.profileImg.getContext())
                .load(user.getProfileImageUrl())
                .transform(new CircleTransform())
                .into(viewHolder.profileImg);

    }

    private void showDialogMore(final Context context, Status status) {
        AlertDialog.Builder  builder = new AlertDialog.Builder(context);
        builder.setItems(new String[]{"关闭", "删除", "分享", "共享"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        MyToast.showToast(context, "关闭");
                        break;
                    case 1:
                        MyToast.showToast(context, "删除");
                        break;
                    case 2:
                        MyToast.showToast(context, "分享");
                        break;
                    case 3:
                        MyToast.showToast(context, "共享");
                        break;
                }
            }
        });
        builder.create().show();
    }

    private void createFavorite(final Status status,final Context context, final ImageView imageView) {
        UserService.createFavorite(status.getId(), new UserService.CallBack() {
            @Override
            public void success() {
                imageView.setImageResource(R.drawable.ic_stared);
                status.setFavorited(true);
            }

            @Override
            public void failure() {
                MyToast.showToast(context, "收藏失败, 请重试");
            }
        });
    }

    private void destroyFavorite(final Status status, final Context context, final ImageView imageView) {
        UserService.destroyFavorite(status.getId(), new UserService.CallBack() {
            @Override
            public void success() {
                imageView.setImageResource(R.drawable.ic_star);
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

    private void followUser(final Context context, final MyViewHolder viewHolder, Status status){
        String id = status.getUserId();
        UserService.createFollow(status.getUserId(), new UserService.CallBack() {
            @Override
            public void success() {
                MyToast.iconSuccess(context, "关注成功");
                viewHolder.follow.setVisibility(View.INVISIBLE);
            }

            @Override
            public void failure() {
                MyToast.showToast(context, "貌似出了一点小问题");
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
        @Bind(R.id.status_ic_reply)ImageView reply;
        @Bind(R.id.status_ic_star)ImageView favorite;
        @Bind(R.id.status_ic_repost)ImageView repost;
        @Bind(R.id.status_ic_more)ImageView more;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
