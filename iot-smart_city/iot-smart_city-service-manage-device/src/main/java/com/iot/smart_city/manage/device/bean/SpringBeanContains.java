package com.iot.smart_city.manage.device.bean;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
/**
 * 从spring容器中获取bean
 * @author chrilwe
 *
 */
@Component
public class SpringBeanContains implements ApplicationContextAware {
	
	ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;

	}
	
	public <T> T getBean(Class<T> classType) {
		return applicationContext.getBean(classType);
	}

}
