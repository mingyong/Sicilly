package xyz.shaohui.sicilly.share;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import java.util.ArrayList;
import java.util.List;
import me.shaohui.bottomdialog.BaseBottomDialog;
import xyz.shaohui.sicilly.R;

/**
 * Created by shaohui on 2016/11/1.
 */

public class ShareDialog extends BaseBottomDialog {

    private String mText;

    private Uri mUri;

    @Override
    public int getLayoutRes() {
        return R.layout.dialog_share;
    }

    @Override
    public void bindView(View v) {

    }

    private void getShareTargets(Intent intent) {
        PackageManager pm = getContext().getPackageManager();
        List<ShareIconItem> result = new ArrayList<>();
        List<ResolveInfo> apps =
                pm.queryIntentActivities(intent, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
        for (ResolveInfo info : apps) {
            if (constain(info.activityInfo.toString())) {
                //result.add(new ShareIconItem(info.loadIcon(pm), ))
            }
        }
    }

    private boolean constain(String info) {
        return originList().contains(info);
    }

    private List<String> originList() {
        List<String> list = new ArrayList<>();
        return list;
    }
}
