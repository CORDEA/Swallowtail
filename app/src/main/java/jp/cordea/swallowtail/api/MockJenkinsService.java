package jp.cordea.swallowtail.api;

import com.google.android.agera.Result;
import com.google.android.agera.Supplier;

import jp.cordea.swallowtail.model.Feed;
import retrofit2.mock.BehaviorDelegate;

/**
 * Created by Yoshihiro Tanaka on 2016/07/08.
 */
@Deprecated
public class MockJenkinsService implements JenkinsService {

    private BehaviorDelegate<JenkinsService> delegate;

    public MockJenkinsService(BehaviorDelegate<JenkinsService> delegate) {
        this.delegate = delegate;
    }

    @Override
    public Supplier<Result<Feed>> getLatestBuild() {
        return null;
    }

    @Override
    public Supplier<Result<Feed>> getFailedBuild() {
        return null;
    }

    @Override
    public Supplier<Result<Feed>> getAllBuild() {
        return null;
    }

}
