package com.app.servlet;

import com.app.util.FileUtil; 
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/admin-file-manager") 
public class AdminFileManagerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        
        String userRole = (String) request.getSession().getAttribute("role");
        
      
        if (userRole != null) {
             request.setAttribute("role", userRole);
        }
        

        String applicationPath = request.getServletContext().getRealPath("/");
        
        List<File> uploadedFiles = FileUtil.listUploadedFiles(applicationPath)
                                         .collect(Collectors.toList());

        request.setAttribute("files", uploadedFiles);
        
        request.setAttribute("success", request.getParameter("success"));
        request.setAttribute("error", request.getParameter("error"));

        request.getRequestDispatcher("/admin_file_manager.jsp").forward(request, response);
    }
}
