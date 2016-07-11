package jp.cordea.swallowtail.api;

import android.content.Context;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jp.cordea.swallowtail.model.Url;
import me.drakeet.retrofit2.adapter.agera.AgeraCallAdapterFactory;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import retrofit2.mock.MockRetrofit;
import retrofit2.mock.NetworkBehavior;

/**
 * Created by Yoshihiro Tanaka on 2016/07/08.
 */

public class JenkinsRssClient {

    public static final DateTimeFormatter JENKINS_DATE_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    static final ExecutorService NETWORK_EXECUTOR = Executors.newSingleThreadExecutor();

    private static Retrofit getRetrofit(Context context) {
        String url = Url.getUrl(context);
        if (!url.endsWith("/")) {
            url += "/";
        }
        if (!url.endsWith("view/All/")) {
            url += "view/All/";
        }
        return new Retrofit.Builder()
                .baseUrl(url)
                .client(new OkHttpClient())
                .addCallAdapterFactory(AgeraCallAdapterFactory.create())
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();
    }

    private static MockRetrofit getMockRetrofit(Context context) {
        return new MockRetrofit.Builder(getRetrofit(context))
                    .networkBehavior(NetworkBehavior.create())
                    .build();
    }

    public static JenkinsService getJenkinsService(Context context) {
        return getRetrofit(context)
                .create(JenkinsService.class);
    }

    @Deprecated
    public static MockJenkinsService getMockJenkinsService(Context context) {
        return new MockJenkinsService(getMockRetrofit(context).create(JenkinsService.class));
    }
}
