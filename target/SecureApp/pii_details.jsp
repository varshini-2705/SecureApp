<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>PII Analysis Details: ${fileName}</title>
    <style>
        body { font-family: sans-serif; background-color: #e9ecef; padding: 20px; }
        .container { background-color: white; padding: 30px; border-radius: 10px;
        box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); max-width: 900px; margin: 0 auto; }
        h1 { color: #dc3545; border-bottom: 2px solid #dc3545; padding-bottom: 10px; margin-top: 0; }
        h2 { color: #007bff; margin-top: 25px; border-bottom: 1px dashed #ccc; padding-bottom: 5px; }
        pre { background: #f8f9fa; padding: 15px; border-radius: 5px; border: 1px solid #dee2e6; overflow-x: auto; white-space: pre-wrap; word-wrap: break-word; }
        .back-link { display: inline-block; margin-top: 20px; color: #6c757d; text-decoration: none;}
        .back-link:hover { text-decoration: underline;}
    </style>
</head>
<body>
    <div class="container">
        <h1>PII Analysis for: ${fileName}</h1>

        <h2>Execution Result Summary</h2>
       

        <h2>Content of found_pii.txt (Saved PII Data)</h2>
        <pre>${piiFileContent}</pre>

        <a class="back-link" href="<c:url value='/admin-file-manager'/>">&larr; Go back to File Manager</a>
    </div>
</body>
</html>
