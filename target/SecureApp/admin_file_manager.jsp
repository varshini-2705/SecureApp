<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title> File Management Console</title>
    <style>
        body { font-family: sans-serif; background-color: #f4f7f6; padding: 20px; }
        .container { max-width: 900px; margin: 0 auto; background: white; padding: 30px; border-radius: 10px; box-shadow: 0 5px 15px rgba(0,0,0,0.1); }
        h1 { color: #343a40; border-bottom: 3px solid #007bff; padding-bottom: 10px; }
        .feature-nav a { display: inline-block; margin-right: 15px; text-decoration: none; color: #007bff; font-weight: bold; }
        .upload-card { border: 1px solid #ced4da; padding: 20px; border-radius: 5px; margin-bottom: 20px; background-color: #f8f9fa; }
        .upload-card button { background-color: #007bff; color: white; border: none; padding: 8px 15px; border-radius: 4px; cursor: pointer; transition: background-color 0.3s; }
        .upload-card button:hover { background-color: #0056b3; }
        .file-list { list-style: none; padding: 0; }
        .file-list li { padding: 12px; border-bottom: 1px solid #eee; display: flex; justify-content: space-between; align-items: center; background: #fff; margin-bottom: 4px; border-radius: 3px; }
        .file-list li .actions { display: flex; gap: 10px; }
        .file-list li a { color: #28a745; text-decoration: none; font-weight: bold; }
        
        .pii-button {
            padding: 8px 15px;
            background-color: #ffc107;
            color: #343a40;
            border: none;
            border-radius: 4px;
            text-decoration: none;
            font-weight: bold;
        }
        .pii-button:hover {
            background-color: #e0a800;
        }

        .message.success { color: #155724; background-color: #d4edda; border: 1px solid #c3e6cb; padding: 10px; margin-bottom: 15px; border-radius: 4px; }
        .message.error { color: #721c24; background-color: #f8d7da; border: 1px solid #f5c6cb; padding: 10px; margin-bottom: 15px; border-radius: 4px; }

        .analysis-section {
            margin-top: 40px;
            padding-top: 20px;
            border-top: 2px solid #eee;
            text-align: center;
        }
        .sonarqube-button {
            display: inline-block;
            padding: 12px 25px;
            background-color: #007bff;
            color: white;
            border-radius: 5px;
            text-decoration: none;
            font-weight: bold;
            font-size: 16px;
            transition: background-color 0.3s;
        }
        .sonarqube-button:hover {
            background-color: #0056b3;
            color: white;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>File Management Console</h1>
        <div class="feature-nav">
            <a href="admin">&larr; Back to User List</a> | 
            <a href="logout">Logout</a>
        </div>
        <hr/>

        <%-- Display status messages --%>
        <c:if test="${not empty success}">
            <div class="message success">Upload Successful: ${success}</div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="message error">Upload Error: ${error}</div>
        </c:if>
        
        <c:if test="${role eq 'admin' or role eq 'developer'}">
        <h2>File Uploads</h2>
        
        <div class="upload-card">
            <h3>1. Upload Java Source (.java) or Archive (.zip)</h3>
            <form action="admin/upload" method="post" enctype="multipart/form-data">
                <input type="hidden" name="upload_type" value="java_zip">
                <input type="file" name="file_to_upload" accept=".java,.zip" required>
                <button type="submit">Upload Source/Archive</button>
            </form>
        </div>

        <div class="upload-card" style="border-color: #28a745;">
            <h3>2. Upload Compiled Class (.class)</h3>
            <form action="admin/upload" method="post" enctype="multipart/form-data">
                <input type="hidden" name="upload_type" value="compiled_class">
                <input type="file" name="file_to_upload" accept=".class" required>
                <button type="submit" style="background-color: #28a745;">Upload Class File</button>
            </form>
        </div>
        </c:if>
        
        <h2>Uploaded Files</h2>
        
        <c:choose><c:when test="${not empty files}">
            <ul class="file-list">
                <c:forEach var="file" items="${files}">
                    <li>
                        <span>${file.name} (Size: ${(file.length() / 1024.0)} KB)</span>
                        <div class="actions">
                            <a href="admin/details?name=${file.name}">View Details & Analysis &rarr;</a>
                            
                            <%-- PII Scan Button for non-compiled files --%>
                            <c:set var="isCompiled" value="${file.name.toLowerCase().endsWith('.class') || file.name.toLowerCase().endsWith('.jar')}" />
                            
                            <c:if test="${!isCompiled}">
                                <a href="<c:url value='admin/pii-details'>
                                        <c:param name='dataFileName' value='${file.name}'/>
                                        <c:param name='dataFilePath' value='${file.absolutePath}'/>
                                       </c:url>" class="pii-button">Run PII Scan</a>
                            </c:if>
                        </div>
                    </li>
                </c:forEach>
            </ul>
        </c:when><c:otherwise>
            <p>No files have been uploaded yet.</p>
        </c:otherwise></c:choose>

        
        <div class="analysis-section">
            <h2>Static Code Analysis</h2>
            <p>Click the button below to proceed to the SonarQube analysis page.</p>
            <a href="<c:url value='/admin/sonarqube-analysis-page'/>" class="sonarqube-button">Perform SonarQube Testing</a>
        </div>
        
    </div>
</body>
</html>
