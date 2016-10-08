package xyz.shaohui.sicilly.views.home;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
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
import com.flyco.tablayout.widget.MsgView;
import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import java.util.ArrayList;
import javax.inject.Inject;
import me.shaohui.sicillylib.utils.ToastUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.SicillyApplication;
import xyz.shaohui.sicilly.base.BaseActivity;
import xyz.shaohui.sicilly.base.HasComponent;
import xyz.shaohui.sicilly.event.FeedbackEvent;
import xyz.shaohui.sicilly.event.FriendRequestEvent;
import xyz.shaohui.sicilly.event.HomeMessageEvent;
import xyz.shaohui.sicilly.event.MentionEvent;
import xyz.shaohui.sicilly.event.MessageEvent;
import xyz.shaohui.sicilly.event.MessageSumEvent;
import xyz.shaohui.sicilly.leanCloud.service.ActiveUserService;
import xyz.shaohui.sicilly.service.SicillyService;
import xyz.shaohui.sicilly.service.aidl.IEventListener;
import xyz.shaohui.sicilly.service.aidl.ISicillyService;
import xyz.shaohui.sicilly.views.home.chat.MessageFragment;
import xyz.shaohui.sicilly.views.home.di.DaggerHomeComponent;
import xyz.shaohui.sicilly.views.home.di.HomeComponent;
import xyz.shaohui.sicilly.views.home.profile.ProfileFragment;
import xyz.shaohui.sicilly.views.home.timeline.HomeTimelineFragment;
import xyz.shaohui.sicilly.views.home.timeline.HomeTimelineFragmentBuilder;

public class IndexActivity extends BaseActivity implements HasComponent<HomeComponent> {

    public static final int ACTION_CHAT = 1;
    public static final int ACTION_MENTION = 2;
    public static final int ACTION_USER = 3;
    public static final int ACTION_NONE = 0;

    @BindView(R.id.bottom_tab)
    CommonTabLayout bottomTab;

    @Inject
    EventBus mBus;

    private Fragment indexFragment =
            HomeTimelineFragmentBuilder.newHomeTimelineFragment(HomeTimelineFragment.TYPE_HOME);
    private Fragment messageFragment = new MessageFragment();
    private Fragment userFragment = new ProfileFragment();

    private HomeComponent mComponent;

    private static final int REQUEST_PERMISSION_CODE = 0;

    private String downloadUrl;

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
        ActiveUserService.activeUser(SicillyApplication.currentUId(),
                SicillyApplication.currentAppUser().name());

        // 启动Service 监听
        startService(new Intent(this, SicillyService.class));
        bindService();
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
                switch (type) {
                    case SicillyService.EVENT_TYPE_HOME:
                        mBus.post(new HomeMessageEvent(count));
                        break;
                    case SicillyService.EVENT_TYPE_MENTION:
                        mBus.post(new MentionEvent(count));
                        break;
                    case SicillyService.EVENT_TYPE_MESSAGE:
                        mBus.post(new MessageEvent(count));
                        break;
                    case SicillyService.EVENT_TYPE_REQUEST:
                        mBus.post(new FriendRequestEvent(count));
                        break;
                    case SicillyService.EVENT_TYPE_SUM_MESSAGE:
                        mBus.post(new MessageSumEvent(count));
                        break;
                }
            }
        };

        ServiceConnection serviceConnection = new ServiceConnection() {
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

        bindService(new Intent(this, SicillyService.class), serviceConnection, BIND_AUTO_CREATE);
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
        tabData.add(new TabEntity(getString(R.string.bottom_tab_message), R.drawable.ic_message_selected,
                R.drawable.ic_message));
        tabData.add(new TabEntity(getString(R.string.bottom_tab_user), R.drawable.ic_user_selected,
                R.drawable.ic_user));

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(indexFragment);
        fragments.add(messageFragment);
        fragments.add(userFragment);

        bottomTab.setTabData(tabData, this, R.id.main_frame, fragments);
    }

    /**
     * 监听更新BottomTab
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void subscirbeMessaegTab(HomeMessageEvent event) {
        if (event.count > 0) {
            bottomTab.showDot(1);
            MsgView msgView = bottomTab.getMsgView(1);
            msgView.setBackgroundColor(getResources().getColor(R.color.red));
        } else {
            bottomTab.hideMsg(1);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void subscribeUserTab(FeedbackEvent event) {
        if (event.count > 0) {
            bottomTab.showDot(2);
            MsgView msgView = bottomTab.getMsgView(2);
            msgView.setBackgroundColor(getResources().getColor(R.color.red));
        } else {
            bottomTab.hideMsg(2);
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
        return mComponent;
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
