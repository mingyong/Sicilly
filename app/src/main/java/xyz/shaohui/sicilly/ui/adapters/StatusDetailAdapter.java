package xyz.shaohui.sicilly.ui.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.data.models.User;
import xyz.shaohui.sicilly.ui.activities.PhotoActivity;
import xyz.shaohui.sicilly.ui.activities.UserInfoActivity;
import xyz.shaohui.sicilly.utils.HtmlParse;
import xyz.shaohui.sicilly.utils.TextHtmlParse;
import xyz.shaohui.sicilly.utils.TimeFormat;
import xyz.shaohui.sicilly.utils.imageUtils.CircleTransform;

/**
 * Created by kpt on 16/3/28.
 */
public class StatusDetailAdapter extends RecyclerView.Adapter<StatusDetailAdapter.MyViewHolder> {

    private List<Status> dataList;

    public StatusDetailAdapter(List<Status> dataList) {
        this.dataList = dataList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new MyViewHolder(inflater.inflate(R.layout.status_detail_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Status status = dataList.get(position);
        User user = status.getUser();
        final Context context = holder.text.getContext();

        holder.text.setText(TextHtmlParse.updateMainText(status.getText()));
        holder.text.setMovementMethod(LinkMovementMethod.getInstance());

        holder.name.setText(user.getNickName());
        holder.resource.setText("来自" + HtmlParse.cleanAllTag(status.getSource()));
        holder.time.setText(TimeFormat.detailTime(status.getCreatedAt()));

        Picasso.with(holder.avatar.getContext())
                .load(Uri.parse(user.getProfileImageUrl()))
                .transform(new CircleTransform())
                .into(holder.avatar);

        holder.avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(UserInfoActivity.newIntent(context, status.getUserId()));
            }
        });

        // 图片处理
        if (TextUtils.isEmpty(status.getImageLargeUrl())) {
            holder.img.setVisibility(View.GONE);
            return;
        }
        holder.img.setVisibility(View.VISIBLE);

        if (status.getImageLargeUrl().endsWith("gif")) {
            Glide.with(context)
                    .load(Uri.parse(status.getImageLargeUrl()))
                    .asGif()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.img);
        } else {
            Picasso.with(context)
                    .load(Uri.parse(status.getImageLargeUrl()))
                    .into(holder.img);
        }
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(PhotoActivity
                        .newIntent(context, status.getImageUrl(), status.getImageLargeUrl()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.status_text)TextView text;
        @Bind(R.id.status_time)TextView time;
        @Bind(R.id.status_resource)TextView resource;
        @Bind(R.id.user_name)TextView name;
        @Bind(R.id.user_avatar)ImageView avatar;
        @Bind(R.id.status_img)ImageView img;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
