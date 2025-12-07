package com.app.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/reports")
public class ReportsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // If the SecurityFilter allowed access, display the reports page.
        // If access was denied, the Filter already sent a 403 Forbidden.
        
        request.getRequestDispatcher("reports.jsp").forward(request, response);
    }
}
