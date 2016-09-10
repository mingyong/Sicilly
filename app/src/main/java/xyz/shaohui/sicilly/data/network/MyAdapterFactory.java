package xyz.shaohui.sicilly.data.network;

import com.google.gson.TypeAdapterFactory;
import com.ryanharter.auto.value.gson.GsonTypeAdapterFactory;

/**
 * Created by shaohui on 16/9/10.
 */

@GsonTypeAdapterFactory
abstract class MyAdapterFactory implements TypeAdapterFactory {

    static TypeAdapterFactory create() {
        return new AutoValueGson_MyAdapterFactory();
    }
}
