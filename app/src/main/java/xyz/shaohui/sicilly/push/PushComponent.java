package xyz.shaohui.sicilly.push;

import dagger.Component;
import xyz.shaohui.sicilly.app.di.AppComponent;

/**
 * Created by shaohui on 16/9/25.
 */

@Component(dependencies = AppComponent.class)
public interface PushComponent {

    void inject(MiBroadcastReceiver receiver);

}
