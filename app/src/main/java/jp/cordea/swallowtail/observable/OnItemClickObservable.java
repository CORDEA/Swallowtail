package jp.cordea.swallowtail.observable;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;

import com.google.android.agera.BaseObservable;
import com.google.android.agera.Result;
import com.google.android.agera.Supplier;

/**
 * Created by Yoshihiro Tanaka on 2016/07/11.
 */

public class OnItemClickObservable extends BaseObservable implements Supplier<Result<Integer>>, AdapterView.OnItemClickListener {

    private Result<Integer> position = Result.absent();

    public void reset() {
        position = Result.absent();
    }

    @NonNull
    @Override
    public Result<Integer> get() {
        return position;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        position = Result.success(i);
        dispatchUpdate();
    }

}
