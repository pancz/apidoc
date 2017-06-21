package com.zhaoyun.docmanager.common.ext;

import com.zhaoyun.docmanager.common.core.Config;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.Map;
import java.util.Properties;

public class ContextPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected void processProperties(
            ConfigurableListableBeanFactory beanFactoryToProcess,
            Properties props) throws BeansException {
        if (props.isEmpty()) {
            return;
        }
        Config.initialize((Map) props);
        super.processProperties(beanFactoryToProcess, props);
    }

}
