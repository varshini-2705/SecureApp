package com.app.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/admin/sonarqube-analysis-page")
public class SonarQubePageServlet extends HttpServlet {

    private String projectsDirectoryPath;

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // Use the variable read from web.xml
        File projectsDir = new File(this.projectsDirectoryPath);
        List<File> projectList = new ArrayList<>();

        if (projectsDir.exists() && projectsDir.isDirectory()) {
            File[] directories = projectsDir.listFiles(File::isDirectory);
            if (directories != null) {
                for (File dir : directories) {
                    projectList.add(dir);
                }
            }
        }
        
        request.setAttribute("projects", projectList);
        request.getRequestDispatcher("/sonarqube-analysis.jsp").forward(request, response);
    }
}
