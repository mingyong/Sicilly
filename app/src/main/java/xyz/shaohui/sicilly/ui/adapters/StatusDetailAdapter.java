package xyz.shaohui.sicilly.ui.adapters;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.data.models.User;
import xyz.shaohui.sicilly.utils.HtmlParse;
import xyz.shaohui.sicilly.utils.TextHtmlParse;
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
        Status status = dataList.get(position);
        User user = status.getUser();

        holder.text.setText(TextHtmlParse.updateMainText(status.getText()));
        holder.text.setMovementMethod(LinkMovementMethod.getInstance());

        holder.name.setText(user.getNickName());
        holder.resource.setText(HtmlParse.cleanAllTag(status.getSource()));
        holder.time.setText(status.getCreatedAt());

        Picasso.with(holder.avatar.getContext())
                .load(Uri.parse(user.getProfileImageUrl()))
                .transform(new CircleTransform())
                .into(holder.avatar);
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

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
