package jp.cordea.swallowtail.observable;

import android.view.View;

import com.google.android.agera.BaseObservable;

/**
 * Created by Yoshihiro Tanaka on 2016/07/10.
 */

public class OnClickObservable extends BaseObservable implements View.OnClickListener {
    @Override
    public void onClick(View view) {
        dispatchUpdate();
    }
}
