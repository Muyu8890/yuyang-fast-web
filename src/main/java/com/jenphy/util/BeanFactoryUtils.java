package com.jenphy.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Component;


@Component
public class BeanFactoryUtils implements BeanFactoryAware {
	public static Logger logger = LoggerFactory.getLogger(BeanFactoryUtils.class);

	private static BeanFactory beanFactory;

	@Override
	public void setBeanFactory(BeanFactory beanFactory) {
		setBeanFactoryStatic(beanFactory);
	}

	private static void setBeanFactoryStatic(BeanFactory beanFactory) {
		BeanFactoryUtils.beanFactory = beanFactory;
	}

	public static <T> T getBean(Class<T> clazz) {
		if (beanFactory == null) {
			return null;
		}
		return beanFactory.getBean(clazz);
	}

	public static Object getBean(String beanName) {
		if (beanFactory == null) {
			return null;
		}
		return beanFactory.getBean(beanName);
	}

	public static <T> T getBean(String beanName, Class<T> clazz) {
		if (beanFactory == null) {
			return null;
		}
		return beanFactory.getBean(beanName, clazz);
	}

}
