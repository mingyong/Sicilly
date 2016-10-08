// ISicillyService.aidl
package xyz.shaohui.sicilly.service.aidl;

// Declare any non-default types here with import statements
import xyz.shaohui.sicilly.service.aidl.IEventListener;

interface ISicillyService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

    void registerListener(IEventListener listener);
}
