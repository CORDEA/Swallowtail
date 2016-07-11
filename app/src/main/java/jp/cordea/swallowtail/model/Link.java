package jp.cordea.swallowtail.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import lombok.Getter;

/**
 * Created by Yoshihiro Tanaka on 2016/07/08.
 */
@Getter
@Root(strict = false)
public class Link {

    @Attribute
    private String href;

}
