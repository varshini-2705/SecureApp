package com.app.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // Clear session
        }

        // Clear "Remember Me" persistent cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("p_auth")) {
                    cookie.setValue(""); // Set empty
                    cookie.setMaxAge(0); // Immediately expire
                    cookie.setPath("/");
                    response.addCookie(cookie);
                    // NOTE: In a real app, you should also clear the token in the DB here
                    break;
                }
            }
        }

        response.sendRedirect("login.jsp?logout=true");
    }
}
