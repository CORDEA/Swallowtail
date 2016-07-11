package jp.cordea.swallowtail.observable;

import android.support.v4.widget.SwipeRefreshLayout;

import com.google.android.agera.BaseObservable;

/**
 * Created by Yoshihiro Tanaka on 2016/07/08.
 */

public class OnRefreshObservable extends BaseObservable implements SwipeRefreshLayout.OnRefreshListener {

    @Override
    public void onRefresh() {
        dispatchUpdate();
    }

}
