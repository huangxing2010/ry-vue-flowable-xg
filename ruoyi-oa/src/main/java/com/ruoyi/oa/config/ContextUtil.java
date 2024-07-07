package com.ruoyi.oa.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ContextUtil implements ApplicationContextAware {

    /**
     * 上下文
     */
    private static ApplicationContext applicationContext;

    /**
     * 设置上下文
     * @param applicationContext 上下文实例
     * @throws BeansException Bean异常
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ContextUtil.applicationContext = applicationContext;
    }



    /**
     * 获取上下文
     * @return
     */
    public static ApplicationContext getContext() {
        return ContextUtil.applicationContext;
    }

    /**
     * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
     */
    @SuppressWarnings("all")
    public static <T> T getBean(String name) {
        assertContextInjected();
        return (T) applicationContext.getBean(name);
    }

    /**
     * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
     */
    public static <T> T getBean(Class<T> requiredType) {
        assertContextInjected();
        return applicationContext.getBean(requiredType);
    }

    /**
     * 检查ApplicationContext不为空.
     */
    private static void assertContextInjected() {
        if (applicationContext == null) {
            throw new IllegalStateException("applicationContext属性未注入");
        }
    }
}
