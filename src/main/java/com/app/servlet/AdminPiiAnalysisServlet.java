package com.app.servlet;

import com.app.util.FileUtil;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@WebServlet("/admin/pii-details")
public class AdminPiiAnalysisServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String targetDataFilePath = request.getParameter("dataFilePath");
        String targetDataFileName = request.getParameter("dataFileName");

        if (targetDataFilePath == null || targetDataFilePath.trim().isEmpty()) {
            request.setAttribute("executionLog", "ERROR: Data file path not provided.");
            request.setAttribute("piiFileContent", "Please click the 'Run PII Scan' button next to an uploaded file.");
            request.getRequestDispatcher("/pii_details.jsp").forward(request, response);
            return;
        }

        File scriptFile = null;
        try {
            
         
            InputStream scriptStream = getServletContext().getResourceAsStream("/WEB-INF/classes/" + FileUtil.PII_SCANNER_SCRIPT_NAME);

            if (scriptStream == null) {
                throw new IOException("PII Scanner script '" + FileUtil.PII_SCANNER_SCRIPT_NAME + "' not found in application resources (src/main/resources).");
            }

            
            scriptFile = File.createTempFile("pii_scanner_", ".sh");
            
      
            Files.copy(scriptStream, scriptFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            scriptFile.setExecutable(true);

          

            String outputFileName = "found_pii_" + targetDataFileName + ".txt";
            request.setAttribute("fileName", targetDataFileName);

            String executionLog = FileUtil.executeShellPiiScan(scriptFile, targetDataFilePath, outputFileName);
            request.setAttribute("executionLog", executionLog);

            String piiFileContent = FileUtil.readPiiOutputFile(outputFileName);
            request.setAttribute("piiFileContent", piiFileContent);

        } catch (Exception e) {
            request.setAttribute("executionLog", "Execution Error: " + e.getMessage());
            request.setAttribute("piiFileContent", "Scan failed due to an exception.");
            e.printStackTrace();
        } finally {
       
            if (scriptFile != null) {
                scriptFile.delete();
            }
        }

        request.getRequestDispatcher("/pii_details.jsp").forward(request, response);
    }
}
