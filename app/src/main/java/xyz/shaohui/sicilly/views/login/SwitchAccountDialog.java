package xyz.shaohui.sicilly.views.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.SicillyApplication;
import xyz.shaohui.sicilly.base.BaseDialogFragment;
import xyz.shaohui.sicilly.data.database.AppUserDbAccessor;
import xyz.shaohui.sicilly.data.models.AppUser;
import xyz.shaohui.sicilly.notification.NotificationUtils;
import xyz.shaohui.sicilly.utils.RxUtils;
import xyz.shaohui.sicilly.views.home.IndexActivity;
import xyz.shaohui.sicilly.views.home.di.HomeComponent;

/**
 * Created by shaohui on 16/9/29.
 */

public class SwitchAccountDialog extends BaseDialogFragment
        implements SwitchAccountListener {

    @BindView(R.id.account_list)
    RecyclerView mRecyclerView;

    @Inject
    AppUserDbAccessor mAppUserDbAccessor;

    private List<AppUser> mAppUsers;

    private AppUserAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HomeComponent component = getComponent(HomeComponent.class);
        component.inject(this);
    }

    @Override
    public int layoutRes() {
        return R.layout.dialog_switch_account;
    }

    @Override
    public void bindView(View view) {
        mAppUsers = new ArrayList<>();
        mAdapter = new AppUserAdapter(mAppUsers, this);

        mRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);

        mAppUserDbAccessor.fetchAllUser().subscribe(appUsers -> {
            mAppUsers.addAll(appUsers);
            mAdapter.notifyDataSetChanged();
        }, RxUtils.ignoreError);
    }

    @OnClick(R.id.action_add)
    void addAccount() {
        new LoginDialogFragment().show(getFragmentManager(), "login");
        dismiss();
    }

    @Override
    public void switchAccount(AppUser user) {
        //mAppUserDbAccessor
        // 切换用户
        // 1. 切换DB中的Active User
        // 2. 切换Application 的 currentUser
        // 3. Service 中自动观察数据库变化 更新Application,
        //      如果不生效, 可以使用AIDL调用, 使Service更新
        // 4. 清除所有的Notification
        // 5. 重启Activity

        mAppUserDbAccessor.switchActiveUser(SicillyApplication.currentAppUser(), user);     //1
        SicillyApplication.setCurrentAppUser(user);         //2
        NotificationUtils.clearAll(getContext());           //4

        startActivity(new Intent(getContext(), IndexActivity.class));       // 5
        getActivity().finish();
    }

    class AppUserAdapter extends RecyclerView.Adapter<AppUserViewHolder> {

        private List<AppUser> dataList;

        private SwitchAccountListener mListener;

        AppUserAdapter(List<AppUser> dataList, SwitchAccountListener listener) {
            this.dataList = dataList;
            this.mListener = listener;
        }

        @Override
        public AppUserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new AppUserViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_account, parent, false));
        }

        @Override
        public void onBindViewHolder(AppUserViewHolder holder, int position) {
            AppUser user = dataList.get(position);

            holder.name.setText(user.name());
            Glide.with(SicillyApplication.getContext()).load(user.avatar()).into(holder.avatar);
            if (SicillyApplication.isSelf(user.id())) {
                holder.dot.setVisibility(View.VISIBLE);
            } else {
                holder.dot.setVisibility(View.INVISIBLE);
            }
            holder.itemView.setOnClickListener(v -> {
                if (!SicillyApplication.isSelf(user.id())) {
                    mListener.switchAccount(user);
                }
            });
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }
    }

    class AppUserViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.user_avatar)
        ImageView avatar;
        @BindView(R.id.user_name)
        TextView name;
        @BindView(R.id.user_dot)
        View dot;

        AppUserViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
