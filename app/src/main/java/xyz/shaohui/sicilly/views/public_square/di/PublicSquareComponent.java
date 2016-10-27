package xyz.shaohui.sicilly.views.public_square.di;

import dagger.Component;
import xyz.shaohui.sicilly.app.di.AppComponent;
import xyz.shaohui.sicilly.data.network.di.FavoriteModule;
import xyz.shaohui.sicilly.data.network.di.StatusModule;
import xyz.shaohui.sicilly.views.public_square.PublicSquareActivity;
import xyz.shaohui.sicilly.views.public_square.PublicSquareFragment;
import xyz.shaohui.sicilly.views.public_square.mvp.PublicSquareMVP;

/**
 * Created by shaohui on 2016/10/27.
 */

@Component(dependencies = AppComponent.class, modules = {
        PublicSquareModule.class, StatusModule.class, FavoriteModule.class
})
public interface PublicSquareComponent {

    void inject(PublicSquareActivity activity);

    void inject(PublicSquareFragment fragment);

    PublicSquareMVP.Presenter presenter();
}
