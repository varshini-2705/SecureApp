package com.app.filter;

import com.app.model.User; 
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter("/sensitiveReports") // Map to the servlet URL
public class ABACFilter implements Filter {
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 1. Get the logged-in user from the session
        User loggedInUser = (User) httpRequest.getSession().getAttribute("user");
        
        if (loggedInUser != null) {
            String userRole = loggedInUser.getRole(); 
           String userDepartment = loggedInUser.getDepartment();
 
          // User must be an 'admin' AND their department must be either 'HR' OR 'Finance'.
boolean isAuthorizedDept = "HR".equalsIgnoreCase(userDepartment) || "Finance".equalsIgnoreCase(userDepartment);
boolean isAuthorizedAdmin = "admin".equalsIgnoreCase(userRole) && isAuthorizedDept;

if (isAuthorizedAdmin) {
    // Permission GRANTED: Continue to SensitiveReportsServlet
    chain.doFilter(request, response);
    return;
}
        }
        
        // 3. Permission DENIED: Send 403 Forbidden error
        httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: You are not authorized to view sensitive reports.");
    }
    
    
}
