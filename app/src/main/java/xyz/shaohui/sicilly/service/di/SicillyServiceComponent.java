package xyz.shaohui.sicilly.service.di;

import dagger.Component;
import xyz.shaohui.sicilly.app.di.AppComponent;
import xyz.shaohui.sicilly.data.network.di.AccountModule;
import xyz.shaohui.sicilly.service.SicillyService;

/**
 * Created by shaohui on 16/9/27.
 */

@Component( dependencies = AppComponent.class,
modules = { AccountModule.class})
public interface SicillyServiceComponent {

    void inject(SicillyService service);

}
