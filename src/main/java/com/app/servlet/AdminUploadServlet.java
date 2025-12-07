package com.app.servlet; 

import com.app.util.FileUtil; 
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2,
                 maxFileSize = 1024 * 1024 * 10,
                 maxRequestSize = 1024 * 1024 * 50)
@WebServlet("/admin/upload")
public class AdminUploadServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
     
        String userRole = (String) request.getSession().getAttribute("role");
        
        
        String normalizedRole = (userRole != null) ? userRole.toLowerCase() : null;

       
        if (normalizedRole == null || !("admin".equals(normalizedRole) || "developer".equals(normalizedRole))) {
            String message = "Authorization Failed: You do not have permission to upload files.";
            String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8.toString());
            String redirectUrl = request.getContextPath() + "/admin-file-manager?error=" + encodedMessage;
            response.sendRedirect(redirectUrl);
            return; 
        }

        String uploadType = request.getParameter("upload_type"); 
        String message = "";
        boolean isError = false;
        
        try {
            String applicationPath = request.getServletContext().getRealPath("/");
            Path uploadPath = FileUtil.getBaseUploadPath(applicationPath);
            
            Part filePart = request.getPart("file_to_upload");
            String fileName = filePart.getSubmittedFileName(); 
            
            if (fileName == null || fileName.isEmpty()) {
                throw new IllegalArgumentException("No file selected.");
            }

         
            if ("java_zip".equals(uploadType) && !(fileName.toLowerCase().endsWith(".java") || fileName.toLowerCase().endsWith(".zip"))) {
                throw new IllegalArgumentException("File must be .java or .zip for Source/ZIP upload.");
            } else if ("compiled_class".equals(uploadType) && !fileName.toLowerCase().endsWith(".class")) {
                throw new IllegalArgumentException("File must be .class for Compiled Class upload.");
            }

           
            Path filePath = uploadPath.resolve(fileName);
            int counter = 1;
            String baseName = fileName.substring(0, fileName.lastIndexOf('.'));
            String extension = fileName.substring(fileName.lastIndexOf('.'));
            while (Files.exists(filePath)) {
                fileName = baseName + "_" + counter + extension;
                filePath = uploadPath.resolve(fileName);
                counter++;
            }

            // Save the file to disk
            filePart.write(filePath.toString());
            message = "File uploaded successfully: " + fileName;
            
        } catch (Exception e) {
            message = "Upload failed: " + e.getMessage();
            isError = true;
            System.err.println(message);
        }
        
        String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8.toString());
        String redirectUrl = request.getContextPath() + "/admin-file-manager?" + (isError ? "error" : "success") + "=" + encodedMessage;
        response.sendRedirect(redirectUrl);
    }
}
