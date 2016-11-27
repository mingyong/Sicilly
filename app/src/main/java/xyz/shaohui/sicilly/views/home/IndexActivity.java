package xyz.shaohui.sicilly.views.home;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.flyco.tablayout.widget.MsgView;
import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import com.tencent.tauth.Tencent;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import me.shaohui.sicillylib.utils.ToastUtils;
import me.shaohui.vistashareutil.VistaShareUtil;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import retrofit2.Retrofit;
import rx.android.schedulers.AndroidSchedulers;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.SicillyApplication;
import xyz.shaohui.sicilly.base.BaseActivity;
import xyz.shaohui.sicilly.base.HasComponent;
import xyz.shaohui.sicilly.data.SPDataManager;
import xyz.shaohui.sicilly.data.database.FeedbackDbAccessor;
import xyz.shaohui.sicilly.event.FriendRequestEvent;
import xyz.shaohui.sicilly.event.HomeMessageEvent;
import xyz.shaohui.sicilly.event.HomeUpdateEvent;
import xyz.shaohui.sicilly.event.MentionEvent;
import xyz.shaohui.sicilly.event.MessageEvent;
import xyz.shaohui.sicilly.leanCloud.service.RemoteService;
import xyz.shaohui.sicilly.service.SicillyService;
import xyz.shaohui.sicilly.service.aidl.IEventListener;
import xyz.shaohui.sicilly.service.aidl.ISicillyService;
import xyz.shaohui.sicilly.utils.RxUtils;
import xyz.shaohui.sicilly.views.create_status.DialogController;
import xyz.shaohui.sicilly.views.home.chat.MessageFragment;
import xyz.shaohui.sicilly.views.home.di.DaggerHomeComponent;
import xyz.shaohui.sicilly.views.home.di.HomeComponent;
import xyz.shaohui.sicilly.views.home.profile.ProfileFragment;
import xyz.shaohui.sicilly.views.home.test.TimelineFragment;
import xyz.shaohui.sicilly.views.home.timeline.HomeTimelineFragment;
import xyz.shaohui.sicilly.views.home.timeline.HomeTimelineFragmentBuilder;

