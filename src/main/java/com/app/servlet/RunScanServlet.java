package com.web.servlets;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

@WebServlet("/admin/run-sonarqube-scan")
public class RunScanServlet extends HttpServlet {

    // Declare instance variables to hold the paths
    private String projectsDirectoryPath;
    private String sonarScannerExecutablePath;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        
        // Read parameters from the Servlet Context (defined in web.xml)
        this.projectsDirectoryPath = getServletContext().getInitParameter("sonar.projects.directory");
        this.sonarScannerExecutablePath = getServletContext().getInitParameter("sonar.scanner.executable");

        if (this.projectsDirectoryPath == null || this.sonarScannerExecutablePath == null) {
            throw new ServletException("Required context parameters for SonarQube are not set in web.xml!");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String projectDirName = request.getParameter("projectDir");

        if (projectDirName == null || projectDirName.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Project directory not specified.");
            return;
        }

        // Use the variables read from web.xml
        File projectDirectory = new File(this.projectsDirectoryPath, projectDirName);
        if (!projectDirectory.exists() || !projectDirectory.isDirectory()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Project directory does not exist: " + projectDirectory.getAbsolutePath());
            return;
        }

        StringBuilder scanLogs = new StringBuilder();
        try {
            // Use the variable for the scanner executable
            ProcessBuilder processBuilder = new ProcessBuilder(this.sonarScannerExecutablePath);
            processBuilder.directory(projectDirectory);
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    scanLogs.append(line).append("\n");
                }
            }

            boolean finished = process.waitFor(5, TimeUnit.MINUTES);
            if (!finished) {
                process.destroy();
                scanLogs.append("\n\nERROR: Analysis timed out after 5 minutes.");
            } else {
                scanLogs.append("\n\n--- Analysis Complete ---");
            }
        } catch (IOException | InterruptedException e) {
            scanLogs.append("\n\nERROR: Failed to run SonarQube analysis.\n");
            scanLogs.append(e.getMessage());
            Thread.currentThread().interrupt();
        }

        request.setAttribute("projectName", projectDirName);
        request.setAttribute("scanLogs", scanLogs.toString());
        
        request.getRequestDispatcher("/scan-results.jsp").forward(request, response);
    }
}
