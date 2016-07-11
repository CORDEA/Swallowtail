package jp.cordea.swallowtail.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import lombok.Getter;

/**
 * Created by Yoshihiro Tanaka on 2016/07/08.
 */
@Getter
@Root
public class Author {

    @Element
    private String name;

}
