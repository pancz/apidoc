package com.zhaoyun.docmanager.common.commons.spring.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class SpringPlaceHolderConfigurer extends PropertyPlaceholderConfigurer {
    @Override
    @SuppressWarnings("rawtypes")
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess,
                                     Properties props) throws BeansException {

        @SuppressWarnings("unchecked")
        Map<String, String> map = new HashMap<String, String>((Map) props);
        SpringConfig.set(map);
        super.processProperties(beanFactoryToProcess, props);
    }

}
