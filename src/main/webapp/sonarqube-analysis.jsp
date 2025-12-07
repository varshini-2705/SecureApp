<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SonarQube Static Analysis</title>
    <style>
        body { 
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif; 
            background-color: #f4f7f6; 
            padding: 20px; 
            color: #333;
        }
        .container { 
            max-width: 900px; 
            margin: 0 auto; 
            background: white; 
            padding: 30px; 
            border-radius: 10px; 
            box-shadow: 0 5px 15px rgba(0,0,0,0.08); 
        }
        h1, h2 { 
            color: #343a40; 
            border-bottom: 3px solid #007bff; 
            padding-bottom: 10px; 
            margin-top: 0;
        }
        h2 {
            border-bottom: 2px solid #eee;
            margin-top: 30px;
        }
        .feature-nav a { 
            display: inline-block; 
            margin-right: 15px; 
            text-decoration: none; 
            color: #007bff; 
            font-weight: bold; 
        }
        .upload-card { 
            border: 1px solid #ced4da; 
            padding: 25px; 
            border-radius: 8px; 
            margin-bottom: 20px; 
            background-color: #f8f9fa; 
        }
        .upload-card button { 
            background-color: #007bff; 
            color: white; 
            border: none; 
            padding: 10px 18px; 
            border-radius: 5px; 
            cursor: pointer; 
            font-size: 16px;
            transition: background-color 0.3s;
        }
        .upload-card button:hover { 
            background-color: #0056b3; 
        }
        .project-list { 
            list-style: none; 
            padding: 0; 
        }
        .project-list li { 
            padding: 15px; 
            border-bottom: 1px solid #eee; 
            display: flex; 
            justify-content: space-between; 
            align-items: center; 
            background: #fff; 
            margin-bottom: 5px; 
            border-radius: 4px;
            font-size: 1.1em;
        }
        .run-button {
            padding: 8px 15px;
            background-color: #28a745; /* Green */
            color: white;
            border: none;
            border-radius: 4px;
            text-decoration: none;
            font-weight: bold;
            transition: background-color 0.3s;
        }
        .run-button:hover {
            background-color: #218838; /* Darker Green */
            color: white;
        }
        .results-section {
            margin-top: 40px;
            padding: 25px;
            background-color: #e9f7ef;
            border-left: 5px solid #28a745;
            border-radius: 5px;
        }
        .results-section h2 {
            border-color: #28a745;
        }
        .results-section a {
             display: inline-block;
             margin-top: 15px;
             font-weight: bold;
             color: #007bff;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>SonarQube Static Analysis</h1>
        <div class="feature-nav">
            <a href="<c:url value='/admin-file-manager'/>">&larr; Back to File Management</a>
        </div>
        <hr/>

        <c:if test="${not empty successMessage}">
            <div class="message success">${successMessage}</div>
        </c:if>
        <c:if test="${not empty errorMessage}">
            <div class="message error">${errorMessage}</div>
        </c:if>

        <h2>1. Upload Project for Analysis</h2>
        <div class="upload-card">
            <form action="<c:url value='/admin/sonarqube-upload'/>" method="post" enctype="multipart/form-data">
                <h4>Upload Project Archive (.zip)</h4>
                <p>This is the recommended method. The server will unzip the file.</p>
                <input type="file" name="zip_file_to_upload" accept=".zip" required>
                <br/><br/>
                <button type="submit">Upload Project</button>
            </form>
        </div>

        <h2>2. Available Projects</h2>
        <c:choose>
            <c:when test="${not empty projects}">
                <ul class="project-list">
                    <%-- 
                        This loop iterates over a list of project directories.
                        Your backend servlet should provide this 'projects' list.
                    --%>
                    <c:forEach var="project" items="${projects}">
                        <li>
                            <span>${project.name}</span>
                            <div class="actions">
                                <a href="<c:url value='/admin/run-sonarqube-scan'>
                                        <c:param name='projectDir' value='${project.name}'/>
                                     </c:url>" class="run-button">Run SonarQube Analysis</a>
                            </div>
                        </li>
                    </c:forEach>
                </ul>
            </c:when>
            <c:otherwise>
                <p>No projects have been uploaded for analysis yet.</p>
            </c:otherwise>
        </c:choose>

        <%-- 
            This section will only be displayed if your backend servlet
            completes an analysis and forwards a request with an 'analysisResult' object.
        --%>
        <c:if test="${not empty analysisResult}">
            <div class="results-section">
                <h2>3. Analysis Result for: ${analysisResult.projectName}</h2>
                <div class="result-box">
                    <p><strong>Status:</strong> <span style="color: green; font-weight: bold;">${analysisResult.status}</span></p>
                    <hr>
                    <h3>Key Metrics:</h3>
                    <ul>
                        <li><strong>Bugs:</strong> ${analysisResult.bugs}</li>
                        <li><strong>Vulnerabilities:</strong> ${analysisResult.vulnerabilities}</li>
                        <li><strong>Code Smells:</strong> ${analysisResult.codeSmells}</li>
                    </ul>
                    <a href="${analysisResult.dashboardUrl}" target="_blank">View Full Report on SonarQube &rarr;</a>
                </div>
            </div>
        </c:if>

    </div>
</body>
</html>
