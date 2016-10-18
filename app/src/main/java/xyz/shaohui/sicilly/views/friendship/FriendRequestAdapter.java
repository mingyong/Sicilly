package xyz.shaohui.sicilly.views.friendship;

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
import xyz.shaohui.sicilly.views.user_info.UserActivity;

/**
 * Created by shaohui on 16/10/18.
 */

public class FriendRequestAdapter
        extends RecyclerView.Adapter<FriendRequestAdapter.FriendRequestViewHolder> {

    private List<User> mUserList;

    private OpListener mListener;

    FriendRequestAdapter(List<User> userList, OpListener listener) {
        mUserList = userList;
        mListener = listener;
    }

    @Override
    public FriendRequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FriendRequestViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_friend_request, parent, false));
    }

    @Override
    public void onBindViewHolder(FriendRequestViewHolder holder, int position) {
        User user = mUserList.get(position);
        Context context = holder.itemView.getContext();

        Glide.with(context)
                .load(user.profile_image_url_large())
                .placeholder(context.getResources().getDrawable(R.drawable.drawable_plcae_holder))
                .into(holder.avatar);

        holder.name.setText(user.screen_name());
        holder.brief.setText(user.description().replaceAll("\\n", " "));

        holder.itemView.setOnClickListener(
                v -> context.startActivity(UserActivity.newIntent(context, user.id())));
        holder.actionAccept.setOnClickListener(v -> mListener.acceptRequest(position, user));
        holder.actionDeny.setOnClickListener(v -> mListener.denyRequest(position, user));
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    class FriendRequestViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.user_avatar)
        RoundedImageView avatar;
        @BindView(R.id.user_name)
        TextView name;
        @BindView(R.id.user_brief)
        TextView brief;
        @BindView(R.id.action_accept)
        ImageButton actionAccept;
        @BindView(R.id.action_deny)
        ImageButton actionDeny;

        FriendRequestViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OpListener {

        void acceptRequest(int position, User user);

        void denyRequest(int position, User user);
    }
}
