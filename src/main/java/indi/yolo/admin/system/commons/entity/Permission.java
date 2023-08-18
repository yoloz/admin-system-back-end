package indi.yolo.admin.system.commons.entity;

import java.lang.annotation.*;

/**
 * @author yoloz
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Permission {
    String[] value() default {};
}
