package xyz.shaohui.sicilly.views.status_detail;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
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
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.data.models.User;
import xyz.shaohui.sicilly.utils.HtmlUtils;
import xyz.shaohui.sicilly.utils.NoUnderlineSpan;
import xyz.shaohui.sicilly.utils.TimeUtils;
import xyz.shaohui.sicilly.views.home.timeline.adapter.IndexStatusAdapter;
import xyz.shaohui.sicilly.views.photo.PictureActivity;
import xyz.shaohui.sicilly.views.user_info.UserActivity;

/**
 * Created by shaohui on 16/9/30.
 */

public class StatusDetailAdapter
        extends RecyclerView.Adapter<StatusDetailAdapter.StatusDetailViewHolder> {

    private List<Status> dataList;

    public StatusDetailAdapter(List<Status> dataList) {
        this.dataList = dataList;
    }

    @Override
    public StatusDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StatusDetailViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_status_detail, parent, false));
    }

    @Override
    public void onBindViewHolder(StatusDetailViewHolder viewHolder, int position) {

        final Status status = dataList.get(position);
        final User user = status.user();
        final Context context = viewHolder.avatar.getContext();

        viewHolder.name.setText(user.screen_name());
        viewHolder.createdTime.setText(TimeUtils.timeFormat(status.created_at()));
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
            viewHolder.gif.setVisibility(View.GONE);
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
                    //mListener.opComment(status);
                    break;
                case R.id.action_repost:
                    //mListener.opRepost(status);
                    break;
                case R.id.action_star:
                    //mListener.opStar(status, position);
                    break;
                case R.id.action_delete:
                    //mListener.opDelete(status, position);
                    break;
            }
        };
        viewHolder.actionComment.setOnClickListener(listener);
        viewHolder.actionRepost.setOnClickListener(listener);
        viewHolder.actionStar.setOnClickListener(listener);
        viewHolder.actionDelete.setOnClickListener(listener);
        viewHolder.header.setOnClickListener(listener);

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class StatusDetailViewHolder extends RecyclerView.ViewHolder {

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

        StatusDetailViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

