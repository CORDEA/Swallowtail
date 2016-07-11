package jp.cordea.swallowtail.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.agera.MutableRepository;
import com.google.android.agera.Repositories;
import com.google.android.agera.Repository;
import com.google.android.agera.Result;
import com.google.android.agera.Supplier;
import com.google.android.agera.Updatable;

import org.joda.time.Duration;

import jp.cordea.swallowtail.R;
import jp.cordea.swallowtail.model.Entry;
import jp.cordea.swallowtail.model.Title;
import lombok.Getter;

/**
 * Created by Yoshihiro Tanaka on 2016/07/11.
 */
public class MainPageListItemViewModel implements Updatable, Supplier<Result<MainPageListItemViewModel>> {

    private static final int ITEM_CLICK_WAIT_MILLIS = 1000;

    @Getter
    private String subTitle;

    @Getter
    private String title;

    @Getter
    private String duration;

    @Getter
    private String updatedDate;

    @Getter
    private String publishedDate;

    @Getter
    private Uri uri;

    public void onClick() {
        mutableRepository.accept(Result.success(this));
    }

    private MutableRepository<Result<MainPageListItemViewModel>> mutableRepository;

    private Repository<Result<Intent>> repository;

    private Context context;

    public MainPageListItemViewModel(Context context, Entry entry) {
        this.context = context;

        Title title = new Title(entry.getTitle());
        this.subTitle = title.getBuildStatus();
        this.title = title.getBuildTitle();
        this.duration = getFormattedDurationText(context, entry.getUpdatedDuration());

        String dateFormat = context.getResources().getString(R.string.latest_list_date_format);
        this.updatedDate = entry.getUpdated().toString(dateFormat);
        this.publishedDate = entry.getPublished().toString(dateFormat);

        this.uri = Uri.parse(entry.getLink().getHref());

        mutableRepository = Repositories.mutableRepository(Result.absent());

        repository =
                Repositories
                        .repositoryWithInitialValue(Result.<Intent>absent())
                        .observe(mutableRepository)
                        .onUpdatesPer(ITEM_CLICK_WAIT_MILLIS)
                        .attemptGetFrom(mutableRepository)
                        .orSkip()
                        .thenTransform(input -> Result.success(new Intent(Intent.ACTION_VIEW, input.getUri())))
                        .compile();

        repository.addUpdatable(this);
    }

    private String getFormattedDurationText(Context context, Duration duration) {
        int formatTextResId = R.string.latest_list_duration_format_text_s;
        long d = duration.getStandardSeconds();
        if (d > 60) {
            d = duration.getStandardMinutes();
            if (d > 60) {
                d = duration.getStandardHours();
                if (d > 24) {
                    formatTextResId = R.string.latest_list_duration_format_text_d;
                } else {
                    formatTextResId = R.string.latest_list_duration_format_text_h;
                }
            } else {
                formatTextResId = R.string.latest_list_duration_format_text_m;
            }
        }

        return String.format(context.getResources().getString(formatTextResId), d);
    }

    @NonNull
    @Override
    public Result<MainPageListItemViewModel> get() {
        return Result.present(this);
    }

    @Override
    public void update() {
        repository.get()
                .ifSucceededSendTo(value -> context.startActivity(value));
    }
}