public class IndexActivity extends BaseActivity
        implements HasComponent<HomeComponent>, DialogController {

    public static final int ACTION_CHAT = 1;
    public static final int ACTION_MENTION = 2;
    public static final int ACTION_USER = 3;
    public static final int ACTION_NONE = 0;

    @BindView(R.id.bottom_tab)
    CommonTabLayout bottomTab;

    @Inject
    EventBus mBus;

    @Inject
    Retrofit mRetrofit;

    @Inject
    FeedbackDbAccessor mFeedbackDbAccessor;

    private HomeComponent mComponent;

    private ServiceConnection mServiceConnection;

    private static final int REQUEST_PERMISSION_CODE = 0;

    private String downloadUrl;

    private ArrayList<Fragment> mFragments;

    private int mAction;

    public static Intent newIntent(Context context, int action) {
        Intent intent = new Intent(context, IndexActivity.class);
        intent.putExtra("action", action);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        ButterKnife.bind(this);
        bottomTab = (CommonTabLayout) findViewById(R.id.bottom_tab);

        initBottomTab(savedInstanceState);

        // 检查更新
        checkUpdate();

        // 激活用户
        RemoteService.activeUser(SicillyApplication.currentUId(),
                SicillyApplication.currentAppUser().name(), SicillyApplication.getRegId());

        // 启动Service 监听
        startService(new Intent(this, SicillyService.class));
        bindService();

        // 保存CurrentUser
        SPDataManager.checkAndSaveAppUser(SicillyApplication.currentAppUser());
    }

    /**
     * 监听Service Event
     */

    private ISicillyService mService;

    private IEventListener mListener;

    private void bindService() {
        mListener = new IEventListener.Stub() {
            @Override
            public void onEvent(int type, int count) throws RemoteException {
                int origin;
                switch (type) {
                    case SicillyService.EVENT_TYPE_MENTION:
                        origin = SPDataManager.getInt(SPDataManager.SP_KEY_MENTION, 0);
                        SPDataManager.setInt(SPDataManager.SP_KEY_MENTION, count + origin, false);
                        mBus.post(new MentionEvent(count));
                        mBus.post(new HomeMessageEvent(true));
                        break;
                    case SicillyService.EVENT_TYPE_MESSAGE:
                        origin = SPDataManager.getInt(SPDataManager.SP_KEY_MESSAGE, 0);
                        SPDataManager.setInt(SPDataManager.SP_KEY_MESSAGE, count + origin, false);
                        mBus.post(new MessageEvent(count));
                        mBus.post(new HomeMessageEvent(true));
                        break;
                    case SicillyService.EVENT_TYPE_REQUEST:
                        origin = SPDataManager.getInt(SPDataManager.SP_KEY_FRIEND_REQUEST, 0);
                        if (origin != count) {
                            SPDataManager.setInt(SPDataManager.SP_KEY_FRIEND_REQUEST, count,
                                    false);
                            mBus.post(new FriendRequestEvent(count));
                        }
                        break;
                }
            }
        };

        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mService = ISicillyService.Stub.asInterface(service);
                try {
                    mService.registerListener(mListener);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };

        bindService(new Intent(this, SicillyService.class), mServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        unbindService(mServiceConnection);
        super.onDestroy();
    }

    @Override
    public void initializeInjector() {
        mComponent = DaggerHomeComponent.builder().appComponent(getAppComponent()).build();
        mComponent.inject(this);
    }

    @Override
    public EventBus getBus() {
        return mBus;
    }

    private void initBottomTab(Bundle savedInstance) {
        ArrayList<CustomTabEntity> tabData = new ArrayList<>();
        tabData.add(new TabEntity(getString(R.string.bottom_tab_home), R.drawable.ic_home_selected,
                R.drawable.ic_home));
        tabData.add(new TabEntity(getString(R.string.bottom_tab_message),
                R.drawable.ic_message_selected, R.drawable.ic_message));
        tabData.add(new TabEntity(getString(R.string.bottom_tab_user), R.drawable.ic_user_selected,
                R.drawable.ic_user));

        //if (savedInstance == null) {
        //    mFragments = new ArrayList<>();
        //    mFragments.add(HomeTimelineFragmentBuilder.newHomeTimelineFragment(
        //            HomeTimelineFragment.TYPE_HOME));
        //    mFragments.add(new MessageFragment());
        //    mFragments.add(new ProfileFragment());
        //}
        mFragments = new ArrayList<>();
        //mFragments.add(HomeTimelineFragmentBuilder.newHomeTimelineFragment(
        //        HomeTimelineFragment.TYPE_HOME));
        mFragments.add(new TimelineFragment());
        mFragments.add(new MessageFragment());
        mFragments.add(new ProfileFragment());

        bottomTab.setTabData(tabData, this, R.id.main_frame, mFragments);
        bottomTab.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {

            }

            @Override
            public void onTabReselect(int position) {
                if (position == 0) {
                    mBus.post(new HomeUpdateEvent());
                }
            }
        });
    }

    /**
     * 监听更新BottomTabMessage
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void subscirbeMessaegTab(HomeMessageEvent event) {
        if (event.show) {
            bottomTab.showDot(1);
            MsgView msgView = bottomTab.getMsgView(1);
            msgView.setBackgroundColor(getResources().getColor(R.color.red));
        } else {
            bottomTab.hideMsg(1);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void subscribeProfileTab(FriendRequestEvent event) {
        checkForShowProfileBadge();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkForShowProfileBadge();

        checkForShowMessageBadge();
    }

    private void checkForShowProfileBadge() {
        mFeedbackDbAccessor.unreadCount()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(integer -> {
                    if (integer > 0
                            || SPDataManager.getInt(SPDataManager.SP_KEY_FRIEND_REQUEST, 0) > 0) {
                        bottomTab.showDot(2);
                        MsgView msgView = bottomTab.getMsgView(2);
                        msgView.setBackgroundColor(getResources().getColor(R.color.red));
                    } else {
                        bottomTab.hideMsg(2);
                    }
                }, RxUtils.ignoreError);
    }

    private void checkForShowMessageBadge() {
        if (SPDataManager.getInt(SPDataManager.SP_KEY_MENTION, 0) > 0
                || SPDataManager.getInt(SPDataManager.SP_KEY_MESSAGE, 0) > 0) {
            bottomTab.showDot(1);
            MsgView msgView = bottomTab.getMsgView(1);
            msgView.setBackgroundColor(getResources().getColor(R.color.red));
        } else {
            bottomTab.hideMsg(1);
        }
    }

    /**
     * 检查更新
     */
    public void checkUpdate() {
        PgyUpdateManager.register(this, new UpdateManagerListener() {
            @Override
            public void onNoUpdateAvailable() {
                Log.i("TAG", "没有新版本");
            }

            @Override
            public void onUpdateAvailable(String s) {
                final AppBean appBean = getAppBeanFromString(s);
                downloadUrl = appBean.getDownloadURL();

                new MaterialDialog.Builder(IndexActivity.this).title(R.string.update_version)
                        .content(appBean.getReleaseNote())
                        .positiveText(R.string.update_confirm)
                        .negativeText(R.string.update_cancel)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog,
                                    @NonNull DialogAction which) {
                                if (requestPermission()) {
                                    startDownloadTask(IndexActivity.this, appBean.getDownloadURL());
                                }
                            }
                        })
                        .show();
            }
        });
    }

    private boolean requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                ToastUtils.showToast(this, R.string.update_permission_request);

                ActivityCompat.requestPermissions(this,
                        new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                        REQUEST_PERMISSION_CODE);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                        REQUEST_PERMISSION_CODE);
            }
        } else {
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_CODE
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            UpdateManagerListener.startDownloadTask(this, downloadUrl);
        }
    }

    @Override
    public HomeComponent getComponent() {
        if (mComponent == null) {
            mComponent = DaggerHomeComponent.builder().appComponent(getAppComponent()).build();
        }
        return mComponent;
    }

    @Override
    public Retrofit getRetrofit() {
        return mRetrofit;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        VistaShareUtil.handleQQResult(data);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        VistaShareUtil.handleWeiboResponse(this, intent);
    }

    class TabEntity implements CustomTabEntity {

        private String title;
        private int selected;
        private int unSelected;

        public TabEntity(String title, int selected, int unSelected) {
            this.title = title;
            this.selected = selected;
            this.unSelected = unSelected;
        }

        @Override
        public String getTabTitle() {
            return title;
        }

        @Override
        public int getTabSelectedIcon() {
            return selected;
        }

        @Override
        public int getTabUnselectedIcon() {
            return unSelected;
        }
    }
}
