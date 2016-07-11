package jp.cordea.swallowtail.api;

import android.support.annotation.NonNull;

import com.google.android.agera.BaseObservable;
import com.google.android.agera.Observable;
import com.google.android.agera.Repositories;
import com.google.android.agera.Repository;
import com.google.android.agera.Result;
import com.google.android.agera.Supplier;
import com.google.android.agera.Updatable;

import java.util.Collections;
import java.util.List;

import jp.cordea.swallowtail.model.Entry;
import jp.cordea.swallowtail.model.Feed;

/**
 * Created by Yoshihiro Tanaka on 2016/07/10.
 */

public class JenkinsRepository extends BaseObservable implements Updatable, Supplier<Result<List<Entry>>>{

    private Repository<Result<List<Entry>>> repository;

    public JenkinsRepository(Observable observable,
                             Supplier<Result<Feed>> supplier) {
        repository =
                Repositories
                        .repositoryWithInitialValue(Result.<List<Entry>>absent())
                        .observe(observable)
                        .onUpdatesPerLoop()
                        .goTo(JenkinsRssClient.NETWORK_EXECUTOR)
                        .attemptGetFrom(supplier)
                        .orSkip()
                        .transform(Feed::getEntry)
                        .thenTransform(input -> {
                            Collections.sort(input, (t0, t1) ->
                                    (int) (t0.getUpdatedDuration().getStandardSeconds() - t1.getUpdatedDuration().getStandardSeconds()));
                            return Result.success(input);
                        })
                        .compile();
    }

    @NonNull
    @Override
    public Result<List<Entry>> get() {
        return repository.get();
    }

    @Override
    public void update() {
        dispatchUpdate();
    }

    public void addUpdatable() {
        repository.addUpdatable(this);
    }

    public void removeUpdatable() {
        repository.removeUpdatable(this);
    }

}
