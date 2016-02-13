package com.bustanil.myapp.infrastructure.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Helper class which is able to autowire a specified class. It holds a static reference to the {@link org
 * .springframework.context.ApplicationContext}.
 *
 * Taken from http://guylabs.ch/2014/02/22/autowiring-pring-beans-in-hibernate-jpa-entity-listeners/
 */
public final class AutowireHelper implements ApplicationContextAware {
 
    private static final AutowireHelper INSTANCE = new AutowireHelper();
    private static ApplicationContext applicationContext;
 
    private AutowireHelper() {
    }
 
    /**
     * Tries to autowire fields in the specified object
     *
     * @param target the instance of the class which holds @Autowire annotations
     * @param fieldsToAutowire the beans which have the @Autowire annotation in the specified {#target}
     */
    public static void autowire(Object target, Object... fieldsToAutowire) {
        for (Object bean : fieldsToAutowire) {
            if (bean == null) {
                applicationContext.getAutowireCapableBeanFactory().autowireBean(target);
                return;
            }
        }
    }
 
    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) {
        AutowireHelper.applicationContext = applicationContext;
    }
 
    /**
     * @return the singleton instance.
     */
    public static AutowireHelper getInstance() {
        return INSTANCE;
    }
 
}