package xyz.shaohui.sicilly.views.user_info.photo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import java.util.List;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.utils.HtmlUtils;

/**
 * Created by shaohui on 16/8/21.
 */
public class UserPhotoAdapter extends RecyclerView.Adapter<UserPhotoAdapter.PhotoViewHolder> {

    private List<Status> statusList;

    private PhotoItemListener mPhotoItemListener;

    public UserPhotoAdapter(List<Status> statusList, PhotoItemListener photoItemListener) {
        this.statusList = statusList;
        this.mPhotoItemListener = photoItemListener;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PhotoViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_photo, parent, false));
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {
        Status status = statusList.get(position);
        Context context = holder.parent.getContext();

        holder.text.setText(HtmlUtils.cleanAllTag(status.text()));
        Glide.with(context)
                .load(status.photo().getImageurl())
                .placeholder(context.getResources().getDrawable(R.drawable.drawable_plcae_holder))
                .into(holder.image);

        holder.itemView.setOnClickListener(
                v -> mPhotoItemListener.onItemClick(status.photo().getLargeurl(), status.text()));
    }

    @Override
    public int getItemCount() {
        return statusList.size();
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.photo_image)
        ImageView image;
        @BindView(R.id.photo_text)
        TextView text;
        private View parent;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            parent = itemView;
        }
    }

    interface PhotoItemListener {

        void onItemClick(String url, String text);
    }
}
