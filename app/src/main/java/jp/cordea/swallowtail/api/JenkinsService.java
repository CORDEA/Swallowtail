package jp.cordea.swallowtail.api;

import com.google.android.agera.Result;
import com.google.android.agera.Supplier;

import jp.cordea.swallowtail.model.Feed;
import retrofit2.http.GET;

/**
 * Created by Yoshihiro Tanaka on 2016/07/08.
 */

public interface JenkinsService {

    @GET("/rssLatest")
    Supplier<Result<Feed>> getLatestBuild();

    @GET("/rssFailed")
    Supplier<Result<Feed>> getFailedBuild();

    @GET("/rssAll")
    Supplier<Result<Feed>> getAllBuild();

}
