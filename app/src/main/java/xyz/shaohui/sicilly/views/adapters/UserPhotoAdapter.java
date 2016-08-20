package xyz.shaohui.sicilly.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

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

    }

    @Override
    public int getItemCount() {
        return statusList.size();
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder {
        public PhotoViewHolder(View itemView) {
            super(itemView);
        }
    }

}
