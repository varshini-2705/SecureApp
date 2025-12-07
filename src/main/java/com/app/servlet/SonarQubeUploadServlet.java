package com.app.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@WebServlet("/admin/sonarqube-upload")
@MultipartConfig
public class SonarQubeUploadServlet extends HttpServlet {

    // 1. Declare an instance variable to hold the path
    private String projectsDirectoryPath;

    /**
     * This init method runs once when the servlet is first loaded.
     * It reads the configuration from the web.xml file.
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        
        // Read the parameter from the Servlet Context (defined in web.xml)
        this.projectsDirectoryPath = getServletContext().getInitParameter("sonar.projects.directory");

        
        if (this.projectsDirectoryPath == null) {
            throw new ServletException("Required context parameter 'sonar.projects.directory' is not set in web.xml!");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        Part filePart = request.getPart("zip_file_to_upload");
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

        if (fileName == null || fileName.isEmpty() || !fileName.toLowerCase().endsWith(".zip")) {
            response.sendRedirect("sonarqube-analysis-page?error=Invalid file type. Please upload a .zip file.");
            return;
        }

        // 3. Use the variable read from web.xml
        File projectsDir = new File(this.projectsDirectoryPath);
        if (!projectsDir.exists()) {
            projectsDir.mkdirs();
        }

        File zipFile = new File(projectsDir, fileName);

        try (var inputStream = filePart.getInputStream();
             var outputStream = new FileOutputStream(zipFile)) {
            inputStream.transferTo(outputStream);
        }

        String destDirName = fileName.substring(0, fileName.lastIndexOf('.'));
        File destDir = new File(projectsDir, destDirName);
        unzip(zipFile, destDir);
        
        zipFile.delete();

        response.sendRedirect(request.getContextPath() + "/admin/sonarqube-analysis-page");
    }

    private void unzip(File zipFile, File destDir) throws IOException {
        byte[] buffer = new byte[1024];
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                File newFile = new File(destDir, zipEntry.getName());
                // Fix for Zip Slip vulnerability
                if (!newFile.getCanonicalPath().startsWith(destDir.getCanonicalPath() + File.separator)) {
                    throw new IOException("Zip entry is outside of the target dir: " + zipEntry.getName());
                }

                if (zipEntry.isDirectory()) {
                    if (!newFile.isDirectory() && !newFile.mkdirs()) {
                        throw new IOException("Failed to create directory " + newFile);
                    }
                } else {
                    File parent = newFile.getParentFile();
                    if (!parent.isDirectory() && !parent.mkdirs()) {
                        throw new IOException("Failed to create directory " + parent);
                    }
                    
                    try (FileOutputStream fos = new FileOutputStream(newFile)) {
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                }
                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
        }
    }
}
