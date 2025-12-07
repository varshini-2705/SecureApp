<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SonarQube Scan Results</title>
    <style>
        body { 
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif; 
            background-color: #f4f7f6; 
            padding: 20px; 
            color: #333;
        }
        .container { 
            max-width: 1000px; 
            margin: 0 auto; 
            background: white; 
            padding: 30px; 
            border-radius: 10px; 
            box-shadow: 0 5px 15px rgba(0,0,0,0.08); 
        }
        h1 { 
            color: #343a40; 
            border-bottom: 3px solid #007bff; 
            padding-bottom: 10px; 
        }
        .back-link { 
            display: inline-block; 
            margin-bottom: 20px;
            text-decoration: none; 
            color: #007bff; 
            font-weight: bold; 
        }
        .logs-container {
            background-color: #2d2d2d; /* Dark background */
            color: #f1f1f1; /* Light text */
            padding: 20px;
            border-radius: 5px;
            font-family: "Courier New", Courier, monospace;
            font-size: 14px;
            white-space: pre-wrap; /* Allows text to wrap */
            word-wrap: break-word; /* Breaks long words */
            max-height: 600px;
            overflow-y: auto; /* Adds a scrollbar if content is too long */
        }
    </style>
</head>
<body>
    <div class="container">
        <a href="<c:url value='/admin/sonarqube-analysis-page'/>" class="back-link">&larr; Back to Analysis Page</a>
        
        <h1>Scan Results for: <strong><c:out value="${projectName}"/></strong></h1>

        <div class="logs-container">
            <c:out value="${scanLogs}"/>
        </div>
    </div>
</body>
</html>
