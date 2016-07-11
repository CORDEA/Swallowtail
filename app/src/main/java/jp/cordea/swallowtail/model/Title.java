package jp.cordea.swallowtail.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * Created by Yoshihiro Tanaka on 2016/07/08.
 */
@Getter
public class Title {

    public Title(String title) {
        this.title = title;
        parse();
    }

    @Getter(AccessLevel.NONE)
    private String title;

    private int buildNumber = 0;

    private String buildTitle;

    private String buildStatus;

    private void parse() {
        Pattern pattern = Pattern.compile("([^\\s]+) #(\\d+) \\((.+)\\)");
        Matcher matcher = pattern.matcher(title);
        if (matcher.matches()) {
            buildTitle = matcher.group(1);
            buildNumber = Integer.parseInt(matcher.group(2));
            buildStatus = matcher.group(3);
        }
    }
}
