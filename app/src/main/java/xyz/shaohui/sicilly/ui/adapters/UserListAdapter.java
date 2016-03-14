package xyz.shaohui.sicilly.ui.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
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
import xyz.shaohui.sicilly.data.models.User;
import xyz.shaohui.sicilly.utils.MyToast;
import xyz.shaohui.sicilly.utils.imageUtils.CircleTransform;

/**
 * Created by kpt on 16/3/14.
 */
public class UserListAdapter extends RecyclerView.Adapter {

    private List<User> dataList;

    public UserListAdapter(List<User> dataList) {
        this.dataList = dataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new MyViewHolder(inflater.inflate(R.layout.user_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MyViewHolder viewHolder = (MyViewHolder) holder;
        User user = dataList.get(position);
        final Context context = viewHolder.name.getContext();

        viewHolder.name.setText(user.getNickName());
        viewHolder.id.setText(user.getId());
        viewHolder.text.setText(user.getFirstStatus().getText());
        Picasso.with(context)
                .load(Uri.parse(user.getProfileImageUrl()))
                .transform(new CircleTransform())
                .into(viewHolder.avatar);

        if (user.isFollowing()) {
            viewHolder.follow.setText("已关注");
        } else {
            viewHolder.follow.setText("点击关注");
        }
        viewHolder.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyToast.showToast(context, viewHolder.follow.getText().toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.user_name)TextView name;
        @Bind(R.id.user_id)TextView id;
        @Bind(R.id.user_avatar)ImageView avatar;
        @Bind(R.id.user_text)TextView text;
        @Bind(R.id.action_follow)TextView follow;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
