package com.app.filter;

import com.app.model.User;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalTime;


@WebFilter(urlPatterns = {"/admin", "/reports", "/admin-file-manager", "/admin/upload", "/admin/details", "/admin/pii-details", "/admin/sonarqube-analysis-page"})
public class SecurityFilter implements Filter {
    
    // Inactivity timeout
    private static final long INACTIVITY_TIMEOUT_MS = 150000; 

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
        throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);
        String path = req.getRequestURI().substring(req.getContextPath().length());

        // 1. Authentication Check
        if (session == null || session.getAttribute("loggedInUser") == null) {
            
            res.sendRedirect(req.getContextPath() + "/login.jsp?error=not_logged_in");
            return;
        }

        // Get user attributes from session
        User user = (User) session.getAttribute("loggedInUser");
        
        // 2. Auto-Logout (Inactivity Check)
    
        if (session.getAttribute("lastActivityTime") == null) {
            session.setAttribute("lastActivityTime", System.currentTimeMillis());
        }
        
        long lastAccess = (long) session.getAttribute("lastActivityTime");
        if (System.currentTimeMillis() - lastAccess > INACTIVITY_TIMEOUT_MS) {
            session.invalidate();
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "403 Forbidden: Session expired due to inactivity.");
            return;
        }
        
        // Update activity time
        session.setAttribute("lastActivityTime", System.currentTimeMillis());

        // 3. Authorization (RBAC for Admin paths)
        
        
        boolean isGeneralAdminPath = path.startsWith("/admin") || path.equals("/admin-file-manager");

        if (isGeneralAdminPath) {
            String method = req.getMethod();

            
            if (!"GET".equalsIgnoreCase(method)) {
                
            
                String userRole = user.getRole();
                String normalizedRole = (userRole != null) ? userRole.toLowerCase() : "";

              
                if (!"admin".equals(normalizedRole) && !"developer".equals(normalizedRole)) {
                    res.sendError(HttpServletResponse.SC_FORBIDDEN, "403 Forbidden: Only users with the 'admin' or 'developer' role can perform administrative actions or modifications.");
                    return;
                }
            }
          
        }


        // 4. Authorization (ABAC for /reports) - Keeping original policy
        if (path.equals("/reports")) {
            if (!checkReportsAccessPolicy(user)) { 
                res.sendError(HttpServletResponse.SC_FORBIDDEN, "403 Forbidden: Access denied by ABAC policy.");
                return;
            }
        }
        
        // Proceed to the servlet/JSP if all checks pass
        chain.doFilter(request, response);
    }
    
    /**
     * ABAC Policy: role=admin AND department=Finance AND clearance_level >= 3 AND log in between 9 AM and 5 PM.
     */
    private boolean checkReportsAccessPolicy(User user) {
        
        boolean roleCheck = "admin".equalsIgnoreCase(user.getRole());
        
        // Department Check
        boolean deptCheck = "Finance".equalsIgnoreCase(user.getDepartment());
        
        // Clearance Level Check
        boolean clearanceCheck = user.getClearanceLevel() >= 3;
        
        // Time Check
        LocalTime now = LocalTime.now();
        LocalTime nineAM = LocalTime.of(9, 0);
        LocalTime fivePM = LocalTime.of(17, 0);
        boolean timeCheck = now.isAfter(nineAM) && now.isBefore(fivePM); 

        return roleCheck && deptCheck && clearanceCheck && timeCheck;
    }
    
    // Filter implementation methods (init and destroy) are optional and omitted for brevity
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}
    
    @Override
    public void destroy() {}
}
