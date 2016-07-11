package jp.cordea.swallowtail.viewmodel;

import android.content.Context;
import android.net.Uri;

import jp.cordea.swallowtail.R;
import jp.cordea.swallowtail.model.Entry;
import jp.cordea.swallowtail.model.Title;
import lombok.Getter;

/**
 * Created by Yoshihiro Tanaka on 2016/07/11.
 */
public class AllListItemViewModel {

    @Getter
    private String title;

    @Getter
    private String description;

    @Getter
    private Uri uri;

    public AllListItemViewModel(Context context, Entry entry) {
        Title title = new Title(entry.getTitle());
        this.title = title.getBuildTitle();
        this.description = getFormattedDescText(context, title);
        this.uri = Uri.parse(entry.getLink().getHref());
    }

    private String getFormattedDescText(Context context, Title title) {
        return String.format(context.getResources().getString(R.string.all_list_description_format),
                title.getBuildNumber(),
                title.getBuildStatus()
        );
    }

}
