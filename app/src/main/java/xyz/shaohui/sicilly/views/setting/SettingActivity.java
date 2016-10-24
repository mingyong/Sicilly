package xyz.shaohui.sicilly.views.setting;

import android.content.Intent;
import android.os.Bundle;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.afollestad.materialdialogs.MaterialDialog;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.SicillyApplication;
import xyz.shaohui.sicilly.base.BaseActivity;
import xyz.shaohui.sicilly.data.database.AppUserDbAccessor;
import xyz.shaohui.sicilly.service.SicillyService;
import xyz.shaohui.sicilly.views.login.LoginActivity;
import xyz.shaohui.sicilly.views.setting.di.DaggerSettingComponent;
import xyz.shaohui.sicilly.views.setting.di.SettingComponent;

public class SettingActivity extends BaseActivity {

    @Inject
    EventBus mBus;

    @Inject
    AppUserDbAccessor mAppUserDbAccessor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        getFragmentManager().beginTransaction()
                .replace(R.id.setting_frame, new SettingFragment())
                .commit();

        findViewById(R.id.btn_log_out).setOnClickListener(
                v -> new MaterialDialog.Builder(SettingActivity.this).content(
                        R.string.log_out_message)
                        .negativeText(R.string.no)
                        .positiveText(R.string.yes)
                        .onPositive((dialog, which) -> logOut())
                        .show());
    }

    private void logOut() {
        // 1. 删除数据库
        // 2. 停止Service
        // 3. 打开LoginActivity
        mAppUserDbAccessor.deleteUser(SicillyApplication.currentUId());             // 1
        stopService(new Intent(this, SicillyService.class));        // 2
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);           // 3
        startActivity(intent);
        finish();
    }

    @Override
    public void initializeInjector() {
        SettingComponent component =
                DaggerSettingComponent.builder().appComponent(getAppComponent()).build();
        component.inject(this);
    }

    @OnClick(R.id.btn_back)
    void btnBack() {
        finish();
    }

    @Override
    public EventBus getBus() {
        return mBus;
    }
}
