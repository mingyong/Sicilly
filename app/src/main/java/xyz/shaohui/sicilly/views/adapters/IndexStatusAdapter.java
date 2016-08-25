package xyz.shaohui.sicilly.views.adapters;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
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

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.data.DataManager;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.data.models.User;
import xyz.shaohui.sicilly.utils.HtmlUtils;
import xyz.shaohui.sicilly.utils.NoUnderlineSpan;
import xyz.shaohui.sicilly.utils.TimeUtils;
import xyz.shaohui.sicilly.views.activities.UserActivity;

/**
 * Created by shaohui on 16/8/19.
 */
public class IndexStatusAdapter extends RecyclerView.Adapter {

    private List<Status> dataList;
    private int mLastPosition = 0;

    public IndexStatusAdapter(List<Status> dataList) {
        this.dataList = dataList;
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
        final User user = status.getUser();
        StatusViewHolder viewHolder = (StatusViewHolder) holder;
        final Context context = viewHolder.avatar.getContext();

        viewHolder.name.setText(user.getScreen_name());
        viewHolder.createdTime.setText(TimeUtils.simpleFormat(status.getCreated_at()));
        viewHolder.source.setText(HtmlUtils.cleanAllTag(status.getSource()));

        // text
        viewHolder.text.setText(Html.fromHtml(HtmlUtils.switchTag(status.getText())));
        Spannable s = new SpannableString(viewHolder.text.getText());
        s.setSpan(new NoUnderlineSpan(), 0, s.length(), Spanned.SPAN_MARK_MARK);
        viewHolder.text.setText(s);
        viewHolder.text.setMovementMethod(LinkMovementMethod.getInstance());

        Glide.with(context)
                .load(user.getProfile_image_url_large())
                .into(viewHolder.avatar);
        if (status.getPhoto() != null) {
            viewHolder.image.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(status.getPhoto().getLargeurl())
                    .into(viewHolder.image);
            // gif
            if (status.getPhoto().getLargeurl().toLowerCase().endsWith(".gif")) {
                viewHolder.gif.setVisibility(View.VISIBLE);
            } else {
                viewHolder.gif.setVisibility(View.GONE);
            }
        } else {
            viewHolder.image.setVisibility(View.GONE);
        }

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.status_header:
                        context.startActivity(UserActivity.newIntent(context, user.getId()));
                        break;
                    case R.id.action_comment:
                        DataManager.actionComment(context, status.getId());
                        break;
                    case R.id.action_repost:
                        DataManager.actionRepost(context, status.getId());
                        break;
                    case R.id.action_star:
                        DataManager.actionRepost(context, status.getId());
                        break;
                }
            }
        };
        viewHolder.actionComment.setOnClickListener(listener);
        viewHolder.actionRepost.setOnClickListener(listener);
        viewHolder.actionStar.setOnClickListener(listener);
        viewHolder.header.setOnClickListener(listener);

        if (status.getFavorited()) {
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

        @BindView(R.id.user_avatar)ImageView avatar;
        @BindView(R.id.user_name)TextView name;
        @BindView(R.id.status_text)TextView text;
        @BindView(R.id.status_source)TextView source;
        @BindView(R.id.status_image)ImageView image;
        @BindView(R.id.status_time)TextView createdTime;
        @BindView(R.id.action_comment)ImageButton actionComment;
        @BindView(R.id.action_repost)ImageButton actionRepost;
        @BindView(R.id.action_star)ImageButton actionStar;
        @BindView(R.id.flag_gif)TextView gif;
        @BindView(R.id.status_header)RelativeLayout header;

        public StatusViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
