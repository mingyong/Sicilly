package xyz.shaohui.sicilly.provider;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by shaohui on 16/9/10.
 */

final public class BusProvider {
    public static EventBus getBus() {
        return BusProviderHolder.INSTANCE;
    }

    private static class BusProviderHolder {
        private static final EventBus INSTANCE = EventBus.builder()
                .logNoSubscriberMessages(false)
                .sendNoSubscriberEvent(false)
                .eventInheritance(true)
                .throwSubscriberException(true)
                .build();
    }

    private BusProvider() {
        // No instances.
    }
}
