package xyz.shaohui.sicilly.views.feed.adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.View;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import java.util.List;
import me.shaohui.sicillylib.utils.ToastUtils;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.data.models.User;
import xyz.shaohui.sicilly.utils.HtmlUtils;
import xyz.shaohui.sicilly.utils.NoUnderlineSpan;
import xyz.shaohui.sicilly.utils.SimpleUtils;
import xyz.shaohui.sicilly.utils.TimeUtils;
import xyz.shaohui.sicilly.views.create_status.CreateStatusActivity;
import xyz.shaohui.sicilly.views.create_status.CreateStatusDialogBuilder;
import xyz.shaohui.sicilly.views.photo.PictureActivity;

/**
 * Created by shaohui on 2016/11/27.
 */

public abstract class BaseFeedAdapter<T extends BaseFeedViewHolder> extends RecyclerView.Adapter<T> {

    public List<Status> dataList;
    public int mLastPosition = 0;
    public FragmentManager mFragmentManager;

    public BaseFeedAdapter(List<Status> dataList, FragmentManager fragmentManager) {
        this.dataList = dataList;
        mFragmentManager = fragmentManager;
    }

    @Override
    public void onBindViewHolder(T holder, int position) {
        final Status status = dataList.get(position);
        final User user = status.user();
        final Context context = holder.itemView.getContext();

        holder.createdTime.setText(TimeUtils.simpleFormat(status.created_at()));

        // text
        holder.text.setText(Html.fromHtml(HtmlUtils.switchTag(status.text())));
        Spannable s = new SpannableString(holder.text.getText());
        s.setSpan(new NoUnderlineSpan(), 0, s.length(), Spanned.SPAN_MARK_MARK);
        holder.text.setText(s);
        holder.text.setMovementMethod(LinkMovementMethod.getInstance());
        holder.text.setOnLongClickListener(v -> {
            SimpleUtils.copyText(context, HtmlUtils.cleanAllTag(status.text()));
            ToastUtils.showToast(context, R.string.copy_text_tip);
            return true;
        });

        if (status.photo() != null) {
            holder.image.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(status.photo().getLargeurl())
                    .asBitmap()
                    .placeholder(
                            context.getResources().getDrawable(R.drawable.drawable_plcae_holder))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.image);
            holder.image.setOnClickListener(v -> {
                Intent intent = PictureActivity.newIntent(context, status.photo().getLargeurl());
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP
                        && !status.photo().getLargeurl().toLowerCase().endsWith(".gif")) {
                    ActivityOptions options =
                            ActivityOptions.makeSceneTransitionAnimation((Activity) context,
                                    holder.image, "image");
                    context.startActivity(intent, options.toBundle());
                } else {
                    context.startActivity(intent);
                }
            });
            // gif
            if (status.photo().getLargeurl().toLowerCase().endsWith(".gif")) {
                holder.gif.setVisibility(View.VISIBLE);
            } else {
                holder.gif.setVisibility(View.GONE);
            }
        } else {
            holder.image.setVisibility(View.GONE);
            holder.gif.setVisibility(View.GONE);
        }
    }

    protected void startAnimator(View view) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0.9f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0.9f, 1f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY);
        animatorSet.setDuration(100).start();
    }

    protected void clearAnimator(View view) {
        ViewCompat.setScaleX(view, 1);
        ViewCompat.setScaleY(view, 1);
    }

    protected void replyStatus(Status status) {
        new CreateStatusDialogBuilder(CreateStatusActivity.TYPE_REPLY).originStatus(status)
                .build()
                .show(mFragmentManager);
    }

    protected void repostStatus(Status status) {
        new CreateStatusDialogBuilder(CreateStatusActivity.TYPE_REPOST).originStatus(status)
                .build()
                .show(mFragmentManager);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
