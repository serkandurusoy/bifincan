/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dna.bifincan.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Web application lifecycle listener.
 *
 * @author hantsy
 */
@WebListener()
public class ServletContextInfoListener implements ServletContextListener {

    private static final Logger log = LoggerFactory.getLogger(ServletContextInfoListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        if (log.isDebugEnabled()) {
            log.debug("call contextInitialized...@");
        }
        ServletContextInfo.setContextPath(sce.getServletContext().getContextPath());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (log.isDebugEnabled()) {
            log.debug("call contextDestroyed...@");
        }
    }
}
