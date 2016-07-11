package jp.cordea.swallowtail.model;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import jp.cordea.swallowtail.api.JenkinsRssClient;
import lombok.Getter;

/**
 * Created by Yoshihiro Tanaka on 2016/07/08.
 */
@Getter
@Root
public class Entry {

    @Element
    private String title;

    @Element
    private String id;

    @Element
    private String published;

    @Element
    private String updated;

    @Element
    private Link link;

    @Element(required = false)
    private String content;

    public DateTime getUpdated() {
        return JenkinsRssClient.JENKINS_DATE_FORMATTER.parseDateTime(updated);
    }

    public Duration getUpdatedDuration() {
        DateTime dateTime = JenkinsRssClient.JENKINS_DATE_FORMATTER.parseDateTime(updated);
        return new Duration(dateTime, new DateTime());
    }

    public DateTime getPublished() {
        return JenkinsRssClient.JENKINS_DATE_FORMATTER.parseDateTime(updated);
    }

    public Duration getPublishedDuration() {
        DateTime dateTime = JenkinsRssClient.JENKINS_DATE_FORMATTER.parseDateTime(updated);
        return new Duration(dateTime, new DateTime());
    }

}
