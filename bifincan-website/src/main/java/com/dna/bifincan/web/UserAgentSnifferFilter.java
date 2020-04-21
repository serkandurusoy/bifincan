package com.dna.bifincan.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.uadetector.*;

import net.sf.uadetector.UADetectorServiceFactory;
import net.sf.uadetector.UserAgent;
import net.sf.uadetector.UserAgentStringParser;
import net.sf.uadetector.UserAgentType;
import org.springframework.security.core.context.SecurityContextHolder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dna.bifincan.util.WebConstants;
import com.dna.bifincan.web.user.CurrentUserHolder;

public class UserAgentSnifferFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(UserAgentSnifferFilter.class);
    private final String USER_AGENT_KEY = "User-Agent";
    // lookup tables for supported browsers and their minimum acceptable major version numbers
    private final String[] browser_names = new String[]{"IE", "Firefox", "Chrome", "Safari", "Opera"};
    private final int[] minimum_versions = new int[]{9, 15, 21, 5, 12};
    // lookup tables for supported mobile browsers and their minimum acceptable major version numbers
    private final String[] mobile_browser_names = new String[]{"Chrome Mobile", "Mobile Safari", "Mobile Firefox", "Opera Mobile"};
    private final int[] mobile_minimum_versions = new int[]{21, 5, 15, 12};
    private final String BROWSER_WARN_URI = "/bu-tarayici-biraz-eski-sanki";
    private final String BROWSER_WARN_URI_MOBILE = "/bu-mobil-tarayicida-maalesef-calisamiyorum";
    private final String[] acceptable_ends = new String[]{BROWSER_WARN_URI, "20120813", ".css", ".js", ".png", ".gif", ".swf",
        ".jpg", ".htc", ".vtt", ".mp4", ".ogv", ".webm", ".jpeg", ".flv", ".zip", ".rar", ".txt", ".xml", "rss-feed"};

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;

        //log.debug(">> url : " + req.getRequestURL() + ", uri : " + req.getRequestURI());

        String uri = req.getRequestURI();

        HttpSession session = req.getSession(false);

        if (session != null && session.getAttribute(WebConstants.CURRENT_USER_SESSION_KEY) != null) {
            for (int i = 0; i < acceptable_ends.length; i++) {
                if (uri.endsWith(acceptable_ends[i])) {
                    filterChain.doFilter(request, response);
                    return;
                }
            }

            String userAgent = req.getHeader(USER_AGENT_KEY);

            try {
                UserAgentStringParser parser = UADetectorServiceFactory.getResourceModuleParser();
                UserAgent agent = parser.parse(userAgent);

                boolean isUAValid = false;
                boolean isMobile = false;

                if (agent.getType() == UserAgentType.BROWSER) {
                    for (int i = 0; i < browser_names.length; i++) {
                        if (browser_names[i].equalsIgnoreCase(agent.getName())) {
                            if (minimum_versions[i] <= Integer.parseInt(agent.getVersionNumber().getMajor())) {
                                isUAValid = true;
                                break;
                            }
                        }
                    }
                }

                if (agent.getType() == UserAgentType.MOBILE_BROWSER) {
                    isMobile = true;
                    for (int i = 0; i < mobile_browser_names.length; i++) {
                        if (mobile_browser_names[i].equalsIgnoreCase(agent.getName())) {
                            if (mobile_minimum_versions[i] <= Integer.parseInt(agent.getVersionNumber().getMajor())) {
                                isUAValid = true;
                                break;
                            }
                        }
                    }
                }

                if (!isUAValid) {
                    /*
                     * log.info("--redirect begin ----------------");
                     * log.info("redirected ip: " + req.getRemoteAddr());
                     * log.info("redirected user: " + session.getAttribute(WebConstants.CURRENT_USER_SESSION_KEY));
                     * log.info("redirected os family: " + agent.getOperatingSystem().getFamilyName());
                     * log.info("redirected os version: " + agent.getOperatingSystem().getVersionNumber().toVersionString());
                     * log.info("redirected browser type: " + agent.getType().getName());
                     * log.info("redirected browser family: " + agent.getFamily());
                     * log.info("redirected browser name: " + agent.getName());
                     * log.info("redirected browser version: " + agent.getVersionNumber().toVersionString());
                     * log.info("--redirect end ----------------");
                     */

                    session.removeAttribute(WebConstants.CURRENT_USER_SESSION_KEY);
                    session.invalidate();  // invalidate session
                    session = null;

                    if (!isMobile) {
                        HttpServletResponse httpResponse = (HttpServletResponse) response;
                        httpResponse.sendRedirect(req.getContextPath() + BROWSER_WARN_URI);
                    } else {
                        HttpServletResponse httpResponse = (HttpServletResponse) response;
                        httpResponse.sendRedirect(req.getContextPath() + BROWSER_WARN_URI_MOBILE);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        filterChain.doFilter(request, response);
    }

    public void destroy() {
    }
}
