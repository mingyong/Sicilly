package xyz.shaohui.sicilly.ui.adapters;

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
import xyz.shaohui.sicilly.utils.imageUtils.CircleTransform;

/**
 * Created by kpt on 16/3/15.
 */
public class LightUserAdapter extends RecyclerView.Adapter {

    private List<User> dataList;

    public LightUserAdapter(List<User> dataList) {
        this.dataList = dataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new MyViewHolder(inflater.inflate(R.layout.light_user_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder viewHolder = (MyViewHolder) holder;
        User user = dataList.get(position);

        viewHolder.name.setText(user.getNickName());
        Picasso.with(viewHolder.name.getContext())
                .load(Uri.parse(user.getProfileImageUrl()))
                .transform(new CircleTransform())
                .into(viewHolder.avatar);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.user_avatar)ImageView avatar;
        @Bind(R.id.user_name)TextView name;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
