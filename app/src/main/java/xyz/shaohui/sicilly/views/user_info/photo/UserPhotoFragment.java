package xyz.shaohui.sicilly.views.user_info.photo;

import android.support.annotation.NonNull;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import butterknife.BindView;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import me.shaohui.scrollablelayout.ScrollableHelper;
import me.shaohui.vistarecyclerview.VistaRecyclerView;
import me.shaohui.vistarecyclerview.decoration.SpacingDecoration;
import org.greenrobot.eventbus.EventBus;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.base.BaseFragment;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.views.photo.PictureActivity;
import xyz.shaohui.sicilly.views.user_info.di.UserInfoComponent;
import xyz.shaohui.sicilly.views.user_info.photo.mvp.UserPhotoPresenter;
import xyz.shaohui.sicilly.views.user_info.photo.mvp.UserPhotoView;

@FragmentWithArgs
public class UserPhotoFragment extends BaseFragment<UserPhotoView, UserPhotoPresenter>
        implements UserPhotoView, ScrollableHelper.ScrollableContainer,
        UserPhotoAdapter.PhotoItemListener {

    public static final String TAG = "UserPhotoFragment";

    @BindView(R.id.recycler)
    VistaRecyclerView recyclerView;

    @Arg
    String userId;

    @Inject
    EventBus mBus;

    private List<Status> statusList;

    private int mPage;

    @NonNull
    @Override
    public EventBus getBus() {
        return mBus;
    }

    @Override
    public void injectDependencies() {
        UserInfoComponent component = getComponent(UserInfoComponent.class);
        component.inject(this);
        presenter = component.userPhotoPresenter();
    }

    @Override
    public int layoutRes() {
        return R.layout.fragment_photo_list;
    }

    @Override
    public void bindViews(View view) {
        super.bindViews(view);
        statusList = new ArrayList<>();
        UserPhotoAdapter adapter = new UserPhotoAdapter(statusList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SpacingDecoration(16, 16));
        recyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setOnMoreListener((total, left, current) -> {
            if (statusList.size() > 0) {
                presenter.fetchMorePhoto(userId, ++mPage, statusList.get(statusList.size() - 1));
            }
        }, 6);

        mPage = 1;
        presenter.fetchPhoto(userId);
    }

    @Override
    public View getScrollableView() {
        return recyclerView.getRecycler();
    }

    @Override
    public void showPhotoStatus(List<Status> statuses) {
        statusList.addAll(statuses);
        recyclerView.notifyDataSetChanged();
    }

    @Override
    public void showMorePhotoStatus(List<Status> statuses) {
        statusList.addAll(statuses);
        recyclerView.notifyDataSetChanged();
    }

    @Override
    public void loadEmpty() {
        recyclerView.showEmptyView();
    }

    @Override
    public void loadError() {
        recyclerView.showErrorView();
    }

    @Override
    public void loadMoreError() {
        mPage--;
        recyclerView.loadMoreFailure();
    }

    @Override
    public void loadNoMore() {
        recyclerView.loadNoMore();
    }

    @Override
    public void onItemClick(String url, String text) {
        startActivity(PictureActivity.newIntent(getContext(), url, text));
    }
}
