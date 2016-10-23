package xyz.shaohui.sicilly.views.setting.di;

import dagger.Component;
import xyz.shaohui.sicilly.app.di.AppComponent;
import xyz.shaohui.sicilly.views.setting.SettingActivity;

/**
 * Created by shaohui on 2016/10/23.
 */

@Component(dependencies = AppComponent.class)
public interface SettingComponent {

    void inject(SettingActivity activity);

}
