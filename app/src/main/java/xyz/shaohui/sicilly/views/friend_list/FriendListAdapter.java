package xyz.shaohui.sicilly.views.friend_list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import java.util.List;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.data.models.User;

/**
 * Created by shaohui on 16/10/14.
 */

public class FriendListAdapter
        extends RecyclerView.Adapter<FriendListAdapter.FriendListViewHolder> {

    private List<User> dataList;
    private boolean isTiny;
    private Action mAction;

    public FriendListAdapter(List<User> dataList, boolean isTiny, Action action) {
        this.dataList = dataList;
        this.isTiny = isTiny;
        mAction = action;
    }

    @Override
    public FriendListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(isTiny ? R.layout.item_friend_list_tiny : R.layout.item_friend_list_full,
                        parent, false);
        return new FriendListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FriendListViewHolder holder, int position) {
        User user = dataList.get(position);
        Context context = holder.itemView.getContext();

        Glide.with(context)
                .load(isTiny ? user.profile_image_url() : user.profile_image_url_large())
                .placeholder(context.getResources().getDrawable(R.drawable.drawable_plcae_holder))
                .into(holder.avatar);

        holder.name.setText(user.screen_name());
        holder.brief.setText(user.description().replaceAll("\\n", " "));

        if (user.following()) {
            holder.actionFollow.setVisibility(View.GONE);
        } else {
            holder.actionFollow.setVisibility(View.GONE);
            holder.actionFollow.setOnClickListener(v -> mAction.actionFollow(position, user));
        }

        holder.itemView.setOnClickListener(v -> mAction.actionClick(user));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class FriendListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.user_avatar)
        RoundedImageView avatar;
        @BindView(R.id.user_name)
        TextView name;
        @BindView(R.id.user_brief)
        TextView brief;
        @BindView(R.id.action_follow)
        ImageButton actionFollow;

        public FriendListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    interface Action {
        void actionFollow(int position, User user);

        void actionClick(User user);
    }

}
