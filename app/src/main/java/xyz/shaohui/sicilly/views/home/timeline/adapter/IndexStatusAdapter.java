package xyz.shaohui.sicilly.views.home.timeline.adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import java.util.List;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.SicillyApplication;
import xyz.shaohui.sicilly.data.DataManager;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.data.models.User;
import xyz.shaohui.sicilly.utils.HtmlUtils;
import xyz.shaohui.sicilly.utils.NoUnderlineSpan;
import xyz.shaohui.sicilly.utils.TimeUtils;
import xyz.shaohui.sicilly.views.activities.PictureActivity;
import xyz.shaohui.sicilly.views.activities.UserActivity;
import xyz.shaohui.sicilly.views.home.timeline.TimelineItemListener;

/**
 * Created by shaohui on 16/8/19.
 */
public class IndexStatusAdapter extends RecyclerView.Adapter {

    private List<Status> dataList;
    private TimelineItemListener mListener;
    private int mLastPosition = 0;

    public IndexStatusAdapter(List<Status> dataList, TimelineItemListener listener) {
        this.dataList = dataList;
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_status_index, parent, false);
        return new StatusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Status status = dataList.get(position);
        final User user = status.user();
        final StatusViewHolder viewHolder = (StatusViewHolder) holder;
        final Context context = viewHolder.avatar.getContext();

        viewHolder.name.setText(user.screen_name());
        viewHolder.createdTime.setText(TimeUtils.simpleFormat(status.created_at()));
        viewHolder.source.setText(HtmlUtils.cleanAllTag(status.source()));

        // text
        viewHolder.text.setText(Html.fromHtml(HtmlUtils.switchTag(status.text())));
        Spannable s = new SpannableString(viewHolder.text.getText());
        s.setSpan(new NoUnderlineSpan(), 0, s.length(), Spanned.SPAN_MARK_MARK);
        viewHolder.text.setText(s);
        viewHolder.text.setMovementMethod(LinkMovementMethod.getInstance());

        Glide.with(context).load(user.profile_image_url_large()).into(viewHolder.avatar);
        if (status.photo() != null) {
            viewHolder.image.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(status.photo().getLargeurl())
                    .asBitmap()
                    .placeholder(
                            context.getResources().getDrawable(R.drawable.drawable_plcae_holder))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(viewHolder.image);
            viewHolder.image.setOnClickListener(v -> {
                Intent intent =
                        PictureActivity.newIntent(context, status.photo().getLargeurl());
                if (android.os.Build.VERSION.SDK_INT
                        >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options =
                            ActivityOptions.makeSceneTransitionAnimation((Activity) context,
                                    viewHolder.image, "image");
                    context.startActivity(intent, options.toBundle());
                } else {
                    context.startActivity(intent);
                }
            });
            // gif
            if (status.photo().getLargeurl().toLowerCase().endsWith(".gif")) {
                viewHolder.gif.setVisibility(View.VISIBLE);
            } else {
                viewHolder.gif.setVisibility(View.GONE);
            }
        } else {
            viewHolder.image.setVisibility(View.GONE);
        }

        // action

        if (SicillyApplication.isSelf(status.user().id())) {
            viewHolder.actionDelete.setVisibility(View.VISIBLE);
        } else {
            viewHolder.actionDelete.setVisibility(View.GONE);
        }

        View.OnClickListener listener = v -> {
            switch (v.getId()) {
                case R.id.status_header:
                    context.startActivity(UserActivity.newIntent(context, user.id()));
                    break;
                case R.id.action_comment:
                    mListener.opComment(status);
                    break;
                case R.id.action_repost:
                    mListener.opRepost(status);
                    break;
                case R.id.action_star:
                    mListener.opStar(status, position);
                    break;
                case R.id.action_delete:
                    mListener.opDelete(status, position);
                    break;
            }
        };
        viewHolder.actionComment.setOnClickListener(listener);
        viewHolder.actionRepost.setOnClickListener(listener);
        viewHolder.actionStar.setOnClickListener(listener);
        viewHolder.actionDelete.setOnClickListener(listener);
        viewHolder.header.setOnClickListener(listener);

        if (status.favorited()) {
            viewHolder.actionStar.setImageResource(R.drawable.ic_star_fill);
        } else {
            viewHolder.actionStar.setImageResource(R.drawable.ic_star);
        }

        // 动画效果
        if (holder.getAdapterPosition() > mLastPosition) {
            startAnimator(viewHolder.itemView);
            mLastPosition = holder.getAdapterPosition();
        } else {
            clearAnimator(viewHolder.itemView);
        }
    }

    private void startAnimator(View view) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0.9f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0.9f, 1f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY);
        animatorSet.setDuration(100).start();
    }

    private void clearAnimator(View view) {
        ViewCompat.setScaleX(view, 1);
        ViewCompat.setScaleY(view, 1);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class StatusViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.user_avatar)
        ImageView avatar;
        @BindView(R.id.user_name)
        TextView name;
        @BindView(R.id.status_text)
        TextView text;
        @BindView(R.id.status_source)
        TextView source;
        @BindView(R.id.status_image)
        ImageView image;
        @BindView(R.id.status_time)
        TextView createdTime;
        @BindView(R.id.action_delete)
        ImageButton actionDelete;
        @BindView(R.id.action_comment)
        ImageButton actionComment;
        @BindView(R.id.action_repost)
        ImageButton actionRepost;
        @BindView(R.id.action_star)
        ImageButton actionStar;
        @BindView(R.id.flag_gif)
        TextView gif;
        @BindView(R.id.status_header)
        RelativeLayout header;

        public StatusViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
