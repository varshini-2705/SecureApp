package com.app.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
// You would also need to import a ReportData class here

@WebServlet("/sensitiveReports")
public class SensitiveReportsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        // 1. Security Check (The ABACFilter should have already handled access control. 
        // If we reach here, the user is authorized.)
        
        // 2. Fetch the sensitive data (Placeholder logic)
        List<String> sensitiveReports = fetchReportsFromDatabase(); // Implement this logic
        
        // 3. Set data as a request attribute
        request.setAttribute("reports", sensitiveReports);

        // 4. Forward to the reports JSP for display
        request.getRequestDispatcher("/reports.jsp").forward(request, response);
    }
    
    /**
     * Placeholder method for actual database report fetching.
     */
    private List<String> fetchReportsFromDatabase() {
        // In a real application, you would use a ReportDao to fetch data via JDBC
        List<String> reports = new ArrayList<>();
        reports.add("Report A: High-Value Financial Data");
        reports.add("Report B: Executive Payroll Details");
        reports.add("Report C: Server Access Logs");
        return reports;
    }
}
