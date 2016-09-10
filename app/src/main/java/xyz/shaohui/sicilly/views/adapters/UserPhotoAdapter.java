package xyz.shaohui.sicilly.views.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoView;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.data.models.Status;

/**
 * Created by shaohui on 16/8/21.
 */
public class UserPhotoAdapter extends RecyclerView.Adapter<UserPhotoAdapter.PhotoViewHolder> {

    private List<Status> statusList;

    public UserPhotoAdapter(List<Status> statusList) {
        this.statusList = statusList;
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

        holder.text.setText(status.text());
        Glide.with(context)
                .load(status.photo().getImageurl())
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return statusList.size();
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.photo_image)ImageView image;
        @BindView(R.id.photo_text)TextView text;
        private View parent;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            parent = itemView;
        }
    }

}
