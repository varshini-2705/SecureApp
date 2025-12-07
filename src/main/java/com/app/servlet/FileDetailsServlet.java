package com.app.servlet;

import com.app.util.FileUtil; 
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;

@WebServlet("/admin/details") 
public class FileDetailsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fileName = request.getParameter("name");
        String applicationPath = request.getServletContext().getRealPath("/");
        
        String analysisHtml = "<p>No analysis performed or file not found.</p>";
        String fileType = "Unknown";
        File file = null;

        if (fileName != null && !fileName.isEmpty()) {
            try {
                Path uploadPath = FileUtil.getBaseUploadPath(applicationPath);
                file = uploadPath.resolve(fileName).toFile();
                
                if (file.exists()) {
                    // Determine file type based on extension
                    if (fileName.toLowerCase().endsWith(".java")) {
                        fileType = "Java Source (.java)";
                        analysisHtml = FileUtil.analyzeJavaFile(file); 
                        
                    } else if (fileName.toLowerCase().endsWith(".class")) {
                        fileType = "Java Class (.class)";
                        analysisHtml = "<p>Static analysis for compiled class files is not supported in this demo.</p>";
                    } else if (fileName.toLowerCase().endsWith(".zip")) {
                        fileType = "Archive (.zip)";
                        analysisHtml = "<p>Analysis for ZIP archives is not supported.</p>";
                    }
                }
            } catch (Exception e) {
                analysisHtml = "<p style='color:red;'>Error during file processing: " + e.getMessage() + "</p>";
                e.printStackTrace();
            }
        }

        request.setAttribute("fileName", fileName);
        request.setAttribute("fileType", fileType);
        request.setAttribute("analysisHtml", analysisHtml);
        request.setAttribute("file", file); // Pass the File object for size/existence checks in JSP

        request.getRequestDispatcher("/file_details.jsp").forward(request, response);
    }
}
