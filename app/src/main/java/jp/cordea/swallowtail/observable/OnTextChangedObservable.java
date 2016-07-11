package jp.cordea.swallowtail.observable;

import android.text.Editable;
import android.text.TextWatcher;

import com.google.android.agera.BaseObservable;

/**
 * Created by Yoshihiro Tanaka on 2016/07/10.
 */

public class OnTextChangedObservable extends BaseObservable implements TextWatcher {

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        dispatchUpdate();
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }

}
