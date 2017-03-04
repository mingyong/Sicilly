package xyz.shaohui.sicilly.views.setting;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import com.pgyersdk.views.PgyerDialog;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import me.shaohui.sicillylib.utils.ToastUtils;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.views.home.IndexActivity;

/**
 * Created by shaohui on 16/10/9.
 */

public class SettingFragment extends PreferenceFragment {

    private static final int REQUEST_PERMISSION_CODE = 0;

    private String mDownloadUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);

        findPreference(getString(R.string.setting_check_update_key))
                .setOnPreferenceClickListener(
                preference -> {
                    checkUpdate();
                    return true;
                });
    }

    private void checkUpdate() {
        PgyUpdateManager.register(getActivity(), new UpdateManagerListener() {
            @Override
            public void onNoUpdateAvailable() {
                ToastUtils.showToast(getActivity(), R.string.update_no_new);
            }

            @Override
            public void onUpdateAvailable(String s) {
                final AppBean appBean = getAppBeanFromString(s);
                mDownloadUrl = appBean.getDownloadURL();

                new MaterialDialog.Builder(getActivity())
                        .title(R.string.update_version)
                        .content(appBean.getReleaseNote())
                        .positiveText(R.string.update_confirm)
                        .negativeText(R.string.update_cancel)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog,
                                    @NonNull DialogAction which) {
                                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || (
                                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                                                && requestPermission())) {
                                    startDownloadTask(getActivity(), appBean.getDownloadURL());
                                }
                            }
                        })
                        .show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean requestPermission() {
        if (ContextCompat
                .checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ToastUtils.showToast(getActivity(), R.string.update_permission_request);

                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_PERMISSION_CODE);
            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
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
            UpdateManagerListener.startDownloadTask(getActivity(), mDownloadUrl);
        }
    }
}
