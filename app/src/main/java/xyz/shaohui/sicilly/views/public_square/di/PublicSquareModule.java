package xyz.shaohui.sicilly.views.public_square.di;

import dagger.Module;
import dagger.Provides;
import xyz.shaohui.sicilly.views.public_square.PublicSquarePresenterImpl;
import xyz.shaohui.sicilly.views.public_square.mvp.PublicSquareMVP;

/**
 * Created by shaohui on 2016/10/27.
 */

@Module
public class PublicSquareModule {

    @Provides
    PublicSquareMVP.Presenter providePresenter(PublicSquarePresenterImpl presenter) {
        return presenter;
    }

}
