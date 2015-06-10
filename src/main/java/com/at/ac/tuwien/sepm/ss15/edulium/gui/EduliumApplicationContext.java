package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class EduliumApplicationContext {
    private static ApplicationContext context = null;

    public static synchronized ApplicationContext getContext() {
        if (context == null) {
            context = new ClassPathXmlApplicationContext("spring/Spring-Edulium.xml");
        }

        return context;
    }
}
