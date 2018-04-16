package com.jenphy;

import com.jenphy.config.GlobalExceptionConfig;
import com.jenphy.util.BeanFactoryUtils;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({GlobalExceptionConfig.class, BeanFactoryUtils.class})
public @interface ImportYuYangFastWebBean {
}
