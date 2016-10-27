package xyz.shaohui.sicilly.views.edit_info.di;

import dagger.Component;
import xyz.shaohui.sicilly.app.di.AppComponent;

/**
 * Created by shaohui on 2016/10/27.
 */

@Component(dependencies = AppComponent.class, modules = {EditInfoModule.class})
public interface EditInfoComponent {
}
