package com.wooplr.base.controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface TestParameter {

    /**
    * The XML parameter name of the testNG.xml file or system property
    */
    String name();

}
