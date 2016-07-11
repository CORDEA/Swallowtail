package jp.cordea.swallowtail.model;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

import jp.cordea.swallowtail.api.JenkinsRssClient;
import lombok.Getter;

/**
 * Created by Yoshihiro Tanaka on 2016/07/08.
 */
@Getter
@Root
public class Feed {

    @Element
    private String title;

    @Element
    private Link link;

    @Element
    private String updated;

    @Element
    private Author author;

    @Element
    private String id;

    @ElementList(inline = true)
    private List<Entry> entry;

    public DateTime getUpdated() {
        return JenkinsRssClient.JENKINS_DATE_FORMATTER.parseDateTime(updated);
    }

    public Duration getUpdatedDuration() {
        DateTime dateTime = JenkinsRssClient.JENKINS_DATE_FORMATTER.parseDateTime(updated);
        return new Duration(dateTime, new DateTime());
    }

}
