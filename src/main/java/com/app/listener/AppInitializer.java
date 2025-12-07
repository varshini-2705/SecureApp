package com.app.listener;

import com.app.util.DBUtil;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AppInitializer implements ServletContextListener {

    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Application starting up. Initializing database structure...");
        
     
        DBUtil.initializeDatabase(sce.getServletContext());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Application shutting down.");
    }
}
